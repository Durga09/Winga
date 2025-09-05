package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.utils.DateTime;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.LoyalityPointHistory;

public class LoyaltyPointsRecyclerAdapter extends RecyclerView.Adapter<LoyaltyPointsRecyclerAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private ArrayList<LoyalityPointHistory> mHistoryList;
    private String status;
    private Context context;

    public LoyaltyPointsRecyclerAdapter(Context context, ArrayList<LoyalityPointHistory> historyList, String status) {
        mHistoryList = historyList;
        this.status = status;
        this.context = context;
    }

    @Override
    public LoyaltyPointsRecyclerAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loyalty_points_history_lay, parent, false);

        return new LoyaltyPointsRecyclerAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoyaltyPointsRecyclerAdapter.OptionItemsViewHolder holder, int position) {
        LoyalityPointHistory history = mHistoryList.get(position);

        try {

            if (!TextUtils.isEmpty(history.getDate())) {
                String date = history.getDate();
                String formattedDate = DateTime.getDateTime(date,"yyyy-MM-dd","MMM-dd, yyyy");
                holder.dateTv.setText(formattedDate);
            }


            holder.loyaltyPointsTv.setText(history.getPointsEarned() + "");


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

    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {


        private TextView dateTv, loyaltyPointsTv;

        public OptionItemsViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            loyaltyPointsTv = view.findViewById(R.id.loyaltyPointsTv);
        }
    }
}