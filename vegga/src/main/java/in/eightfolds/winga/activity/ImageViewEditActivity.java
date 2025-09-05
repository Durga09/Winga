package in.eightfolds.winga.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
/*import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;*/
import net.gotev.uploadservice.data.UploadInfo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;*/
import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.rest.UploadFile;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.R;
import in.eightfolds.winga.fragment.ImageFragment;
import in.eightfolds.winga.interfaces.EightfoldsImageListener;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AppFile;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.TouchImageView;
import in.eightfolds.winga.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ImageViewEditActivity extends BaseActivity implements View.OnClickListener, OnEventListener, EightfoldsImageListener, VolleyResultCallBack, BottomSheetImagePicker.OnImagesSelectedListener,EasyPermissions.PermissionCallbacks {

    private static final String TAG = ImageFragment.class.getSimpleName();
    private static final int RC_CAMERA_AND_LOCATION = 123;
    public TouchImageView imageView;
    private Object fileId;
    boolean isLocalImage;
    private User userResponse;
    private Activity myContext;
    private String path;
    private String imageBitmap;
    private String selectedOption = "";
    private Dialog myProgressDialog;
    private ImageView editIv;

    private boolean isProfilePicRemove = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_item);
        myContext = this;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            fileId = bundle.getString(Constants.DATA);
            isLocalImage = bundle.getBoolean(Constants.IS_LOCAL_IMAGE);
        }
        imageView = findViewById(R.id.imageView);
        imageView.setMaxZoom(4f);
        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(myContext, Constants.USER_DETAILS, User.class);


        findViewById(R.id.backIv).setOnClickListener(this);
        // findViewById(R.id.editIv).setOnClickListener(this);
        // findViewById(R.id.editIv).setVisibility(View.VISIBLE);
        findViewById(R.id.headerRightIconIv).setOnClickListener(this);

        setHeaderWithEdit("");

        if (fileId != null) {
            setImageFromFileId(fileId);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Logg.e("IMAGE URL ", "Image ID IS " + fileId);
    }


    public void setImageFromFileId(Object selectedImagePath) {
        String filePath = "";

        if (isLocalImage) {
            filePath = selectedImagePath.toString();
        } else {
            filePath = Constants.FILE_URL + selectedImagePath.toString();
        }
        Glide.with(this)
                .load(filePath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.profle_darkbg)
                .error(R.drawable.profle_darkbg)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
      /*  switch (v.getId()) {
            case R.id.editIv:
                showEditPicDialog();
                break;
            case R.id.headerRightIconIv:
                showEditPicDialog();
                break;
            case R.id.backIv:
                onBackPressed();
                break;
        }*/
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.DATA, userResponse);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }


    private void showEditPicDialog() {
        boolean hasProfilePic = false;
        if (userResponse != null && userResponse.getProfilePicId() != null && userResponse.getProfilePicId().intValue() != 0) {
            hasProfilePic = true;
        }
        MyDialog.uploadPhotosDialog(myContext, this, hasProfilePic);
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        try {
            if (type == R.id.galleryLL) {
                isProfilePicRemove = false;
//                EightfoldsUtils.getInstance().verifyStoragePermissions(this);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Do something after 5s = 5000ms
             /*   Permissions.check(this,new String[] {Manifest.permission.CAMERA}, null,null,new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        methodRequiresTwoPermission();
                    }
                });*/

          //      selectImage();
//                    }
//                }, 1000);
            } else if (type == R.id.cameraLL) {
                isProfilePicRemove = false;
                selectedOption = "camera";
                path = EightfoldsImage.getInstance().captureImageFromCamera(myContext);
            } else if (type == R.id.removePhotoLL) {
                isProfilePicRemove = true;
                userResponse.setProfilePicId((long) 0);
                path = null;
                imageBitmap = null;
                Glide.with(this)
                        .load(R.drawable.profle_darkbg)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.profle_darkbg)
                        .error(R.drawable.profle_darkbg)
                        .into(imageView);
                editProfile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EightfoldsImage.SELECT_FILE) {
                EightfoldsImage.getInstance().saveOnActivityResult(myContext, this, path, EightfoldsImage.SELECT_FILE, data);
            } else if (requestCode == EightfoldsImage.TAKE_PICTURE) {
                EightfoldsImage.getInstance().saveOnActivityResult(myContext, this, path, EightfoldsImage.TAKE_PICTURE, data);
            } else if (requestCode == Constants.CROP) {
                path = data.getStringExtra(Constants.PATH);
                setImage(path);
            }else if (requestCode == 2 || requestCode == 2296) {
                openFilePicker();
            } else {
                if (data != null) {

        /*            switch (requestCode) {
                        case FilePickerConst.REQUEST_CODE_DOC:
                        case FilePickerConst.REQUEST_CODE_PHOTO:
                            ArrayList<Uri> uriArrayList=new ArrayList<>();
                            uriArrayList.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                            try {
                                gotoCropImageActivity(Objects.requireNonNull(ContentUriUtils.INSTANCE.getFilePath(this
                                        , uriArrayList.get(0))));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                    }*/

                }
            }
        }
    }


    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onEvent(int requestType) {

    }

    @Override
    public void onEvent(int requestType, Object object) {
        if (requestType == EightfoldsImage.SELECT_FILE && object != null) {
            gotoCropImageActivity(object);
        } else if (requestType == EightfoldsImage.TAKE_PICTURE && object != null) {
            gotoCropImageActivity(object);
        }
    }

    private void gotoCropImageActivity(Object objectPath) {
        Intent intent = new Intent(myContext, ImageViewActivity.class);
        intent.putExtra(Constants.DATA, objectPath.toString());
        intent.putExtra(Constants.IS_TO_CROP, true);
        myContext.startActivityForResult(intent, Constants.CROP);
    }

    private void setImage(final Object object) {
        try {
            if (!TextUtils.isEmpty(object.toString())) {
                imageBitmap = object.toString();
            }

            if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
                myProgressDialog = MyDialog.showProgress(myContext);
//                UploadFile.uploadMultipartWithOutNotification(myContext, path, this, true, this);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        uploadInfo.getProgressPercent();
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        Logg.i("onCompleted", "onCompleted");

        if (myProgressDialog != null)
            myProgressDialog.dismiss();

        AppFile appFile = null;

        try {
            appFile = (AppFile) Api.fromJson(serverResponse.getBodyAsString(), AppFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onVolleyResultListener(appFile, Constants.UPLOAD_FILE_URL);

        Logg.i("onCompleted", "" + serverResponse.getBodyAsString());
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        Logg.i("onCancelled", "onCancelled");

        if (myProgressDialog != null)
            myProgressDialog.dismiss();

        onVolleyErrorListener("Upload cancelled");
    }
*/
    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof User) {
            userResponse = (User) object;

            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.USER_DETAILS, userResponse);
            Intent homeRefreshIntent = new Intent();
            homeRefreshIntent.putExtra(Constants.DATA, userResponse);
            homeRefreshIntent.setAction(Constants.HOME_REFRESH_ACTION);

            Intent profileRefreshIntent = new Intent();
            profileRefreshIntent.putExtra(Constants.DATA, userResponse);
            profileRefreshIntent.setAction(Constants.PROFILE_REFRESH_ACTION);

            sendBroadcast(homeRefreshIntent);
            sendBroadcast(profileRefreshIntent);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.DATA, userResponse);
            setResult(RESULT_OK, resultIntent);

            if (userResponse.getProfilePicId() != 0) {
                MyDialog.showToast(myContext, getString(R.string.image_changed_successfully));
            } else {
                MyDialog.showToast(myContext, getString(R.string.image_removed_successfully));
            }

            if (isProfilePicRemove && (userResponse.getProfilePicId() == null || userResponse.getProfilePicId() == 0)) {
                isProfilePicRemove = false;
                onBackPressed();
            }

        } else if (requestType.equalsIgnoreCase(Constants.UPLOAD_FILE_URL)) {
            Logg.v(TAG, "**Upload profile pic success");
            AppFile appFile = (AppFile) object;
            userResponse.setProfilePicId(appFile.getFileId());
            Glide.with(this)
                    .load(Constants.FILE_URL + userResponse.getProfilePicId())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.profle_darkbg)
                    .error(R.drawable.profle_darkbg)
                    .into(imageView);

            editProfile();
        }
    }

    private void pickorCaptureImageOnPermissionsGranted() {
        if (selectedOption.equalsIgnoreCase("gallery")) {
            path = EightfoldsImage.getInstance().captureImageFromSdCard(this);
        } else if (selectedOption.equalsIgnoreCase("camera")) {
            path = EightfoldsImage.getInstance().captureImageFromCamera(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(RC_CAMERA_AND_LOCATION!=requestCode){
            int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            if (writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED && cameraPermission == PackageManager.PERMISSION_GRANTED) {
                pickorCaptureImageOnPermissionsGranted();
            }
        }else{
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(myContext, object);
    }

    private void editProfile() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
            EightfoldsVolley.getInstance().showProgress(myContext);
            String url = Constants.EDIT_USER_PROFILE_URL;
            url = url.replace("{langId}", Utils.getCurrentLangId());

            EightfoldsVolley.getInstance().makingJsonRequest(this, User.class, Request.Method.POST, url, userResponse);

        }
    }

    private void selectImage() {
        new BottomSheetImagePicker.Builder(BuildConfig.APPLICATION_ID + ".provider")
                .singleSelectTitle(R.string.pick_single)
                .peekHeight(R.dimen.peekHeight)
                .columnSize(R.dimen.columnSize)
                .show(getSupportFragmentManager(), "Image Picker");
    }

    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @org.jetbrains.annotations.Nullable String s) {
        if (!list.isEmpty()) {
            gotoCropImageActivity(list.get(0).getPath());
        }
    }


  /*  @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {

        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            // Already have permission, do the thing
            // ...
            final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms

                        openFilePicker();
                    }
                }, 1000);

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    RC_CAMERA_AND_LOCATION, FilePickerConst.PERMISSIONS_FILE_PICKER);

        }
    }*/

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private void openFilePicker() {
    /*   try{
           FilePickerBuilder.getInstance()
                   .setMaxCount(1) //optional
                   .setActivityTheme(R.style.LibAppTheme)
                   .enableVideoPicker(false)
                   .enableCameraSupport(true).enableImagePicker(true)
                   .pickPhoto(ImageViewEditActivity.this);
       }catch (Exception e){
           e.printStackTrace();
       }*/

    }
}
