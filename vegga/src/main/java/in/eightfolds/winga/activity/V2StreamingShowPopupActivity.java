package in.eightfolds.winga.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.GuessAndWinResponse;
import in.eightfolds.winga.v2.model.StreamingItem;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

public class V2StreamingShowPopupActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private StreamingWinResponse streamingWinResponse;
    Dialog wonDialog;
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
            if (streamingWinResponse.getPoints() != null) {
                claimPoints(streamingWinResponse);
            }

            if (!TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())) {
                calRedirectUrl();
            }

            wonDialog.dismiss();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,V2OttActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if(object instanceof CommonServerResponse && !requestType.contains(WingaConstants.GET_STREAMING_REDIRECT_URL)){
            CommonServerResponse commonServerResponse=(CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());

            if(TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())){
                onBackPressed();
            }


        }
    }

    private void claimPoints(StreamingWinResponse streamingWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            //sendRedirectUrl(streamingWinResponse);
            String url = WingaConstants.CLAIM_STREAMING_WIN_POINTS + streamingWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);

        }

    }

    void calRedirectUrl(){
        if(!TextUtils.isEmpty(streamingWinResponse.getRedirectUrl())){
            sendRedirectUrl(streamingWinResponse);
            Utils.openInBrowser(this,streamingWinResponse.getRedirectUrl());


            finish();
        }
    }

    private void sendRedirectUrl(StreamingWinResponse guessAndWinResponse) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_STREAMING_REDIRECT_URL + guessAndWinResponse.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_streaming_show_popup);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                streamingWinResponse = (StreamingWinResponse) bundle.get(Constants.DATA);
            }
        }
        initialize(true);
    }

    private void initialize(boolean b) {

        wonDialog =   MyDialog.showStreamingWonDialog(V2StreamingShowPopupActivity.this, streamingWinResponse, this);

    }
}
