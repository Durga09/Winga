package in.eightfolds.winga.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.SpecialSessionsAdapter;
import in.eightfolds.winga.model.GameSession;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class SpecialSessionsListActivity extends BaseActivity {

    ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    RecyclerView loyaltyPointsRecyclerView;
    private Integer state = 0;
    private ImageView backIv;
    private boolean paginatedRefresh;
    private ArrayList<GameSession> upcomingSponsoredGameSessions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special_sessions);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                upcomingSponsoredGameSessions = (ArrayList<GameSession>) bundle.get(Constants.DATA);
            }
        }
        initialize();
    }


    private void initialize() {

        backIv = findViewById(R.id.backIv);
        loyaltyPointsRecyclerView = findViewById(R.id.loyaltyPointsRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        // swipeRefreshLayout.setOnRefreshListener(this);
        backIv.setOnClickListener(this);


        loyaltyPointsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    paginatedRefresh = true;
                    // onRefresh();
                }
            }
        });

        if ((swipeRefreshLayout.isRefreshing()))
            swipeRefreshLayout.setRefreshing(false);

        setAdapter();

    }


    private void setAdapter() {


        if (upcomingSponsoredGameSessions.size() > 0) {
            SpecialSessionsAdapter specialSessionsAdapter = new SpecialSessionsAdapter(this, upcomingSponsoredGameSessions, "");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
            loyaltyPointsRecyclerView.setLayoutManager(layoutManager);


            loyaltyPointsRecyclerView.setAdapter(specialSessionsAdapter);
        } else {

            (findViewById(R.id.topRL)).setBackground(getResources().getDrawable(R.drawable.noplayhistory));
            findViewById(R.id.noItemsLL).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv) {
            finish();
        }
    }


}