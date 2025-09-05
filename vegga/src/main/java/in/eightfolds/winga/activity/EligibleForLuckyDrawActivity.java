package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by sp on 09/05/18.
 */

public class EligibleForLuckyDrawActivity extends BaseActivity implements VolleyResultCallBack {

    RelativeLayout playMoreGamesRL;
    //private HomePageResponse homePageResponse;
    private ImageView qualityGifIv;
    private boolean isElegibilityCase;
    private in.eightfolds.winga.model.GameResultResponse gameResultResponse;

    private LinearLayout elegibleLL;
    private RelativeLayout congratesRl;
    private RelativeLayout playMoreGamesRL1;
    private TextView congratsDescTv, elegibleTv;


    private RelativeLayout congratsRL, congratsPlayMoreRL;
    private TextView congratsExitTv, congratsCenterDescTv;
    private ImageView congratsCenterIv, congratsBottomIv ,congratsTopIv;
    private int numberOfWins;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_eligible_for_lucky_draw);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gameResultResponse = (GameResultResponse) bundle.get(Constants.DATA);
            if (bundle.containsKey("isElegibilityCase"))
                isElegibilityCase = bundle.getBoolean("isElegibilityCase");
            if (bundle.containsKey("numberOfWins"))
                numberOfWins = bundle.getInt("numberOfWins");
        }
        initialize();
    }

    private void initialize() {
        playMoreGamesRL = findViewById(R.id.playMoreGamesRL);
        qualityGifIv = findViewById(R.id.qualityGifIv);

        elegibleLL = findViewById(R.id.elegibleLL);
        congratesRl = findViewById(R.id.congratesRl);
        playMoreGamesRL1 = findViewById(R.id.playMoreGamesRL1);
        congratsDescTv = findViewById(R.id.congratsDescTv);
        elegibleTv = findViewById(R.id.elegibleTv);

        congratsRL = findViewById(R.id.congratsRL);
        congratsTopIv = findViewById(R.id.congratsTopIv);
        congratsPlayMoreRL = findViewById(R.id.congratsPlayMoreRL);
        congratsExitTv = findViewById(R.id.congratsExitTv);
        congratsCenterDescTv = findViewById(R.id.congratsCenterDescTv);
        congratsCenterIv = findViewById(R.id.congratsCenterIv);
        congratsBottomIv = findViewById(R.id.congratsBottomIv);


        playMoreGamesRL.setOnClickListener(this);
        playMoreGamesRL1.setOnClickListener(this);
        congratsPlayMoreRL.setOnClickListener(this);
        //congratsExitTv.setOnClickListener(this);


       // numberOfWins = 12;
       // gameResultResponse.setCongratsMsg("You are eligible for lucky dip. Play more games  to increase your chance of lucky dip win. You have won "+numberOfWins + " till now. keep playing.");

        if (isElegibilityCase) {
            elegibleLL.setVisibility(View.VISIBLE);
            congratesRl.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(gameResultResponse.getCongratsMsg())) {
                Spanned htmlAsSpanned = Html.fromHtml(gameResultResponse.getCongratsMsg());
                elegibleTv.setText(htmlAsSpanned);
            }
        } else {
            elegibleLL.setVisibility(View.GONE);
           // congratesRl.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(gameResultResponse.getCongratsMsg())) {
                Spanned htmlAsSpanned = Html.fromHtml(gameResultResponse.getCongratsMsg());
                congratsCenterDescTv.setText(htmlAsSpanned);
            }

            congratsRL.setVisibility(View.VISIBLE);
            int ratio = numberOfWins / 3;
            int remainder = ratio % 3;
            if(remainder == 2){  //6, 15, 24, 33 (2,5,8,11,)
                congratsTopIv.setImageDrawable(getResources().getDrawable(R.drawable.win_top_one));
                congratsCenterIv.setImageDrawable(getResources().getDrawable(R.drawable.win_center_one));
                congratsBottomIv.setImageDrawable(getResources().getDrawable(R.drawable.win_bottom_one));

            }else if(remainder == 0 ){ //9, 18, 27, 36
                congratsTopIv.setImageDrawable(getResources().getDrawable(R.drawable.win_top_two));
                congratsCenterIv.setImageDrawable(getResources().getDrawable(R.drawable.win_center_two));
                congratsBottomIv.setImageDrawable(getResources().getDrawable(R.drawable.win_bottom_two));

            }else if(remainder == 1){ //12,  21, 30, 39
                congratsTopIv.setImageDrawable(getResources().getDrawable(R.drawable.win_top_three));
                congratsCenterIv.setImageDrawable(getResources().getDrawable(R.drawable.win_center_three));
                congratsBottomIv.setImageDrawable(getResources().getDrawable(R.drawable.win_bottom_three));
            }
        }

        Utils.loadGifResourceToImageViewWithCount(this, qualityGifIv, R.drawable.qualify, 1);
        makeExitSpannable(isElegibilityCase);
    }


    private void makeExitSpannable(boolean isElegibilityCase) {

        StyleSpan iss = new StyleSpan(Typeface.BOLD);

        if(isElegibilityCase) {

            TextView firstTimeElegibleExit = findViewById(R.id.exitTv);
            SpannableString exitSpan = makeLinkSpan(getString(R.string.exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EligibleForLuckyDrawActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            });

            exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            firstTimeElegibleExit.setText(exitSpan);

            makeLinksFocusable(firstTimeElegibleExit);
        }else{
            TextView congratsExitTv = findViewById(R.id.congratsExitTv);

            SpannableString exitSpan1 = makeLinkSpan(getString(R.string.exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EligibleForLuckyDrawActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            });

            exitSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, exitSpan1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            exitSpan1.setSpan(iss, 0, exitSpan1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            congratsExitTv.setText(exitSpan1);
            makeLinksFocusable(congratsExitTv);
        }
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
        if (v.getId() == R.id.playMoreGamesRL || v.getId() == R.id.playMoreGamesRL1 || v.getId() == R.id.congratsPlayMoreRL) {
            getNewGameResponse();
        }else if(v.getId() == R.id.congratsExitTv){
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
        // super.onBackPressed();
        callDashBoard();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof NewGameResponse) {
            NewGameResponse newGameResponse = (NewGameResponse) object;
            (new UsageAnalytics()).trackPlayGame("", newGameResponse);
           // Logg.v("dash", "items count >> " + newGameResponse.getContents().size());
            if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                onVolleyErrorListener(getString(R.string.session_closed));
                callDashBoard();
            } else {
                NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra(Constants.DATA, filteredLangGameResp);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }
}
