package in.eightfolds.winga.activity;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.CategoryListAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.CategoryResponse;
import in.eightfolds.winga.model.CategoryTranslation;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.constant.WingaConstants;

public class HomeBaseActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {


    private LinearLayout gameSessionLL;
    private ImageView logoIv, backIv;
    private RecyclerView categoryREcyclerView;
    private CategoryListAdapter categoryListAdapter;
    private ImageView profilePicIV;
    private RelativeLayout profileRL, pointsRL;
    private TextView pointTv, titleTv;
    private String title, information;
    private CategoryResponse categoryResponse;
    private Long profilePicId;
    public TextView gamesTv;
    public LinearLayout categorLL;


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finishAffinity();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, V2HomeFeatureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof CategoryResponse) {
            categoryResponse = (CategoryResponse) object;
            setProfileImage();
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, categoryResponse.getUserDetail());
            if (categoryResponse.getExclusiveCategory() != null) {
                categorLL.setVisibility(View.VISIBLE);
                gamesTv.setText(categoryResponse.getExclusiveCategory().getName());
                categorLL.setOnClickListener(this);
            }
            setAdapter(((CategoryResponse) object).getCategories());
        } else if (object instanceof HomePageResponse) {
            callHomeWithAnim((HomePageResponse) object);
        }
    }

    private void setAdapter(List<CategoryTranslation> categories) {
        if (categories.size() != 0) {
            categoryListAdapter = new CategoryListAdapter(this, (ArrayList<CategoryTranslation>) categories, categoryResponse);
            categoryREcyclerView.setAdapter(categoryListAdapter);
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onResume() {
        try {
            unBlockTouchEvents();
            callCategogryResponse();
            super.onResume();

            setProfileImage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void callHomeWithAnim(HomePageResponse homePageResponse) {
        Utils.subscribeToAlertsPromosUpdates(homePageResponse.getUserDetail());
        Utils.refreshAccessToken(this);
        Intent intent = new Intent(HomeBaseActivity.this, HomeActivity.class);
        intent.putExtra(Constants.HOME_DATA, homePageResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                HomeBaseActivity.this,
                new Pair<View, String>(logoIv
                        ,
                        HomeActivity.VIEW_NAME_LOGO_IMAGE));
        ActivityCompat.startActivity(HomeBaseActivity.this, intent, activityOptions.toBundle());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() ==  R.id.profileRL) {
            try {
                blockUserEvents();
                Intent intent = new Intent(this, ProfileActivity.class);
                if (profilePicId != null && profilePicId.intValue() != 0) {
                    intent.putExtra("profilepic", profilePicId);
                }
                ActivityOptionsCompat activityOptions = makeSceneTransitionAnimation(
                        this,
                        new Pair<View, String>(profilePicIV
                                ,
                                ProfileActivity.VIEW_NAME_HEADER_IMAGE));
                ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
                setOnStop();
            } catch (Exception ex) {
                ex.printStackTrace();
                Intent intent = new Intent(this, ProfileActivity.class);
                if (profilePicId != null && profilePicId.intValue() != 0) {
                    intent.putExtra("profilepic", profilePicId);
                }
                startActivity(intent);
            }
        }
            else if(view.getId() == R.id.pointsRL) {
            try {
                blockUserEvents();
                Intent todayIntent = new Intent(this, LoyaltyPointsHistoryActivity.class);
                if (categoryResponse != null && categoryResponse.getUserDetail() != null && categoryResponse.getUserDetail().getTotalLoyalityPoints() != null) {
                    todayIntent.putExtra("points", categoryResponse.getUserDetail().getTotalLoyalityPoints());
                }
                ActivityOptionsCompat historyactivityOptions = makeSceneTransitionAnimation(
                        this);
                ActivityCompat.startActivity(this, todayIntent, historyactivityOptions.toBundle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
          else if(view.getId() == R.id.games) {
            Intent exclusiveGameIntent = new Intent(this, AnouncementActivity.class);
            exclusiveGameIntent.putExtra(Constants.DATA, categoryResponse.getExclusiveCategory());
            startActivity(exclusiveGameIntent);

        }
          else if(view.getId() == R.id.backIv) {
            Intent intent = new Intent(this, V2HomeFeatureActivity.class);
            this.startActivity(intent);
            finish();
        }

    }

    //    private void getHomePageResponse(boolean showProgress) {
//
//        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
//            String url = Constants.GET_HOME_PAGE_RESPONSE;
//            if (showProgress) {
//                EightfoldsVolley.getInstance().showProgress(this);
//            }
//            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
//            url = url.replace("{langId}", selectedLangId);
//            EightfoldsVolley.getInstance().makingStringRequest(this, HomePageResponse.class, Request.Method.GET, url);
//        }
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_home);

//        Bundle bundle=getIntent().getExtras();
//        if(bundle!=null){
//            if(bundle.containsKey("title")){
//                title= (String) bundle.get("title");
//            }
//            if(bundle.containsKey("information")){
//                information=(String) bundle.get("information");
//            }
//        }

        initialize(true);

    }

    private void callCategogryResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_CATEGORIES_LIST;

            EightfoldsVolley.getInstance().showProgress(this);

            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", selectedLangId);
            EightfoldsVolley.getInstance().makingStringRequest(this, CategoryResponse.class, Request.Method.GET, url);
        }
    }

    private void initialize(boolean fromConfigChange) {
        information = EightfoldsUtils.getInstance().getFromSharedPreference(this, WingaConstants.GET_INFO);
        title = EightfoldsUtils.getInstance().getFromSharedPreference(this, WingaConstants.SAVE_TITLE);
        logoIv = findViewById(R.id.logoIv);
        profilePicIV = findViewById(R.id.profilePicIV);
        profileRL = findViewById(R.id.profileRL);
        pointsRL = findViewById(R.id.pointsRL);
        pointTv = findViewById(R.id.pointTv);
        categoryREcyclerView = findViewById(R.id.categoryRv);
        titleTv = findViewById(R.id.title);
        categoryREcyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryREcyclerView.setLayoutManager(layoutManager);
        // notificationsRecyclerView.setItemViewCacheSize(20);
        layoutManager.setAutoMeasureEnabled(true);
        gamesTv = findViewById(R.id.gameTitle);
        categorLL = findViewById(R.id.games);

        profileRL.setOnClickListener(this);
        pointsRL.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        findViewById(R.id.failureReasonsIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.showInformationPopUp(HomeBaseActivity.this, information, title);

            }
        });

        callCategogryResponse();


    }

    private void blockUserEvents() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unBlockTouchEvents() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void setProfileImage() {
        try {
            if (categoryResponse != null && categoryResponse.getUserDetail() != null) {
                if (categoryResponse.getUserDetail().getProfilePicId() != null && categoryResponse.getUserDetail().getProfilePicId().intValue() != 0) {
                    profilePicId = categoryResponse.getUserDetail().getProfilePicId();
                    Glide.with(getApplicationContext())
                            .load(EightfoldsUtils.getInstance().getImageFullPath(categoryResponse.getUserDetail().getProfilePicId(), Constants.FILE_URL))
                            .placeholder(R.drawable.ic_user_filled)
                            .error(R.drawable.ic_user_filled)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(profilePicIV);
                } else {
                    profilePicIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_filled));
                }
                updateLevel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateLevel() {
        if (categoryResponse.getUserDetail().getCurrentLevel() != null && categoryResponse.getUserDetail().getCurrentLevel() != 0) {
            Utils.setProfileBGBasedOnLevel(this, categoryResponse.getUserDetail().getCurrentLevel().intValue(), profileRL);
        }
        if (categoryResponse.getUserDetail() != null && categoryResponse.getUserDetail().getTotalLoyalityPoints() != null) {
            adjustPointsFontSizeAndBg();
        }
    }

    private void adjustPointsFontSizeAndBg() {

        String points = Integer.toString(categoryResponse.getUserDetail().getTotalLoyalityPoints().intValue());
        Utils.adjustPointsFontSizeAndBg(this, points, pointsRL, pointTv);
    }
}

