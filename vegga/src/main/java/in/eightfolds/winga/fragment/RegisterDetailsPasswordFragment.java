package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.activity.RegistrationSuccessActivity;
import in.eightfolds.winga.activity.V2HomeFeatureActivity;
import in.eightfolds.winga.activity.WingaNotLiveActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

public class RegisterDetailsPasswordFragment extends Fragment implements View.OnClickListener, VolleyResultCallBack {

    private Button registerBtn;
    private EditText passwordET, retypePasswordET;
    private LoginData registerRequest;
    private LinearLayout passwordLL, rettypepasswordLL;
    private String otpToken;
    private ImageView iv_showretypePassword, iv_showPassword;
    private Activity myContext;
    private boolean hidePassword = true, hideRetypePassword = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_password_details, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA))
                registerRequest = (LoginData) bundle.get(Constants.DATA);
            if (bundle.containsKey("otpToken")) {
                otpToken = bundle.getString("otpToken");
            }
        }
        initialize(view);
        ((LoginActivity) myContext).setLoginFrameInCenter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(RegisterDetailsFragment.class.getSimpleName());
    }

    private void initialize(View view) {

        registerBtn = view.findViewById(R.id.registerBtn);

        passwordET = view.findViewById(R.id.passwordET);
        retypePasswordET = view.findViewById(R.id.retypePasswordET);

        passwordLL = view.findViewById(R.id.passwordLL);
        rettypepasswordLL = view.findViewById(R.id.rettypepasswordLL);


        iv_showretypePassword = view.findViewById(R.id.iv_showretypePassword);
        iv_showPassword = view.findViewById(R.id.iv_showPassword);

        registerBtn.setOnClickListener(this);
        iv_showretypePassword.setOnClickListener(this);
        iv_showPassword.setOnClickListener(this);


        String loginType = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.LOGIN_TYPE);
        if (loginType != null && !TextUtils.isEmpty(loginType) && (loginType.equalsIgnoreCase(Constants.GOOGLE_LOGIN) || loginType.equalsIgnoreCase(Constants.FB_LOGIN))) {
            passwordLL.setVisibility(View.GONE);
            rettypepasswordLL.setVisibility(View.GONE);
        }
    }


    private void registerUser() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
            String password = passwordET.getText().toString();
            String reenterPassword = retypePasswordET.getText().toString();

            if (passwordLL.getVisibility() == View.VISIBLE && (TextUtils.isEmpty(password))) {
                onVolleyErrorListener(getString(R.string.enter_password));
            } else if (password.length() < Constants.PASSWORD_LENGTH) {
                onVolleyErrorListener(getString(R.string.enter_eight_digit_password));
            } else if (!EightfoldsUtils.isAlphaNumeric(password)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_password));
            } else if (rettypepasswordLL.getVisibility() == View.VISIBLE && (TextUtils.isEmpty(reenterPassword))) {
                onVolleyErrorListener(getString(R.string.enter_reenter_password));
            } else if (rettypepasswordLL.getVisibility() == View.VISIBLE && !(password.equalsIgnoreCase(reenterPassword))) {
                onVolleyErrorListener(getString(R.string.passwords_mustbesame));
            } else {
                if (passwordET.getVisibility() == View.VISIBLE) {
                    registerRequest.setPassword(passwordET.getText().toString());
                }
                String imei = EightfoldsUtils.getInstance().getDeviceIMEI(myContext);
                registerRequest.setImei(imei);
                String url = Constants.REGISTRATION_URL;
                url = url.replace("{otpToken}", otpToken);
                url = url.replace("{langId}", Utils.getCurrentLangId());
                EightfoldsVolley.getInstance().showProgress(myContext);
                EightfoldsVolley.getInstance().makingJsonRequest(RegisterDetailsPasswordFragment.this, LoginData.class, Request.Method.POST, url, registerRequest);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            registerUser();
        } else if (v.getId() == R.id.iv_showretypePassword) {
            if (!hideRetypePassword) {
                retypePasswordET.setTransformationMethod(new PasswordTransformationMethod());
                hideRetypePassword = true;
                iv_showretypePassword.setImageResource(R.drawable.ic_hidepassword);
                retypePasswordET.setSelection(retypePasswordET.getText().length());
            } else {
                hideRetypePassword = false;
                retypePasswordET.setTransformationMethod(null);
                iv_showretypePassword.setImageResource(R.drawable.ic_showpassword);
                retypePasswordET.setSelection(retypePasswordET.getText().length());
            }
        } else if (v.getId() == R.id.iv_showPassword) {
            if (!hidePassword) {
                passwordET.setTransformationMethod(new PasswordTransformationMethod());
                hidePassword = true;
                iv_showPassword.setImageResource(R.drawable.ic_hidepassword);
                passwordET.setSelection(passwordET.getText().length());
            } else {
                hidePassword = false;
                passwordET.setTransformationMethod(null);
                iv_showPassword.setImageResource(R.drawable.ic_showpassword);
                passwordET.setSelection(passwordET.getText().length());
            }
        }
    }


    private void callRegisterSuccessActivity(LoginData loginData) {
        // Intent intent = new Intent(myContext, RegistrationSuccessActivity.class);
        Intent intent ;
//        if (loginData.isGoLive()) {
//            intent = new Intent(myContext, RegistrationSuccessActivity.class);
//        } else {
//            intent = new Intent(myContext, WingaNotLiveActivity.class);
//            if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
//                intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
//            if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
//                intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
//            if (!TextUtils.isEmpty(loginData.getCurrentTime()))
//                intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
//        }

        intent = new Intent(myContext, V2HomeFeatureActivity.class);

        myContext.finish();
        startActivity(intent);
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof LoginData) {
            LoginData loginData = (LoginData) object;
            EightfoldsUtils.getInstance().saveToSharedPreference(myContext, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.USER_DETAILS, loginData.getUser());
            if (loginData.getUser().getTotalLoyalityPoints() != null) {
                EightfoldsUtils.getInstance().saveToSharedPreference(myContext, Constants.LOYALITY_POINTS, String.valueOf(loginData.getUser().getTotalLoyalityPoints().intValue()));
            }
            callRegisterSuccessActivity(loginData);

        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(myContext, object);

        if (object instanceof CommonResponse) {
            CommonResponse response = (CommonResponse) object;
            if (response.getCode() == Constants.OTP_EXPIRED) {


                Bundle bundle = new Bundle();
                RegisterBaseFrament registerBaseFrament = new RegisterBaseFrament();
                registerBaseFrament.setArguments(bundle);

                FragmentTransaction fragTransaction = null;
                if (getFragmentManager() != null) {
                    fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.replace(R.id.mainFL, registerBaseFrament);
                    fragTransaction.commitAllowingStateLoss();
                }

            } else if (response.getCode() == Constants.INVALID_PINCODE) {

                getFragmentManager().popBackStack();
               /* Bundle bundle = new Bundle();
                RegisterDetailsFragment registerDetailsFragment = new RegisterDetailsFragment();
                registerDetailsFragment.setArguments(bundle);

                FragmentTransaction fragTransaction = null;
                if (getFragmentManager() != null) {
                    fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.replace(R.id.mainFL, registerDetailsFragment);
                    fragTransaction.commitAllowingStateLoss();
                }*/

            } else if (response.getCode() == Constants.INVALID_REFERRAL_CODE) {

                getFragmentManager().popBackStack();

            }
        }
    }
}
