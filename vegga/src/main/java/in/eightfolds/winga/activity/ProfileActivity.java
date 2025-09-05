package in.eightfolds.winga.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
/*import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;*/

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;*/
import in.eightfolds.WingaApplication;
import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.rest.UploadFile;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.utils.MyScrollView;
import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.EightfoldsImageListener;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AppFile;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.model.UserAddress;
import in.eightfolds.winga.swipe.SwipeActivity;
import in.eightfolds.winga.swipe.SwipeLayout;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.ShowCaseUtils;
import in.eightfolds.winga.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class ProfileActivity extends SwipeActivity implements View.OnClickListener, OnEventListener, VolleyResultCallBack, EightfoldsImageListener, CompoundButton.OnCheckedChangeListener, OnSuccessListener<ShortDynamicLink>, BottomSheetImagePicker.OnImagesSelectedListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int UPDATEPROFILE_CODE = 102;
    private TextView versionNumTv, viewAllTv;
    private String path;
    private ImageView disableNotificationsSwitch;
    private User userResponse;
    private TextView loyaltyPointsTv, numberOfgamesTv, numberOfGamesWonTv, levelTv,tv_centre_mobile;
    private TextView addressTv;
    public static int SETTINGS_CHANGE_REQ_CODE = 100;
    public static int ADDRESS_REQ_CODE = 300;

    private static final int RC_CAMERA_AND_LOCATION = 123;
    Dialog myProgressDialog;

    private ProfileRefreshBroadcastReceiver profileRefreshBroadcastReceiver;

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";


    ImageView iv_profile_Top, iv_profile_centre;
    MyScrollView scrollView;
    RelativeLayout mHeader;

    private int mMinHeaderTranslation;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    int mActionBarHeight;

    TextView tv_centre_name, tv_top_name;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    private ImageView editIv;
    private TextView rewardsValueTv;

    Configuration configuration;
    private String mInvitationUrl;

    private String totalRewardsAmountText = "";
    private String selectedOption = "";
    private TextView poweredByTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        setContentView(R.layout.activity_profile);
        String imei = EightfoldsUtils.getInstance().getDeviceIMEI(this);
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.IMEI, imei);
        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        initialize(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (configuration != null) {
            onConfigurationChanged(configuration);
        }

        setDragEdge(SwipeLayout.DragEdge.TOP);


        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);

        if (userResponse != null && userResponse.getProfilePicId() != null && userResponse.getProfilePicId() != 0) {
            loadProfilePic(userResponse.getProfilePicId());
        } else {
            iv_profile_centre.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_filled));
        }

        if (profileRefreshBroadcastReceiver == null) {
            profileRefreshBroadcastReceiver = new ProfileRefreshBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.PROFILE_REFRESH_ACTION);
            filter.addAction(Constants.PROFILE_LANGUAGE_REFRESH_ACTION);
            this.registerReceiver(profileRefreshBroadcastReceiver, filter);
        }


        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                getProfile();
                setVersionNum();
            }
        };
        handler.postDelayed(r, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (profileRefreshBroadcastReceiver != null) {
            unregisterReceiver(profileRefreshBroadcastReceiver);
        }
    }

    private void getProfile() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().makingStringRequest(this, User.class, Request.Method.GET, Constants.GET_USER_PROFILE_URL);

        }
    }

    private void editProfile() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.EDIT_USER_PROFILE_URL;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingJsonRequest(this, User.class, Request.Method.POST, url, userResponse);
        }
    }
  private  void  selectImage() {
        new BottomSheetImagePicker.Builder(BuildConfig.APPLICATION_ID + ".provider")
                .singleSelectTitle(R.string.pick_single)
                .peekHeight(R.dimen.peekHeight)
                .columnSize(R.dimen.columnSize)
                .show(getSupportFragmentManager(),"Image Picker");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("translateY", mHeader.getTranslationY());
        outState.putInt("scrollY", scrollView.getScrollY());
        Logg.v(TAG, "$$onSaveInstanceState >. mMinHeaderTranslation >> " + mMinHeaderTranslation);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Float translateY = savedInstanceState.getFloat("translateY");
        Logg.v(TAG, "**onRestoreInstanceState >. mMinHeaderTranslation >> " + mMinHeaderTranslation);
        finish();
        startActivity(getIntent());
    }

    private void performOnScroll(int scrollY) {

        Logg.v(TAG, "&&scrollY >> " + scrollY + " mMinHeaderTranslation " + mMinHeaderTranslation);
        mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
        float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
        interpolate(iv_profile_centre, iv_profile_Top, mSmoothInterpolator.getInterpolation(ratio));
        interpolate(tv_centre_name, tv_top_name, mSmoothInterpolator.getInterpolation(ratio));

        Logg.v(TAG, "&&Ratio >> " + ratio);
        if (ratio == 0) {
            setViewAlpha(1.0F);
        } else
            setViewAlpha(0.5F - ratio);// //clamp(1.0F - ratio , 1.0F, 0.0F));  //5.0F * ratio - 4.0F
    }

    private int previousScrollY;

    private void addAnimation() {
        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        int mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        int actionBarHeight = getActionBarHeight();
        // actionBarHeight = 100;
        mMinHeaderTranslation = -mHeaderHeight + actionBarHeight;
        Logg.v(TAG, "actionBarHeight>> " + actionBarHeight + "mHeaderHeight : " + mHeaderHeight + " mMinHeaderTranslation >" + mMinHeaderTranslation);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                int scrollY = scrollView.getScrollY();
                performOnScroll(scrollY);

            }

        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    scrollView.startScrollerTask();
                }

                return false;
            }
        });
        scrollView.setOnScrollStoppedListener(new MyScrollView.OnScrollStoppedListener() {

            public void onScrollStopped() {


                int scrollY = scrollView.getScrollY();
                Logg.i(TAG, "stopped &&  scrollY >>" + scrollY + "  ,previousScrollY >> " + previousScrollY);
                if (scrollY > previousScrollY) {
                    if (scrollY < 510 && scrollY > 20) {
                        //performOnScroll(542);
                        //  scrollView.scrollTo(0,522);
                        scroolToYWithAnimation(510);
                    }
                } else if (scrollY < previousScrollY) {
                    if (scrollY < 300) {
                        scroolToYWithAnimation(0);
                    }
                }

                previousScrollY = scrollY;
            }
        });
    }

    public void scroolToYWithAnimation(int y) {
        int x = 0;
        //int y = 0;
        // ObjectAnimator xTranslate = ObjectAnimator.ofInt(scrollView, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollView, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(200L);
        //  animators.playTogether(xTranslate, yTranslate);
        animators.play(yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        animators.start();
    }


    public void setNameViewAlpha(float nameViewAlpha) {
        tv_centre_name.requestLayout();
        tv_centre_name.setAlpha(nameViewAlpha);

    }

    public void setViewAlpha(float viewAlpha) {
        View view = findViewById(R.id.levelTv);
        view.requestLayout();
        Logg.v(TAG, "&&& viewAlpha>> " + viewAlpha);
        Constants.viewAlpha = viewAlpha;
        view.setAlpha(viewAlpha);
        tv_centre_mobile.setAlpha(viewAlpha);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private void getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }


    public int getActionBarHeight() {

        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        TypedValue mTypedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        // MyDialog.showToast(this,"Action bar height : "+mActionBarHeight);
        if (mActionBarHeight < 115) {
            mActionBarHeight = 115;
        }
        return mActionBarHeight;
    }

    private void initialize(boolean fromConfigChange) {
        LinearLayout rewardsCountLL = findViewById(R.id.rewardsCountLL);
        LinearLayout contentLL = findViewById(R.id.contentLL);
        rewardsValueTv = findViewById(R.id.rewardsValueTv);
        editIv = findViewById(R.id.editIv);
        userResponse = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        addressTv = findViewById(R.id.addressTv);
        tv_centre_name = findViewById(R.id.tv_centre_name);
        versionNumTv = findViewById(R.id.versionNumTv);
        viewAllTv = findViewById(R.id.viewAllTv);
        tv_centre_mobile = findViewById(R.id.tv_centre_mobile);
        ImageView closeIv = findViewById(R.id.closeIv);

        iv_profile_Top = findViewById(R.id.iv_profile);
        scrollView = findViewById(R.id.scrollView);
        mHeader = findViewById(R.id.header);
        tv_top_name = findViewById(R.id.tv_top_name);
        iv_profile_centre = findViewById(R.id.iv_profile_centre);
        disableNotificationsSwitch = findViewById(R.id.disableNotificationsSwitch);
        poweredByTv = findViewById(R.id.poweredByTv);
        LinearLayout historyLL = findViewById(R.id.historyLL);
        LinearLayout referEarnLL = findViewById(R.id.referEarnLL);
        LinearLayout tutorialLL = findViewById(R.id.tutorialLL);
        LinearLayout notificationsLL = findViewById(R.id.notificationsLL);
        LinearLayout settingsLL = findViewById(R.id.settingsLL);
        LinearLayout rateOurAppLL = findViewById(R.id.rateOurAppLL);
        LinearLayout shareLL = findViewById(R.id.shareLL);
        LinearLayout supportLL = findViewById(R.id.supportLL);
        LinearLayout termsConditionsLL = findViewById(R.id.termsConditionsLL);
        LinearLayout privacyPolicyLL=findViewById(R.id.privacyPolicyLL);
        LinearLayout logoutLL = findViewById(R.id.logoutLL);
        LinearLayout addressesLL = findViewById(R.id.addressesLL);

        levelTv = findViewById(R.id.levelTv);

        loyaltyPointsTv = findViewById(R.id.numberOfLoyaltyPointsTv);
        numberOfgamesTv = findViewById(R.id.numberOfgamesTv);
        numberOfGamesWonTv = findViewById(R.id.numberOfGamesWonTv);
        String privacyPolicyText=" & "+getString(R.string.privacy_policy);

        rewardsCountLL.setOnClickListener(this);
        findViewById(R.id.loyaltyPointsLL).setOnClickListener(this);
        findViewById(R.id.gamesLL).setOnClickListener(this);
        findViewById(R.id.gamesWonLL).setOnClickListener(this);
        closeIv.setOnClickListener(this);
        editIv.setOnClickListener(this);
        historyLL.setOnClickListener(this);
        referEarnLL.setOnClickListener(this);
        tutorialLL.setOnClickListener(this);
        notificationsLL.setOnClickListener(this);
        settingsLL.setOnClickListener(this);
        rateOurAppLL.setOnClickListener(this);
        privacyPolicyLL.setOnClickListener(this);
        shareLL.setOnClickListener(this);
        supportLL.setOnClickListener(this);
        termsConditionsLL.setOnClickListener(this);
        findViewById(R.id.faqLL).setOnClickListener(this);
        addressesLL.setOnClickListener(this);
        logoutLL.setOnClickListener(this);
        disableNotificationsSwitch.setOnClickListener(this);
        findViewById(R.id.versionLL).setOnClickListener(this);

        iv_profile_centre.setOnClickListener(this);
        findViewById(R.id.iv_center_position_view).setOnClickListener(this);

        addAnimation();


        ViewCompat.setTransitionName(iv_profile_centre, VIEW_NAME_HEADER_IMAGE);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("profilepic")) {
                Long profilePicId = b.getLong("profilepic");
                loadProfilePic(profilePicId);
            }

            if (b.containsKey("name")) {
                String name = b.getString("name");
                tv_centre_name.setText(!TextUtils.isEmpty(name) ? name : "");
                tv_top_name.setText(!TextUtils.isEmpty(name) ? name : "");
            }

            if (b.containsKey("level")) {
                String level = b.getString("level");
                levelTv.setText(!TextUtils.isEmpty(level) ? String.format(Utils.getCurrentLocale(), getString(R.string.you_are_in_level), level) : "");

            }
        }

        refreshData();


        if (!fromConfigChange) {
            // LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            //contentLL.setLayoutAnimation(animation);
        }


        editIv.post(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {

                        ShowCaseUtils.presentProfileShowcaseSequence(ProfileActivity.this, editIv);
                    }
                };
                handler.postDelayed(r, 400);

            }
        });
        poweredByTv.setOnClickListener(this);
    }


    private void setNumberOfDays(String date) {

        try {

            long datemillis = DateTime.getInMilliesFromUTC(date);
            String getNowInUtc = DateTime.getNowInUTC();
            long todayMillis = DateTime.getInMilliesFromUTC(getNowInUtc);


            long different = todayMillis - datemillis;

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;


            long elapsedDays = different / daysInMilli;
            long daysTillNow = elapsedDays % daysInMilli;

            loyaltyPointsTv.setText(String.format(Utils.getCurrentLocale(), "%d", daysTillNow + 1));


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    String viewAllTxt = "";

    private void setViewAddressSpan() {

//        if (userResponse != null && userResponse.getPrimaryAddress() != null && !TextUtils.isEmpty(userResponse.getPrimaryAddress().getPincode())) {
//            viewAllTxt = getString(R.string.view_all);
//        } else {
//            viewAllTxt = getString(R.string.add_address);
//        }
        SpannableString viewAddressSpan = makeLinkSpan(viewAllTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAllTxt.equalsIgnoreCase(getString(R.string.view_all))) {

                    Intent intent = new Intent(ProfileActivity.this, MyAddressActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfileActivity.this, AddNewAddressActivity.class);
                    startActivityForResult(intent, ADDRESS_REQ_CODE);
                }
            }
        });

        viewAddressSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_red)), 0, viewAddressSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan iss = new StyleSpan(Typeface.NORMAL);
        viewAddressSpan.setSpan(iss, 0, viewAddressSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewAllTv.setText(viewAddressSpan);
        makeLinksFocusable(viewAllTv);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        // setContentView(R.layout.activity_profile);
        // initialize(true);
        resetAllTextViews();
        configuration = null;


    }

    private void resetAllTextViews() {

        ((TextView) findViewById(R.id.totalRewardsTv)).setText(getString(R.string.total_rewards));
        ((TextView) findViewById(R.id.loyaltyPointsDesc)).setText(getString(R.string.loyalty_points));
        ((TextView) findViewById(R.id.gamesPlayedDesc)).setText(getString(R.string.games_play));
        ((TextView) findViewById(R.id.myHistoryDescTv)).setText(getString(R.string.my_history));
        ((TextView) findViewById(R.id.addressesDescTv)).setText(getString(R.string.addresses));
        ((TextView) findViewById(R.id.viewAllTv)).setText(getString(R.string.view_all));
        ((TextView) findViewById(R.id.referEarnDescTv)).setText(getString(R.string.refer_earn));
        ((TextView) findViewById(R.id.notificationsDescTv)).setText(getString(R.string.notifications));
        ((TextView) findViewById(R.id.settingsDescTv)).setText(getString(R.string.settings));
        ((TextView) findViewById(R.id.rateOurAppDescTv)).setText(getString(R.string.rate_our_app));
        ((TextView) findViewById(R.id.shareDescTv)).setText(getString(R.string.share));
        ((TextView) findViewById(R.id.supportDescTv)).setText(getString(R.string.support));
        ((TextView) findViewById(R.id.termsDescTv)).setText(getString(R.string.terms_conditions));
        ((TextView) findViewById(R.id.privacyPolicyTv)).setText(getString(R.string.privacy_policy));

        ((TextView) findViewById(R.id.logOutDescTv)).setText(getString(R.string.logout));
        ((TextView) findViewById(R.id.aboutDescTv)).setText(getString(R.string.about));
        ((TextView) findViewById(R.id.tutorialDescTv)).setText(getString(R.string.tutorial));


        setVersionNum();
        setViewAddressSpan();

        if (userResponse != null && userResponse.getTotalWins() != null && userResponse.getTotalWins() == 1) {
            ((TextView) findViewById(R.id.gameWonDesc)).setText(getString(R.string.game_won));
        } else {
            ((TextView) findViewById(R.id.gameWonDesc)).setText(getString(R.string.games_won));
        }

        String userLevel = null;
        if (userResponse != null) {
            userLevel = Utils.getUserLevelFromId(this, userResponse.getCurrentLevel());
        }
        levelTv.setText(!TextUtils.isEmpty(userLevel) ? String.format(Utils.getCurrentLocale(), getString(R.string.you_are_in_level), userLevel) : "");
    }

    int poweredByClicked = 0;

    @Override
    public void onClick(View v) {
        /*try {
            Intent intent;
            switch (v.getId()) {

                case R.id.loyaltyPointsLL:
                    intent = new Intent(this, LoyaltyPointsHistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.gamesLL:
                    intent = new Intent(this, PlayHistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.gamesWonLL:
                    intent = new Intent(this, PlayHistoryActivity.class);
                    intent.putExtra("showWinOnly", true);
                    startActivity(intent);
                    break;
                case R.id.rewardsCountLL:
                    intent = new Intent(this, TotalRewardsActivity.class);
                    if (!TextUtils.isEmpty(totalRewardsAmountText)) {
                        intent.putExtra(Constants.DATA, totalRewardsAmountText);
                    }
                    startActivity(intent);

                    break;
                case R.id.versionLL:
                    intent = new Intent(this, WebBrowserActivity.class);
                    intent.putExtra(Constants.DATA, Constants.WEGGA_WEBSITE_URL);
                    intent.putExtra(Constants.TITLE, getString(R.string.about));
                    startActivity(intent);
                    break;
                case R.id.addressesLL:
                    intent = new Intent(ProfileActivity.this, MyAddressActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_profile_centre:
                case R.id.iv_center_position_view:
                    if (userResponse != null && userResponse.getProfilePicId() != null && userResponse.getProfilePicId() != 0) {
                        intent = new Intent(this, ImageViewEditActivity.class);
                        intent.putExtra(Constants.DATA, userResponse.getProfilePicId() + "");
                        startActivity(intent);
                    } else {
//                        EightfoldsUtils.getInstance().verifyStoragePermissions(this);
////                        final Handler handler = new Handler();
////                        handler.postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
//                                // Do something after 5s = 5000ms
//                                selectImage();
////                            }
////                        }, 1000);

                     *//*   Permissions.check(this,new String[] {Manifest.permission.CAMERA}, null,null,new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                methodRequiresTwoPermission();
                            }
                        });
*//*

                    }
                    break;
                case R.id.camIV:
                    // MyDialog.uploadPhotosDialog(this, this);

                    break;
                case R.id.editIv:
                    if (userResponse != null && !TextUtils.isEmpty(userResponse.getName())) {
                        Intent intent1 = new Intent(this, UpdateProfileActivity.class);
                        startActivityForResult(intent1, UPDATEPROFILE_CODE);
                    }
                    break;

                case R.id.closeIv:
                    onBackPressed();
                    break;

                case R.id.historyLL:
                    intent = new Intent(this, TotalRewardsActivity.class);
                    if (!TextUtils.isEmpty(totalRewardsAmountText)) {
                        intent.putExtra(Constants.DATA, totalRewardsAmountText);
                    }
                    startActivity(intent);
                    break;

                case R.id.referEarnLL:
                    intent = new Intent(this, CodesAndInvitesActivity.class);
                    intent.putExtra(Constants.NAVIGATE_TAB_POSITION, 2);
                    startActivity(intent);
                    break;
                case R.id.tutorialLL:

//                    ShowCaseUtils.RestartShowCaseView(this);
//                    intent = new Intent(this, HomeActivity.class);
//                    intent.putExtra(Constants.FROM_SPLASH, false);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    finish();
//                    startActivity(intent);


                    break;

                case R.id.notificationsLL:
                    intent = new Intent(this, NotificationActivity.class);
                    startActivity(intent);
                    break;

                case R.id.settingsLL:
                    intent = new Intent(this, SettingsActivity.class);
                    if (userResponse != null) {
                        intent.putExtra(Constants.DATA, userResponse);

                        startActivityForResult(intent, SETTINGS_CHANGE_REQ_CODE);
                    }
                    break;

                case R.id.rateOurAppLL:
                    String PACKAGE_NAME = Utils.getPackageName(this);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        MyDialog.showToast(this, getString(R.string.unable_top_open_playstore));
                    }


                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                    break;

                case R.id.shareLL:

                    if (EightfoldsImage.getInstance().verifyCameraStoragePermissions(this)) {
                        shareApp();
                    }
                    break;

                case R.id.supportLL:
                    intent = new Intent(this, SupportActivity.class);
                    startActivity(intent);
                    break;

                case R.id.termsConditionsLL:
                    intent = new Intent(this, WebBrowserActivity.class);
                    // intent = new Intent(this, WinLuckyDrawActivity.class);
                    intent.putExtra(Constants.DATA, Constants.TERMS_CONDITIONS_URL);
                    intent.putExtra(Constants.TITLE, getString(R.string.terms_conditions));
                    startActivity(intent);
                    break;

                case R.id.privacyPolicyLL:
                    intent = new Intent(this, WebBrowserActivity.class);
                    // intent = new Intent(this, WinLuckyDrawActivity.class);
                    intent.putExtra(Constants.DATA, Constants.PRIVACY_POLICY_URL);
                    intent.putExtra(Constants.TITLE, getString(R.string.privacy_policy));
                    startActivity(intent);
                    break;
                case R.id.faqLL:
                    intent = new Intent(this, WebBrowserActivity.class);
                    String url = Constants.FAQ_URL;
                    if (url.contains("<LANG>")) {
                        url = url.replace("<LANG>", Utils.getCurrentISO(this));
                    }
                    Logg.v(TAG, "FAQ_URL > " + url);
                    intent.putExtra(Constants.DATA, url);
                    intent.putExtra(Constants.TITLE, getString(R.string.faq));
                    startActivity(intent);
                    break;

                case R.id.logoutLL:
                    MyDialog.logoutDialog(this, this, getString(R.string.logout), getString(R.string.do_u_want_logout), true);
                    break;

                case R.id.disableNotificationsSwitch:
                    if (userResponse != null) {
                        intent = new Intent(this, AlertsPromosUpdatesActivity.class);
                        intent.putExtra(Constants.DATA, userResponse);
                        startActivityForResult(intent, SettingsActivity.SETTINGS_CHANGE_REQ_CODE);
                    }
                    break;

                case R.id.poweredByTv:
                    poweredByClicked++;
                    if (poweredByClicked == 5) {
                      //  Constants.DontShowLogs = false;
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_SHARE || requestCode == EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_UPDATE_PROFILE_PIC) {
            int writePermission = ActivityCompat.checkSelfPermission(this
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (writePermission == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_SHARE) {
                    shareApp();
                } else if (requestCode == EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_UPDATE_PROFILE_PIC) {
                    pickorCaptureImageOnPermissionsGranted();
                }
            }
        } else if (requestCode == EightfoldsUtils.REQUEST_READ_PHONE_STATE) {
            int phoneStatePermission = ActivityCompat.checkSelfPermission(this
                    , Manifest.permission.READ_PHONE_STATE);
            if (phoneStatePermission == PackageManager.PERMISSION_GRANTED) {
             //   Utils.sendPushRegId(this, this);
            }
        }else if(RC_CAMERA_AND_LOCATION==requestCode){
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    private void shareApp() {
        if (userResponse != null && !TextUtils.isEmpty(userResponse.getReferralCode())) {
            String referralCode = userResponse.getReferralCode();

            if (TextUtils.isEmpty(mInvitationUrl)) {
                Utils.generateDynamicLink(referralCode, this);
            } else {
                Utils.shareCodeUsingFCMGeneratedURL(this, mInvitationUrl);
            }

        } else {
            MyDialog.showToast(this, getString(R.string.service_not_available));
        }
    }

    private void callLogout() {
        Utils.logOutUser(this, this);
        /*Intent intent = new Intent(this, LoginActivity.class);
        //   ShowCaseUtils.RestartShowCaseView(this);
        EightfoldsUtils.getInstance().saveToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, null);
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, "");
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.MOBILE_NUMBER, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOGIN_TYPE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRST_GAME_PLAYED, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.INVITED_BY_REF_CODE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.READ_NOTIFICATION_IDS, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRE_BASE_TOKEN, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.APP_FIRST_LOGGEDIN_DATE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.LOYALITY_POINTS, "");
        //EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.IS_TUTORIAL_SKIPPED, "");
        // EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION, "");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        MyDialog.showToast(this, getString(R.string.logged_out));

        startActivity(intent);
        finish();*/
    }

    private void setVersionNum() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionNumTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.version), version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEventListener() {

    }

    private void pickorCaptureImageOnPermissionsGranted() {
        if (selectedOption.equalsIgnoreCase("gallery")) {
            path = EightfoldsImage.getInstance().captureImageFromSdCard(this);
        } else if (selectedOption.equalsIgnoreCase("camera")) {
            path = EightfoldsImage.getInstance().captureImageFromCamera(this);
        }
    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            callLogout();
        }
        if (type == R.id.galleryLL) {
            selectedOption = "gallery";
            path = EightfoldsImage.getInstance().captureImageFromSdCard(this);
        } else if (type == R.id.cameraLL) {
            selectedOption = "camera";
            path = EightfoldsImage.getInstance().captureImageFromCamera(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EightfoldsImage.SELECT_FILE) {
                EightfoldsImage.getInstance().saveOnActivityResult(this, this, path, EightfoldsImage.SELECT_FILE, data);
            } else if (requestCode == EightfoldsImage.TAKE_PICTURE) {
                EightfoldsImage.getInstance().saveOnActivityResult(this, this, path, EightfoldsImage.TAKE_PICTURE, data);
            } else if (requestCode == Constants.CROP) {
                path = data.getStringExtra(Constants.PATH);
                setImage(path);
            } else if (requestCode == SETTINGS_CHANGE_REQ_CODE || requestCode == SettingsActivity.SETTINGS_CHANGE_REQ_CODE) {
                if (data != null && data.hasExtra(Constants.DATA)) {
                    userResponse = (User) data.getExtras().get(Constants.DATA);
                    if (userResponse != null) {
                        refreshData();
                    }
                } else {
                    if (WingaApplication.getInstance().getConfiguration() != null) {
                        configuration = WingaApplication.getInstance().getConfiguration();
                    } else {
                        configuration = Utils.setAppLanguage(this);
                    }
                    if (configuration != null) {
                        onConfigurationChanged(configuration);
                    }
                }

            } else if (requestCode == ADDRESS_REQ_CODE) {
                //  getProfile();
            } else if (requestCode == UPDATEPROFILE_CODE) {
                if (data.getExtras() != null && data.getExtras().containsKey(Constants.DATA)) {
                    userResponse = (User) data.getExtras().get(Constants.DATA);
                    if (userResponse != null) {
                        refreshData();
                    }
                }
            }else if (requestCode == 2 || requestCode == 2296) {
                openFilePicker();
            } else {
                if (data != null) {

                    switch (requestCode) {
                      /*  case FilePickerConst.REQUEST_CODE_DOC:
                        case FilePickerConst.REQUEST_CODE_PHOTO:
                            ArrayList<Uri> uriArrayList=new ArrayList<>();
                            uriArrayList.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                            try {
                                gotoCropImageActivity(Objects.requireNonNull(ContentUriUtils.INSTANCE.getFilePath(this
                                        , uriArrayList.get(0))));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }*/
                    }

                }
            }
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

        if (type == R.id.yesTv) {
            boolean isChecked = (boolean) object;
            if (isChecked != userResponse.isNotification()) {
                userResponse.setNotification(isChecked);
                disableNotificationsSwitch.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_on));
                editProfile();
            }
        }
    }

    private void setPrimaryAddress(UserAddress primaryAddress) {
        if (primaryAddress != null) {
            String addressText = "";

            if (!TextUtils.isEmpty(primaryAddress.getAddress2())) {
                addressText += primaryAddress.getAddress2();
            }
            if (!TextUtils.isEmpty(primaryAddress.getAddress1())) {
                addressText += ", " + primaryAddress.getAddress1();
            }
            if (!TextUtils.isEmpty(primaryAddress.getCity())) {
                addressText += ", " + primaryAddress.getCity();
            }

            if (primaryAddress.getStateId() != 0) {
                addressText += ", " + Utils.getStateNameFromId(this, primaryAddress.getStateId().intValue());
            }

            if (!TextUtils.isEmpty(primaryAddress.getLandmark())) {
                addressText += ", Landmark: " + primaryAddress.getLandmark();
            }
            if (!TextUtils.isEmpty(primaryAddress.getPincode())) {
                addressText += ", Pincode: " + primaryAddress.getPincode();
            }

            addressTv.setText(addressText);
        } else {
            addressTv.setText("");
        }
    }


    private void refreshData() {

        if (userResponse != null) {
            setViewAddressSpan();

            String userLevel = null;
            if (userResponse != null) {
                userLevel = Utils.getUserLevelFromId(this, userResponse.getCurrentLevel());
            }
            levelTv.setText(!TextUtils.isEmpty(userLevel) ? String.format(Utils.getDefualtLocale(), getString(R.string.you_are_in_level), userLevel) : "");


            disableNotificationsSwitch.setVisibility(View.VISIBLE);
            if (!userResponse.isAlert() && !userResponse.isPromotion() && !userResponse.isUpdates()) {
                disableNotificationsSwitch.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_off));
            } else {
                disableNotificationsSwitch.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_on));
            }
            numberOfgamesTv.setText(String.format(Utils.getDefualtLocale(), "%d", userResponse.getTotalGamePlayed()));

            if (userResponse.getTotalWins() != null) {
                if (userResponse.getTotalWins() == 1) {
                    ((TextView) findViewById(R.id.gameWonDesc)).setText(getString(R.string.game_won));
                } else {
                    ((TextView) findViewById(R.id.gameWonDesc)).setText(getString(R.string.games_won));
                }
                numberOfGamesWonTv.setText(String.format(Utils.getDefualtLocale(), "%d", userResponse.getTotalWins()));
            } else {
                numberOfGamesWonTv.setText("0");
            }

            float rewardsAmount = userResponse.getTotalRewardsAmt();
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);

            DecimalFormat decimalFormat = (DecimalFormat) nf;
            decimalFormat.applyPattern("#.##");
            String twoDigitsF = decimalFormat.format(rewardsAmount);


            totalRewardsAmountText = getString(R.string.rs) + twoDigitsF;

            rewardsValueTv.setText(totalRewardsAmountText);


            tv_centre_name.setText(!TextUtils.isEmpty(userResponse.getName()) ? userResponse.getName() : "");
            tv_top_name.setText(!TextUtils.isEmpty(userResponse.getName()) ? userResponse.getName() : "");
            tv_centre_name.setVisibility(View.VISIBLE);

            tv_centre_mobile.setText(!TextUtils.isEmpty(userResponse.getMobile()) ? userResponse.getMobile() : "");
            tv_centre_mobile.setVisibility(View.VISIBLE);
            //setNumberOfDays(userResponse.getCreatedTime());

            if (userResponse.getTotalLoyalityPoints() != null) {
                loyaltyPointsTv.setText(Integer.toString(userResponse.getTotalLoyalityPoints().intValue()));
            }
//            if (userResponse.getPrimaryAddress() != null) {
//                setPrimaryAddress(userResponse.getPrimaryAddress());
//            } else {
//                addressTv.setText("");
//            }
            if (userResponse.getProfilePicId() != null && userResponse.getProfilePicId() != 0) {
                loadProfilePic(userResponse.getProfilePicId());
            } else {
                iv_profile_centre.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_filled));
            }
        }
    }

    private void loadProfilePic(long profilePicId) {
        try {
            String activityName = EightfoldsUtils.getInstance().getTopmostActivity(this);
            if ((activityName.equals("in.eightfolds.winga.activity.ProfileActivity"))) {
                Glide.with(this)
                        .load(Constants.FILE_URL + profilePicId)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.ic_user_filled)
                        .error(R.drawable.ic_user_filled)
                        .into(iv_profile_centre);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof User) {
            userResponse = (User) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, userResponse);
            refreshData();

        } else if (requestType.equalsIgnoreCase(Constants.UPLOAD_FILE_URL)) {
            Logg.v(TAG, "**Upload profile pic success");
            AppFile appFile = (AppFile) object;
            userResponse.setProfilePicId(appFile.getFileId());
            loadProfilePic(userResponse.getProfilePicId());
            editProfile();
        }
    }


    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onEvent(int requestType) {

    }

    @Override
    public void onEvent(int requestType, Object object) {

        if (requestType == EightfoldsImage.SELECT_FILE && object != null) {

            //setImage(object);
            gotoCropImageActivity(object);
        } else if (requestType == EightfoldsImage.TAKE_PICTURE && object != null) {
            //setImage(object);
            gotoCropImageActivity(object);
        }
        /*if (requestType == EightfoldsImage.SELECT_FILE && object != null) {

            setImage(object);
        } else if (requestType == EightfoldsImage.TAKE_PICTURE && object != null) {
            setImage(object);
        }*/
    }

    private void gotoCropImageActivity(Object objectPath) {
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra(Constants.DATA, objectPath.toString());
        intent.putExtra(Constants.IS_TO_CROP, true);
        startActivityForResult(intent, Constants.CROP);
    }

    private void setImage(final Object object) {
        try {
            String pathString = null;
            if (!TextUtils.isEmpty(object.toString())) {
                pathString = EightfoldsUtils.getInstance().getImageFullPath(object, Constants.FILE_URL);
            }

            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                myProgressDialog = MyDialog.showProgress(this);
//                UploadFile.uploadMultipartWithOutNotification(this, path, this, true, this);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (userResponse != null && isChecked != userResponse.isNotification()) {
            if (isChecked) {
                MyDialog.NotificationsDialog(this, this, getString(R.string.notifications), getString(R.string.enable_notications), isChecked);
            } else {
                MyDialog.NotificationsDialog(this, this, getString(R.string.notifications), getString(R.string.turn_off_notications), isChecked);

            }
        }
    }


   /* @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        Logg.i("onProgress", "onProgress");

        uploadInfo.getProgressPercent();
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
//                                    Logg.i("onError", "onError" + exception.toString());

        onVolleyErrorListener(context.getString(R.string.something_wrong));
        if (myProgressDialog != null)
            myProgressDialog.dismiss();
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

        Logg.i("onCompleted", "onCompleted");

        if (myProgressDialog != null)
            myProgressDialog.dismiss();

        AppFile appFile = null;

        try {
            appFile = (AppFile) Api.fromJson(serverResponse.getBodyAsString(), AppFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onVolleyResultListener(appFile, Constants.UPLOAD_FILE_URL);

        Logg.i("onCompleted", "" + serverResponse.getBodyAsString());


    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        Logg.i("onCancelled", "onCancelled");

        if (myProgressDialog != null)
            myProgressDialog.dismiss();

        onVolleyErrorListener("Upload cancelled");
    }
*/
    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @org.jetbrains.annotations.Nullable String s) {

        if(!list.isEmpty()){
            gotoCropImageActivity(list.get(0).getPath());
        }

    }



//    public void uploadImages() {
//        UploadFileAsyncTask(
//                WeakReference(requireActivity()),
//                this,
//                uriList!!,
//                showCount = false
//        ).execute()
//    }



    public class ProfileRefreshBroadcastReceiver extends BroadcastReceiver {
        private final String TAG = ProfileRefreshBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(Constants.PROFILE_REFRESH_ACTION)) {
                Logg.v(TAG, "*** Profile page RefreshBoardcast received");

                if (intent.getExtras() != null && intent.getExtras().containsKey(Constants.DATA)) {
                    userResponse = (User) intent.getExtras().get(Constants.DATA);
                } else {
                    getProfile();
                }
            } else if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(Constants.PROFILE_LANGUAGE_REFRESH_ACTION)) {
                Logg.v(TAG, "*** Profile page language RefreshBoardcast received");
                if (WingaApplication.getInstance().getConfiguration() != null) {
                    configuration = WingaApplication.getInstance().getConfiguration();
                } else {
                    configuration = Utils.setAppLanguage(ProfileActivity.this);
                }
            }
        }
    }

    public SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);

        link.setSpan(new BaseActivity.ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    @Override
    public void onSuccess(ShortDynamicLink shortDynamicLink) {
        Logg.v(TAG, "onSuccess >> shortDynamicLink ;  " + shortDynamicLink);
        mInvitationUrl = shortDynamicLink.getShortLink().toString();
        Utils.shareCodeUsingFCMGeneratedURL(this, mInvitationUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logg.v(TAG, " && Profile Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logg.v(TAG, " && Profile Stop");
    }

/*    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {

        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            // Already have permission, do the thing
            // ...
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    openFilePicker();
                }
            }, 1000);

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    RC_CAMERA_AND_LOCATION, FilePickerConst.PERMISSIONS_FILE_PICKER);
        }
    }*/

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private void openFilePicker() {
        try{
          /*  FilePickerBuilder.getInstance()
                    .setMaxCount(1) //optional
                    .setActivityTheme(R.style.LibAppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(true).enableImagePicker(true)
                    .pickPhoto(this);*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}



