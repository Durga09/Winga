package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class PaytmTransferSuccessfulActivity extends BaseActivity implements VolleyResultCallBack {

    private Button continueBtn;
    private boolean fromSplash;
    private boolean isVoucherTransfer, isRetry;
    private TextView descTv;
    private ImageView logoIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_paytm_transfer_successful);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.FROM_SPLASH)) {
                fromSplash = bundle.getBoolean(Constants.FROM_SPLASH);
            }
            if (bundle.containsKey(Constants.IS_VOUCHER_TRANSFER)) {
                isVoucherTransfer = bundle.getBoolean(Constants.IS_VOUCHER_TRANSFER);
            }
            if (bundle.containsKey(Constants.IS_RETRY)) {
                isRetry = bundle.getBoolean(Constants.IS_RETRY);
            }
        }
        initialize();
    }

    private void initialize() {
        continueBtn = findViewById(R.id.continueBtn);
        descTv = findViewById(R.id.descTv);
        logoIv = findViewById(R.id.logoIv);
        continueBtn.setOnClickListener(this);

        TextView successTv = findViewById(R.id.successTv);
        successTv.setText(String.format(Utils.getCurrentLocale(), "%s !", getString(R.string.successful)));

        if (isVoucherTransfer) {
            if(isRetry){
                descTv.setText(getString(R.string.voucher_transfer_successful));
            }else {
                descTv.setText(getString(R.string.voucher_transfer_successful));
            }
            logoIv.setImageResource(R.drawable.voucher);

        } else {
            descTv.setText(getString(R.string.transferred_to_paytm));
            logoIv.setImageResource(R.drawable.paytm_logo);
        }

    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.continueBtn) {
            getSetup();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
            //goHome((HomePageResponse) object);
            goHome();
        }
        else if(object instanceof  Setup){
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
        //    getHomePageResponse();
            goHome();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


//    private void getHomePageResponse() {
//        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//            String url = Constants.GET_HOME_PAGE_RESPONSE;
//            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
//            url = url.replace("{langId}", selectedLangId);
//            EightfoldsVolley.getInstance().showProgress(this);
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
//        }
//    }

    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_SETUP_SECURE;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }

    private void goHome() {
//        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(this);
//
//        if (fromSplash && homePageAd != null) {
//            Intent intent = new Intent(this, AdActivity.class);
//            if (homePageResponse != null) {
//                intent.putExtra(Constants.HOME_DATA, homePageResponse);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra(Constants.FROM_SPLASH, true);
//            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
//            finish();
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            if (homePageResponse != null) {
//                intent.putExtra(Constants.HOME_DATA, homePageResponse);
//            }
//            intent.putExtra(Constants.FROM_SPLASH, true);
//            startActivity(intent);
//            finish();
        Intent intent = new Intent(this, HomeBaseActivity.class);
        startActivity(intent);
        finish();

    }
}

