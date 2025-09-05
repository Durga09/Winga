package in.eightfolds.winga.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.GameSessionPrize;

/**
 * Created by Swapnika on 20-Apr-18.
 */

public class GiftItemsRecyclerAdapter extends RecyclerView.Adapter<GiftItemsRecyclerAdapter.GiftItemsViewHolder> {

    private ArrayList<GameSessionPrize> mGiftsList;

    public GiftItemsRecyclerAdapter( ArrayList<GameSessionPrize> giftsList) {
        mGiftsList = giftsList;
    }

    @Override
    public GiftItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gift_item_lay, parent, false);

        return new GiftItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GiftItemsViewHolder holder, int position) {
        GameSessionPrize gameSessionPrize = mGiftsList.get(position);

        holder.giftDescTv.setText( !TextUtils.isEmpty(gameSessionPrize.getDesc())?gameSessionPrize.getDesc(): "");
    }

    @Override
    public int getItemCount() {
        return mGiftsList.size();
    }

    public class GiftItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView giftDescTv;

        public GiftItemsViewHolder(View view) {
            super(view);
            giftDescTv =  view.findViewById(R.id.giftDescTv);
        }
    }

}
