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
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 01-May-18.
 */

public class ChangePasswordActivity extends BaseActivity implements VolleyResultCallBack {

  private EditText currentPasswordEt, newPasswordEt, retypePasswordEt;
  private Button saveChangesBtn;
  private LoginData loginData;
  private String mobileNumber;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Utils.setAppLanguage(this);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_changepassword);
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {

      if (bundle.containsKey(Constants.DATA)) {
        loginData = (LoginData) bundle.get(Constants.DATA);
      }
      if (bundle.containsKey(Constants.MOBILE_NUMBER)) {
        mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
      }
    }
    initialize();
  }

  private void initialize() {
    setHeader(getString(R.string.change_password));

    currentPasswordEt = findViewById(R.id.currentPasswordEt);
    newPasswordEt = findViewById(R.id.newPasswordEt);
    retypePasswordEt = findViewById(R.id.retypePasswordEt);
    saveChangesBtn = findViewById(R.id.saveChangesBtn);

    saveChangesBtn.setOnClickListener(this);
  }

  private void callChangePasswordForForgotPassword() {
    if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

      String newPassword = newPasswordEt.getText().toString();
      String confirmPassword = retypePasswordEt.getText().toString();

      if (TextUtils.isEmpty(newPassword)) {
        onVolleyErrorListener(getString(R.string.enter_new_password));

      }
      else if (newPassword.length() < Constants.PASSWORD_LENGTH) {
        onVolleyErrorListener(getString(R.string.enter_eight_digit_new_password));
      }
      else if (!EightfoldsUtils.isAlphaNumeric(newPassword)) {
        onVolleyErrorListener(getString(R.string.no_special_characters_new_password));
      }
      else if (TextUtils.isEmpty(confirmPassword)) {
        onVolleyErrorListener(getString(R.string.enter_reenter_password));

      }
      else if (!newPassword.equals(confirmPassword)) {
        onVolleyErrorListener(getString(R.string.passwords_mustbesame));

      }
      else {
        String url = Constants.FORGOT_PASSWORD_CHANGE_URL.replace("{password}", newPassword);
        url = url.replace("{langId}", Utils.getCurrentLangId());

        if (loginData != null && !TextUtils.isEmpty(loginData.getAccessToken())) {
          EightfoldsVolley.getInstance().showProgress(this);

          EightfoldsVolley.getInstance(loginData.getAccessToken()).makingStringRequest(this, LoginData.class, Request.Method.POST, url);
        }
      }
    }
  }

  private void callChangePasswordForLogin() {
    if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

      String currentPassword = currentPasswordEt.getText().toString();
      String newPassword = newPasswordEt.getText().toString();
      String confirmPassword = retypePasswordEt.getText().toString();

      if (TextUtils.isEmpty(currentPassword)) {
        onVolleyErrorListener(getString(R.string.enter_current_password));

      }
      else if (TextUtils.isEmpty(newPassword)) {
        onVolleyErrorListener(getString(R.string.enter_new_password));
      }
      else if (!EightfoldsUtils.isAlphaNumeric(newPassword)) {
        onVolleyErrorListener(getString(R.string.no_special_characters_new_password));
      }
      else if (newPassword.length() < Constants.PASSWORD_LENGTH) {
        onVolleyErrorListener(getString(R.string.enter_eight_digit_new_password));
      }
      else if (currentPassword.equals(newPassword)) {
        onVolleyErrorListener(getString(R.string.current_password_new_password_not_same));
      }
      else if (TextUtils.isEmpty(confirmPassword)) {
        onVolleyErrorListener(getString(R.string.enter_reenter_password));

      }
      else if (!newPassword.equals(confirmPassword)) {
        onVolleyErrorListener(getString(R.string.passwords_mustbesame));

      }
      else {
        String url = Constants.CHANGE_PASSWORD_URL.replace("{password}", newPassword);
        url = url.replace("{oldPassword}", currentPassword);
        url = url.replace("{langId}", Utils.getCurrentLangId());
        EightfoldsVolley.getInstance().showProgress(this);
        EightfoldsVolley.getInstance().makingStringRequest(this, LoginData.class, Request.Method.POST, url);
      }
    }
  }

  @Override
  public void onClick(View v) {
    super.onClick(v);
    EightfoldsUtils.getInstance().hideKeyboard(this);
    if (v.getId()==R.id.saveChangesBtn) {

        callChangePasswordForLogin();


    }
  }

  @Override
  public void onVolleyResultListener(Object object, String requestType) {

    if (object instanceof LoginData) {
      LoginData loginData = (LoginData) object;

      MyDialog.showToast(this, getString(R.string.change_password_success));

      EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.LOGIN_DATA, loginData);
      if (loginData.getUser() != null) EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, loginData.getUser());
      if (loginData.getAccessToken() != null) EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
      Intent intent = new Intent(this, ProfileActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();

    }
    else if (object instanceof CommonResponse) {
      onVolleyErrorListener(object);
    }
  }

  @Override
  public void onVolleyErrorListener(Object object) {
    Utils.handleCommonErrors(this, object);
  }
}
