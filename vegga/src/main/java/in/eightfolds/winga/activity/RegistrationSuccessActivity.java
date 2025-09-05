package in.eightfolds.winga.activity;

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
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 03-May-18.
 */

public class RegistrationSuccessActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack {

    private RelativeLayout playGameRL;
    private TextView pointRupeeTv;
    //  private NewGameResponse newGameResponse;
    private User userDetails;
    private boolean fromLogin = false;
    private ImageView regSuccessGifIV;
    private TextView registrationdescTv, successTv, playPointsTv, playGameTv, watchTutorialTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration_success);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("fromLogin")) {
                fromLogin = bundle.getBoolean("fromLogin");
            }
        }
        initialize();
    }

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
        watchTutorialTv = findViewById(R.id.watchTutorialTv);

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

        Utils.loadGifResourceToImageView(this, regSuccessGifIV, R.drawable.registration_successful);
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
                Intent intent = new Intent(this, V2HomeFeatureActivity.class);

                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    public void onEventListener() {
        //Intentionally left blank
    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finish();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {
        //Intentionally left blank
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
            goToDashBoardActivity((HomePageResponse) object);
        } else if (object instanceof Setup) {
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
            goToDashBoardActivity(new HomePageResponse());
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


    private void getHomePageResponse() {
        Intent intent = new Intent(this, HomeBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.FROM_SPLASH, true);

        Utils.setAppLanguage(this);
        this.finish();
        this.startActivity(intent);
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
        } else {
            Intent intent = new Intent(this, V2HomeFeatureActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            startActivity(intent);
            finish();
        }
    }
}
