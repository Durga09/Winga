package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;

import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.ExclusiveCategoryItem;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;


public class AnouncementDetailActivity extends BaseActivity{

    private ImageView anouncementIv;
    private TextView anouncementDescTv, anouncementTitleTv, anouncementDateTv,anouncementLinkTv;
    private ExclusiveCategoryItem exclusiveCategoryItem;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_anouncements_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                exclusiveCategoryItem = (ExclusiveCategoryItem) bundle.get(Constants.DATA);
            }


        }
        initialize();
    }

    private void initialize() {
        setHeader(exclusiveCategoryItem.getTitle());
        anouncementIv = findViewById(R.id.anouncementIv);
        anouncementDescTv = findViewById(R.id.anouncementDescTv);
        anouncementTitleTv = findViewById(R.id.anouncementTitleTv);
        anouncementDateTv = findViewById(R.id.anouncementDateTv);
        anouncementLinkTv = findViewById(R.id.anouncementLinkTv);
        anouncementDescTv.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.backIv).setOnClickListener(this);
        displayNotification(exclusiveCategoryItem);
        if(!TextUtils.isEmpty(exclusiveCategoryItem.getRedirectUrl())){
            setRedirectURlSpan();
        }

    }

    private void setRedirectURlSpan() {
        SpannableString redirectUlrSpan = Utils.makeLinkSpan(exclusiveCategoryItem.getRedirectUrl(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(exclusiveCategoryItem.getRedirectUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        redirectUlrSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_header_color)), 0, redirectUlrSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.NORMAL);
        redirectUlrSpan.setSpan(iss, 0, redirectUlrSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        anouncementLinkTv.append(" ");
        anouncementLinkTv.append(redirectUlrSpan);
        Utils.makeLinksFocusable(anouncementLinkTv);
    }

    private void displayNotification(ExclusiveCategoryItem exclusiveCategoryItem) {
        if (!TextUtils.isEmpty(exclusiveCategoryItem.getMessage())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                anouncementDescTv.setText(Html.fromHtml(exclusiveCategoryItem.getMessage(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                anouncementDescTv.setText(Html.fromHtml(exclusiveCategoryItem.getMessage()));
            }
        }

        if (!TextUtils.isEmpty(exclusiveCategoryItem.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                anouncementTitleTv.setText(Html.fromHtml(exclusiveCategoryItem.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                anouncementTitleTv.setText(Html.fromHtml(exclusiveCategoryItem.getTitle()));
            }
        }

        if (exclusiveCategoryItem.getCreatedTime() != null) {

            anouncementDateTv.setText(exclusiveCategoryItem.getCreatedTime());

        }
        if (exclusiveCategoryItem.getImageId() != null) {

            Glide.with(this)
                    .load(EightfoldsUtils.getInstance().getImageFullPath(exclusiveCategoryItem.getImageId(), Constants.FILE_URL))
                    // .placeholder(R.drawable.ic_user_filled)
                    //.error(R.drawable.ic_user_filled)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                                  @Override
                                  public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                      // notificationIv.setVisibility(View.GONE);

                                      return false;
                                  }

                                  @Override
                                  public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                      return false;
                                  }
                              }
                    )
                    .into(anouncementIv);
            ;
        } else if(!TextUtils.isEmpty(exclusiveCategoryItem.getImageUrl())){
            Glide.with(this)
                    .load(exclusiveCategoryItem.getImageUrl())
                    // .placeholder(R.drawable.ic_user_filled)
                    //.error(R.drawable.ic_user_filled)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                                  @Override
                                  public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                      // notificationIv.setVisibility(View.GONE);

                                      return false;
                                  }

                                  @Override
                                  public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                      return false;
                                  }
                              }
                    )
                    .into(anouncementIv);


        }else{
            anouncementIv.setVisibility(View.GONE);
        }
    }
}
