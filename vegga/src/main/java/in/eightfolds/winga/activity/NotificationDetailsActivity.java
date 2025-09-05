package in.eightfolds.winga.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.Utils;

public class NotificationDetailsActivity extends BaseActivity implements VolleyResultCallBack {
    private static final String TAG = NotificationDetailsActivity.class.getSimpleName();
    private ImageView notificationIv;
    private TextView notificationDescTv, notificationTitleTv, notificationDateTv;
    // private ImageView closeIv;
    private ProgressDialog progressDialog;
    private boolean fromNotification;
    private Notification notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_notification_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                notification = (Notification) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
            }

        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.notifications));
        notificationIv = findViewById(R.id.notificationIv);
        notificationDescTv = findViewById(R.id.notificationDescTv);
        notificationTitleTv = findViewById(R.id.notificationTitleTv);
        notificationDateTv = findViewById(R.id.notificationDateTv);
        notificationDescTv.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.backIv).setOnClickListener(this);

        if (fromNotification && notification.getNotificationId() != null) {
            String url = Constants.GET_NOTIFICATION_DETAIL;
            url = url.replace("{notificationId}", notification.getNotificationId().toString());
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, Notification.class, Request.Method.GET, url);
            Utils.makeNotificationAsRead(this, notification.getNotificationId());
       }
        displayNotification(notification);
    }

    private void displayNotification(Notification notification) {
        if (!TextUtils.isEmpty(notification.getMessage())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                notificationDescTv.setText(Html.fromHtml(notification.getMessage(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                notificationDescTv.setText(Html.fromHtml(notification.getMessage()));
            }
        }

        if (!TextUtils.isEmpty(notification.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                notificationTitleTv.setText(Html.fromHtml(notification.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                notificationTitleTv.setText(Html.fromHtml(notification.getTitle()));
            }
        }

        if (notification.getCreatedTime() != null) {
            String date = notification.getCreatedTime();
            String formattedDate = null;
            try {
                formattedDate = DateTime.getDateFromUTC(date, "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");
                notificationDateTv.setText(!TextUtils.isEmpty(formattedDate) ? formattedDate : "");
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        if (notification.getImageId() != null) {

            Glide.with(this)
                    .load(EightfoldsUtils.getInstance().getImageFullPath(notification.getImageId(), Constants.FILE_URL))
                    // .placeholder(R.drawable.ic_user_filled)
                    //.error(R.drawable.ic_user_filled)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                                  @Override
                                  public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                      // notificationIv.setVisibility(View.GONE);
                                      dismissProgress();
                                      return false;
                                  }

                                  @Override
                                  public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                      dismissProgress();
                                      return false;
                                  }
                              }
                    )
                    .into(notificationIv);
            ;
        } else {
            notificationIv.setVisibility(View.GONE);
            dismissProgress();
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        Logg.v(TAG, "fromNotification > " + fromNotification);
        if (fromNotification) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void dismissProgress() {
        notificationDescTv.setVisibility(View.VISIBLE);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv) {
            handleBack();
        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof Notification) {
            Notification notification = (Notification) object;
            if (notification.getTitle() != null) {
                displayNotification(notification);
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }
}
