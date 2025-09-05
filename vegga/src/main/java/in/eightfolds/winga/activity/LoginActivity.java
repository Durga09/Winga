package in.eightfolds.winga.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;

import in.eightfolds.WingaApplication;
import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.fragment.LoginFragment;
import in.eightfolds.winga.fragment.RegisterBaseFrament;
import in.eightfolds.winga.fragment.RegisterDetailsFragment;
import in.eightfolds.winga.interfaces.ListenFromActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, VolleyResultCallBack {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LinearLayout socialLL;
    private TextView loginTv, registerTv;
    private boolean isLoginSelected = true;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    LoginData loginData;
    private String mobileNumber;
    private LinearLayout loginRegisterLL;
    private ImageView logoIv;
    private FrameLayout rootLFL;
    public ListenFromActivity activityListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initialize();
    }



    private void initialize() {
        socialLL = findViewById(R.id.socialLL);
        loginTv = findViewById(R.id.loginTv);
        registerTv = findViewById(R.id.registerTv);
        loginRegisterLL = findViewById(R.id.loginRegisterLL);
        logoIv = findViewById(R.id.logoIv);
        rootLFL = findViewById(R.id.rootLFL);

        loginTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        (findViewById(R.id.fbLL)).setOnClickListener(this);
        (findViewById(R.id.googlePlusLL)).setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
            }
        }

        addLoginFragment();
        callbackManager = CallbackManager.Factory.create();
        initGoogle();

        String imei = EightfoldsUtils.getInstance().getDeviceIMEIWIthSMSPermission(this);
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.IMEI, imei);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (null != activityListener) {
            activityListener.doSomethingInFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(LoginActivity.class.getSimpleName());
    }


    @Override
    public void onBackPressed() {

        String fragmentName = getSupportFragmentManager().findFragmentById(R.id.mainFL).getClass().getSimpleName();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFL);

        Logg.v(TAG, "**fragmentName >> " + fragmentName);
        if (fragmentName.equalsIgnoreCase(RegisterBaseFrament.class.getSimpleName())) {
            highLightLogin();
            super.onBackPressed();
        } else if (f instanceof RegisterDetailsFragment) {
            ((RegisterDetailsFragment) f).onBackPressed();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        EightfoldsUtils.getInstance().hideKeyboard(this);
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOGIN_TYPE, null);

        if (v.getId() == R.id.loginTv) {
            if (!isLoginSelected)
                onLoginClick();
        } else if (v.getId() == R.id.registerTv) {
            if (isLoginSelected)
                onRegisterClick();
        } else if (v.getId() == R.id.fbLL) {

            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOGIN_TYPE, Constants.FB_LOGIN);
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, resultFacebookCallback);
            }


        } else if (v.getId() == R.id.googlePlusLL) {
            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOGIN_TYPE, Constants.GOOGLE_LOGIN);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                }
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == RC_SIGN_IN) {
            Logg.v(TAG, "**google sign in result");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode != 64206) {
            activityListener.onAction(requestCode, resultCode, data);
        }


    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (requestType.contains(Constants.FB_VALIDATE_URL) || requestType.contains(Constants.GOOGLE_VALIDATE_URL)) {
            LoginData loginData = (LoginData) object;
            //Old
            EightfoldsUtils.getInstance().saveToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());

            (new UsageAnalytics()).trackLogin(!TextUtils.isEmpty(loginData.getMobile()) ? loginData.getMobile() : "");

            if (TextUtils.isEmpty(loginData.getAccessToken())) {
                onRegisterClick();

            } else {

                if (loginData.isGoLive()) {
                    if (loginData.isFirstGamePlayed()) {
                        getSetup();
                    } else {
                        playNewGame(loginData);
                    }
                } else {
                    Intent intent = new Intent(this, WingaNotLiveActivity.class);

                    if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
                        intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
                    if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
                        intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
                    if (!TextUtils.isEmpty(loginData.getCurrentTime()))
                        intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
                    finish();
                    startActivity(intent);
                }
            }
        } else if (object instanceof HomePageResponse) {
            goHome((HomePageResponse) object);

        } else if (object instanceof Setup) {
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
            getHomePageResponse();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        if (object != null) {
            if (object instanceof String) {
                Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
            } else if (object instanceof CommonResponse) {
                CommonResponse commonResponse = (CommonResponse) object;
                if (commonResponse.getCode() == Constants.INVALID_USER) {
                    onRegisterClick();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(Constants.CLIENT_ID)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void setLoginFrameInCenter() {
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        loginRegisterLL.setLayoutParams(params1);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rootLFL.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        rootLFL.setLayoutParams(params);
    }

    public void setLoginFrameBelowLogin() {
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.BELOW, R.id.logoIv);
        loginRegisterLL.setLayoutParams(params1);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rootLFL.getLayoutParams();
        int dpValue = 40; // margin in dips
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d); // margin in pixels
        params.setMargins(0, 0, 0, margin);
        rootLFL.setLayoutParams(params);
    }

    public void setActivityListener(ListenFromActivity activityListener) {
        this.activityListener = activityListener;
    }

    private void addRegisterBaseFragment() {
        Bundle bundle = new Bundle();
        RegisterBaseFrament registerBaseFrament = new RegisterBaseFrament();
        registerBaseFrament.setArguments(bundle);
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        fragTransaction.replace(R.id.mainFL, registerBaseFrament);
        fragTransaction.addToBackStack(null);
        fragTransaction.commitAllowingStateLoss();
    }

    public void onLoginClick() {
        highLightLogin();
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        addLoginFragment();
    }

    public void highLightLogin() {

        isLoginSelected = true;
        socialLL.setVisibility(View.VISIBLE);

        loginTv.setTextColor(getResources().getColor(R.color.app_theme_red));
        setTextsize(loginTv, true);

        registerTv.setTextColor(getResources().getColor(R.color.colorWhite));
        setTextsize(registerTv, false);
    }

    public void onRegisterClick() {
        addRegisterBaseFragment();
        highLightRegister();
    }

    public void highLightRegister() {

        isLoginSelected = false;
        socialLL.setVisibility(View.GONE);

        loginTv.setTextColor(getResources().getColor(R.color.colorWhite));
        setTextsize(loginTv, false);

        registerTv.setTextColor(getResources().getColor(R.color.app_theme_red));
        setTextsize(registerTv, true);
    }

    private void setTextsize(TextView textView, Boolean isHighlight) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (displayMetrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {

            if (isHighlight) {
                textView.setTextSize(16);
            } else {
                textView.setTextSize(12);
            }
        } else {
            if (isHighlight) {
                textView.setTextSize(22);
            } else {
                textView.setTextSize(14);
            }
        }
    }

    private FacebookCallback<LoginResult> resultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest graphRequest = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            AccessToken token = loginResult.getAccessToken();
                            com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                            loginData = new LoginData();
                            loginData.setUsername(token.getUserId());

                            loginData.setPassword(token.getUserId());
                            loginData.setSocialAccessToken(token.getToken());
                            loginData.setSocialProvider(Constants.FACEBOOK);
                            loginData.setSocialProviderUserId(token.getUserId());

                            if (response.getError() != null) {
                                //TODO handle error
                            } else {
                                String email = me.optString("email");
                                String name = me.optString("name");
                                String picture = me.optString("picture");

                                loginData.setEmail(email);

                                loginData.setName(name);


                            }
                            WingaApplication.getInstance().setLoginData(loginData);
                            if (EightfoldsUtils.getInstance().isNetworkAvailable(LoginActivity.this)) {
                                Uri.Builder builder = Uri.parse(Constants.FB_VALIDATE_URL).buildUpon();
                                builder.appendQueryParameter("fbUserId", loginData.getUsername());
                                builder.appendQueryParameter("accessToken", loginData.getSocialAccessToken());
                                builder.appendQueryParameter("langId", Utils.getCurrentLangId());
                                String loginUrl = builder.build().toString();

                                EightfoldsVolley.getInstance().showProgress(LoginActivity.this);
                                EightfoldsVolley.getInstance().makingStringRequest(LoginActivity.this, LoginData.class, Request.Method.GET, loginUrl);
                            }
                        }
                    });

            Bundle parameters = new Bundle();

            //Add the fields that you need, you dont forget add_menu the right permission
            parameters.putString("fields", "email,id,name,gender,picture,cover,birthday");
            graphRequest.setParameters(parameters);

            //Now you can execute
            GraphRequest.executeBatchAsync(graphRequest);
        }

        @Override
        public void onCancel() {
            Logg.d("TAG", "Hi");
            onVolleyErrorListener("facebook login failed");
        }

        @Override
        public void onError(FacebookException e) {
            Logg.d("TAG", "Hi");
            onVolleyErrorListener("facebook login failed");
            if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("User logged in as different Facebook user.")) {
                LoginManager.getInstance().logOut();
                View view = new View(LoginActivity.this);
                view.setId(R.id.fbLL);
                onClick(view);
            }
        }
    };

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String token = acct.getIdToken();
            loginData = new LoginData();
            loginData.setPassword(acct.getId());
            loginData.setName(acct.getDisplayName());
            loginData.setEmail(acct.getEmail());
            loginData.setUsername(acct.getId());
            loginData.setSocialProvider(Constants.GOOGLE);
            loginData.setSocialAccessToken(acct.getIdToken());
            loginData.setSocialProviderUserId(acct.getId());
            WingaApplication.getInstance().setLoginData(loginData);
            if (token != null) {
                if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                    Uri.Builder builder = Uri.parse(Constants.GOOGLE_VALIDATE_URL).buildUpon();
                    builder.appendQueryParameter("googleUserId", loginData.getUsername());
                    builder.appendQueryParameter("accessToken", loginData.getSocialAccessToken());
                    builder.appendQueryParameter("langId", Utils.getCurrentLangId());
                    String loginUrl = builder.build().toString();

                    EightfoldsVolley.getInstance().showProgress(LoginActivity.this);
                    EightfoldsVolley.getInstance().makingStringRequest(LoginActivity.this, LoginData.class, Request.Method.GET, loginUrl);
                }
            }
        }
    }

    private void getHomePageResponse() {
//        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//            String url = Constants.GET_CATEGORIES_LIST;
//            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
//            url = url.replace("{langId}", selectedLangId);
//
//            EightfoldsVolley.getInstance().showProgress(this);
//
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
//        }
      goHome(new HomePageResponse());
    }


    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_SETUP_SECURE;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }

    private void goHome(HomePageResponse homePageResponse) {
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRST_GAME_PLAYED, "true");

        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(this);
        Intent intent;
        if (homePageAd != null) {
            intent = new Intent(this, AdActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
        } else {

            intent = new Intent(this, V2HomeFeatureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        finish();
        startActivity(intent);
    }

    private void playNewGame(LoginData loginData) {
        Intent intent;
        intent = new Intent(this, V2HomeFeatureActivity.class);
        intent.putExtra("fromLogin", true);
        finish();
        startActivity(intent);
    }


    private void addLoginFragment() {
        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(mobileNumber)) {
            bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        }
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);

        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(R.anim.slide_in_try, R.anim.slide_out_try);
        fragTransaction.replace(R.id.mainFL, loginFragment);
        fragTransaction.commitAllowingStateLoss();
    }

}

