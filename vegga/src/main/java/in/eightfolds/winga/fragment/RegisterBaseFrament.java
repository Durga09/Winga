package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class RegisterBaseFrament extends Fragment implements View.OnClickListener, VolleyResultCallBack {

    private Button generateOTPBtn;
    private EditText mobET;
    private LinearLayout mobLL;
    private String mobileNumber;


    private Activity myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_base, container, false);


        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MOBILE_NUMBER))
                mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);

        }
        initialize(view);
        ((LoginActivity) myContext).setLoginFrameInCenter();
        return view;
    }


    private void initialize(View view) {
        generateOTPBtn = view.findViewById(R.id.generateOTPBtn);
        mobLL = view.findViewById(R.id.mobLL);
        mobET = view.findViewById(R.id.mobET);
        if (!TextUtils.isEmpty(mobileNumber)) {
            mobET.setText(mobileNumber);
        }
        generateOTPBtn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        (new UsageAnalytics()).trackPage(RegisterBaseFrament.class.getSimpleName());

       /* ColorDrawable[] color = {new ColorDrawable(getResources().getColor(R.color.trasparant)), new ColorDrawable(getResources().getColor(R.color.app_theme_red))};
        TransitionDrawable trans = new TransitionDrawable(color);
        mobLL.setBackground(trans);
        trans.startTransition(500);*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.generateOTPBtn) {
            EightfoldsUtils.getInstance().readSMSPermission(myContext);
            //replaceVerifyOTPFrag();
           callGenerateOTP();
        }
    }

    private void callGenerateOTP() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(myContext)) {

            String phoneNum = mobET.getText().toString();
            if (TextUtils.isEmpty(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_mobile));

            } else if (!EightfoldsUtils.getInstance().isValidMobile(phoneNum)) {
                onVolleyErrorListener(getString(R.string.enter_valid_phone));

            } else {
                String url = Constants.GENERATE_OTP_URL.replace("{mobile}", phoneNum);
                UserDeviceDetail userDeviceDetail=EightfoldsUtils.buildUserDeviceDetail(getContext());
                String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.FIRE_BASE_TOKEN);
                userDeviceDetail.setPushRegId(refreshedToken);

                userDeviceDetail.setImei(EightfoldsUtils.getInstance().getDeviceId(getContext()));


                EightfoldsVolley.getInstance().showProgress(myContext);
                EightfoldsVolley.getInstance().makingJsonRequest(RegisterBaseFrament.this, CommonResponse.class, Request.Method.POST, url,userDeviceDetail);
                Utils.regSmsReceiver(getActivity());
            }
        }
    }


    private void replaceVerifyOTPFrag() {
        RegisterVerifyOtpFragment registerVerifyOtpFragment = new RegisterVerifyOtpFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MOBILE_NUMBER, mobET.getText().toString());
        registerVerifyOtpFragment.setArguments(bundle);


        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFL, registerVerifyOtpFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    private void replaceLoginFrag() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();

        String mobileNumber = mobET.getText().toString();

        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);

        //LoginFragment loginFragment = new LoginFragment();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.mainFL, loginFragment);
        fragTransaction.commitAllowingStateLoss();


        ((LoginActivity) myContext).highLightLogin();

    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        CommonResponse response = (CommonResponse) object;

        if (response.getCode() == Constants.SUCCESS) {
            replaceVerifyOTPFrag();
        } else {
            onVolleyErrorListener(response.getMessage());
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        if (object instanceof CommonResponse) {
            CommonResponse response = (CommonResponse) object;

            if (response.getCode() == Constants.ALREADY_REGISTERED) {
                replaceLoginFrag();
            }
        }
        Utils.handleCommonErrors(myContext, object);
    }


}
