package in.eightfolds;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;

import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.gotev.uploadservice.UploadService;

import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.FooterAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Foreground;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by sp on 09/05/18.
 */

//@ReportsCrashes(mailTo = "swapnika.voora@eightfolds.in", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)

public class WingaApplication extends MultiDexApplication implements VolleyResultCallBack {
    private final String TAG = WingaApplication.class.getSimpleName();
    private static WingaApplication application;
    public static Context applicationContext;
    long countDownInterval = 20 * 1000; // 1 minute

    public  Long adBannerBitmap;
    private  FooterAd footerAd;

    public final Handler mHandler = new Handler();


    private LoginData loginData;
    public static FirebaseAnalytics analytics;


    public static WingaApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        //ACRA.init(this);
        Foreground.init(this);

        applicationContext = getApplicationContext();
        EightfoldsImage.getInstance().initImageLoader(getApplicationContext());
        EightfoldsVolley.getInstance().init(getApplicationContext());
//        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        //getFooterAddResponse();

        initTracker(getApplicationContext());
        try {
            new WebView(this).destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // mHandler.removeCallbacks(mUpdateTimeTask);

    }


    @Override
    protected void attachBaseContext(Context base) {
//        Utils.setAppLanguage(base);
        super.attachBaseContext(base);
    }

    public void initTracker(Context context) {
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    private Configuration configuration;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Logg.d(TAG, "** Update Banner Task");

            mHandler.postDelayed(mUpdateTimeTask, countDownInterval);

          //  getFooterAddResponse();



        }
    };

    public  Long getAdBannerBitmap() {
        return adBannerBitmap;
    }

    public FooterAd getFooterAd() {
        return footerAd;
    }

    public  void setAdBannerBitmap(Long adBannerBitmap) {
       this.adBannerBitmap = adBannerBitmap;
    }

    private void getFooterAddResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            Long lastFooterId = null;
            if (footerAd != null && footerAd.getFooterAddId() > 0) {
                lastFooterId = footerAd.getFooterAddId();
            }
            String filePath = Constants.GET_ADD_BANNER_URL;
            filePath = filePath.replace("{random}", System.currentTimeMillis() + "");
            if (lastFooterId != null) {
                filePath = filePath.replace("{exclude}", lastFooterId + "");
            } else {
                filePath = filePath.replace("{exclude}", "");
            }

            EightfoldsVolley.getInstance().makingStringRequest(this, FooterAd.class, Request.Method.GET, filePath);
            //WingaApplication.getInstance().setAdBannerBitmap((long) 0);

            Intent intent = new Intent();
            intent.setAction(Constants.REFRESH_BANNER_ACTION);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof FooterAd) {
            footerAd = (FooterAd) object;
            if (footerAd.getFrequency() > 0) {
                countDownInterval = footerAd.getFrequency() * 1000;
            }
            if (footerAd.getFileId() > 0) {
                setWinGaBanner(getApplicationContext(), footerAd.getFileId());
            }
        }
    }

    public  void setWinGaBanner(final Context context, final long fileId) {
//        String filePath = Constants.GET_ADD_BANNER_URL;
        String filePath = Constants.GET_FOOTER_IMAGE_URL;
        if (filePath.contains("{footerAddId}")) {
            filePath = filePath.replace("{footerAddId}", fileId + "");
        }
        Logg.d(TAG, "** Banner >> " + filePath);
        Glide.with(context)
                .asBitmap()
                .load(filePath)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            // isFirstTime = false;
                            //  bannerIv.setImageBitmap(resource);
                            Bitmap bmp2 = resource.copy(resource.getConfig(), true);
                            WingaApplication.getInstance().setAdBannerBitmap(fileId);

                            Intent intent = new Intent();
                            intent.setAction(Constants.REFRESH_BANNER_ACTION);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }

                });
    }


    @Override
    public void onVolleyErrorListener(Object object) {
        if (object != null) {
            if (object instanceof String) {
                Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        }
}
