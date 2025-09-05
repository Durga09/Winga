package in.eightfolds.winga.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.WinnersListAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Winner;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class SessionWinnersListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, VolleyResultCallBack {

    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    private RecyclerView winnersRecyclerView;
    private ArrayList<Winner> winners;
    private TextView noWinnersTv;
    private LinearLayout mainLL;
    WinnersListAdapter winnersListAdapter;
    private boolean paginatedRefresh = false;
    private Long gSessionId;
    private TextView selectedDateTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session_wise_winners);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("gSessionId")) {
                gSessionId = bundle.getLong("gSessionId");
            }
        }
        initialize();
    }


    private void initialize() {
        setHeader(getString(R.string.winners_list));
        winnersRecyclerView = findViewById(R.id.winnersRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        noWinnersTv = findViewById(R.id.noWinnersTv);
        mainLL = findViewById(R.id.mainLL);
        selectedDateTv = findViewById(R.id.selectedDateTv);

        findViewById(R.id.backIv).setOnClickListener(this);
        getWinners(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        winnersRecyclerView.setNestedScrollingEnabled(false);
        winnersRecyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        winnersRecyclerView.setLayoutManager(layoutManager);
        winnersRecyclerView.setItemViewCacheSize(20);



        winnersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    paginatedRefresh = true;
                    onRefresh();
                }
            }
        });
    }


    private void setNotificationAdapter(final ArrayList<Winner> winners) {

        if (winners.size() > 0) {
            winnersListAdapter = new WinnersListAdapter(SessionWinnersListActivity.this, winners);

            winnersRecyclerView.setAdapter(winnersListAdapter);

        } else {
            noWinnersTv.setVisibility(View.VISIBLE);
            mainLL.setBackground(getResources().getDrawable(R.drawable.nowinners));
        }

    }

    private void getWinners(boolean showProgress) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

            String url = Constants.SESSION_WISE_WINNERS_LIST;

            if(url.contains("{cgsId}")){
                url = url.replace("{cgsId}",gSessionId+"");
            }
            url = url.replace("{langId}", langId);

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
            EightfoldsVolley.getInstance().makingStringRequest(this, Winner[].class, Request.Method.GET, url);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv) {
                super.onBackPressed();

        }
    }



    @Override
    public void onRefresh() {
        //  if ((previousCount != totalItemCount && totalItemCount >= ((pageNo - 1) * Constants.PAGE_SIZE)) || totalItemCount == 0) {

        if (!paginatedRefresh) {
            pageNo = 1;
            totalItemCount = 0;
            getWinners(true);
        } else {
            paginatedRefresh = false;
            if (previousCount != 0) {
                pageNo++;
                getWinners(true);
            } else {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }
        previousCount = totalItemCount;
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (winners == null) {
            winners = new ArrayList<>();
        }

        if (object instanceof Winner[]) {

            Winner[] winnersArray = (Winner[]) object;

            previousCount = winnersArray.length;
            if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);

            if (pageNo == 1) {

                winners = new ArrayList<>();
                winners.addAll(Arrays.asList(winnersArray));
                if (winners.size() > 0) {
                    setNotificationAdapter(winners);

                    String formattedDate = null;
                    try {
                        if(!TextUtils.isEmpty( winners.get(0).getGameDate() )) {
                            formattedDate = DateTime.getDateFromUTC(winners.get(0).getGameDate(), "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");
                            selectedDateTv.setText(!TextUtils.isEmpty(formattedDate) ? formattedDate : "");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else {
                    setNotificationAdapter(new ArrayList<Winner>());
                }
            } else {
                if (winnersListAdapter != null) {
                    List<Winner> latestNotifications = Arrays.asList(winnersArray);
                    winnersListAdapter.refreshData(latestNotifications);
                }
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        if ((swipeRefreshLayout.isRefreshing()))
            swipeRefreshLayout.setRefreshing(false);
        Utils.handleCommonErrors(this, object);
    }




}
