package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Category;
import in.eightfolds.winga.v2.model.TopBrands;

public class TopBrandsAdapter extends RecyclerView.Adapter<TopBrandsAdapter.TopBrandsItemViewHolder> implements View.OnClickListener {

    private final ArrayList<TopBrands> topBrands;

    private final Context context;

    private final OnEventListener onEventListener;

    private final int selectedTypeView ;

    public TopBrandsAdapter(ArrayList<TopBrands> topBrands, Context context, OnEventListener onEventListener, int selectedTypeView) {
        this.topBrands = topBrands;
        this.context = context;
        this.onEventListener = onEventListener;
        this.selectedTypeView = selectedTypeView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof TopBrands) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }

    @NonNull
    @Override
    public TopBrandsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_feature, parent, false);

        return new TopBrandsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TopBrandsItemViewHolder holder, int position) {
        holder.featureName.setText(topBrands.get(position).getName());

        if (topBrands.get(position).getIconId() != null && topBrands.get(position).getIconId() != 0) {
            Glide.with(context)
                    .load(WingaConstants.GET_FILE_URL_IMAGE + topBrands.get(position).getIconId())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.shimmer_view_container.stopShimmer();
                            holder.shimmer_view_container.hideShimmer();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.shimmer_view_container.stopShimmer();
                            holder.shimmer_view_container.hideShimmer();
                            return false;
                        }
                    })
                    .placeholder(R.drawable.watch_and_win)
                    .error(R.drawable.watch_and_win)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.featureImage);

            ;

        } else if (topBrands.get(position).getIconUrl() != null) {
            Glide.with(context)
                    .load(topBrands.get(position).getIconUrl())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.shimmer_view_container.stopShimmer();
                            holder.shimmer_view_container.hideShimmer();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.shimmer_view_container.stopShimmer();
                            holder.shimmer_view_container.hideShimmer();
                            return false;
                        }
                    })
                    .placeholder(R.drawable.watch_and_win)
                    .error(R.drawable.watch_and_win)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.featureImage);


        }else{
            holder.shimmer_view_container.stopShimmer();
            holder.shimmer_view_container.hideShimmer();
            holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.watch_and_win));
        }

        holder.llayout.setOnClickListener(this);
        holder.llayout.setTag(topBrands.get(position));
    }

    @Override
    public int getItemCount() {
//        if(selectedTypeView==0){
//            return Math.min(topBrands.size(), 4);
//        }else{
//            return topBrands.size();
//        }

        return topBrands.size();

    }

    public static class TopBrandsItemViewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        ShimmerFrameLayout shimmer_view_container;
        LinearLayout llayout;
        ImageView featureImage;

        public TopBrandsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            featureName = itemView.findViewById(R.id.featureName);
            shimmer_view_container = itemView.findViewById(R.id.shimmer_view_container);
            llayout = itemView.findViewById(R.id.linearLayout);
            featureImage = itemView.findViewById(R.id.featureImage);
        }
    }
}

