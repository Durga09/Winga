package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
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

import java.util.Arrays;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

public class CongratulationsActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private static final String TAG = CongratulationsActivity.class.getSimpleName();
    RelativeLayout playMoreRL, eligiblePlayMoreGamesRL;
    private ImageView qualityGifIv;
    private boolean isElegibilityCase;
    private in.eightfolds.winga.model.GameResultResponse gameResultResponse;
    private RelativeLayout contentLL;

    private TextView centerDescTv1, centerDescTv2, centerDescTv3, centerDescTv4, elegibleDescTv;
    private LinearLayout congratsLL1, congratsLL2, congratsLL3, congratsLL4, elegibleLL;

    private boolean showBlackExitText = false;


    private static final String AD_UNIT_ID = "ca-app-pub-3530294870311813/1954091489";
//    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean isUserEarnedReward = false;
    NewGameResponse newGameResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_congrats);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            gameResultResponse = (GameResultResponse) bundle.get(Constants.DATA);
            if (bundle.containsKey("isElegibilityCase"))
                isElegibilityCase = bundle.getBoolean("isElegibilityCase");


        }
        Logg.v(TAG, "isElegibilityCase >> " + isElegibilityCase);
        initialize();
    }

    private void initialize() {
        playMoreRL = findViewById(R.id.playMoreRL);
        contentLL = findViewById(R.id.contentLL);
        playMoreRL.setOnClickListener(this);

        setScreen();
        makeExitSpannable(isElegibilityCase);
    }

    private void setScreen() {
        Spanned htmlAsSpanned = null;
        if (!TextUtils.isEmpty(gameResultResponse.getCongratsMsg())) {
            htmlAsSpanned = Html.fromHtml(gameResultResponse.getCongratsMsg());
        }

        if (isElegibilityCase) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View inflatedLayout = inflater.inflate(R.layout.lay_win_eligible, null);
            contentLL.addView(inflatedLayout);
            eligiblePlayMoreGamesRL = findViewById(R.id.eligiblePlayMoreGamesRL);
            qualityGifIv = findViewById(R.id.qualityGifIv);
            elegibleDescTv = findViewById(R.id.elegibleDescTv);
            elegibleLL = findViewById(R.id.elegibleLL);
            eligiblePlayMoreGamesRL.setOnClickListener(this);


            showBlackExitText = true;
            playMoreRL.setVisibility(View.GONE);
            elegibleLL.setVisibility(View.VISIBLE);
            elegibleDescTv.setText(htmlAsSpanned != null ? htmlAsSpanned : "");
            Utils.loadGifResourceToImageViewWithCount(this, qualityGifIv, R.drawable.qualify, 1);

        } else {
            //6,  9,  12, 15
            //18, 21, 24, 27
            //30, 33, 36, 39
            //42, 45, 48, 51
            //54, 57, 60, 61


            //2,  3,  4, 5
            //6,  7,  8, 9
            //10, 11, 12, 13
            //14, 15, 16, 17
            //18, 19, 20, 21

            int ratio = gameResultResponse.getNoOfGameWins() / gameResultResponse.getNoOfGameForEligible();
            int remainder = ratio % 4;
            if (remainder == 2) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View inflatedLayout = inflater.inflate(R.layout.lay_win_one, null);
                contentLL.addView(inflatedLayout);
                centerDescTv1 = findViewById(R.id.centerDescTv1);
                congratsLL1 = findViewById(R.id.congratsLL1);

                congratsLL1.setVisibility(View.VISIBLE);
                playMoreRL.setVisibility(View.VISIBLE);
                centerDescTv1.setText(htmlAsSpanned != null ? htmlAsSpanned : "");
            } else if (remainder == 3) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View inflatedLayout = inflater.inflate(R.layout.lay_win_two, null);
                contentLL.addView(inflatedLayout);
                centerDescTv2 = findViewById(R.id.centerDescTv2);
                congratsLL2 = findViewById(R.id.congratsLL2);

                congratsLL2.setVisibility(View.VISIBLE);
                playMoreRL.setVisibility(View.VISIBLE);
                centerDescTv2.setText(htmlAsSpanned != null ? htmlAsSpanned : "");
            } else if (remainder == 0) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View inflatedLayout = inflater.inflate(R.layout.lay_win_three, null);
                contentLL.addView(inflatedLayout);
                centerDescTv3 = findViewById(R.id.centerDescTv3);
                congratsLL3 = findViewById(R.id.congratsLL3);

                congratsLL3.setVisibility(View.VISIBLE);
                playMoreRL.setVisibility(View.VISIBLE);
                centerDescTv3.setText(htmlAsSpanned != null ? htmlAsSpanned : "");
            } else if (remainder == 1) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View inflatedLayout = inflater.inflate(R.layout.lay_win_four, null);
                contentLL.addView(inflatedLayout);
                findViewById(R.id.playMoreRL4).setOnClickListener(this);
                centerDescTv4 = findViewById(R.id.centerDescTv4);
                congratsLL4 = findViewById(R.id.congratsLL4);


                showBlackExitText = true;
                congratsLL4.setVisibility(View.VISIBLE);
                playMoreRL.setVisibility(View.GONE);
                centerDescTv4.setText(htmlAsSpanned != null ? htmlAsSpanned : "");

            }
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
                            CongratulationsActivity.this.isLoading = false;



                            showRewardedVideo();

                        }

                        @Override
                        public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                            // Ad failed to load.

                            EightfoldsVolley.getInstance().dismissProgress();

                            CongratulationsActivity.this.isLoading = false;

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
                                Intent intent = new Intent(CongratulationsActivity.this, ResultsActivity.class);
                                intent.putExtra(Constants.fromGoogleAdVideo, true);
                                intent.putExtra(Constants.earnAmountFromAdd, newGameResponse.getGoogleAdMobPerViewPoints());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                getNewGameResponse();
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
                            Toast.makeText(CongratulationsActivity.this, "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    };
            rewardedAd.show(this, adCallback);
        }
    }*/
    private void makeExitSpannable(boolean isElegibilityCase) {

        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        TextView exitTv = findViewById(R.id.exitTv);

        SpannableString exitSpan = makeLinkSpan(getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CongratulationsActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            }
        });

        if (showBlackExitText) {

            exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);

        makeLinksFocusable(exitTv);

    }

    private void getNewGameResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_NEW_GAME_URL;
            String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url=url.replace("{categoryId}",id);

            EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.playMoreRL || v.getId() == R.id.eligiblePlayMoreGamesRL || v.getId() == R.id.playMoreRL4) {
            getNewGameResponse();
        } else if (v.getId() == R.id.congratsExitTv) {
            callDashBoard();
        }
    }


    private void callDashBoard() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        callDashBoard();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        String url = Constants.GET_NEW_GAME_URL;
        String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);
        String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
        url = url.replace("{langId}", langId);
        url=url.replace("{categoryId}",id);

        if (requestType.equalsIgnoreCase(url)) {
            if (object instanceof NewGameResponse) {
                newGameResponse  = (NewGameResponse) object;
                (new UsageAnalytics()).trackPlayGame("", newGameResponse);
                // Logg.v("dash", "items count >> " + newGameResponse.getContents().size());
                if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                    onVolleyErrorListener(getString(R.string.session_closed));
                    callDashBoard();
                } else {
                    if(newGameResponse.getContents()!=null){
                        NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                        Intent intent = new Intent(this, VideoActivity.class);
                        intent.putExtra(Constants.DATA, filteredLangGameResp);
                        startActivity(intent);

                    }else {
                        if (newGameResponse.getGoogleAdMobViewCount() >= newGameResponse.getGoogleAdMobMaxViewCount()) {
                            Intent intent = new Intent(this, HomeActivity.class);
                            startActivity(intent);
                        } else {


//                            EightfoldsVolley.getInstance().showProgress(this);
//
//                            loadRewardedAd();

                            Intent intent = new Intent(this, HomeActivity.class);
                            startActivity(intent);

                        }


                    }
            } }else if (object instanceof CommonResponse) {
                CommonResponse commonResponse = (CommonResponse) object;
                if (commonResponse.getCode() == Constants.TODAY_GAME_LIMIT_REACHED) {
                    //game limit exceeded.
                    MyDialog.showCommonDialogWithType(this, this, getString(R.string.alert),
                            commonResponse.getMessage()
                            , getString(R.string.okay), Constants.TODAY_GAME_LIMIT_REACHED);
                }
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.TODAY_GAME_LIMIT_REACHED) {
                //game limit exceeded.
                MyDialog.showCommonDialogWithType(this, this, getString(R.string.alert), commonResponse.getMessage()
                        , getString(R.string.okay), Constants.TODAY_GAME_LIMIT_REACHED);
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

    }

    @Override
    public void onEventListener(int type, Object object) {
        if (object.toString().equalsIgnoreCase(Constants.TODAY_GAME_LIMIT_REACHED + ""))
            callDashBoard();
    }
}
