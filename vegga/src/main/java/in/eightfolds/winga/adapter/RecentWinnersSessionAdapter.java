package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.CategoryGameWinner;
import in.eightfolds.winga.model.GameSessionWinnerResponse;
import in.eightfolds.winga.model.Winner;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ItemDecorator;
import in.eightfolds.winga.utils.Utils;

public class RecentWinnersSessionAdapter extends RecyclerView.Adapter<RecentWinnersSessionAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private ArrayList<GameSessionWinnerResponse> mHistoryList;
    private Context context;
    private int previousItemMonth = 0;

    private LinearLayoutManager layoutManager;

    public RecentWinnersSessionAdapter(Context context, ArrayList<GameSessionWinnerResponse> historyList) {
        mHistoryList = historyList;
        this.context = context;
    }

    @Override
    public RecentWinnersSessionAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_winners_session_item, parent, false);

        OptionItemsViewHolder holder = new RecentWinnersSessionAdapter.OptionItemsViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecentWinnersSessionAdapter.OptionItemsViewHolder holder, int position) {
        GameSessionWinnerResponse history = mHistoryList.get(position);

        try {

            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.yesterDayWinnersRecyclerView.setLayoutManager(layoutManager);
            ItemDecorator itemDecorator = new ItemDecorator(-12);
            holder.yesterDayWinnersRecyclerView.addItemDecoration(itemDecorator);
            ViewCompat.setNestedScrollingEnabled(holder.yesterDayWinnersRecyclerView, false);

            setGiftsText(history, holder);


            if (history.getWinner() != null && history.getWinner().getCgspId() != null) {
                holder.myProfileLL.setVisibility(View.VISIBLE);
                setMyDetails(history.getWinner(),holder);

            }
            //holder.winnerCountLL.setVisibility(View.VISIBLE);
            setAdapter(history,holder);

            if(mHistoryList.size() > 1){
                holder.sessionNameTv.setVisibility(View.VISIBLE);
                String winnerPubTime = DateTime.getDateFromUTC(history.getWinerPubTime(), "yyyy-MM-dd HH:mm:ss", "hh:mm a");

                holder.sessionNameTv.setText(winnerPubTime);
            }else{
                holder.sessionNameTv.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGiftsText(GameSessionWinnerResponse gameSessionWinnerResponse, OptionItemsViewHolder holder) {
        holder.giftsWinTv.setText("");
        /*if (gameSessionWinnerResponse.getGameSessionPrizes() != null) {
            for (int i = 0; i < gameSessionWinnerResponse.getGameSessionPrizes().size(); i++) {
                if(gameSessionWinnerResponse.getGameSessionPrizes().get(i).getTotalQty() != null) {
                    String count = gameSessionWinnerResponse.getGameSessionPrizes().get(i).getTotalQty() + "";
                    String text = String.format(Utils.getCurrentLocale(), context.getString(R.string.users_win_yesterday_lucky_daw), count,
                            gameSessionWinnerResponse.getGameSessionPrizes().get(i).getTitle()) + "\n" + "\n";
                    SpannableString ss1 = new SpannableString(text);
                    ss1.setSpan(new RelativeSizeSpan(1.2f), 0, count.length(), 0); // set size
                    holder.giftsWinTv.append(ss1);
                }
            }
        }*/

    }

    private void setMyDetails(CategoryGameWinner winner, OptionItemsViewHolder holder) {

        String prize = "";
        if (winner.getPrizeType() != null && winner.getPrizeType() == 3) {
            String amount = winner.getAmount() + "";
            if (amount.contains(".0")) {
                amount = amount.replace(".0", "");
            }

            prize = context.getString(R.string.rs) + "" + amount + "";
        } else if (!TextUtils.isEmpty(winner.getPrizeName())) {
            prize = winner.getPrizeName();
        }


        holder.myGiftTv.setText( String.format(Utils.getCurrentLocale(), context.getString(R.string.you_won_a), prize));

        //holder.myGiftTv.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.you_won_a), winner.getPrizeName()));
        holder.mynameTv.setText(winner.getUserName());
        if (winner.getProfilePicId() != null) {
            Glide.with(context)
                    .load(EightfoldsUtils.getInstance().getImageFullPath(winner.getProfilePicId(), Constants.FILE_URL))
                    .placeholder(R.drawable.ic_user_filled)
                    .error(R.drawable.ic_user_filled)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.myProfilePicIV);
        }
    }

    private int totalItemsCount = 0;

    private void setAdapter(GameSessionWinnerResponse gameSessionWinnerResponse,OptionItemsViewHolder holder) {

        if(gameSessionWinnerResponse.getWinners() != null && gameSessionWinnerResponse.getWinners().size() >0) {
            try {
                totalItemsCount = gameSessionWinnerResponse.getWinners().size();
                RecentWinnersRecyclerAdapter yesterdayWinnersRecyclerAdapter = new RecentWinnersRecyclerAdapter(context, gameSessionWinnerResponse);
                holder.yesterDayWinnersRecyclerView.setAdapter(yesterdayWinnersRecyclerAdapter);
                int postion = layoutManager.findLastCompletelyVisibleItemPosition();
                int remainingItems = totalItemsCount - postion;
                holder.winnersCountTv.setText(Integer.toString(gameSessionWinnerResponse.getTotalWinner()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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


        private TextView giftsWinTv;

        private TextView mynameTv, myGiftTv;
        private ImageView myProfilePicIV;
        private LinearLayout myProfileLL;
        private LinearLayout winnerCountLL;
        private RecyclerView yesterDayWinnersRecyclerView;
        private TextView winnersCountTv, sessionNameTv;

        public OptionItemsViewHolder(View view) {
            super(view);
            giftsWinTv = view.findViewById(R.id.giftsWinTv);
            mynameTv = view.findViewById(R.id.mynameTv);
            myGiftTv = view.findViewById(R.id.myGiftTv);
            myProfilePicIV = view.findViewById(R.id.myProfilePicIV);
            myProfileLL = view.findViewById(R.id.myProfileLL);
            winnerCountLL = view.findViewById(R.id.winnerCountLL);
            yesterDayWinnersRecyclerView = view.findViewById(R.id.yesterDayWinnersRecyclerView);
            winnersCountTv = view.findViewById(R.id.winnersCountTv);
            sessionNameTv = view.findViewById(R.id.sessionNameTv);

        }
    }
}