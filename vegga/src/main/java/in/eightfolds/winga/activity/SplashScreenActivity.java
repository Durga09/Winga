package in.eightfolds.winga.activity;

import static in.eightfolds.rest.EightfoldsVolley.ACCESS_TOKEN;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
//import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.crashlytics.buildtools.reloc.org.checkerframework.checker.nullness.compatqual.NullableDecl;


import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.R;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.fcm.MyFirebaseMessagingService;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.MobileAppVersion;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.AppSignatureHelper;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ContentViewDetailsManager;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.constant.WingaConstants;

public class SplashScreenActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private String accessToken;
    // private HTextView flipTv;
    private ImageView logoIv, flipIv;

    private volatile boolean animationCompleted = false;
    private boolean animationStarted = false;
    private boolean onExited, onBackPressed;
//    private WebpDrawable gifDrawable;
    private int notificationType = 0;
    private boolean fromNotification = false;
    Notification notification = new Notification();
    private Setup registerSetUpDetails;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        super.onCreate(savedInstanceState);
        if (Constants.WRITELOGS_TO_TEXTFILE) {
            EightfoldsUtils.getInstance().verifyStoragePermissions(this);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Utils.getScreenResolution(this);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Logg.d(TAG, "Key: " + key + " Value: " + value.toString());
            }

            if (getIntent().getExtras().containsKey("type")) {
                notificationType = Integer.valueOf(getIntent().getExtras().getString("type"));
            }
        }


        logoIv = findViewById(R.id.logoIv);
        flipIv = findViewById(R.id.flipIv);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void test() {
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        List<String> strings = appSignatureHelper.getAppSignatures();
    }


    Dialog errorDialog;

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                if (errorDialog == null) {
                    errorDialog = googleApiAvailability.getErrorDialog(this, resultCode, 2404);
                    errorDialog.setCancelable(false);
                }
                if (!errorDialog.isShowing())
                    errorDialog.show();
            }
        }

        return resultCode == ConnectionResult.SUCCESS;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            DateTime.getCurrentTimeZone();
            if (checkPlayServices()) {

                TimerTask task = new TimerTask() {
                    public void run() {
                        Utils.getDynamicLink(SplashScreenActivity.this);
                        Utils.subscribeToTopic();

                        getSetup();
                        startService(new Intent(SplashScreenActivity.this, MyFirebaseMessagingService.class));
                    }
                };
                Timer timer = new Timer("Timer");

                long delay = 3000L;
                timer.schedule(task, delay);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pushContentViewDetailsToServer() {
        ContentViewDetailsManager.pushContentViewDetailsToServer(AppDataBase.getAppDatabase(this));
        ContentViewDetailsManager.pushAdContentViewDetailsToServer(SplashScreenActivity.this, Utils.getUserId(this), AppDataBase.getAppDatabase(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onExited = true;
        Glide.get(this).clearMemory();
    }

    @Override
    public void onBackPressed() {
        animationCompleted = false;
        onBackPressed = true;
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Logg.v(TAG, "onWindowFocusChanged");
        if (!hasFocus || animationStarted) {
            return;
        }

        fade();

        //flipTv.setText();

        super.onWindowFocusChanged(hasFocus);
    }

    public void zoom(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
        image.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fade();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadGif() {
        flipIv.setVisibility(View.VISIBLE);
        try {
            //splash_text_win
            Glide.with(this)
                    .load(R.drawable.splash_text_win)

                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Logg.v(TAG, "**onResourceReady**");

//                            if (resource instanceof WebpDrawable) { //GifDrawable
//                                gifDrawable = (WebpDrawable) resource;
//                                gifDrawable.startFromFirstFrame();
//                                gifDrawable.setLoopCount(1);
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                        while (!onExited) {
//                                            if (!gifDrawable.isRunning() && !onBackPressed) {
//                                                animationCompleted = true;
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }).start();
//                            }
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(flipIv);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void fade() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(logoIv, "alpha", 0f, 1f);
        objectAnimator.setStartDelay(300);
        objectAnimator.setDuration(900);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animationStarted = true;
                logoIv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                loadGif();
            }
        });
        objectAnimator.start();

    }


    private void login() {
        Intent intent = new Intent(SplashScreenActivity.this, V2HomeFeatureActivity.class);

        if (Utils.isAppIsInBackground(SplashScreenActivity.this)) {
            startActivity(intent);
        } else {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    SplashScreenActivity.this,
                    new Pair<View, String>(logoIv
                            ,
                            HomeActivity.VIEW_NAME_LOGO_IMAGE));
            ActivityCompat.startActivity(SplashScreenActivity.this, intent, activityOptions.toBundle());
        }
        finish();
    }


    private void getHomePageResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            Intent intent = new Intent(this, HomeBaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            Utils.setAppLanguage(this);
            this.finish();
            this.startActivity(intent);
        } else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }

    private void goToLanguageSelectionActivity() {

        Thread someThread = new Thread() {

            @Override
            public void run() {
                //some actions
                try {
                    while (true) {
//                        if (animationCompleted) { //wait for condition
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    EightfoldsUtils.getInstance().saveToSharedPreference(SplashScreenActivity.this, Constants.SELECTED_LANGUAGE_ID, 1);
                                    Utils.setAppLanguage(SplashScreenActivity.this);
                                    intent.putExtra("isfromsplash", true);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            break;

                        }
                        Thread.sleep(300);
//                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        someThread.start();
    }

    private void goToDashBoardActivity(final HomePageResponse homePageResponse) {

        Thread someThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (true) {
                        if (animationCompleted) { //wait for completion
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        getSetup();
//                                        Utils.subscribeToAlertsPromosUpdates(homePageResponse.getUserDetail());
//
//                                        if (notificationType == 0) {
//                                            callHomeWithAnim(homePageResponse);
//                                        } else {
//                                            fromNotification = true;
//                                            Intent intent = new Intent();
//                                            PrizeWin prizeWin = null;
//
//                                            Bundle bundle = getIntent().getExtras();
//
//                                            if (bundle != null) {
//                                                if (bundle.containsKey("notificationId")) {
//                                                    String notId = bundle.getString("notificationId");
//                                                    if (notId != null && !TextUtils.isEmpty(notId) && !notId.equalsIgnoreCase("null")) {
//                                                        Long notificationId = Long.valueOf(notId);
//                                                        notification.setNotificationId(notificationId);
//                                                    }
//                                                }
//                                                if (bundle.containsKey("imageId")) {
//                                                    String imgId = bundle.getString("imageId");
//                                                    if (!TextUtils.isEmpty(imgId)) {
//                                                        try {
//                                                            Long imageId = Long.valueOf(imgId);
//                                                            Logg.v(TAG, "imageId : " + imageId);
//                                                            notification.setImageId(imageId);
//                                                        } catch (NumberFormatException ex) {
//                                                            ex.printStackTrace();
//                                                        }
//                                                    }
//                                                }
//                                                if (bundle.containsKey("title")) {
//                                                    notification.setTitle(bundle.getString("title"));
//                                                }
//
//                                                if (bundle.containsKey("message")) {
//                                                    notification.setMessage(bundle.getString("message"));
//                                                }
//                                                notification.setType(notificationType);
//
//                                                String jsonData = "";
//                                                if (bundle.containsKey("jsonData")) {
//                                                    jsonData = bundle.getString("jsonData");
//                                                    notification.setJsonData(jsonData);
//                                                } else if (bundle.containsKey("prizeObj")) {
//                                                    jsonData = bundle.getString("prizeObj");
//                                                    notification.setJsonData(jsonData);
//                                                }
//                                                try {
//                                                    if (!TextUtils.isEmpty(jsonData)) {
//                                                        prizeWin = (PrizeWin) Api.fromJson(jsonData, PrizeWin.class);
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            if (notificationType == Notification.NOTIFICATION_TYPE_SESSION_END
//                                                    || notificationType == Notification.NOTIFICATION_TYPE_SESSION_START) {
//                                                callHomeWithAnim(homePageResponse);
//                                            } else {
//                                                if (notificationType == Notification.NOTIFICATION_TYPE_LUCKY_DRAW && prizeWin != null) {
//                                                    intent = new Intent(SplashScreenActivity.this, WinLuckyDrawActivity.class);
//                                                    intent.putExtra(Constants.OTHER_DATA, prizeWin);
//                                                } else if (notificationType == Notification.NOTIFICATION_TYPE_REFERRAL) {
//                                                    intent = new Intent(SplashScreenActivity.this, CodesAndInvitesActivity.class);
//                                                    intent.putExtra("toInvitesTab", true);
//                                                } else if (notificationType == Notification.NOTIFICATION_TYPE_RESULT_DECLARED) {
//                                                    intent = new Intent(SplashScreenActivity.this, RecentWinnersActivity.class);
//                                                } else if (notificationType == Notification.NOTIFICATION_TYPE_DISBURSEMENT) {
//                                                    intent = new Intent(SplashScreenActivity.this, TotalRewardsActivity.class);
//                                                } else {
//                                                    intent = new Intent(SplashScreenActivity.this, NotificationDetailsActivity.class);
//                                                }
//                                                intent.putExtra(Constants.DATA, notification);
//                                                intent.putExtra(Constants.FROM_NOTIFICATION, true);
//                                                startActivity(intent);
//                                            }
//                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            });
                            break;
                        }
                        Thread.sleep(300);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        someThread.start();
    }

    private void callHomeWithAnim(HomePageResponse homePageResponse) {
        Utils.refreshAccessToken(this);

        boolean isLuckyDrawNotificationUnread = false;
        if (homePageResponse.getNotification() != null && homePageResponse.getNotification().getNotificationId() != null && homePageResponse.getNotification().getType() == Notification.NOTIFICATION_TYPE_LUCKY_DRAW) {
            if (!Utils.isNotificationIsRead(this, homePageResponse.getNotification())) {
                isLuckyDrawNotificationUnread = true;
            }
        }
        if (registerSetUpDetails != null && registerSetUpDetails.getHomePageAd() != null) {
            Intent intent = new Intent(SplashScreenActivity.this, AdActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);

            intent.putExtra(Constants.FROM_NOTIFICATION, fromNotification);
            intent.putExtra(Constants.DATA, notification);
            intent.putExtra(Constants.ADD_DETAILS, registerSetUpDetails.getHomePageAd());
            finish();
            startActivity(intent);
            // Utils.goHomeOrAdd(this, intent);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, V2HomeFeatureActivity.class);
            startActivity(intent);
            finish();
//            if (Utils.isAppIsInBackground(SplashScreenActivity.this)) {
//                startActivity(intent);
//                finish();
//            } else {
//                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        SplashScreenActivity.this,
//                        new Pair<View, String>(logoIv
//                                ,
//                                HomeActivity.VIEW_NAME_LOGO_IMAGE));
//                ActivityCompat.startActivity(SplashScreenActivity.this, intent, activityOptions.toBundle());
//            }

        }
    }


    private void playNewGame(final LoginData loginData) {
        Thread someThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (true) {
//                        if (animationCompleted) { //wait for condition
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent;
                                    if (loginData.isGoLive()) {
                                        intent = new Intent(SplashScreenActivity.this, V2HomeFeatureActivity.class);
                                        intent.putExtra("fromLogin", true);
                                    } else {
                                        intent = new Intent(SplashScreenActivity.this, WingaNotLiveActivity.class);
                                        if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                                            intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
                                        if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                                            intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
                                        if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                                            intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            break;
                        }
                        Thread.sleep(300);
//                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        someThread.start();
    }


    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = "";
            accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, ACCESS_TOKEN);
            Utils.setAppLanguage(this);
            if (TextUtils.isEmpty(accessToken)) {
                url = Constants.GET_SETUP;
            } else {
                url = Constants.GET_SETUP_SECURE;
            }
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        } else {
            goToNoInternetActivity();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
//            Intent intent = new Intent(SplashScreenActivity.this, HomeBaseActivity.class);
//            startActivity(intent);
            LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.LOGIN_DATA, LoginData.class);

        } else if (requestType.toLowerCase().contains("login")) {
            LoginData loginData = (LoginData) object;
            EightfoldsUtils.getInstance().saveToSharedPreference(this, ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());

//            UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail();
//            userDeviceDetail.setUserId(loginData.getUserId());
//            userDeviceDetail.setImei(loginData.getImei());
//            String url = Constants.USER_DEVICE_DETAILS_URL;
//
//
//
//
//            EightfoldsVolley.getInstance().dismissProgress();
            if (loginData.isGoLive()) {
                if (loginData.isFirstGamePlayed()) {
                    EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRST_GAME_PLAYED, "true");
                    goToDashBoardActivity(new HomePageResponse());
                } else {
                    playNewGame(loginData);
                }
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, WingaNotLiveActivity.class);
                if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                    intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
                if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                    intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
                if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                    intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
                startActivity(intent);
                finish();
            }
        } else if (object instanceof Setup) {
            registerSetUpDetails = (Setup) object;
            LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.LOGIN_DATA, LoginData.class);



//            EightfoldsVolley.getInstance().dismissProgress();

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(this);
//            }
//            getNetwork();

            try {

                MobileAppVersion androidDetail = null;
                Map<Integer, MobileAppVersion> mobileAppVersionMap = registerSetUpDetails.getLatestMobileAppVersions();
                if (mobileAppVersionMap != null) {
                    for (Map.Entry<Integer, MobileAppVersion> entry : mobileAppVersionMap.entrySet()) {
                        if (entry.getKey() == Constants.ANDROID_PLATFORM_ID) {
                            androidDetail = entry.getValue();
                            break;
                        }
                    }
                    if (androidDetail != null) {
                        int appVersionCode = BuildConfig.VERSION_CODE;
                        long serverVersionCode = androidDetail.getVersionCode();
                        if (appVersionCode < serverVersionCode) {
                            MyDialog.showVersionUpdateDialog(this, this, androidDetail.isMandatory());
                        } else {
                            proceedAfterSetUp(registerSetUpDetails);
                        }
                    } else {
                        proceedAfterSetUp(registerSetUpDetails);
                    }
                } else {
                    proceedAfterSetUp(registerSetUpDetails);
                }
                if(loginData!=null) {


                    UserDeviceDetail userDeviceDetail = EightfoldsUtils.buildUserDeviceDetail(this);
                    userDeviceDetail.setUserId(loginData.getUserId());

                    userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceIMEI(this));


                    String url = WingaConstants.USER_DEVICE_DETAILS_URL;
                    EightfoldsVolley.getInstance().makingJsonRequest(this, UserDeviceDetail.class, Request.Method.POST, url, userDeviceDetail);
                }

//
            } catch (Exception ex) {
                ex.printStackTrace();
                proceedAfterSetUp(registerSetUpDetails);
            }
        }
    }

    private void proceedAfterSetUp(Setup registerSetUpDetails) {

        EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
        accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, ACCESS_TOKEN);
        Utils.setAppLanguage(this);

        if (TextUtils.isEmpty(accessToken)) {

            goToLanguageSelectionActivity();

        } else {
            pushContentViewDetailsToServer();
            Intent intent;
            if (registerSetUpDetails.isGoLive()) {
                String firstGamePlayed = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.FIRST_GAME_PLAYED);
//                if (firstGamePlayed != null && firstGamePlayed.equalsIgnoreCase("true")) {
                Utils.sendPushRegId(SplashScreenActivity.this, SplashScreenActivity.this);
                callHomeWithAnim(new HomePageResponse());
//                } else {
//                    login();
//                }
            } else {
                intent = new Intent(SplashScreenActivity.this, WingaNotLiveActivity.class);
                if (!TextUtils.isEmpty(registerSetUpDetails.getGoLiveTime()))
                    intent.putExtra(Constants.GOLIVE_DATE, registerSetUpDetails.getGoLiveTime());
                if (!TextUtils.isEmpty(registerSetUpDetails.getGoLiveMessage()))
                    intent.putExtra(Constants.GOLIVE_MESSAGE, registerSetUpDetails.getGoLiveMessage());
                if (!TextUtils.isEmpty(registerSetUpDetails.getCurrentTime()))
                    intent.putExtra(Constants.CURRENT_SERVER_TIME, registerSetUpDetails.getCurrentTime());
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            Logg.i(TAG, "***Code>> " + commonResponse.getCode() + "  ,Message>> " + commonResponse.getMessage());
            Utils.handleCommonErrors(this, object);
        } else if (object instanceof String) {
            String message = (String) object;
            Logg.i(TAG, "***Message>> " + message);
            if (message.toLowerCase().contains("timedout") || message.toLowerCase().contains("socketexception") || message.toLowerCase().contains("connectexception")
                    || message.toLowerCase().contains("html")) {
                goToNoInternetActivity();
                Utils.handleCommonErrors(this, getString(R.string.check_internet));
            } else {
                Utils.handleCommonErrors(this, object);
            }
        }

    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type==R.id.noText) {

                if (registerSetUpDetails != null) {
                    proceedAfterSetUp(registerSetUpDetails);
                }

        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }






}



