package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.UserReferralItem;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class InvitesRecyclerAdapter extends RecyclerView.Adapter<InvitesRecyclerAdapter.InvitesItemViewHolder> {

    private ArrayList<UserReferralItem> mInvitesList;
    private Context mContext;

    public InvitesRecyclerAdapter(Context context , ArrayList<UserReferralItem> invitesList) {
        mInvitesList = invitesList;
        mContext =context;
    }

    @Override
    public InvitesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invite_lay, parent, false);

        return new InvitesItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvitesItemViewHolder holder, int position) {
        UserReferralItem inviteModel = mInvitesList.get(position);
        holder.shortNameTv.setText(!TextUtils.isEmpty(inviteModel.getName()) ? Utils.GetShortName(inviteModel.getName()) : "");
        holder.nameTv.setText(!TextUtils.isEmpty(inviteModel.getName()) ? inviteModel.getName() : "");
        holder.mobTv.setText(!TextUtils.isEmpty(inviteModel.getMobile()) ? inviteModel.getMobile() : "");
        String amountEarned = "";
        if(inviteModel.getEarnedPoint()!=0){
            amountEarned= String.valueOf(inviteModel.getEarnedPoint());
            holder.pointsTv.setText(amountEarned+" points");
        }
       else if(inviteModel.getEarnedAmt()!=null) {
            amountEarned = inviteModel.getEarnedAmt();
            if(amountEarned.endsWith(".0")){
                amountEarned = amountEarned.replace(".0","");
            }
            holder.pointsTv.setText(!TextUtils.isEmpty(inviteModel.getEarnedAmt()) ? mContext.getString(R.string.rs) +" "+amountEarned  : "");
        }


    }

    @Override
    public int getItemCount() {
        return mInvitesList.size();
    }

    public class InvitesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView shortNameTv, nameTv, mobTv, pointsTv;

        public InvitesItemViewHolder(View view) {
            super(view);
            shortNameTv =  view.findViewById(R.id.shortNameTv);
            nameTv =  view.findViewById(R.id.nameTv);
            mobTv =  view.findViewById(R.id.mobTv);
            pointsTv =  view.findViewById(R.id.pointsTv);
        }
    }

}

