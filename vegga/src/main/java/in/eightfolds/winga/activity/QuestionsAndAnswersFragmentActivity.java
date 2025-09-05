package in.eightfolds.winga.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import java.util.List;

import in.eightfolds.winga.R;
import in.eightfolds.winga.fragment.FormFillingFragment;
import in.eightfolds.winga.fragment.QuestionsAndAnswersFragment;
import in.eightfolds.winga.fragment.WatchVideoFragment;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 18-Jun-18.
 */
public class QuestionsAndAnswersFragmentActivity extends BaseActivity implements OnEventListener {
    private static final String TAG = QuestionsAndAnswersFragmentActivity.class.getSimpleName();
    private NewGameResponse newGameResponse;
    private boolean firstgame;
    private boolean mIsInBackAnimation = false;
    QuestionsAndAnswersFragment questionsAndAnswersFragment;
    FormFillingFragment formFillingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fragment);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newGameResponse = (NewGameResponse) bundle.get(Constants.DATA);
            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }


        if (savedInstanceState == null) {
            if(newGameResponse.getForm()==null){
                questionsAndAnswersFragment = new QuestionsAndAnswersFragment();
                questionsAndAnswersFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, questionsAndAnswersFragment)
                        .commit();

            }else{
                formFillingFragment = new FormFillingFragment();
                formFillingFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, formFillingFragment)
                        .commit();

            }
                     }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.v(TAG, "&&onResume() ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Logg.v(TAG, "&&onPause() ");
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsInBackAnimation) return;
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            //fragments.size() is not correct.
            final int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments != null && fragmentCount > 0) {
                Fragment lastFragment = fragments.get(fragmentCount );
                //SupportRequestManagerFragment{5859e15 #1 com.bumptech.glide.manager}{parent=null}
               // if(lastFragment instanceof  SupportRequestManagerFragment){
                   lastFragment =  fragments.get(fragments.size()-1 );
               // }
                if (lastFragment != null && lastFragment instanceof WatchVideoFragment) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait

                    ((WatchVideoFragment) lastFragment).onBackPressed(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                            mIsInBackAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            getSupportFragmentManager().popBackStackImmediate();
                            mIsInBackAnimation = false;
                            onVideoHidden();
                        }
                    });
                    return;
                }
            }
            showGotoHomePopUp();
            // super.onBackPressed();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showGotoHomePopUp() {
        if (firstgame) {
            MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
        } else {
            MyDialog.logoutDialog(this, this, getString(R.string.alert), getString(R.string.do_u_want_go_home), false);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            if (firstgame) {
                finish();
            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    public void onVideoHidden() {
        String fragmentName =  getSupportFragmentManager().findFragmentById(R.id.container).getClass().getSimpleName();
        Logg.v(TAG,"**fragmentName >> "+fragmentName);
        if(fragmentName.equalsIgnoreCase(QuestionsAndAnswersFragment.class.getSimpleName())){
            QuestionsAndAnswersFragment f = (QuestionsAndAnswersFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            f.setQuestionDurationDetails();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // Utils.setAppLanguage(this);
       /* if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){



            if(questionsAndAnswersFragment != null){
                questionsAndAnswersFragment.setImagePropertiesInLandscapeMode();
            }
        }

        else{

            if(questionsAndAnswersFragment != null){
                questionsAndAnswersFragment.setImagePropertiesInPortraitMode();
            }
        }*/

    }
}
