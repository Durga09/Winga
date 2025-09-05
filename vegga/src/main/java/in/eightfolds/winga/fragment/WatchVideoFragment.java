package in.eightfolds.winga.fragment;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import in.eightfolds.WingaApplication;
import in.eightfolds.circleprogress.CircleProgress;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.reveal.RevealLayout;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.QuestionsAndAnswersFragmentActivity;
import in.eightfolds.winga.activity.VideoActivity;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.ContentResponse;
import in.eightfolds.winga.model.DurationDetails;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.utils.Config;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.ShowCaseUtils;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 18-Jun-18.
 */
public class WatchVideoFragment extends Fragment implements View.OnClickListener,YouTubePlayer.OnFullscreenListener , YouTubePlayer.OnInitializedListener, VolleyResultCallBack{

    private static String TAG = WatchVideoFragment.class.getSimpleName();
    private Activity myContext;
    private NewGameResponse newGameResponse;
    private boolean firstgame;
    ContentResponse currentContent;
    public YouTubePlayer YPlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RevealLayout mRevealLayout;
    private boolean isFromQuestion;
    private int[] lastTouchDownXY = new int[2];
    private Button askQstnBtn;
    private LinearLayout iamReadyLL,youtubeLinearLayout;
    private RelativeLayout showTaskLL;
    private ImageView questionIv;
    private ImageView closeVideoIv, backVideoIv, shareIv;
    private RelativeLayout mainRL;
    private CountDownTimer countDownTimer;
    private LinearLayout videoProgressLL;
    private ProgressBar videoDurationProgressBar;
    private TextView currentVideoProgressTv, totalVideoDurationTv, showText, showTitle;
    private long durationDetailsId;
    private AppDataBase appDataBase;
    private long uniqueViewId;
    private boolean pausedManually = false;
    private int currentPositionMills = 0;
    private LinearLayout processingLL;
    private FrameLayout youtube_fragment;
    private Timer progressTimer;
    private int progress;
    private CircleProgress circleProgress;
    private boolean isUnexpectedYoutubeErrorOccurred = false;
    private int retryCount = 1;
    private LinearLayout adBannerLL, bottomLL;
    private String formattedDuration;
    private boolean isRunning = false;
    private boolean isImageLoaded = false;
    private int totalSecondsForImage = 30;
    private int remainingSecondsForImage;

    private LinearLayout replayMainLay, replayLL;
    private RelativeLayout shopNowLL;

    private boolean isFromReplay = false;
    private boolean frmoAlert = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_watch_video, container, false);
        try {

            // progressDialog = MyDialog.showProgress(myContext);
            if (Constants.captureDurationDetails) {
                appDataBase = AppDataBase.getAppDatabase(myContext);
            }

            Bundle bundle = getArguments();
            if (bundle != null) {
                newGameResponse = (NewGameResponse) bundle.get(Constants.DATA);
                if (bundle.containsKey("firstgame")) {
                    firstgame = bundle.getBoolean("firstgame");
                }
                if (bundle.containsKey("isFromQuestion")) {
                    isFromQuestion = bundle.getBoolean("isFromQuestion");
                }
                if (bundle.containsKey("xy")) {
                    lastTouchDownXY = bundle.getIntArray("xy");
                    if (lastTouchDownXY != null && lastTouchDownXY.length == 2) {
                        Logg.v(TAG, "*** X >>> " + lastTouchDownXY[0]);
                        Logg.v(TAG, "*** Y >>> " + lastTouchDownXY[1]);
                    }
                }
            }

            initialize(rootview);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootview;
    }

    public void setContentDurationDetails() {
        try {
            if (Constants.captureDurationDetails) {
                Logg.v(TAG, "*setContentDurationDetails >  durationDetailsId:  " + durationDetailsId + " ,  uniqueID : " + uniqueViewId);
                DurationDetails olddurationDetails = appDataBase.getDurationDetailsDao().getDurationDetailsForId(durationDetailsId);
                if (olddurationDetails != null && olddurationDetails.getEndMills() == 0) {
                    //TODO  do nothing if last id's end details are not updated. Just use that
                } else {

                    DurationDetails durationDetails = new DurationDetails();
                    durationDetails.setContentId(currentContent.getContentId());
                    UUID uniqueId = UUID.randomUUID();
                    durationDetails.setUniqueUuid(uniqueId);
                    if (YPlayer != null && currentContent.getType() != null && currentContent.getType() != Constants.IMAGE_CONTENT_ID) {
                        durationDetails.setStartMillis(YPlayer.getCurrentTimeMillis());
                    } else if (currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                        durationDetails.setStartMillis(System.currentTimeMillis());
                    }
                    durationDetails.setVideoStartUniqueId(uniqueViewId);
                    durationDetails.setGameId(newGameResponse.getCategoryGameId());
                    durationDetailsId = appDataBase.getDurationDetailsDao().insert(durationDetails);
                    List<DurationDetails> durationDetailsList = appDataBase.getDurationDetailsDao().getAll();
                    Logg.v(TAG, "*durationDetailsList > " + durationDetailsList);
                    durationDetails.setId(durationDetailsId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateContentDurationDetails() {
        try {
            if (Constants.captureDurationDetails) {
                Logg.v(TAG, "*updateContentDurationDetails >  durationDetailsId:  " + durationDetailsId + " ,  uniqueID : " + uniqueViewId);

                DurationDetails durationDetails = appDataBase.getDurationDetailsDao().getDurationDetailsForId(durationDetailsId);
                if (durationDetails != null) {
                    long startMills = durationDetails.getStartMillis();
                    long endMills = 0;
                    if (YPlayer != null && currentContent.getType() != null && currentContent.getType() != Constants.IMAGE_CONTENT_ID) {
                        endMills = YPlayer.getCurrentTimeMillis();
                    } else if (currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                        endMills = System.currentTimeMillis();
                    }
                    durationDetails.setEndMills(endMills);
                    long activeMillis = endMills - startMills;
                    durationDetails.setActiveMills(activeMillis);
                    appDataBase.getDurationDetailsDao().update(durationDetails);

                    List<DurationDetails> durationDetailsList = appDataBase.getDurationDetailsDao().getAll();
                    Logg.v(TAG, "*durationDetailsList > " + durationDetailsList);
                    durationDetailsId = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            handlerFor30Sec.removeCallbacks(timerFor30Sec);

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
    public void onDestroyView() {
        super.onDestroyView();
        try {
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            Logg.v(TAG, "**onResume()");
            if (isImageLoaded && currentContent != null && currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                setContentDurationDetails();
                if (!isFromQuestion && remainingSecondsForImage >= 0) {
                    handlerFor30Sec.postDelayed(timerFor30Sec, 1 * 1000);
                }
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

    public void pause() {
        try {
            if (YPlayer != null) {
                pausedManually = true;
                currentPositionMills = YPlayer.getCurrentTimeMillis();
                YPlayer.pause();
            }
            if (currentContent != null && currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                updateContentDurationDetails();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (currentContent != null && currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                updateContentDurationDetails();
            }
        }
    }

    public void myPlayYouTube() {
        try {
            if (YPlayer != null && !YPlayer.isPlaying()) {
                YPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void initialize(View rootview) {
        try {
            mRevealLayout = rootview.findViewById(R.id.reveal_layout);
            askQstnBtn = rootview.findViewById(R.id.askQstnBtn);
            iamReadyLL = rootview.findViewById(R.id.iamReadyLL);
            questionIv = rootview.findViewById(R.id.questionIv);
            closeVideoIv = rootview.findViewById(R.id.closeVideoIv);
            shareIv = rootview.findViewById(R.id.shareIv);
            backVideoIv = rootview.findViewById(R.id.backVideoIv);
            processingLL = rootview.findViewById(R.id.processingLL);
            youtube_fragment = rootview.findViewById(R.id.youtube_fragment);
            circleProgress = rootview.findViewById(R.id.circle_progress);
            mainRL = rootview.findViewById(R.id.mainRL);
            adBannerLL = rootview.findViewById(R.id.adBannerLL);
            bottomLL = rootview.findViewById(R.id.bottomLL);
            replayMainLay = rootview.findViewById(R.id.replayMainLay);
            replayLL = rootview.findViewById(R.id.replayLL);
            shopNowLL = rootview.findViewById(R.id.shopNowLL);
        //    youtubeLinearLayout=rootview.findViewById(R.id.youtubeLinearLayout);

            showTaskLL = rootview.findViewById(R.id.showTaskLL);

            showText = rootview.findViewById(R.id.up);
            showText.setMovementMethod(new ScrollingMovementMethod());
            showTitle = rootview.findViewById(R.id.showTitle);

            videoProgressLL = rootview.findViewById(R.id.videoProgressLL);
            videoDurationProgressBar = rootview.findViewById(R.id.videoDurationProgressBar);
            currentVideoProgressTv = rootview.findViewById(R.id.currentVideoProgressTv);
            totalVideoDurationTv = rootview.findViewById(R.id.totalVideoDurationTv);

            closeVideoIv.setOnClickListener(this);
            shareIv.setOnClickListener(this);
            backVideoIv.setOnClickListener(this);
            askQstnBtn.setOnClickListener(this);
            replayLL.setOnClickListener(this);
            shopNowLL.setOnClickListener(this);

            showTaskLL.setOnClickListener(this);

            if (isFromQuestion) {
                backVideoIv.setVisibility(View.VISIBLE);
                closeVideoIv.setVisibility(View.GONE);

            } else {
                backVideoIv.setVisibility(View.GONE);
                closeVideoIv.setVisibility(View.VISIBLE);
            }
            setUpGame();
            addBannerFragment();
            if (currentContent != null && currentContent.getShareLinkData() != null) {
                shareIv.setVisibility(View.VISIBLE);
            } else {
                shareIv.setVisibility(View.GONE);
            }

            if (currentContent != null && currentContent.getBuyLinkData() != null) {
//                if(newGameResponse.getForm()==null){
//                    shopNowLL.setVisibility(View.VISIBLE);
//                }else{
//                    showTitle.setText(currentContent.getBuyLinkData().getBuyLinkButtonText());
//                }

                if (currentContent.getBuyLinkData().getBuyLinkButtonText() != null) {
                    showTitle.setText(currentContent.getBuyLinkData().getBuyLinkButtonText());

                } else {
                    shopNowLL.setVisibility(View.VISIBLE);
                }
            }
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
            if (isFromQuestion) {
                if (!isFromReplay) {
                    mRevealLayout.setContentShown(false);
                    mRevealLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mRevealLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                //noinspection deprecation
                                mRevealLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }

                            mRevealLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mRevealLayout.show(lastTouchDownXY[0], lastTouchDownXY[1]);
                                }
                            }, 150);

                        }
                    });
                }
                askQstnBtn.setText(getString(R.string.go_to_questions));
            } else {
                if (newGameResponse.getForm() != null) {
                    if (newGameResponse.getForm().getFormButtonText() != null) {
                        askQstnBtn.setText(newGameResponse.getForm().getFormButtonText());
                    } else if (currentContent.getAnsQuestionBtnTxt() != null) {
                        askQstnBtn.setText(currentContent.getAnsQuestionBtnTxt());
                    } else {
                        askQstnBtn.setText(getString(R.string.askquestions));
                    }

                    shopNowLL.setVisibility(View.GONE);


                }

                mainRL.setBackgroundColor(myContext.getResources().getColor(R.color.colorBlack));
            }

            if (newGameResponse.getAppcontents().size() > newGameResponse.getCurrentContentID()) {
                currentContent = newGameResponse.getAppcontents().get(newGameResponse.getCurrentContentID());
                if (currentContent != null && currentContent.getType() != null && currentContent.getType() == Constants.IMAGE_CONTENT_ID) {
                    (new UsageAnalytics()).trackWatchVideo("", currentContent);
                    questionIv.setVisibility(View.VISIBLE);
                    if (currentContent.getFileId() != null) {

                        videoProgressLL.setVisibility(View.GONE);
                        myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        if (currentContent.getBannerDuration() != null && currentContent.getBannerDuration() > 0) {
                            totalSecondsForImage = currentContent.getBannerDuration().intValue();
                        }
                        remainingSecondsForImage = totalSecondsForImage;
                        if (!isFromQuestion) {
                            askQstnBtn.setEnabled(false);
                        }
                        loadPhoto(currentContent.getFileId(), questionIv);

                    }


                } else {
                    initializeYouTube();
                    questionIv.setVisibility(View.GONE);
                }
                if (newGameResponse.getForm() != null) {
                    if (newGameResponse.getForm().getFormButtonText() != null) {
                        askQstnBtn.setText(newGameResponse.getForm().getFormButtonText());
                    } else if (currentContent.getAnsQuestionBtnTxt() != null) {
                        askQstnBtn.setText(currentContent.getAnsQuestionBtnTxt());
                    } else {
                        askQstnBtn.setText(getString(R.string.askquestions));
                    }

                    shopNowLL.setVisibility(View.GONE);


                } else {
                    if (currentContent != null) {
                        if (currentContent.getAnsQuestionBtnTxt() != null) {
                            askQstnBtn.setText(currentContent.getAnsQuestionBtnTxt());
                        } else {
                            askQstnBtn.setText(getString(R.string.askquestions));
                        }
                    }
                }

            } else {
                MyDialog.showToast(myContext, getString(R.string.something_wrong));
                finishOrGoHome();
            }

            setUniqueViewId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    YouTubePlayerSupportFragment youTubePlayerFragment;

    private void initializeYouTube() {
        try {
            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();
            youTubePlayerFragment.initialize(Config.DEVELOPER_KEY, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void removeYoutubeFragmentAndSetup() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        final int commit = transaction.remove(youTubePlayerFragment).commit();

    }

    public void onBackPressed(Animation.AnimationListener listener) {
        try {
            if (YPlayer != null) {
                YPlayer.pause();
            }
            mRevealLayout.hide(lastTouchDownXY[0], lastTouchDownXY[1], listener);
        } catch (Exception ex) {
            try {
                mRevealLayout.hide(lastTouchDownXY[0], lastTouchDownXY[1], listener);
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    private void loadVideo(int millis) {
        try {
            String videoID = currentContent.getYoutubeVid();
            // videoID = "WR7cc5t7tv8"; //Danger
            YPlayer.loadVideo(videoID, millis);

            currentPositionMills = 0;
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    int imageReloadCount = 0;

    private final Handler handlerFor30Sec = new Handler();

    Runnable timerFor30Sec = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            try {
                askQstnBtn.setVisibility(View.VISIBLE);
                iamReadyLL.setVisibility(View.VISIBLE);
                if (remainingSecondsForImage == 0) {


                    if (newGameResponse.getForm() != null) {

                        if (newGameResponse.getForm().getFormDescription() != null) {
                            showText.setVisibility(View.VISIBLE);
                            showText.setText(Html.fromHtml(newGameResponse.getForm().getFormDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                        } else {
                            showText.setVisibility(View.GONE);
                        }

                    }


                    if (currentContent.getAnsQuestionBtnTxt() != null) {
                        askQstnBtn.setText(currentContent.getAnsQuestionBtnTxt());
                    } else {
                        askQstnBtn.setText(getString(R.string.askquestions));
                    }
                    if (currentContent.getBuyLinkData() != null) {
                        if (currentContent.getBuyLinkData().getBuyLinkButtonText() != null) {
                            showTaskLL.setVisibility(View.VISIBLE);
                        } else {
                            shopNowLL.setVisibility(View.VISIBLE);
                        }
                    }

                    if (isAdded() && myContext != null) {
                        if (newGameResponse.getForm() == null) {
                            ShowCaseUtils.presentVideoShowcaseSequence(myContext, askQstnBtn, getString(R.string.tutorial_answer_questions));
                        } else {
                            ShowCaseUtils.presentVideoShowcaseSequence(myContext, askQstnBtn, "To fill form");
                        }

                    }
                    askQstnBtn.setEnabled(true);
                } else {
                    if (currentContent.getBuyLinkData() != null) {
                        if (currentContent.getBuyLinkData().getBuyLinkButtonText() != null) {
                            showTitle.setText(currentContent.getBuyLinkData().getBuyLinkButtonText());

                        } else {
                            shopNowLL.setVisibility(View.VISIBLE);
                        }
                    }

                    askQstnBtn.setText(remainingSecondsForImage + " Sec");
                    remainingSecondsForImage--;
                    askQstnBtn.setEnabled(false);
                    handlerFor30Sec.postDelayed(timerFor30Sec, 1 * 1000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private void loadPhoto(final long fileId, final ImageView imageView) {
        try {
            Logg.v(TAG, "IMG >> " + Constants.FILE_URL + fileId);
            if (!isFromQuestion) {
                handlerFor30Sec.removeCallbacks(timerFor30Sec);
            }

            Glide.with(WingaApplication.applicationContext)
                    .asBitmap()
                    .load(Constants.FILE_URL + fileId)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                processingLL.setVisibility(View.GONE);
                                imageView.setImageBitmap(resource);
                                isImageLoaded = true;
                                setContentDurationDetails();
                                askQstnBtn.setVisibility(View.VISIBLE);
                                iamReadyLL.setVisibility(View.VISIBLE);

                                if (!isFromQuestion) {
                                    askQstnBtn.setText(remainingSecondsForImage + " Sec");
                                    remainingSecondsForImage--;
                                    handlerFor30Sec.postDelayed(timerFor30Sec, 1 * 1000);
                                }
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            processingLL.setVisibility(View.GONE);
                            if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

                                if (imageReloadCount <= 2) {
                                    loadPhoto(fileId, imageView);
                                } else {
                                    getNewGameResponse();
                                }
                            } else {
                                finishOrGoHome();
                            }
                        }
                    });

            imageReloadCount++;

          /*
            GlideApp.with(WingaApplication.applicationContext)
                    .load(Constants.FILE_URL + fileId)
                    .centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setUniqueViewId() {
        if (Constants.captureDurationDetails) {
            Logg.v(TAG, "*setUniqueViewId()");
            uniqueViewId = System.currentTimeMillis();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        try {
            int id = view.getId();

            if (id == R.id.showTaskLL) {
                if (YPlayer != null) {
                    pause();
                }
                if (currentContent.getBuyLinkData() != null && currentContent.getBuyLinkData().getBuyLinkAlertText() != null) {
                    showAlertBox();
                } else if (currentContent.getBuyLinkData() != null && currentContent.getBuyLinkData().getContentId() > 0) {
                    LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(getContext(), Constants.LOGIN_DATA, LoginData.class);
                    if (loginData != null) {
                        String url = Constants.BUY_CONTENT_URL;
                        url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
                        url = url.replace("{contentId}", String.valueOf(currentContent.getBuyLinkData().getContentId()));
                        Utils.openInBrowser(getContext(), url);
                    }
                }

            } else if (id == R.id.shopNowLL) {
                if (currentContent.getBuyLinkData() != null && currentContent.getBuyLinkData().getContentId() > 0) {
                    LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(getContext(), Constants.LOGIN_DATA, LoginData.class);
                    if (loginData != null) {
                        String url = Constants.BUY_CONTENT_URL;
                        url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
                        url = url.replace("{contentId}", String.valueOf(currentContent.getBuyLinkData().getContentId()));
                        Utils.openInBrowser(getContext(), url);
                    }
                }

            } else if (id == R.id.replayLL) {
                youtube_fragment.setVisibility(View.VISIBLE);
                if (currentContent.getBuyLinkData() != null) {
                    if (currentContent.getBuyLinkData().getBuyLinkButtonText() != null) {
                        showTaskLL.setVisibility(View.GONE);
                    } else {
                        shopNowLL.setVisibility(View.GONE);
                    }
                    showText.setVisibility(View.GONE);
                }

                isFromReplay = true;
                setProgressTimer();
                replayMainLay.setVisibility(View.GONE);
                setUpGame();

            } else if (id == R.id.closeVideoIv || id == R.id.backVideoIv) {
                if (YPlayer != null) {
                    pause();
                }
                myContext.onBackPressed();

            } else if (id == R.id.shareIv) {
                if (currentContent.getShareLinkData() != null) {
                    getShareLink();
                }

            } else if (id == R.id.askQstnBtn) {
                if (!isFromQuestion) {
                    if (currentContent.getQuestions() != null && currentContent.getQuestions().size() > 0) {
                        Intent intent = new Intent(myContext, QuestionsAndAnswersFragmentActivity.class);
                        intent.putExtra(Constants.DATA, newGameResponse);
                        intent.putExtra("firstgame", firstgame);
                        startActivity(intent);
                        myContext.finish();
                    } else if (newGameResponse.getForm() != null) {
                        if (newGameResponse.getForm().getAlert1() != null) {
                            showDailog();
                        } else {
                            Intent intent = new Intent(myContext, QuestionsAndAnswersFragmentActivity.class);
                            intent.putExtra(Constants.DATA, newGameResponse);
                            intent.putExtra("firstgame", firstgame);
                            startActivity(intent);
                            myContext.finish();
                        }
                    } else {
                        MyDialog.showToast(myContext, getString(R.string.something_wrong));
                    }
                } else {
                    myContext.onBackPressed();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getShareLink() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {


            EightfoldsVolley.getInstance().showProgress(myContext);
            String url = Constants.SHARE_LINK_CONTENT_URL;
            LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(getContext(), Constants.LOGIN_DATA, LoginData.class);
            url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
            url = url.replace("{contentId}", currentContent.getShareLinkData().getContentId());

            EightfoldsVolley.getInstance().makingStringRequest(this, String.class, Request.Method.GET, url);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDailog() {

        MyDialog.showButtonDialog(myContext, new OnEventListener() {
            @Override
            public void onEventListener() {

            }

            @Override
            public void onEventListener(int type) {
                if (type == R.id.yesTv) {
                    Intent intent = new Intent(myContext, QuestionsAndAnswersFragmentActivity.class);
                    intent.putExtra(Constants.DATA, newGameResponse);
                    intent.putExtra("firstgame", firstgame);
                    startActivity(intent);
                    myContext.finish();
                }
                if (type == R.id.noText) {
                    frmoAlert = true;
                    getNewGameResponse();
                }

            }

            @Override
            public void onEventListener(int type, Object object) {

            }
        }, getString(R.string.alert), newGameResponse.getForm().getAlert1(), "Yes", "No");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAlertBox() {

        MyDialog.showButtonDialog(myContext, new OnEventListener() {
            @Override
            public void onEventListener() {

            }

            @Override
            public void onEventListener(int type) {
                if (type == R.id.yesTv) {
                    LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(getContext(), Constants.LOGIN_DATA, LoginData.class);
                    if (loginData != null) {
                        String url = Constants.BUY_CONTENT_URL;
                        url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
                        url = url.replace("{contentId}", String.valueOf(currentContent.getBuyLinkData().getContentId()));
                        Utils.openInBrowser(getContext(), url);
                    }
                }
                if (type == R.id.noText) {

                }

            }

            @Override
            public void onEventListener(int type, Object object) {

            }
        }, getString(R.string.alert), currentContent.getBuyLinkData().getBuyLinkAlertText(), "Yes", "No");
    }

    private void closeTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (!isFromQuestion) {
            handlerFor30Sec.removeCallbacks(timerFor30Sec);
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

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoId) {
        try {
            String imgUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
            return imgUrl;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean onRestored) {
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
                            performOnVideoStarted();
                        }
                    }

                    @Override
                    public void onStopped() {
                        // Logg.v(TAG, "**onStopped()");
                        if (YPlayer != null) {
                            Logg.v(TAG, "**onStopped()  >>  " + YPlayer.getCurrentTimeMillis());
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
                       // youtube_fragment.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,500));
                        // performOnVideoStarted();


                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onVideoEnded() {
                        try {
                            Logg.v("Video", "Video Ended >> ");
                           // youtubeLinearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                            currentPositionMills = 0;
                            askQstnBtn.setVisibility(View.VISIBLE);
                            askQstnBtn.setEnabled(true);
                            iamReadyLL.setVisibility(View.VISIBLE);
                            if (currentContent != null && currentContent.getBuyLinkData() != null) {
//

                                if (currentContent.getBuyLinkData().getBuyLinkButtonText() != null) {
                                    showTaskLL.setVisibility(View.VISIBLE);
                                } else {
                                    shopNowLL.setVisibility(View.VISIBLE);
                                }
                            }
                            if (newGameResponse.getForm() != null) {
                                showTaskLL.setVisibility(View.VISIBLE);
                                if (newGameResponse.getForm().getFormDescription() != null) {
                                    showText.setVisibility(View.VISIBLE);
                                    showText.setText(Html.fromHtml(newGameResponse.getForm().getFormDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                                } else {
                                    showText.setVisibility(View.GONE);
                                }

                            }


                            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
                            if (newGameResponse.getForm() == null) {
                                ShowCaseUtils.presentVideoShowcaseSequence(myContext, askQstnBtn, getString(R.string.tutorial_answer_questions));
                            } else {
                                ShowCaseUtils.presentVideoShowcaseSequence(myContext, askQstnBtn, "To fill form");
                            }
                            setUniqueViewId();

                            youtube_fragment.setVisibility(View.GONE);
                         /*   videoProgressLL.setVisibility(View.GONE);
                            replayMainLay.setVisibility(View.VISIBLE);*/

                            videoProgressLL.setVisibility(View.GONE);
                            replayMainLay.setVisibility(View.VISIBLE);
                            removeYoutubeFragmentAndSetup();
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

                                if (!firstgame) {
                                    if (retryCount == 1)
                                        MyDialog.showToast(myContext, getString(R.string.video_not_playable));
                                    getNewGameResponse();

                                } else {
                                    if (retryCount == 1)
                                        MyDialog.showToast(myContext, getString(R.string.not_able_to_play));
                                    finishOrGoHome();
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

    private void performOnVideoStarted() {
        try {
            Logg.v(TAG, "*performOnVideoStarted");
         //   myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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

    private void getNewGameResponse() {
        try {
            if (retryCount >= 3) {

                if (YPlayer != null) {
                    YPlayer.release();
                    YPlayer = null;
                }
                MyDialog.showCommonDialog(myContext, new OnEventListener() {
                    @Override
                    public void onEventListener() {

                    }

                    @Override
                    public void onEventListener(int type) {
                        if (type == R.id.yesTv) {
                            finishOrGoHome();
                        }
                    }

                    @Override
                    public void onEventListener(int type, Object object) {

                    }
                }, getString(R.string.alert), getString(R.string.not_able_to_play), getString(R.string.okay));
            } else {
                if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

                    retryCount++;
                    Logg.v(TAG, "retryCount >> " + retryCount);
                    EightfoldsVolley.getInstance().showProgress(myContext);
                    String url = Constants.GET_NEW_GAME_URL;
                    String id = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.SELECTED_CATEGORY);
                    String langId = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.SELECTED_LANGUAGE_ID);
                    url = url.replace("{langId}", langId);
                    url = url.replace("{categoryId}", id);

                    EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);

                } else {
                    //   myContext.finish();
                    finishOrGoHome();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        try {
            if (object instanceof NewGameResponse) {

                newGameResponse = (NewGameResponse) object;
                if (!frmoAlert) {
                    if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {

                        onVolleyErrorListener(getString(R.string.session_closed));

                        finishOrGoHome();
                    } else {
                        (new UsageAnalytics()).trackPlayGame("", newGameResponse);
                        newGameResponse = Utils.getSelectedLangContents(myContext, newGameResponse);
                        // Logg.v("dash", "items count >> " + newGameResponse.getContents().size());
                        setUpGame();
                    }
                } else {
                    NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(getContext(), newGameResponse);
                    Intent intent = new Intent(getContext(), VideoActivity.class);
                    intent.putExtra(Constants.DATA, filteredLangGameResp);
                    startActivity(intent);
                }
            } else if (object instanceof String) {
                String result = (String) object;
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getContext().getResources().getString(R.string.app_name));
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                getContext().startActivity(Intent.createChooser(shareIntent, getContext().getString(R.string.share) + " " + getContext().getString(R.string.app_name)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            finishOrGoHome();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

        Utils.handleCommonErrors(myContext, object);
        finishOrGoHome();
    }

    private void finishOrGoHome() {
        try {
            if (!firstgame) {
                Intent intent = new Intent(myContext, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                myContext.finish();
            } else {
                myContext.finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setImagePropertiesInPortraitMode() {
        bottomLL.setVisibility(View.VISIBLE);
    }

    public void setImagePropertiesInLandscapeMode() {
        bottomLL.setVisibility(View.GONE);
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

    @Override
    public void onFullscreen(boolean b) {
        if (b) {
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        YPlayer.setFullscreen(b);
    }
}


