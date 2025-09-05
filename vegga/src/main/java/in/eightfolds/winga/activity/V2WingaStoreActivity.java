package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.v2.adapter.BannersAdapter;
import in.eightfolds.winga.v2.adapter.CategoryAdapter;
import in.eightfolds.winga.v2.adapter.TopBrandsAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;
import in.eightfolds.winga.v2.model.Category;
import in.eightfolds.winga.v2.model.ProductDetailResponse;
import in.eightfolds.winga.v2.model.TopBrands;
import in.eightfolds.winga.v2.model.WingaStore;

public class V2WingaStoreActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {



    private BannersAdapter bannersAdapter;
    private SliderView sliderView;
    private ImageView backIV;
    private TextView categoriesViewAll,topBrandsViewAll,titleTv,topBrandsTv;
    String title,information;
    private RecyclerView categoryRv, topBrandRv;
    private CategoryAdapter categoryAdapter;
    private TopBrandsAdapter topBrandsAdapter;
    private ArrayList<Banner> banners;
    private WingaStore wingaStore;

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {
      if(object instanceof Category){
          Intent intent=new Intent(V2WingaStoreActivity.this, V2CategoryHomeActivity.class);
          intent.putExtra("Selected_id",((Category) object).getNumber());
          intent.putExtra("title",((Category) object).getName());
          V2WingaStoreActivity.this.startActivity(intent);
      }
        if(object instanceof TopBrands){
            Intent intent=new Intent(V2WingaStoreActivity.this, V2CategoryHomeActivity.class);
            intent.putExtra("Selected_id",((TopBrands) object).getNumber());
            intent.putExtra("title",((TopBrands) object).getName());
            V2WingaStoreActivity.this.startActivity(intent);
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

        }

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof WingaStore) {
            wingaStore= (WingaStore) object;
            banners = ((WingaStore) object).getBannerItems();
            setAdapter(banners);
            if(wingaStore.getCategories().size()<=4){
                categoriesViewAll.setVisibility(View.GONE);
            }
            if(wingaStore.getTopBrands().size()<=4){
                topBrandsViewAll.setVisibility(View.GONE);
            }
            setTopBrandAdapter(((WingaStore) object).getTopBrands());
            setCategoryAdapter(((WingaStore) object).getCategories());
        }
        if(object instanceof ProductDetailResponse){
            Intent intent = new Intent(this, V2ProductDetailActivity.class);
            intent.putExtra(Constants.DATA,(ProductDetailResponse) object);

            startActivity(intent);
        }
    }

    private void setTopBrandAdapter(ArrayList<TopBrands> topBrands) {
        if(topBrands.size()!=0){
            topBrandsAdapter = new TopBrandsAdapter(topBrands, V2WingaStoreActivity.this, this, 0);
            topBrandRv.setAdapter(topBrandsAdapter);
        }else{
            topBrandsTv.setVisibility(View.GONE);
        }

    }

    private void setCategoryAdapter(ArrayList<Category> categories) {
        categoryAdapter = new CategoryAdapter(categories, V2WingaStoreActivity.this, this, 0);
        categoryRv.setAdapter(categoryAdapter);
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_winga_store_home);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("title")){
                title= (String) bundle.get("title");
            }
            if(bundle.containsKey("information")){
                information=(String) bundle.get("information");
            }
        }
        initialize(true);

    }

    private void initialize(boolean b) {
        backIV = findViewById(R.id.backIv);
        sliderView = findViewById(R.id.imageSlider);
        topBrandRv = findViewById(R.id.topBrandsRv);
        categoryRv = findViewById(R.id.categoriesRv);
        topBrandsTv= findViewById(R.id.topBrands);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRv.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topBrandRv.setLayoutManager(layoutManager1);
        topBrandsViewAll=findViewById(R.id.topBrandsViewAll);
        categoriesViewAll=findViewById(R.id.categoriesViewAll);
        titleTv=findViewById(R.id.title);
        if(!TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }
        findViewById(R.id.failureReasonsIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.showInformationPopUp(V2WingaStoreActivity.this,information,title);

            }
        });



        categoriesViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(V2WingaStoreActivity.this, V2ViewAllActivity.class);
                intent.putExtra(Constants.DATA,wingaStore);
                intent.putExtra("selected_category",1);
                V2WingaStoreActivity.this.startActivity(intent);
            }
        });

        topBrandsViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(V2WingaStoreActivity.this, V2ViewAllActivity.class);
                intent.putExtra(Constants.DATA,wingaStore);
                intent.putExtra("selected_category",0);
                V2WingaStoreActivity.this.startActivity(intent);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWingaStore();
    }

    private void getWingaStore() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_WINGA_STORE_HOME;
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, WingaStore.class, Request.Method.GET, url);
        }


    }

    private void setAdapter(ArrayList<Banner> banners) {
        if (banners.size() > 0) {
            bannersAdapter = new BannersAdapter(banners, getApplicationContext(), this, 0);
            sliderView.setSliderAdapter(bannersAdapter);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);


        }
    }
}
