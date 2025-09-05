package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

//import com.adefruandta.spinningwheel.SpinningWheelView;
import com.android.volley.Request;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Random;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.BannerResponse;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.SpinItem;
import in.eightfolds.winga.v2.model.SpinResponse;
import in.eightfolds.winga.v2.model.SpinWinResponse;

public class V2SpinAndWinActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    ImageView backIv;
    Button spin;
    TextView termsDescTv, termsTv, titleTv,info;
    String title, information;
//    SpinningWheelView wheel;
    ArrayList<String> wheelItems;
    SpinResponse spinResponse;
    SpinWinResponse spinWinResponse;
    int spinOneTime=0;
    // KonfettiView konfettiView;

    int[] wheelDegrees;
    int degrees;
    Random random = new Random();
    private Dialog wonDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_spin_win);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("title")) {
                title = (String) bundle.get("title");
            }
            if (bundle.containsKey("information")) {
                information = (String) bundle.get("information");
            }
        }
        getWheelItems();
        initialize(true);
    }




    private void initialize(boolean b) {
        backIv = findViewById(R.id.backIv);

        spin = findViewById(R.id.spin);
//        wheel = findViewById(R.id.wheel);
        termsDescTv = findViewById(R.id.termsDescTv);
        termsTv = findViewById(R.id.termsConditionsLL);
        titleTv = findViewById(R.id.title);
        info=findViewById(R.id.info);


        //  konfettiView = findViewById(R.id.konfettiView);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        findViewById(R.id.failureReasonsIv).setOnClickListener(v -> MyDialog.showInformationPopUp(V2SpinAndWinActivity.this, information, title));

        if (information != null && !TextUtils.isEmpty(information)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                info.setText(Html.fromHtml(information.trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                info.setText(Html.fromHtml(information.trim()));
            }
        }
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinOneTime==0){
                    spinWheel();
                    spinOneTime=1;
                }


            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void spinWheel() {
        degrees = findIndex(spinResponse);
//        wheel.rotate((360 * wheelItems.size()) + wheelDegrees[degrees], 3000, 50);

    }

    private int findIndex(SpinResponse spinResponse) {

        int i = 0;
        for (SpinItem spinItem : spinResponse.getSpinItems()
        ) {
            if (spinItem.getNumber().equals(spinResponse.getResultSpinItemNumber())) {
                return i;
            } else {
                i++;
            }

        }
        return 0;
    }

    private void getWheelItems() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_SPIN_RESPONSE;
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, SpinResponse.class, Request.Method.GET, url);
        }

    }

    private void updateSpinWinItem(String id) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_SPIN_WIN_RESPONSE + id;
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, SpinWinResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof SpinResponse) {
            spinResponse = (SpinResponse) object;
            termsTv.setVisibility(View.GONE);
            if (spinResponse.getSpinTermsAndConditions() != null && !TextUtils.isEmpty(spinResponse.getSpinTermsAndConditions())) {
                termsTv.setVisibility(View.VISIBLE);
                termsTv.setOnClickListener(v -> {
                    showBottomSheetDialog();
                });

            } else {

                termsDescTv.setVisibility(View.GONE);
            }
            createWheelItems(spinResponse.getSpinItems());
        }
        if (object instanceof SpinWinResponse) {
            spinWinResponse = (SpinWinResponse) object;
            MyDialog.showToast(V2SpinAndWinActivity.this, spinWinResponse.getTitle());
            if (spinWinResponse.isShowPopUp()) {
                wonDialog = MyDialog.showSpinWonDialog(V2SpinAndWinActivity.this, spinWinResponse, this);
            } else {
                Intent intent = new Intent(this, V2SpinWinVoucherActivity.class);
                intent.putExtra(Constants.DATA, spinWinResponse);
                this.startActivity(intent);
                finish();
            }
        }
        if (object instanceof CommonServerResponse && !requestType.contains(WingaConstants.GET_SPIN_REDIRECT_URL)) {
            CommonServerResponse commonServerResponse = (CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());
            if (requestType.contains(WingaConstants.GET_SPIN_RESPONSE)) {
                onBackPressed();
            }
            if (TextUtils.isEmpty(spinWinResponse.getRedirectUrl())) {
                onBackPressed();
            }


        }
    }

    private void getDegreesofEachWheelSector() {
        int sector = 360 / wheelItems.size();
        wheelDegrees = new int[wheelItems.size()];
        for (int i = 0; i < wheelItems.size(); i++) {
            wheelDegrees[i] = (i + 1) * sector;

        }
    }

    private void createWheelItems(ArrayList<SpinItem> spinItems) {

        wheelItems = new ArrayList<>();

        for (SpinItem spinItem : spinItems) {


            wheelItems.add("\n\t"+spinItem.getSpinValue());


        }
        getDegreesofEachWheelSector();

       /* wheel.setItems(wheelItems);
        wheel.setEnabled(false);

        wheel.setOnRotationListener(new SpinningWheelView.OnRotationListener<String>() {
            // Call once when start rotation
            @Override
            public void onRotation() {

            }

            // Call once when stop rotation
            @Override
            public void onStopRotation(String item) {

                int index = wheelItems.indexOf(item);
                updateSpinWinItem(spinResponse.getSpinItems().get(index).getNumber());




            }
        });*/


    }




    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        if (type ==R.id.exitTv) {


            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
                onBackPressed();
            }

        }

           else if(type==R.id.continueBtn)
                if (spinWinResponse.getPoints() != null) {
                    claimPoints(spinWinResponse);
                }

                if (!TextUtils.isEmpty(spinWinResponse.getRedirectUrl())) {
                    callRedirectUrl();
                }

                wonDialog.dismiss();


    }

    private void claimPoints(SpinWinResponse spinWinResponse) {


        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            //sendRedirectUrl(spinWinResponse);
            String url = WingaConstants.CLIAM_POINTS + spinWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
            callRedirectUrl();
        }
    }


    void callRedirectUrl() {
        if (!TextUtils.isEmpty(spinWinResponse.getRedirectUrl())) {
            sendRedirectUrl(spinWinResponse);
            Utils.openInBrowser(this, spinWinResponse.getRedirectUrl());
//            Intent intent = new Intent(this, WebBrowserActivity.class);
//            intent.putExtra(Constants.DATA, spinWinResponse.getRedirectUrl());
//            intent.putExtra(Constants.TITLE, spinWinResponse.getTitle());
//            intent.putExtra("fromWhichPage", 2);
//            startActivity(intent);
        }
    }

    private void sendRedirectUrl(SpinWinResponse bannerResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_SPIN_REDIRECT_URL + bannerResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.v2_bottom_sheet_dailog);
        ImageView close = bottomSheetDialog.findViewById(R.id.cross);
        TextView textView = (TextView) bottomSheetDialog.findViewById(R.id.bottom_text);

        if (spinResponse.getSpinTermsAndConditions() != null && !TextUtils.isEmpty(spinResponse.getSpinTermsAndConditions())) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(spinResponse.getSpinTermsAndConditions().trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(spinResponse.getSpinTermsAndConditions().trim()));
            }


        }
        assert close != null;
        close.setOnClickListener(v -> bottomSheetDialog.cancel());


        bottomSheetDialog.show();
    }
}
