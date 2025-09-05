package in.eightfolds.winga.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.FilterItemModel;

/**
 * Created by Swapnika on 02-May-18.
 */

public class HistoryFilterItemsRecyclerAdapter extends RecyclerView.Adapter<HistoryFilterItemsRecyclerAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    ArrayList<FilterItemModel> mOptionsList;
    FilterItemModel filterItem;

    public HistoryFilterItemsRecyclerAdapter(ArrayList<FilterItemModel> optionsList) {
        mOptionsList = optionsList;
    }

    @Override
    public HistoryFilterItemsRecyclerAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_filter, parent, false);


        return new HistoryFilterItemsRecyclerAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryFilterItemsRecyclerAdapter.OptionItemsViewHolder holder, int position) {
        filterItem = mOptionsList.get(position);
        holder.filterItemTv.setText(filterItem.getItem());
        holder.filterItemLL.setOnClickListener(this);
        holder.filterItemLL.setTag(position);
        if (filterItem.isSelected()) {
            holder.selectIv.setImageResource(R.drawable.ic_checked);
        } else {
            holder.selectIv.setImageResource(R.drawable.ic_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return mOptionsList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        int id = v.getId();

        if (id == R.id.filterItemLL) {
            for (int i = 0; i < mOptionsList.size(); i++) {
                if (i == position) {
                    mOptionsList.get(i).setSelected(true);
                } else {
                    mOptionsList.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView filterItemTv;
        public ImageView selectIv;
        private LinearLayout filterItemLL;

        public OptionItemsViewHolder(View view) {
            super(view);
            filterItemTv = view.findViewById(R.id.filterItemTv);
            selectIv =  view.findViewById(R.id.selectIv);
            filterItemLL = view.findViewById(R.id.filterItemLL);
        }
    }

}
