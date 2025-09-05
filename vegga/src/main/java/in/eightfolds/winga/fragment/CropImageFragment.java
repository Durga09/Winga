package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.canhub.cropper.CropImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import in.eightfolds.commons.AnimateFirstDisplayListener;
import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.winga.R;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;

public class CropImageFragment extends Fragment {

//    public CropImageView imageView;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Object fileId;
    ImageView star;
    boolean isBitMap;
    
    
    private Activity myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.crop_image_item, container,
                false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fileId = bundle.getString(Constants.DATA);
            isBitMap = bundle.getBoolean(Constants.IS_LOCAL_IMAGE);
        }

//        imageView =  rootView.findViewById(R.id.imageView);
//        this.options = EightfoldsImage.getInstance().getDisplayImageOption(myContext, R.drawable.ic_launcher, null, null);
        if (isBitMap && fileId != null) {
            byte[] decodedString = android.util.Base64.decode(fileId.toString(), android.util.Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedByte != null) {


//                imageView.setImageBitmap(decodedByte);
            }

        } else {
            if (fileId != null) {
                Bitmap bitmap = getBitmap((String) fileId);
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(fileId.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }

                if (bitmap != null) {
//                    imageView.setImageBitmap(rotatedBitmap);
                }
            }
        }
        return rootView;

    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.e("IMAGE URL ", "URL IS " + fileId);
    }


   /* public CropImageView getImageView() {
        return imageView;
    }

    public void setImageView(CropImageView imageView) {
        this.imageView = imageView;
    }*/
}
