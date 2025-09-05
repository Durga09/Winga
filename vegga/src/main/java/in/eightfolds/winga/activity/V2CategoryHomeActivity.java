package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.v2.adapter.BannersAdapter;
import in.eightfolds.winga.v2.adapter.StoreItemAdapter;
import in.eightfolds.winga.v2.adapter.TopBrandsAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;
import in.eightfolds.winga.v2.model.ProductDetailResponse;
import in.eightfolds.winga.v2.model.StoreItem;
import in.eightfolds.winga.v2.model.StoreItemsAndTopBrandsResponse;
import in.eightfolds.winga.v2.model.TopBrands;

public class V2CategoryHomeActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {

    private ArrayList<Banner> banners;
    private RecyclerView productsRv, topBrandRv;
    private TopBrandsAdapter topBrandsAdapter;
    private StoreItemAdapter storeItemAdapter;
    private ImageView backIV;
    private BannersAdapter bannersAdapter;
    private TextView topBrandsTv,titleTv;
    private SliderView sliderView;


    String selectedId;

    String title;


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {
        if (object instanceof TopBrands) {
            Intent intent=new Intent(V2CategoryHomeActivity.this, V2BrandsItemsListActivity.class);
            intent.putExtra("Selected_id",((TopBrands) object).getNumber());
            intent.putExtra("title",((TopBrands) object).getName());
            V2CategoryHomeActivity.this.startActivity(intent);

        }
        if(object instanceof StoreItem){
             callProductDetail(((StoreItem) object).getNumber());
        }
        if (object instanceof Banner) {
            Banner banner = (Banner) object;

            callProductDetail(banner.getNumber());

        }

    }



    private void callProductDetail(String id) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_PRODUCT_DETAIL_WINGA_STORE + id;

            EightfoldsVolley.getInstance().showProgress(this);

            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, ProductDetailResponse.class, Request.Method.GET, url);

        }else {
            goToNoInternetActivity();
        }
    }




    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof StoreItemsAndTopBrandsResponse) {
            banners = ((StoreItemsAndTopBrandsResponse) object).getBannerItems();
            setAdapter(banners);
            setTopBrandAdapter(((StoreItemsAndTopBrandsResponse) object).getTopBrands());
            setProductsAdapter(((StoreItemsAndTopBrandsResponse)object).getStoreItems());
            titleTv.setText(title);

        }
        if(object instanceof ProductDetailResponse){
            Intent intent = new Intent(this, V2ProductDetailActivity.class);
            intent.putExtra(Constants.DATA,(ProductDetailResponse) object);

            startActivity(intent);
        }
    }

    private void setAdapter(ArrayList<Banner> banners) {
        if (banners.size() > 0) {
            sliderView.setVisibility(View.VISIBLE);
            bannersAdapter = new BannersAdapter(banners, getApplicationContext(), this, 0);
            sliderView.setSliderAdapter(bannersAdapter);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);


        }else{
            sliderView.setVisibility(View.GONE);
        }
    }

    private void setProductsAdapter(ArrayList<StoreItem> storeItems) {
        if (storeItems.size() > 0) {
            productsRv.setVisibility(View.VISIBLE);
            storeItemAdapter = new StoreItemAdapter( storeItems,V2CategoryHomeActivity.this,this);
            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            productsRv.setLayoutManager(layoutManager);
            productsRv.setAdapter(storeItemAdapter);

        }else{
            productsRv.setVisibility(View.GONE);
        }

    }

    private void setTopBrandAdapter(ArrayList<TopBrands> topBrands) {
       if(topBrands.size()>0){
           topBrandsTv.setVisibility(View.VISIBLE);
           topBrandRv.setVisibility(View.VISIBLE);
           topBrandsAdapter = new TopBrandsAdapter(topBrands, V2CategoryHomeActivity.this, this, 1);
           topBrandRv.setAdapter(topBrandsAdapter);

       }else {
           topBrandsTv.setVisibility(View.GONE);
           topBrandRv.setVisibility(View.GONE);
       }

    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_category_home);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("Selected_id")) {
                selectedId = (String) bundle.get("Selected_id");
            }
            if(bundle.containsKey("title")){
                title = (String) bundle.get("title");
            }
        }
        initialize(true);

    }

    private void initialize(boolean b) {
        backIV = findViewById(R.id.backIv);
        sliderView = findViewById(R.id.imageSlider);
        topBrandRv = findViewById(R.id.topBrandsRv);
        productsRv=findViewById(R.id.productsRv);
        topBrandsTv=findViewById(R.id.topBrands);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topBrandRv.setLayoutManager(layoutManager);
        titleTv=findViewById(R.id.title);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWingaStoreItem();
    }

    private void getWingaStoreItem() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = WingaConstants.GET_WINGA_STORE_CATEGORY_HOME;
            url=url.replace("{id}", selectedId);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, StoreItemsAndTopBrandsResponse.class, Request.Method.GET, url);
        }
        else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }

}
