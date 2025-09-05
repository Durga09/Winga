package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.InstaReelItem;


public class InstaReelAdapter extends RecyclerView.Adapter<InstaReelAdapter.InstaReelViewHolder> implements View.OnClickListener {


    public ArrayList<InstaReelItem> instaReelItems;

    private Context context;

    private OnEventListener onEventListener;

    public ArrayList<InstaReelItem> getInstaReelItems() {
        return instaReelItems;
    }

    public void setInstaReelItems(ArrayList<InstaReelItem> instaReelItems) {
        this.instaReelItems = instaReelItems;
    }

    public InstaReelAdapter(ArrayList<InstaReelItem> instaReelItems, Context context, OnEventListener onEventListener) {
        this.instaReelItems = instaReelItems;
        this.context = context;
        this.onEventListener = onEventListener;
    }

    @NonNull
    @Override
    public InstaReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_instagram_reel, parent, false);
        return new InstaReelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstaReelViewHolder holder, int position) {
        if (instaReelItems.get(position).getThumbnailId() != null && instaReelItems.get(position).getThumbnailId() != 0) {
            Glide.with(context).load(WingaConstants.GET_FILE_URL_IMAGE + instaReelItems.get(position).getThumbnailId())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.roundedImageView);
        } else {
            Glide.with(context).load(instaReelItems.get(position).getThumbnailUrl())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.roundedImageView);
        }

        if (!TextUtils.isEmpty(instaReelItems.get(position).getTitle())) {
            holder.title.setText(instaReelItems.get(position).getTitle());
        }

        if (!TextUtils.isEmpty(instaReelItems.get(position).getSubtitle())) {
            holder.subTitle.setText(instaReelItems.get(position).getSubtitle());
        }

    holder.cLL.setOnClickListener(this);
        holder.cLL.setTag(instaReelItems.get(position));
    }

    @Override
    public int getItemCount() {
        return instaReelItems.size();
    }

    public static class InstaReelViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cLL;
        RoundedImageView roundedImageView;
        TextView title, subTitle;

        public InstaReelViewHolder(@NonNull View itemView) {
            super(itemView);

            cLL = itemView.findViewById(R.id.cLL);
            roundedImageView = itemView.findViewById(R.id.instaReelImage);
            title = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.subTitle);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof InstaReelItem) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }
}


