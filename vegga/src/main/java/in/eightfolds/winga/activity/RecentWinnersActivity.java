package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.RecentWinnersSessionAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.GameSessionWinnerResponse;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.ResultDeclaredNotificationJson;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class RecentWinnersActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack {

    private static final String TAG = RecentWinnersActivity.class.getSimpleName();
    private int pageNo = 1;
    private RecyclerView recentWinnersSessionRecyclerView;
    private TextView selectedDateTv;
    long yesterdayMillis;
    private LinearLayout noItemsLL, mainLL;
    private EditText searchET;
    private Date selectedDate;
    private Calendar selectedCalendarDate;
    private ImageView backIv;
    private TextView titleTv;
    private boolean fromNotification;
    private Notification notification;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Utils.setAppLanguage(this);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_recent_winners);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                    fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
                }
                if (bundle.containsKey(Constants.DATA)) {
                    notification = (Notification) bundle.get(Constants.DATA);
                }
            }
            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialize() {
        selectedDateTv = findViewById(R.id.selectedDateTv);
        backIv = findViewById(R.id.backIv);
        recentWinnersSessionRecyclerView = findViewById(R.id.recentWinnersSessionRecyclerView);
        searchET = findViewById(R.id.searchET);
        mainLL = findViewById(R.id.mainLL);
        noItemsLL = findViewById(R.id.noItemsLL);
        titleTv = findViewById(R.id.titleTv);
        findViewById(R.id.calenderLL).setOnClickListener(this);
        backIv.setOnClickListener(this);
        if (fromNotification && notification.getNotificationId() != null) {
            Utils.makeNotificationAsRead(this, notification.getNotificationId());
        }


        if (notification != null && !TextUtils.isEmpty(notification.getJsonData())) {
            try {
                ResultDeclaredNotificationJson resultDeclaredNotificationJson = (ResultDeclaredNotificationJson) Api.fromJson(notification.getJsonData(), ResultDeclaredNotificationJson.class);
                if (!TextUtils.isEmpty(resultDeclaredNotificationJson.getDate())) {
                    Date date = DateTime.getDateFromUTC(resultDeclaredNotificationJson.getDate(), "yyyy-MM-dd HH:mm:ss");
                    getWinnersForDate(date, "");
                } else {
                    getWinnersForDate(null, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                getWinnersForDate(null, "");
            }

        } else {
            getWinnersForDate(null, "");
        }
    }


    private void getWinnersForDate(Date date, String searchItem) {

        String serverDate = "";
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            String day = dateFormat.format(date);

            selectedDateTv.setText(day);

            SimpleDateFormat serverDateFromat = new SimpleDateFormat("yyyy-MM-dd"); //2018-05-23
            serverDate = serverDateFromat.format(date);
        }


        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            String url = Constants.GET_WINNERS_LIST_URL;
            String id = EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(), Constants.SELECTED_CATEGORY);
            url = url.replace("{langId}", langId);
            url = url.replace("{search}", searchItem);
            url = url.replace("{selectedDate}", serverDate);
            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");
            url = url.replace("{categoryId}", id);

            EightfoldsVolley.getInstance().showProgress(this);
//            swipeRefreshLayout.setRefreshing(showProgress);
            EightfoldsVolley.getInstance().makingStringRequest(this, GameSessionWinnerResponse[].class, Request.Method.GET, url);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.calenderLL) {

            long millisNow;
            Calendar cal = Calendar.getInstance();
            Date yesterdayDate = new Date(cal.getTime().getTime()); // 7days, 172800000L 2 days

            millisNow = yesterdayDate.getTime();
            MyDialog.showDatePicker(this, millisNow, selectedCalendarDate, this);


        } else if (v.getId() == R.id.searchIv) {
            searchET.setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.searchIv)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.titleTv)).setVisibility(View.GONE);


        } else if (v.getId() == R.id.backIv) {
            handleBackPress();
        }
    }

    private void handleBackPress() {

        if (fromNotification) {
            Intent intent = new Intent(this, HomeBaseActivity.class);
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
        handleBackPress();
    }


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {
        if (object instanceof Calendar) {
            selectedCalendarDate = (Calendar) object;
            selectedDate = new Date(selectedCalendarDate.getTime().getTime());

            getWinnersForDate(selectedDate, "");

            Calendar cal = Calendar.getInstance();
            Date yesterdayDate = new Date(cal.getTime().getTime() - 86400000); // 7days, 172800000L 2 days

            yesterdayMillis = yesterdayDate.getTime();
            if (selectedDate.getTime() == yesterdayDate.getTime() || selectedDate.getTime() == (new Date()).getTime()) {
                titleTv.setText(getString(R.string.yesterday_winner));
                findViewById(R.id.selectedDateDescTv).setVisibility(View.VISIBLE);
            } else {
                // titleTv.setText(getString(R.string.winners));
                titleTv.setVisibility(View.GONE);
                findViewById(R.id.selectedDateDescTv).setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof GameSessionWinnerResponse[]) {
            GameSessionWinnerResponse[] winners = (GameSessionWinnerResponse[]) object;
            ArrayList<GameSessionWinnerResponse> winnerArrayList = new ArrayList<>();
            winnerArrayList.addAll(Arrays.asList(winners));

            if (winnerArrayList.size() <= 0 || (winnerArrayList.size() == 1 && winnerArrayList.get(0).getWinners() == null)) {

                setAdapter(new ArrayList<GameSessionWinnerResponse>());
                mainLL.setBackgroundColor(getResources().getColor(R.color.winga_bg_color));
                noItemsLL.setVisibility(View.VISIBLE);

                if (selectedDate == null) {
                    titleTv.setText(getString(R.string.yesterday_winner));
                    findViewById(R.id.selectedDateDescTv).setVisibility(View.GONE);

                }

            } else {
                if (selectedDate == null && !TextUtils.isEmpty(winnerArrayList.get(0).getWinerPubTime())) {
                    String pubTime = winnerArrayList.get(0).getWinerPubTime();
                    try {
                        yesterdayMillis = DateTime.getInMilliesFromUTC(pubTime);
                        selectedDate = new Date(yesterdayMillis);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
                        String day = dateFormat.format(selectedDate);
                        selectedDateTv.setText(day);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                setAdapter(winnerArrayList);
                mainLL.setBackground(getResources().getDrawable(R.drawable.winnersbg));
                noItemsLL.setVisibility(View.GONE);
            }
        }
    }

    private void setAdapter(ArrayList<GameSessionWinnerResponse> winnerArrayList) {
        // recentWinnersSessionRecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recentWinnersSessionRecyclerView.setLayoutManager(layoutManager);
        RecentWinnersSessionAdapter recentWinnersSessionAdapter = new RecentWinnersSessionAdapter(this, winnerArrayList);
        recentWinnersSessionRecyclerView.setAdapter(recentWinnersSessionAdapter);
    }


    @Override
    public void onVolleyErrorListener(Object object) {

        setAdapter(new ArrayList<GameSessionWinnerResponse>());
        mainLL.setBackgroundColor(getResources().getColor(R.color.winga_bg_color));
        noItemsLL.setVisibility(View.VISIBLE);

        if (selectedDate == null) {
            titleTv.setText(getString(R.string.yesterday_winner));
            findViewById(R.id.selectedDateDescTv).setVisibility(View.GONE);

        }
        Utils.handleCommonErrors(this, object);
    }
}



