package in.eightfolds.winga.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.Winner;
import in.eightfolds.winga.utils.Constants;

public class WinnersListAdapter extends RecyclerView.Adapter<WinnersListAdapter.WinnersViewHolder> {

    private ArrayList<Winner> mWinners;
    private Context mContext;


    public WinnersListAdapter(Context context, ArrayList<Winner> winners) {
        mWinners = winners;
        mContext = context;
    }

    @NonNull
    @Override
    public WinnersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_winner, parent, false);

        return new WinnersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WinnersViewHolder holder, int position) {
        try {
            Winner winner = mWinners.get(position);
            holder.nameTv.setText(!TextUtils.isEmpty(winner.getUserName()) ? winner.getUserName() : "");

            String prize = "";

            if (winner.getPrizeType() != null && winner.getPrizeType() == 3) {
                if (winner.getAmount() != null) {
                    String amount = winner.getAmount() + "";

                    if (amount.contains(".0")) {
                        amount = amount.replace(".0", "");
                    }


                    prize = mContext.getString(R.string.rs) + "" + amount + "";
                }
            } else if (winner.getPrizeType() != null && winner.getPrizeType() == 4) {
                prize = winner.getPoints() + " points" ;
            } else if (!TextUtils.isEmpty(winner.getPrizeName())) {
                prize = winner.getPrizeName();
            }


            holder.prizeTv.setText(prize);
            holder.stateTv.setText(!TextUtils.isEmpty(winner.getStateName()) ? winner.getStateName() : "");

            //  String formattedDate = DateTime.getDateFromUTC(winner.getGameDate(), "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");

            // holder.dateTv.setText(!TextUtils.isEmpty(formattedDate) ? formattedDate : "");

            if (winner.getProfilePicId() != null) {
                Glide.with(mContext)
                        .load(EightfoldsUtils.getInstance().getImageFullPath(winner.getProfilePicId(), Constants.FILE_URL))
                        .placeholder(R.drawable.ic_user_filled)
                        .error(R.drawable.ic_user_filled)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.winnerIv);
            } else {
                holder.winnerIv.setImageResource(R.drawable.ic_user_filled);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mWinners.size();
    }


    public void refreshData(List<Winner> winners) {
        mWinners.addAll(winners);
        notifyDataSetChanged();

    }

    public class WinnersViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView winnerIv;
        private TextView nameTv, prizeTv, stateTv, dateTv;

        public WinnersViewHolder(View view) {
            super(view);
            winnerIv = view.findViewById(R.id.winnerIv);
            nameTv = view.findViewById(R.id.nameTv);
            prizeTv = view.findViewById(R.id.prizeTv);
            stateTv = view.findViewById(R.id.stateTv);
            dateTv = view.findViewById(R.id.dateTv);
        }
    }


}
