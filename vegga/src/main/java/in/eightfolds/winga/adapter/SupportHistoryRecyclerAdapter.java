package in.eightfolds.winga.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.utils.DateTime;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.SupportRequestDetailsActivity;
import in.eightfolds.winga.model.SupportRequest;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ConstantsManager;

public class SupportHistoryRecyclerAdapter extends RecyclerView.Adapter<SupportHistoryRecyclerAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private static final String TAG = SupportHistoryRecyclerAdapter.class.getSimpleName();
    private ArrayList<SupportRequest> mHistoryList;
    private String status;
    private Context context;


    public SupportHistoryRecyclerAdapter(Context context, ArrayList<SupportRequest> historyList, String status) {
        mHistoryList = historyList;
        this.status = status;
        this.context = context;
    }

    @Override
    public SupportHistoryRecyclerAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_support_ticket, parent, false);

        return new SupportHistoryRecyclerAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SupportHistoryRecyclerAdapter.OptionItemsViewHolder holder, int position) {
        SupportRequest supportTicket = mHistoryList.get(position);

        try {

            if (!TextUtils.isEmpty(supportTicket.getCreatedTime())) {
                String date = supportTicket.getCreatedTime();
                String formattedDate = DateTime.getDateTime(date, "yyyy-MM-dd HH:mm:ss", "MMM-dd, yyyy.");
                holder.dateTv.setText(formattedDate);
            }


            if (!TextUtils.isEmpty(supportTicket.getSupportNumber()))
                holder.ticketNumberTv.setText(supportTicket.getSupportNumber());

            String supportType = ConstantsManager.getSupportTypeFromId(context, supportTicket.getSupportTypeId());
            if (!TextUtils.isEmpty(supportType)) {
                holder.ticketTypeTv.setText(supportType);
            } else {
                holder.ticketTypeTv.setVisibility(View.GONE);
            }


            holder.ticketStatusTv.setText(ConstantsManager.getSupportState(context,supportTicket.getState()));
                    //Utils.getSupportStateFromId(context, supportTicket.getState()));

            holder.mainLL.setTag(supportTicket);
            holder.mainLL.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.mainLL){
            SupportRequest supportRequest = (SupportRequest) v.getTag();
            Intent intent = new Intent(context,SupportRequestDetailsActivity.class);
            intent.putExtra(Constants.DATA,supportRequest);
            context.startActivity(intent);
        }
    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {


        private TextView ticketNumberTv, dateTv, ticketTypeTv, ticketStatusTv;
        private LinearLayout mainLL;

        public OptionItemsViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            ticketNumberTv = view.findViewById(R.id.ticketNumberTv);
            ticketTypeTv = view.findViewById(R.id.ticketTypeTv);
            ticketStatusTv = view.findViewById(R.id.ticketStatusTv);
            mainLL = view.findViewById(R.id.mainLL);
        }
    }
}