package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.LoyaltyPointsRecyclerAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.FilterItemModel;
import in.eightfolds.winga.model.LoyalityPointHistory;
import in.eightfolds.winga.model.LoyalityPointHistoryResponse;
import in.eightfolds.winga.model.RedemptionOption;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class LoyaltyPointsHistoryActivity extends BaseActivity implements VolleyResultCallBack, SwipeRefreshLayout.OnRefreshListener, OnEventListener {

    public static final String VIEW_NAME_POINTS = "detail:points";
    ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;
    RecyclerView loyaltyPointsRecyclerView;
    private ArrayList<LoyalityPointHistory> historyList;
    private Integer state = 0;
    private ImageView backIv;
    private TextView loyalityPointsDescTv, availablePointsTv;
    private boolean paginatedRefresh = false;
    Double redeemedLoyaltyPoints = 0.0, availablePoints;
    private int minimumRedeemablePoints = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loyalty_points_history);
        initialize();
    }


    private void initialize() {
        backIv = findViewById(R.id.backIv);
        loyaltyPointsRecyclerView = findViewById(R.id.loyaltyPointsRecyclerView);
        loyalityPointsDescTv = findViewById(R.id.loyalityPointsDescTv);
        availablePointsTv = findViewById(R.id.availablePointsTv);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        findViewById(R.id.transferMoneyIv).setOnClickListener(this);
        backIv.setOnClickListener(this);
        //  ViewCompat.setTransitionName(findViewById(R.id.historyTv),VIEW_NAME_POINTS);
        String language = Utils.getCurrentLanguage(this);
       /* if(!language.equalsIgnoreCase("en")){
            loyalityPointsDescTv.setTextSize(22);
        }*/
        loyaltyPointsRecyclerView.setNestedScrollingEnabled(false);

        getHistory(false, state);
        loyaltyPointsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    paginatedRefresh = true;
                    onRefresh();
                }
            }
        });

        showLoyalityPoints();
    }

    private void showLoyalityPoints() {
        User userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        showLoyalityPoints(userResponse.getTotalLoyalityPoints(), userResponse.getRedeemedLoyalityPoints());

        if (userResponse.getMinimumRedemptionPoint() != null) {
            minimumRedeemablePoints = userResponse.getMinimumRedemptionPoint().intValue();
        }

       /* if (userResponse.getTotalLoyalityPoints() != null) {
            loyalityPointsDescTv.setText(Integer.toString(userResponse.getTotalLoyalityPoints().intValue()));
        }
        Double redeemedLoyaltyPoints = 0.0, availablePoints;
        if (userResponse.getRedeemedLoyalityPoints() != null) {
            redeemedLoyaltyPoints = userResponse.getRedeemedLoyalityPoints();
        }

        availablePoints = userResponse.getTotalLoyalityPoints() - redeemedLoyaltyPoints;
        availablePointsTv.setText(Integer.toString(availablePoints.intValue()));*/

    }

    private void showLoyalityPoints(Double totalPoints, Double redeemedPoints) {

        if (totalPoints != null) {

            loyalityPointsDescTv.setText(Integer.toString(totalPoints.intValue()));


            if (redeemedPoints != null) {
                redeemedLoyaltyPoints = redeemedPoints;
            }

            availablePoints = totalPoints - redeemedLoyaltyPoints;
            availablePointsTv.setText(Integer.toString(availablePoints.intValue()));
        }

    }

    private void setAdapter() {

        if (historyList.size() > 0) {
            LoyaltyPointsRecyclerAdapter loyaltyPointsRecyclerAdapter = new LoyaltyPointsRecyclerAdapter(this, historyList, "");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
            loyaltyPointsRecyclerView.setLayoutManager(layoutManager);


            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            loyaltyPointsRecyclerView.setLayoutAnimation(animation);

            loyaltyPointsRecyclerView.setAdapter(loyaltyPointsRecyclerAdapter);

            //  (findViewById(R.id.topRL)).setBackgroundColor(getResources().getColor(R.color.header_text_color));
            findViewById(R.id.noItemsLL).setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {

            //  (findViewById(R.id.topRL)).setBackground(getResources().getDrawable(R.drawable.noloyalitypoints));
            findViewById(R.id.noItemsLL).setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
        ViewCompat.setNestedScrollingEnabled(loyaltyPointsRecyclerView, false);

    }

    private int INTENT_FILTER_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null && data.getExtras().containsKey(Constants.DATA)) {
                    FilterItemModel selectedFilterItem = (FilterItemModel) data.getSerializableExtra(Constants.DATA);
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
            finish();
        } else if (v.getId() == R.id.transferMoneyIv) {
            // redeemPoints();
            //redeemUsingPaytm();
            showRedemptionOptions(Utils.getRedemptionOptionsFromSetUp(this));
        }
    }


    private void showRedemptionOptions(ArrayList<RedemptionOption> redemptionOptionArrayList) {
        if (redemptionOptionArrayList != null && redemptionOptionArrayList.size() > 0) {
            MyDialog.showRedeemPointsPopUp(this, this, redemptionOptionArrayList);
        } else {
            MyDialog.showToast(this, getString(R.string.no_redemption_option_available));
        }
    }

    /*private void getRedemptionOptions() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_REDEMPTION_OPTIONS;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, RedemptionOption[].class, Request.Method.GET, url);
        }
    }*/

    private void redeemUsingPaytm() {
        Intent intent = new Intent(this, RedeemPointsUsingPaytmActivity.class);
        startActivity(intent);
    }

    private void redeemVouchers() {
        Intent intent = new Intent(this, VouchersActivity.class);
        intent.putExtra(Constants.DATA, availablePoints);
        intent.putExtra(Constants.OTHER_DATA, minimumRedeemablePoints);

        startActivity(intent);
    }


    private void getHistory(boolean showProgress, Integer state) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = Constants.GET_LOYALTYPOINTS_HISTORY;


            try {
                url = url.replace("{tz}", URLEncoder.encode(DateTime.getCurrentTimeZoneNameAndTimeDiff(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");

            if (showProgress) {
                swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, LoyalityPointHistoryResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof LoyalityPointHistoryResponse) {
            LoyalityPointHistoryResponse loyalityPointHistoryResponse = (LoyalityPointHistoryResponse) object;
            historyList = new ArrayList<>();
            if (loyalityPointHistoryResponse.getLoyalityPointHistories() != null && loyalityPointHistoryResponse.getLoyalityPointHistories().size() > 0) {
                List<LoyalityPointHistory> loyalityPointHistories = loyalityPointHistoryResponse.getLoyalityPointHistories();
                historyList.addAll(loyalityPointHistories);
            }

            showLoyalityPoints(loyalityPointHistoryResponse.getTotalLoyalityPoints(), loyalityPointHistoryResponse.getRedeemedLoyalityPoints());
            if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);

            setAdapter();
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

        if (object instanceof RedemptionOption) {
            RedemptionOption redemptionOption = (RedemptionOption) object;
            if (redemptionOption.getTitle().equalsIgnoreCase(Constants.OPTION_PAYTM_TITLE)) {
                redeemUsingPaytm();
            } else if (redemptionOption.getTitle().equalsIgnoreCase(Constants.OPTION_VOUCHERS_TITLE)) {
                redeemVouchers();
            } else {
                MyDialog.showToast(this, String.format(getString(R.string.not_available_now), redemptionOption.getSubTitle()));
            }
        }
    }
}
