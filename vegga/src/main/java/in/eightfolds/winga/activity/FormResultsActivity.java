package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.FormResultsRecyclerAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.NewGameSubmitRequest;
import in.eightfolds.winga.model.SurveyResult;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.scratch.ScratchTextView;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.ShowCaseUtils;
import in.eightfolds.winga.utils.Utils;

public class FormResultsActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private RecyclerView progressRecyclerView;
    private in.eightfolds.winga.model.GameResultResponse gameResultResponse;
    private TextView exitTv;
    private ScratchTextView scratchTextView;
    private ImageView profilePicIV;
    private TextView totalPointTv, winPointsTv, questionTV;
    private Double previousPoints = 0d;
    private boolean isCardScratched = false;
    private RelativeLayout pointsRL;
    private User userDetails;
    private Dialog wonDialog;
    private boolean isElegibilityCase;
    private Double totalPoints;
    private NewGameSubmitRequest newGameSubmitRequest;
    NewGameResponse newGameResponse;


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type== R.id.exitTv) {


            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
            }
            callDashBoard();
        }
        else if (type==  R.id.continueBtn)


                getNewGameResponse();

        else if (type==R.id.noText) {
        }

            else if (type== R.id.yesTv) {
            //Continue


            getHomePageResponse(true);
        }



    }

    private void getHomePageResponse(boolean showProgress) {

        Intent intent = new Intent(this, HomeBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.FROM_SPLASH, true);

        Utils.setAppLanguage(this);
        this.finish();
        this.startActivity(intent);
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
    public void onEventListener(int type, Object object) {
        if (object.toString().equalsIgnoreCase(Constants.TODAY_GAME_LIMIT_REACHED + ""))
            callDashBoard();

    }

    @Override
    public void onBackPressed() {
        callSuccessOrDashBoardOnResponse();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        String url = Constants.GET_NEW_GAME_URL;
        String id = EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(), Constants.SELECTED_CATEGORY);
        String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
        url = url.replace("{langId}", langId);
        url = url.replace("{categoryId}", id);

        if (requestType.equalsIgnoreCase(url)) {
            if (object instanceof NewGameResponse) {
                newGameResponse = (NewGameResponse) object;


                if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                    onVolleyErrorListener(getString(R.string.session_closed));
                    callDashBoard();
                } else {
                    if (newGameResponse.getContents() != null) {
                        NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                        Intent intent = new Intent(this, VideoActivity.class);
                        intent.putExtra(Constants.DATA, filteredLangGameResp);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);

                    }
                    finish();
                }


            } else if (object instanceof CommonResponse) {
                CommonResponse commonResponse = (CommonResponse) object;
                if (commonResponse.getCode() == Constants.TODAY_GAME_LIMIT_REACHED) {
                    //game limit exceeded.
                    MyDialog.showCommonDialogWithType(this, this, getString(R.string.alert), commonResponse.getMessage()
                            , getString(R.string.okay), Constants.TODAY_GAME_LIMIT_REACHED);
                }
            }
        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.SUCCESS) {
                if (totalPoints != null) {
                    EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOYALITY_POINTS, String.valueOf(totalPoints.intValue()));
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form_results);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gameResultResponse = (GameResultResponse) bundle.get(Constants.DATA);
            newGameSubmitRequest = (NewGameSubmitRequest) bundle.get(Constants.OTHER_DATA);
            if (bundle.containsKey("isElegibilityCase")) {

                isElegibilityCase = bundle.getBoolean("isElegibilityCase");
            }
        }
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialize() {
        try {
            findViewById(R.id.backIv).setOnClickListener(this);
            profilePicIV = findViewById(R.id.profilePicIV);
            progressRecyclerView = findViewById(R.id.answerStatusRecyclerView);
            TextView bottomDescTxt = findViewById(R.id.bottomDescTxt);
            totalPointTv = findViewById(R.id.totalPointTv);
            winPointsTv = findViewById(R.id.winPointsTv);
            RelativeLayout profileRL = findViewById(R.id.profileRL);
            profileRL.setOnClickListener(this);
            pointsRL = findViewById(R.id.pointsRL);
            TextView topTitleTv = findViewById(R.id.topTitleTv);
            pointsRL.setOnClickListener(this);
            scratchTextView = findViewById(R.id.scratch_view);
            exitTv = findViewById(R.id.exitTv);
            TextView winOrLossScratchDescTv = findViewById(R.id.winOrLossScratchDescTv);
            questionTV=findViewById(R.id.questionTV);

            userDetails = (User) EightfoldsUtils.getInstance().
                    getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
            if (userDetails != null && userDetails.getProfilePicId() != null && userDetails.getProfilePicId().intValue() != 0) {
                loadProfilePic(userDetails.getProfilePicId());
            }

            questionTV.setText(gameResultResponse.getSurveyResults().get(0).getQuestionTxt());


            int currentLevel = 1;
            if (userDetails != null && userDetails.getCurrentLevel() != null) {
                currentLevel = userDetails.getCurrentLevel().intValue();
            }

            Utils.setProfileBGBasedOnLevel(this, currentLevel, profileRL);
            topTitleTv.setText(getString(R.string.results));


            String loyalityPoints = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.LOYALITY_POINTS);
            if (!TextUtils.isEmpty(loyalityPoints)) {
                previousPoints = Double.parseDouble(loyalityPoints);

                Utils.adjustPointsFontSizeAndBg(this, loyalityPoints, pointsRL, totalPointTv);

            }

            if (gameResultResponse.getPointsWin() != null) {
                scratchTextView.setText(Integer.toString(gameResultResponse.getPointsWin().intValue()));
            }


            makeExitSpannable();


            setadapter();


            //setNumberofcorrectAnswers();
            if(scratchTextView!=null) {


//                if (gameResultResponse.getGameResultMessage() != null && !TextUtils.isEmpty(gameResultResponse.getGameResultMessage().getMessage())) {
////                    bottomDescTxt.setText(gameResultResponse.getGameResultMessage().getMessage());
//                    bottomDescTxt.setText(Html.fromHtml(gameResultResponse.getGameResultMessage().getMessage(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
//                    bottomDescTxt.setVisibility(View.VISIBLE);
//                }

                if (gameResultResponse.isWinFlag()) {
                    winOrLossScratchDescTv.setText(getString(R.string.win_scratch_desc));
                } else {
                    winOrLossScratchDescTv.setText(getString(R.string.loss_scratch_desc));
                }


                scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
                    @Override
                    public void onRevealed(ScratchTextView tv) {
                        isCardScratched = true;
                        scratchTextView.revealWholeArea();


                        Double currentPoints = 0d;

                        if (gameResultResponse.getPointsWin() != null) {
                            currentPoints = gameResultResponse.getPointsWin();
                        }
                        totalPoints = previousPoints + currentPoints;
                        updateScratchStatus();
                        String points = Integer.toString(totalPoints.intValue());
                        Utils.adjustPointsFontSizeAndBg(FormResultsActivity.this, points, pointsRL, totalPointTv);
                        if (isElegibilityCase || !TextUtils.isEmpty(gameResultResponse.getCongratsMsg())) {
                            performPointsAnimation();

                        } else {
                            if (gameResultResponse.getPointsWin() != null && gameResultResponse.getPointsWin() != 0) {
                                performPointsAnimation();
                            } else {
                                exitTv.setVisibility(View.GONE);
                                MyDialog.showLoseDialog(FormResultsActivity.this, FormResultsActivity.this);
                            }

                        }


                    }

                    @Override
                    public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {

                    }
                });

            }
            //

            findViewById(R.id.scratchLL).post(new Runnable() {
                @Override
                public void run() {
                    ShowCaseUtils.presentResultsShowcaseSequence(FormResultsActivity.this, findViewById(R.id.scratchLL));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setadapter() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<SurveyResult> surveyResults = new ArrayList<>();
        long total = 0;

        for (SurveyResult surveyResult : gameResultResponse.getSurveyResults()) {

            total += surveyResult.getUserCount();

        }

        for (SurveyResult surveyResult : gameResultResponse.getSurveyResults()) {

            surveyResult.setTotalNumberOfUsers(total);

            surveyResults.add(surveyResult);
        }




        FormResultsRecyclerAdapter adapter = new FormResultsRecyclerAdapter(surveyResults, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        progressRecyclerView.setLayoutManager(layoutManager);
        progressRecyclerView.setAdapter(adapter);
    }

    private void performPointsAnimation() {
        winPointsTv.setText(String.format(Utils.getCurrentLocale(), "+ %s", gameResultResponse.getPointsWin().intValue() + "")); //TO DO replace with points received in the scratch view
        if (gameResultResponse.getPointsWin() != null) {


            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 200);
            translateAnimation.setDuration(500);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    winPointsTv.setVisibility(View.INVISIBLE);
                    if (isElegibilityCase || !TextUtils.isEmpty(gameResultResponse.getCongratsMsg())) {
                        callSuccessPage();
                    } else if (gameResultResponse.getPointsWin() != null && gameResultResponse.getPointsWin() != 0) {
                        exitTv.setVisibility(View.GONE);
                        wonDialog = MyDialog.showWonDialog(FormResultsActivity.this, scratchTextView.getText().toString(), FormResultsActivity.this);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            winPointsTv.startAnimation(translateAnimation);
        }

    }

    private void loadProfilePic(long profilePicId) {
        Glide.with(this)
                .load(Constants.FILE_URL + profilePicId)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_user_filled)
                .error(R.drawable.ic_user_filled)
                .into(profilePicIV);
    }

    private void updateScratchStatus() {
        String url = Constants.SCRATCH_URL.replace("{scratchCardId}", gameResultResponse.getScratchCardId() + "");
        EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.PUT, url);


    }

    private void makeExitSpannable() {
        SpannableString exitSpan = makeLinkSpan(getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSuccessOrDashBoardOnResponse();
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        makeLinksFocusable(exitTv);
    }


    private void callSuccessOrDashBoardOnResponse() {

        if (!isCardScratched) {
            MyDialog.showTwoButtonDialog(this, this, getString(R.string.info),
                    getString(R.string.not_scratched), getString(R.string.continue_text), getString(R.string.cancel));
        } else {
            if (gameResultResponse.isWinFlag() && gameResultResponse.isEligible() && gameResultResponse.getNoOfGameForEligible() == gameResultResponse.getNoOfGameWins()) {
                callSuccessPage();
            } else {
                callDashBoard();
            }
        }
    }

    private void callDashBoard() {
        Intent intent = new Intent(FormResultsActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void callSuccessPage() {

        Intent intent = new Intent(this, CongratulationsActivity.class);

        intent.putExtra("isElegibilityCase", isElegibilityCase);
        intent.putExtra(Constants.DATA, gameResultResponse);
        intent.putExtra("numberOfWins", gameResultResponse.getNoOfGameWins());



        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.doneTv) {
            callSuccessOrDashBoardOnResponse();
        } else if (v.getId() == R.id.backIv) {
            callSuccessOrDashBoardOnResponse();
        } else if (v.getId() == R.id.playMoreGamesRL) {
            getNewGameResponse();

        } else if (v.getId() == R.id.pointsRL) {


            Intent todayIntent = new Intent(this, LoyaltyPointsHistoryActivity.class);
            if (userDetails != null && userDetails.getTotalLoyalityPoints() != null) {
                todayIntent.putExtra("points", userDetails.getTotalLoyalityPoints());
            }

            ActivityOptionsCompat historyactivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this);
            ActivityCompat.startActivity(this, todayIntent, historyactivityOptions.toBundle());


        } else if (v.getId() == R.id.profileRL) {
            Intent intent = new Intent(this, ProfileActivity.class);
            if (userDetails != null && userDetails.getProfilePicId() != null) {
                intent.putExtra("profilepic", userDetails.getProfilePicId());
            }
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    new Pair<View, String>(v,
                            ProfileActivity.VIEW_NAME_HEADER_IMAGE));
            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());

        }

    }
}
