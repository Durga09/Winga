package in.eightfolds.winga.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.SessionWinnersListActivity;
import in.eightfolds.winga.model.CategoryGameWinner;
import in.eightfolds.winga.model.GameSessionWinnerResponse;
import in.eightfolds.winga.model.Winner;
import in.eightfolds.winga.utils.Constants;

public class RecentWinnersRecyclerAdapter extends RecyclerView.Adapter<RecentWinnersRecyclerAdapter.RecentWinnersViewHolder> implements View.OnClickListener {

    private ArrayList<CategoryGameWinner> mWinnersList;
    private GameSessionWinnerResponse gameSessionWinnerResponse;

    private Context mContext;

    public RecentWinnersRecyclerAdapter(Context context, GameSessionWinnerResponse gameSessionWinnerResponse) {
        mWinnersList = (ArrayList<CategoryGameWinner>) gameSessionWinnerResponse.getWinners();
        mContext = context;
        this.gameSessionWinnerResponse = gameSessionWinnerResponse;
    }

    @Override
    public RecentWinnersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.winner_item_lay, parent, false);

        return new RecentWinnersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentWinnersViewHolder holder, int position) {
        holder.winnerCountLL.setOnClickListener(this);
        holder.winnerCountLL.setTag(gameSessionWinnerResponse.getCgsId());
        if (position == getItemCount() - 1) {
            int totalItemsCount = gameSessionWinnerResponse.getWinners().size();
            if(gameSessionWinnerResponse.getTotalWinner() != null) {
                holder.winnersCountTv.setText(Integer.toString(gameSessionWinnerResponse.getTotalWinner()));
            }else{
                holder.winnersCountTv.setText(Integer.toString(totalItemsCount));

            }
            holder.winnerCountLL.setVisibility(View.VISIBLE);
        } else {

            CategoryGameWinner winner = mWinnersList.get(position);

            if (winner.getProfilePicId() != null) {
                Glide.with(mContext)
                        .load(EightfoldsUtils.getInstance().getImageFullPath(winner.getProfilePicId(), Constants.FILE_URL))
                        .placeholder(R.drawable.ic_user_filled)
                        .error(R.drawable.ic_user_filled)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.winnerIv);
            }else{
                holder.winnerIv.setImageResource(R.drawable.ic_user_filled);
            }
        }
    }

    @Override
    public int getItemCount() {

        if (mWinnersList.size() < 7) {
            return mWinnersList.size() + 1;
        } else {
            //even greater than 7, show only 7 items
            return 7;
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.winnerCountLL) {
            Long tag = (Long) v.getTag();
            Intent intent = new Intent(mContext, SessionWinnersListActivity.class);
            intent.putExtra("gSessionId", tag);
            mContext.startActivity(intent);
        }
    }

    public class RecentWinnersViewHolder extends RecyclerView.ViewHolder {
        public ImageView winnerIv;
        private TextView winnersCountTv;
        private LinearLayout winnerCountLL;

        public RecentWinnersViewHolder(View view) {
            super(view);
            winnerIv = view.findViewById(R.id.winnerIv);
            winnersCountTv = view.findViewById(R.id.winnersCountTv);
            winnerCountLL = view.findViewById(R.id.winnerCountLL);

        }
    }

}
