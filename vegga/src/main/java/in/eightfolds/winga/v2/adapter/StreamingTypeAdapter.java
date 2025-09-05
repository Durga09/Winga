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
import in.eightfolds.winga.v2.model.StreamingType;

public class StreamingTypeAdapter extends RecyclerView.Adapter<StreamingTypeAdapter.StreamingTypeiewHolder> implements View.OnClickListener {

    private ArrayList<StreamingType> streamingTypes;

    private Context context;
    private OnEventListener eventListener;

    public StreamingTypeAdapter(ArrayList<StreamingType> streamingTypes, Context context, OnEventListener eventListener) {
        this.streamingTypes = streamingTypes;
        this.context = context;
        this.eventListener = eventListener;
    }

    @Override
    public void onClick(View v) {

        StreamingType streamingType=(StreamingType) v.getTag();
        if (v.getId() == R.id.itemLL){
                for (StreamingType streamingType1:streamingTypes){
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
    public StreamingTypeAdapter.StreamingTypeiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_ott, parent, false);

        return new StreamingTypeiewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamingTypeAdapter.StreamingTypeiewHolder holder, int position) {
        if(!TextUtils.isEmpty(streamingTypes.get(position).getName())){
            holder.itemTv.setText(streamingTypes.get(position).getName());
        }
        if(streamingTypes.get(position).isSelected()){
            holder.itemLl.setBackground(context.getResources().getDrawable(R.drawable.ott_selected));
            holder.itemTv.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            holder.itemLl.setBackground(context.getResources().getDrawable(R.drawable.ott_item));
            holder.itemTv.setTextColor(context.getResources().getColor(R.color.colorBlack));

        }
        holder.itemLl.setOnClickListener(this);
        holder.itemLl.setTag(streamingTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return streamingTypes.size();
    }

    public static class StreamingTypeiewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLl;
        TextView itemTv;

        public StreamingTypeiewHolder(@NonNull View itemView) {
            super(itemView);
            itemLl=itemView.findViewById(R.id.itemLL);
            itemTv=itemView.findViewById(R.id.itemTv);
        }
    }
}
