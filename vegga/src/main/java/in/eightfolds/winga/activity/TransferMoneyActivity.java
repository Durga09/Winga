package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

public class TransferMoneyActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack {

    private EditText mobET;
    private Button transferMoneyBtn;
    private boolean firstgame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transfermoney);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }
        initialize();
    }

    private void initialize() {
        mobET = findViewById(R.id.mobET);
        transferMoneyBtn = findViewById(R.id.transferMoneyBtn);
        mobET.setText(Utils.getMobileNumber(this));
        transferMoneyBtn.setOnClickListener(this);
        findViewById(R.id.backIv).setOnClickListener(this);
        findViewById(R.id.doItLater).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        int id = v.getId();

        if (id == R.id.doItLater) {
            getHomePageResponse();
        } else if (id == R.id.transferMoneyBtn) {
            getHomePageResponse();
        } else if (id == R.id.backIv) {
            onBackPressed();
        }

    }

    private void getHomePageResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            Intent intent = new Intent(this, HomeBaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.FROM_SPLASH, true);
                       Utils.setAppLanguage(this);
            this.finish();
            this.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (firstgame) {
            MyDialog.logoutDialog(this, this, getString(R.string.exit), getString(R.string.do_u_want_exit), false);
        } else {
            getHomePageResponse();
        }
    }

    private void goHome(HomePageResponse homePageResponse) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.FROM_SPLASH, true);
        intent.putExtra(Constants.HOME_DATA, homePageResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            finishAffinity();
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof HomePageResponse) {
            goHome((HomePageResponse) object);
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }
}
