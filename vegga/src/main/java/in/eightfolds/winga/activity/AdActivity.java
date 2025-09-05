package in.eightfolds.winga.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import in.eightfolds.WingaApplication;
import in.eightfolds.circleprogress.CircleProgress;
import in.eightfolds.reveal.RevealLayout;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.model.DurationDetails;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Config;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

public class AdActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {

    private static String TAG = AdActivity.class.getSimpleName();
    private Activity myContext;
    private YouTubePlayer YPlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RevealLayout mRevealLayout;
    private int[] lastTouchDownXY = new int[2];
    private ImageView questionIv, closeVideoIv;
    private RelativeLayout mainRL;
    private CountDownTimer countDownTimer;
    private LinearLayout videoProgressLL;
    private TextView currentVideoProgressTv, totalVideoDurationTv, timeCounterTv;
    private long durationDetailsId;
    private AppDataBase appDataBase;
    private long uniqueViewId;
    private boolean pausedManually = false;
    private int currentPositionMills = 0;
    private LinearLayout processingLL;
    private FrameLayout youtube_fragment;
    private Timer progressTimer;
    private RelativeLayout shopNowLL;

    private int progress;
    private CircleProgress circleProgress;
    private boolean isUnexpectedYoutubeErrorOccurred = false;
    private int retryCount = 1;
    private String formattedDuration;
    private boolean isRunning = false;
    private boolean isImageLoaded = false;
    private HomePageAd homePageAd;
    private long uniqueAddId;
    private long imageViewSeconds = Constants.imageViewSec;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            myContext = this;
            setContentView(R.layout.layout_ad_activity);
            if (Constants.captureDurationDetails) {
                appDataBase = AppDataBase.getAppDatabase(myContext);
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constants.ADD_DETAILS)) {
                    homePageAd = (HomePageAd) bundle.get(Constants.ADD_DETAILS);
                }

                if (bundle.containsKey("xy")) {
                    lastTouchDownXY = bundle.getIntArray("xy");
                    if (lastTouchDownXY != null && lastTouchDownXY.length == 2) {
                        Logg.v(TAG, "*** X >>> " + lastTouchDownXY[0]);
                        Logg.v(TAG, "*** Y >>> " + lastTouchDownXY[1]);
                    }
                }
            }

            uniqueAddId = System.currentTimeMillis();


            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialize() {
        try {
            mRevealLayout = findViewById(R.id.reveal_layout);
            questionIv = findViewById(R.id.questionIv);
            closeVideoIv = findViewById(R.id.closeVideoIv);
            processingLL = findViewById(R.id.processingLL);
            youtube_fragment = findViewById(R.id.youtube_fragment);
            circleProgress = findViewById(R.id.circle_progress);
            mainRL = findViewById(R.id.mainRL);
            videoProgressLL = findViewById(R.id.videoProgressLL);
            currentVideoProgressTv = findViewById(R.id.currentVideoProgressTv);
            totalVideoDurationTv = findViewById(R.id.totalVideoDurationTv);
            timeCounterTv = findViewById(R.id.timeCounterTv);
            shopNowLL = findViewById(R.id.shopNowLL);

            closeVideoIv.setOnClickListener(this);
            mainRL.setOnClickListener(this);
            shopNowLL.setOnClickListener(this);
            setUpGame();
            addBannerFragment();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setUpGame() {
        try {
            setProgressTimer();
            if (YPlayer != null) {
                YPlayer.release();
                YPlayer = null;
            }
            mainRL.setBackgroundColor(myContext.getResources().getColor(R.color.colorBlack));
            if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                questionIv.setVisibility(View.VISIBLE);
                if (homePageAd.getFileId() != null) {
                    processingLL.setVisibility(View.GONE);
                    videoProgressLL.setVisibility(View.GONE);
                    myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    loadPhoto(homePageAd.getFileId(), questionIv);
                }

            } else {
                shopNowLL.setVisibility(View.VISIBLE);
                initializeYouTube();
                questionIv.setVisibility(View.GONE);
            }
            setUniqueViewId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeYouTube() {
        try {
            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
            youTubePlayerFragment.initialize(Config.DEVELOPER_KEY, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setContentDurationDetails() {
        try {
            if (Constants.captureDurationDetails) {
                DurationDetails durationDetails = new DurationDetails();
                durationDetails.setHomePageAddId(homePageAd.getHomePageAddId());
                durationDetails.setAdUniqueId(uniqueAddId);
                durationDetails.setIsAdDetail(1);
                UUID uniqueId = UUID.randomUUID();
                durationDetails.setUniqueUuid(uniqueId);
                if (YPlayer != null && homePageAd.getType() != Constants.IMAGE_CONTENT_ID) {
                    durationDetails.setStartMillis(YPlayer.getCurrentTimeMillis());
                } else if (homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                    durationDetails.setStartMillis(System.currentTimeMillis());
                }
                durationDetails.setVideoStartUniqueId(uniqueViewId);
                durationDetails.setGameId(0);
                durationDetailsId = appDataBase.getDurationDetailsDao().insert(durationDetails);
                durationDetails.setId(durationDetailsId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateContentDurationDetails() {
        try {
            if (Constants.captureDurationDetails) {
                Log.v(TAG, "**durationDetailsId>> " + durationDetailsId);
                DurationDetails durationDetails = appDataBase.getDurationDetailsDao().getDurationDetailsForId(durationDetailsId);
                if (durationDetails != null) {
                    long startMills = durationDetails.getStartMillis();
                    long endMills = 0;
                    if (YPlayer != null && homePageAd.getType() != Constants.IMAGE_CONTENT_ID) {
                        endMills = YPlayer.getCurrentTimeMillis();
                    } else if (homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                        endMills = System.currentTimeMillis();
                    }
                    durationDetails.setEndMills(endMills);
                    long activeMillis = endMills - startMills;
                    durationDetails.setActiveMills(activeMillis);
                    appDataBase.getDurationDetailsDao().update(durationDetails);

                    durationDetailsId = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Logg.v(TAG, "**Video Fragment onPause()");
        pause();


        closeTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            if (YPlayer != null) {
                YPlayer.release();
            }
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onResume() {
        try {
            super.onResume();
            Logg.v(TAG, "**onResume()");
            if (isImageLoaded && homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                setContentDurationDetails();
                setTimer();
            }
            resume();

            if (isUnexpectedYoutubeErrorOccurred) {
                initializeYouTube();
                isUnexpectedYoutubeErrorOccurred = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void pause() {
        try {
            if (YPlayer != null) {
                pausedManually = true;
                currentPositionMills = YPlayer.getCurrentTimeMillis();
                YPlayer.pause();
            }
            if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                updateContentDurationDetails();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                updateContentDurationDetails();
            }
        }
    }

    private void resume() {
        try {
            if (YPlayer != null && pausedManually && currentPositionMills < YPlayer.getDurationMillis()) {
                YPlayer.release();

                initializeYouTube();
                pausedManually = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void onBackPressed(Animation.AnimationListener listener) {
        try {
            if (YPlayer != null) {
                YPlayer.pause();
            }
            mRevealLayout.hide(lastTouchDownXY[0], lastTouchDownXY[1], listener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVideo(int millis) {
        try {
            if (homePageAd.getYoutubeId() != null) {
                String videoID = homePageAd.getYoutubeId();
                YPlayer.loadVideo(videoID, millis);
                currentPositionMills = 0;
                myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            } else {
                goHome();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    int imageReloadCount = 0;

    private void loadPhoto(final long fileId, final ImageView imageView) {
        try {
            Logg.v(TAG, "IMG >> " + Constants.FILE_URL + fileId);
            Glide.with(WingaApplication.applicationContext)
                    .asBitmap()
                    .load(Constants.FILE_URL + fileId)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                imageView.setImageBitmap(resource);
                                isImageLoaded = true;
                                setContentDurationDetails();
                                setTimer();
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            goHome();

                        }
                    });

            imageReloadCount++;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setUniqueViewId() {
        if (Constants.captureDurationDetails) {
            uniqueViewId = System.currentTimeMillis();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.closeVideoIv) {
                goHome();
            } else if (view.getId() == R.id.mainRL || view.getId() == R.id.shopNowLL) {
                if (homePageAd != null && homePageAd.getHomePageAddId() > 0 && !TextUtils.isEmpty(homePageAd.getRedirectLink())) {
                    LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.LOGIN_DATA, LoginData.class);
                    if (loginData != null) {
                        String url = Constants.BUY_HOME_CONTENT_URL;
                        url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
                        url = url.replace("{homePageAddId}", String.valueOf(homePageAd.getHomePageAddId()));
                        Utils.openInBrowser(this, url);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void closeTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    long timeMillis;

    private int skipInsec = 5;

    private void setTimer() {
        try {
            closeTimer();

            if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                timeMillis = imageViewSeconds * 1000;
            } else {
                formattedDuration = getTimeFromMillis(YPlayer.getDurationMillis());
                totalVideoDurationTv.setText(formattedDuration);


                timeMillis = YPlayer.getDurationMillis() - YPlayer.getCurrentTimeMillis();
                Logg.i("onTick", "Video Timer >> " + timeMillis);
            }

            countDownTimer = new CountDownTimer((timeMillis), 1000) {
                public void onTick(long millisUntilFinished) {
                    isRunning = true;

                    if (skipInsec <= 0) {
                        closeVideoIv.setVisibility(View.VISIBLE);
                    }


                    if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                        skipInsec--;
                        int sec = (int) (millisUntilFinished / 1000) + 1;
                        imageViewSeconds = sec;
                        timeCounterTv.setText(String.format(getString(R.string.game_starts_in_sec), sec));

                    } else {
                        if (YPlayer != null) {
                            skipInsec--; //= 5 - (int)(YPlayer.getCurrentTimeMillis()/1000);
                            long remainingMillis = YPlayer.getDurationMillis() - YPlayer.getCurrentTimeMillis();
                            int sec = (int) remainingMillis / 1000;
                            timeCounterTv.setText(String.format(getString(R.string.game_starts_in_sec), sec));

                            Logg.v(TAG, "skipInsec>> " + skipInsec + " , remainint sec>> " + sec);
                            String time = getTimeFromMillis(YPlayer.getCurrentTimeMillis());
                            currentVideoProgressTv.setText(time);
                        }
                    }
                }

                public void onFinish() {
                    isRunning = false;
                    if (homePageAd != null && homePageAd.getType() == Constants.IMAGE_CONTENT_ID) {
                        goHome();
                    } else {

                        currentVideoProgressTv.setText(formattedDuration);
                    }
                }
            }.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getTimeFromMillis(long millis) {
        try {
            long secondsRemaining = (millis / 1000);
            long minutesRemaining = secondsRemaining / 60;
            long hoursRemaining = minutesRemaining / 60;

            long finalminutes = minutesRemaining % 60;
            long finalSeconds = secondsRemaining % 60;

            String hours = hoursRemaining + "";
            if (hours.length() == 1) {
                hours = "0" + hours;
            }

            String minutes = finalminutes + "";
            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }

            String seconds = finalSeconds + "";
            if (seconds.length() == 1) {
                seconds = "0" + seconds;
            }


            String time = "";
            if (hoursRemaining > 0) {
                time += hours;
            }
            time += minutes + " : " + seconds;
            return time;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private void goHome() {
        Utils.deleteAdInSetupAfterViewing(this);
        Intent intent = new Intent(AdActivity.this, V2HomeFeatureActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean onRestored) {
        try {
            if (!onRestored) {
                Logg.v(TAG, "**onInitializationSuccess() ");
                YPlayer = youTubePlayer;

                if (Constants.isForTestingVegga2) {
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                } else {
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                }
                YPlayer.setShowFullscreenButton(true);
                YPlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                if (currentPositionMills != 0) {
                    loadVideo(currentPositionMills);
                } else {
                    loadVideo(0);
                }
                YPlayer.play();


                YPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPaused() {
                        if (YPlayer != null) {
                            Logg.v(TAG, "**onPaused()  >>  " + YPlayer.getCurrentTimeMillis());
                            updateContentDurationDetails();
                            closeTimer();
                            currentPositionMills = YPlayer.getCurrentTimeMillis();
                        }
                    }

                    @Override
                    public void onPlaying() {

                        if (YPlayer != null) {
                            Logg.v(TAG, "**onPlaying() >>  " + YPlayer.getCurrentTimeMillis());
                            if (YPlayer.getCurrentTimeMillis() == 0) {
                                performOnVideoStarted();
                            }
                            setTimer();
                        }
                    }

                    @Override
                    public void onStopped() {
                        // Logg.v(TAG, "**onStopped()");
                        if (YPlayer != null) {
                            Logg.v(TAG, "**onPaused()  >>  " + YPlayer.getCurrentTimeMillis());
                            updateContentDurationDetails();
                        }
                    }

                    @Override
                    public void onBuffering(boolean b) {

                    }

                    @Override
                    public void onSeekTo(int i) {
                        Logg.v(TAG, "**onSeekTo() >>  ");
                    }
                });
                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        Logg.v(TAG, "**onLoading() >>  ");
                    }

                    @Override
                    public void onLoaded(String s) {
                        Logg.v(TAG, "**onLoaded() >>  ");
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                        Logg.v(TAG, "**onVideoStarted() >>  ");

                        performOnVideoStarted();
                    }

                    @Override
                    public void onVideoEnded() {
                        Logg.v("Video", "Video Ended >> ");
                        currentPositionMills = 0;
                        goHome();
                        myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                        try {
                            Logg.v(TAG, "**Player Error () >> " + errorReason.name());
                            if (progressTimer != null)
                                progressTimer.cancel();
                            youtube_fragment.setVisibility(View.VISIBLE);
                            circleProgress.setVisibility(View.GONE);

                            if (errorReason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                                // When this error occurs the player is released and can no longer be used.
                                isUnexpectedYoutubeErrorOccurred = true;
                                if (YPlayer != null) {
                                    YPlayer.release();
                                    YPlayer = null;
                                }
                                if (retryCount == 1)
                                    MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                goHome();

                            } else if (errorReason == YouTubePlayer.ErrorReason.NOT_PLAYABLE || errorReason == YouTubePlayer.ErrorReason.UNKNOWN) {


                                if (retryCount == 1)
                                    MyDialog.showToast(myContext, getString(R.string.not_able_to_play));
                                goHome();

                            } else if (errorReason == YouTubePlayer.ErrorReason.INTERNAL_ERROR ||
                                    errorReason == YouTubePlayer.ErrorReason.PLAYER_VIEW_TOO_SMALL ||
                                    errorReason == YouTubePlayer.ErrorReason.UNAUTHORIZED_OVERLAY ||
                                    errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_RESTRICTED_CONTENT ||
                                    errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_HIGH_BANDWIDTH) {
                                isUnexpectedYoutubeErrorOccurred = true;
                                if (YPlayer != null) {
                                    YPlayer.release();
                                    YPlayer = null;
                                }
                                if (retryCount == 1)
                                    MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                goHome();

                            } else if (errorReason == YouTubePlayer.ErrorReason.NETWORK_ERROR) {
                                // MyDialog.showToast(myContext, getString(R.string.no_internet));
                                // myContext.finish();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void performOnVideoStarted() {
        try {
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            setContentDurationDetails();
            setTimer();
            if (processingLL.getVisibility() == View.VISIBLE) {
                processingLL.setVisibility(View.GONE);
                if (progressTimer != null)
                    progressTimer.cancel();
            }
            youtube_fragment.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
        Logg.v(TAG, "Youtube Initialization error >> isUserRecoverableError : " + arg1.isUserRecoverableError());

        try {
            if (arg1.equals(YouTubeInitializationResult.SERVICE_MISSING) ||
                    arg1.equals(YouTubeInitializationResult.SERVICE_DISABLED) ||
                    arg1.equals(YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED)) {
                Dialog dialog = arg1.getErrorDialog(myContext, VideoActivity.YOUTUBE_ERROR_DIALOG_CODE, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MyDialog.showToast(myContext, getString(R.string.youtube_init_error));
                        goHome();
                    }
                });
                dialog.show();
            } else if (arg1.equals(YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE)) {

            }

            if (progressTimer != null)
                progressTimer.cancel();
            youtube_fragment.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setProgressTimer() {
        try {
            circleProgress.setFinishedColor(myContext.getResources().getColor(R.color.yellow_header_color));
            progressTimer = new Timer();
            progressTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    myContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                boolean a = false;
                                if (progress == 100) {
                                    progress = 0;
                                } else {
                                    progress = progress + 10;
                                }
                                circleProgress.setProgress(progress);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    });
                }
            }, 0, 100);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setImagePropertiesInPortraitMode() {
    }

    public void setImagePropertiesInLandscapeMode() {
    }

    private void addBannerFragment() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logg.v(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);

        Utils.setAppLanguage(myContext);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setImagePropertiesInLandscapeMode();
        } else {
            setImagePropertiesInPortraitMode();
        }
    }
}


