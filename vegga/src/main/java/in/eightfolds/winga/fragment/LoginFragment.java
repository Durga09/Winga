package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.AdActivity;
import in.eightfolds.winga.activity.FindPhoneNumActivity;
import in.eightfolds.winga.activity.HomeBaseActivity;
import in.eightfolds.winga.activity.V2HomeFeatureActivity;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.activity.RegistrationSuccessActivity;
import in.eightfolds.winga.activity.WingaNotLiveActivity;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.LoginRequest;
import in.eightfolds.winga.v2.model.UserCredentials;

public class LoginFragment extends Fragment implements View.OnClickListener, VolleyResultCallBack, View.OnFocusChangeListener, OnEventListener {

    private Button loginBtn;
    private TextView forgotPasswordTv;
    private boolean hidePassword = true;
    private String mobileNumber;
    private LinearLayout passInnerLL, passwordLL;
    private View passwordView;
    private ImageView passwordIv;
    private EditText passwordET;
    private ImageView iv_showPassword;
    private EditText phoneET;
    private LinearLayout mobInnerLL;
    private ImageView mobIv;
    private View mobView;
    private boolean passWordHighLighted = false;

    private Activity myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
            }
        }
        ((LoginActivity) myContext).setLoginFrameInCenter();
        initialize(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(LoginFragment.class.getSimpleName());
        phoneET.requestFocus();
    }

    private void initialize(View view) {
        loginBtn = view.findViewById(R.id.loginBtn);
        forgotPasswordTv = view.findViewById(R.id.forgotPasswordTv);

        passwordET = view.findViewById(R.id.passwordET);
        passInnerLL = view.findViewById(R.id.passInnerLL);
        passwordLL = view.findViewById(R.id.passwordLL);
        passwordView = view.findViewById(R.id.passwordView);
        passwordIv = view.findViewById(R.id.passwordIv);
        iv_showPassword = view.findViewById(R.id.iv_showPassword);

        phoneET = view.findViewById(R.id.phoneET);
        mobInnerLL = view.findViewById(R.id.mobInnerLL);
        mobIv = view.findViewById(R.id.mobIv);
        mobView = view.findViewById(R.id.mobView);

        forgotPasswordTv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        iv_showPassword.setOnClickListener(this);

        passwordET.setOnFocusChangeListener(this);
        phoneET.setOnFocusChangeListener(this);

        if (!TextUtils.isEmpty(mobileNumber)) {
            phoneET.setText(mobileNumber);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            try {
                login();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.forgotPasswordTv) {
            Intent intent = new Intent(myContext, FindPhoneNumActivity.class);
            myContext.startActivity(intent);
        } else if (v.getId() == R.id.iv_showPassword) {

            if (!hidePassword) {

                passwordET.setTransformationMethod(new PasswordTransformationMethod());
                hidePassword = true;
                if (passWordHighLighted) {
                    iv_showPassword.setImageResource(R.drawable.ic_hidepassword_white);
                } else {
                    iv_showPassword.setImageResource(R.drawable.ic_hidepassword);
                }
                passwordET.setSelection(passwordET.getText().length());

            } else {
                passwordET.setTransformationMethod(null);
                hidePassword = false;
                if (passWordHighLighted) {
                    iv_showPassword.setImageResource(R.drawable.ic_showpassword_white);
                } else {
                    iv_showPassword.setImageResource(R.drawable.ic_showpassword);
                }
                passwordET.setSelection(passwordET.getText().length());
            }
        }
    }

    //region VOLLEY CALLBACKS


    @Override
    public void onVolleyErrorListener(Object object) {

        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.DEACTIVATED_CODE) {
                MyDialog.showTwoButtonDialog(myContext, this, getString(R.string.info), getString(R.string.do_u_want_to_reactivate), getString(R.string.yes), getString(R.string.no));
            } else if (commonResponse.getCode() == Constants.DEACTIVATED_CODE_FROM_BACKEND) {
                MyDialog.showCommonDialogWithType(myContext, this, getString(R.string.info), getString(R.string.your_acnt_deactivated_contact_support), getString(R.string.okay), Constants.DEACTIVATED_CODE_FROM_BACKEND);
            } else if (commonResponse.getCode() == Constants.MOBILE_NOT_REGISTERED) {
                MyDialog.showToast(myContext, getString(R.string.not_registered_register_to_continue));
                replaceRegisterFrag();
            } else {
                Utils.handleCommonErrors(myContext, object);
            }
        } else {
            Utils.handleCommonErrors(myContext, object);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof User) {
            User user = (User) object;
            LoginData loginData= new LoginData();

            loginData.setAccessToken(EightfoldsUtils.getInstance().getFromSharedPreference(myContext, EightfoldsVolley.ACCESS_TOKEN));
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.USER_DETAILS, user);
            if (user.getTotalLoyalityPoints() != null) {
                EightfoldsUtils.getInstance().saveToSharedPreference(myContext, Constants.LOYALITY_POINTS, String.valueOf(user.getTotalLoyalityPoints().intValue()));
            }
            (new UsageAnalytics()).trackLogin(mobileNumber);
//            Utils.subscribeToAlertsPromosUpdates(loginData);
////            if (loginData.isFirstGamePlayed()) {
            EightfoldsUtils.getInstance().saveToSharedPreference(myContext, Constants.FIRST_GAME_PLAYED, "true");
//            Intent intent = new Intent(myContext, V2HomeFeatureActivity.class);
//            myContext.finish();
//            startActivity(intent);
              getSetup();
//            } else {
//                playNewGame(loginData);
//            }
        } else if (object instanceof HomePageResponse) {
        } else if (object instanceof Setup) {

            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.SET_UP_DETAILS, registerSetUpDetails);

            if (registerSetUpDetails.isGoLive()) {
                EightfoldsVolley.getInstance().showProgress(myContext);
                callDashboard();
            } else {
                Intent intent = new Intent(myContext, WingaNotLiveActivity.class);
                if (!TextUtils.isEmpty(registerSetUpDetails.getGoLiveTime()))
                    intent.putExtra(Constants.GOLIVE_DATE, registerSetUpDetails.getGoLiveTime());
                if (!TextUtils.isEmpty(registerSetUpDetails.getGoLiveMessage()))
                    intent.putExtra(Constants.GOLIVE_MESSAGE, registerSetUpDetails.getGoLiveMessage());
                if (!TextUtils.isEmpty(registerSetUpDetails.getCurrentTime()))
                    intent.putExtra(Constants.CURRENT_SERVER_TIME, registerSetUpDetails.getCurrentTime());
                myContext.finish();
                startActivity(intent);
            }
        }
    }

    //endregion

    //region EVENT LISTNERS
    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            Intent intent = new Intent(myContext, FindPhoneNumActivity.class);
            intent.putExtra(Constants.MOBILE_NUMBER, phoneET.getText().toString());
            intent.putExtra("toActivate", true);
            myContext.startActivity(intent);
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    //endregion


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.phoneET) {
            if (hasFocus) {
                highlightField(mobView, mobInnerLL, phoneET, mobIv, null, true);
            } else {
                makeFieldNormal(mobView, mobInnerLL, phoneET, mobIv, null, true);
            }
        } else if (id == R.id.passwordET) {
            if (hasFocus) {
                passWordHighLighted = true;
                highlightField(passwordView, passInnerLL, passwordET, passwordIv, iv_showPassword, false);
            } else {
                passWordHighLighted = false;
                makeFieldNormal(passwordView, passInnerLL, passwordET, passwordIv, iv_showPassword, false);
            }
        }

    }

    private void highlightField(View line, final LinearLayout lay, EditText editText, ImageView leftIcon, ImageView rightIcon, boolean isMobile) {
        line.setVisibility(View.GONE);
        lay.setBackgroundColor(getResources().getColor(R.color.app_theme_red));

        ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(400);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        lay.startAnimation(fade_in);

        int hightInDp = (int) getResources().getDimension(R.dimen.login_field_height);
        int marginInDp = 20;
        int margindpAsPixels = Utils.getDpAsPixels(myContext, marginInDp);

        int paddingInDp = 10;
        int paddingdpAsPixels = Utils.getDpAsPixels(myContext, paddingInDp);

        int paddingLeftRightInDp = 40;
        int paddingLeftRightdpAsPixels = Utils.getDpAsPixels(myContext, paddingLeftRightInDp);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, hightInDp);
        params.setMargins(margindpAsPixels, 0, margindpAsPixels, 0);
        lay.setPadding(paddingLeftRightdpAsPixels, paddingdpAsPixels, paddingLeftRightdpAsPixels, paddingdpAsPixels);
        lay.setLayoutParams(params);

        if (leftIcon != null) {
            if (isMobile) {
                leftIcon.setImageResource(R.drawable.ic_mobile_white);
            } else {
                leftIcon.setImageResource(R.drawable.ic_password_white);
            }
        }
        if (rightIcon != null) {
            if (!hidePassword) {
                rightIcon.setImageResource(R.drawable.ic_showpassword_white);
            } else {
                rightIcon.setImageResource(R.drawable.ic_hidepassword_white);
            }
        }
        editText.setTextColor(getResources().getColor(R.color.colorWhite));
        editText.setHintTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void makeFieldNormal(View line, LinearLayout lay, EditText editText, ImageView leftIcon, ImageView rightIcon, boolean isMobile) {
        line.setVisibility(View.VISIBLE);
        lay.setBackgroundColor(getResources().getColor(R.color.trasparant));
        int hightInDp = (int) getResources().getDimension(R.dimen.login_field_height);

        int marginInDp = 50;
        int margindpAsPixels = Utils.getDpAsPixels(myContext, marginInDp);

        int paddingInDp = 10;
        int paddingdpAsPixels = Utils.getDpAsPixels(myContext, paddingInDp);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, hightInDp);
        params.setMargins(margindpAsPixels, 0, margindpAsPixels, 0);
        lay.setPadding(paddingdpAsPixels, paddingdpAsPixels, paddingdpAsPixels, paddingdpAsPixels);

        lay.setLayoutParams(params);
        if (leftIcon != null) {
            if (isMobile) {
                leftIcon.setImageResource(R.drawable.ic_mobile_white);
            } else {
                leftIcon.setImageResource(R.drawable.ic_password_white);
            }
        }
        if (rightIcon != null) {
            if (!isMobile) {
                if (!hidePassword) {
                    rightIcon.setImageResource(R.drawable.ic_showpassword);
                } else {
                    rightIcon.setImageResource(R.drawable.ic_hidepassword);
                }
            }
        }

        editText.setTextColor(getResources().getColor(R.color.colorWhite));
        editText.setHintTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void login() throws JSONException {
        Utils.hideKeyboard(myContext, passwordET);
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
            String phoneNum = phoneET.getText().toString();
            String password = passwordET.getText().toString();
            if (TextUtils.isEmpty(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_mobile));
            } else if (!EightfoldsUtils.getInstance().isValidMobile(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_valid_phone));
            } else if (TextUtils.isEmpty(password)) {
                onVolleyErrorListener(getString(R.string.enter_password));
            } else if (password.length() < Constants.PASSWORD_LENGTH) {
                onVolleyErrorListener(getString(R.string.enter_eight_digit_password));
            } else if (!EightfoldsUtils.isAlphaNumeric(password)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_password));
            } else {
                String url = WingaConstants.LOGIN_URL;
                url = url.replace("{langId}", Utils.getCurrentLangId());
                EightfoldsVolley.getInstance().showProgress(myContext);


                LoginRequest loginRequest=new LoginRequest();
                UserCredentials user=new UserCredentials();
                user.setUsername(phoneNum);
                user.setPassword(password);

                loginRequest.setUser(user);
                UserDeviceDetail userDeviceDetail = EightfoldsUtils.buildUserDeviceDetail(getContext());


                userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(getContext()));


                userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(getContext()));
                loginRequest.setUserDeviceDetail(userDeviceDetail);
                EightfoldsVolley.getInstance().makingJsonRequestUsingJsonObject(this, User.class, Request.Method.POST, url, loginRequest);
            }
        }
    }

    private void getHomePageResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
//            String url = Constants.GET_HOME_PAGE_RESPONSE;
//            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.SELECTED_LANGUAGE_ID);
//            url = url.replace("{langId}", selectedLangId);
//
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
        }
    }

    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
            EightfoldsVolley.getInstance().showProgress(myContext);
            String url = Constants.GET_SETUP_SECURE;
            url = url.replace("{langId}", Utils.getCurrentLangId());

            Utils.setAppLanguage(myContext);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }


    private void replaceRegisterFrag() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();

        String mobileNumber = phoneET.getText().toString();

        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);

        RegisterBaseFrament registerBaseFrament = new RegisterBaseFrament();
        registerBaseFrament.setArguments(bundle);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.mainFL, registerBaseFrament);
        fragTransaction.commitAllowingStateLoss();

        ((LoginActivity) myContext).highLightRegister();

    }


    private void playNewGame(LoginData loginData) {
        //Intent intent = new Intent(myContext, RegistrationSuccessActivity.class);
        Intent intent;
        if (loginData.isGoLive()) {
            intent = new Intent(myContext, V2HomeFeatureActivity.class);
            intent.putExtra("firstgame", true);
            intent.putExtra("fromLogin", true);
        } else {
            intent = new Intent(myContext, WingaNotLiveActivity.class);
            if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
            if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
            if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
        }

        myContext.finish();
        startActivity(intent);
    }

    private void callDashboard() {

        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(myContext);
        Intent intent;
        if (homePageAd != null) {
            intent = new Intent(myContext, AdActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
        } else {
            intent = new Intent(myContext, V2HomeFeatureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);

            Utils.setAppLanguage(myContext);
        }
        myContext.finish();
        myContext.startActivity(intent);
    }


}
