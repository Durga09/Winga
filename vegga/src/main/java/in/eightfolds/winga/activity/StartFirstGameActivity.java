package in.eightfolds.winga.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class StartFirstGameActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack {

    private RelativeLayout playGameRL;
    private TextView pointRupeeTv;
    private User userDetails;
    private boolean fromLogin = false;
    private ImageView regSuccessGifIV;
    private TextView registrationdescTv, successTv, playPointsTv, playGameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_firstgame);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("fromLogin")) {
                fromLogin = bundle.getBoolean("fromLogin");
            }
        }
        initialize();
    }

    @SuppressLint("StringFormatInvalid")
    private void initialize() {

        userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        playGameRL = findViewById(R.id.playGameRL);
        pointRupeeTv = findViewById(R.id.pointRupeeTv);
        regSuccessGifIV = findViewById(R.id.regSuccessGifIV);
        playGameRL.setOnClickListener(this);
        registrationdescTv = findViewById(R.id.registrationdescTv);
        successTv = findViewById(R.id.successTv);
        playPointsTv = findViewById(R.id.playPointsTv);
        playGameTv = findViewById(R.id.playGameTv);

        if (fromLogin) {
            registrationdescTv.setVisibility(View.GONE);
            successTv.setText(getString(R.string.welcome));
        }

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
        int points = 1;
        String rupees = "";
        if (setup != null && setup.getRupesForEachPoint() != null) {
            rupees = Integer.toString(setup.getRupesForEachPoint().intValue());
        }
        pointRupeeTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.point_rupee), points + "", rupees + ""));
        String firstGameWinAmt = "";

        if (setup != null && setup.getFirstGameWinAmt() != null) {
            firstGameWinAmt = Integer.toString(setup.getFirstGameWinAmt().intValue());
        }
        playPointsTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.play_game_get_rupees),""));

        if (userDetails != null && userDetails.isFirstGamePlayed()) {
            playPointsTv.setVisibility(View.GONE);
        } else {
            playPointsTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.playGameRL) {
            if (userDetails != null && userDetails.isFirstGamePlayed()) {
                getSetup();
            } else {
                login();
            }
        }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finish();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {

            goToDashBoardActivity((HomePageResponse) object);
        } else if (object instanceof LoginData) {
            LoginData loginData = (LoginData) object;

            EightfoldsUtils.getInstance().saveToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());
            if (loginData.isFirstGamePlayed()) {
                EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRST_GAME_PLAYED, "true");
                getSetup();
            } else {

                if (loginData.getNewGameResponse() != null && loginData.getNewGameResponse().getContents().size() > 0) {
                    NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, loginData.getNewGameResponse());

                    Intent intent = new Intent(this, VideoActivity.class);
                    intent.putExtra(Constants.DATA, filteredLangGameResp);
                    intent.putExtra("firstgame", true);
                    startActivity(intent);
                } else {

                    //todo if the user has got the response of newgameresponse is null then he is directly coming to this line

                    onVolleyErrorListener(getString(R.string.somthing_wrong));
                }
            }

        }else if(object instanceof Setup){
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
            getHomePageResponse();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    private void getHomePageResponse() {
        goToDashBoardActivity(new HomePageResponse());
    }


    private void login() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.LOGIN_URL;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingStringRequest(this, LoginData.class, Request.Method.GET, url);
        }
    }

    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_SETUP_SECURE;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }

    private void goToDashBoardActivity(HomePageResponse homePageResponse) {

        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(this);
        if (homePageAd != null) {
            Intent intent = new Intent(this, AdActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
            finish();
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);

            startActivity(intent);
            finish();
        }
    }
}
