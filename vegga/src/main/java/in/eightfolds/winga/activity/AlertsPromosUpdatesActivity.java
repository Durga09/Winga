package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 04-May-18.
 */

public class AlertsPromosUpdatesActivity extends BaseActivity implements VolleyResultCallBack, CompoundButton.OnCheckedChangeListener {

    private CheckBox updatesCB, promosCB, alertsCB;
    private User userResponse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alerts_promos_updates);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                userResponse = (User) bundle.get(Constants.DATA);
            }
        }
        initialize();


    }

    private void initialize() {
        setHeader(getString(R.string.alerts_promos_updates));
        updatesCB = findViewById(R.id.updatesCB);
        promosCB = findViewById(R.id.promosCB);
        alertsCB = findViewById(R.id.alertsCB);


        updatesCB.setOnCheckedChangeListener(this);
        promosCB.setOnCheckedChangeListener(this);
        alertsCB.setOnCheckedChangeListener(this);

        refreshData();
    }

    private void refreshData() {
        if(userResponse != null) {
            updatesCB.setChecked(userResponse.isUpdates());
            promosCB.setChecked(userResponse.isPromotion());
            alertsCB.setChecked(userResponse.isAlert());
        }
    }

    private void editProfile() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.EDIT_USER_PROFILE_URL;
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingJsonRequest(this, User.class, Request.Method.POST, url, userResponse);

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.backIv) {
            handleBack();
        }
    }

    private void handleBack() {

        Intent intent = new Intent();
        intent.putExtra(Constants.DATA, userResponse);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof User) {
            userResponse = (User) object;
            refreshData();


        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

       if(buttonView.getId()==R.id.updatesCB) {
           if (isChecked != userResponse.isUpdates()) {
               userResponse.setUpdates(isChecked);
               editProfile();
               if (isChecked) {
                   Utils.subscribeToTopic(Constants.TOPIC_NAME_UPDATES);
               } else {
                   Utils.unSubscribeToTopic(Constants.TOPIC_NAME_UPDATES);
               }
           }

       }
            else if(buttonView.getId()==R.id.promosCB)
                if (isChecked != userResponse.isPromotion()) {
                    userResponse.setPromotion(isChecked);
                    editProfile();
                    if(isChecked){
                        Utils.subscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
                    }else{
                        Utils.unSubscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
                    }
                }

            else if(buttonView.getId()==R.id.alertsCB)
                if (isChecked != userResponse.isAlert()) {
                    userResponse.setAlert(isChecked);
                    editProfile();
                    if(isChecked){
                        Utils.subscribeToTopic(Constants.TOPIC_NAME_ALERTS);
                    }else{
                        Utils.unSubscribeToTopic(Constants.TOPIC_NAME_ALERTS);
                    }
                }



    }
}

