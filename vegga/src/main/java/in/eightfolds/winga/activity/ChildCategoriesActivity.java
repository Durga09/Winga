package in.eightfolds.winga.activity;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.CategoryListAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.CategoryHomePageResponse;
import in.eightfolds.winga.model.CategoryResponse;
import in.eightfolds.winga.model.CategoryTranslation;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class ChildCategoriesActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {

    private ImageView logoIv;
    private RecyclerView categoryREcyclerView;
    private CategoryListAdapter categoryListAdapter;
    private ImageView profilePicIV,home;
    private RelativeLayout profileRL, pointsRL;
    private TextView pointTv;
    private CategoryResponse categoryResponse;
    private Long profilePicId;
    private CategoryTranslation categoryTranslation;
    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_categories);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey(Constants.DATA)) {
                categoryTranslation = (CategoryTranslation) bundle.get(Constants.DATA);

            }
            if(bundle.containsKey(Constants.CATEGORY_TRANSLATION)){
                categoryResponse=(CategoryResponse) bundle.get(Constants.CATEGORY_TRANSLATION);
            }

        }
        initialize(true);

    }

    private void initialize(boolean fromConfigChange) {

        logoIv = findViewById(R.id.logoIv);
        profilePicIV = findViewById(R.id.profilePicIV);
        profileRL = findViewById(R.id.profileRL);
        pointsRL = findViewById(R.id.pointsRL);
        pointTv = findViewById(R.id.pointTv);
        home=findViewById(R.id.home);
        home.setOnClickListener(this);
        categoryREcyclerView=findViewById(R.id.categoryRv);
        categoryREcyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryREcyclerView.setLayoutManager(layoutManager);
        // notificationsRecyclerView.setItemViewCacheSize(20);
        layoutManager.setAutoMeasureEnabled(true);

        profileRL.setOnClickListener(this);
        pointsRL.setOnClickListener(this);
        setProfileImage();
        setAdapter(categoryTranslation.getChildCategories());




    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.profileRL) {
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
            else if( view.getId()==R.id.pointsRL)
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
        else if( view.getId()==R.id.home)

                onBackPressed();




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

    private void setAdapter(List<CategoryTranslation> categories) {
        if (categories.size() != 0) {
            categoryListAdapter = new CategoryListAdapter(this, (ArrayList<CategoryTranslation>) categories,categoryResponse);
            categoryREcyclerView.setAdapter(categoryListAdapter);
        }
    }
}
