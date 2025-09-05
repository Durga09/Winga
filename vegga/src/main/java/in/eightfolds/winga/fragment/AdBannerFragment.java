package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/

import in.eightfolds.WingaApplication;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.ProfileActivity;
import in.eightfolds.winga.model.FooterAd;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.Utils;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdBannerFragment extends Fragment implements View.OnClickListener {

    private final String TAG = AdBannerFragment.class.getSimpleName();
    public ImageView bannerIv;

    private Activity myContext;
    private View bannerCL;
//    private AdView mAdView;
    Bitmap previousDrawable = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ad_banner, container,
                false);
/*
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/
        bannerIv = rootView.findViewById(R.id.bannerIv);
        bannerCL = rootView.findViewById(R.id.bannerCL);
        ((ImageView) rootView.findViewById(R.id.bannerIv)).setOnClickListener(this);

        if (myContext instanceof HomeActivity || myContext instanceof ProfileActivity) {
            bannerCL.setBackgroundColor(myContext.getResources().getColor(R.color.winga_bg_color));
        }
      //  setImage();
        Logg.d(TAG, "*** Register Banner Refresh receiver .. " + myContext.getClass().getSimpleName());
        // setImage(true);
        LocalBroadcastManager.getInstance(myContext).registerReceiver(bannerRefreshBroadcastReceiver,
                new IntentFilter(Constants.REFRESH_BANNER_ACTION));

        /*mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity activity = myContext;
        Logg.d(TAG, "** UnRegister Banner Refresh receiver .. " + activity.getClass().getSimpleName());

        LocalBroadcastManager.getInstance(myContext).unregisterReceiver(bannerRefreshBroadcastReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = myContext;
    }

    private boolean isFirstTime = true;

    public void setImage() {

        try {



                if (WingaApplication.getInstance().getAdBannerBitmap() != 0) {
                    bannerIv.setVisibility(View.VISIBLE);
//                    String filePath = Constants.GET_FOOTER_IMAGE_URL;
//                    if (filePath.contains("{footerAddId}")) {
//                        filePath = filePath.replace("{footerAddId}", WingaApplication.getInstance().getAdBannerBitmap() + "");
//                    }



                    Glide.with(getApplicationContext())
                             .load(EightfoldsUtils.getInstance().getImageFullPath(WingaApplication.getInstance().getAdBannerBitmap(), Constants.FILE_URL))
                            .placeholder(R.drawable.addspace_default)
                            .error(R.drawable.addspace_default)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(bannerIv);
//                    mAdView.setVisibility(View.GONE);
//                mAdView.setVisibility(View.GONE);
            } else {

//                mAdView.setVisibility(View.VISIBLE);
                bannerIv.setImageDrawable(getResources().getDrawable(R.drawable.addspace_default));
            }


          /*   if (isFirstTime) {

            }

            if(!isFromResume) {

            }else{
                if(WingaApplication.getAdBannerBitmap() != null){
                    bannerIv.setImageBitmap(WingaApplication.getAdBannerBitmap());
                }else {
                    bannerIv.setImageDrawable(getResources().getDrawable(R.drawable.addspace_default));
                }
            }*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final BroadcastReceiver bannerRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Constants.REFRESH_BANNER_ACTION)) {
                Activity activity = myContext;

                Logg.e(TAG, "** Banner refresh broad cast received : " + activity.getClass().getSimpleName());
                bannerIv.setVisibility(View.GONE);
                setImage();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bannerIv) {
            FooterAd footerAd = WingaApplication.getInstance().getFooterAd();
            if (footerAd != null && footerAd.getFooterAddId() > 0 && footerAd.getRedirectLink() != null) {
                LoginData loginData = (LoginData) EightfoldsUtils.getInstance().getObjectFromSharedPreference(getContext(), Constants.LOGIN_DATA, LoginData.class);
                if (loginData != null) {
                    String url = Constants.BUY_FOOTER_CONTENT_URL;
                    url = url.replace("{userId}", String.valueOf(loginData.getUserId()));
                    url = url.replace("{footerAddId}", String.valueOf(footerAd.getFooterAddId()));
                    Utils.openInBrowser(getContext(), url);
                }
            }
        }
    }
}
