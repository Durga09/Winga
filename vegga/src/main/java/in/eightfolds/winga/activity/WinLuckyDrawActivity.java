package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

import com.android.volley.Request;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

public class WinLuckyDrawActivity extends BaseActivity implements VolleyResultCallBack {

    private ImageView luckyDrawIv;
    private TextView pointsWonTv, nameTv, contactTv;
    private RelativeLayout playmoreGamesRL;
    private PrizeWin prizeWin;
    private boolean fromNotification;
    private Notification notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_winluckydraw);
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
        if (prizeWin != null) {
            if (prizeWin.getType() == 3) {
                String amount = prizeWin.getAmount() + "";
                if (amount.contains(".0")) {
                    amount = amount.replace(".0", "");
                }

                prize = getString(R.string.rs) + "" + amount + "";
            } else if (prizeWin.getType()==4) {
                prize = prizeWin.getPoints()+" points";
            }
        }

        String name = Utils.getUserName(this);
        if (!TextUtils.isEmpty(name)) {
            nameTv.setText(name);
        }

        pointsWonTv.setText(String.format(Utils.getCurrentLocale(), getString(R.string.you_are_lucky_star), prize));
        setContactWinGaSpan();
        makeExitSpannable();
    }

    private void makeExitSpannable() {

        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        TextView exitTv = findViewById(R.id.exitTv);

        SpannableString exitSpan = makeLinkSpan(getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinLuckyDrawActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);

        makeLinksFocusable(exitTv);

    }

    private void setContactWinGaSpan() {
        String currentLanguage = Utils.getCurrentLanguage(this);

        SpannableString contactWinGaSpan = Utils.makeLinkSpan(getString(R.string.contact_winga), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinLuckyDrawActivity.this, SupportActivity.class);
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

    private void getNewGameResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_NEW_GAME_URL;
            String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url=url.replace("{categoryId}",id);

            EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);
        }
    }

    private void handleBack() {
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

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();

        if (id == R.id.contactTv) {
            Intent intent = new Intent(this, SupportActivity.class);
            startActivity(intent);
        } else if (id == R.id.playmoreGamesRL) {
            Intent intent1 = new Intent(this, HomeBaseActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

            // getNewGameResponse(); // Uncomment if needed

    /*
    Intent intent = new Intent(this, StartGameActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // intent.putExtra(Constants.HOME_DATA, homePageResponse);
    startActivity(intent);
    */
        }

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof NewGameResponse) {
            NewGameResponse newGameResponse = (NewGameResponse) object;
            (new UsageAnalytics()).trackPlayGame("", newGameResponse);
            if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                onVolleyErrorListener(getString(R.string.session_closed));
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Logg.v("dash", "items count >> " + newGameResponse.getContents().size());
                NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra(Constants.DATA, filteredLangGameResp);
                finish();

                startActivity(intent);
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }
}
