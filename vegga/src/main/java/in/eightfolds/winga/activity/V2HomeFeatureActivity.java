package in.eightfolds.winga.activity;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.adapter.BannersAdapter;
import in.eightfolds.winga.v2.adapter.FeaturesAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;
import in.eightfolds.winga.v2.model.BannerFeatureResponse;
import in.eightfolds.winga.v2.model.BannerResponse;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.Feature;
import in.eightfolds.winga.v2.model.GuessItemResponse;
import in.eightfolds.winga.v2.model.SpinResponse;

public class V2HomeFeatureActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {


    private ImageView profilePicIV;
    private RelativeLayout profileRL, pointsRL;
    private TextView pointTv;

    private Long profilePicId;
    private BannersAdapter bannersAdapter;

    private SliderView sliderView;
    private RecyclerView featureGridView;
    private FeaturesAdapter featuresAdapter;
    private ArrayList<Banner> banners;
    BannerResponse bannerResponse;

    private User user;

    private int selectedImagePosition = 0;

    private Dialog wonDialog;

    private Feature selectedFeature;


    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof BannerFeatureResponse) {
            banners = ((BannerFeatureResponse) object).banners;

            setAdapter(banners);
            user = ((BannerFeatureResponse) object).user;
            user.setUserId(user.getId());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, user);

            setProfileImage();


            setFeatureAdapter(((BannerFeatureResponse) object).features);
        }
        if (object instanceof String) {

            banners.get(selectedImagePosition).setImageSHowUrl((String) object);
            selectedImagePosition++;
            if (selectedImagePosition != banners.size()) {

                for (int i = selectedImagePosition; i < banners.size(); i++) {
                    if (banners.get(i).getImageId() != null && banners.get(i).getImageId() != 0) {
                        getImageUrl(banners.get(selectedImagePosition));
                        break;
                    } else {
                        selectedImagePosition = i;
                    }
                }
            } else {
                setAdapter(banners);
            }


        }
        if (object instanceof BannerResponse) {
            bannerResponse = (BannerResponse) object;
            if (!bannerResponse.isShowPopUp()) {
                Intent intent = new Intent(this, V2BannerDetailsActivity.class);
                intent.putExtra(Constants.DATA, bannerResponse);
                this.startActivity(intent);
            } else {
                wonDialog = MyDialog.showBannerWonDialog(V2HomeFeatureActivity.this, bannerResponse, this);

            }
        }
        if (object instanceof CommonServerResponse && !requestType.contains(WingaConstants.GET_REDIRECT_URL)) {
            CommonServerResponse commonServerResponse = (CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());

        }

        if (object instanceof SpinResponse) {
            Intent intent = new Intent(this, V2SpinAndWinActivity.class);
            intent.putExtra("title", selectedFeature.getTitle());
            intent.putExtra("information", selectedFeature.getInformation());
            startActivity(intent);

        }

        if(object instanceof GuessItemResponse){
            Intent intent = new Intent(this,V2GuessTheBrand.class);
            intent.putExtra("title", selectedFeature.getTitle());
            intent.putExtra("information", selectedFeature.getInformation());
            startActivity(intent);

        }
    }


    @Override
    public void onBackPressed() {


        MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);


    }

    @Override
    protected void onResume() {
        try {
            unBlockTouchEvents();

            super.onResume();
            getHomeFeatures();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getImageUrl(Banner banner) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_FILE_URL + banner.getImageId();


            //String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

            EightfoldsVolley.getInstance().makingStringRequestForImage(this, String.class, Request.Method.GET, url);

        } else {
            goToNoInternetActivity();
        }

    }

    private void setFeatureAdapter(ArrayList<Feature> features) {
        if (features.size() > 0) {
            featuresAdapter = new FeaturesAdapter(V2HomeFeatureActivity.this, features, this);
            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            featureGridView.setLayoutManager(layoutManager);
            featureGridView.setAdapter(featuresAdapter);

        }

    }

    private void setAdapter(ArrayList<Banner> banners) {
        if (banners.size() > 0) {
            bannersAdapter = new BannersAdapter(banners, getApplicationContext(), this, 1);
            sliderView.setSliderAdapter(bannersAdapter);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);


        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        if (id == R.id.profileRL) {
            try {
                blockUserEvents();
                Intent intent = new Intent(this, ProfileActivity.class);
                if (profilePicId != null && profilePicId.intValue() != 0) {
                    intent.putExtra("profilepic", profilePicId);
                }
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        new Pair<View, String>(profilePicIV, ProfileActivity.VIEW_NAME_HEADER_IMAGE)
                );
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
        } else if (id == R.id.pointsRL) {
            try {
                blockUserEvents();
                Intent todayIntent = new Intent(this, LoyaltyPointsHistoryActivity.class);
                if (user != null && user.getTotalLoyalityPoints() != null) {
                    todayIntent.putExtra("points", user.getTotalLoyalityPoints());
                }
                ActivityOptionsCompat historyactivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                ActivityCompat.startActivity(this, todayIntent, historyactivityOptions.toBundle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_featured_home);

        initialize(true);

    }

    private void initialize(boolean fromConfigChange) {

        profilePicIV = findViewById(R.id.profilePicIV);
        profileRL = findViewById(R.id.profileRL);
        pointsRL = findViewById(R.id.pointsRL);
        pointTv = findViewById(R.id.pointTv);

        sliderView = findViewById(R.id.imageSlider);
        featureGridView = findViewById(R.id.featureGridView);

        profileRL.setOnClickListener(this);
        pointsRL.setOnClickListener(this);

        getHomeFeatures();


    }

    private void getHomeFeatures() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_HOME_URL;


            //String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, BannerFeatureResponse.class, Request.Method.GET, url);
        } else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }


    private void getBannerDetails(Banner banner) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_BANNNER_RESPONSE + banner.getNumber();


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, BannerResponse.class, Request.Method.GET, url);
        } else {
            goToNoInternetActivity();
        }
    }


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        int typeId = type;

        if (typeId == R.id.yesTv) {
            finish();

            // Note: In the original switch, there's no `break` after `finish()`,
            // so the code for `exitTv` will also execute. To preserve that behavior:
            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
            }

        } else if (typeId == R.id.exitTv) {
            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
            }

        } else if (typeId == R.id.continueBtn) {
            if (bannerResponse.getPoints() != null) {
                // claimPoints(bannerResponse);
            }

            if (!TextUtils.isEmpty(bannerResponse.getRedirectUrl())) {
                callRedirectUrl();
            }

            wonDialog.dismiss();
        }

    }

    private void callRedirectUrl() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            if (!TextUtils.isEmpty(bannerResponse.getRedirectUrl())) {
                sendRedirectUrl(bannerResponse);
                Utils.openInBrowser(this, bannerResponse.getRedirectUrl());
//                Intent intent = new Intent(this, WebBrowserActivity.class);
//                intent.putExtra(Constants.DATA, bannerResponse.getRedirectUrl());
//                intent.putExtra(Constants.TITLE, bannerResponse.getTitle());
//                intent.putExtra("fromWhichPage", 3);
//                startActivity(intent);
            }
        } else {
            goToNoInternetActivity();
        }
    }

    private void sendRedirectUrl(BannerResponse bannerResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_REDIRECT_URL + bannerResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }


    @Override
    public void onEventListener(int type, Object object) {

        if (object instanceof Banner) {
            Banner banner = (Banner) object;
            getBannerDetails(banner);
        }
        if (object instanceof Feature) {
            if (((Feature) object).getId() == 1) {
                Intent intent = new Intent(this, HomeBaseActivity.class);
                intent.putExtra("title", ((Feature) object).getTitle());
                intent.putExtra("information", ((Feature) object).getInformation());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (((Feature) object).getId() == 2) {
                selectedFeature = (Feature) object;
                getWheelItems();
            }else if (((Feature) object).getId() == 4) {

                selectedFeature= (Feature) object;
               getScratchItems();
            }
        }

    }

    private void getScratchItems() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_GUESS_THE_BRAND;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, GuessItemResponse.class, Request.Method.GET, url);
        }else {
            goToNoInternetActivity();
        }


    }

    private void getWheelItems() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_SPIN_RESPONSE;
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, SpinResponse.class, Request.Method.GET, url);
        }

    }

    private void blockUserEvents() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unBlockTouchEvents() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void setProfileImage() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            try {
                if (user != null) {
                    if (user.getProfilePicId() != null && user.getProfilePicId().intValue() != 0) {
                        profilePicId = user.getProfilePicId();
                        Glide.with(getApplicationContext())
                                .load(EightfoldsUtils.getInstance().getImageFullPath(user.getProfilePicId(), Constants.FILE_URL))
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
        } else {
            goToNoInternetActivity();
        }
    }

    private void updateLevel() {
        if (user.getCurrentLevel() != null && user.getCurrentLevel() != 0) {
            Utils.setProfileBGBasedOnLevel(this, user.getCurrentLevel().intValue(), profileRL);
        }
        if (user != null && user.getTotalLoyalityPoints() != null) {
            adjustPointsFontSizeAndBg();
        }
    }

    private void adjustPointsFontSizeAndBg() {

        String points = Integer.toString(user.getTotalLoyalityPoints().intValue());
        Utils.adjustPointsFontSizeAndBg(this, points, pointsRL, pointTv);
    }
}
