package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.NoInternetActivity;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.StreamingItem;

public class StreamItemAdapter extends RecyclerView.Adapter<StreamItemAdapter.StreamItemViewHolder> implements View.OnClickListener {

    private ArrayList<StreamingItem> streamingItems;

    private Context context;
    private OnEventListener eventListener;


    public StreamItemAdapter(ArrayList<StreamingItem> streamingItems, Context context, OnEventListener eventListener) {
        this.streamingItems = streamingItems;
        this.context = context;
        this.eventListener = eventListener;

    }

    @Override
    public void onClick(View v) {
        if(EightfoldsUtils.getInstance().isNetworkAvailable(context)) {
            if (v.getId() == R.id.cLL) {


                eventListener.onEventListener(v.getId(),v.getTag());
            }
        }else{
            Intent intent = new Intent(context, NoInternetActivity.class);

            context.startActivity(intent);

        }
    }

    @NonNull
    @Override
    public StreamItemAdapter.StreamItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_ott_video, parent, false);

        return new StreamItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamItemAdapter.StreamItemViewHolder holder, int position) {

        if(streamingItems.get(position).getIconId()!=null){
            Glide.with(context)
                    .load(WingaConstants.GET_FILE_URL_IMAGE +streamingItems.get(position).getIconId())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.iv_icon);
        }else{
            Glide.with(context)
                    .load(streamingItems.get(position).getIconUrl())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.iv_icon);
        }
        if(streamingItems.get(position).getImageId()!=null){
            Glide.with(context)
                    .load(WingaConstants.GET_FILE_URL_IMAGE +streamingItems.get(position).getImageId())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.thumbnailImage);
        }else{
            Glide.with(context)
                    .load(streamingItems.get(position).getImageUrl())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.thumbnailImage);
        }




        if(!TextUtils.isEmpty(streamingItems.get(position).getTitle())){
            holder.title.setText(streamingItems.get(position).getTitle());
        }
        holder.cLL.setOnClickListener(this);
        holder.cLL.setTag(streamingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return streamingItems.size();
    }

    public static class StreamItemViewHolder  extends RecyclerView.ViewHolder {
        ImageView thumbnailImage,iv_icon;
        TextView title;
        ConstraintLayout cLL;
        public StreamItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage=itemView.findViewById(R.id.thumbnailImage);
            iv_icon=itemView.findViewById(R.id.iv_icon);
            title=itemView.findViewById(R.id.title);
            cLL=itemView.findViewById(R.id.cLL);
        }
    }
}
