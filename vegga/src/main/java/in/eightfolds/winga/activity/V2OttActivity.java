package in.eightfolds.winga.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.v2.adapter.StreamItemAdapter;
import in.eightfolds.winga.v2.adapter.StreamingTypeAdapter;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.CommonServerResponse;
import in.eightfolds.winga.v2.model.StreamingItem;
import in.eightfolds.winga.v2.model.StreamingType;
import in.eightfolds.winga.v2.model.StreamingTypeAndItem;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

public class V2OttActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    ImageView backIv;

    ArrayList<StreamingType> streamingTypes;
    Map<StreamingType, ArrayList<StreamingItem>> map = new HashMap<>();

    private RecyclerView ottTypeRecyclerView;
    private RecyclerView OttItemRecyclerView;
    private StreamingTypeAdapter streamingTypeAdapter;
    private StreamItemAdapter streamItemAdapter;
    String title,information;
    TextView titleTv;

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {

        if (object instanceof StreamingType) {
            setItemAdapter(map.get(object));
        }
        if(object instanceof  StreamingItem){
             getWinOtt((StreamingItem) object);
        }

    }

    private void getWinOtt(StreamingItem streamingItem) {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_STREAMING_WIN + streamingItem.getNumber();
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, StreamingWinResponse.class, Request.Method.GET, url);
        }

    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof StreamingTypeAndItem) {
            streamingTypes = new ArrayList<>();
            streamingTypes.add(getAllStreamingType());
            streamingTypes.addAll(((StreamingTypeAndItem) object).getStreamingTypes());
            createMapOfStreamItems(streamingTypes, ((StreamingTypeAndItem) object).getStreamingItems());
        }
        if(object instanceof  StreamingWinResponse){
            StreamingWinResponse streamingWinResponse = (StreamingWinResponse) object;
            Intent intent = new Intent(this, V2PlayVideoActivity.class);
            intent.putExtra(Constants.DATA, streamingWinResponse);
            startActivity(intent);

        }
        if(object instanceof CommonServerResponse){
            CommonServerResponse commonServerResponse=(CommonServerResponse) object;

            MyDialog.showToast(this, commonServerResponse.getStatusMessage());


        }
    }

    private void createMapOfStreamItems(ArrayList<StreamingType> streamingTypes, ArrayList<StreamingItem> streamingItems) {
        for (StreamingType streamingType : streamingTypes) {
            map.put(streamingType, getStreamItems(streamingType, streamingItems));
        }

        setAdapter(streamingTypes);


    }

    private void setItemAdapter(ArrayList<StreamingItem> streamingItems) {
        streamItemAdapter = new StreamItemAdapter(streamingItems, V2OttActivity.this, this);
        OttItemRecyclerView.setAdapter(streamItemAdapter);
    }

    private void setAdapter(ArrayList<StreamingType> streamingTypes) {

        streamingTypeAdapter = new StreamingTypeAdapter(streamingTypes, V2OttActivity.this, this);
        ottTypeRecyclerView.setAdapter(streamingTypeAdapter);
        setItemAdapter(map.get(streamingTypes.get(0)));


    }

    private ArrayList<StreamingItem> getStreamItems(StreamingType streamingType, ArrayList<StreamingItem> streamingItems) {
        ArrayList<StreamingItem> streamingItems1 = new ArrayList<>();
        if (streamingType.getId() != 0) {
            for (StreamingItem streamingItem : streamingItems
            ) {
                if (streamingItem.getStreamingTypeId().equals(streamingType.getId())) {
                    streamingItems1.add(streamingItem);
                }
            }
            return streamingItems1;
        } else {
            return streamingItems;
        }

    }

    private StreamingType getAllStreamingType() {

        StreamingType streamingType = new StreamingType();
        streamingType.setId(0);
        streamingType.setSelected(true);
        streamingType.setName("All");
        return streamingType;
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_ott);
        Bundle bundle=getIntent().getExtras();
        V2OttActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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

    private void getOttItems() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            String url = WingaConstants.GET_OTT_ACTIVE;
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequestForCommonResponse(this, StreamingTypeAndItem.class, Request.Method.GET, url);
        }else {
            goToNoInternetActivity();
        }
    }


    private void goToNoInternetActivity() {
        Intent intent = new Intent(this, NoInternetActivity.class);

        startActivity(intent);
        finish();
    }

    private void initialize(boolean b) {

        information = EightfoldsUtils.getInstance().getFromSharedPreference(this, WingaConstants.GET_INFO);
        title = EightfoldsUtils.getInstance().getFromSharedPreference(this, WingaConstants.SAVE_TITLE);

        backIv = findViewById(R.id.backIv);

        ottTypeRecyclerView = findViewById(R.id.ottTypes);
        ottTypeRecyclerView.canScrollVertically(View.SCROLL_AXIS_HORIZONTAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ottTypeRecyclerView.setLayoutManager(layoutManager);

        OttItemRecyclerView = findViewById(R.id.ottItems);
        OttItemRecyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        OttItemRecyclerView.setLayoutManager(layoutManager1);
        titleTv=findViewById(R.id.title);
        if(!TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }

        findViewById(R.id.failureReasonsIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.showInformationPopUp(V2OttActivity.this,information,title);

            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getOttItems();
    }
}
