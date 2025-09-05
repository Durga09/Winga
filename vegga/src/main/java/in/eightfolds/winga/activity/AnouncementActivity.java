package in.eightfolds.winga.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.AnouncementsAdapter;
import in.eightfolds.winga.adapter.NotificationsAdapter;
import in.eightfolds.winga.model.ExclusiveCategory;
import in.eightfolds.winga.model.ExclusiveCategoryItem;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class AnouncementActivity extends BaseActivity  {
    private RecyclerView anouncementRecyclerView;
    private AnouncementsAdapter anouncementsAdapter;
    private ExclusiveCategory exclusiveCategory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_anouncements);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                exclusiveCategory = (ExclusiveCategory) bundle.get(Constants.DATA);
            }


        }
        initialize();
    }

    private void initialize() {
        setHeader(exclusiveCategory.getName());
        findViewById(R.id.backIv).setOnClickListener(this);
        anouncementRecyclerView = findViewById(R.id.anouncementsRecyclerView);
        anouncementRecyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        anouncementRecyclerView.setLayoutManager(layoutManager);

        anouncementsAdapter = new AnouncementsAdapter(AnouncementActivity.this, (ArrayList<ExclusiveCategoryItem>) exclusiveCategory.getItems());

        anouncementRecyclerView.setAdapter(anouncementsAdapter);
    }
}
