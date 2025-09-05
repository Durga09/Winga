package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HistoryFilterActivity;
import in.eightfolds.winga.adapter.InvitesRecyclerAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserReferralItem;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class InvitesFragment extends Fragment implements VolleyResultCallBack, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    public static final String VIEW_NAME_POINTS = "detail:points";
    ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    RecyclerView invitesRecyclerView;
    private ArrayList<UserReferralItem> historyList;
    private Integer state = 0;
    private RelativeLayout topLL;
    private LinearLayout  noItemsLL;
    private boolean paginatedRefresh = false;


    private Activity myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invites, container, false);

        initialize(rootView);

        return rootView;
    }


    private void initialize(View rootView) {

        invitesRecyclerView =rootView. findViewById(R.id.invitesRecyclerView);
        noItemsLL = rootView.findViewById(R.id.noItemsLL);
        topLL = rootView.findViewById(R.id.topLL);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        getHistory(false, state);
        invitesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    private void setAdapter() {

        if (historyList!= null && historyList.size() > 0) {
            InvitesRecyclerAdapter invitesRecyclerAdapter = new InvitesRecyclerAdapter(myContext, historyList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(myContext, GridLayoutManager.VERTICAL, false);
            invitesRecyclerView.setLayoutManager(layoutManager);
            invitesRecyclerView.setAdapter(invitesRecyclerAdapter);
        } else {
            noItemsLL.setVisibility(View.VISIBLE);
        }
    }

    private int INTENT_FILTER_CODE = 100;



    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.headerRightIconIv) {
            Intent intent = new Intent(myContext, HistoryFilterActivity.class);
            startActivityForResult(intent, INTENT_FILTER_CODE);
        } else if (v.getId() == R.id.backIv) {
            myContext.finish();
        }
    }

    private void getHistory(boolean showProgress, Integer state) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            String url = Constants.GET_REFERRED_HISTORY;


            try {
                url = url.replace("{tz}", URLEncoder.encode(DateTime.getCurrentTimeZoneNameAndTimeDiff(), "utf-8") );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");

            if (showProgress) {
                swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(myContext);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, UserReferralItem[].class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof UserReferralItem[]) {
            historyList = new ArrayList<>();
            UserReferralItem[] userReferralItems = (UserReferralItem[]) object;
            historyList.addAll(Arrays.asList(userReferralItems));
            if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);
        }

        setAdapter();
    }

    @Override
    public void onVolleyErrorListener(Object object) {

        if ((swipeRefreshLayout.isRefreshing()))
            swipeRefreshLayout.setRefreshing(false);
        setAdapter();
        Utils.handleCommonErrors(myContext, object);
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
