package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.WindowManager;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.CodeInvitesPagerAdapter;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class CodesAndInvitesActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private TabLayout activityTabLayout;

    private ViewPager fragmentPager;
    private boolean toInvitesTab;
    private boolean fromNotification;
    private Notification notification;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_codes_invites);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.containsKey("toInvitesTab")){
                toInvitesTab = bundle.getBoolean("toInvitesTab");
            }
            if(bundle.containsKey(Constants.DATA)){
                notification = (Notification) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
            }
        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.refer_earn));

        if(fromNotification && notification.getNotificationId() != null){
            Utils.makeNotificationAsRead(this,notification.getNotificationId());
        }
        activityTabLayout =  findViewById(R.id.activityTabLayout);
        fragmentPager =  findViewById(R.id.fragmentPager);

        findViewById(R.id.backIv).setOnClickListener(this);
        setActivityTabText();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if( v.getId()==R.id.backIv) {
                handleBack();

        }
    }

    private void handleBack(){
        if(fromNotification){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }


    private void setActivityTabText() {

        CodeInvitesPagerAdapter codeInvitesPagerAdapter = new CodeInvitesPagerAdapter(this, getSupportFragmentManager(), 2);
        fragmentPager.setAdapter(codeInvitesPagerAdapter);
        fragmentPager.addOnPageChangeListener(this);
        if(toInvitesTab){
            fragmentPager.setCurrentItem(1);
        }else {
            fragmentPager.setCurrentItem(0);
        }
        setTitle();
        codeInvitesPagerAdapter.notifyDataSetChanged();
        activityTabLayout.setupWithViewPager(fragmentPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    private void setTitle(){

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

