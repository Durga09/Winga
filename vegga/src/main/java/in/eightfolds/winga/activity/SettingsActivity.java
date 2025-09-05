package in.eightfolds.winga.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.List;

import in.eightfolds.WingaApplication;
import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.State;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class SettingsActivity extends BaseActivity implements OnEventListener, VolleyResultCallBack, AdapterView.OnItemSelectedListener {

    private LinearLayout changePasswordLL, alertPromotionsLL, deactivateAccLL, languagesLL;
    private CheckBox deactivateAccSwitch;
    private User userResponse;
    public static int SETTINGS_CHANGE_REQ_CODE = 200;
    public static int LANGUAGE_CHANGE_REQ_CODE = 500;
    private Spinner preferredStateSpinner;
    private Spinner preferredContentSpinner;
    private List<State> states;
    private List<State> preferredContentTypes;
    private State selectedState;
    private State preferredContentType;
    private int preferredStateId = 0;
    private int preferredContentId = 0;
    private LinearLayout selectPreferredStateLL;
    private TextView preferredStateTv;
    private Configuration configuration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                userResponse = (User) bundle.get(Constants.DATA);
            }
        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.settings));
        changePasswordLL = findViewById(R.id.changePasswordLL);
        alertPromotionsLL = findViewById(R.id.alertPromotionsLL);
        deactivateAccLL = findViewById(R.id.deactivateAccLL);
        selectPreferredStateLL = findViewById(R.id.selectPreferredStateLL);
        preferredStateTv = findViewById(R.id.preferredStateTv);
        languagesLL = findViewById(R.id.languagesLL);
        deactivateAccSwitch = findViewById(R.id.deactivateAccSwitch);
        preferredStateSpinner = findViewById(R.id.preferredStateSpinner);
        preferredContentSpinner = findViewById(R.id.preferredContentSpinner);

        changePasswordLL.setOnClickListener(this);
        alertPromotionsLL.setOnClickListener(this);
        languagesLL.setOnClickListener(this);
        deactivateAccSwitch.setOnClickListener(this);
        deactivateAccLL.setOnClickListener(this);
        preferredStateSpinner.setOnItemSelectedListener(this);
        preferredContentSpinner.setOnItemSelectedListener(this);

        String loginType = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.LOGIN_TYPE);
        if (loginType != null && (loginType.equalsIgnoreCase(Constants.FB_LOGIN) || loginType.equalsIgnoreCase(Constants.GOOGLE_LOGIN))) {
            changePasswordLL.setVisibility(View.GONE);
            findViewById(R.id.changePasswordView).setVisibility(View.GONE);
        }

        setPreferredStatesSpinner();
        setPreferredContentTypesSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CHANGE_REQ_CODE) {
            if (data != null && data.hasExtra(Constants.DATA)) {
                Object object = data.getExtras().get(Constants.DATA);
                if (object instanceof User) {
                    userResponse = (User) data.getExtras().get(Constants.DATA);
                }
            }
        } else if (requestCode == LANGUAGE_CHANGE_REQ_CODE) {
            //setResult(RESULT_OK);
            // finish();

            if (WingaApplication.getInstance().getConfiguration() != null) {
                configuration = WingaApplication.getInstance().getConfiguration();
            } else {
                configuration = Utils.setAppLanguage(this);
            }
            if (configuration != null) {
                onConfigurationChanged(configuration);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        //  setContentView(R.layout.activity_settings);
        //  initialize();
        resetAllTextViews();
        configuration = null;
    }

    private void resetAllTextViews() {
        ((TextView) findViewById(R.id.changePasswordTv)).setText(getResources().getString(R.string.change_password));
        ((TextView) findViewById(R.id.iAcceptTv)).setText(getResources().getString(R.string.i_accept));
        ((TextView) findViewById(R.id.alertsPromsUpdatesTv)).setText(getResources().getString(R.string.alerts_promos_updates));
        ((TextView) findViewById(R.id.deactivateAccTv)).setText(getResources().getString(R.string.deactivate_acct));
        ((TextView) findViewById(R.id.languagesTv)).setText(getResources().getString(R.string.languages));
        ((TextView) findViewById(R.id.preferredStateTitleTv)).setText(getResources().getString(R.string.preferred_state));
        ((TextView) findViewById(R.id.preferredContentTitleTv)).setText(getResources().getString(R.string.preferred_content));
        setHeader(getString(R.string.settings));

    }

    private void handleBack() {

        Intent intent = new Intent();
        intent.putExtra(Constants.DATA, userResponse);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        if (v.getId() == R.id.changePasswordLL) {

            intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }
            else if(v.getId() == R.id.alertPromotionsLL)
                if (userResponse != null) {
                    intent = new Intent(this, AlertsPromosUpdatesActivity.class);
                    intent.putExtra(Constants.DATA, userResponse);
                    startActivityForResult(intent, SETTINGS_CHANGE_REQ_CODE);
                }
                else if(v.getId() ==R.id.deactivateAccLL) {
                /*intent = new Intent(this, DeactivateAccountActivity.class);
                startActivity(intent);*/
                    MyDialog.logoutDialog(this, this, getString(R.string.confirmation), getString(R.string.are_u_sure_to_deactivate), false);
                }
                else if(v.getId() ==R.id.languagesLL) {
                    intent = new Intent(this, LanguageActivity.class);
                    startActivityForResult(intent, LANGUAGE_CHANGE_REQ_CODE);
                }
               else if(v.getId() ==R.id.backIv){
                handleBack();
        }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type ==R.id.yesTv) {
                if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                    EightfoldsVolley.getInstance().showProgress(this);
                    EightfoldsVolley.getInstance().makingStringRequest(this, CommonResponse.class, Request.Method.PUT, Constants.DEACTIVATE_ACCOUNT);
                }
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.SUCCESS) {
                MyDialog.showToast(this, getString(R.string.deactivated_successfully));
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onVolleyErrorListener(commonResponse);
            }
        } else if (object instanceof User) {
            userResponse = (User) object;
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(this, Constants.USER_DETAILS, userResponse);
            if (userResponse.getPreferredStateId() != null) {
                preferredStateId = userResponse.getPreferredStateId().intValue();
            }
            preferredContentId = userResponse.getPreferredContentType();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }


    public void setPreferredContentTypesSpinner() {
        preferredContentTypes = new ArrayList<>();

        State bothContent = new State();
        bothContent.setId(0);
        bothContent.setName(getString(R.string.both));
        preferredContentTypes.add(bothContent);

        State videoContent = new State();
        videoContent.setId(1);
        videoContent.setName(getString(R.string.video));
        preferredContentTypes.add(videoContent);

        State imageContent = new State();
        imageContent.setId(2);
        imageContent.setName(getString(R.string.image));
        preferredContentTypes.add(imageContent);


        int selection = 0;

        ArrayAdapter<String> supportTypesAdapter = new ArrayAdapter<>(this, R.layout.state_spinner_item);
        supportTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferredContentSpinner.setAdapter(supportTypesAdapter);


        preferredContentId= userResponse.getPreferredContentType();


        for (int i = 0; i < preferredContentTypes.size(); i++) {
            State state = preferredContentTypes.get(i);
            if (state.getId() == preferredContentId) {
                selection = i;
            }
            supportTypesAdapter.add(!TextUtils.isEmpty(state.getName()) ? state.getName() : "");
        }

        preferredContentSpinner.setSelection(selection);
    }

    public void setPreferredStatesSpinner() {
        State otherCountry = new State();
        otherCountry.setId(0);
        otherCountry.setName(getString(R.string.other_country));

        states = new ArrayList<>();
        states.add(otherCountry);


        Setup setUp = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
        if (setUp.getStates() != null && setUp.getStates().size() > 0) {
            states.addAll(setUp.getStates());
        }


        int selection = 0;

        ArrayAdapter<String> supportTypesAdapter = new ArrayAdapter<>(this, R.layout.state_spinner_item);
        supportTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferredStateSpinner.setAdapter(supportTypesAdapter);

        if (userResponse.getPreferredStateId() != null) {
            preferredStateId = userResponse.getPreferredStateId().intValue();
        } else if (userResponse.getStateId() != null) {
            preferredStateId = userResponse.getStateId().intValue();
        }

        if (states != null && !states.isEmpty()) {
            for (int i = 0; i < states.size(); i++) {
                State state = states.get(i);
                if (state.getId() == preferredStateId) {
                    selection = i;
                }
                supportTypesAdapter.add(!TextUtils.isEmpty(state.getName()) ? state.getName() : "");
            }
        }
        preferredStateSpinner.setSelection(selection);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.address_text_color));
        int i = adapterView.getId();
        if (i == R.id.preferredStateSpinner) {
            selectedState = states.get(position);
            if (selectedState.getId() != preferredStateId) {
                updatePreferredState(selectedState.getId() + "");
            }
        }else   if (i == R.id.preferredContentSpinner) {
            preferredContentType = preferredContentTypes.get(position);
            if (preferredContentType.getId() != preferredContentId) {
                updatePreferredContentType(preferredContentType.getId() + "");
            }
        }
    }

    private void updatePreferredState(String stateId) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.UPDATE_PREFERRED_STATE;
            url = url.replace("{stateId}", stateId);
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingStringRequest(this, User.class, Request.Method.PUT, url);
        }
    }

    private void updatePreferredContentType(String stateId) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.UPDATE_PREFERRED_CONTENT_TYPE_URL;
            url = url.replace("{type}", stateId);
            url = url.replace("{langId}", Utils.getCurrentLangId());
            EightfoldsVolley.getInstance().makingStringRequest(this, User.class, Request.Method.PUT, url);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
