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
import in.eightfolds.winga.model.GameHistoryItem;
import in.eightfolds.winga.utils.Logg;

public class PlayHistoryRecyclerAdapter extends RecyclerView.Adapter<PlayHistoryRecyclerAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private static final String TAG = PlayHistoryRecyclerAdapter.class.getSimpleName();
    private ArrayList<GameHistoryItem> mHistoryList;
    private String status;
    private Context context;


    public PlayHistoryRecyclerAdapter(Context context, ArrayList<GameHistoryItem> historyList, String status) {
        mHistoryList = historyList;
        this.status = status;
        this.context = context;
    }

    @Override
    public PlayHistoryRecyclerAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_history_lay, parent, false);

        return new PlayHistoryRecyclerAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayHistoryRecyclerAdapter.OptionItemsViewHolder holder, int position) {
        GameHistoryItem history = mHistoryList.get(position);

        try {

            if (!TextUtils.isEmpty(history.getDate())) {
                String date = history.getDate();
                String formattedDate = DateTime.getDateTime(date, "yyyy-MM-dd", "MMM-dd, yyyy.");
                holder.dateTv.setText(formattedDate);
            }


            if (!TextUtils.isEmpty(history.getPointsWin()))
                holder.loyaltyPointsTv.setText(history.getPointsWin());

            if (!TextUtils.isEmpty(history.getNoOfGamePlayed() + ""))
                holder.gamesPlayedTv.setText(history.getNoOfGamePlayed());

            if (!TextUtils.isEmpty(history.getNoOfWins() + ""))
                holder.gamesWonTv.setText(history.getNoOfWins());


            int presentMonth = DateTime.getMonthFromDate(history.getDate());

            int previousItemMonth = 0;
            if(position !=0){
                previousItemMonth = DateTime.getMonthFromDate(mHistoryList.get(position - 1).getDate());
            }

            Logg.v(TAG, " Present Month >> "+ presentMonth +" - Previous Month >> "+previousItemMonth);
            if (previousItemMonth != presentMonth) {
                String formattedMonth = DateTime.getDateTime(history.getDate(), "yyyy-MM-dd", "MMM-yyyy.");
                holder.monthTv.setText(formattedMonth);
                holder.monthTv.setVisibility(View.VISIBLE);
            }else{
                holder.monthTv.setVisibility(View.GONE);
            }


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


        private TextView monthTv, dateTv, gamesPlayedTv, gamesWonTv, loyaltyPointsTv;

        public OptionItemsViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            monthTv = view.findViewById(R.id.monthTv);
            gamesPlayedTv = view.findViewById(R.id.gamesPlayedTv);
            gamesWonTv = view.findViewById(R.id.gamesWonTv);
            loyaltyPointsTv = view.findViewById(R.id.loyaltyPointsTv);
        }
    }
}