package in.eightfolds.winga.activity;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import static in.eightfolds.winga.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.CategoryListAdapter;
import in.eightfolds.winga.adapter.HtmlGamesAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.CategoryContentGame;
import in.eightfolds.winga.model.CategoryResponse;

import in.eightfolds.winga.model.CategoryTranslation;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class HtmlGameSessionActicity extends BaseActivity  implements View.OnClickListener, VolleyResultCallBack, OnEventListener {
    private static final String TAG = HtmlGameSessionActicity.class.getSimpleName();
    private RecyclerView recyclerView;
    private HtmlGamesAdapter htmlGamesAdapter;
    private LoginData loginData;
    private ArrayList<CategoryContentGame> categoryContentGames;
    private CategoryResponse categoryResponse;
    private ImageView profilePicIV,home;
    private RelativeLayout profileRL, pointsRL;
    private TextView pointTv;
    private Long profilePicId;

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
        if (object instanceof CategoryContentGame[]) {
            categoryContentGames = new ArrayList<>();
            CategoryContentGame[] userGameSessions = (CategoryContentGame[]) object;
            categoryContentGames.addAll(Arrays.asList(userGameSessions));
            if(userGameSessions.length>0){
                setAdapter();
            }

        }

    }

    private void setAdapter() {
      htmlGamesAdapter   = new HtmlGamesAdapter(categoryContentGames,this,loginData.getUserId());
        recyclerView.setAdapter(htmlGamesAdapter);
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.v(TAG, "**onCreate()");
            Utils.setAppLanguage(this);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(layout.activity_html_game_session);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {


                if(bundle.containsKey(Constants.DATA)){
                    categoryResponse=(CategoryResponse) bundle.get(Constants.DATA);
                }

            }
            initialize();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialize() {

        recyclerView=findViewById(id.htmlGameRv);
        profilePicIV = findViewById(id.profilePicIV);
        profileRL = findViewById(id.profileRL);
        pointsRL = findViewById(id.pointsRL);
        pointTv = findViewById(id.pointTv);
        home=findViewById(id.home);
        home.setOnClickListener(this);
        recyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
         loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.LOGIN_DATA, LoginData.class);
        getHtmlGamesList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        profileRL.setOnClickListener(this);
        pointsRL.setOnClickListener(this);
        setProfileImage();


    }

    private void getHtmlGamesList() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.HTML_CONTENT_GAMES_LINK_URL;

            EightfoldsVolley.getInstance().showProgress(this);

            String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", selectedLangId);
            String categoryId = EightfoldsUtils.getInstance().getFromSharedPreference(this,Constants.SELECTED_CATEGORY);
            url=url.replace("{categoryId}",categoryId);
           // url+="/"+ loginData.getUserId();
            EightfoldsVolley.getInstance().makingStringRequest(this, CategoryContentGame[].class, Request.Method.GET, url);
        }
    }


    public static final int BUTTON1_ID = R.id.profileRL;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == id.profileRL) {
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
        } else if (view.getId() == id.pointsRL)
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

        else if(view.getId()== id.home)

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
                            .placeholder(drawable.ic_user_filled)
                            .error(drawable.ic_user_filled)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(profilePicIV);
                } else {
                    profilePicIV.setImageDrawable(getResources().getDrawable(drawable.ic_user_filled));
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
