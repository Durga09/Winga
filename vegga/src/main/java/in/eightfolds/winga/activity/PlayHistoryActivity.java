package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.PlayHistoryRecyclerAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.FilterItemModel;
import in.eightfolds.winga.model.GameHistoryItem;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class PlayHistoryActivity extends BaseActivity implements VolleyResultCallBack, SwipeRefreshLayout.OnRefreshListener {

    ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    RecyclerView loyaltyPointsRecyclerView;
    private ArrayList<GameHistoryItem> historyList;
    private Integer state = 0;
    private ImageView backIv;
    private boolean paginatedRefresh;
    private boolean showWinOnly;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_history);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null ){
            if(bundle.containsKey("showWinOnly")){
                showWinOnly = bundle.getBoolean("showWinOnly");
            }
        }
        initialize();
    }


    private void initialize() {

        backIv = findViewById(R.id.backIv);
        loyaltyPointsRecyclerView = findViewById(R.id.loyaltyPointsRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        backIv.setOnClickListener(this);


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

        getHistory(false, state);
    }


    private void setAdapter() {


        if (historyList.size() > 0) {
            PlayHistoryRecyclerAdapter playHistoryRecyclerAdapter = new PlayHistoryRecyclerAdapter(this, historyList, "");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
            loyaltyPointsRecyclerView.setLayoutManager(layoutManager);


            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            loyaltyPointsRecyclerView.setLayoutAnimation(animation);

//        vouchersRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL));

            loyaltyPointsRecyclerView.setAdapter(playHistoryRecyclerAdapter);
            findViewById(R.id.noItemsLL).setVisibility(View.GONE);
            //(findViewById(R.id.backgroundIv)).setBackground(getResources().getDrawable(R.color.header_text_color));
            //(findViewById(R.id.backgroundIv)).setVisibility(View.GONE);
        } else {

            //(findViewById(R.id.backgroundIv)).setVisibility(View.VISIBLE);
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
        }
    }

    private void getHistory(boolean showProgress, Integer state) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = Constants.GET_GAMEPLAY_HISTORY;


            try {
                url = url.replace("{tz}", URLEncoder.encode(DateTime.getCurrentTimeZoneNameAndTimeDiff(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");

            if(url.contains("{winFlag}")){
                if(showWinOnly) {
                    url = url.replace("{winFlag}","1" );
                }else{
                    url = url.replace("{winFlag}","0" );
                }
            }

            if (showProgress) {
                swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, GameHistoryItem[].class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof GameHistoryItem[]) {
            historyList = new ArrayList<>();
            GameHistoryItem[] userGameSessions = (GameHistoryItem[]) object;
            historyList.addAll(Arrays.asList(userGameSessions));
            if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);
        }

        setAdapter();



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
}