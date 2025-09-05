package in.eightfolds.winga.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.SupportHistoryRecyclerAdapter;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.HelpDesk;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.SupportRequest;
import in.eightfolds.winga.model.SupportType;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ConstantsManager;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 04-May-18.
 */

public class SupportActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, VolleyResultCallBack {
    private EditText writeCommentEt;
    private Spinner contentSpinner;
    private Button sendBtn;
    private ArrayList<SupportType> supportTypes = new ArrayList<>();
    private SupportType selectedContentType;
    private SupportRequest supportRequest;
    private Setup setup;
    private TextView customerNumTv, emailTv;
    private LinearLayout contactCustomerCareLL, emailLL;
    private int pageNo = 1;
    private ArrayList<SupportRequest> supportRequestHistoryList;
    private RecyclerView ticketsHistoryRecyclerView;
    private TextView ticketsCounttv, commentLetterCountTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_support);
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.support));
        writeCommentEt = findViewById(R.id.writeCommentEt);
        contentSpinner = findViewById(R.id.contentSpinner);
        sendBtn = findViewById(R.id.sendBtn);
        emailTv = findViewById(R.id.emailTv);
        commentLetterCountTv = findViewById(R.id.commentLetterCountTv);
        customerNumTv = findViewById(R.id.customerNumTv);
        contactCustomerCareLL = findViewById(R.id.contactCustomerCareLL);
        emailLL = findViewById(R.id.emailLL);
        ticketsHistoryRecyclerView = findViewById(R.id.ticketsHistoryRecyclerView);
        ticketsCounttv = findViewById(R.id.ticketsCounttv);


        writeCommentEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        writeCommentEt.setRawInputType(InputType.TYPE_CLASS_TEXT);

        setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);

        supportTypes = ConstantsManager.getSupportTypes(this);
        setSupportTypesSpinner();

        sendBtn.setOnClickListener(this);
        contentSpinner.setOnItemSelectedListener(this);
        contactCustomerCareLL.setOnClickListener(this);
        emailLL.setOnClickListener(this);

        if (setup != null) {
            if (setup.getHelpDesks() != null && setup.getHelpDesks().size() > 0) {
                for (HelpDesk helpDesk : setup.getHelpDesks()) {
                    if (helpDesk.getType() == 1) {
                        if (!TextUtils.isEmpty(helpDesk.getVal())) {
                            customerNumTv.setText(helpDesk.getVal());
                        } else {
                            contactCustomerCareLL.setVisibility(View.GONE);
                        }
                    }

                    if (helpDesk.getType() == 2) {
                        if (!TextUtils.isEmpty(helpDesk.getVal())) {
                            emailTv.setText(helpDesk.getVal());
                        } else {
                            emailLL.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

        getHistory(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        writeCommentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                commentLetterCountTv.setText(length + " / 200");
            }
        });

        //writeCommentEt.setFilters(new InputFilter[]{EMOJI_FILTER});

    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketsHistoryRecyclerView.setFocusable(true);
    }

    public void setSupportTypesSpinner() {
        int selection = 0;
        ArrayAdapter<String> supportTypesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        supportTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //supportTypesAdapter.add(this.getString(R.string.content));
        contentSpinner.setAdapter(supportTypesAdapter);
        if (supportTypes != null && !supportTypes.isEmpty()) {
            for (int i = 0; i < supportTypes.size(); i++) {
                SupportType supportType = supportTypes.get(i);
                supportTypesAdapter.add(!TextUtils.isEmpty(supportType.getTitle()) ? supportType.getTitle() : "");
            }
        }
        contentSpinner.setSelection(selection);
    }

    private void callCustomerCare() {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + customerNumTv.getText().toString()));
        startActivity(dialIntent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();

        if (id == R.id.emailLL) {
            emailCustomerCare();
        } else if (id == R.id.contactCustomerCareLL) {
            callCustomerCare();
        } else if (id == R.id.sendBtn) {
            supportRequest = new SupportRequest();
            String comment = writeCommentEt.getText().toString();

            if (selectedContentType == null) {
                MyDialog.showToast(this, getString(R.string.select_content_type));
            } else if (comment == null || comment.length() < 3) {
                MyDialog.showToast(this, getString(R.string.select_valid_comment));
            } else {
                supportRequest.setSupportTypeId(selectedContentType.getSupportTypeId());
                supportRequest.setMessage(writeCommentEt.getText().toString());
                sendSupportReq();
            }
        }


    }

    private void emailCustomerCare() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailTv.getText().toString()});
        String mobileNumber = EightfoldsUtils.getInstance().getFromSharedPreference(this,Constants.MOBILE_NUMBER);
        User user = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.USER_DETAILS, User.class);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mobile - "+ user.getMobile());
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "\n \n \n \n \n \n \nRegards, \n"+ user.getMobile());
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));



        /*final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+getString(R.string.customer_care_email)));

        emailIntent.setType("plain/text");
         //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");


        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));*/

    }

    private void callProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendSupportReq() {
        EightfoldsUtils.getInstance().hideKeyboard(this);
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingJsonRequest(this, CommonResponse.class, Request.Method.POST, Constants.ADD_SUPPORT_REQUEST_URL, supportRequest);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // if (adapterView.getChildCount() > 0 && adapterView.getChildAt(0) instanceof TextView) {
        //  ((TextView) adapterView.getChildAt(0)).setTextColor(position == 0 ? getResources().getColor(R.color.address_hint_text_color) : getResources().getColor(R.color.address_text_color));
        ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorWhite));

        // }
        int i = adapterView.getId();
        if (i == R.id.contentSpinner) {
            selectedContentType = supportTypes.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            if (commonResponse.getCode() == Constants.SUCCESS) {
                MyDialog.showToast(this, getString(R.string.successfully_submitted));
                writeCommentEt.setText("");

                getHistory(true);
                //  callProfile();
            } else {
                onVolleyErrorListener(commonResponse);
            }
        } else if (object instanceof SupportRequest[]) {
            supportRequestHistoryList = new ArrayList<>();
            SupportRequest[] supportRequests = (SupportRequest[]) object;
            supportRequestHistoryList.addAll(Arrays.asList(supportRequests));
            if (supportRequestHistoryList.size() > 0) {
                ticketsCounttv.setText(String.format(getString(R.string.all), supportRequestHistoryList.size()));
            } else {
                ticketsCounttv.setText("");
            }
            setAdapter();
           /* if ((swipeRefreshLayout.isRefreshing()))
                swipeRefreshLayout.setRefreshing(false);*/
        }


    }

    private void setAdapter() {


        if (supportRequestHistoryList.size() > 0) {
            findViewById(R.id.historyLL).setVisibility(View.VISIBLE);
            SupportHistoryRecyclerAdapter playHistoryRecyclerAdapter = new SupportHistoryRecyclerAdapter(this, supportRequestHistoryList, "");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
            ticketsHistoryRecyclerView.setLayoutManager(layoutManager);


            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            ticketsHistoryRecyclerView.setLayoutAnimation(animation);


            ticketsHistoryRecyclerView.setAdapter(playHistoryRecyclerAdapter);
            findViewById(R.id.historyLL).setVisibility(View.VISIBLE);
        } else {

            // (findViewById(R.id.topRL)).setBackground(getResources().getDrawable(R.drawable.noplayhistory));
            findViewById(R.id.historyLL).setVisibility(View.GONE);
        }

        ViewCompat.setNestedScrollingEnabled(ticketsHistoryRecyclerView, false);
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    private void getHistory(boolean showProgress) {

        EightfoldsUtils.getInstance().hideKeyboard(this);
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            String url = Constants.GET_SUPPORT_TICKETS_HISTORY;


            try {
                url = url.replace("{tz}", URLEncoder.encode(DateTime.getCurrentTimeZoneNameAndTimeDiff(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            url = url.replace("{page}", pageNo + "");
            url = url.replace("{pageSize}", Constants.PAGE_SIZE + "");

            if (showProgress) {
                // swipeRefreshLayout.setRefreshing(showProgress);
            } else {
                EightfoldsVolley.getInstance().showProgress(this);
            }
            EightfoldsVolley.getInstance().makingStringRequest(this, SupportRequest[].class, Request.Method.GET, url);
        }
    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE || type == Character.NON_SPACING_MARK) {
                    return "";
                }
            }
            return null;
        }
    };

}

