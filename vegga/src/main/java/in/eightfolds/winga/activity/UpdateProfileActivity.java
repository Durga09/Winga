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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
//import com.nabinbhandari.android.permissions.PermissionHandler;
//import com.nabinbhandari.android.permissions.Permissions;

//import net.gotev.uploadservice.ServerResponse;
//import net.gotev.uploadservice.UploadInfo;
//import net.gotev.uploadservice.UploadStatusDelegate;

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
import in.eightfolds.winga.interfaces.EightfoldsImageListener;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AgeRange;
import in.eightfolds.winga.model.AppFile;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class UpdateProfileActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, VolleyResultCallBack, OnEventListener, EightfoldsImageListener, BottomSheetImagePicker.OnImagesSelectedListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = UpdateProfileActivity.class.getSimpleName();
    private User userResponse;
    private EditText firstNameET, emailET, pinCodeET;
    private Spinner ageRangeSpinner, genderSpinner;
    private ArrayList<AgeRange> ageRanges, genders;
    private AgeRange selectedAgeRange, selectedGender;
    private Button saveBtn;
    private ImageView profilePicIV;
    private Button camIV;
    private String path;
    private Dialog myProgressDialog;
    private String selectedOption = "";
    private boolean isProfilePicUpdate = false;
    private boolean isSpinnerTouched = false;

    private static final int RC_CAMERA_AND_LOCATION = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_updateprofile);
        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);

    }

    private void initialize() {
        firstNameET = findViewById(R.id.firstNameET);
        emailET = findViewById(R.id.emailET);
        pinCodeET = findViewById(R.id.pinCodeET);
        ageRangeSpinner = findViewById(R.id.ageRangeSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveBtn = findViewById(R.id.saveBtn);
        profilePicIV = findViewById(R.id.profilePicIV);
        camIV = findViewById(R.id.camIV);
        saveBtn.setOnClickListener(this);
        profilePicIV.setOnClickListener(this);

        ageRangeSpinner.setOnItemSelectedListener(this);
        genderSpinner.setOnItemSelectedListener(this);
        findViewById(R.id.closeIv).setOnClickListener(this);
        findViewById(R.id.ageRangeSpinnerLL).setOnClickListener(this);
        findViewById(R.id.genderSpinnerLL).setOnClickListener(this);
        camIV.setOnClickListener(this);
        setAgeSpinner(null);
        setGenderSpinner(null);

        if (userResponse != null) {

            setCountryCodeValidations();
            firstNameET.setText(!TextUtils.isEmpty(userResponse.getName()) ? userResponse.getName() : "");
            emailET.setText(!TextUtils.isEmpty(userResponse.getEmail()) ? userResponse.getEmail() : "");
            pinCodeET.setText(!TextUtils.isEmpty(userResponse.getPincode()) ? userResponse.getPincode() : "");


            Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);

            if (setup != null && setup.getAgeRanges() != null && setup.getAgeRanges().size() > 0) {
                ageRanges = new ArrayList<>(setup.getAgeRanges());
                setAgeSpinner(ageRanges);
            }

            if (userResponse.getProfilePicId() != null && userResponse.getProfilePicId() != 0) {
                Glide.with(this)
                        .load(Constants.FILE_URL + userResponse.getProfilePicId())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.ic_user_filled)
                        .error(R.drawable.ic_user_filled)
                        .into(profilePicIV);
            }

            genders = Utils.getGendersList(this);
            setGenderSpinner(genders);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isSpinnerTouched = true;
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        handleBack();
    }

    private void handleBack() {
        try {


            super.onBackPressed();
        } catch (Exception ex) {
            super.onBackPressed();
            ex.printStackTrace();
        }
    }

   /* @Override
    public void onClick(View v) {
        super.onClick(v);
        try {
            switch (v.getId()) {
                case R.id.saveBtn:
                    saveDetails();
                    break;
                case R.id.closeIv:
                    handleBack();
                    break;

                case R.id.ageRangeSpinnerLL:
                    ageRangeSpinner.performClick();
                    break;
                case R.id.genderSpinnerLL:
                    genderSpinner.performClick();
                    break;
                case R.id.camIV:
//                    EightfoldsUtils.getInstance().verifyStoragePermissions(this);
//                    final Handler handler = new Handler();
////                    handler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            // Do something after 5s = 5000ms
//                            selectImage();
////                        }
////                    }, 5000);

                    Permissions.check(this,new String[] {Manifest.permission.CAMERA}, null,null,new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            methodRequiresTwoPermission();
                        }
                    });
                    break;
                case R.id.profilePicIV:


                    if (userResponse != null && userResponse.getProfilePicId() != null && userResponse.getProfilePicId().intValue() != 0) {


                        Intent intent = new Intent(this, ImageViewEditActivity.class);
                        intent.putExtra(Constants.DATA, userResponse.getProfilePicId() + "");
                        startActivityForResult(intent, 222);

                    } else {
                        EightfoldsUtils.getInstance().verifyStoragePermissions(this);
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                selectImage();
                            }
                        }, 5000);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    private void showEditPicDialog() {
        boolean hasProfilePic = false;
        if (userResponse != null && userResponse.getProfilePicId() != null && userResponse.getProfilePicId().intValue() != 0) {
            hasProfilePic = true;
        }
        MyDialog.uploadPhotosDialog(this, this, hasProfilePic);
    }

    private void editProfile(boolean isprofile) {
        isProfilePicUpdate = isprofile;
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.EDIT_USER_PROFILE_URL;
            url = url.replace("{langId}", Utils.getCurrentLangId());

            EightfoldsVolley.getInstance().makingJsonRequest(this, User.class, Request.Method.POST, url, userResponse);

        }
    }

    private void saveDetails() {
        String name = firstNameET.getText().toString();
        String email = emailET.getText().toString();
        String pinCode = pinCodeET.getText().toString();
        String countryCode = "";
        if (userResponse.getCountryCode() != null) {
            countryCode = userResponse.getCountryCode();
        }
        if (TextUtils.isEmpty(name)) {
            onVolleyErrorListener(getString(R.string.enter_name));
        } else if (TextUtils.isEmpty(pinCode)) {
            onVolleyErrorListener(getString(R.string.enter_valid_pincode));
        } else if (EightfoldsUtils.isAllZeros(pinCode)) {
            onVolleyErrorListener(getString(R.string.pincode_not_accept_zeros));
        } else if (countryCode.equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE) && (TextUtils.isEmpty(pinCode) || pinCode.length() != 6)) {
            onVolleyErrorListener(getString(R.string.enter_valid_pincode));
        } else if (!countryCode.equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE) && (TextUtils.isEmpty(pinCode) || !EightfoldsUtils.isValidPostalCode(pinCode))) {
            onVolleyErrorListener(getString(R.string.enter_valid_pincode));
        } else if (!EightfoldsUtils.isAlphabetsAndSpaceOnly(name)) {
            onVolleyErrorListener(getString(R.string.no_special_characters_name));
        } else if (!TextUtils.isEmpty(email) && !EightfoldsUtils.getInstance().isValidEmail(email)) {
            onVolleyErrorListener(getString(R.string.enter_valid_email));
        } else if (selectedAgeRange == null) {
            onVolleyErrorListener(getString(R.string.select_age_range));
        } else if (selectedGender == null) {
            onVolleyErrorListener(getString(R.string.select_gender));
        } else {
            userResponse.setName(firstNameET.getText().toString());

            userResponse.setPincode(pinCode);

            if (TextUtils.isEmpty(countryCode)) {
                userResponse.setCountryCode(Constants.DEFAULT_COUNTRYCODE);
            }
            if (!TextUtils.isEmpty(email))
                userResponse.setEmail(email);
            else
                userResponse.setEmail("");

            if (selectedAgeRange != null) {
                userResponse.setAgeRangeId(selectedAgeRange.getAgeRangeId());
            } else {
                // userResponse.setAgeRangeId((long) 0);
            }

            if (selectedGender != null) {
                userResponse.setGender(selectedGender.getAgeRangeId());
            } else {
                //  userResponse.setGender((long) 0);
            }
            EightfoldsUtils.getInstance().hideKeyboard(this);
            editProfile(false);
        }
    }

    public void setAgeSpinner(List<AgeRange> ageRanges) {
        int selection = 0;
        ArrayAdapter<String> ageRangeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        ageRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRangeSpinner.setAdapter(ageRangeAdapter);
        if (ageRanges != null && !ageRanges.isEmpty()) {
            for (int i = 0; i < ageRanges.size(); i++) {
                AgeRange ageRange = ageRanges.get(i);
                ageRangeAdapter.add(ageRange.getTitle());
                if (userResponse != null && userResponse.getAgeRangeId() != null && userResponse.getAgeRangeId().equals(ageRange.getAgeRangeId())) {
                    selection = i;
                    selectedAgeRange = ageRange;
                }
            }
        }
        Logg.v(TAG, "prompt >> " + ageRangeSpinner.getPrompt());
        ageRangeSpinner.setSelection(selection);
        ageRangeSpinner.setPrompt(getString(R.string.age_range));
    }

    public void setGenderSpinner(List<AgeRange> genders) {
        int selection = 0;
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        if (genders != null && !genders.isEmpty()) {
            for (int i = 0; i < genders.size(); i++) {
                AgeRange gender = genders.get(i);
                genderAdapter.add(gender.getTitle());
                if (userResponse != null && userResponse.getGender() != null && userResponse.getGender().equals(gender.getAgeRangeId())) {
                    selection = i;
                    selectedGender = gender;
                }
            }
        }
        Logg.v(TAG, "prompt >> " + ageRangeSpinner.getPrompt());
        genderSpinner.setSelection(selection);
        genderSpinner.setPrompt(getString(R.string.gender));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView.getChildCount() > 0 && adapterView.getChildAt(0) instanceof TextView) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorWhite));
        }
        int i = adapterView.getId();
        if (i == R.id.ageRangeSpinner) {

            if (isSpinnerTouched || (userResponse != null && userResponse.getAgeRangeId() != null)) {
                ((TextView) adapterView.getChildAt(0)).setText(ageRanges.get(position).getTitle());
                selectedAgeRange = ageRanges.get(position);
            } else {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.age_range));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.address_hint_text_color));
                ageRangeSpinner.setSelected(false);
                ageRangeSpinner.setSelection(-1);
            }

        } else if (i == R.id.genderSpinner) {

            if (isSpinnerTouched || (userResponse != null && userResponse.getGender() != null)) {
                ((TextView) adapterView.getChildAt(0)).setText(genders.get(position).getTitle());
                selectedGender = genders.get(position);
            } else {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.gender));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.address_hint_text_color));
                genderSpinner.setSelected(false);
                genderSpinner.setSelection(-1);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof User) {
            userResponse = (User) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, userResponse);

            Intent intent = new Intent();
            intent.putExtra(Constants.DATA, userResponse);
            setResult(RESULT_OK, intent);


            Intent homePageRefreshIntent = new Intent();
            homePageRefreshIntent.setAction(Constants.HOME_REFRESH_ACTION);
            sendBroadcast(homePageRefreshIntent);
            if (!isProfilePicUpdate) {
                finish();
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
                    .into(profilePicIV);

            editProfile(true);
        }


    }

    private void setCountryCodeValidations() {
        if (userResponse.getCountryCode() == null || userResponse.getCountryCode().equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE)) {
            //Pincode validations should be there. 6 digit numeric
            pinCodeET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6), new InputFilter.AllCaps()});
            pinCodeET.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else {

            // 0-15 alpha numeric.

            pinCodeET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15), new InputFilter.AllCaps()});
            pinCodeET.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        try {
            if (type == R.id.galleryLL) {
                path = EightfoldsImage.getInstance().captureImageFromSdCard(this);
                selectedOption = "gallery";
            } else if (type == R.id.cameraLL) {
                selectedOption = "camera";
                path = EightfoldsImage.getInstance().captureImageFromCamera(this);
            } else if (type == R.id.removePhotoLL) {
                userResponse.setProfilePicId((long) 0);
                path = null;
                Glide.with(this)
                        .load(R.drawable.profle_darkbg)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.profle_darkbg)
                        .error(R.drawable.profle_darkbg)
                        .into(profilePicIV);
                editProfile(true);

            } else if (type == R.id.yesTv) {
                saveDetails();
            } else if (type == R.id.noText) {
                super.onBackPressed();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void pickorCaptureImageOnPermissionsGranted() {
        if (selectedOption.equalsIgnoreCase("gallery")) {
            path = EightfoldsImage.getInstance().captureImageFromSdCard(this);
        } else if (selectedOption.equalsIgnoreCase("camera")) {
            path = EightfoldsImage.getInstance().captureImageFromCamera(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EightfoldsImage.SELECT_FILE) {
                EightfoldsImage.getInstance().saveOnActivityResult(this, this, path, EightfoldsImage.SELECT_FILE, data);
            } else if (requestCode == EightfoldsImage.TAKE_PICTURE) {
                EightfoldsImage.getInstance().saveOnActivityResult(this, this, path, EightfoldsImage.TAKE_PICTURE, data);
            } else if (requestCode == Constants.CROP) {
                path = data.getStringExtra(Constants.PATH);
                setImage(path);
            } else if (requestCode == 222) {
                if (data != null) {

                    User edittedUser = (User) data.getExtras().get(Constants.DATA);
                    if (edittedUser != null) {
                        userResponse = edittedUser;
                    }
                    if (edittedUser != null && edittedUser.getProfilePicId() != null && edittedUser.getProfilePicId() != 0) {
                        Glide.with(this)
                                .load(Constants.FILE_URL + userResponse.getProfilePicId())
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .placeholder(R.drawable.profle_darkbg)
                                .error(R.drawable.profle_darkbg)
                                .into(profilePicIV);
                    } else {
                        profilePicIV.setImageResource(R.drawable.profle_darkbg);
                    }
                }
            }else if (requestCode == 2 || requestCode == 2296) {
                openFilePicker();
            } else {
                if (data != null) {

                 /*   switch (requestCode) {
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
    public void onEventListener(int requestType, Object object) {
        if (requestType == EightfoldsImage.SELECT_FILE && object != null) {

            setImage(object);
        } else if (requestType == EightfoldsImage.TAKE_PICTURE && object != null) {
            setImage(object);
        }
    }

    private void setImage(final Object object) {
        try {


            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                myProgressDialog = MyDialog.showProgress(this);
//                UploadFile.uploadMultipartWithOutNotification(this, path, this, true, this);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        Logg.i("onProgress", "onProgress");

        uploadInfo.getProgressPercent();
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
//                                    Logg.i("onError", "onError" + exception.toString());

        onVolleyErrorListener(context.getString(R.string.something_wrong));
        if (myProgressDialog != null)
            myProgressDialog.dismiss();
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
    public void onEvent(int requestType) {

    }

    @Override
    public void onEvent(int requestType, Object object) {
        if (requestType == EightfoldsImage.SELECT_FILE && object != null) {

            //setImage(object);
            gotoCropImageActivity(object);
        } else if (requestType == EightfoldsImage.TAKE_PICTURE && object != null) {
            //setImage(object);
            gotoCropImageActivity(object);
        }
    }

    private void gotoCropImageActivity(Object objectPath) {
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra(Constants.DATA, objectPath.toString());
        intent.putExtra(Constants.IS_TO_CROP, true);
        startActivityForResult(intent, Constants.CROP);
    }
    private  void  selectImage() {
        new BottomSheetImagePicker.Builder(BuildConfig.APPLICATION_ID + ".provider")
                .singleSelectTitle(R.string.pick_single)
                .peekHeight(R.dimen.peekHeight)
                .columnSize(R.dimen.columnSize)
                .show(getSupportFragmentManager(),"Image Picker");
    }
    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @org.jetbrains.annotations.Nullable String s) {
        if(!list.isEmpty()){
            gotoCropImageActivity(list.get(0).getPath());
        }
    }

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
        try{
           /* FilePickerBuilder.getInstance()
                    .setMaxCount(1) //optional
                    .setActivityTheme(R.style.LibAppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(true).enableImagePicker(true)
                    .pickPhoto(this);*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }


   /* @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
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
}
