package in.eightfolds.winga.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.IOException;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by sp on 09/05/18.
 */

public class EnterCodeActivity extends BaseActivity implements VolleyResultCallBack {

    private EditText codeEt;
    private Button nextBtn, changeMobBtn;
    private TextView enterCodeDescTv, resendCodeTv, timeTv;
    private String mobileNum;
    private String FORGOT_PASSWORD_GET_OTP_URL = "";
    private CountDownTimer countDownTimer;
    private long timeRemainingSeconds = 3 * 60;
    private boolean toActivate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_entercode);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNum = bundle.getString(Constants.MOBILE_NUMBER);
            }
            if (bundle.containsKey("toActivate")) {
                toActivate = bundle.getBoolean("toActivate");
            }
        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.enter_code));
        codeEt = findViewById(R.id.codeEt);
        nextBtn = findViewById(R.id.nextBtn);
        enterCodeDescTv = findViewById(R.id.enterCodeDescTv);
        changeMobBtn = findViewById(R.id.changeMobBtn);
        resendCodeTv = findViewById(R.id.resendCodeTv);
        timeTv = findViewById(R.id.timeTv);
        if (!TextUtils.isEmpty(mobileNum)) {
            enterCodeDescTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.sent_code_desc), mobileNum));
        }
        nextBtn.setOnClickListener(this);
        resendCodeTv.setOnClickListener(this);
        changeMobBtn.setOnClickListener(this);
        setTimer();

        if (toActivate) {
            changeMobBtn.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.RECEIVED_OTP_ACTION);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.RECEIVED_OTP_ACTION)) {
                String otp = intent.getStringExtra(Constants.DATA);
                if (!TextUtils.isEmpty(otp)) {
                    codeEt.setText(otp);
//                    ((EditText) findViewById(R.id.otpET)).setSelection(otp.length());

                    closeTimer();
                    //countDownTimer.onFinish();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        closeTimer();
        super.onDestroy();
    }


    private void callVerifyOTP() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String code = codeEt.getText().toString();
            if (TextUtils.isEmpty(code)) {  //|| code.length() != 6
                onVolleyErrorListener(getString(R.string.enter_valid_ver_code));

            } else {
                String url = Constants.FORGOT_PASSWORD_VERIFY_OTP_URL.replace("{username}", mobileNum)
                        .replace("{verificationCode}", code);
                url = url.replace("{langId}", Utils.getCurrentLangId());
                EightfoldsVolley.getInstance().showProgress(this);
                EightfoldsVolley.getInstance().makingStringRequest(this, LoginData.class, Request.Method.POST, url);
            }

        }
    }

    private void callGenerateOTP() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            FORGOT_PASSWORD_GET_OTP_URL = Constants.FORGOT_PASSWORD_GET_OTP_URL.replace("{username}", mobileNum);
            FORGOT_PASSWORD_GET_OTP_URL = FORGOT_PASSWORD_GET_OTP_URL.replace("{confirm}", true + "");
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.GET, FORGOT_PASSWORD_GET_OTP_URL);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.resendCodeTv) {
            codeEt.setText("");
            callGenerateOTP();
        } else if (v.getId() == R.id.nextBtn){
            EightfoldsUtils.getInstance().hideKeyboard(this);
        callVerifyOTP();
    }
            else if(v.getId()== R.id.changeMobBtn){
                finish();
        }

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof LoginData) {
            LoginData loginData = (LoginData) object;
            MyDialog.showToast(this, getString(R.string.your_acnt_activated_success));
            if (toActivate) {
                EightfoldsUtils.getInstance().saveToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
                EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
                EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());
                if (loginData.getUser().getTotalLoyalityPoints() != null) {
                    EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOYALITY_POINTS, String.valueOf(loginData.getUser().getTotalLoyalityPoints().intValue()));
                }
                Utils.subscribeToAlertsPromosUpdates(loginData);
                if (loginData.isGoLive()) {
                    if (loginData.isFirstGamePlayed()) {
                        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRST_GAME_PLAYED, "true");
                        getHomePageResponse();
                    } else {
                        playNewGame(loginData);
                    }
                } else {
                    Intent intent = new Intent(this, WingaNotLiveActivity.class);
                    if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                        intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
                    if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                        intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
                    if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                        intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
                    finish();
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra(Constants.MOBILE_NUMBER, mobileNum);
                intent.putExtra(Constants.DATA, loginData);
                intent.putExtra("toActivate", toActivate);
                startActivity(intent);
                finish();
            }

        }
        if (object instanceof HomePageResponse) {
            callDashboard((HomePageResponse) object);
        } else if (object instanceof CommonResponse) {
            CommonResponse response = (CommonResponse) object;
            if (requestType.equalsIgnoreCase(FORGOT_PASSWORD_GET_OTP_URL)) {
                if (response.getCode() == Constants.SUCCESS) {
                    MyDialog.showToast(this, getString(R.string.code_sent_successfully));
                    setTimer();
                    resendCodeTv.setVisibility(View.GONE);
                    timeTv.setVisibility(View.VISIBLE);
                } else {
                    onVolleyErrorListener(object);
                }
            } else {
                onVolleyErrorListener(object);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                resendCodeTv.setVisibility(View.VISIBLE);
                timeTv.setVisibility(View.GONE);
            }
        }.start();
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    private void callDashboard(HomePageResponse homePageResponse) {
        boolean isLuckyDrawNotificationUnread = false;
        if (homePageResponse.getNotification() != null && homePageResponse.getNotification().getNotificationId() != null && homePageResponse.getNotification().getType() == Notification.NOTIFICATION_TYPE_LUCKY_DRAW) {
            if (!Utils.isNotificationIsRead(this, homePageResponse.getNotification())) {
                isLuckyDrawNotificationUnread = true;
            }
        }
        if (isLuckyDrawNotificationUnread) {
            Intent intent = new Intent(this, WinLuckyDrawActivity.class);
            try {
                if (!TextUtils.isEmpty(homePageResponse.getNotification().getJsonData())) {
                    PrizeWin prizeWin = (PrizeWin) Api.fromJson(homePageResponse.getNotification().getJsonData(), PrizeWin.class);
                    intent.putExtra(Constants.OTHER_DATA, prizeWin);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtra(Constants.DATA, homePageResponse.getNotification());
            intent.putExtra(Constants.FROM_NOTIFICATION, true);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, HomeBaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            Utils.setAppLanguage(this);
            this.finish();
            this.startActivity(intent);
        }
    }

    private void getHomePageResponse() {
//        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//            String url = Constants.GET_CATEGORIES_LIST;
//            url = url.replace("{langId}", Utils.getCurrentLangId());
//
//            EightfoldsVolley.getInstance().showProgress(this);
//
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
//        }
        callDashboard(new HomePageResponse());
    }


    private void playNewGame(LoginData loginData) {

        Intent intent;
        if (loginData.isGoLive()) {
            intent = new Intent(this, RegistrationSuccessActivity.class);
            intent.putExtra("firstgame", true);
            intent.putExtra("fromLogin", true);
        } else {
            intent = new Intent(this, WingaNotLiveActivity.class);
            if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
            if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
            if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
        }
        finish();
        startActivity(intent);

    }
}
