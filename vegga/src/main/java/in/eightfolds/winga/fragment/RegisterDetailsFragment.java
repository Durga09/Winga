package in.eightfolds.winga.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.eightfolds.WingaApplication;
import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.activity.RegistrationSuccessActivity;
import in.eightfolds.winga.activity.V2HomeFeatureActivity;
import in.eightfolds.winga.activity.WebBrowserActivity;
import in.eightfolds.winga.activity.WingaNotLiveActivity;
import in.eightfolds.winga.interfaces.ListenFromActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AgeRange;
import in.eightfolds.winga.model.Country;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, VolleyResultCallBack, LocationListener, ListenFromActivity {

    private static final String TAG = RegisterDetailsFragment.class.getSimpleName();
    private Button registerBtn;
    private ArrayList<Country> countries;
    private Country selectedCountry;
    private Country indiaDetails;
    private LinearLayout otherCountryLL;
    private String mobileNumber;
    private EditText firstNameET, pinCodeET, referralCodeET, cityET; //emailET,
    private Spinner countrySpinner; //ageRangeSpinner,
    private RadioButton indiaRB, otherCountryRB;
    private ImageView citySearchIv,ageRangeSpinnerDown,genderSpinnerDown;
    private LoginData registerRequest;
    private CheckBox acceptAlertsCB, acceptTermsCB;
    private TextView alertsTv, termsTv;
    private boolean isSocialLogin = false;
    private String otpToken;
    private Activity myContext;
    private LocationManager locationManager;
    private String provider;
    private String countryCode;



    private Spinner ageRangeSpinner, genderSpinner;
    private ArrayList<AgeRange> ageRanges;
    private ArrayList<AgeRange> genders;
    private AgeRange selectedAgeRange, selectedGender;
    private boolean isSpinnerTouched = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        countryCode = Constants.DEFAULT_COUNTRYCODE;
        myContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_details, container, false);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
            if (bundle.containsKey("otpToken")) {
                otpToken = bundle.getString("otpToken");
            }
        }
        if (!Utils.isTablet(myContext)) {
            ((LoginActivity) myContext).setLoginFrameBelowLogin();
        }
        ((LoginActivity) myContext).setActivityListener(this);
      //  displayLocationSettingsRequest(myContext);

//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), Constants.GOOGLE_PLACES_API);
//        }
        EightfoldsUtils.getInstance().hideKeyboard(getActivity());

//// Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(getActivity());


        initialize(view);


        return view;
    }


    public boolean giveMapPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, EightfoldsUtils.PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    private boolean locationEnabled;

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        try {
            if (!giveMapPermission(myContext)) {
                return null;
            }
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
            }
            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                // Do something with the recent location fix
                //  otherwise wait for the update below
                onLocationChanged(location);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            }
//            List<String> providers = locationManager.getProviders(true);
//            Location bestLocation = null;
//            for (String provider : providers) {
//                @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
//                if (l == null) {
//
//                    continue;
//                }
//                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                    bestLocation = l;
//                }
//            }
//            if (bestLocation != null) {
//                onLocationChanged(bestLocation);
//
//
//            } else {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
////                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myContext);
////                mFusedLocationClient.getLastLocation()
////                        .addOnSuccessListener(new OnSuccessListener<Location>() {
////                            @Override
////                            public void onSuccess(Location location) {
////                                if (location != null) {
////                                    onLocationChanged(location);
////                                } else {
////                                    getLastKnownLocation();
////                                    cityET.setEnabled(true);
////                                    pinCodeET.setEnabled(true);
////                                 //   MyDialog.showToast(myContext, myContext.getString(R.string.unable_to_get_location));
////
////
////                                }
////                            }
////                        })
////                        .addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                getLastKnownLocation();
////                                Logg.d(TAG, "Error trying to get last GPS location");
////                                MyDialog.showToast(myContext, myContext.getString(R.string.unable_to_get_location));
////                                e.printStackTrace();
////                            }
////                        });
//            }
            return location;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            double lat = (location.getLatitude());
            double lng = (location.getLongitude());
            Logg.v(TAG, "lat " + lat + " lng " + lng);
           locationManager.removeUpdates(this);

            Geocoder geoCoder = new Geocoder(myContext, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }

                if (address.size() > 0) {
                    String pincode = address.get(0).getPostalCode();
                    String fnialAddress = builder.toString(); //This is the complete address.
                    String city = address.get(0).getLocality();

                    countryCode = address.get(0).getCountryCode();

                    Logg.v(TAG, "pincode >> " + pincode + " Final address >> " + fnialAddress + " countryCode >> " + countryCode);


                    setCountryCodeValidations();

                    if (!TextUtils.isEmpty(pincode)) {
                        pinCodeET.setText(pincode);


                    }

                    if (!TextUtils.isEmpty(city)) {
                        cityET.setText(city);

                    }
                }

            } catch (IOException e) {
                // Handle IOException
                e.printStackTrace();
            } catch (NullPointerException e) {
                // Handle NullPointerException
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void showGooglePlaces() {
        int AUTOCOMPLETE_REQUEST_CODE = 2;

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.ADDRESS_COMPONENTS,Place.Field.PLUS_CODE,Place.Field.RATING);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).setCountry("IN")
                .build(getActivity());
        startActivityForResult(intent, 100);
    }

    private void setCountryCodeValidations() {
        if (countryCode.equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE)) {
            //Pincode validations should be there. 6 digit numeric
            pinCodeET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6), new InputFilter.AllCaps()});
            pinCodeET.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else {

            // 0-15 alpha numeric.

            pinCodeET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15), new InputFilter.AllCaps()});
            pinCodeET.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(RegisterDetailsFragment.class.getSimpleName());
    }

    private void initialize(View view) {

        registerBtn = view.findViewById(R.id.registerBtn);
        otherCountryLL = view.findViewById(R.id.otherCountryLL);
        firstNameET = view.findViewById(R.id.firstNameET);
        referralCodeET = view.findViewById(R.id.referralCodeET);

        cityET = view.findViewById(R.id.cityET);
        pinCodeET = view.findViewById(R.id.pinCodeET);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        indiaRB = view.findViewById(R.id.indiaRB);
        otherCountryRB = view.findViewById(R.id.otherCountryRB);
        citySearchIv = view.findViewById(R.id.citySearchIv);

        acceptTermsCB = view.findViewById(R.id.acceptTermsCB);
        alertsTv = view.findViewById(R.id.alertsTv);
        termsTv = view.findViewById(R.id.termsTv);
        acceptAlertsCB = view.findViewById(R.id.acceptAlertsCB);

        ageRangeSpinner = view.findViewById(R.id.ageRangeSpinner);

        genderSpinner = view.findViewById(R.id.genderSpinner);
        ageRangeSpinnerDown=view.findViewById(R.id.ageRangeSpinnerDown);
        genderSpinnerDown=view.findViewById(R.id.genderSpinnerDown);

       getLastKnownLocation();
        setTermsAndConditionsSpan();
        setPrivacyPolicySpan();
        setAlertsSpan();
        getSetup()
        ;


        setCountryCodeValidations();
        String invitedByRefCode = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.INVITED_BY_REF_CODE);
        if (!TextUtils.isEmpty(invitedByRefCode)) {
            referralCodeET.setText(invitedByRefCode);
        }

        registerRequest = new LoginData();

        String loginType = EightfoldsUtils.getInstance().getFromSharedPreference(myContext, Constants.LOGIN_TYPE);
        if (loginType != null && !TextUtils.isEmpty(loginType) && (loginType.equalsIgnoreCase(Constants.GOOGLE_LOGIN) || loginType.equalsIgnoreCase(Constants.FB_LOGIN))) {
            isSocialLogin = true;
            LoginData loginData = WingaApplication.getInstance().getLoginData();
            if (loginData != null && !TextUtils.isEmpty(loginData.getPassword())) {
                if (!TextUtils.isEmpty(loginData.getName())) {
                    firstNameET.setText(loginData.getName());
                }
                if (!TextUtils.isEmpty(loginData.getEmail())) {
                    registerRequest.setEmail(loginData.getEmail());
                }
                if (!TextUtils.isEmpty(loginData.getSocialAccessToken())) {
                    registerRequest.setSocialAccessToken(loginData.getSocialAccessToken());
                }
                if (!TextUtils.isEmpty(loginData.getPassword())) {
                    registerRequest.setPassword(loginData.getPassword());
                }
                if (!TextUtils.isEmpty(loginData.getSocialProvider())) {
                    registerRequest.setSocialProvider(loginData.getSocialProvider());
                }
                if (!TextUtils.isEmpty(loginData.getSocialProviderUserId())) {
                    registerRequest.setSocialProviderUserId(loginData.getSocialProviderUserId());
                }
            }
            registerBtn.setText(getString(R.string.register));
        } else {
            registerBtn.setText(getString(R.string.next));
        }

        registerBtn.setOnClickListener(this);
        cityET.setOnClickListener(this);


        indiaRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    countrySpinner.setSelection(0);
                }
            }
        });

        otherCountryLL.setOnClickListener(this);
        citySearchIv.setOnClickListener(this);
        ageRangeSpinner.setOnItemSelectedListener(this);
        countrySpinner.setOnItemSelectedListener(this);
        genderSpinner.setOnItemSelectedListener(this);
        ageRangeSpinnerDown.setOnClickListener(this);
        genderSpinnerDown.setOnClickListener(this);
        //setAgeSpinner(null);
        setCountrySpinner(null);

        firstNameET.requestFocus();

//        InputMethodManager inputMethodManager = (InputMethodManager) myContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (inputMethodManager != null) {
//            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        }

    }

    public void onAction(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
            switch (requestCode) {
                case 0x1:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            // All required changes were successfully made
                            final Handler handler = new Handler();

                            //  startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Do something after 5s = 5000ms
                            getLastKnownLocation();
//                            }
//                        }, 5000);



                            break;
                        case Activity.RESULT_CANCELED:
                            // The user was asked to change settings, but chose not to
                            //  displayLocationSettingsRequest(myContext);

                            break;
                        default:
                            break;
                    }
                    break;
            }
        } else {
            if (resultCode != 0) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                onLocationChanged(location);
            }

        }

    }

    private void displayLocationSettingsRequest(Context context) {
        Handler mHandler = new Handler();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        getLastKnownLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        try {


                            status.startResolutionForResult(getActivity(), 0x1);


                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public void setAgeSpinner(List<AgeRange> ageRanges) {
        int selection = 0;
        ArrayAdapter<String> ageRangeAdapter = new ArrayAdapter<String>(myContext, R.layout.spinner_item);
        ageRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRangeSpinner.setAdapter(ageRangeAdapter);
        if (ageRanges != null && !ageRanges.isEmpty()) {
            for (int i = 0; i < ageRanges.size(); i++) {
                AgeRange ageRange = ageRanges.get(i);
                ageRangeAdapter.add(ageRange.getTitle());
            }
        }
        ageRangeSpinner.setSelection(selection);
        ageRangeSpinner.setPrompt(getString(R.string.age_range));
    }

    public void setGenderSpinner(List<AgeRange> genders) {
        int selection = 0;
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(myContext, R.layout.spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        if (genders != null && !genders.isEmpty()) {
            for (int i = 0; i < genders.size(); i++) {
                AgeRange gender = genders.get(i);
                genderAdapter.add(gender.getTitle());
            }
        }
        genderSpinner.setSelection(selection);
        genderSpinner.setPrompt(getString(R.string.gender));
    }

    public void setCountrySpinner(List<Country> countries) {
        int selection = 0;
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        countryAdapter.add(myContext.getString(R.string.other));
        countrySpinner.setAdapter(countryAdapter);
        if (countries != null && !countries.isEmpty()) {
            for (int i = 0; i < countries.size(); i++) {
                Country country = countries.get(i);
                countryAdapter.add(country.getName());

            }
        }
        countrySpinner.setSelection(selection);

    }

    private void registerUser() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            String name = firstNameET.getText().toString().trim();
            // String email = emailET.getText().toString();
            String city = cityET.getText().toString().trim();
            String pincode = pinCodeET.getText().toString().trim();
            String referralCode = referralCodeET.getText().toString().trim();


            if (TextUtils.isEmpty(name)) {
                onVolleyErrorListener(getString(R.string.enter_name));
            } else if (!EightfoldsUtils.isAlphabetsAndSpaceOnly(name)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_name));
            } else if (name.length() < 3 || name.length() > 15) {
                onVolleyErrorListener("Please enter a valid name");
            }

            /*else if (TextUtils.isEmpty(email) || !EightfoldsUtils.getInstance().isValidEmail(email)) {
                onVolleyErrorListener(getString(R.string.enter_valid_email));
            } */


            else if (TextUtils.isEmpty(city)) {
                onVolleyErrorListener(getString(R.string.enter_valid_city));
            } else if (city.length() < 3) {
                onVolleyErrorListener("Please enter a valid city");
            } else if (!EightfoldsUtils.isAlphabetsAndSpaceOnly(city)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_city));
            } else if (EightfoldsUtils.isAllZeros(pincode)) {
                onVolleyErrorListener(getString(R.string.pincode_not_accept_zeros));
            } else if (countryCode.equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE) && (TextUtils.isEmpty(pincode) || pincode.length() != 6)) {
                onVolleyErrorListener(getString(R.string.enter_valid_pincode));
            } else if (!countryCode.equalsIgnoreCase(Constants.DEFAULT_COUNTRYCODE) && (TextUtils.isEmpty(pincode) || !EightfoldsUtils.isValidPostalCode(pincode))) {
                onVolleyErrorListener(getString(R.string.enter_valid_pincode));
            } else if (EightfoldsUtils.isAllZeros(pincode)) {
                onVolleyErrorListener(getString(R.string.pincode_not_accept_zeros));
            } else if (selectedAgeRange == null) {
                onVolleyErrorListener(getString(R.string.select_age_range));
            } else if (selectedGender == null) {
                onVolleyErrorListener(getString(R.string.select_gender));
            } else if (!TextUtils.isEmpty(referralCode) && !EightfoldsUtils.isAlphaNumeric(referralCode)) {
                onVolleyErrorListener(getString(R.string.no_special_characters_referralcode));
            } else if (!acceptTermsCB.isChecked()) {
                onVolleyErrorListener(getString(R.string.please_accept_terms));
            } else {
                registerRequest.setMobile(mobileNumber);
                registerRequest.setName(firstNameET.getText().toString());
                //registerRequest.setEmail(""); //emailET.getText().toString()
                String imei = EightfoldsUtils.getInstance().getDeviceIMEI(myContext);
                registerRequest.setImei(imei);
                registerRequest.setCity(cityET.getText().toString());
                registerRequest.setPincode(pinCodeET.getText().toString());
                if (!TextUtils.isEmpty(referralCode)) {
                    registerRequest.setReferralCode(referralCode);
                } else {
                    registerRequest.setReferralCode(null);
                }

                if (acceptAlertsCB.isChecked()) {
                    registerRequest.setNotification(true);
                    registerRequest.setUpdates(true);
                    registerRequest.setAlert(true);
                    registerRequest.setPromotion(true);
                }

                if (!TextUtils.isEmpty(countryCode)) {
                    registerRequest.setCountryCode(countryCode);
                }

                registerRequest.setAgeRangeId(selectedAgeRange.getAgeRangeId());
                registerRequest.setGender(selectedGender.getAgeRangeId());
                if (indiaRB.isChecked() && indiaDetails != null) {
                    registerRequest.setCountryId((long) indiaDetails.getId());
                } else {
                    registerRequest.setCountryId((long) selectedCountry.getId());
                }
                registerRequest.setUsername(mobileNumber);
                UserDeviceDetail userDeviceDetail = EightfoldsUtils.buildUserDeviceDetail(getContext());
                String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.FIRE_BASE_TOKEN);
                userDeviceDetail.setPushRegId(refreshedToken);
//            userDeviceDetail.setUserId(loginData.getUserId());
                userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(myContext));
                registerRequest.setUserDeviceDetail(userDeviceDetail);


                if (isSocialLogin) {

                    EightfoldsVolley.getInstance().showProgress(myContext);
                    String url = Constants.REGISTRATION_URL;
                    url = url.replace("{otpToken}", otpToken);
                    url = url.replace("{langId}", Utils.getCurrentLangId());
                    EightfoldsVolley.getInstance().makingJsonRequest(RegisterDetailsFragment.this, LoginData.class, Request.Method.POST, url, registerRequest);

                } else {
                    RegisterDetailsPasswordFragment registerDetailsFragment = new RegisterDetailsPasswordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.DATA, registerRequest);
                    bundle.putSerializable("otpToken", otpToken);
                    registerDetailsFragment.setArguments(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFL, registerDetailsFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        }
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            registerUser();

        } else if (v.getId() == R.id.otherCountryLL) {
            countrySpinner.performClick();
        } else if (v.getId() == R.id.citySearchIv) {
              getLastKnownLocation();
        } else if (v.getId() == R.id.cityET) {
           getLastKnownLocation();
        }
    }

    private void setAlertsSpan() {

        SpannableString alertsSpan = new SpannableString(getString(R.string.alerts_promos_updates));
        alertsSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_red)), 0, alertsSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan iss = new StyleSpan(Typeface.NORMAL);
        alertsSpan.setSpan(iss, 0, alertsSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alertsTv.append(" ");
        alertsTv.append(alertsSpan);
    }

    private void setTermsAndConditionsSpan() {
        SpannableString termsAndConditionsSpan = Utils.makeLinkSpan(getString(R.string.terms_conditions), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, WebBrowserActivity.class);
                intent.putExtra(Constants.DATA, Constants.TERMS_CONDITIONS_URL);
                intent.putExtra(Constants.TITLE, getString(R.string.terms_conditions));
                startActivity(intent);
            }
        });


        termsAndConditionsSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_red)), 0, termsAndConditionsSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.NORMAL);
        termsAndConditionsSpan.setSpan(iss, 0, termsAndConditionsSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.append(" ");
        termsTv.append(termsAndConditionsSpan);
        Utils.makeLinksFocusable(termsTv);
    }

    private void setPrivacyPolicySpan() {
        SpannableString privacyPolicySpan = Utils.makeLinkSpan(getString(R.string.privacy_policy), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, WebBrowserActivity.class);
                intent.putExtra(Constants.DATA, Constants.PRIVACY_POLICY_URL);
                intent.putExtra(Constants.TITLE, getString(R.string.privacy_policy));
                startActivity(intent);
            }
        });
        privacyPolicySpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_red)), 0, privacyPolicySpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.NORMAL);
        privacyPolicySpan.setSpan(iss, 0, privacyPolicySpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.append(" & ");
        termsTv.append(privacyPolicySpan);
        Utils.makeLinksFocusable(termsTv);
    }


    private void getSetup() {
        Setup registerSetUpDetails = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(myContext, Constants.SET_UP_DETAILS, Setup.class);
        if (registerSetUpDetails == null) {

            if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {
                String url = Constants.GET_SETUP;
                url = url.replace("{langId}", Utils.getCurrentLangId());
                EightfoldsVolley.getInstance().showProgress(myContext);
                EightfoldsVolley.getInstance().makingStringRequest(RegisterDetailsFragment.this, Setup.class, Request.Method.GET, url);
            }
        } else {
            countries = new ArrayList<>(registerSetUpDetails.getCountries());
            ageRanges = new ArrayList<>(registerSetUpDetails.getAgeRanges());

            for (Country country : countries) {
                if (country.getSortname().equalsIgnoreCase("IN")) {
                    indiaDetails = country;
                }
            }

            countries.remove(indiaDetails);
            setAgeSpinner(ageRanges);
            setCountrySpinner(countries);

            genders = Utils.getGendersList(myContext);

            setGenderSpinner(genders);

        }
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof Setup) {
            Setup registerSetUpDetails = (Setup) object;
            countries = new ArrayList<>(registerSetUpDetails.getCountries());
            //ageRanges = new ArrayList<>(registerSetUpDetails.getAgeRanges());

            for (Country country : countries) {
                if (country.getSortname().equalsIgnoreCase("IN")) {
                    indiaDetails = country;
                }
            }

            countries.remove(indiaDetails);
            setAgeSpinner(ageRanges);
            setCountrySpinner(countries);


        } else if (object instanceof LoginData) {
            LoginData loginData = (LoginData) object;
            EightfoldsUtils.getInstance().saveToSharedPreference(myContext, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.LOGIN_DATA, loginData);
            EightfoldsUtils.getInstance().saveObjectToSharedPreference(myContext, Constants.USER_DETAILS, loginData.getUser());

            callRegisterSuccessActivity(loginData);

        }
    }

    private void callRegisterSuccessActivity(LoginData loginData) {
        //  Intent intent = new Intent(myContext, RegistrationSuccessActivity.class);

        Intent intent;
//        if (loginData.isGoLive()) {
//            intent = new Intent(myContext, RegistrationSuccessActivity.class);
//        } else {
//            intent = new Intent(myContext, WingaNotLiveActivity.class);
//            if (!TextUtils.isEmpty(loginData.getGoLiveTime()))
//                intent.putExtra(Constants.GOLIVE_DATE, loginData.getGoLiveTime());
//            if (!TextUtils.isEmpty(loginData.getGoLiveMessage()))
//                intent.putExtra(Constants.GOLIVE_MESSAGE, loginData.getGoLiveMessage());
//            if (!TextUtils.isEmpty(loginData.getCurrentTime()))
//                intent.putExtra(Constants.CURRENT_SERVER_TIME, loginData.getCurrentTime());
//        }
        intent = new Intent(myContext, V2HomeFeatureActivity.class);
        myContext.finish();
        startActivity(intent);
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(myContext, object);

        if (object instanceof CommonResponse) {
            CommonResponse response = (CommonResponse) object;
            if (response.getCode() == Constants.OTP_EXPIRED) {


                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.MOBILE_NUMBER, mobileNumber);
                RegisterBaseFrament registerBaseFrament = new RegisterBaseFrament();
                registerBaseFrament.setArguments(bundle);

                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.mainFL, registerBaseFrament);
                fragTransaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
       /* if (adapterView.getChildCount() > 0 && adapterView.getChildAt(0) instanceof TextView) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(position == 0 ? getResources().getColor(R.color.gray_color) : getResources().getColor(R.color.colorBlack));
        }*/

        if (adapterView.getChildCount() > 0 && adapterView.getChildAt(0) instanceof TextView) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
        }
        int i = adapterView.getId();
        if (i == R.id.ageRangeSpinner || i==R.id.ageRangeSpinnerDown) {
            if (isSpinnerTouched) {
                ((TextView) adapterView.getChildAt(0)).setText(ageRanges.get(position).getTitle());

                selectedAgeRange = ageRanges.get(position);
            } else {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.age_range));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_color));
                ageRangeSpinner.setSelected(false);
                ageRangeSpinner.setSelection(-1);
            }

        } else if (i == R.id.genderSpinner || i== R.id.genderSpinnerDown) {
            if (isSpinnerTouched) {
                ((TextView) adapterView.getChildAt(0)).setText(genders.get(position).getTitle());
                selectedGender = genders.get(position);
            } else {
                ((TextView) adapterView.getChildAt(0)).setText(getString(R.string.gender));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_color));
                genderSpinner.setSelected(false);
                genderSpinner.setSelection(-1);
            }

        } else if (i == R.id.countrySpinner) {
            if (position != 0) {
                selectedCountry = countries.get(position - 1);
                indiaRB.setChecked(false);
                otherCountryRB.setChecked(true);
            } else {
                selectedCountry = null;
                indiaRB.setChecked(true);
                otherCountryRB.setChecked(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int accessFineLocation = ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (accessFineLocation == PackageManager.PERMISSION_GRANTED && accessCoarseLocation == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void doSomethingInFragment() {
        isSpinnerTouched = true;
    }

}
