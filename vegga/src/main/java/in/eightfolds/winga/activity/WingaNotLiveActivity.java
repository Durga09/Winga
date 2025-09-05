package in.eightfolds.winga.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.ParseException;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class WingaNotLiveActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack, OnSuccessListener<ShortDynamicLink> {

    private User userDetails;
    private TextView goLiveTimeTv, referralCodeTv, dateDescTv, goLiveDescTv, logOutTv;
    String mInvitationUrl;

    private String goLiveTime, goLiveMessage, currentServerTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_winga_not_live);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.GOLIVE_DATE)) {
                goLiveTime = bundle.getString(Constants.GOLIVE_DATE);
            }
            if (bundle.containsKey(Constants.GOLIVE_MESSAGE)) {
                goLiveMessage = bundle.getString(Constants.GOLIVE_MESSAGE);
            }
            if (bundle.containsKey(Constants.CURRENT_SERVER_TIME)) {
                currentServerTime = bundle.getString(Constants.CURRENT_SERVER_TIME);
            }
        }
        initialize();
    }

    private void initialize() {

        goLiveTimeTv = findViewById(R.id.goLiveTimeTv);
        referralCodeTv = findViewById(R.id.referralCodeTv);
        dateDescTv = findViewById(R.id.dateDescTv);
        goLiveDescTv = findViewById(R.id.goLiveDescTv);
        logOutTv = findViewById(R.id.logOutTv);
        findViewById(R.id.shareBtn).setOnClickListener(this);
        findViewById(R.id.tapAreaLL).setOnClickListener(this);
        findViewById(R.id.closeIv).setOnClickListener(this);
        //logOutTv.setOnClickListener(this);
        setLogOut();
        userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);

        if (!TextUtils.isEmpty(goLiveMessage)) {
            goLiveDescTv.setText(goLiveMessage);
        }
        if (!TextUtils.isEmpty(goLiveTime)) {
            try {
                goLiveTimeTv.setText(DateTime.getDateFromUTC(goLiveTime, "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a"));
                dateDescTv.setVisibility(View.VISIBLE);
                goLiveTimeTv.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            dateDescTv.setVisibility(View.GONE);
            goLiveTimeTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userDetails.getReferralCode())) {
            referralCodeTv.setText(userDetails.getReferralCode());
        }
    }

    private void setLogOut(){
        SpannableString exitSpan = Utils.makeLinkSpan(getString(R.string.logout), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.logOutUser(WingaNotLiveActivity.this, WingaNotLiveActivity.this);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logOutTv.setText(exitSpan);
        Utils.makeLinksFocusable(logOutTv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.logOutTv) {

        } else if (v.getId() == R.id.closeIv) {
            onBackPressed();
        } else if (v.getId() == R.id.tapAreaLL) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("referralcode", referralCodeTv.getText());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(WingaNotLiveActivity.this, getString(R.string.code_copied));
        } else if (v.getId() == R.id.shareBtn) {
            String referralCode = userDetails.getReferralCode();
            if (!TextUtils.isEmpty(referralCode)) {
                if (TextUtils.isEmpty(mInvitationUrl)) {
                    Utils.generateDynamicLink(referralCode, this);
                } else {
                    Utils.shareCodeUsingFCMGeneratedURL(WingaNotLiveActivity.this, mInvitationUrl);
                }
            } else {
                MyDialog.showToast(WingaNotLiveActivity.this, getString(R.string.service_not_available));
            }
        }
    }


    @Override
    public void onEventListener() {
        //Intentionally left blank
    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finish();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {
        //Intentionally left blank
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
            goToDashBoardActivity((HomePageResponse) object);
        } else if (object instanceof Setup) {
            Setup registerSetUpDetails = (Setup) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.SET_UP_DETAILS, registerSetUpDetails);
            getHomePageResponse();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


    private void getHomePageResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            Intent intent = new Intent(this, HomeBaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            Utils.setAppLanguage(this);
            this.finish();
            this.startActivity(intent);
        }
    }

    private void getSetup() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = Constants.GET_SETUP_SECURE;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Setup.class, Request.Method.GET, url);
        }
    }

    private void goToDashBoardActivity(HomePageResponse homePageResponse) {
        HomePageAd homePageAd = Utils.getHomePageAdFromSetUp(this);
        if (homePageAd != null) {
            Intent intent = new Intent(this, AdActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            intent.putExtra(Constants.ADD_DETAILS, homePageAd);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.HOME_DATA, homePageResponse);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSuccess(ShortDynamicLink shortDynamicLink) {
        try {
            mInvitationUrl = shortDynamicLink.getShortLink().toString();
            Utils.shareCodeUsingFCMGeneratedURL(WingaNotLiveActivity.this, mInvitationUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
