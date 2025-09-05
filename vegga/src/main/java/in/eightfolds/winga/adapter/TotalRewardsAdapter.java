package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.PrizeWin;

public class TotalRewardsAdapter extends RecyclerView.Adapter<TotalRewardsAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private ArrayList<PrizeWin> mHistoryList;
    private String status;
    private Context context;
    private OnEventListener onEventListener;

    public TotalRewardsAdapter(Context context, ArrayList<PrizeWin> historyList, String status, OnEventListener onEventListener) {
        mHistoryList = historyList;
        this.status = status;
        this.context = context;
        this.onEventListener = onEventListener;
    }

    @Override
    public TotalRewardsAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reward_lay, parent, false);

        return new TotalRewardsAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TotalRewardsAdapter.OptionItemsViewHolder holder, int position) {
        PrizeWin history = mHistoryList.get(position);

        try {
           // history.setTitle("Orange tall water bottle");



            if (!TextUtils.isEmpty(history.getTitle())) {

                String title = history.getTitle();
                int length = title.length();

                if(length <= 18){
                    holder.giftTv.setTextSize(14);
                }else if( length <12){
                    holder.giftTv.setTextSize(12);
                }else{
                    holder.giftTv.setTextSize(12);
                    title = title.substring(0,20) + "..";
                }

                holder.giftTv.setText(title);
            }
            if(history.getAmount()!=0.0){
                holder.pointsOrAmount.setText(context.getString(R.string.rs) +String.valueOf(history.getAmount() )+ " /-");
            }else if(history.getPoints()!=0){
                holder.pointsOrAmount.setText(String.valueOf(history.getPoints())+" pts");
            }


            if(!TextUtils.isEmpty(history.getDesc())){
                holder.youWonTv.setText(history.getDesc());
            }

           /* if(position % 3 == 0){
                //success
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_success);
            }else if(position % 3 == 1 ){
                //failure
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_failure);
            }else if(position % 3 == 2){
                //Pending
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_green);
            }*/

          // history.setStatus(3);
            if(history.getStatus() == 3 || history.getStatus()==-10 || history.getStatus()==1){
                //success
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_green);
            }else if(history.getStatus() == -1 || history.getStatus()==-2){
                //failure
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_failure);
            }else if(history.getStatus() ==0 ){
                //Pending
                holder.prizeWinLoseIV.setImageResource(R.drawable.rewards_yellow);
            }


            holder.mainLL.setTag(history);
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
        if (v.getTag() != null && v.getTag() instanceof PrizeWin) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {

        private TextView giftTv, youWonTv,pointsOrAmount;
        private FrameLayout mainLL;
        private ImageView prizeWinLoseIV;

        public OptionItemsViewHolder(View view) {
            super(view);
            giftTv = view.findViewById(R.id.giftTv);
            mainLL = view.findViewById(R.id.mainLL);
            youWonTv = view.findViewById(R.id.youWonTv);
            prizeWinLoseIV = view.findViewById(R.id.prizeWinLoseIV);
            pointsOrAmount=view.findViewById(R.id.poinstOrAmountTv);
        }
    }
}

