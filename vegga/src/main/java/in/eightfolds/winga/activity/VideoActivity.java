package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.WindowManager;

import in.eightfolds.winga.R;
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
public class VideoActivity extends BaseActivity implements OnEventListener {
    private static final String TAG = VideoActivity.class.getSimpleName();
    private NewGameResponse newGameResponse;
    private boolean firstgame;
    private Dialog exitDialog;
    private WatchVideoFragment watchVideoFragment;

    public static final int YOUTUBE_ERROR_DIALOG_CODE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newGameResponse = (NewGameResponse) bundle.get(Constants.DATA);
            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }


        if (getFragmentManager().findFragmentByTag("video_frag") == null) {
            Logg.d(TAG, this + ": Existing fragment not found.");
            watchVideoFragment = new WatchVideoFragment();
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            watchVideoFragment.setArguments(bundle);
            // fragTransaction.setCustomAnimations(R.anim.slide_in_try, R.anim.slide_out_try);
            fragTransaction.replace(R.id.mainFL, watchVideoFragment, "video_frag");
            fragTransaction.commitAllowingStateLoss();
        } else {
            Logg.d(TAG, this + ": Existing fragment found.");
        }


    }

    @Override
    public void onBackPressed() {
        showGotoHomePopUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
        }
        if (watchVideoFragment != null && watchVideoFragment.YPlayer!=null) {
            watchVideoFragment.pause();
        }
        super.onPause();
    }

    private void showGotoHomePopUp() {
       /* if (firstgame) {
            exitDialog=  MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
        } else {*/
        if (watchVideoFragment != null  && watchVideoFragment.YPlayer!=null) {
            watchVideoFragment.pause();
        }
        exitDialog = MyDialog.logoutDialog(this, this, getString(R.string.alert), getString(R.string.do_u_want_go_home), false);
        exitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (watchVideoFragment != null  && watchVideoFragment.YPlayer!=null) {
                    watchVideoFragment.myPlayYouTube();
                }
            }
        });
        // }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode == YOUTUBE_ERROR_DIALOG_CODE) {
            Logg.v(TAG, "onActivityResult()");
            MyDialog.showToast(this, getString(R.string.youtube_init_error));
        }
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.setAppLanguage(this);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (exitDialog != null && exitDialog.isShowing()) {
                exitDialog.dismiss();
                showGotoHomePopUp();

            }

            if (watchVideoFragment != null) {
                watchVideoFragment.setImagePropertiesInLandscapeMode();
            }
        } else {
            if (exitDialog != null && exitDialog.isShowing()) {
                exitDialog.dismiss();
                showGotoHomePopUp();
            }
            if (watchVideoFragment != null) {
                watchVideoFragment.setImagePropertiesInPortraitMode();
            }
        }

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
        if (type == R.id.noText) {
            if (watchVideoFragment != null) {
                watchVideoFragment.myPlayYouTube();
            }
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }
}
