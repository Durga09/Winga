package in.eightfolds.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.EightfoldsImageListener;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;


/**
 * Created by Sanjay on 11/3/2015.
 */
public class EightfoldsImage {

    private static final String TAG = "EightfoldsImage";
    private static EightfoldsImage image;
    public static String IMAGE_PATH = "IMAGE_PATH";
    public static int SELECT_FILE = 1001;
    public static int TAKE_PICTURE = 1002;
    public static final int REQUEST_EXTERNAL_STORAGE_FOR_SHARE = 1;
    public static final int REQUEST_EXTERNAL_STORAGE_FOR_UPDATE_PROFILE_PIC = 2;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };

    public static EightfoldsImage getInstance() {
        if (image == null) {
            image = new EightfoldsImage();
        }
        return image;
    }

    public Bitmap getBitmap(String imageFilePath, int width, int height)
            throws IOException {
        File imgFile = new File(imageFilePath);
        if (imgFile.exists()) {
            Bitmap bitmap = decodeSampledBitmapFromFile(imageFilePath, width,
                    height);
            if (bitmap == null) {
                return null;
            }
            return rotateImage(imageFilePath, bitmap);

        }
        return null;
    }

    public Bitmap decodeSampledBitmapFromFile(String pathName,
                                              int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap rotateImageIfRequired(String imagePath,
                                               Context activityContext) {
        System.gc();
        int degrees = 0;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
                default:
                    // no rotation required
                    return null;
            }
        } catch (IOException e) {
            Logg.e(TAG, "Error in reading Exif data of " + imagePath, e);
        }

        BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
        decodeBounds.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
        int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
        int maxPixels = 2048 * 1536; // requires 12 MB heap

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;

        bitmap = BitmapFactory.decodeFile(imagePath, options);

        if (bitmap == null) {
            Toast.makeText(activityContext, "Could not rotate " + imagePath,
                    Toast.LENGTH_LONG).show();
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    public Bitmap rotateImage(String _path, Bitmap bitmap)
            throws IOException {
        // _path = path to the image to be OCRed
        ExifInterface exif = new ExifInterface(_path);
        int exifOrientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        if (rotate != 0) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
        }
        return bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    /**
     * Compress image as per the quality provided
     *
     * @param context   Activity context
     * @param imagePath
     * @param quality
     */
    public void compressAndSaveImage(Context context, String imagePath, int quality) {
        Bitmap bitmap = rotateImageIfRequired(imagePath, context);

        if (bitmap == null) {
            BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
            decodeBounds.inJustDecodeBounds = true;

            // bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
            int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
            int maxPixels = 2048 * 1536; // requires 12 MB heap

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        }

        try {
            FileOutputStream out;
            out = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            Logg.v(TAG, "Could not compress " + imagePath + ": " + e.getMessage());
            MyDialog.showToast(context, context.getString(R.string.select_another_pic));
        }
    }

    /**
     * @param context Activity context
     */
    public String captureImageFromSdCard(Context context) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        if ((context instanceof Activity)) {
//            ((Activity) context).startActivityForResult(intent, SELECT_FILE);
//        }
        if (context instanceof Activity && !verifyStoragePermissions((Activity) context)) {
            return null;
        }
        String mediaPath = Environment.getExternalStorageDirectory() + "/"
                + context.getResources().getString(R.string.app_name) + "/img"
                + System.currentTimeMillis() + ".jpg";

        EightfoldsUtils.getInstance().saveToSharedPreference(context, IMAGE_PATH, mediaPath);


        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");


        File imageFile = new File(mediaPath);
        File imageDir = new File(imageFile.getParent());
        if (!imageDir.exists()) {
            Logg.i(TAG, "Creating directory: " + imageFile.getParent());


            imageDir.mkdirs();
        }


        Logg.d(TAG, "Picking compressed image as: " + mediaPath);


        ((Activity) context).startActivityForResult(
                Intent.createChooser(intent, "Pick image from"), SELECT_FILE);

        return mediaPath;

    }

    /**
     * @param context Activity context
     */
    public String captureImageFromCamera(Context context) {
        if (context instanceof Activity && !verifyStoragePermissions((Activity) context)) {
            return null;
        }
        EightfoldsUtils.getInstance().saveToSharedPreference(context, IMAGE_PATH, null);
        if (!EightfoldsUtils.getInstance().isSDCardValid(context, true)) {
            return "";
        }

        String imageFilePath = Environment.getExternalStorageDirectory() + "/"
                + context.getResources().getString(R.string.app_name) + "/img"
                + System.currentTimeMillis() + ".jpg";

        EightfoldsUtils.getInstance().saveToSharedPreference(context, IMAGE_PATH, imageFilePath);

        File imageFile = new File(imageFilePath);
        Uri outputFileUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);
        } else {
            outputFileUri = Uri.fromFile(imageFile);
        }
        File imageDir = new File(imageFile.getParent());
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        if ((context instanceof Activity)) {
            ((Activity) context).startActivityForResult(intent, TAKE_PICTURE);
        }

        return imageFilePath;

    }


    public DisplayImageOptions getDisplayImageOption(Context context, int defaultImage) {
        return getDisplayImageOption(context, defaultImage, null, null);
    }

    public DisplayImageOptions getDisplayImageOption(
            final Context context, int defaultImage, final String userName, final String password) {
        if (userName != null) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password.toCharArray());
                }
            });
        }
        return new DisplayImageOptions.Builder().showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnFail(defaultImage).cacheInMemory(true).cacheOnDisk(true)
                .build();
    }


    /**
     * Always call this method in Application class
     *
     * @param context application context
     */
    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    /**
     * @param activity
     * @param eightfoldsImageListener
     * @param mediaPath
     * @param requestCode
     * @param data
     */
    public void saveOnActivityResult(Activity activity,
                                     EightfoldsImageListener eightfoldsImageListener, String mediaPath, int requestCode,
                                     Intent data) {
        String selectedImagePath = null;
        boolean isSavedInMediaPath = false;
        if (TextUtils.isEmpty(mediaPath)) {
            mediaPath = EightfoldsUtils.getInstance().getFromSharedPreference(activity,
                    IMAGE_PATH);
        }

        if (requestCode == TAKE_PICTURE) {
            selectedImagePath = mediaPath;
        } else if (requestCode == SELECT_FILE) {
            Uri imageUri = data.getData();


            String[] projection = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor cur = activity.managedQuery(imageUri,
                    projection, null, null, null);
            cur.moveToFirst();
            selectedImagePath = cur.getString(cur
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            if (TextUtils.isEmpty(selectedImagePath)) {
                if (imageUri != null && imageUri.toString().startsWith("content://com.google.android.apps.photos.content")) {
                    try {
                        InputStream is = activity.getContentResolver().openInputStream(imageUri);
                        if (is != null) {
                            Logg.v(TAG, "**Image is downloaded image");
                            Bitmap downloadedPictureBitmap = BitmapFactory.decodeStream(is);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                                try (FileOutputStream out = new FileOutputStream(mediaPath)) {
                                    downloadedPictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                    selectedImagePath = mediaPath;
                                    isSavedInMediaPath = true ;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        if (TextUtils.isEmpty(selectedImagePath)) {
            Toast.makeText(
                    activity,
                    "Please pick another image, could not get the local media path.",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            if (requestCode == EightfoldsImage.SELECT_FILE) {
                if(!isSavedInMediaPath) {
                    EightfoldsUtils.getInstance().copyFile(activity,
                            selectedImagePath, mediaPath);
                }
            }
            EightfoldsImage.getInstance().compressAndSaveImage(activity,
                    mediaPath, 60);
            eightfoldsImageListener.onEvent(requestCode, mediaPath);
        }

    }

    /**
     * Set image as wallpaper.
     *
     * @param context
     * @param imageUri
     * @param title
     */
    public void setImageAsWallpaper(Context context, Uri imageUri, String title) {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(imageUri, "image/jpeg");
        intent.putExtra("mimeType", "image/jpeg");
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * Scale down image to a given size
     *
     * @param realImage    bitmap
     * @param maxImageSize max size of the image
     * @param filter       true
     * @return
     */
    public static Bitmap scaleDownImage(Bitmap realImage, float maxImageSize,
                                        boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE_FOR_UPDATE_PROFILE_PIC
            );
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyCameraStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE_FOR_SHARE
            );
            return false;
        } else {
            return true;
        }
    }


}