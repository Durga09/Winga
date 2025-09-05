package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by sp on 09/05/18.
 */

public class FindPhoneNumActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private EditText phoneEt;
    private Button nextBtn;
    private String mobileNumber;
    private boolean toActivate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_phonenum);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
                mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
            }
            if (bundle.containsKey("toActivate")) {
                toActivate = bundle.getBoolean("toActivate");
            }
        }
        initialize();
    }

    private void initialize() {

        phoneEt = findViewById(R.id.phoneEt);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);


        if (!TextUtils.isEmpty(mobileNumber)) {
            phoneEt.setText(mobileNumber);
        }
        EightfoldsUtils.getInstance().readSMSPermission(this);

        if(toActivate){
            phoneEt.setEnabled(false);
            setHeader(getString(R.string.activate_your_mobile_number));
        }else{
            setHeader(getString(R.string.find_your_mobile));
        }


    }

    private void callGenerateOTP(boolean confirm) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String phoneNum = phoneEt.getText().toString();
            if (TextUtils.isEmpty(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_mobile));

            } else if (!EightfoldsUtils.getInstance().isValidMobile(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_valid_phone));

            } else {
                String url = Constants.FORGOT_PASSWORD_GET_OTP_URL.replace("{username}", phoneNum);
                url = url.replace("{confirm}", confirm + "");

                EightfoldsVolley.getInstance().showProgress(this);
                EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.GET, url);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.nextBtn) {
            EightfoldsUtils.getInstance().hideKeyboard(this);
            callGenerateOTP(toActivate);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        CommonResponse response = (CommonResponse) object;

        if (response.getCode() == Constants.SUCCESS) {
            Intent intent = new Intent(this, EnterCodeActivity.class);
            intent.putExtra(Constants.MOBILE_NUMBER, phoneEt.getText().toString());
            intent.putExtra("toActivate", toActivate);
            startActivity(intent);

        } else if (response.getCode() == Constants.DEACTIVATED_CODE) {
            MyDialog.showTwoButtonDialog(this, this, getString(R.string.info), getString(R.string.do_u_want_to_reactivate), getString(R.string.yes), getString(R.string.no));
        } else if (response.getCode() == Constants.DEACTIVATED_CODE_FROM_BACKEND) {
            MyDialog.showCommonDialogWithType(this, this, getString(R.string.info), getString(R.string.your_acnt_deactivated_contact_support), getString(R.string.okay), Constants.DEACTIVATED_CODE_FROM_BACKEND);
        } else {
            onVolleyErrorListener(response.getMessage());
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type == R.id.yesTv) {
            callGenerateOTP(true);
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }
}
