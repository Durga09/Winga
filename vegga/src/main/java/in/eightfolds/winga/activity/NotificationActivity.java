package in.eightfolds.winga.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.NotificationsAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 23-Apr-18.
 */

public class NotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, VolleyResultCallBack {

    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;
    private int totalItemCount = 0, previousCount = 0;

    private RecyclerView notificationsRecyclerView;
    private ArrayList<Notification> notifications;
    private LinearLayout noNotificationsLL;
    private LinearLayout mainLL;
    private boolean fromNotification;
    NotificationsAdapter notificationsRecyclerAdapter;
    private boolean paginatedRefresh = false;
    private NotificationReceivedReceiver notificationReceivedReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
            }
        }
        initialize();
    }


    private void initialize() {
        setHeader(getString(R.string.notifications));
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        noNotificationsLL = findViewById(R.id.noNotificationsLL);
        mainLL = findViewById(R.id.mainLL);

        findViewById(R.id.backIv).setOnClickListener(this);
        getNotifications(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        // notificationsRecyclerView.setNestedScrollingEnabled(false);
        notificationsRecyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notificationsRecyclerView.setLayoutManager(layoutManager);
        // notificationsRecyclerView.setItemViewCacheSize(20);
        layoutManager.setAutoMeasureEnabled(true);


        notificationsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationReceivedReceiver == null) {
            notificationReceivedReceiver = new NotificationReceivedReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.NOTIFICATION_RECEIVED_ACTION);
            this.registerReceiver(notificationReceivedReceiver, intentFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(notificationReceivedReceiver != null){
            this.unregisterReceiver(notificationReceivedReceiver);
        }
    }

    private void setNotificationAdapter(final ArrayList<Notification> notifications) {

        if (notifications.size() > 0) {
            new AsyncTask<ArrayList<Notification>, Void, ArrayList<Notification>>() {

                ProgressDialog progressDialog;

                @Override
                protected ArrayList<Notification> doInBackground(ArrayList<Notification>... arrayLists) {

                    if (arrayLists.length > 0) {
                        ArrayList<Notification> filteredNotifications = arrayLists[0];
                        filteredNotifications = Utils.setNotificationsReadStatus(NotificationActivity.this, filteredNotifications);
                        return filteredNotifications;
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    progressDialog = MyDialog.showProgress(NotificationActivity.this);
                }

                @Override
                protected void onPostExecute(ArrayList<Notification> notifications) {
                    super.onPostExecute(notifications);
                    notificationsRecyclerAdapter = new NotificationsAdapter(NotificationActivity.this, notifications);

                    notificationsRecyclerView.setAdapter(notificationsRecyclerAdapter);
                    progressDialog.dismiss();
                    ViewCompat.setNestedScrollingEnabled(notificationsRecyclerView, false);
                }


            }.execute(notifications);
            noNotificationsLL.setVisibility(View.GONE);
            mainLL.setBackgroundColor(getResources().getColor(R.color.winga_bg_color));
        } else {
            noNotificationsLL.setVisibility(View.VISIBLE);
            // mainLL.setBackground(getResources().getDrawable(R.drawable.nonotifications));
        }


    }

    private void getNotifications(boolean showProgress) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = Constants.GET_NOTIFICATIONS;


            try {
                url = url.replace("{tz}", URLEncoder.encode(DateTime.getCurrentTimeZoneNameAndTimeDiff(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.NOTIFICATION_PAGE_SIZE + "");

            if (showProgress) {
                swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, Notification[].class, Request.Method.GET, url);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv) {
            handleBack();
        }
        }


    private void handleBack() {
        if (fromNotification) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    public void onRefresh() {
        //  if ((previousCount != totalItemCount && totalItemCount >= ((pageNo - 1) * Constants.PAGE_SIZE)) || totalItemCount == 0) {

        if (!paginatedRefresh) {
            pageNo = 1;
            totalItemCount = 0;
            getNotifications(true);
        } else {
            paginatedRefresh = false;
            if (previousCount != 0) {
                pageNo++;
                getNotifications(true);
            } else {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }
        previousCount = totalItemCount;
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }

        if (object instanceof Notification[]) {

            Notification[] notificationsArray = (Notification[]) object;

            previousCount = notificationsArray.length;
            if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);

            if (pageNo == 1) {

                notifications = new ArrayList<>();
                notifications.addAll(Arrays.asList(notificationsArray));
                if (notifications.size() > 0) {
                    setNotificationAdapter(notifications);
                } else {
                    setNotificationAdapter(new ArrayList<Notification>());
                }
            } else {
                if (notificationsRecyclerAdapter != null) {
                    List<Notification> latestNotifications = Arrays.asList(notificationsArray);
                    latestNotifications = Utils.setNotificationsReadStatus(this, new ArrayList<Notification>(latestNotifications));
                    notificationsRecyclerAdapter.refreshData(latestNotifications);
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

    public class NotificationReceivedReceiver extends BroadcastReceiver {
        private final String TAG = NotificationReceivedReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Constants.NOTIFICATION_RECEIVED_ACTION)) {
                Logg.v(TAG, "*** Notification received Boardcast received");
                MyDialog.showToast(NotificationActivity.this, getString(R.string.new_notification_received));
                pageNo = 1;
                totalItemCount = 0;
                getNotifications(true);
            }
        }
    }


}
