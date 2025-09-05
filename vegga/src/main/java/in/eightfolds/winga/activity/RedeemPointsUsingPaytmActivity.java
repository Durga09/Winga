package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class RedeemPointsUsingPaytmActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private TextView totalPointsTv, redeemedPointsTv, availablePointsTv, availableRupeesTv, minRedeemPointsTv, rupeesConversionTv;
    private EditText redeemPointsET;
    private LinearLayout redeemPointsLL;
    private int minimumRedeemablePoints = 0;
    Double rupeesForEachPoint = Double.valueOf(0);
    double pointsFor1Rupee;
    private Double availablePoints;
    private float calculatedAmt;
    private Dialog paytmTransferDialog;
    private int pointsToRedeem;

    private String requestedMobileNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_redeem_points_using_paytm);
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.redeem_points));

        totalPointsTv = findViewById(R.id.totalPointsTv);
        redeemedPointsTv = findViewById(R.id.redeemedPointsTv);
        availablePointsTv = findViewById(R.id.availablePointsTv);
        availableRupeesTv = findViewById(R.id.availableRupeesTv);
        minRedeemPointsTv = findViewById(R.id.minRedeemPointsTv);
        redeemPointsET = findViewById(R.id.redeemPointsET);
        redeemPointsLL = findViewById(R.id.redeemPointsLL);
        rupeesConversionTv = findViewById(R.id.rupeesConversionTv);
        redeemPointsLL.setOnClickListener(this);
        findViewById(R.id.failureReasonsIv).setOnClickListener(this);
        getLoginData();
    }

    private void getLoginData() {

        setDetailsToScreen((User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(RedeemPointsUsingPaytmActivity.this, Constants.USER_DETAILS, User.class));


    }

    private void setDetailsToScreen(User loginData) {
        Double redeemedLoyaltyPoints = Double.valueOf(0);


        if (loginData.getTotalLoyalityPoints() != null) {
            totalPointsTv.setText(Integer.toString(loginData.getTotalLoyalityPoints().intValue()));
        }

        if (loginData.getMinimumRedemptionPoint() != null) {
            minimumRedeemablePoints = loginData.getMinimumRedemptionPoint().intValue();
        }


        minRedeemPointsTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.minimum_x_points), Integer.toString(minimumRedeemablePoints)));

        if (loginData.getRedeemedLoyalityPoints() != null) {
            redeemedLoyaltyPoints = loginData.getRedeemedLoyalityPoints();
        }

        redeemedPointsTv.setText(Integer.toString(redeemedLoyaltyPoints.intValue()));

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
        rupeesForEachPoint = setup.getRupesForEachPoint();

        availablePoints = loginData.getTotalLoyalityPoints() - redeemedLoyaltyPoints;

        float availableRupees = 0;
        if (rupeesForEachPoint != null && rupeesForEachPoint != 0) {
            pointsFor1Rupee = 1 / rupeesForEachPoint;
            redeemPointsET.setHint((int) pointsFor1Rupee);
            availableRupees = (float) (rupeesForEachPoint * availablePoints);
        }

        String formattedAvailableRupees = String.format(getString(R.string.rupee_value), String.format("%.2f", availableRupees));
        availableRupeesTv.setText(formattedAvailableRupees);
        availablePointsTv.setText(Integer.toString(availablePoints.intValue()));

        int editLength = (availablePoints.intValue() + "").length();
        redeemPointsET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editLength)});

        redeemPointsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    if (s.length() > 0) {
                        int enteredPoints = Integer.parseInt(s.toString());

                        if (enteredPoints > availablePoints) {
                            String str = redeemPointsET.getText().toString().substring(0, redeemPointsET.getText().length() - 1);
                            redeemPointsET.setText(str);
                            redeemPointsET.setSelection(redeemPointsET.getText().length());
                            enteredPoints = Integer.parseInt(str);

                        }
                        if (enteredPoints > 0) {

                            float availableRupees = (float) (rupeesForEachPoint * enteredPoints);
                            String formattedRupees = String.format("%.2f", availableRupees);
                            rupeesConversionTv.setText(String.format(getString(R.string.rupee_value), formattedRupees));
                        } else {
                            rupeesConversionTv.setText("");
                        }
                    } else {
                        rupeesConversionTv.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            super.onClick(v);
            if (v.getId() == R.id.failureReasonsIv) {
                MyDialog.showPayTmFailureReasonsPopUp(this);
            }
                else if(v.getId() == R.id.redeemPointsLL) {
                String mobile = Utils.getMobileNumber(this);

                String enteredPoints = redeemPointsET.getText().toString();
                if (availablePoints <= 0) {
                    MyDialog.showAlertDialog(this, null,
                            getString(R.string.alert),
                            getString(R.string.no_points_available),
                            getString(R.string.okay));
                } else if (TextUtils.isEmpty(enteredPoints)) {
                    MyDialog.showAlertDialog(this, null,
                            getString(R.string.alert),
                            getString(R.string.enter_points_to_redeem),
                            getString(R.string.okay));
                } else {
                    pointsToRedeem = Integer.parseInt(enteredPoints);

                    if (pointsToRedeem < minimumRedeemablePoints) {
                        MyDialog.showAlertDialog(this, null,
                                getString(R.string.alert),
                                String.format(Utils.getCurrentLocale(), getString(R.string.redeem_points_more_than_x), minimumRedeemablePoints + ""),
                                getString(R.string.okay));
                    } else if (pointsToRedeem > availablePoints) {
                        MyDialog.showAlertDialog(this, null,
                                getString(R.string.alert),
                                String.format(Utils.getCurrentLocale(), getString(R.string.redeem_points_cannot_be_more_than_x), availablePoints.intValue() + ""),
                                getString(R.string.okay));
                    } else if (pointsToRedeem % pointsFor1Rupee != 0) {
                        MyDialog.showAlertDialog(this, null,
                                getString(R.string.alert),
                                String.format(Utils.getCurrentLocale(), getString(R.string.redeem_points_should_be_in_multiple), (int) pointsFor1Rupee + ""),
                                getString(R.string.okay));
                    } else {
                        calculatedAmt = (float) (pointsToRedeem * rupeesForEachPoint);
                        paytmTransferDialog = MyDialog.showPayTmTransferDialog(this, mobile, calculatedAmt, this, null);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (requestType.toLowerCase().contains("login")) {
            User loginData = (User) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData);
            setDetailsToScreen(loginData);

        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;

            if (paytmTransferDialog != null && paytmTransferDialog.isShowing()) {
                paytmTransferDialog.dismiss();
            }
            if (commonResponse.getCode() == Constants.SUCCESS) {
                if (requestType.contains(requestedMobileNumber)) {
                    requestedMobileNumber = "";
                    Intent intent = new Intent(this, PaytmTransferSuccessfulActivity.class);
                    intent.putExtra(Constants.FROM_SPLASH, false);
                    startActivity(intent);
                } else {
                    if (commonResponse.getData() != null && commonResponse.getData().containsKey("redeemToken")) {
                        requestForRedemption(commonResponse.getData().get("redeemToken").toString());
                    } else {
                        requestedMobileNumber = "";
                       onVolleyErrorListener(object);
                    }
                }

            } else {
                onVolleyErrorListener(object);
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
        if (paytmTransferDialog != null && paytmTransferDialog.isShowing()) {
            paytmTransferDialog.dismiss();
        }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            getLoginData();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {
        if (type == R.id.continueBtn) {
            requestedMobileNumber = object.toString();
            requestForRedeemToken();
        }
    }

    private void requestForRedeemToken() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_LOYALITY_POINTS_REDEEM_TOKEN;
            url = url.replace("{points}", pointsToRedeem + "");
            url = url.replace("{type}", Constants.PAYTM_ID + "");
            UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(this);
            String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.FIRE_BASE_TOKEN);
            userDeviceDetail.setPushRegId(refreshedToken);

            userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(this));
            EightfoldsVolley.getInstance().makingJsonRequest(this, CommonResponse.class, Request.Method.POST, url,userDeviceDetail);
        }
    }

    private void requestForRedemption(String redeemToken) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.REDEEM_LOYALITY_POINTS;
            url = url.replace("{points}", pointsToRedeem + "");
            url = url.replace("{mobile}", requestedMobileNumber);
            url = url.replace("{redeemToken}", redeemToken);
            url = url.replace("{type}", Constants.PAYTM_ID + "");

            EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.POST, url);
        }
    }


}
