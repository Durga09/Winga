package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 04-May-18.
 */

public class ResetPasswordActivity extends BaseActivity implements VolleyResultCallBack {

    private EditText currentPasswordEt, newPasswordEt, retypePasswordEt;
    private Button saveChangesBtn;
    private LoginData loginData;
    private String mobileNumber;
    private boolean toActivate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resetpassword);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            if (bundle.containsKey(Constants.DATA)) {
                loginData = (LoginData) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
            }
            if (bundle.containsKey("toActivate")) {
                toActivate = bundle.getBoolean("toActivate");
            }
        }
        initialize();
    }

    private void initialize() {

        setHeader(getString(R.string.set_password));


        currentPasswordEt = findViewById(R.id.currentPasswordEt);
        newPasswordEt = findViewById(R.id.newPasswordEt);
        retypePasswordEt = findViewById(R.id.retypePasswordEt);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);


        saveChangesBtn.setOnClickListener(this);
    }

    private void callChangePasswordForForgotPassword() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String newPassword = newPasswordEt.getText().toString();
            String confirmPassword = retypePasswordEt.getText().toString();

            if (TextUtils.isEmpty(newPassword)) {
                onVolleyErrorListener(getString(R.string.enter_new_password));

            } else if (newPassword.length() < Constants.PASSWORD_LENGTH) {
                onVolleyErrorListener(getString(R.string.enter_eight_digit_new_password));
            } else if (!EightfoldsUtils.isAlphaNumeric(newPassword)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_new_password));
            } else if (TextUtils.isEmpty(confirmPassword)) {
                onVolleyErrorListener(getString(R.string.enter_reenter_password));

            } else if (!newPassword.equals(confirmPassword)) {
                onVolleyErrorListener(getString(R.string.passwords_mustbesame));

            } else {
                String url = Constants.FORGOT_PASSWORD_CHANGE_URL.replace("{password}", newPassword);
                url = url.replace("{langId}", Utils.getCurrentLangId());

                if (loginData != null && !TextUtils.isEmpty(loginData.getAccessToken())) {
                    EightfoldsVolley.getInstance().showProgress(this);

                    EightfoldsVolley.getInstance(loginData.getAccessToken()).makingStringRequest(this, LoginData.class, Request.Method.POST, url);
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        EightfoldsUtils.getInstance().hideKeyboard(this);
        if (v.getId() == R.id.saveChangesBtn) {

                callChangePasswordForForgotPassword();


        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof LoginData) {
            LoginData loginData = (LoginData) object;
            MyDialog.showToast(this, getString(R.string.change_password_success));
            EightfoldsUtils.getInstance().saveToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());
            if (loginData.getUser().getTotalLoyalityPoints() != null) {
                EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOYALITY_POINTS, String.valueOf(loginData.getUser().getTotalLoyalityPoints().intValue()));
            }
            (new UsageAnalytics()).trackLogin(mobileNumber);
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
        }
        if (object instanceof HomePageResponse) {

            callDashboard((HomePageResponse) object);
        } else if (object instanceof CommonResponse) {
            onVolleyErrorListener(object);
        }
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
            Intent intent = new Intent(this, V2HomeFeatureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Utils.setAppLanguage(this);
            this.finish();
            this.startActivity(intent);
        }
    }

    private void getHomePageResponse() {
        callDashboard(new HomePageResponse() );
    }


    private void playNewGame(LoginData loginData) {

        NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, loginData.getNewGameResponse());

        //Intent intent = new Intent(this, RegistrationSuccessActivity.class);
        Intent intent;
        intent = new Intent(this, RegistrationSuccessActivity.class);
        intent.putExtra(Constants.DATA, filteredLangGameResp);
        intent.putExtra("firstgame", true);
        intent.putExtra("fromLogin", true);

        finish();
        startActivity(intent);

    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }
}
