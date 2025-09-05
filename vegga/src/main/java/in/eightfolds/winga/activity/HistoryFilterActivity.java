package in.eightfolds.winga.activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.HistoryFilterItemsRecyclerAdapter;
import in.eightfolds.winga.model.FilterItemModel;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 02-May-18.
 */

public class HistoryFilterActivity extends BaseActivity {

    private Button applyBtn;
    private RecyclerView filterItemsRecyclerView;
    private ArrayList<FilterItemModel> filterItemModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_history);
        initialize();
    }

    private void initialize() {
        setHeaderWithClear(getString(R.string.history));
        filterItemsRecyclerView =  findViewById(R.id.filterItemsRecyclerView);
        applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);
        getFilterItems();
        HistoryFilterItemsRecyclerAdapter adapter = new HistoryFilterItemsRecyclerAdapter(filterItemModels);

        filterItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterItemsRecyclerView.setAdapter(adapter);
        filterItemsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()== R.id.applyBtn){
                FilterItemModel selectedItem = null;
                for (FilterItemModel item: filterItemModels){
                    if(item.isSelected()){
                        selectedItem = item;
                        break;
                    }
                }
                if(selectedItem == null){
                    MyDialog.showToast(this,getString(R.string.select_an_option_to_proceed));
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA,selectedItem);
                    setResult(RESULT_OK);
                    finish();
                }
        }
    }

    private void getFilterItems() {
        filterItemModels = new ArrayList<>();
        FilterItemModel filterItemModel1 = new FilterItemModel();
        filterItemModel1.setItem(getString(R.string.pending));
        filterItemModel1.setState(1);
        filterItemModels.add(filterItemModel1);


        FilterItemModel filterItemModel2 = new FilterItemModel();
        filterItemModel2.setItem(getString(R.string.inprocess));
        filterItemModel1.setState(2);
        filterItemModels.add(filterItemModel2);

        FilterItemModel filterItemModel3 = new FilterItemModel();
        filterItemModel3.setItem(getString(R.string.delivered));
        filterItemModel1.setState(3);
        filterItemModels.add(filterItemModel3);
    }
}
