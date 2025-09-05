package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.v2.adapter.CategoryAdapter;
import in.eightfolds.winga.v2.adapter.TopBrandsAdapter;
import in.eightfolds.winga.v2.model.Category;
import in.eightfolds.winga.v2.model.TopBrands;
import in.eightfolds.winga.v2.model.WingaStore;

public class V2ViewAllActivity extends BaseActivity implements View.OnClickListener, VolleyResultCallBack, OnEventListener {

    private RecyclerView categoryRv;
    private ImageView backIV;
    private TextView categories;
    private CategoryAdapter categoryAdapter;
    private TopBrandsAdapter topBrandsAdapter;
    WingaStore wingaStore;
    private int selectedType;

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {
        if (object instanceof Category) {
            Intent intent = new Intent(V2ViewAllActivity.this, V2CategoryHomeActivity.class);
            intent.putExtra("Selected_id", ((Category) object).getNumber());
            intent.putExtra("title",((Category) object).getName());
            V2ViewAllActivity.this.startActivity(intent);
        }
        if (object instanceof TopBrands) {
            Intent intent = new Intent(V2ViewAllActivity.this, V2CategoryHomeActivity.class);
            intent.putExtra("Selected_id", ((TopBrands) object).getNumber());
            intent.putExtra("title",((TopBrands) object).getName());
            V2ViewAllActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_view_all);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                wingaStore = (WingaStore) bundle.get(Constants.DATA);

            }
            if (bundle.containsKey("selected_category")) {
                selectedType = (int) bundle.get("selected_category");
            }
        }
        initialize(true);

    }

    private void initialize(boolean b) {
        backIV = findViewById(R.id.backIv);
        categoryRv = findViewById(R.id.categoriesRv);
        categories = findViewById(R.id.categories);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(selectedType==1){
            categories.setText("Categories");
        }else{
            categories.setText("Top brands");
        }

        if(selectedType==1){
            setCategoriesAdapter(wingaStore.getCategories());
        }else{
            setTopBrandsAdapter(wingaStore.getTopBrands());
        }


    }

    private void setCategoriesAdapter(ArrayList<Category> categories) {
        if (categories.size() > 0) {
            categoryAdapter = new CategoryAdapter( categories,V2ViewAllActivity.this,this, 1);
            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            categoryRv.setLayoutManager(layoutManager);
            categoryRv.setAdapter(categoryAdapter);

        }

    }

    private void setTopBrandsAdapter(ArrayList<TopBrands> topBrands) {
        if (topBrands.size() > 0) {
            topBrandsAdapter = new TopBrandsAdapter( topBrands,V2ViewAllActivity.this,this, 1);
            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            categoryRv.setLayoutManager(layoutManager);
            categoryRv.setAdapter(topBrandsAdapter);

        }

    }
}
