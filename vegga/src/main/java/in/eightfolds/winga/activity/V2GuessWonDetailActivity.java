package in.eightfolds.winga.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.adapter.BannersAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.GuessAndWinResponse;

public class V2GuessWonDetailActivity extends BaseActivity implements VolleyResultCallBack {

    GuessAndWinResponse guessAndWinResponse;
    TextView discountPriceTv,actualPriceTv,discount,bannerTitle,description,redeemCode,copy,termsDescTv,codeTv,termsConditionsLL;
    Button gameTitle;
    int selectedImagePosition=0;
    ImageView backIv,image;
    private BannersAdapter bannersAdapter;

    private SliderView sliderView;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V2GuessWonDetailActivity.this, V2HomeFeatureActivity.class);
        V2GuessWonDetailActivity.this.startActivity(intent);
        finish();
    }
    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if(object instanceof CommonServerResponse){
            CommonServerResponse commonServerResponse=(CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());

           if(TextUtils.isEmpty(guessAndWinResponse.getRedirectUrl())){
               onBackPressed();
           }


        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    private void setAdapter(ArrayList<Banner> banners) {
        if (banners.size() > 0) {
            if (banners.size() == 1) {
                sliderView.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                setImageView(banners);
            }else{
                sliderView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                bannersAdapter = new BannersAdapter(banners, getApplicationContext(), null, 0);
                sliderView.setSliderAdapter(bannersAdapter);
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            }



        } else {
            sliderView.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
    }

    private void sendRedirectUrl(GuessAndWinResponse guessAndWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_GUESS_REDIRECT_URL + guessAndWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    private void setImageView(ArrayList<Banner> banners) {
        if(banners.get(0).getImageId()!=0 && banners.get(0).getImageId()!=null){
            Glide.with(this)
                    .load(WingaConstants.GET_FILE_URL_IMAGE+banners.get(0).getImageId())

                    .placeholder(R.drawable.voucher_default)
                    .error(R.drawable.voucher_default)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(image);

            ;

        }else if(banners.get(0).getImageUrl()!=null){
            Glide.with(this)
                    .load(banners.get(0).getImageUrl())

                    .placeholder(R.drawable.voucher_default)
                    .error(R.drawable.voucher_default)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(image);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()== R.id.copy){
            ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("couponCode", guessAndWinResponse.getCouponCode());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(this, getString(R.string.code_copied));
        }
        if(v.getId()==R.id.gameTitle){
            if(guessAndWinResponse.getPoints()!=null){
                claimPoints(guessAndWinResponse);
            }
            if(!TextUtils.isEmpty(guessAndWinResponse.getCouponCode())){
                ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("couponCode", guessAndWinResponse.getCouponCode());
                clipboardManager.setPrimaryClip(clipData);
                MyDialog.showToast(this, getString(R.string.code_copied));
            }

            if(!TextUtils.isEmpty(guessAndWinResponse.getRedirectUrl())){
                sendRedirectUrl(guessAndWinResponse);
                callRedirectUrl();
            }

        }
    }

    private void claimPoints(GuessAndWinResponse guessAndWinResponse) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = WingaConstants.CLAIM_GUESS_POINTS + guessAndWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
            callRedirectUrl();
        }else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }

    private void callRedirectUrl() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            Utils.openInBrowser(this, guessAndWinResponse.getRedirectUrl());
//            Intent intent = new Intent(this, WebBrowserActivity.class);
//            intent.putExtra(Constants.DATA, guessAndWinResponse.getRedirectUrl());
//            intent.putExtra(Constants.TITLE, guessAndWinResponse.getTitle());
//            startActivity(intent);
        }else {
            goToNoInternetActivity();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_banner_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                guessAndWinResponse = (GuessAndWinResponse) bundle.get(Constants.DATA);
            }
        }

        initialize(true);

    }

    private void initialize(boolean b) {

        discountPriceTv=findViewById(R.id.discountPriceTv);
        actualPriceTv=findViewById(R.id.actualPriceTv);
        discount=findViewById(R.id.discount);
        bannerTitle=findViewById(R.id.bannerTitle);
        description=findViewById(R.id.description);
        redeemCode=findViewById(R.id.redeemCode);
        copy=findViewById(R.id.copy);
        gameTitle=findViewById(R.id.gameTitle);
        sliderView = findViewById(R.id.imageSlider);
        termsDescTv=findViewById(R.id.termsDescTv);
        copy.setOnClickListener(this);
        codeTv=findViewById(R.id.codeTv);
        termsConditionsLL=findViewById(R.id.termsConditionsLL);
        backIv=findViewById(R.id.backIv);
        image=findViewById(R.id.image);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(guessAndWinResponse.getTitle()!=null){
            bannerTitle.setText(guessAndWinResponse.getTitle());
        }else{
            bannerTitle.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(guessAndWinResponse.getDescription())){

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(guessAndWinResponse.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(guessAndWinResponse.getDescription()));
            }

        }else{
            description.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(guessAndWinResponse.getCouponCode())){
            redeemCode.setText(guessAndWinResponse.getCouponCode());
        }else{
            redeemCode.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            codeTv.setVisibility(View.GONE);

        }

        if(!TextUtils.isEmpty(guessAndWinResponse.getButtonText())){
            gameTitle.setText(guessAndWinResponse.getButtonText());
            gameTitle.setOnClickListener(this);
        }else{
            gameTitle.setVisibility(View.GONE);
        }


        if(!TextUtils.isEmpty(guessAndWinResponse.getTermsAndConditions())){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                termsDescTv.setText(Html.fromHtml(guessAndWinResponse.getTermsAndConditions(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                termsDescTv.setText(Html.fromHtml(guessAndWinResponse.getTermsAndConditions()));
            }
        }else{
            termsConditionsLL.setVisibility(View.GONE);
            termsDescTv.setVisibility(View.GONE);
        }

//        for (Banner banner : guessAndWinResponse.getImages()) {
//            if (banner.getImageId() != null && banner.getImageId() != 0) {
//                getImageUrl(guessAndWinResponse.getImages().get(selectedImagePosition));
//                break;
//            } else {
//                selectedImagePosition++;
//            }
//        }


            setAdapter(guessAndWinResponse.getImages());

    }

    private void getImageUrl(Banner banner) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_FILE_URL + banner.getImageId();

            EightfoldsVolley.getInstance().showProgress(this);

            //String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

            EightfoldsVolley.getInstance().makingStringRequestForImage(this, String.class, Request.Method.GET, url);

        }

    }
}
