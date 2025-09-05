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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder> implements View.OnClickListener {

    private final ArrayList<Category> categories;

    private final Context context;

    private final OnEventListener onEventListener;

    private final int selectedTypeView ;

    public CategoryAdapter(ArrayList<Category> categories, Context context, OnEventListener onEventListener, int selectedTypeView) {
        this.categories = categories;
        this.context = context;
        this.onEventListener = onEventListener;
        this.selectedTypeView = selectedTypeView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Category) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_feature, parent, false);

        return new CategoryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryItemViewHolder holder, int position) {
        holder.featureName.setText(categories.get(position).getName());

        if (categories.get(position).getIconId() != null&&categories.get(position).getIconId() != 0) {
            Glide.with(context)
                    .load(WingaConstants.GET_FILE_URL_IMAGE + categories.get(position).getIconId())
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

        } else if (categories.get(position).getIconUrl() != null) {
            Glide.with(context)
                    .load(categories.get(position).getIconUrl())
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
            holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dark_yellow_round_circle_bg));
        }
        holder.llayout.setOnClickListener(this);
        holder.llayout.setTag(categories.get(position));
    }

    @Override
    public int getItemCount() {
//
//        if(selectedTypeView==0){
//            return Math.min(categories.size(), 4);
//        }else{
//            return categories.size();
//        }

        return categories.size();

    }

    public static class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        ShimmerFrameLayout shimmer_view_container;
        LinearLayout llayout;
        ImageView featureImage;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            featureName = itemView.findViewById(R.id.featureName);
            shimmer_view_container = itemView.findViewById(R.id.shimmer_view_container);
            llayout = itemView.findViewById(R.id.linearLayout);
            featureImage = itemView.findViewById(R.id.featureImage);
        }
    }
}
