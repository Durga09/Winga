package in.eightfolds.winga.activity;

import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.eightfolds.winga.R;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class WebBrowserActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = WebBrowserActivity.class.getSimpleName();
    private WebView webView;
    private String url, title;

    private String voucherTerms;

    private int fromWhichPage=0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromWhichPage==1){
            Intent intent=new Intent(this,V2OttActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        }else if(fromWhichPage==2 || fromWhichPage==3){
            Intent intent=new Intent(this,V2HomeFeatureActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() ==  R.id.backIv){
                onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(WebBrowserActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_browser);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                url = bundle.getString(Constants.DATA);
            }
            if (bundle.containsKey(Constants.TITLE)) {
                title = bundle.getString(Constants.TITLE);
            }
            if(bundle.containsKey("voucherTerms")){
                voucherTerms= bundle.getString("voucherTerms");
            }
            if(bundle.containsKey("fromWhichPage")){
                fromWhichPage=bundle.getInt("fromWhichPage");
            }
        }
        initialize();
    }

    private void initialize() {
        if (!TextUtils.isEmpty(title)) {
            setHeader(title);
        }

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
       /* else{
            webView.loadData(voucherTerms, "text/html; charset=utf-8", "UTF-8");
        }*/
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("mailto:")) {


                MailTo mt = MailTo.parse(url);

                   sendEmail(WebBrowserActivity.this, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());


                view.reload();
                return true;

            } else {
                view.loadUrl(url);
                return false;
            }

        }
    }

    private void sendEmail(Context context, String address, String subject, String body, String cc) {


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

