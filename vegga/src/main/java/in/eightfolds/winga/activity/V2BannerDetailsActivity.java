package in.eightfolds.winga.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import in.eightfolds.winga.v2.model.BannerResponse;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

public class V2BannerDetailsActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack {

    BannerResponse bannerResponse;
    TextView discountPriceTv, actualPriceTv, discount, bannerTitle, description, redeemCode, copy, termsDescTv, codeTv, termsConditionsLL,title;
    Button gameTitle;
    int selectedImage0 = 0;
    ImageView backIv,image;
    private BannersAdapter bannersAdapter;

    private SliderView sliderView;

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof String) {

            bannerResponse.getImages().get(selectedImage0).setImageSHowUrl((String) object);
            selectedImage0++;
            if (selectedImage0 != bannerResponse.getImages().size()) {

                for (int i = selectedImage0; i < bannerResponse.getImages().size(); i++) {
                    if (bannerResponse.getImages().get(i).getImageId() != null && bannerResponse.getImages().get(i).getImageId() != 0) {
                        getImageUrl(bannerResponse.getImages().get(selectedImage0));
                        break;
                    } else {
                        selectedImage0 = i;
                    }
                }
            } else {
                setAdapter(bannerResponse.getImages());
            }


        }
        if (object instanceof CommonServerResponse && !requestType.contains(WingaConstants.GET_REDIRECT_URL)) {
            CommonServerResponse commonServerResponse = (CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());

            if (TextUtils.isEmpty(bannerResponse.getRedirectUrl())) {
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
                sliderView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                setImageView(banners);
            }else{

                sliderView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                bannersAdapter = new BannersAdapter(banners, getApplicationContext(), null, 1);
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
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.copy) {
            ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("couponCode", bannerResponse.getCouponCode());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(this, getString(R.string.code_copied));
        }
        if (v.getId() == R.id.gameTitle) {

            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                if(!TextUtils.isEmpty(bannerResponse.getCouponCode())){
                    ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("couponCode", bannerResponse.getCouponCode());
                    clipboardManager.setPrimaryClip(clipData);
                    MyDialog.showToast(this, getString(R.string.code_copied));
                }

                sendRedirectUrl(bannerResponse);
                Utils.openInBrowser(this, bannerResponse.getRedirectUrl());
//                Intent intent = new Intent(this, WebBrowserActivity.class);
//                intent.putExtra(Constants.DATA, bannerResponse.getRedirectUrl());
//                intent.putExtra(Constants.TITLE, bannerResponse.getTitle());
//                startActivity(intent);
            } else {
                goToNoInternetActivity();
            }
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_banner_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                bannerResponse = (BannerResponse) bundle.get(Constants.DATA);
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
        image=findViewById(R.id.image);
        termsConditionsLL = findViewById(R.id.termsConditionsLL);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title=findViewById(R.id.title);
        title.setText("Details");
        if (bannerResponse.getTitle() != null) {
            bannerTitle.setText(bannerResponse.getTitle());
        } else {
            bannerTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(bannerResponse.getDescription())) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(bannerResponse.getDescription().trim(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(bannerResponse.getDescription().trim()));
            }

        } else {
            description.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(bannerResponse.getCouponCode())) {
            redeemCode.setText(bannerResponse.getCouponCode());
        } else {
            redeemCode.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            codeTv.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(bannerResponse.getButtomText())) {
            gameTitle.setText(bannerResponse.getButtomText());
            gameTitle.setOnClickListener(this);
        } else {
            gameTitle.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(bannerResponse.getTermsAndConditions())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                termsDescTv.setText(Html.fromHtml(bannerResponse.getTermsAndConditions(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                termsDescTv.setText(Html.fromHtml(bannerResponse.getTermsAndConditions()));
            }
        } else {
            termsConditionsLL.setVisibility(View.GONE);
            termsDescTv.setVisibility(View.GONE);
        }

        setAdapter(bannerResponse.getImages());
    }

    private void getImageUrl(Banner banner) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_FILE_URL + banner.getImageId();

            EightfoldsVolley.getInstance().showProgress(this);

            EightfoldsVolley.getInstance().makingStringRequestForImage(this, String.class, Request.Method.GET, url);

        }else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }


}
