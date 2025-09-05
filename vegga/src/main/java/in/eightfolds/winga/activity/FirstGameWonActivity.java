package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.FirstGameResultResponse;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 03-May-18.
 */

public class FirstGameWonActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack {

    private RelativeLayout redeemPointsRL;
    private View redeemPointsLay;
    private TextView pointRupeeTv, pointsWonTv;
    private FirstGameResultResponse firstGameResultResponse;
    private boolean firstgame;
    private Button skipBtn;
    private Double totalAmountWon;
    private Dialog paytmTransferDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_game_won);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                firstGameResultResponse = (FirstGameResultResponse) bundle.get(Constants.DATA);
            }

            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }
        initialize();
    }

    private void initialize() {
        redeemPointsRL = findViewById(R.id.redeemPointsRL);
        redeemPointsLay = findViewById(R.id.redeemPointsLay);
        pointRupeeTv = findViewById(R.id.pointRupeeTv);
        pointsWonTv = findViewById(R.id.pointsWonTv);
        skipBtn = findViewById(R.id.skipBtn);
        redeemPointsRL.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
        findViewById(R.id.paytmLL).setOnClickListener(this);

        if (firstGameResultResponse != null && firstGameResultResponse.getAmtWin() != null) {
            if (firstGameResultResponse.getAmtWin() > 0) {
                totalAmountWon = firstGameResultResponse.getAmtWin();
                pointsWonTv.setText(firstGameResultResponse.getMessage());

            } else {
                redeemPointsLay.setVisibility(View.INVISIBLE);
                redeemPointsRL.setVisibility(View.INVISIBLE);
                pointsWonTv.setVisibility(View.INVISIBLE);
            }
        } else {
            redeemPointsRL.setVisibility(View.INVISIBLE);
            redeemPointsLay.setVisibility(View.INVISIBLE);
            pointsWonTv.setText(firstGameResultResponse.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (redeemPointsLay.getVisibility() == View.VISIBLE) {
            redeemPointsLay.setVisibility(View.GONE);
        } else {
            if (firstgame) {
                MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
            } else {
                getSetup();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.redeemPointsRL) {
            redeemPointsLay.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.paytmLL) {
            String mobile = Utils.getMobileNumber(this);
            paytmTransferDialog = MyDialog.showPayTmTransferDialog(this, mobile, totalAmountWon.floatValue(), this, null);

        } else if (v.getId() == R.id.skipBtn) {
            getSetup();
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
        if (type == R.id.continueBtn) {
            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                EightfoldsVolley.getInstance().showProgress(this);
                String mobile = object.toString();
                String url = Constants.RETRY_REDEEM_AMOUNT_TO_PAYTM;
                url = url.replace("{mobile}", mobile);
                if (firstGameResultResponse.getRefType() != null) {
                    url = url.replace("{refType}", firstGameResultResponse.getRefType().toString());
                } else {
                    url = url.replace("{refType}", "");
                }

                if (firstGameResultResponse.getRefId() != null) {
                    url = url.replace("{refId}", firstGameResultResponse.getRefId().toString());
                } else {
                    url = url.replace("{refId}", "");
                }

                EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.POST, url);
            }
        } else if (type == R.id.yesTv) {
            redeemPointsLay.setVisibility(View.GONE);
            getSetup();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
            goHome();
        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;

            if (paytmTransferDialog != null && paytmTransferDialog.isShowing()) {
                paytmTransferDialog.dismiss();
            }
            if (commonResponse.getCode() == Constants.SUCCESS) {
                Intent intent = new Intent(this, PaytmTransferSuccessfulActivity.class);
                intent.putExtra(Constants.FROM_SPLASH, true);
                startActivity(intent);
            } else {
                MyDialog.showToast(this, getString(R.string.something_wrong));
            }

        } else if (object instanceof Setup) {
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
            goHome();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_SETUP_SECURE;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }


//

    private void goHome() {
        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(this);

        if (homePageAd != null) {
            Intent intent = new Intent(this, AdActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
            finish();
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.FROM_SPLASH, true);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
}

