package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.adapter.ScratchAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.GuessAndWinResponse;
import in.eightfolds.winga.v2.model.GuessItem;
import in.eightfolds.winga.v2.model.GuessItemResponse;

public class V2GuessTheBrand extends BaseActivity implements View.OnClickListener, OnEventListener, VolleyResultCallBack {

    GuessItemResponse guessItemResponse;
    private RecyclerView guessItemRecyclerView;
    private ScratchAdapter scratchAdapter;
    private TextView termsAndConditionsTv, termsAndConditionsDescTv,titleTv,info;
    private ImageView backIv;
    private String title,information;
    private Dialog wonDialog;
    GuessAndWinResponse guessAndWinResponse;


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {
        int typeId = type;

        if (typeId == R.id.exitTv) {
            if (wonDialog != null && wonDialog.isShowing()) {
                wonDialog.dismiss();
                onBackPressed();
            }
        } else if (typeId == R.id.continueBtn) {
            if (guessAndWinResponse.getPoints() != null) {
                claimPoints(guessAndWinResponse);
            }

            if (!TextUtils.isEmpty(guessAndWinResponse.getRedirectUrl())) {
                callRedirectUrl();
            }

            wonDialog.dismiss();
        }

    }

    @Override
    public void onEventListener(int type, Object object) {
        if(object instanceof GuessItem){
            sendGuessItemWon((GuessItem)object);
        }

    }



    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof GuessItemResponse) {
            guessItemResponse = (GuessItemResponse) object;
            setAdapter(guessItemResponse.guessItems);
            if (!TextUtils.isEmpty(guessItemResponse.termsAndConditions)) {
                termsAndConditionsDescTv.setVisibility(View.GONE);
                termsAndConditionsTv.setVisibility(View.VISIBLE);
                 termsAndConditionsTv.setOnClickListener(v -> {
                     showBottomSheetDialog();
                 });
//                termsAndConditionsDescTv.setText(guessItemResponse.termsAndConditions);

            } else {
                termsAndConditionsDescTv.setVisibility(View.GONE);
                termsAndConditionsTv.setVisibility(View.GONE);
            }
        }
        if (object instanceof GuessAndWinResponse) {
            guessAndWinResponse = (GuessAndWinResponse) object;
            if (guessAndWinResponse.isShowPopUp()) {
                wonDialog = MyDialog.showGuessWonDialog(V2GuessTheBrand.this, guessAndWinResponse, this);
            }else{
                Intent intent= new Intent(this,V2GuessWonDetailActivity.class);
                intent.putExtra(Constants.DATA,guessAndWinResponse);
                this.startActivity(intent);
                finish();
            }
        }
        if(object instanceof CommonServerResponse  && !requestType.contains(WingaConstants.GET_GUESS_REDIRECT_URL)){
            CommonServerResponse commonServerResponse=(CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());
            if (requestType.contains(WingaConstants.GET_GUESS_THE_BRAND)){
                onBackPressed();
            }

            if(TextUtils.isEmpty(guessAndWinResponse.getRedirectUrl())){
                onBackPressed();
            }




        }
    }

    private void setAdapter(ArrayList<GuessItem> guessItems) {

        scratchAdapter = new ScratchAdapter(guessItems, V2GuessTheBrand.this, this);

        guessItemRecyclerView.setAdapter(scratchAdapter);
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_guees_the_brand);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("title")){
                title= (String) bundle.get("title");
            }
            if(bundle.containsKey("information")){
                information=(String) bundle.get("information");
            }
        }

        initialize(true);

    }

    private void initialize(boolean b) {
        getScratchItems();
        backIv = findViewById(R.id.backIv);
        guessItemRecyclerView = findViewById(R.id.scratch_Rv);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        guessItemRecyclerView.setLayoutManager(layoutManager1);
        termsAndConditionsTv = findViewById(R.id.termsConditionsLL);
        termsAndConditionsDescTv = findViewById(R.id.termsDescTv);
        titleTv=findViewById(R.id.title);
        info=findViewById(R.id.info);


        if(!TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }

        if (information != null && !TextUtils.isEmpty(information)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                info.setText(Html.fromHtml(information.trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                info.setText(Html.fromHtml(information.trim()));
            }
        }

        findViewById(R.id.failureReasonsIv).setOnClickListener(v -> MyDialog.showInformationPopUp(V2GuessTheBrand.this,information,title));


        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void getScratchItems() {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_GUESS_THE_BRAND;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, GuessItemResponse.class, Request.Method.GET, url);
        }else {
            goToNoInternetActivity();
        }


    }

    private void sendGuessItemWon(GuessItem object) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_GUESS_WIN+object.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, GuessAndWinResponse.class, Request.Method.GET, url);

        }
    }

    private void claimPoints(GuessAndWinResponse guessAndWinResponse) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = WingaConstants.CLAIM_GUESS_POINTS + guessAndWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
           callRedirectUrl();
        }else {
            goToNoInternetActivity();
        }
    }

    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }

    private void callRedirectUrl() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            if (!TextUtils.isEmpty(guessAndWinResponse.getRedirectUrl())) {
                sendRedirectUrl(guessAndWinResponse);
                Utils.openInBrowser(this,guessAndWinResponse.getRedirectUrl());
//                Intent intent = new Intent(this, WebBrowserActivity.class);
//                intent.putExtra(Constants.DATA, guessAndWinResponse.getRedirectUrl());
//                intent.putExtra(Constants.TITLE, guessAndWinResponse.getTitle());
//                intent.putExtra("fromWhichPage", 3);
//                startActivity(intent);
            }
        } else {
            goToNoInternetActivity();
        }
    }

    private void sendRedirectUrl(GuessAndWinResponse guessAndWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_GUESS_REDIRECT_URL + guessAndWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.v2_bottom_sheet_dailog);
        ImageView close = bottomSheetDialog.findViewById(R.id.cross);
        TextView textView =(TextView) bottomSheetDialog.findViewById(R.id.bottom_text);

        if (guessItemResponse.termsAndConditions != null && !TextUtils.isEmpty(guessItemResponse.termsAndConditions)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(guessItemResponse.termsAndConditions.trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(guessItemResponse.termsAndConditions.trim()));
            }


        }
        assert close != null;
        close.setOnClickListener(v -> bottomSheetDialog.cancel());


        bottomSheetDialog.show();
    }
}
