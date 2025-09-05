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
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import in.eightfolds.winga.v2.model.SpinWinResponse;

public class V2SpinWinVoucherActivity extends BaseActivity implements VolleyResultCallBack {

    SpinWinResponse spinWinResponse;
    TextView discountPriceTv, actualPriceTv, discount, bannerTitle, description, redeemCode, copy, termsDescTv,codeTv,termsConditionsLL;
    Button gameTitle;
    int selectedImagePosition = 0;
    ImageView backIv,image;
    private BannersAdapter bannersAdapter;

    private SliderView sliderView;

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
//        if (object instanceof String) {
//
//            spinWinResponse.getImages().get(selectedImagePosition).setImageSHowUrl((String) object);
//            selectedImagePosition++;
//            if (selectedImagePosition != spinWinResponse.getImages().size()) {
//
//                for (int i = selectedImagePosition; i < spinWinResponse.getImages().size(); i++) {
//                    if (spinWinResponse.getImages().get(i).getImageId() != null && spinWinResponse.getImages().get(i).getImageId() != 0) {
//                        getImageUrl(spinWinResponse.getImages().get(selectedImagePosition));
//                        break;
//                    } else {
//                        selectedImagePosition = i;
//                    }
//                }
//            } else {
//
//            }
//
//
//        }
        if(object instanceof CommonServerResponse){
            CommonServerResponse commonServerResponse=(CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());


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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V2SpinWinVoucherActivity.this, V2HomeFeatureActivity.class);
        V2SpinWinVoucherActivity.this.startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.copy) {
            ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("couponCode", spinWinResponse.getCouponCode());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(this, getString(R.string.code_copied));
        }
        if (v.getId() == R.id.gameTitle) {


            if(!TextUtils.isEmpty(spinWinResponse.getCouponCode())){
                ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("couponCode", spinWinResponse.getCouponCode());
                clipboardManager.setPrimaryClip(clipData);
                MyDialog.showToast(this, getString(R.string.code_copied));
            }
            if(spinWinResponse.getPoints()!=null){
                claimPoints(spinWinResponse);
            }

            if(!TextUtils.isEmpty(spinWinResponse.getRedirectUrl())){
                sendRedirectUrl(spinWinResponse);
                callRedirectUrl();
            }


        }
    }

    private void claimPoints(SpinWinResponse spinWinResponse) {


        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            //sendRedirectUrl(spinWinResponse);
            String url = WingaConstants.CLIAM_POINTS + spinWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
            callRedirectUrl();
        }
    }



    void callRedirectUrl(){
        if(!TextUtils.isEmpty(spinWinResponse.getRedirectUrl())){

            Utils.openInBrowser(this, spinWinResponse.getRedirectUrl());
//            Intent intent = new Intent(this, WebBrowserActivity.class);
//            intent.putExtra(Constants.DATA, spinWinResponse.getRedirectUrl());
//            intent.putExtra(Constants.TITLE, spinWinResponse.getTitle());
//            intent.putExtra("fromWhichPage",2);
//            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_banner_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                spinWinResponse = (SpinWinResponse) bundle.get(Constants.DATA);
            }
        }

        initialize(true);

    }

    private void initialize(boolean b) {

        discountPriceTv = findViewById(R.id.discountPriceTv);
        actualPriceTv = findViewById(R.id.actualPriceTv);
        discount = findViewById(R.id.discount);
        bannerTitle = findViewById(R.id.bannerTitle);
        description = findViewById(R.id.description);
        redeemCode = findViewById(R.id.redeemCode);
        copy = findViewById(R.id.copy);
        gameTitle = findViewById(R.id.gameTitle);
        sliderView = findViewById(R.id.imageSlider);
        termsDescTv = findViewById(R.id.termsDescTv);
        copy.setOnClickListener(this);
        backIv = findViewById(R.id.backIv);
        codeTv=findViewById(R.id.codeTv);
        image=findViewById(R.id.image);
        termsConditionsLL=findViewById(R.id.termsConditionsLL);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (spinWinResponse.getTitle() != null) {
            bannerTitle.setText(spinWinResponse.getTitle());
        }else{
            bannerTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spinWinResponse.getDescription())) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(spinWinResponse.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(spinWinResponse.getDescription()));
            }

        }else{
            description.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(spinWinResponse.getCouponCode())) {
            redeemCode.setText(spinWinResponse.getCouponCode());
        }else{
            redeemCode.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            codeTv.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(spinWinResponse.getButtonText())) {
            gameTitle.setText(spinWinResponse.getButtonText());
            gameTitle.setOnClickListener(this);
        } else {
            gameTitle.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(spinWinResponse.getTermsAndConditions())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                termsDescTv.setText(Html.fromHtml(spinWinResponse.getTermsAndConditions(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                termsDescTv.setText(Html.fromHtml(spinWinResponse.getTermsAndConditions()));
            }
        }else{
            termsConditionsLL.setVisibility(View.GONE);
            termsDescTv.setVisibility(View.GONE);
        }

        setAdapter(spinWinResponse.getImages());

//        for (Banner banner : spinWinResponse.getImages()) {
//            if (banner.getImageId() != null && banner.getImageId() != 0) {
//                getImageUrl(spinWinResponse.getImages().get(selectedImagePosition));
//                break;
//            } else {
//                selectedImagePosition++;
//            }
//        }
    }

    private void getImageUrl(Banner banner) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_FILE_URL + banner.getImageId();

            EightfoldsVolley.getInstance().showProgress(this);

            //String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

            EightfoldsVolley.getInstance().makingStringRequestForImage(this, String.class, Request.Method.GET, url);

        }

    }

    private void sendRedirectUrl(SpinWinResponse spinWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_SPIN_REDIRECT_URL + spinWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

//    private void showBottomSheetDialog() {
//
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
//
//
//
//        bottomSheetDialog.show();
//    }
}
