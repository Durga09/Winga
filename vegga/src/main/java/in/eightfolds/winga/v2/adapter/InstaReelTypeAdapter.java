package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.model.InstaReelType;


public class InstaReelTypeAdapter extends RecyclerView.Adapter<InstaReelTypeAdapter.InstaReelTypeiewHolder> implements View.OnClickListener {

    private ArrayList<InstaReelType> instaReelTypes;

    private Context context;
    private OnEventListener eventListener;

    public InstaReelTypeAdapter(ArrayList<InstaReelType> instaReelTypes, Context context, OnEventListener eventListener) {
        this.instaReelTypes = instaReelTypes;
        this.context = context;
        this.eventListener = eventListener;
    }

    @Override
    public void onClick(View v) {

        InstaReelType streamingType=(InstaReelType) v.getTag();
        if (v.getId() ==  R.id.itemLL){
                for (InstaReelType streamingType1:instaReelTypes){
                    if(streamingType.getId()==streamingType1.getId()){
                        streamingType1.setSelected(true);
                    }else{
                        streamingType1.setSelected(false);
                    }
                }
                notifyDataSetChanged();
                if (eventListener != null) {
                    eventListener.onEventListener(R.id.itemLL, v.getTag());
                }
        }

    }

    @NonNull
    @Override
    public InstaReelTypeAdapter.InstaReelTypeiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_ott, parent, false);

        return new InstaReelTypeAdapter.InstaReelTypeiewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstaReelTypeAdapter.InstaReelTypeiewHolder holder, int position) {
        if(!TextUtils.isEmpty(instaReelTypes.get(position).getName())){
            holder.itemTv.setText(instaReelTypes.get(position).getName());
        }
        if(instaReelTypes.get(position).isSelected()){
            holder.itemLl.setBackground(context.getResources().getDrawable(R.drawable.ott_selected));
            holder.itemTv.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            holder.itemLl.setBackground(context.getResources().getDrawable(R.drawable.ott_item));
            holder.itemTv.setTextColor(context.getResources().getColor(R.color.colorBlack));

        }
        holder.itemLl.setOnClickListener(this);
        holder.itemLl.setTag(instaReelTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return instaReelTypes.size();
    }

    public static class InstaReelTypeiewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLl;
        TextView itemTv;

        public InstaReelTypeiewHolder(@NonNull View itemView) {
            super(itemView);
            itemLl=itemView.findViewById(R.id.itemLL);
            itemTv=itemView.findViewById(R.id.itemTv);
        }
    }
}

