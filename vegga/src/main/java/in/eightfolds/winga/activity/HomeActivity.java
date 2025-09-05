package in.eightfolds.winga.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
/*import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;*/

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import in.eightfolds.WingaApplication;
import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.fragment.AdBannerFragment;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.CategoryGameSession;
import in.eightfolds.winga.model.CategoryHomePageResponse;
import in.eightfolds.winga.model.GameSession;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.model.SpinAndWinResponse;
import in.eightfolds.winga.model.UserLoggedDevice;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ContentViewDetailsManager;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.ShowCaseUtils;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

//import com.google.android.gms.ads.rewarded.RewardedAd;

/**
 * Created by Swapnika on 13-Jun-18.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button yesterdayWinnersBtn;

    private ImageView playIv, profilePicIV, home;
    private RelativeLayout profileRL, pointsRL;
    private TextView pointTv, timeTv, leftForSessionTv;
    private CategoryHomePageResponse homePageResponse = new CategoryHomePageResponse();
    private Long profilePicId;
    private long publishTimeRemainingMillis;
    private long specialSessionStartTimeInMillis;
    private boolean specialSessionExistsInFuture = false;
    HomeRefreshBroadcastReceiver br;
    public static final String VIEW_NAME_LOGO_IMAGE = "detail:logo:image";
    private ImageView logoIv;
    private TextView gameMsgTv;
    private boolean sessionOnGoing = false;
    private Configuration configuration;
    private boolean fromSplash = false;
    private TextView timeTopTv;
    private LinearLayout contentLL;
    private TextView specialSessiontimeTv;
    private LinearLayout specialSessionLL;
    private AdBannerFragment adBannerFragment;
    private static final String AD_UNIT_ID = "ca-app-pub-3530294870311813/1954091489";
//    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean isUserEarnedReward = false;

    private final Handler mHandler = new Handler();

    private boolean fromNotification;
    private Notification notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.v(TAG, "**onCreate()");
            Utils.setAppLanguage(this);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_home);


          /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });*/


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                if (bundle.containsKey(Constants.HOME_DATA)) {
                    homePageResponse = (CategoryHomePageResponse) bundle.get(Constants.HOME_DATA);
                    Utils.saveAppFirstLoggedInDate(this, homePageResponse);
                }
                if (bundle.containsKey(Constants.FROM_SPLASH)) {
                    fromSplash = bundle.getBoolean(Constants.FROM_SPLASH);
                }
                if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                    fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
                }
                if (bundle.containsKey(Constants.DATA)) {
                    notification = (Notification) bundle.get(Constants.DATA);
                }
            }
            getCategoryHomePage();

            initialize(false);
            updateUserDevice();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getCategoryHomePage() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_CATEGORY_HOMEPAGE;
            String id = EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(), Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url = url.replace("{categoryId}", id);

            EightfoldsVolley.getInstance().makingStringRequest(this, CategoryHomePageResponse.class, Request.Method.GET, url);
        }


    }

/*    private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            isLoading = true;
            rewardedAd = new RewardedAd(this, "ca-app-pub-3940256099942544/5224354917");

            rewardedAd.loadAd(
                    new PublisherAdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
                            HomeActivity.this.isLoading = false;


                            showRewardedVideo();

                        }

                        @Override
                        public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                            // Ad failed to load.
                            playIv.setImageDrawable(getResources().getDrawable(R.drawable.fortheresults));
                            playIv.setEnabled(false);
                            EightfoldsVolley.getInstance().dismissProgress();

                            HomeActivity.this.isLoading = false;

                        }
                    });
        }
    }

    private void showRewardedVideo() {

        if (rewardedAd.isLoaded()) {
            EightfoldsVolley.getInstance().dismissProgress();
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.

                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.


                            if (isUserEarnedReward) {
                                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                                intent.putExtra(Constants.fromGoogleAdVideo, true);
                                intent.putExtra(Constants.earnAmountFromAdd, homePageResponse.getGoogleAdMobPerViewPoints());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            // User earned reward.
                            isUserEarnedReward = true;

                            // addCoins(rewardItem.getAmount());
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display
                            Toast.makeText(HomeActivity.this, "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    };
            rewardedAd.show(this, adCallback);
        }
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();

    }


    private void registerReceiver() {
        if (br == null) {
            br = new HomeActivity.HomeRefreshBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.HOME_REFRESH_ACTION);
            filter.addAction(Constants.HOME_LANGUAGE_REFRESH_ACTION);
            this.registerReceiver(br, filter);
        }
    }

    @Override
    protected void onResume() {
        try {
            unBlockTouchEvents();
            Logg.v(TAG, "***OnResume");
            super.onResume();
            if (!fromSplash) {
                showTutorial(sessionOnGoing);
            }
            if (!fromSplash) {
                long userId = Utils.getUserId(this);
                if (userId != 0) {
                    ContentViewDetailsManager.pushContentViewDetailsToServer(AppDataBase.getAppDatabase(this));
                    ContentViewDetailsManager.pushAdContentViewDetailsToServer(HomeActivity.this, Utils.getUserId(this), AppDataBase.getAppDatabase(this));
                    if (homePageResponse != null) {
                        getCategoryHomePage();
                    } else {
                        getCategoryHomePage();
                    }
                }
            } else {
                fromSplash = false;
            }
            if (configuration != null) {
                onConfigurationChanged(configuration);
            } else {
                setProfileImage();
            }

            if (homePageResponse != null) {
                updateHome();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logg.v(TAG, "**onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Logg.v(TAG, "**OnConfigChanged");
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_home);
        initialize(true);
        configuration = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (br != null) {
            unregisterReceiver(br);
        }
    }

    private void initialize(boolean fromConfigChange) {
        addBannerFragment();
        yesterdayWinnersBtn = findViewById(R.id.yesterdayWinnersBtn);
        playIv = findViewById(R.id.playIv);
        profilePicIV = findViewById(R.id.profilePicIV);
        profileRL = findViewById(R.id.profileRL);
        pointsRL = findViewById(R.id.pointsRL);
        pointTv = findViewById(R.id.pointTv);
        timeTv = findViewById(R.id.timeTv);
        leftForSessionTv = findViewById(R.id.leftForSessionTv);
        gameMsgTv = findViewById(R.id.gameMsgTv);
        logoIv = findViewById(R.id.logoIv);
        timeTopTv = findViewById(R.id.timeTopTv);
        contentLL = findViewById(R.id.contentLL);
        specialSessionLL = findViewById(R.id.specialSessionLL);
        specialSessiontimeTv = findViewById(R.id.specialSessiontimeTv);
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        playIv.setOnClickListener(this);
        profileRL.setOnClickListener(this);
        pointsRL.setOnClickListener(this);
        yesterdayWinnersBtn.setOnClickListener(this);
        logoIv.setOnClickListener(this);
        specialSessionLL.setOnClickListener(this);
        if (fromNotification && notification.getNotificationId() != null) {
            Utils.makeNotificationAsRead(this, notification.getNotificationId());
        }

        if (!fromConfigChange && fromSplash) {
            ViewCompat.setTransitionName(logoIv, VIEW_NAME_LOGO_IMAGE);
            if (homePageResponse != null) {
                updateHome();
            }

        } else {
            profileRL.setVisibility(View.VISIBLE);
            pointsRL.setVisibility(View.VISIBLE);
            if (homePageResponse != null) {
                updateHome();
                Utils.subscribeToAlertsPromosUpdates(homePageResponse.getUserDetail());
            }
            addBannerFragment();
            registerReceiver();
        }
    }

    private void getNewGameResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_NEW_GAME_URL;
            String id = EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(), Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url = url.replace("{categoryId}", id);

            EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home) {


            onBackPressed();
        } else if (view.getId() == R.id.specialSessionLL) {

            if (homePageResponse.getUpcomingSponsoredGameSessions() != null && homePageResponse.getUpcomingSponsoredGameSessions().size() > 0) {
                Intent intent = new Intent(this, SpecialSessionsListActivity.class);
                intent.putExtra(Constants.DATA, homePageResponse.getUpcomingSponsoredGameSessions());
                startActivity(intent);
            }
        } else if (view.getId() == R.id.logoIv) {
//                Intent tutorialIntent = new Intent(this, QuickTourActivity.class);
//                tutorialIntent.putExtra("isFromHomeActivity",1);
//                startActivity(tutorialIntent);
        } else if (view.getId() == R.id.playIv) {
            if (homePageResponse.getGameSessionMessage().getCode() == Constants.SPIN_AND_WIN_PENDING) {
                makeRequestForSpinANdWin(true);
//                    Intent startGameIntent = new Intent(HomeActivity.this, SpinAndWinActivity.class);
//                    startActivity(startGameIntent);
            } else if (homePageResponse.getGameSessionMessage().getCode() == 1017) {

                EightfoldsVolley.getInstance().showProgress(this);

//                    loadRewardedAd();

            } else if (sessionOnGoing && EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//                    Intent startGameIntent = new Intent(HomeActivity.this, StartGameActivity.class);
//                    startActivity(startGameIntent);
                getNewGameResponse();
            }

        } else if (view.getId() == R.id.profileRL) {
            try {
                blockUserEvents();
                Intent intent = new Intent(this, ProfileActivity.class);
                if (profilePicId != null && profilePicId.intValue() != 0) {
                    intent.putExtra("profilepic", profilePicId);
                }
                ActivityOptionsCompat activityOptions = makeSceneTransitionAnimation(
                        this,
                        new Pair<View, String>(profilePicIV
                                ,
                                ProfileActivity.VIEW_NAME_HEADER_IMAGE));
                ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
                setOnStop();
            } catch (Exception ex) {
                ex.printStackTrace();
                Intent intent = new Intent(this, ProfileActivity.class);
                if (profilePicId != null && profilePicId.intValue() != 0) {
                    intent.putExtra("profilepic", profilePicId);
                }
                startActivity(intent);
            }

        } else if (view.getId() == R.id.pointsRL) {
            try {
                blockUserEvents();
                Intent todayIntent = new Intent(this, LoyaltyPointsHistoryActivity.class);
                if (homePageResponse != null && homePageResponse.getUserDetail() != null && homePageResponse.getUserDetail().getTotalLoyalityPoints() != null) {
                    todayIntent.putExtra("points", homePageResponse.getUserDetail().getTotalLoyalityPoints());
                }
                ActivityOptionsCompat historyactivityOptions = makeSceneTransitionAnimation(
                        this);
                ActivityCompat.startActivity(this, todayIntent, historyactivityOptions.toBundle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (view.getId() == R.id.yesterdayWinnersBtn) {
            blockUserEvents();
            Intent winnersIntent = new Intent(this, RecentWinnersActivity.class);
            startActivity(winnersIntent);
        }
    }




//    private void getHomePageResponse(boolean showProgress) {
//
//        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//            String url = Constants.GET_HOME_PAGE_RESPONSE;
//            if (showProgress) {
//                EightfoldsVolley.getInstance().showProgress(this);
//            }
//            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
//            url = url.replace("{langId}", selectedLangId);
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
//        }
//    }

    private void makeRequestForSpinANdWin(boolean showProgress) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_PLAY_SPIN_AND_WIN_NEW;
            if (showProgress) {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url=url.replace("{categoryId}",id);
            EightfoldsVolley.getInstance().makingStringRequest(this, SpinAndWinResponse.class, Request.Method.GET, url);
        }
    }

    private void updateUserDevice() {
        String lastSavedTime = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.DEVICE_INFO_LAST_UPDATED_TIME);
        if (!TextUtils.isEmpty(lastSavedTime)) {
            long timeDifference = System.currentTimeMillis() - Long.parseLong(lastSavedTime);
            int twentyFourHours = 24 * 60 * 60 * 1000;
            if (timeDifference < twentyFourHours) {
                return;
            }
        }

        UserLoggedDevice userDeviceInfo = Utils.getUserDeviceInfo(this);
        EightfoldsVolley.getInstance().makingJsonRequest(this, CommonResponse.class, Request.Method.POST, Constants.UPDATE_DEVICE_INFO, userDeviceInfo);
    }

    private void showTutorial(boolean sessionOnGoing) {
        if (sessionOnGoing) {
            boolean isHomeTutorialShownInNoSession = EightfoldsUtils.getInstance().getBooleanFromSharedPreference(HomeActivity.this, Constants.IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION);
            if (isHomeTutorialShownInNoSession) {
                playIv.post(new Runnable() {
                    @Override
                    public void run() {
                        ShowCaseUtils.presentHomeShowcaseSequence(HomeActivity.this, playIv);
                    }
                });
            }
            playIv.post(new Runnable() {
                @Override
                public void run() {

                    ShowCaseUtils.presentHomeShowcaseSequence(HomeActivity.this, playIv, profileRL, pointsRL, yesterdayWinnersBtn);
                }
            });
        } else {
            profileRL.post(new Runnable() {
                @Override
                public void run() {
                    ShowCaseUtils.presentHomeShowcaseSequence(HomeActivity.this, null, profileRL, pointsRL, yesterdayWinnersBtn);
                }
            });
        }
    }

    private void showPrizeWinNotificationIfUnread() {
        boolean isLuckyDrawNotificationUnread = false;
//        if (homePageResponse.getNotification() != null && homePageResponse.getNotification().getNotificationId() != null && homePageResponse.getNotification().getType() == Notification.NOTIFICATION_TYPE_LUCKY_DRAW) {
//            if (!Utils.isNotificationIsRead(this, homePageResponse.getNotification())) {
//                isLuckyDrawNotificationUnread = true;
//            }
//        }
//
//        if (isLuckyDrawNotificationUnread) {
//            Intent intent = new Intent(this, WinLuckyDrawActivity.class);
//            try {
//                if (!TextUtils.isEmpty(homePageResponse.getNotification().getJsonData())) {
//                    PrizeWin prizeWin = (PrizeWin) Api.fromJson(homePageResponse.getNotification().getJsonData(), PrizeWin.class);
//                    intent.putExtra(Constants.OTHER_DATA, prizeWin);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            intent.putExtra(Constants.DATA, homePageResponse.getNotification());
//            intent.putExtra(Constants.FROM_NOTIFICATION, true);
//            startActivity(intent);
//        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        try {

            if (object instanceof CategoryHomePageResponse) {
                homePageResponse = (CategoryHomePageResponse) object;
                if (homePageResponse.getUserDetail() != null && homePageResponse.getUserDetail().getTotalLoyalityPoints() != null) {
                    EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOYALITY_POINTS, String.valueOf(homePageResponse.getUserDetail().getTotalLoyalityPoints().intValue()));
                    EightfoldsUtils.getInstance().saveToSharedPreference(this,Constants.CATEGORY_GAMESESSION_ID,String.valueOf(homePageResponse.getCategoryGameSession().getCgsId()));
                }
                String activityName = EightfoldsUtils.getInstance().getTopmostActivity(this);
                if ((activityName.equals("in.eightfolds.winga.activity.HomeActivity"))) {
                    setProfileImage();
                }
                updateHome();
                showPrizeWinNotificationIfUnread();

            } else if (object instanceof NewGameResponse) {
                NewGameResponse newGameResponse = (NewGameResponse) object;
                if (newGameResponse.getGameSessionMessage().getCode() == Constants.LUCKY_DRAW_NOT_ANNOUNCED_CODE) {

                    String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);

                    onVolleyErrorListener("No more  games available");



                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                }else{
                    (new UsageAnalytics()).trackPlayGame("", newGameResponse);
                    Logg.v("dash", "items count >> " + newGameResponse.getContents().size());


                    NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                    Intent intent = new Intent(this, VideoActivity.class);
                    intent.putExtra(Constants.DATA, filteredLangGameResp);
                    startActivity(intent);
                }

            } else if (object instanceof SpinAndWinResponse) {
                Intent startGameIntent = new Intent(HomeActivity.this, SpinAndWinActivity.class);
                startGameIntent.putExtra(Constants.DATA, (SpinAndWinResponse) object);
                startActivity(startGameIntent);
            } else if (object instanceof CommonResponse) {
                if (requestType.contains(Constants.UPDATE_DEVICE_INFO)) {
                    EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.DEVICE_INFO_LAST_UPDATED_TIME, String.valueOf(System.currentTimeMillis()));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void adjustPointsFontSizeAndBg() {

        String points = Integer.toString(homePageResponse.getUserDetail().getTotalLoyalityPoints().intValue());
        Utils.adjustPointsFontSizeAndBg(this, points, pointsRL, pointTv);
    }

    private void updateLevel() {
        if (homePageResponse.getUserDetail().getCurrentLevel() != null && homePageResponse.getUserDetail().getCurrentLevel() != 0) {
            Utils.setProfileBGBasedOnLevel(this, homePageResponse.getUserDetail().getCurrentLevel().intValue(), profileRL);
        }
        if (homePageResponse.getUserDetail() != null && homePageResponse.getUserDetail().getTotalLoyalityPoints() != null) {
            adjustPointsFontSizeAndBg();
        }
    }

    private void updateHome() {
        try {
            timeTv.setVisibility(View.VISIBLE);
            Logg.v(TAG, "updateHome()");
            setBottomAnimTry();

            if (homePageResponse != null) {
                if (homePageResponse.getUpcomingSponsoredGameSessions() != null && homePageResponse.getUpcomingSponsoredGameSessions().size() > 0) {
                    specialSessionLL.setVisibility(View.VISIBLE);
                    contentLL.setPadding(30, 0, 30, 5);
                } else {
                    specialSessionLL.setVisibility(View.GONE);
                    contentLL.setPadding(30, 20, 30, 5);
               }
                updateLevel();
                if (!fromSplash) {
                    setProfileImage();
                }
                if (homePageResponse.getGameSessionMessage().getCode() == Constants.NOEVENT_CODE) {
                    sessionOnGoing = false;
                    setScreenForNoSessionDeclared();
                } else if (homePageResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                    sessionOnGoing = false;
                    setSecreenForSessionAvailableInFuture(homePageResponse.getCategoryGameSession());
                } else if (homePageResponse.getGameSessionMessage().getCode() == Constants.LUCKY_DRAW_NOT_ANNOUNCED_CODE) {
                    sessionOnGoing = false;
                    setSecreenForWaitingForLuckyDraw(homePageResponse.getCategoryGameSession());
                } else if (homePageResponse.getGameSessionMessage().getCode() == Constants.PROMOTION_SESSION_NOT_STARTED) {
                    sessionOnGoing = false;
                    setScreenForPromitionUserSessionWillStartInFuture();
                } else if (homePageResponse.getGameSessionMessage().getCode() == Constants.SPIN_AND_WIN_PENDING) {
                    sessionOnGoing = false;
                    setScreenForSpinAndWin(homePageResponse.getCategoryGameSession());
                }else if(homePageResponse.getGameSessionMessage().getCode() == 1017){
                    sessionOnGoing=false;
                    setGoogleGameSession(homePageResponse.getCategoryGameSession());

                }

                else {
                    sessionOnGoing = true;

                    setScreenForSessionAvaialble(homePageResponse.getCategoryGameSession());
                }
                if (homePageResponse.getGameSessionMessage() != null && !TextUtils.isEmpty(homePageResponse.getGameSessionMessage().getMessage())) {
                    Spanned htmlAsSpanned = Html.fromHtml(homePageResponse.getGameSessionMessage().getMessage());
                    gameMsgTv.setText(htmlAsSpanned);
                } else {
                    gameMsgTv.setVisibility(View.GONE);
                }
                yesterdayWinnersBtn.setText(getResources().getString(R.string.yesterday_winner));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setScreenForSpinAndWin(CategoryGameSession gameSession) {
        playIv.setImageDrawable(getResources().getDrawable(R.drawable.spinand_win));
        playIv.setEnabled(true);
        if (homePageResponse.getPublishTimeRemainingMillis() == 0) {
            long publishTimeRemainingMinutes = Utils.getDifferenceBetweenDatesInMins(gameSession.getCurrentTime(), gameSession.getEndTime());
            publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(gameSession.getCurrentTime(), gameSession.getEndTime());

            leftForSessionTv.setVisibility(View.VISIBLE);
            if (publishTimeRemainingMinutes < 30) {
                setScreenForSessionAvaialbleFor30MinutesOnly();
            } else {
                timeTopTv.setVisibility(View.GONE);
                leftForSessionTv.setText(getString(R.string.left_for_today_draw));
            }
            startTimerFirstTime();
        }
    }

    private void setScreenForSessionAvaialble(CategoryGameSession gameSession) {
        timeTv.setVisibility(View.VISIBLE);
        leftForSessionTv.setVisibility(View.VISIBLE);
        if (homePageResponse.getCategoryGameSession().isSponsored()) {
            playIv.setImageDrawable(getResources().getDrawable(R.drawable.specialsession));
        } else {
            playIv.setImageDrawable(getResources().getDrawable(R.drawable.play_home));
        }
        playIv.setEnabled(true);
        if (homePageResponse.getPublishTimeRemainingMillis() == 0) {
            long publishTimeRemainingMinutes = Utils.getDifferenceBetweenDatesInMins(gameSession.getCurrentTime(), gameSession.getEndTime());
            publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(gameSession.getCurrentTime(), gameSession.getEndTime());

            leftForSessionTv.setVisibility(View.VISIBLE);
            if (publishTimeRemainingMinutes < 30) {
                setScreenForSessionAvaialbleFor30MinutesOnly();
            } else {
                timeTopTv.setVisibility(View.GONE);
                leftForSessionTv.setText(getString(R.string.left_for_today_draw));
            }
            startTimerFirstTime();
        }
    }

    private void setScreenForNoSessionDeclared() {
        playIv.setImageDrawable(getResources().getDrawable(R.drawable.comingsoon));
        playIv.setEnabled(false);
        timeTopTv.setVisibility(View.VISIBLE);
        timeTopTv.setText(getString(R.string.session_start_soon));
        timeTopTv.setTextSize(32);
        timeTv.setVisibility(View.GONE);
        leftForSessionTv.setVisibility(View.GONE);
    }

    private void setScreenForSessionAvaialbleFor30MinutesOnly() {
        timeTopTv.setVisibility(View.VISIBLE);
        timeTopTv.setText(getString(R.string.hurry_text));
        String locale = Utils.getCurrentLanguage(HomeActivity.this);
        if (locale.equalsIgnoreCase("en")) {
            timeTopTv.setTextSize(38);
            timeTv.setTextSize(38);
        } else {
            timeTopTv.setTextSize(20);
            timeTv.setTextSize(26);
        }
        leftForSessionTv.setText(getString(R.string.hurry_30mnts_left));
    }


    private void setScreenForPromitionUserSessionWillStartInFuture() {
        playIv.setImageDrawable(getResources().getDrawable(R.drawable.next_session_home));
        playIv.setEnabled(false);
        timeTopTv.setVisibility(View.GONE);

        if (homePageResponse.getCategoryGameSession().getStartTime() != null && !TextUtils.isEmpty(homePageResponse.getCategoryGameSession().getStartTime())) {
            String serverCurrenttime;
            String nextSessionStartTime = homePageResponse.getCategoryGameSession().getStartTime();
            if (homePageResponse != null && !TextUtils.isEmpty(homePageResponse.getCategoryGameSession().getCurrentTime())) {
                serverCurrenttime = homePageResponse.getCategoryGameSession().getCurrentTime();
            } else {
                serverCurrenttime = DateTime.getNowInUTC();
            }
            publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(serverCurrenttime, nextSessionStartTime);
            try {
                String formattedNextSessionTime = DateTime.getDateFromUTC(nextSessionStartTime, "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");
                leftForSessionTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.starts_on), formattedNextSessionTime));
                leftForSessionTv.setTextSize(16);
                leftForSessionTv.setVisibility(View.VISIBLE);
                startTimerFirstTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            findViewById(R.id.timeLL).setVisibility(View.GONE);
        }
    }

    private void setSecreenForSessionAvailableInFuture(CategoryGameSession gameSession) {
        Logg.v(TAG, "*$$* setSecreenForSessionAvailableInFuture ");
        playIv.setImageDrawable(getResources().getDrawable(R.drawable.next_session_home));
        playIv.setEnabled(true);
        timeTopTv.setVisibility(View.GONE);
        if (homePageResponse.getPublishTimeRemainingMillis() == 0) {
            if (homePageResponse.getNextGameSessionTime() != null && !TextUtils.isEmpty(homePageResponse.getNextGameSessionTime())) {
                String serverCurrenttime;
                String nextSessionStartTime = homePageResponse.getNextGameSessionTime();
                if (homePageResponse != null && !TextUtils.isEmpty(homePageResponse.getCurrentTime())) {
                    serverCurrenttime = homePageResponse.getCurrentTime();
                } else {
                    serverCurrenttime = DateTime.getNowInUTC();
                }
                publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(serverCurrenttime, nextSessionStartTime);
                try {
                    String formattedNextSessionTime = DateTime.getDateFromUTC(nextSessionStartTime, "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");
                    leftForSessionTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.starts_on), formattedNextSessionTime));
                    leftForSessionTv.setTextSize(16);
                    leftForSessionTv.setVisibility(View.VISIBLE);
                    startTimerFirstTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.timeLL).setVisibility(View.GONE);
            }
        }
    }
    private void setGoogleGameSession(CategoryGameSession gameSession){
        //if (homePageResponse.getGoogleAdMobViewCount() >= homePageResponse.getGoogleAdMobMaxViewCount()) {
//        playIv.setImageDrawable(getResources().getDrawable(R.drawable.fortheresults));
//        playIv.setEnabled(false);
//        } else {
        playIv.setImageDrawable(getResources().getDrawable(R.drawable.play_home));
           playIv.setEnabled(true);
//   }

        leftForSessionTv.setVisibility(View.GONE);
        if (homePageResponse.getUpcomingSponsoredGameSessions() != null && homePageResponse.getUpcomingSponsoredGameSessions().size() > 0) {
            contentLL.setPadding(30, 100, 30, 5);
        } else {
            contentLL.setPadding(30, 20, 30, 5);
        }


        if (homePageResponse.getPublishTimeRemainingMillis() == 0) {
            if (homePageResponse.getCategoryGameSession().getWinerPubTime() != null && !TextUtils.isEmpty(homePageResponse.getCategoryGameSession().getWinerPubTime())) {
                String luckyDrawAnnounceTime = homePageResponse.getCategoryGameSession().getWinerPubTime();

                try {
                    String serverCurrenttime;
                    if (gameSession != null && !TextUtils.isEmpty(homePageResponse.getCurrentTime())) {
                        serverCurrenttime = homePageResponse.getCurrentTime();
                    } else {
                        serverCurrenttime = DateTime.getNowInUTC();
                    }
                    if (!TextUtils.isEmpty(serverCurrenttime)) {
                        publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(serverCurrenttime, luckyDrawAnnounceTime);
                    }
                    timeTopTv.setVisibility(View.VISIBLE);
                    timeTopTv.setText(getString(R.string.today_results_in));
                    String locale = Utils.getCurrentLanguage(HomeActivity.this);
                    if (locale.equalsIgnoreCase("en")) {
                        timeTopTv.setTextSize(32);
                    } else {
                        timeTopTv.setTextSize(22);
                    }
                    startTimerFirstTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.timeLL).setVisibility(View.GONE);
            }
        }

    }

    private void setSecreenForWaitingForLuckyDraw(CategoryGameSession gameSession) {
        //if (homePageResponse.getGoogleAdMobViewCount() >= homePageResponse.getGoogleAdMobMaxViewCount()) {
            playIv.setImageDrawable(getResources().getDrawable(R.drawable.fortheresults));
            playIv.setEnabled(false);
//        } else {
//        playIv.setImageDrawable(getResources().getDrawable(R.drawable.play_home));
//           playIv.setEnabled(true);
//   }

        leftForSessionTv.setVisibility(View.GONE);
        if (homePageResponse.getUpcomingSponsoredGameSessions() != null && homePageResponse.getUpcomingSponsoredGameSessions().size() > 0) {
            contentLL.setPadding(30, 100, 30, 5);
        } else {
            contentLL.setPadding(30, 20, 30, 5);
      }


        if (homePageResponse.getPublishTimeRemainingMillis() == 0) {
            if (homePageResponse.getCategoryGameSession().getWinerPubTime() != null && !TextUtils.isEmpty(homePageResponse.getCategoryGameSession().getWinerPubTime())) {
                String luckyDrawAnnounceTime = homePageResponse.getCategoryGameSession().getWinerPubTime();

                try {
                    String serverCurrenttime;
                    if (gameSession != null && !TextUtils.isEmpty(homePageResponse.getCurrentTime())) {
                        serverCurrenttime = homePageResponse.getCurrentTime();
                    } else {
                        serverCurrenttime = DateTime.getNowInUTC();
                    }
                    if (!TextUtils.isEmpty(serverCurrenttime)) {
                        publishTimeRemainingMillis = Utils.getDifferenceBetweenDatesInMills(serverCurrenttime, luckyDrawAnnounceTime);
                    }
                    timeTopTv.setVisibility(View.VISIBLE);
                    timeTopTv.setText(getString(R.string.today_results_in));
                    String locale = Utils.getCurrentLanguage(HomeActivity.this);
                    if (locale.equalsIgnoreCase("en")) {
                        timeTopTv.setTextSize(32);
                    } else {
                        timeTopTv.setTextSize(22);
                    }
                    startTimerFirstTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.timeLL).setVisibility(View.GONE);
            }
        }
    }


    public void setProfileImage() {
        try {
            if (homePageResponse != null && homePageResponse.getUserDetail() != null) {
                if (homePageResponse.getUserDetail().getProfilePicId() != null && homePageResponse.getUserDetail().getProfilePicId().intValue() != 0) {
                    profilePicId = homePageResponse.getUserDetail().getProfilePicId();
                    Glide.with(getApplicationContext())
                            .load(EightfoldsUtils.getInstance().getImageFullPath(homePageResponse.getUserDetail().getProfilePicId(), Constants.FILE_URL))
                            .placeholder(R.drawable.ic_user_filled)
                            .error(R.drawable.ic_user_filled)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(profilePicIV);
                } else {
                    profilePicIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_filled));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


    private void startTimerFirstTime() {
        homePageResponse.setPublishTimeRemainingMillis(publishTimeRemainingMillis);
        if (homePageResponse.getUpcomingSponsoredGameSessions() != null && homePageResponse.getUpcomingSponsoredGameSessions().size() > 0) {
            specialSessionStartTimeInMillis = Utils.getDifferenceBetweenDatesInMills(homePageResponse.getUpcomingSponsoredGameSessions().get(0).getCurrentTime(), homePageResponse.getUpcomingSponsoredGameSessions().get(0).getStartTime());
            homePageResponse.setSpecialSessionStartInMillis(specialSessionStartTimeInMillis);
            specialSessionExistsInFuture = true;
        } else {
            specialSessionStartTimeInMillis = 0;
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.removeCallbacks(specialSessionTimerTask);
        setValuesOnTimerTick();
    }

    long timerTickIntervalInSeconds = 60;


    private long setFormattedTimeTextFromMillisToTextView(long mills, TextView textView) {
        long secondsRemaining = (mills / 1000);
        long minutesRemaining = secondsRemaining / 60;
        long hoursRemaining = minutesRemaining / 60;
        long finalMinutes = minutesRemaining % 60;
        long finalSeconds = secondsRemaining % (60 * 60);

        Logg.v(TAG, " finalMinutes>> " + finalMinutes + " finalSeconds>> " + finalSeconds);
        if (secondsRemaining % 60 != 0) {
            finalMinutes++;
        }
        String hours = hoursRemaining + "";
        String minutes = finalMinutes + "";
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String time;
        if (secondsRemaining < 60) {
            if (secondsRemaining == 1) {
                time = finalSeconds + " " + getString(R.string.sec_inshort_en);
            } else if (secondsRemaining > 1) {
                time = finalSeconds + " " + getString(R.string.seconds_inshort_en);
            } else {
                time = "1 " + getString(R.string.sec_inshort_en);
            }
        } else {
            if (hoursRemaining == 0) {
                if (finalMinutes == 1) {
                    time = minutes + " " + getString(R.string.min_inshort_en);
                } else {
                    time = minutes + " " + getString(R.string.minuts_inshort_en);
                }
            } else {
                String currentLanguage = Utils.getCurrentLanguage(this);
                if (currentLanguage.equalsIgnoreCase(Constants.TELUGU_LOCALE)) {
                    String text = getString(R.string.minuts_inshort_en);
                    if (!sessionOnGoing) {
                        text = getString(R.string.in_minutes);
                    }
                    time = hours + ":" + minutes + " " + text;
                } else {
                    time = hours + "h " + minutes + "m";
                }
            }
        }
        textView.setText(time);
        return secondsRemaining;
    }

    private void setValuesOnTimerTick() {
        long secondsRemaining = (publishTimeRemainingMillis / 1000);
        long minutesRemaining = secondsRemaining / 60;
        long hoursRemaining = minutesRemaining / 60;
        long finalMinutes = minutesRemaining % 60;
        long finalSeconds = secondsRemaining % (60 * 60);

        Logg.v(TAG, " finalMinutes>> " + finalMinutes + " finalSeconds>> " + finalSeconds);
        if (secondsRemaining % 60 != 0) {
            finalMinutes++;
        }

        String hours = hoursRemaining + "";
        String minutes = finalMinutes + "";
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String time;
        if (secondsRemaining < 60) {
            if (secondsRemaining == 1) {
                time = finalSeconds + " " + getString(R.string.sec_inshort_en);
            } else if (secondsRemaining > 1) {
                time = finalSeconds + " " + getString(R.string.seconds_inshort_en);
            } else {
                time = "1 " + getString(R.string.sec_inshort_en);
            }
        } else {
            if (hoursRemaining == 0) {
                if (finalMinutes == 1) {
                    time = minutes + " " + getString(R.string.min_inshort_en);
                } else {
                    time = minutes + " " + getString(R.string.minuts_inshort_en);
                }
            } else {

                String currentLanguage = Utils.getCurrentLanguage(this);
                if (currentLanguage.equalsIgnoreCase(Constants.TELUGU_LOCALE)) {
                    String text = getString(R.string.minuts_inshort_en);
                    if (!sessionOnGoing) {
                        text = getString(R.string.in_minutes);
                    }
                    time = hours + ":" + minutes + " " + text;
                } else {
                    time = hours + "h " + minutes + "m";
                }
            }
        }
        timeTv.setText(time);
        if (sessionOnGoing) {
            if (hoursRemaining == 0 && finalMinutes < 30) {
                setScreenForSessionAvaialbleFor30MinutesOnly();
            } else {
                timeTopTv.setVisibility(View.GONE);
            }
        }
        homePageResponse.setCurrentTime("");
        Logg.i("onTick", "Seconds remaining: " + secondsRemaining);

        if (secondsRemaining <= 60) {
            timerTickIntervalInSeconds = 1;
        } else if (secondsRemaining % 60 != 0) {
            timerTickIntervalInSeconds = (secondsRemaining % 60);
        } else {
            timerTickIntervalInSeconds = 60;
        }
        Logg.i(TAG, " Delay for >> " + timerTickIntervalInSeconds);
        if (secondsRemaining < 0) {
            getCategoryHomePage();
        } else {
            mHandler.postDelayed(mUpdateTimeTask, timerTickIntervalInSeconds * 1000);
        }

        Logg.v(TAG, "specialSessionStartTimeInMillis >> " + specialSessionStartTimeInMillis);
        if (specialSessionExistsInFuture && specialSessionStartTimeInMillis > 0) {
            Logg.v(TAG, "specialSessionStartTimeInMins >> " + specialSessionStartTimeInMillis / (60 * 1000));

            specialSessiontimeTv.setVisibility(View.VISIBLE);
            long specialSessionSecondsRemaining = setFormattedTimeTextFromMillisToTextView(specialSessionStartTimeInMillis, specialSessiontimeTv);
            if (specialSessionStartTimeInMillis > (timerTickIntervalInSeconds * 1000)) {

                specialSessionStartTimeInMillis = specialSessionStartTimeInMillis - (timerTickIntervalInSeconds * 1000);
                homePageResponse.setSpecialSessionStartInMillis(specialSessionStartTimeInMillis);
            } else {
                if (specialSessionSecondsRemaining < 0) {
                    getCategoryHomePage();
                } else {
                    mHandler.postDelayed(specialSessionTimerTask, 1 * 1000);
                }
            }
        } else {
            specialSessiontimeTv.setVisibility(View.GONE);
        }
    }

    Runnable specialSessionTimerTask = new Runnable() {
        public void run() {
            specialSessiontimeTv.setVisibility(View.VISIBLE);
            long specialSessionSecondsRemaining = setFormattedTimeTextFromMillisToTextView(specialSessionStartTimeInMillis, specialSessiontimeTv);
            if (specialSessionSecondsRemaining <= 0) {
                getCategoryHomePage();
            } else {
                specialSessionStartTimeInMillis = specialSessionStartTimeInMillis - (1 * 1000);
                homePageResponse.setSpecialSessionStartInMillis(specialSessionStartTimeInMillis);

                mHandler.postDelayed(specialSessionTimerTask, 1 * 1000);
            }
        }
    };


    Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            publishTimeRemainingMillis = publishTimeRemainingMillis - timerTickIntervalInSeconds * 1000;
            homePageResponse.setPublishTimeRemainingMillis(publishTimeRemainingMillis);
            setValuesOnTimerTick();
        }
    };


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finishAffinity();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    public class HomeRefreshBroadcastReceiver extends BroadcastReceiver {
        private final String TAG = HomeRefreshBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Constants.HOME_REFRESH_ACTION)) {
                Logg.v(TAG, "*** Home page RefreshBoardcast received");
                String activityName = EightfoldsUtils.getInstance().getTopmostActivity(HomeActivity.this);
                if ((activityName.equals("in.eightfolds.winga.activity.HomeActivity"))) {
                    getCategoryHomePage();
                }
            } else if (intent.getAction().equalsIgnoreCase(Constants.HOME_LANGUAGE_REFRESH_ACTION)) {
                Logg.v(TAG, "*** HOME_LANGUAGE_REFRESH_ACTION received");

                if (WingaApplication.getInstance().getConfiguration() != null) {
                    configuration = WingaApplication.getInstance().getConfiguration();
                } else {
                    configuration = Utils.setAppLanguage(HomeActivity.this);
                }
            }
        }
    }





    private void setBottomAnimTry() {
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        profileRL.startAnimation(animation1);

        Animation animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_right);
        pointsRL.startAnimation(animation2);

        if (homePageResponse != null) {
            //updateHome();
            ContentViewDetailsManager.pushContentViewDetailsToServer(AppDataBase.getAppDatabase(HomeActivity.this));
       //     Utils.sendPushRegId(HomeActivity.this, HomeActivity.this);
            Utils.subscribeToAlertsPromosUpdates(homePageResponse.getUserDetail());
            showTutorial(sessionOnGoing);
            setProfileImage();
            addBannerFragment();
            registerReceiver();

            profileRL.setVisibility(View.VISIBLE);
        }
        fromSplash = false;
    }

    private void blockUserEvents() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unBlockTouchEvents() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void addBannerFragment() {
        adBannerFragment = new AdBannerFragment();
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        findViewById(R.id.addBannerFL).setVisibility(View.GONE);
        fragTransaction.replace(R.id.addBannerFL, adBannerFragment, "adbanner_frag");
        fragTransaction.commitAllowingStateLoss();
    }
}

