package in.eightfolds.winga.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.TotalRewardsAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.FilterItemModel;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.model.PrizeWinReponse;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;


public class TotalRewardsActivity extends BaseActivity implements VolleyResultCallBack, SwipeRefreshLayout.OnRefreshListener, OnEventListener {

    public static final String VIEW_NAME_POINTS = "detail:points";
    ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    RecyclerView historyRecyclerView;
    private ArrayList<PrizeWin> historyList;
    private TextView pointsTv, successRedeemedTv;
    private Integer state = 0;
    private ImageView backIv;
    private String totalRewardsAmount = "";
    private boolean paginatedRefresh = false;

    private Dialog paytmTransferDialog;
    private Dialog prizeDialog;
    private String url = "";
    private int requestedType = 0;//1 for Paytm and 2 for Vouchers

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_total_rewards);

        initialize();
    }


    private void initialize() {

        backIv = findViewById(R.id.backIv);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        pointsTv = findViewById(R.id.pointsTv);
        successRedeemedTv = findViewById(R.id.successRedeemedTv);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        backIv.setOnClickListener(this);

        ViewCompat.setTransitionName(pointsTv, VIEW_NAME_POINTS);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(Constants.DATA)) {
                String amount = b.getString(Constants.DATA);
                pointsTv.setText(amount);
            }
        }
        getHistory(false, state);

        historyRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    paginatedRefresh = true;
                    onRefresh();
                }
            }
        });
        showSuccessPoints();
    }

    @SuppressLint("StringFormatMatches")
    private void showSuccessPoints() {
        User userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);


        if (userResponse.getRedemptionAmounts() != null) {
            successRedeemedTv.setText(String.format(getString(R.string.rupee_value), userResponse.getRedemptionAmounts().getSuccessAmt())); // String.format(getString(R.string.paid_amount),));
        }
    }

    @SuppressLint("StringFormatMatches")
    private void showSuccessPoints(float totalRewardsAmt, float successAmt) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat decimalFormat = (DecimalFormat) nf;
        decimalFormat.applyPattern("#.##");
        String twoDigitsF = decimalFormat.format(totalRewardsAmt);
        String totalRewardsAmountText = getString(R.string.rs) + twoDigitsF;
        pointsTv.setText(totalRewardsAmountText);
        successRedeemedTv.setText(String.format(getString(R.string.rupee_value), successAmt));
    }

    private void setAdapter() {


        if (historyList.size() > 0) {
            TotalRewardsAdapter historyRecyclerAdapter = new TotalRewardsAdapter(this, historyList, "", this);
            GridLayoutManager layoutManager;
            int spanCount = 2;
            if (Utils.isTablet(this)) {
                spanCount = 3;
            }
            layoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
            historyRecyclerView.setLayoutManager(layoutManager);


            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            historyRecyclerView.setLayoutAnimation(animation);

//        historyRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL));

            historyRecyclerView.setAdapter(historyRecyclerAdapter);
            (findViewById(R.id.topRL)).setBackground(getResources().getDrawable(R.drawable.winnersbg));
            findViewById(R.id.noItemsLL).setVisibility(View.GONE);

        } else {
            // noItemsTv.setVisibility(View.VISIBLE);

            (findViewById(R.id.topRL)).setBackgroundColor(getResources().getColor(R.color.winga_bg_color));
            findViewById(R.id.noItemsLL).setVisibility(View.VISIBLE);
        }
    }

    private int INTENT_FILTER_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras().containsKey(Constants.DATA)) {
                    FilterItemModel selectedFilterItem = (FilterItemModel) data.getSerializableExtra(Constants.DATA);
                    //TODO implement refresh
                    pageNo = 1;
                    totalItemCount = 0;
                    previousCount = 0;
                    historyList = new ArrayList<>();
                    getHistory(true, selectedFilterItem.getState());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.headerRightIconIv) {
            Intent intent = new Intent(this, HistoryFilterActivity.class);
            startActivityForResult(intent, INTENT_FILTER_CODE);
        } else if (v.getId() == R.id.backIv) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    private void getHistory(boolean showProgress, Integer state) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            String url = Constants.GET_HISTORY_URL;

            url = url.replace("{langId}", langId);

            url = url.replace("{search}", "");

            url = url.replace("{selectedDate}", "");
            if (state != null) {
                url = url.replace("{state}", state + "");
            }
            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");

            if (showProgress) {
                swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, PrizeWinReponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof PrizeWinReponse) {
            PrizeWinReponse prizeWinReponse = (PrizeWinReponse) object;
            if (prizeWinReponse != null && prizeWinReponse.getPrizeWins() != null && prizeWinReponse.getPrizeWins().size() > 0) {
                historyList = new ArrayList<>();
                if (prizeWinReponse.getPrizeWins() != null && prizeWinReponse.getPrizeWins().size() > 0) {
                    List<PrizeWin> userGameSessions = prizeWinReponse.getPrizeWins();
                    historyList.addAll(userGameSessions);
                }

                showSuccessPoints(prizeWinReponse.getTotalRewardsAmt(), prizeWinReponse.getRedemptionAmounts().getSuccessAmt());
                if ((swipeRefreshLayout.isRefreshing()))
                    swipeRefreshLayout.setRefreshing(false);
                setAdapter();
            }
        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (paytmTransferDialog != null && paytmTransferDialog.isShowing()) {
                paytmTransferDialog.dismiss();
            }
            if (commonResponse.getCode() == Constants.SUCCESS) {
                if (prizeDialog != null && prizeDialog.isShowing()) {
                    prizeDialog.dismiss();
                }
                if (paytmTransferDialog != null && paytmTransferDialog.isShowing()) {
                    paytmTransferDialog.dismiss();
                }
                Intent intent = new Intent(this, PaytmTransferSuccessfulActivity.class);
                intent.putExtra(Constants.FROM_SPLASH, false);
                if (requestedType == Constants.VOUCHER_ID) {
                    intent.putExtra(Constants.IS_VOUCHER_TRANSFER, true);
                    intent.putExtra(Constants.IS_RETRY, true);
                }
                startActivity(intent);
            } else {
                MyDialog.showToast(this, getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

        if ((swipeRefreshLayout.isRefreshing()))
            swipeRefreshLayout.setRefreshing(false);
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onRefresh() {

        if (!paginatedRefresh) {
            pageNo = 1;
            totalItemCount = 0;
            getHistory(true, state);
        } else {
            paginatedRefresh = false;
            if (previousCount != 0) {
                pageNo++;
                getHistory(true, state);
            } else {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }
        previousCount = totalItemCount;
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {
        if (type == R.id.redeemButton && object instanceof PrizeWin) {
            PrizeWin prizeWin = (PrizeWin) object;
            String mobile = Utils.getMobileNumber(this);
            if (prizeWin.getType() == Constants.PAYTM_ID) {
                paytmTransferDialog = MyDialog.showPayTmTransferDialog(this, mobile, prizeWin.getAmount(), this, prizeWin);
            } else if (prizeWin.getType() == Constants.VOUCHER_ID) {
                makeRequest(prizeWin);
            }
        } else if (type == R.id.continueBtn && object instanceof PrizeWin) {
            PrizeWin prizeWin = (PrizeWin) object;
            makeRequest(prizeWin);
        } else {
            if (object instanceof PrizeWin) {
                PrizeWin prizeWin = (PrizeWin) object;
                String mobile = Utils.getMobileNumber(this);

                prizeDialog = MyDialog.showPrizeDetailsDialog(this, prizeWin, mobile, this);
            }
        }
    }

    private void makeRequest(PrizeWin prizeWin) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            if (prizeWin.getType() == Constants.PAYTM_ID) {
                EightfoldsVolley.getInstance().showProgress(this);
                url = Constants.RETRY_REDEEM_AMOUNT_TO_PAYTM;
                if (prizeWin.getUserName() != null) {
                    url = url.replace("{mobile}", prizeWin.getUserName());
                } else {
                    url = url.replace("{mobile}", "");
                }
                if (prizeWin.getRefType() != null) {
                    url = url.replace("{refType}", prizeWin.getRefType().toString());
                } else {
                    url = url.replace("{refType}", "");
                }

                if (prizeWin.getRefId() != null) {
                    url = url.replace("{refId}", prizeWin.getRefId().toString());
                } else {
                    url = url.replace("{refId}", "");
                }
                requestedType = Constants.PAYTM_ID;
                EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.POST, url);
            } else if (prizeWin.getType() == Constants.VOUCHER_ID) {
                EightfoldsVolley.getInstance().showProgress(this);
                url = Constants.RETRY_REDEEM_USING_VOUCHER;
                if (prizeWin.getRefType() != null) {
                    url = url.replace("{refType}", prizeWin.getRefType().toString());
                } else {
                    url = url.replace("{refType}", "");
                }

                if (prizeWin.getRefId() != null) {
                    url = url.replace("{refId}", prizeWin.getRefId().toString());
                } else {
                    url = url.replace("{refId}", "");
                }
                requestedType = Constants.VOUCHER_ID;
                EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.POST, url);
            }
        }
    }
}
