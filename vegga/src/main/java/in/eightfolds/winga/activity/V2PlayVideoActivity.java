package in.eightfolds.winga.activity;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Timer;
import java.util.TimerTask;

import in.eightfolds.circleprogress.CircleProgress;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.reveal.RevealLayout;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Config;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

public class V2PlayVideoActivity extends BaseActivity implements View.OnClickListener
        , YouTubePlayer.OnFullscreenListener, YouTubePlayer.OnInitializedListener,
        VolleyResultCallBack, OnEventListener {
    private static String TAG = V2PlayVideoActivity.class.getSimpleName();

    private RevealLayout mRevealLayout;

    private RelativeLayout mainRL;
    private ImageView closeVideoIv, backVideoIv, shareIv;
    public YouTubePlayer YPlayer;
    private LinearLayout processingLL;
    private FrameLayout youtube_fragment;
    private Timer progressTimer;
    private int progress;
    private CircleProgress circleProgress;
    private Activity myContext;
    private int currentPositionMills = 0;
    private CountDownTimer countDownTimer;
    boolean isRunning = false;
    private String formattedDuration;
    TextView currentVideoProgressTv, totalVideoDurationTv;
    private ProgressBar videoDurationProgressBar;

    private LinearLayout videoProgressLL;
    private LinearLayout replayMainLay, replayLL;
    private boolean isUnexpectedYoutubeErrorOccurred = false;
    private boolean pausedManually = false;
    private int retryCount = 1;
    private StreamingWinResponse streamingWinResponse;

    private int setUpGameOnce=0;

    private Dialog wonDialog;


    @Override
    public void onFullscreen(boolean b) {
//        if (b) {
//            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        }
//        YPlayer.setFullscreen(b);
    }

    private void closeTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }

    @Override
    public void onBackPressed() {

        finishOrGoHome();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        if (id == R.id.closeVideoIv || id == R.id.backVideoIv) {
            if (YPlayer != null) {
                pause();
            }
            myContext.onBackPressed();
        } else if (id == R.id.replayLL) {
            youtube_fragment.setVisibility(View.VISIBLE);
            setProgressTimer();
            replayMainLay.setVisibility(View.GONE);
            setUpGame();
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


    @Override
    public void onPause() {
        Logg.v(TAG, "**Video Fragment onPause()");
        pause();

        closeTimer();
        super.onPause();
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
                YPlayer = null;
            }
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

            resume();

            if (isUnexpectedYoutubeErrorOccurred && !streamingWinResponse.getYoutubeId().equals("")) {
                initializeYouTube();
                isUnexpectedYoutubeErrorOccurred = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pause() {
        try {
            if (YPlayer != null && !streamingWinResponse.getYoutubeId().equals("")) {
                pausedManually = true;
                currentPositionMills = YPlayer.getCurrentTimeMillis();
                YPlayer.pause();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void myPlayYouTube() {
        try {
            if (YPlayer != null && !YPlayer.isPlaying() && !streamingWinResponse.getYoutubeId().equals("")) {
                YPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resume() {
        try {
            if (YPlayer != null && pausedManually && currentPositionMills < YPlayer.getDurationMillis() && !streamingWinResponse.getYoutubeId().equals("")) {
                YPlayer.release();

                setUpGame();
                pausedManually = false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTimer() {
        try {
            formattedDuration = getTimeFromMillis(YPlayer.getDurationMillis());
            totalVideoDurationTv.setText(formattedDuration);
            videoDurationProgressBar.setVisibility(View.GONE); //make visible

            closeTimer();
            final long timeMillis = YPlayer.getDurationMillis() - YPlayer.getCurrentTimeMillis();
            Logg.i("onTick", "Video Timer >> " + timeMillis);

            // long countDownInterval = YPlayer.getDurationMillis() /  100;

            countDownTimer = new CountDownTimer((timeMillis), 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        isRunning = true;

                        if (YPlayer != null) {
                            String time = getTimeFromMillis(YPlayer.getCurrentTimeMillis());
                            currentVideoProgressTv.setText(time);
                            int percent = (YPlayer.getCurrentTimeMillis() * 100) / YPlayer.getDurationMillis();
                            videoDurationProgressBar.setProgress(percent);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                public void onFinish() {
                    isRunning = false;
                    currentVideoProgressTv.setText(formattedDuration);
                    videoDurationProgressBar.setProgress(100);
                }
            }.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void performOnVideoStarted() {
        try {
            Logg.v(TAG, "*performOnVideoStarted");
            //   myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean onRestored) {
        try {
            if (!onRestored) {
                Logg.v(TAG, "**onInitializationSuccess() ");
                YPlayer = youTubePlayer;


                if (Constants.isForTestingVegga2) {
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT); //CHROMELESS);//MINIMAL
                } else {
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL); //CHROMELESS);//MINIMAL
                }
                YPlayer.setShowFullscreenButton(true);
                // YPlayer.setFullscreen(true);
                //  YPlayer.setFullscreenControlFlags(1);
                YPlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                // YPlayer.setFullscreen(true);
                if (currentPositionMills != 0) {
                    loadVideo(currentPositionMills);
                } else {
                    loadVideo(0);
                }
              //  YPlayer.play();

                YPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {

                    @Override
                    public void onPaused() {
                        if (YPlayer != null && !streamingWinResponse.getYoutubeId().equals("")) {
                            Logg.v(TAG, "**onPaused()  >>  " + YPlayer.getCurrentTimeMillis());

                            closeTimer();
                            currentPositionMills = YPlayer.getCurrentTimeMillis();
                        }
                    }

                    @Override
                    public void onPlaying() {

                        if (YPlayer != null && !streamingWinResponse.getYoutubeId().equals("")) {
                            Logg.v(TAG, "**onPlaying() >>  " + YPlayer.getCurrentTimeMillis());

                        }
                    }

                    @Override
                    public void onStopped() {
                        // Logg.v(TAG, "**onStopped()");
                        if (YPlayer != null) {
                            Logg.v(TAG, "**onStopped()  >>  " + YPlayer.getCurrentTimeMillis());


                        }
                    }


                    @Override
                    public void onBuffering(boolean b) {
                        Logg.v(TAG, "**onSeekTo() >>  ");
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

                        if(YPlayer!=null){
                            performOnVideoStarted();
                        }
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                        Logg.v(TAG, "**onVideoStarted() >>  ");
                        // performOnVideoStarted();


                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onVideoEnded() {
                        try {
                            Logg.v("Video", "Video Ended >> ");
                            // youtubeLinearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                            currentPositionMills = 0;


                            videoProgressLL.setVisibility(View.GONE);
                            replayMainLay.setVisibility(View.GONE);
                            removeYoutubeFragmentAndSetup();
                            // myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            streamingWinResponse.setYoutubeId("");
                            moveToNextPage();


                            closeTimer();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                        try {
                            Logg.v(TAG, "**Player Error () >> " + errorReason.name());
                            if (progressTimer != null)
                                progressTimer.cancel();
                            youtube_fragment.setVisibility(View.VISIBLE);
                            circleProgress.setVisibility(View.GONE);

                            if (errorReason == YouTubePlayer.ErrorReason.INTERNAL_ERROR ||
                                    errorReason == YouTubePlayer.ErrorReason.PLAYER_VIEW_TOO_SMALL ||
                                    errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_RESTRICTED_CONTENT ||
                                    errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_HIGH_BANDWIDTH ||
                                    errorReason == YouTubePlayer.ErrorReason.NETWORK_ERROR ||
                                    errorReason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                                // When this error occurs the player is released and can no longer be used.
                                isUnexpectedYoutubeErrorOccurred = true;
                                if (YPlayer != null) {
                                    YPlayer.release();

                                    YPlayer = null;
                                }
                                if (retryCount == 1) {
                                    if (errorReason == YouTubePlayer.ErrorReason.INTERNAL_ERROR) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.PLAYER_VIEW_TOO_SMALL) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.UNAUTHORIZED_OVERLAY) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_RESTRICTED_CONTENT) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.USER_DECLINED_HIGH_BANDWIDTH) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.NETWORK_ERROR) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    } else if (errorReason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                                    }

                                }
                                finishOrGoHome();
                            } else if (errorReason == YouTubePlayer.ErrorReason.NOT_PLAYABLE || errorReason == YouTubePlayer.ErrorReason.UNKNOWN) {


                                if (retryCount == 1)
                                    MyDialog.showToast(myContext, getString(R.string.not_able_to_play));
                                finishOrGoHome();

                            } else {
                                if (YPlayer != null) {
                                    YPlayer.play();

                                }

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



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult arg1) {
        Logg.v(TAG, "Youtube Initialization error >> isUserRecoverableError : " + arg1.isUserRecoverableError());

        try {
            if (arg1.equals(YouTubeInitializationResult.SERVICE_MISSING) ||
                    arg1.equals(YouTubeInitializationResult.SERVICE_DISABLED) ||
                    arg1.equals(YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED)) {
                Dialog dialog = arg1.getErrorDialog(myContext, VideoActivity.YOUTUBE_ERROR_DIALOG_CODE, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MyDialog.showToast(myContext, getString(R.string.youtube_init_error));
                        finishOrGoHome();
                    }
                });
                dialog.show();
            } else if (arg1.equals(YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE)) {
         /*  Intent intent = YouTubeStandalonePlayer.createVideoIntent(myContext, Config.DEVELOPER_KEY, currentContent.getYoutubeVid());
            startActivity(intent);
            myContext.finish(); //to exit current Activity in which YouTubeFragment is not working*/

            }

            if (progressTimer != null)
                progressTimer.cancel();
            youtube_fragment.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof CommonServerResponse) {
            CommonServerResponse commonServerResponse = (CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());

          //  if (TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())) {
            initialize(true);
           // }


        }

        if (object instanceof StreamingWinResponse) {
            streamingWinResponse = (StreamingWinResponse) object;

           // removeYoutubeFragmentAndSetup();
            initialize(true);
            //myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            setUpGame();


            //finish();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }


    public void moveToNextPage() {
        Intent intent;
        if (streamingWinResponse.isShowPopUp()) {
            intent = new Intent(this, V2StreamingShowPopupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(this, V2StreamingWonDetailActivity.class);

        }
        intent.putExtra(Constants.DATA, streamingWinResponse);
        this.startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_play_video);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                streamingWinResponse = (StreamingWinResponse) bundle.get(Constants.DATA);
            }
        }
        initialize(true);



    }

    private void initialize(boolean b) {

        try {
            mRevealLayout = findViewById(R.id.reveal_layout);
            mainRL = findViewById(R.id.mainRL);
            closeVideoIv = findViewById(R.id.closeVideoIv);
            shareIv = findViewById(R.id.shareIv);
            backVideoIv = findViewById(R.id.backVideoIv);
            processingLL = findViewById(R.id.processingLL);
            youtube_fragment = findViewById(R.id.youtube_fragment);
            circleProgress = findViewById(R.id.circle_progress);
            closeVideoIv.setOnClickListener(this);
            shareIv.setOnClickListener(this);
            backVideoIv.setOnClickListener(this);
            videoProgressLL = findViewById(R.id.videoProgressLL);
            videoDurationProgressBar = findViewById(R.id.videoDurationProgressBar);
            currentVideoProgressTv = findViewById(R.id.currentVideoProgressTv);
            totalVideoDurationTv = findViewById(R.id.totalVideoDurationTv);
            myContext = V2PlayVideoActivity.this;
            replayMainLay = findViewById(R.id.replayMainLay);
            replayLL = findViewById(R.id.replayLL);
            replayLL.setOnClickListener(this);
            replayMainLay.setOnClickListener(this);
            if(setUpGameOnce==0){
                setUpGameOnce=1;
                setUpGame();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpGame() {
        try {
            if (!streamingWinResponse.getYoutubeId().equals("")) {
                setProgressTimer();
                if (YPlayer != null) {
                    YPlayer.release();
                    YPlayer = null;
                }

                initializeYouTube();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    YouTubePlayerSupportFragment youTubePlayerFragment;

    private void initializeYouTube() {
        try {
            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            int commit = transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();
            youTubePlayerFragment.initialize(Config.DEVELOPER_KEY, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void removeYoutubeFragmentAndSetup() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int commit = transaction.remove(youTubePlayerFragment).commit();

    }

    public void onBackPressed(Animation.AnimationListener listener) {
        try {
            if (YPlayer != null) {
                YPlayer.pause();
            }
//            mRevealLayout.hide(lastTouchDownXY[0], lastTouchDownXY[1], listener);
        } catch (Exception ex) {
            try {
                //   mRevealLayout.hide(lastTouchDownXY[0], lastTouchDownXY[1], listener);
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        }

    }

    private void finishOrGoHome() {
        try {

            Intent intent = new Intent(myContext, V2OttActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            myContext.finish();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void loadVideo(int millis) {
        try {
//            String videoID = currentContent.getYoutubeVid();
//            // videoID = "WR7cc5t7tv8"; //Danger
            if (!streamingWinResponse.getYoutubeId().equals("")) {
                YPlayer.cueVideo(streamingWinResponse.getYoutubeId(), millis);

               // YPlayer.cueVideo("y4eU7D_4J3o",millis);


                currentPositionMills = 0;
                myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }


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

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoId) {
        try {
            String imgUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
            return imgUrl;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    private void claimPoints(StreamingWinResponse streamingWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            //sendRedirectUrl(streamingWinResponse);
            String url = WingaConstants.CLAIM_STREAMING_WIN_POINTS + streamingWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
            calRedirectUrl();

        }

    }

    void calRedirectUrl() {
        if (!TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())) {
            Intent intent = new Intent(this, WebBrowserActivity.class);
            intent.putExtra(Constants.DATA, streamingWinResponse.getRedirectUrl());
            intent.putExtra(Constants.TITLE, streamingWinResponse.getTitle());
            intent.putExtra("fromWhichPage", 1);
            startActivity(intent);
        }
    }

    private void sendRedirectUrl(StreamingWinResponse streamingWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_STREAMING_REDIRECT_URL + streamingWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        int typeId = type;

        if (typeId == R.id.exitTv) {
            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
                onBackPressed();
            }
        } else if (typeId == R.id.continueBtn) {
            if (streamingWinResponse.getPoints() != null) {
                claimPoints(streamingWinResponse);
            }

            if (!TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())) {
                calRedirectUrl();
            }

            wonDialog.dismiss();
        }


    }


    @Override
    public void onEventListener(int type, Object object) {
    }

}
