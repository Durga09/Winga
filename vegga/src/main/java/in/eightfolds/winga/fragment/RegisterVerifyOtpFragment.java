package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.model.VerifyOTPResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class RegisterVerifyOtpFragment extends Fragment implements View.OnClickListener, VolleyResultCallBack {

    private Button verifyOTPBtn;
    private String mobileNum;
    private TextView mobET;
    private EditText otpET;
    private TextView resendTv, changeMobTv, timeTv;
    private ImageView iv_showOtp;
    private boolean hideOtp = true;
    private CountDownTimer countDownTimer;
    private long timeRemainingSeconds = 3 * 60;
    boolean onFragAttached = false;

    private Activity myContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragAttached = true;
        myContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!onFragAttached) {


        }
        View view = inflater.inflate(R.layout.fragment_register_otp, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNum = bundle.getString(Constants.MOBILE_NUMBER);
            }
        }
        ((LoginActivity) myContext).setLoginFrameInCenter();
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        iv_showOtp = (ImageView) view.findViewById(R.id.iv_showOtp);
        verifyOTPBtn = view.findViewById(R.id.verifyOTPBtn);
        resendTv = view.findViewById(R.id.resendTv);
        changeMobTv = view.findViewById(R.id.changeMobTv);
        timeTv = view.findViewById(R.id.timeTv);
        otpET = view.findViewById(R.id.otpET);
        mobET = view.findViewById(R.id.mobET);
        mobET.setText(!TextUtils.isEmpty(mobileNum) ? mobileNum : "");
        verifyOTPBtn.setOnClickListener(this);
        iv_showOtp.setOnClickListener(this);
        resendTv.setOnClickListener(this);
        changeMobTv.setOnClickListener(this);
        setTimer();

    }

    @Override
    public void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(RegisterVerifyOtpFragment.class.getSimpleName());

        registerReceiver();
    }

    private void closeTimer() {
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
    }

    private void setTimer() {
        Logg.i("onTick", "timeRemainingSeconds >> " + timeRemainingSeconds);
        closeTimer();

        countDownTimer = new CountDownTimer(((timeRemainingSeconds) * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = (millisUntilFinished / 1000);
                long minutesRemaining = secondsRemaining / 60;
                long hoursRemaining = minutesRemaining / 60;

                long finalminutes = minutesRemaining % 60;
                long finalSeconds = secondsRemaining % 60;

                String minutes = finalminutes + "";

                String seconds = finalSeconds + "";

                if (minutes.length() == 1) {
                    minutes = "0" + minutes;
                }
                if (seconds.length() == 1) {
                    seconds = "0" + seconds;
                }
                String time = "";

                time = minutes + " : " + seconds;


                timeTv.setText(time);
                Logg.i("onTick", "Minutes remaining: " + (millisUntilFinished / 1000) / 60);

            }

            public void onFinish() {
                resendTv.setVisibility(View.VISIBLE);
                timeTv.setVisibility(View.GONE);
            }
        }.start();
    }


    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.RECEIVED_OTP_ACTION);
        myContext.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.RECEIVED_OTP_ACTION)) {
                String otp = intent.getStringExtra(Constants.DATA);
                if (!TextUtils.isEmpty(otp)) {
                    otpET.setText(otp);
//                    ((EditText) findViewById(R.id.otpET)).setSelection(otp.length());

                    if (hideOtp) {
                        otpET.setTransformationMethod(null);
                        hideOtp = false;
                        iv_showOtp.setImageResource(R.drawable.ic_showpassword);
                        otpET.setSelection(otpET.getText().length());
                    }

                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        try {
            if (receiver != null) {
                myContext.unregisterReceiver(receiver);
            }
            closeTimer();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.verifyOTPBtn) {
           callVerifyOTP();
            //replaceRegisterDetailsFrag("");
        } else if (v.getId() == R.id.resendTv) {
            otpET.setText("");
            callGenerateOTP();
        } else if (v.getId() == R.id.changeMobTv) {
            //TODO take the user to enter mob fragment
            myContext.onBackPressed();

        } else if (v.getId() == R.id.iv_showOtp) {
            if (!hideOtp) {

                otpET.setTransformationMethod(new PasswordTransformationMethod());
                hideOtp = true;
                iv_showOtp.setImageResource(R.drawable.ic_hidepassword);
                otpET.setSelection(otpET.getText().length());

            } else {
                otpET.setTransformationMethod(null);
                hideOtp = false;
                iv_showOtp.setImageResource(R.drawable.ic_showpassword);
                otpET.setSelection(otpET.getText().length());
            }
        }
    }

    private void replaceRegisterDetailsFrag(String otpToken) {
        RegisterDetailsFragment registerDetailsFragment = new RegisterDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MOBILE_NUMBER, mobileNum);
        bundle.putString("otpToken", otpToken);
        registerDetailsFragment.setArguments(bundle);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFL, registerDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void callGenerateOTP() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            String url = Constants.GENERATE_OTP_URL.replace("{mobile}", mobileNum);
            EightfoldsVolley.getInstance().showProgress(myContext);

            UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(getContext());
            String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.FIRE_BASE_TOKEN);
            userDeviceDetail.setPushRegId(refreshedToken);

            userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(getContext()));


            EightfoldsVolley.getInstance().showProgress(myContext);
            EightfoldsVolley.getInstance().makingJsonRequest(RegisterVerifyOtpFragment.this, CommonResponse.class, Request.Method.POST, url,userDeviceDetail);
           }
    }

    private void callVerifyOTP() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            String otp = otpET.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                onVolleyErrorListener(getString(R.string.enter_otp));

            } else if (otp.length() < 6) {
                onVolleyErrorListener(getString(R.string.enter_digit_otp));
            } else {
                String url = Constants.VERIFY_OTP_URL.replace("{mobile}", mobileNum)
                        .replace("{otp}", otp);

                UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(getContext());
                String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.FIRE_BASE_TOKEN);
                userDeviceDetail.setPushRegId(refreshedToken);

                userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(getContext()));

                EightfoldsVolley.getInstance().showProgress(myContext);
                EightfoldsVolley.getInstance().makingJsonRequest(RegisterVerifyOtpFragment.this, VerifyOTPResponse.class, Request.Method.POST, url,userDeviceDetail);
            }
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (requestType.contains("verify")) {
            if (object instanceof VerifyOTPResponse) {
                VerifyOTPResponse response = (VerifyOTPResponse) object;

                if (response.getCode() == Constants.SUCCESS) {
                    if (response.getData() != null && !TextUtils.isEmpty(response.getData().getOtpToken())) {
                        replaceRegisterDetailsFrag(response.getData().getOtpToken());
                    } else {
                        replaceRegisterDetailsFrag("");
                    }
                } else {
                    onVolleyErrorListener(response);
                }
            }
        } else {
            if (object instanceof CommonResponse) {
                CommonResponse response = (CommonResponse) object;
                setTimer();
                resendTv.setVisibility(View.GONE);
                timeTv.setVisibility(View.VISIBLE);
                onVolleyErrorListener(getString(R.string.otp_sent));
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(myContext, object);
    }
}
