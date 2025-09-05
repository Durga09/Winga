package in.eightfolds.winga.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.BaseActivity;
import in.eightfolds.winga.activity.FormResultsActivity;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.ResultsActivity;
import in.eightfolds.winga.activity.VideoActivity;
import in.eightfolds.winga.activity.FirstGameWonActivity;
import in.eightfolds.winga.adapter.QuestionOptionsAdapter;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.ContentResponse;
import in.eightfolds.winga.model.DurationDetails;
import in.eightfolds.winga.model.FirstGameResultResponse;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.NewGameSubmitRequest;
import in.eightfolds.winga.model.QuestionOptionResponse;
import in.eightfolds.winga.model.QuestionResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ContentViewDetailsManager;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.ShowCaseUtils;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 18-Jun-18.
 */
public class QuestionsAndAnswersFragment extends Fragment implements View.OnClickListener, OnEventListener, VolleyResultCallBack {

    private static String TAG = QuestionsAndAnswersFragment.class.getSimpleName();
    private TextView questionTv,commentsTv;
    private Button nextBtn;

    private NewGameResponse newGameResponse;
    private RecyclerView recyclerView_options;
    private QuestionOptionsAdapter adapter;
    private boolean firstgame;
    private int currentContentPosition = 0;
    private int currentQuestionPosition = 0;
    private ContentResponse currentContent;
    private QuestionResponse currentQuestion;
    private int totalQuestionsInCurrentContent = 0;
    private int totalContents;
    private TextView gotoHomeBtn;
    private ImageView watchVideoIv;
    private TextView totalquestionsTv, questionProgressTv;
    private ImageView backIv;
    private boolean fullScreen;
    private int[] lastTouchDownXY = new int[2];
    private View startPlayerView;
    Bundle bundle;


    private FragmentActivity myContext;
    private long durationDetailsId;
    private AppDataBase appDataBase;
    private ScrollView questionAnswerdsScrollview;
    NewGameSubmitRequest newGameSubmitRequest;
    WatchVideoFragment watchVideoFragment;
    QuestionsAndAnswersFragment questionsAndAnswersFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_questions_answers, container, false);
        Logg.v(TAG, "**onCreateView");
        if (Constants.captureDurationDetails) {
            appDataBase = AppDataBase.getAppDatabase(myContext);
        }
        bundle = getArguments();
        if (bundle != null) {
            newGameResponse = (NewGameResponse) bundle.get(Constants.DATA);

            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }
        // ViewCompat.setTransitionName(iv_profile_centre, VIEW_NAME_HEADER_IMAGE);
        totalContents = newGameResponse.getAppcontents().size();
        currentContentPosition = newGameResponse.getCurrentContentID();
        currentContent = newGameResponse.getAppcontents().get(currentContentPosition);
        currentQuestionPosition = currentContent.getCurrentQuestion();

        currentQuestion = currentContent.getQuestions().get(currentQuestionPosition);

        if (Constants.isForTestingVegga2) {
            if (currentQuestion.getOptions() != null && currentQuestion.getOptions().size() > 0) {
                for (QuestionOptionResponse option : currentQuestion.getOptions()) {
                    if (option.isCorrect()) {
                        Toast.makeText(myContext, option.getTxt(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        }

        if (currentQuestionPosition != 0) {
            //overridePendingTransition(R.anim.slide_in_long, R.anim.slide_out_long);
        }
        initialize(rootview);
        return rootview;
    }


    @Override
    public void onResume() {
        super.onResume();
        Logg.v(TAG, "$$$onResume() ");
        setQuestionDurationDetails();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
        Logg.v(TAG, "**onAttach");

    }

    @Override
    public void onPause() {
        super.onPause();
        Logg.v(TAG, "$$$onPause() ");
        updateQuestionDurationDetails();
    }

    public void setQuestionDurationDetails() {
        watchVideoIv.setEnabled(true);
        if (Constants.captureDurationDetails) {
            if (currentQuestion != null) {
                DurationDetails durationDetails = new DurationDetails();
                UUID uniqueId = UUID.randomUUID();
                durationDetails.setContentId(currentQuestion.getContentId());
                durationDetails.setUniqueUuid(uniqueId);
                durationDetails.setQuestionId(currentQuestion.getqId());
                durationDetails.setStartMillis(System.currentTimeMillis());
                durationDetails.setGameId(newGameResponse.getCategoryGameId());
                durationDetailsId = appDataBase.getDurationDetailsDao().insert(durationDetails);

                List<DurationDetails> durationDetailsList = appDataBase.getDurationDetailsDao().getAll();
                Logg.v(TAG,"*durationDetailsList > "+durationDetailsList);

            }
        }
    }

    private void updateQuestionDurationDetails() {
        if (Constants.captureDurationDetails) {
            DurationDetails durationDetails = appDataBase.getDurationDetailsDao().getDurationDetailsForId(durationDetailsId);

            if (durationDetails != null) {
                long startMills = durationDetails.getStartMillis();
                long endMills = System.currentTimeMillis();
                durationDetails.setEndMills(endMills);
                long activeMillis = endMills - startMills;
                durationDetails.setQuestionActivemillis(activeMillis);

                appDataBase.getDurationDetailsDao().update(durationDetails);

                List<DurationDetails> durationDetailsList = appDataBase.getDurationDetailsDao().getAll();
                Logg.v(TAG,"*durationDetailsList > "+durationDetailsList);
                durationDetailsId = 0;
            }
        }
    }

    private void initialize(final View rootview) {
        try {

            gotoHomeBtn = rootview.findViewById(R.id.gotoHomeBtn);
            backIv = rootview.findViewById(R.id.backIv);
            questionTv = rootview.findViewById(R.id.questionTv);
            commentsTv=rootview.findViewById(R.id.commentsTv);
            //   linear_progressHeader = findViewById(R.id.linear_progressHeader);
            nextBtn = rootview.findViewById(R.id.nextBtn);
            recyclerView_options = rootview.findViewById(R.id.recyclerView_options);

            totalquestionsTv = rootview.findViewById(R.id.totalquestionsTv);
            questionAnswerdsScrollview = rootview.findViewById(R.id.questionAnswerdsScrollview);
            questionProgressTv = rootview.findViewById(R.id.questionProgressTv);
            watchVideoIv = rootview.findViewById(R.id.watchVideoIv);
            Logg.v(TAG, "**initialize .. watchVideoIv");
            startPlayerView = rootview.findViewById(R.id.startPlayerView);

            nextBtn.setOnClickListener(this);
            backIv.setOnClickListener(this);
            backIv.setVisibility(View.VISIBLE);
            rootview.findViewById(R.id.backIv).setOnClickListener(this);
            gotoHomeBtn.setOnClickListener(this);
            totalQuestionsInCurrentContent = currentContent.getQuestions().size();



            if (!TextUtils.isEmpty(currentQuestion.getTxt())) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    questionTv.setText(Html.fromHtml(currentQuestion.getTxt(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    questionTv.setText(Html.fromHtml(currentQuestion.getTxt()));
                }
            }
            if (currentQuestion.getTxt().length() < 100) {
                questionTv.setTextSize(18);
            } else {
                questionTv.setTextSize(14);
            }


            Logg.v(TAG, "Current Content ID : " + currentContentPosition);
            setAdapter();

            if (firstgame) {
                //rootview.findViewById(R.id.progressScrollV).setVisibility(View.GONE);
                nextBtn.setText(getString(R.string.submit));
                gotoHomeBtn.setVisibility(View.GONE);

            } else {
                if (totalContents - 1 == currentContentPosition && totalQuestionsInCurrentContent - 1 == currentQuestionPosition) {
                    //If it is last question in last content
                    nextBtn.setText(getString(R.string.submit));
                }
                //   Utils.setProgressHeader(this, linear_progressHeader, totalQuestionsInCurrentContent, currentQuestionPosition + 1);
            }
            if(newGameResponse.getSurvey()){
                commentsTv.setVisibility(View.VISIBLE);
            }else{
                commentsTv.setVisibility(View.GONE);
            }

            totalquestionsTv.setText(Integer.toString(totalQuestionsInCurrentContent));
            questionProgressTv.setText(Integer.toString(currentQuestionPosition + 1) + "/");

            makeHomeSpannable();


            watchVideoIv.setOnClickListener(this);


            watchVideoIv.post(new Runnable() {
                @Override
                public void run() {

                  //  ShowCaseUtils.presentQuestionsAnswersShowcaseSequence(myContext, watchVideoIv, nextBtn);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setAdapter() {
        if (currentQuestion.getOptions() != null && currentQuestion.getOptions().size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(myContext);
            layoutManager.setStackFromEnd(true);
            recyclerView_options.setLayoutManager(layoutManager);
            adapter = new QuestionOptionsAdapter(myContext, currentQuestion, this);
            recyclerView_options.setAdapter(adapter);


            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {

                    //   questionAnswerdsScrollview.smoothScrollTo(0, 0);
                    //   recyclerView_options.scrollToPosition(0);
                }
            });
        }

    }


    private void callVideo() {

        Rect endPosition = new Rect();
        startPlayerView.getGlobalVisibleRect(endPosition);
        Logg.v(TAG, "**** getGlobalVisibleRect height>> " + endPosition.height() + " Width>> " + endPosition.width() + "  CenterX >> " + endPosition.centerX()
                + "  CenterY >> " + endPosition.centerY());


        int[] viewLocation = new int[2];
        startPlayerView.getLocationOnScreen(viewLocation);


        int relativeLeft = viewLocation[0];
        int relativeTop = viewLocation[1];
        Logg.v(TAG, " relativeLeft>> " + relativeLeft + "  ,relativeTop>> " + relativeTop);

                       /* int pixelsWidth = Utils.getDpAsPixels(myContext, watchVideoIv.getWidth());
                        int pixelsHeight = Utils.getDpAsPixels(myContext, watchVideoIv.getHeight());
                        Logg.v(TAG, "pixelsWidth>> " + pixelsWidth + " ,pixelsHeight>> "+pixelsHeight);*/

        int centreX = (int) (relativeLeft + startPlayerView.getWidth() / 2);  //watchVideoIv.getX()
        int centreY = (int) (relativeTop + startPlayerView.getHeight() / 2);  //watchVideoIv.getY()

        Logg.v(TAG, "centreX>> " + centreX + " ,centreY>> " + centreY);
        lastTouchDownXY[0] = centreX; //relativeLeft +
        lastTouchDownXY[1] = centreY;  //- 45 Testing relativeTop +
        Logg.v(TAG, "lastTouchDownXY X>> " + lastTouchDownXY[0] + " ,Y>>  " + lastTouchDownXY[1]);

         watchVideoFragment = new WatchVideoFragment();
        FragmentTransaction fragTransaction = myContext.getSupportFragmentManager().beginTransaction();
        bundle.putBoolean("isFromQuestion", true);
        bundle.putIntArray("xy", lastTouchDownXY);

        watchVideoFragment.setArguments(bundle);
        updateQuestionDurationDetails();
        fragTransaction.add(R.id.container, watchVideoFragment);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id == R.id.watchVideoIv) {
            watchVideoIv.setEnabled(false);
            callVideo();
        } else if (id == R.id.gotoHomeBtn) {
            // No action defined
        } else if (id == R.id.backIv) {
            showGotoHomePopUp();
        } else if (id == R.id.nextBtn) {
            executeOnNext();
        }


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logg.v(TAG, "** isVisibleToUser>> " + isVisibleToUser);
    }

    private void executeOnNext() {
        try {
            if (currentQuestion.getSelectedOppId() == null || currentQuestion.getSelectedOppId() == 0) {
                onVolleyErrorListener(getString(R.string.please_select_answer_to_continue));
            } else {
                updateQuestionDurationDetails();
                (new UsageAnalytics()).trackAnswerQuestion("", currentQuestion);
                if (firstgame) {
                    currentContent.getQuestions().set(currentQuestionPosition, currentQuestion);
                    newGameResponse.getAppcontents().set(currentContentPosition, currentContent);
                    submitFirstGame();
                } else {
                    Logg.v(TAG, "currentContentPosition >> " + currentContentPosition);
                    Logg.v(TAG, "currentQuestionPosition >> " + currentQuestionPosition);
                    if(newGameResponse.getSurvey()){
                        if(!commentsTv.getText().toString().isEmpty()){
                            currentQuestion.setComment(commentsTv.getText().toString().trim());
                        }

                    }
                    currentContent.getQuestions().set(currentQuestionPosition, currentQuestion);
                    newGameResponse.getAppcontents().set(currentContentPosition, currentContent);


                    boolean isLastQuestion = false;
                    if (currentQuestionPosition + 1 >= currentContent.getQuestions().size()) {
                        isLastQuestion = true;
                    }
                    boolean isLastContent = false;
                    if ((currentContentPosition + 1 >= newGameResponse.getAppcontents().size())) {
                        isLastContent = true;
                    }

                    if (isLastContent && isLastQuestion) {
                        submitGame();
                    } else {
                        if (isLastQuestion && currentContentPosition + 1 < newGameResponse.getAppcontents().size()) {
                            newGameResponse.setCurrentContentID(currentContentPosition + 1);
                        }
                        if (currentQuestionPosition + 1 < currentContent.getQuestions().size()) {
                            currentContent.setCurrentQuestion(currentQuestionPosition + 1);
                        }

                        if (!isLastQuestion) {
                             questionsAndAnswersFragment = new QuestionsAndAnswersFragment();
                            questionsAndAnswersFragment.setArguments(bundle);
                            myContext.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, questionsAndAnswersFragment)
                                    .commit();
                        } else {
                            Intent intent = new Intent(myContext, VideoActivity.class);
                            intent.putExtra(Constants.DATA, newGameResponse);
                            // finish();
                            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            myContext.startActivity(intent);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void submitGame() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            newGameSubmitRequest = new NewGameSubmitRequest();
            newGameSubmitRequest.setCategoryGameId(newGameResponse.getCategoryGameId());

            newGameSubmitRequest.setContentViewDetails(ContentViewDetailsManager.calculateDurationDetails(newGameResponse.getCategoryGameId(), appDataBase));
            newGameSubmitRequest.setFirstGame(false);
            newGameSubmitRequest.setCgsId(newGameResponse.getCgsId());
            newGameSubmitRequest.setUserId(newGameResponse.getUserId());
            List<QuestionResponse> questions = new ArrayList<>();
            for (ContentResponse contentResponse : newGameResponse.getAppcontents()) {
                questions.addAll(contentResponse.getQuestions());
            }
            newGameSubmitRequest.setQuestions(questions);

            EightfoldsVolley.getInstance().showProgress(myContext);
            String url = Constants.NEW_SUBMIT_GAME_URL;
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);

            EightfoldsVolley.getInstance().makingJsonRequest(this, GameResultResponse.class, Request.Method.POST, url, newGameSubmitRequest);
        }
    }

    private void makeHomeSpannable() {
        SpannableString homeSpan = makeLinkSpan(getString(R.string.home), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGotoHomePopUp();
            }
        });

        homeSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, homeSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        homeSpan.setSpan(iss, 0, homeSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gotoHomeBtn.setText(homeSpan);
        makeLinksFocusable(gotoHomeBtn);
    }


    private void showGotoHomePopUp() {
        if (firstgame) {
            MyDialog.logoutDialog(myContext, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
        } else {
            MyDialog.logoutDialog(myContext, this, getString(R.string.alert), getString(R.string.do_u_want_go_home), false);
        }
    }

    private void submitFirstGame() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
            NewGameSubmitRequest newGameSubmitRequest = new NewGameSubmitRequest();
            newGameSubmitRequest.setCategoryGameId(newGameResponse.getCategoryGameId());
            newGameSubmitRequest.setFirstGame(true);
            newGameSubmitRequest.setContentViewDetails(ContentViewDetailsManager.calculateDurationDetails(newGameResponse.getCategoryGameId(), appDataBase));
            newGameSubmitRequest.setCgsId(newGameResponse.getCgsId());
            newGameSubmitRequest.setUserId(newGameResponse.getUserId());
            List<QuestionResponse> questions = new ArrayList<>();
            for (ContentResponse contentResponse : newGameResponse.getAppcontents()) {
                for (QuestionResponse questionResponse : contentResponse.getQuestions()) {
                    questions.add(questionResponse);
                }
            }
            newGameSubmitRequest.setQuestions(questions);
            EightfoldsVolley.getInstance().showProgress(myContext);

            String url = Constants.SUBMIT_FIRST_GAME_URL;
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);

            EightfoldsVolley.getInstance().makingJsonRequest(this, FirstGameResultResponse.class, Request.Method.POST, url, newGameSubmitRequest);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof FirstGameResultResponse) {
            FirstGameResultResponse firstGameResultResponse = (FirstGameResultResponse) object;

            ContentViewDetailsManager.deleteTheSubmittedDetails(newGameResponse.getCategoryGameId(), appDataBase);
            EightfoldsUtils.getInstance().saveToSharedPreference(myContext, Constants.FIRST_GAME_PLAYED, "true");
            Intent intent = new Intent(myContext, FirstGameWonActivity.class);
            intent.putExtra(Constants.DATA, firstGameResultResponse);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("firstgame", firstgame);
            //  finish();
            myContext.startActivity(intent);
        } else if (object instanceof GameResultResponse) {
            GameResultResponse result = (GameResultResponse) object;
            ContentViewDetailsManager.deleteTheSubmittedDetails(newGameResponse.getCategoryGameId(), appDataBase);
            callSuccessOrResultsActivity(result);
        }
    }

    private void callSuccessOrResultsActivity(GameResultResponse result) {
        Intent homeRefreshIntent = new Intent();
        homeRefreshIntent.setAction(Constants.HOME_REFRESH_ACTION);
        myContext.sendBroadcast(homeRefreshIntent);
       if(result.isSurvey()){
           Intent intent = new Intent(myContext, FormResultsActivity.class);
           intent.putExtra(Constants.DATA, result);
           intent.putExtra(Constants.OTHER_DATA, newGameSubmitRequest);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           if (result.isWinFlag() && result.isEligible() && result.getNoOfGameForEligible() == result.getNoOfGameWins()) {
               //Eligible For luckyDraw
               intent.putExtra("isElegibilityCase", true);
           } else {
               myContext.startActivity(intent);
           }
           myContext.startActivity(intent);
       }else{

           Intent intent = new Intent(myContext, ResultsActivity.class);
           intent.putExtra(Constants.DATA, result);
           intent.putExtra(Constants.OTHER_DATA, newGameSubmitRequest);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           if (result.isWinFlag() && result.isEligible() && result.getNoOfGameForEligible() == result.getNoOfGameWins()) {
               //Eligible For luckyDraw
               intent.putExtra("isElegibilityCase", true);
           } else {
               myContext.startActivity(intent);
           }
           myContext.startActivity(intent);
       }


    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(myContext, object);
        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.SESSION_END_CODE) {
                goToHome();
            }
            else if(commonResponse.getCode() == Constants.GAME_ALREADY_SUBMITTED){
                goToHome();
            }
        }
    }

    @Override
    public void onEventListener() {
        nextBtn.setBackgroundColor(getResources().getColor(R.color.submit_enable_color));
    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {

            goToHome();

        }
    }

    private void goToHome() {
        if (firstgame) {
            myContext.finish();
        } else {
            Intent intent = new Intent(myContext, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            myContext.startActivity(intent);
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    public SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);

        link.setSpan(new BaseActivity.ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
    public void setImagePropertiesInPortraitMode() {

        if( watchVideoFragment != null ){
             watchVideoFragment.setImagePropertiesInPortraitMode();
        }

    }

    public void setImagePropertiesInLandscapeMode() {
        if( watchVideoFragment != null ){
            watchVideoFragment.setImagePropertiesInLandscapeMode();
        }

    }

}
