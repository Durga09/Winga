package in.eightfolds.winga.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.adapter.BannersAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.ProductDetailResponse;

public class V2ProductDetailActivity extends BaseActivity  implements View.OnClickListener, VolleyResultCallBack {

    TextView discountPriceTv, actualPriceTv, discount, bannerTitle, description, redeemCode, copy, termsDescTv, codeTv, termsConditionsLL,wingaCoinsTv;
    Button gameTitle;
    int selectedImagePosition = 0;
    ImageView backIv,image;
    private BannersAdapter bannersAdapter;

    ProductDetailResponse productDetailResponse;

    private SliderView sliderView;
    @Override
    public void onVolleyResultListener(Object object, String requestType) {
      if(object instanceof CommonServerResponse && requestType.contains(WingaConstants.POST_REDEMPTION_URL)){
          Toast.makeText(this,((CommonServerResponse) object).getStatusMessage(),Toast.LENGTH_LONG).show();
          if(((CommonServerResponse) object).status){
              onBackPressed();
          }
      }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.copy) {
            ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("couponCode", productDetailResponse.getCouponCode());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(this, getString(R.string.code_copied));
        }
        if (v.getId() == R.id.gameTitle) {

            if(!TextUtils.isEmpty(productDetailResponse.getCouponCode())){
                ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("couponCode", productDetailResponse.getCouponCode());
                clipboardManager.setPrimaryClip(clipData);
                MyDialog.showToast(this, getString(R.string.code_copied));
            }

            if(productDetailResponse.getStoreItemTypeId().equals(1)){
                if(EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                    sendRedirectUrl(productDetailResponse);
                    Utils.openInBrowser(this, productDetailResponse.getRedirectUrl());
//                    Intent intent = new Intent(this, WebBrowserActivity.class);
//                    intent.putExtra(Constants.DATA, productDetailResponse.getRedirectUrl());
//                    intent.putExtra(Constants.TITLE, productDetailResponse.getTitle());
//                    startActivity(intent);
                }else{
                    goToNoInternetActivity();
                }
            }else{
                callRedemption(productDetailResponse);
            }
            }

    }

    private void sendRedirectUrl(ProductDetailResponse productDetailResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.POST_REDIRECT_FOR_PRODUCT + productDetailResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    private void callRedemption(ProductDetailResponse productDetailResponse) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.POST_REDEMPTION_URL+productDetailResponse.getNumber();
            UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(this);
            String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.FIRE_BASE_TOKEN);
            userDeviceDetail.setPushRegId(refreshedToken);

            userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(this));
            EightfoldsVolley.getInstance().makingJsonRequestUsingJsonObject(this, CommonServerResponse.class, Request.Method.POST, url,userDeviceDetail);
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_banner_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                productDetailResponse = (ProductDetailResponse) bundle.get(Constants.DATA);
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
        codeTv = findViewById(R.id.codeTv);
        termsConditionsLL = findViewById(R.id.termsConditionsLL);
        image=findViewById(R.id.image);
        wingaCoinsTv=findViewById(R.id.wingaCoinsTv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("##,##,###.##");


        if(productDetailResponse.getWingaPoints()!=null){
            wingaCoinsTv.setVisibility(View.VISIBLE);
            wingaCoinsTv.setText("By using "+decimalFormat.format(productDetailResponse.getWingaPoints()).toString()+" WinGa Coins");
        }else{
            wingaCoinsTv.setVisibility(View.GONE);
        }

        if (productDetailResponse.getDiscountPrice() != null) {
            discountPriceTv.setVisibility(View.VISIBLE);
            discountPriceTv.setText("₹" + decimalFormat.format(productDetailResponse.getDiscountPrice()).toString());
        }else{
            discountPriceTv.setVisibility(View.GONE);
        }

        if(productDetailResponse.getActualPrice()!=null){
            actualPriceTv.setVisibility(View.VISIBLE);
            actualPriceTv.setText("₹" +decimalFormat.format(productDetailResponse.getActualPrice()).toString());
            actualPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }else{
            actualPriceTv.setVisibility(View.GONE);
        }

        if(productDetailResponse.getDiscountPercentage()!=null){
            discount.setVisibility(View.VISIBLE);
            discount.setText(productDetailResponse.getDiscountPercentage().toString()+"%");

        }else{
            discount.setVisibility(View.GONE);
        }
        if (productDetailResponse.getTitle() != null) {
            bannerTitle.setText(productDetailResponse.getTitle());
        } else {
            bannerTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(productDetailResponse.getDescription())) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(productDetailResponse.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(productDetailResponse.getDescription()));
            }

        } else {
            description.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(productDetailResponse.getCouponCode())) {
            redeemCode.setText(productDetailResponse.getCouponCode());
        } else {
            redeemCode.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            codeTv.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(productDetailResponse.getButtonText())) {
            gameTitle.setText(productDetailResponse.getButtonText());
            gameTitle.setOnClickListener(this);
        } else {
            gameTitle.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(productDetailResponse.getTermsAndConditions())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                termsDescTv.setText(Html.fromHtml(productDetailResponse.getTermsAndConditions(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                termsDescTv.setText(Html.fromHtml(productDetailResponse.getTermsAndConditions()));
            }
        } else {
            termsConditionsLL.setVisibility(View.GONE);
            termsDescTv.setVisibility(View.GONE);
        }

        setAdapter(productDetailResponse.getImages());
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
                bannersAdapter = new BannersAdapter(banners, getApplicationContext(), null,0);
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

                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(image);

            ;

        }else if(banners.get(0).getImageUrl()!=null){
            Glide.with(this)
                    .load(banners.get(0).getImageUrl())

                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(image);
        }
    }

//    private void showBottomSheetDialog() {
//
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(R.layout.v2_bottom_sheet_dailog);
//        ImageView close = bottomSheetDialog.findViewById(R.id.cross);
//        TextView textView = (TextView) bottomSheetDialog.findViewById(R.id.bottom_text);
//
//        if (productDetailResponse.getSpinTermsAndConditions() != null && !TextUtils.isEmpty(productDetailResponse.getSpinTermsAndConditions())) {
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                textView.setText(Html.fromHtml(spinResponse.getSpinTermsAndConditions().trim(), Html.FROM_HTML_MODE_LEGACY));
//            } else {
//                textView.setText(Html.fromHtml(spinResponse.getSpinTermsAndConditions().trim()));
//            }
//
//
//        }
//        assert close != null;
//        close.setOnClickListener(v -> bottomSheetDialog.cancel());
//
//
//        bottomSheetDialog.show();
//    }


}
