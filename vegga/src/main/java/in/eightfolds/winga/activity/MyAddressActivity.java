package in.eightfolds.winga.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Arrays;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.AddressRecyclerAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserAddress;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 27-Apr-18.
 */

public class MyAddressActivity extends BaseActivity implements VolleyResultCallBack, OnEventListener {

    private static final String TAG = MyAddressActivity.class.getSimpleName();
    private RecyclerView addressRecyclerView;

    private ArrayList<UserAddress> userAddressArrayList;
    private int ADD_ADDRESS_REQUEST_CODE = 100;

    private String UPDATE_PRIMARY_USER_ADDRESS_URL = "";
    private String DELETE_USER_ADDRESS_URL = "";
    private LinearLayout noItemsLL,topLL;
    private TextView addAddressTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_myaddress);
        initialize();


    }

    private void initialize() {

        setHeader(getString(R.string.my_addresses), true);

        addressRecyclerView = findViewById(R.id.addressRecyclerView);
        addAddressTv = findViewById(R.id.addAddressTv);
        noItemsLL = findViewById(R.id.noItemsLL);
        topLL = findViewById(R.id.topLL);

        ((ImageView) findViewById(R.id.headerRightIconIv)).setOnClickListener(this);

        getUserAddresses();
        makeAddAddressSpannable();
    }


    private void makeAddAddressSpannable() {
        SpannableString addAddressSpan = makeLinkSpan(getString(R.string.add_address), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddAdress();
            }
        });

        addAddressSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, addAddressSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        addAddressSpan.setSpan(iss, 0, addAddressSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        addAddressTv.setText(addAddressSpan);
        makeLinksFocusable(addAddressTv);
    }
    private void setadapter() {
      //  LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
      //  addressRecyclerView.setLayoutAnimation(animation);


        if(userAddressArrayList .size() >0) {


            noItemsLL.setVisibility(View.GONE);
            AddressRecyclerAdapter addressRecyclerAdapter = new AddressRecyclerAdapter(this, userAddressArrayList, this);
            addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            addressRecyclerView.setAdapter(addressRecyclerAdapter);
            addressRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            addressRecyclerView.setVisibility(View.VISIBLE);
        }else{

            noItemsLL.setVisibility(View.VISIBLE);
        }
    }

    private void getUserAddresses() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            EightfoldsVolley.getInstance().makingStringRequest(this, UserAddress[].class, Request.Method.GET, Constants.GET_USER_ADDRESSES_URL);

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.headerRightIconIv) {
            goToAddAdress();
        }
    }

    private void goToAddAdress(){
        Intent intent = new Intent(this, AddNewAddressActivity.class);
        startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == ADD_ADDRESS_REQUEST_CODE) {
                if (data != null && data.getExtras() != null && data.getExtras().containsKey(Constants.ADDRESS)) {
                    Object object =  data.getExtras().get(Constants.ADDRESS);
                    UserAddress[] addresses = null;
                    if(object instanceof Object[]){
                        Object[] addressObjects = (Object[]) object;
                        ArrayList<UserAddress> userAddressArrayList = new ArrayList<>();
                        for (Object obj : addressObjects){
                            if(obj instanceof UserAddress){
                                userAddressArrayList.add((UserAddress) obj);
                            }
                        }
                        refreshAddresses(userAddressArrayList);

                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void refreshAddresses(UserAddress[] userAddresses) {
        userAddressArrayList = new ArrayList<>();
        userAddressArrayList.addAll(Arrays.asList(userAddresses));

        setadapter();
    }
    private void refreshAddresses(ArrayList<UserAddress> userAddresses) {
        userAddressArrayList = new ArrayList<>();
        userAddressArrayList.addAll(userAddresses);
        setadapter();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {

        if (object instanceof UserAddress[]) {
            UserAddress[] userAddresses = (UserAddress[]) object;

            if (requestType.equalsIgnoreCase(UPDATE_PRIMARY_USER_ADDRESS_URL)) {
                onVolleyErrorListener(getString(R.string.updated_primary_address_successfully));
            } else if (requestType.equalsIgnoreCase(DELETE_USER_ADDRESS_URL)) {
                onVolleyErrorListener(getString(R.string.deleted_address_successfully));
            }
            refreshAddresses(userAddresses);


            if (userAddresses.length <= 0) {

                noItemsLL.setVisibility(View.VISIBLE);
                addressRecyclerView.setVisibility(View.GONE);

            }else{

                noItemsLL.setVisibility(View.GONE);
                addressRecyclerView.setVisibility(View.VISIBLE);
            }

            Intent profileRefreshIntent = new Intent();
            profileRefreshIntent.setAction(Constants.PROFILE_REFRESH_ACTION);
            sendBroadcast(profileRefreshIntent);
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    private void updatePrimaryAddress(UserAddress userAddress) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            UPDATE_PRIMARY_USER_ADDRESS_URL = Constants.UPDATE_PRIMARY_USER_ADDRESS_URL;
            UPDATE_PRIMARY_USER_ADDRESS_URL = UPDATE_PRIMARY_USER_ADDRESS_URL.replace("{addressId}", userAddress.getAddressId() + "");
            //primary flag -1, default flag - 0
            UPDATE_PRIMARY_USER_ADDRESS_URL = UPDATE_PRIMARY_USER_ADDRESS_URL.replace("{addressFlag}", "1");
            EightfoldsVolley.getInstance().makingStringRequest(this, UserAddress[].class, Request.Method.PUT, UPDATE_PRIMARY_USER_ADDRESS_URL);
        }
    }

    private void deleteAddress(UserAddress userAddress) {

        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {
            EightfoldsVolley.getInstance().showProgress(this);
            DELETE_USER_ADDRESS_URL = Constants.DELETE_USER_ADDRESS_URL;
            DELETE_USER_ADDRESS_URL = DELETE_USER_ADDRESS_URL.replace("{addressId}", userAddress.getAddressId() + "");
            EightfoldsVolley.getInstance().makingStringRequest(this, UserAddress[].class, Request.Method.DELETE, DELETE_USER_ADDRESS_URL);
        }
    }

    private void editAddress(UserAddress userAddress) {
        Intent intent = new Intent(this, AddNewAddressActivity.class);
        intent.putExtra(Constants.DATA, userAddress);
        intent.putExtra("isForEdit", true);
        startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onEventListener(int type, Object object) {
        UserAddress userAddress = (UserAddress) object;

        if (object instanceof UserAddress) {
            if (type == R.id.setAsPrimary) {
                updatePrimaryAddress(userAddress);
            } else if (type == R.id.delete)

                MyDialog.showTwoButtonDialog(this, userAddress, this, getString(R.string.alert), getString(R.string.are_u_sure_to_delete_address), getString(R.string.yes), getString(R.string.no));

        }
        else if (type == R.id.edit) {
            editAddress(userAddress);

        }
        else if (type == R.id.yesTv) {
            deleteAddress(userAddress);
        }
            }



    @Override
    protected void onStart() {
        super.onStart();
        Logg.v(TAG," && Address Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logg.v(TAG," && Address Stop");
    }
}




    /* private ArrayList<Address> getAddresses() {
            ArrayList<Address> addresses = new ArrayList<>();
            Address address1 = new Address();
            address1.setAddressId(1);
            address1.setAddressType("Home");
            address1.setDefault(true);
            address1.setAddress("#301, H.No.12-34-1, Plot No: 317, DLKR House, Kamalapuri Colony, Banjara Hills, Hyderabad TG- 500012");
            addresses.add(address1);

            Address address2 = new Address();
            address2.setAddressId(2);
            address2.setAddressType("Office");
            address2.setDefault(false);
            address2.setAddress("#301, H.No.12-34-1, Plot No: 317, DLKR House, Kamalapuri Colony, Banjara Hills, Hyderabad TG- 500012");
            addresses.add(address2);

            Address address3 = new Address();
            address3.setAddressId(3);
            address3.setAddressType("Hometown");
            address3.setDefault(false);
            address3.setAddress("#301, H.No.12-34-1, Plot No: 317, DLKR House, Kamalapuri Colony, Banjara Hills, Hyderabad TG- 500012");
            addresses.add(address3);

            if (address != null && !TextUtils.isEmpty(address.getAddress())) {
                addresses.add(address);
            }

            return addresses;
        }
    */
