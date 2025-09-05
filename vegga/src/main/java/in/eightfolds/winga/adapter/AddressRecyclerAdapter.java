package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.UserAddress;
import in.eightfolds.winga.utils.ConstantsManager;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 27-Apr-18.
 */

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder> {


    private ArrayList<UserAddress> mAddressesList;
    private OnEventListener onEventListener;
    private Context mContext;

    public AddressRecyclerAdapter(Context context, ArrayList<UserAddress> addressesList, OnEventListener onEventListener) {
        mAddressesList = addressesList;
        this.onEventListener = onEventListener;
        mContext = context;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item_lay, parent, false);

        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddressViewHolder holder, int position) {
        final UserAddress address = mAddressesList.get(position);
        holder.addressTypeTv.setText(ConstantsManager.getAddressTypeFromId(mContext, address.getAdreTypeId()));

        String addressText = "";

        if (!TextUtils.isEmpty(address.getAddress2())) {
            addressText += address.getAddress2();
        }
        if (!TextUtils.isEmpty(address.getAddress1())) {
            addressText += ", " + address.getAddress1();
        }
        if (!TextUtils.isEmpty(address.getCity())) {
            addressText += ", " + address.getCity();
        }

        if (address.getStateId() != 0) {
            addressText += ", " + Utils.getStateNameFromId(mContext,address.getStateId().intValue());
        }

        if (!TextUtils.isEmpty(address.getLandmark())) {
            addressText += ", Landmark: " + address.getLandmark();
        }
        if (!TextUtils.isEmpty(address.getPincode())) {
            addressText += ", Pincode: " + address.getPincode();
        }

        holder.addressTv.setText(addressText);
        holder.defaultTv.setText((address.isAddrIsDefault()) ? "(" + mContext.getString(R.string.default_string) + ")" : "");

        holder.menuIv.setTag(address);
        holder.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.menuIv);
                //inflating menu from xml resource
                if (address.isAddrIsDefault()) {
                    popup.inflate(R.menu.options_primary_menu);
                } else {
                    popup.inflate(R.menu.options_menu);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        UserAddress address1 = (UserAddress) holder.menuIv.getTag();
                        int itemId = item.getItemId();

                        if (itemId == R.id.setAsPrimary) {
                            // handle menu1 click
                            onEventListener.onEventListener(R.id.setAsPrimary, address1);
                        } else if (itemId == R.id.delete) {
                            // handle menu2 click
                            onEventListener.onEventListener(R.id.delete, address1);
                        } else if (itemId == R.id.edit) {
                            onEventListener.onEventListener(R.id.edit, address1);
                            // handle menu3 click
                        }

                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressesList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTypeTv, defaultTv, addressTv;
        public ImageView menuIv;

        public AddressViewHolder(View view) {
            super(view);
            addressTypeTv = view.findViewById(R.id.addressTypeTv);
            defaultTv = view.findViewById(R.id.defaultTv);
            addressTv = view.findViewById(R.id.addressTv);
            menuIv = (ImageView) view.findViewById(R.id.menuIv);
        }
    }
}
