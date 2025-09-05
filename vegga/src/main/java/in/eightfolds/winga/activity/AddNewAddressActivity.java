package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AddressType;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.State;
import in.eightfolds.winga.model.UserAddress;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ConstantsManager;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.NDSpinner;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 27-Apr-18.
 */

public class AddNewAddressActivity extends BaseActivity implements VolleyResultCallBack, AdapterView.OnItemSelectedListener {

    private EditText cityEt, localityEt, flatNumEt, pinCodeEt, stateEt, landmarkEt, typeEt;
    private Button saveBtn;
    private UserAddress userAddress;
    private Spinner addressTypeSpinner;
    private NDSpinner statesSpinner;
    private AddressType selectedAddressType;
    private State selectedState;
    ArrayList<AddressType> addressTypes;
    private List<State> states;
    private boolean isForEdit;
    private boolean isSpinnerTouched = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_address);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                userAddress = (UserAddress) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey("isForEdit")) {
                isForEdit = bundle.getBoolean("isForEdit");
            }
        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.address));

        cityEt = findViewById(R.id.cityEt);
        localityEt = findViewById(R.id.localityEt);
        flatNumEt = findViewById(R.id.flatNumEt);
        pinCodeEt = findViewById(R.id.pinCodeEt);
        stateEt = findViewById(R.id.stateEt);
        landmarkEt = findViewById(R.id.landmarkEt);
        typeEt = findViewById(R.id.typeEt);
        saveBtn = findViewById(R.id.saveBtn);
        addressTypeSpinner = findViewById(R.id.addressTypeSpinner);
        statesSpinner = findViewById(R.id.statesSpinner);
        addressTypeSpinner.setOnItemSelectedListener(this);

        saveBtn.setOnClickListener(this);
        findViewById(R.id.statesSpinnerLL).setOnClickListener(this);

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
        addressTypes = ConstantsManager.getAddressTypes(this);
        states = setup.getStates();
        setAddressTypesSpinner(addressTypes);
        setStatesSpinner(states);


        statesSpinner.setOnItemSelectedListener(this);


        if (userAddress != null) {

            cityEt.setText(!TextUtils.isEmpty(userAddress.getCity()) ? userAddress.getCity() : "");
            landmarkEt.setText(!TextUtils.isEmpty(userAddress.getLandmark()) ? userAddress.getLandmark() : "");
            pinCodeEt.setText(!TextUtils.isEmpty(userAddress.getPincode()) ? userAddress.getPincode() : "");
            localityEt.setText(!TextUtils.isEmpty(userAddress.getAddress1()) ? userAddress.getAddress1() : "");
            flatNumEt.setText(!TextUtils.isEmpty(userAddress.getAddress2()) ? userAddress.getAddress2() : "");
        }

        if (isForEdit) {
            saveBtn.setText(getString(R.string.save));
        }

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isSpinnerTouched = true;
    }


    ArrayAdapter<String> addressTypeAdapter;

    public void setAddressTypesSpinner(List<AddressType> addressTypes) {
        int selection = 0;
        long selectedId = 0;
        if (userAddress != null) {
            selectedId = userAddress.getAdreTypeId();
        }
        addressTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        //addressTypeAdapter.add(this.getString(R.string.type));
        addressTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressTypeSpinner.setAdapter(addressTypeAdapter);
        if (addressTypes != null && !addressTypes.isEmpty()) {
            for (int i = 0; i < addressTypes.size(); i++) {
                if (selectedId == addressTypes.get(i).getAdreTypeId()) {
                    selection = i;
                    selectedAddressType = addressTypes.get(i);
                }
                String addressType = addressTypes.get(i).getTitle();
                addressTypeAdapter.add(addressType);
            }
        }
        addressTypeSpinner.setSelection(selection);

    }


    public void setStatesSpinner(List<State> states) {
        int selection = 0;
        long selectedId = 0;
        if (userAddress != null) {
            selectedId = userAddress.getStateId();
        }
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(stateAdapter);
        if (states != null && !states.isEmpty()) {
            for (int i = 0; i < states.size(); i++) {
                if (selectedId == states.get(i).getId()) {
                    selection = i;
                    selectedState = states.get(i);
                }
                String state = states.get(i).getName();
                stateAdapter.add(state);
            }
        }
         statesSpinner.setSelection(selection);
    }

    private void editAddress(String userJson) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.UPDATE_USER_ADDRESS_URL;
            Logg.v("Edit>> ", userJson);
            EightfoldsVolley.getInstance().makingStringRequestWithBody(userJson, this, UserAddress[].class, Request.Method.PUT, url);

        }
    }

    private void addUserAddress(String userJson) {
        EightfoldsVolley.getInstance().makingStringRequestWithBody(userJson, this, UserAddress[].class, Request.Method.POST, Constants.ADD_USER_ADDRESS_URL);
    }

    private void setValidation() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String city = cityEt.getText().toString();
            String locality = localityEt.getText().toString();
            String flatNum = flatNumEt.getText().toString();
            String landmark = landmarkEt.getText().toString();
            String pincode = pinCodeEt.getText().toString();

            if (TextUtils.isEmpty(city)) {
                onVolleyErrorListener(getString(R.string.enter_valid_city));
            } else if (!EightfoldsUtils.isAlphabetsAndSpaceOnly(city)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_city));
            } else if (TextUtils.isEmpty(locality)) {
                onVolleyErrorListener(getString(R.string.enter_valid_locality));
            } else if (TextUtils.isEmpty(flatNum)) {
                onVolleyErrorListener(getString(R.string.enter_valid_flatnum_bul_name));
            } else if (TextUtils.isEmpty(pincode) || pincode.length() != 6) {
                onVolleyErrorListener(getString(R.string.enter_valid_pincode));
            } else if (selectedState == null || selectedState.getId() == 0) {
                onVolleyErrorListener(getString(R.string.select_state));
            } else if (selectedAddressType == null) {
                onVolleyErrorListener(getString(R.string.please_select_address_type));
            } else {
                if (!isForEdit) {
                    userAddress = new UserAddress();
                }
                userAddress.setCity(city);
                userAddress.setLandmark(landmark);
                userAddress.setPincode(pincode);
                userAddress.setStateId((long) selectedState.getId()); //(long) 1;
                userAddress.setAdreTypeId(selectedAddressType.getAdreTypeId());
                userAddress.setAddress1(locality);
                userAddress.setAddress2(flatNum);

                EightfoldsVolley.getInstance().showProgress(this);
                String jsonBody = null;
                try {
                    jsonBody = Api.toJson(userAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (isForEdit) {
                    editAddress(jsonBody);
                } else {
                    addUserAddress(jsonBody);
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.saveBtn) {
            setValidation();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof UserAddress[]) {
            Intent profileRefreshIntent = new Intent();
            profileRefreshIntent.setAction(Constants.PROFILE_REFRESH_ACTION);

            sendBroadcast(profileRefreshIntent);
            UserAddress[] addresses = null;
            try {
                addresses = (UserAddress[]) object;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isForEdit) {
                MyDialog.showToast(this, getString(R.string.address_edited_successfully));
            } else {
                MyDialog.showToast(this, getString(R.string.address_added_successfully));
            }

            Intent intent = new Intent();
            intent.putExtra(Constants.ADDRESS, addresses);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorWhite));

       /* if (adapterView.getChildCount() > 0 && adapterView.getChildAt(0) instanceof TextView) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(position == 0 ? getResources().getColor(R.color.address_hint_text_color) : getResources().getColor(R.color.address_text_color));
            }*/
        int i = adapterView.getId();
        if (i == R.id.addressTypeSpinner) {


            if (!isSpinnerTouched && !isForEdit) {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.type));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.address_hint_text_color));
                addressTypeSpinner.setSelected(false);
                addressTypeSpinner.setSelection(-1);

            } else {

                ((TextView) adapterView.getChildAt(0)).setText(addressTypes.get(position).getTitle());
                selectedAddressType = addressTypes.get(position);
            }

        } else if (i == R.id.statesSpinner) {

            if (!isSpinnerTouched && !isForEdit) {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.state));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.address_hint_text_color));
                statesSpinner.setSelected(false);
                statesSpinner.setSelection(-1);

            } else {

                ((TextView) adapterView.getChildAt(0)).setText(states.get(position).getName());
                selectedState = states.get(position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
