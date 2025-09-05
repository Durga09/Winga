package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Iterator;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.VouchersAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.model.Voucher;
import in.eightfolds.winga.model.VoucherMainRequest;
import in.eightfolds.winga.model.VoucherRequestItem;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class VouchersActivity extends BaseActivity implements VolleyResultCallBack {

    private static final String TAG = VouchersActivity.class.getSimpleName();
    RecyclerView vouchersRecyclerView;
    private ArrayList<Voucher> vouchersList;
    private RelativeLayout bottomButtonRL;
    private TextView totalPointsTv, balancePointsTv, noVouchersTv;
    private Double totalAvailableLoyalityPoints;
    private int balanceAfterVouchersAdded;
    private int addedPoints;
    private VouchersAdapter vouchersAdapter;
    private int minimumRedeemablePoints = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vouchers);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                totalAvailableLoyalityPoints = bundle.getDouble(Constants.DATA);
            }
            if (bundle.containsKey(Constants.OTHER_DATA)) {
                minimumRedeemablePoints = bundle.getInt(Constants.OTHER_DATA);
            }
        }

        Log.v(TAG, "** minimum >> " + minimumRedeemablePoints);
        initialize();
    }


    private void initialize() {


        ImageView backIv = findViewById(R.id.backIv);
        vouchersRecyclerView = findViewById(R.id.vouchersRecyclerView);
        bottomButtonRL = findViewById(R.id.bottomButtonRL);
        totalPointsTv = findViewById(R.id.totalPointsTv);
        balancePointsTv = findViewById(R.id.balancePointsTv);
        noVouchersTv = findViewById(R.id.noVouchersTv);

        backIv.setOnClickListener(this);
        bottomButtonRL.setOnClickListener(this);

        setHeader(getString(R.string.vouchers));


        if (totalAvailableLoyalityPoints != null) {
            balanceAfterVouchersAdded = totalAvailableLoyalityPoints.intValue();
            balancePointsTv.setText(String.format(getString(R.string.balance_points), String.valueOf(totalAvailableLoyalityPoints.intValue())));
        }
        totalPointsTv.setText("0");
        //vouchersList = getVouchers();
        // setAdapter();

        getVouchers();
    }


    private void setAdapter() {
        if (vouchersList.size() > 0) {
            vouchersAdapter = new VouchersAdapter(this, vouchersList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
            vouchersRecyclerView.setLayoutManager(layoutManager);
            //LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            //vouchersRecyclerView.setLayoutAnimation(animation);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_divider_line));
            vouchersRecyclerView.addItemDecoration(dividerItemDecoration);
            vouchersRecyclerView.setAdapter(vouchersAdapter);
            findViewById(R.id.bottomButtonRL).setVisibility(View.VISIBLE);
            findViewById(R.id.noItemsLL).setVisibility(View.GONE);
        } else {
            findViewById(R.id.noItemsLL).setVisibility(View.VISIBLE);
            findViewById(R.id.bottomButtonRL).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.noVouchersTv)).setText(String.format(getString(R.string.not_available_now), getString(R.string.vouchers)));
        }
    }


    public boolean UpdatesPointsAndReturnSufficient(int points, boolean isIncrement) {
        boolean isSufficient = false;
        if (isIncrement) {
            if (balanceAfterVouchersAdded >= points) {
                addedPoints += points;
                balanceAfterVouchersAdded -= points;
                isSufficient = true;
            } else {
                MyDialog.showToast(this, getString(R.string.insufficient_points));
                isSufficient = false;
            }
        } else if (addedPoints >= points) {
            addedPoints -= points;
            balanceAfterVouchersAdded += points;
            isSufficient = true;
        }
        balancePointsTv.setText(String.format(getString(R.string.balance_points), String.valueOf(balanceAfterVouchersAdded)));
        totalPointsTv.setText(String.valueOf(addedPoints));
        return isSufficient;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv) {
            finish();
        } else if (v.getId() == R.id.bottomButtonRL) {
            if (addedPoints > 0) {
                requestForRedeemToken();
            } else {
                MyDialog.showToast(this, getString(R.string.select_voucher));
            }
        }
    }


    private void requestForRedeemToken() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_LOYALITY_POINTS_REDEEM_TOKEN;
            url = url.replace("{points}", addedPoints + "");
            url = url.replace("{type}", Constants.VOUCHER_ID + "");
            UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(this);
            String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.FIRE_BASE_TOKEN);
            userDeviceDetail.setPushRegId(refreshedToken);

            userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(this));
            EightfoldsVolley.getInstance().makingJsonRequest(this, CommonResponse.class, Request.Method.POST, url,userDeviceDetail);
        }
    }

    private void redeemVouchers(String redeemToken) {
        ArrayList<Voucher> vouchers = vouchersAdapter.getVouchersList();
        Logg.v(TAG, "vouchers count>" + vouchers.size());
        ArrayList<VoucherRequestItem> voucherRequestItems = new ArrayList<>();
        Iterator<Voucher> it = vouchers.iterator();
        while (it.hasNext()) {
            Voucher voucher = it.next();
            VoucherRequestItem voucherRequestItem = new VoucherRequestItem();
            if (voucher.getVoucherCountSelected() > 0) {
                voucherRequestItem.setDenomination(voucher.getDenomination());
                voucherRequestItem.setProductId(voucher.getProduct_id());
                voucherRequestItem.setQuantity(voucher.getVoucherCountSelected());
                voucherRequestItems.add(voucherRequestItem);
            }
        }
        if (voucherRequestItems.size() > 0) {
            VoucherMainRequest voucherMainRequest = new VoucherMainRequest();
            voucherMainRequest.setVoucherRequests(voucherRequestItems);
            voucherMainRequest.setRedeemReqGroupId(System.currentTimeMillis());
            voucherMainRequest.setPoints(addedPoints);

            if (addedPoints < minimumRedeemablePoints) {
                MyDialog.showToast(this, String.format(Utils.getCurrentLocale(), getString(R.string.redeem_points_more_than_x), minimumRedeemablePoints + ""));
            } else {
                if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                    String url = Constants.REDEEM_VOUCHERS_URL;
                    url = url.replace("{redeemToken}", redeemToken);
                    EightfoldsVolley.getInstance().showProgress(this);
                    EightfoldsVolley.getInstance().makingJsonRequest(this, CommonResponse.class, Request.Method.POST, url, voucherMainRequest);
                }
            }
        } else {
            MyDialog.showToast(this, getString(R.string.select_voucher));
        }
    }

    private void getVouchers() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_VOUCHERS_URL;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Voucher[].class, Request.Method.GET, url);
        }
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof Voucher[]) {
            vouchersList = new ArrayList<>();
            Voucher[] vouchers = (Voucher[]) object;

            Logg.v(TAG, "Count >> " + vouchers.length);
            Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
            Double rupeesForEachPoint = setup.getRupesForEachPoint();

            for (Voucher voucher : vouchers) {
                double voucherAmount = voucher.getDenomination();
                double loyalityPoints = (voucherAmount / rupeesForEachPoint);

                voucher.setLoyalityPoints((int) Math.round(loyalityPoints));
                vouchersList.add(voucher);
            }
            setAdapter();
        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.SUCCESS) {
                if (requestType.contains("redeemToken")) {
                    Intent intent = new Intent(this, PaytmTransferSuccessfulActivity.class);
                    intent.putExtra(Constants.IS_VOUCHER_TRANSFER, true);
                    startActivity(intent);
                } else {
                    if (commonResponse.getData() != null && commonResponse.getData().containsKey("redeemToken")) {
                        redeemVouchers(commonResponse.getData().get("redeemToken").toString());
                    } else {
                        onVolleyErrorListener(object);
                    }
                }
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

}