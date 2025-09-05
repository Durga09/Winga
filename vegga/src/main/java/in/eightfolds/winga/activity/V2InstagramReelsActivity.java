package in.eightfolds.winga.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.v2.adapter.InstaReelAdapter;
import in.eightfolds.winga.v2.adapter.InstaReelTypeAdapter;
import in.eightfolds.winga.v2.adapter.StreamingTypeAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.InstaReelItem;
import in.eightfolds.winga.v2.model.InstaReelType;
import in.eightfolds.winga.v2.model.InstareelResponse;
import in.eightfolds.winga.v2.model.StreamingItem;
import in.eightfolds.winga.v2.model.InstaReelType;
import in.eightfolds.winga.v2.model.StreamingType;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

public class V2InstagramReelsActivity extends BaseActivity implements View.OnClickListener, OnEventListener, VolleyResultCallBack {


    ImageView backIv;
    EditText searchText;
    RecyclerView instaReelRv;
    InstaReelAdapter instaReelAdapter;
    TextView titleTv;
    String title, information;

    ArrayList<InstaReelType> instaReelTypes;

     RecyclerView instaReelTypeRecyclerView;

     InstaReelTypeAdapter instaReelTypeAdapter;
     InstaReelType selectedInstaReelType;

    List<InstaReelItem> instaReelItemList = new ArrayList<>();
    Map<InstaReelType, ArrayList<InstaReelItem>> map = new HashMap<>();
    int pageFirstTimeLaunched=0;


    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }


    private InstaReelType getAllInstaReelType() {

        InstaReelType instaReelType = new InstaReelType();
        instaReelType.setId(0);
        instaReelType.setSelected(true);
        instaReelType.setName("All");
        selectedInstaReelType=instaReelType;
        return instaReelType;
    }

    private void sendRedirectUrl(InstaReelItem instaReelItem) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_INSTA_REDIRECT_URL + instaReelItem.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);


            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, CommonServerResponse.class, Request.Method.GET, url);
        }
    }

    @Override
    public void onEventListener(int type, Object object) {
        if (object instanceof InstaReelItem) {
            sendRedirectUrl((InstaReelItem) object);
            if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
                Uri uri = Uri.parse(((InstaReelItem) object).getInstaReelUrl());
                Intent likeIng = new Intent(Intent.ACTION_VIEW);
                likeIng.setData(uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);

                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(((InstaReelItem) object).getInstaReelUrl())));
                }

            }else {
                goToNoInternetActivity();
            }
        }else   if (object instanceof InstaReelType) {
            searchText.getText().clear();
            selectedInstaReelType= (InstaReelType) object;
            if(((InstaReelType) object).getId()==0){
                setAdapter(instaReelItemList);
                pageFirstTimeLaunched=0;
            }else{
                getInstGramItems((InstaReelType) object);
            }

        }
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof InstareelResponse) {
             if(pageFirstTimeLaunched==0){
                 sortlist(((InstareelResponse) object).getInstaReelItems(), ((InstareelResponse) object).getInstaReelTypes());
             }else{
                 sortTypeList(((InstareelResponse) object).getInstaReelItems(), ((InstareelResponse) object).getInstaReelTypes());
             }


        }
    }

    private void sortTypeList(ArrayList<InstaReelItem> instaReelItems, ArrayList<InstaReelType> instaReelTypes) {
       ArrayList<InstaReelItem> instaReelItemArrayList=new ArrayList<>();
        for (InstaReelItem instaReelItem : instaReelItems
        ) {
            for (InstaReelType instaReelType : instaReelTypes
            ) {
                if (instaReelItem.getInstaReelTypeId().equals(instaReelType.getId())) {
                    instaReelItem.setSubtitle(instaReelType.getName());
                    instaReelItemArrayList.add(instaReelItem);
                }
            }
        }
        setAdapter(instaReelItemArrayList);
    }

    private void sortlist(ArrayList<InstaReelItem> instaReelItems, ArrayList<InstaReelType> instaReelTypes) {

        for (InstaReelItem instaReelItem : instaReelItems
        ) {
            for (InstaReelType instaReelType : instaReelTypes
            ) {
                if (instaReelItem.getInstaReelTypeId().equals(instaReelType.getId())) {
                    instaReelItem.setSubtitle(instaReelType.getName());
                    instaReelItemList.add(instaReelItem);
                }
            }
        }

            this.instaReelTypes=new ArrayList<>();
            this.instaReelTypes.add(getAllInstaReelType());
            this.instaReelTypes.addAll(instaReelTypes);
            createMapOfStreamItems(this.instaReelTypes, (ArrayList<InstaReelItem>) instaReelItemList);


    }

    private void setAdapter(List<InstaReelItem> instaReelItemList) {

        instaReelAdapter = new InstaReelAdapter((ArrayList<InstaReelItem>) instaReelItemList, V2InstagramReelsActivity.this, this);
        instaReelRv.setAdapter(instaReelAdapter);

    }

    private void createMapOfStreamItems(ArrayList<InstaReelType> instaReelTypes, ArrayList<InstaReelItem> instaReelItems) {
        for (InstaReelType instaReelType : instaReelTypes) {
            map.put(instaReelType, getStreamItems(instaReelType, instaReelItems));
        }


     setAdapter(instaReelTypes);

    }

    private void setAdapter(ArrayList<InstaReelType> instaReelTypes) {

        instaReelTypeAdapter = new InstaReelTypeAdapter(instaReelTypes, V2InstagramReelsActivity.this, this);
        instaReelTypeRecyclerView.setAdapter(instaReelTypeAdapter);
        setAdapter(instaReelItemList);



    }

    private ArrayList<InstaReelItem> getStreamItems(InstaReelType instaReelType, ArrayList<InstaReelItem> instaReelItems) {
        ArrayList<InstaReelItem> instaReelItemArrayList = new ArrayList<>();
        if (instaReelType.getId() != 0) {
            for (InstaReelItem instaReelItem : instaReelItems
            ) {
                if (instaReelItem.getInstaReelTypeId().equals(instaReelType.getId())) {
                    instaReelItemArrayList.add(instaReelItem);
                }
            }
        }else{
            return instaReelItems;
        }
        return instaReelItemArrayList;

    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_insta_reel);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("title")) {
                title = (String) bundle.get("title");
            }
            if(bundle.containsKey("information")){
                information=(String) bundle.get("information");
            }
        }
        initialize(true);

    }

    private void initialize(boolean b) {
        backIv = findViewById(R.id.backIv);
        instaReelRv = findViewById(R.id.instaReelRv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        instaReelRv.setLayoutManager(layoutManager);
        titleTv = findViewById(R.id.title);
        searchText = findViewById(R.id.searchET);

        instaReelTypeRecyclerView = findViewById(R.id.instaReelTypes);
        instaReelTypeRecyclerView.canScrollVertically(View.SCROLL_AXIS_HORIZONTAL);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        instaReelTypeRecyclerView.setLayoutManager(layoutManager1);

        backIv.setOnClickListener(v -> onBackPressed());

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        } else {
            titleTv.setText("Instagram Reels");
        }
        findViewById(R.id.failureReasonsIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.showInformationPopUp(V2InstagramReelsActivity.this,information,title);

            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getInstGramItems();
    }

    private void filter(CharSequence s) {
        List<InstaReelItem> filteredList = new ArrayList<>();
        if (!TextUtils.isEmpty(s)) {
            for (InstaReelItem instaReelItem : instaReelAdapter.getInstaReelItems()
            ) {
                if (instaReelItem.getTitle().toLowerCase(Locale.ROOT).contains(s.toString().toLowerCase(Locale.ROOT))) {
                    filteredList.add(instaReelItem);
                }
            }


            if (instaReelAdapter != null) {
               instaReelAdapter.setInstaReelItems((ArrayList<InstaReelItem>) filteredList);
                instaReelAdapter.notifyDataSetChanged();
            }
        } else {
            if (instaReelAdapter != null) {
                if(selectedInstaReelType.getId()!=0){
                    getInstGramItems(selectedInstaReelType);
                }else{
                    instaReelAdapter.setInstaReelItems((ArrayList<InstaReelItem>) instaReelItemList);
                    instaReelAdapter.notifyDataSetChanged();
                }

            }
        }

    }

    private void getInstGramItems() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_INSTAGRAM_RESPONSE;

            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, InstareelResponse.class, Request.Method.GET, url);
        } else {
            goToNoInternetActivity();
        }
    }

    private void getInstGramItems(InstaReelType instaReelType) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            pageFirstTimeLaunched=1;
            String url = WingaConstants.GET_INSTAGRAM_RESPONSE+"/"+instaReelType.getId();

            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, InstareelResponse.class, Request.Method.GET, url);
        } else {
            goToNoInternetActivity();
        }
    }




    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);

    }
}
