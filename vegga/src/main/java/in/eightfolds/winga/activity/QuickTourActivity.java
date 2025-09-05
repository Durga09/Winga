package in.eightfolds.winga.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.QuickTourAdapter;
import in.eightfolds.winga.fragment.QuickTourFragment;
import in.eightfolds.winga.model.QuickTourModel;
import in.eightfolds.winga.utils.ConstantsManager;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.Utils;


public class QuickTourActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static String TAG = QuickTourActivity.class.getSimpleName();
    private ViewPager pager;
    private LinearLayout imagesLL;
    private LayoutInflater inflater;
    private int position = 0;

    private boolean firstgame;
    private TextView skipTv, nextTv;
    private QuickTourAdapter adapter;

    private int fromHomeActivity=0;

    ArrayList<QuickTourModel> quickTourItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        setContentView(R.layout.activity_quick);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame", false);
            }
            if(bundle.containsKey("isFromHomeActivity")){
                fromHomeActivity=bundle.getInt("isFromHomeActivity",0);
            }
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pager = findViewById(R.id.pager);
        imagesLL = findViewById(R.id.imagesLL);
        skipTv = findViewById(R.id.skipTv);
        nextTv = findViewById(R.id.nextTv);
        quickTourItemsList = ConstantsManager.getQuickTourData(this);
        setCirclePageIndicator(0);
        adapter = new QuickTourAdapter(getSupportFragmentManager(), this, quickTourItemsList);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        skipTv.setOnClickListener(this);
        nextTv.setOnClickListener(this);
        pager.setOffscreenPageLimit(0);
    }


    private void proceedNext() {
        if (firstgame) {
            Intent intent = new Intent(this, StartFirstGameActivity.class);
            intent.putExtra("firstgame", firstgame);
            startActivity(intent);
            finish();
        } else {

            if(fromHomeActivity==0) {
                Intent intent = new Intent(this, V2HomeFeatureActivity.class);
                intent.putExtra("firstgame", firstgame);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("firstgame", firstgame);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.nextTv) {
            setNext();
        } else if (view.getId() == R.id.skipTv)
            proceedNext();
    }



    private void setPrevious() {
        if (position != 0) {
            position = position - 1;
        }
        pager.setCurrentItem(position);
    }

    private void setNext() {
        if (position != quickTourItemsList.size() - 1) {
            position = position + 1;
        } else {
            proceedNext();
        }
        pager.setCurrentItem(position);
    }

    private void setCirclePageIndicator(int position) {
        imagesLL.removeAllViews();
        for (int i = 0; i < quickTourItemsList.size(); i++) {
            View view = inflater.inflate(R.layout.add_circle_layout, null);
            ImageView circleImageView = view.findViewById(R.id.circleImageView);
            if (position == i) {
                circleImageView.setImageResource((R.drawable.filled_circle));
            }
            imagesLL.addView(view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        int pos = pager.getCurrentItem();
        Logg.v(TAG, "**onPageSelected Pager >> " + pos);
            Fragment activeFragment = adapter.getItem(pos);
            ((QuickTourFragment) activeFragment).performAnimation();
        setCirclePageIndicator(position);
        if (position == quickTourItemsList.size() - 1) {
            nextTv.setText(getString(R.string.continue_text));
        } else {
            nextTv.setText(getString(R.string.next));
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
