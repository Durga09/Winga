package in.eightfolds.winga.activity;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.eightfolds.winga.R;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backIv) {
            if (!(this instanceof RecentWinnersActivity || this instanceof ResultsActivity || this instanceof SettingsActivity || this instanceof AlertsPromosUpdatesActivity
                    || this instanceof QuestionsAndAnswersFragmentActivity) || this instanceof NotificationActivity
                    || this instanceof CodesAndInvitesActivity || this instanceof NotificationDetailsActivity) {
                finish();
            }
        }
    }


    public void setHeader(String title) {
        TextView titleTv = findViewById(R.id.titleTv);
        ImageView backIv = findViewById(R.id.backIv);

        titleTv.setText(title);
        backIv.setOnClickListener(this);

    }

    public void setHeaderWithEdit(String title) {
        TextView titleTv = findViewById(R.id.titleTv);
        ImageView backIv = findViewById(R.id.backIv);
        ImageView headerRightIconIv = findViewById(R.id.headerRightIconIv);

        headerRightIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_black));
        headerRightIconIv.setVisibility(View.VISIBLE);
        titleTv.setText(title);
        backIv.setOnClickListener(this);
    }

    public void setHeader(String title, boolean showAddress) {
        setHeader(title);
        ImageView addAddressIv = findViewById(R.id.headerRightIconIv);
        addAddressIv.setImageResource(R.drawable.ic_add);
        addAddressIv.setVisibility(View.VISIBLE);
    }

    public void setHeaderWithFilter(String title) {
        setHeader(title);
        ImageView addAddressIv = findViewById(R.id.headerRightIconIv);
        addAddressIv.setVisibility(View.VISIBLE);
        addAddressIv.setOnClickListener(this);
        addAddressIv.setImageResource(R.drawable.ic_filter);
    }

    public void setHeaderWithDone(String title) {
        setHeader(title);
        TextView doneTv = findViewById(R.id.doneTv);
        doneTv.setVisibility(View.VISIBLE);
        doneTv.setOnClickListener(this);
    }

    public void setHeaderWithClear(String title) {
        setHeader(title);
        TextView doneTv = findViewById(R.id.doneTv);
        doneTv.setText(getString(R.string.clear));
        doneTv.setVisibility(View.VISIBLE);
        doneTv.setOnClickListener(this);
    }

    public void setHeaderWithSearchCalendar(String title) {
        setHeader(title);
        ImageView calenderIv = findViewById(R.id.headerRightIconIv);
        ImageView searchIv = findViewById(R.id.searchIv);
        calenderIv.setImageResource(R.drawable.ic_calendar);
        calenderIv.setVisibility(View.VISIBLE);
        searchIv.setVisibility(View.VISIBLE);
        calenderIv.setOnClickListener(this);
        searchIv.setOnClickListener(this);


    }


    public static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;

        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    public SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);

        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    @Override
    protected void onResume() {
        String SCREEN_LABEL = ((Activity) this).getClass().getSimpleName();

        Logg.e(TAG, "**********************onStart() : " + SCREEN_LABEL);

        super.onResume();

        (new UsageAnalytics()).trackPage(SCREEN_LABEL);
        if (!Constants.isAppInFg) {
            Constants.isAppInFg = true;
            Constants.isChangeScrFg = false;
            Logg.e(TAG, "** App Foreground");
        } else {
            Constants.isChangeScrFg = true;
        }
        Constants.isScrInFg = true;
    }

    @Override
    protected void onPause() {
        try {
          //  setOnStop();

            super.onPause();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setOnStop() {
        String SCREEN_LABEL = ((Activity) this).getClass().getSimpleName();


        Logg.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%%onStop() : " + SCREEN_LABEL);


        if (!Constants.isScrInFg || !Constants.isChangeScrFg) {
            Constants.isAppInFg = false;
            Logg.e(TAG, "** App Background");
        }
        Constants.isScrInFg = false;
    }
}
