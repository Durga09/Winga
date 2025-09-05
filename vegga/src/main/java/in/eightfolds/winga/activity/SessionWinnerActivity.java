package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class SessionWinnerActivity extends BaseActivity {


    private ImageView luckyDrawIv;
    private TextView pointsWonTv, nameTv, contactTv;
    private RelativeLayout playmoreGamesRL;
    private PrizeWin prizeWin;
    private boolean fromNotification;
    private Notification notification;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_winner_session);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.OTHER_DATA)) {
                prizeWin = (PrizeWin) bundle.get(Constants.OTHER_DATA);
            }
            if (bundle.containsKey(Constants.DATA)) {
                notification = (Notification) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey(Constants.FROM_NOTIFICATION)) {
                fromNotification = bundle.getBoolean(Constants.FROM_NOTIFICATION);
            }
        }
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialize() {
        luckyDrawIv = findViewById(R.id.luckyDrawIv);
        playmoreGamesRL = findViewById(R.id.playmoreGamesRL);
        pointsWonTv = findViewById(R.id.pointsWonTv);
        nameTv = findViewById(R.id.nameTv);
        contactTv = findViewById(R.id.contactTv);

        if (fromNotification && notification.getNotificationId() != null) {
            Utils.makeNotificationAsRead(this, notification.getNotificationId());
        }

        Utils.loadGifResourceToImageView(this, luckyDrawIv, R.drawable.giphy);
        playmoreGamesRL.setOnClickListener(this);
        // contactTv.setOnClickListener(this);

        String prize = "";


        String name = Utils.getUserName(this);
        if (!TextUtils.isEmpty(name)) {
            nameTv.setText(name);
        }


        pointsWonTv.setText(Html.fromHtml(notification.getMessage(),Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        setContactWinGaSpan();

    }

    private void setContactWinGaSpan() {
        String currentLanguage = Utils.getCurrentLanguage(this);

        SpannableString contactWinGaSpan = Utils.makeLinkSpan(getString(R.string.contact_winga), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionWinnerActivity.this, SupportActivity.class);
                startActivity(intent);
            }
        });
        StyleSpan iss = new StyleSpan(Typeface.NORMAL);

        if (currentLanguage.equalsIgnoreCase(Constants.TELUGU_LOCALE)) {
            contactTv.append(getString(R.string.for_further_info));
            contactTv.append(" ");
            contactWinGaSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_header_color)), 0, contactWinGaSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactWinGaSpan.setSpan(iss, 0, contactWinGaSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            contactWinGaSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_header_color)), 0, contactWinGaSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactWinGaSpan.setSpan(iss, 0, contactWinGaSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //contactTv.append(" ");
        contactTv.append(contactWinGaSpan);
        if (!currentLanguage.equalsIgnoreCase(Constants.TELUGU_LOCALE)) {
            contactTv.append(" ");
            contactTv.append(getString(R.string.for_further_info));
        }
        Utils.makeLinksFocusable(contactTv);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.contactTv) {
            Intent intent = new Intent(this, SupportActivity.class);
            startActivity(intent);
        }
            else if(v.getId() == R.id.playmoreGamesRL) {
            Intent intent1 = new Intent(this, HomeBaseActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            //  getNewGameResponse();

                /*Intent intent = new Intent(this, StartGameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.putExtra(Constants.HOME_DATA, homePageResponse);
                startActivity(intent);*/


        }
    }

}
