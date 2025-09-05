package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.StoreItem;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder> implements View.OnClickListener{

    private ArrayList<StoreItem> storeItems;

    private Context context;

    private OnEventListener onEventListener;

    public StoreItemAdapter(ArrayList<StoreItem> storeItems, Context context, OnEventListener onEventListener) {
        this.storeItems = storeItems;
        this.context = context;
        this.onEventListener = onEventListener;
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_product, parent, false);
        return new StoreItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        if (storeItems.get(position).getThumbnailId() != null && storeItems.get(position).getThumbnailId() != 0) {
            Glide.with(context).load(WingaConstants.GET_FILE_URL_IMAGE + storeItems.get(position).getThumbnailId())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p).listener(new RequestListener<Drawable>() {

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
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.productImage);
        } else {
            Glide.with(context).load(storeItems.get(position).getThumbnailUrl())
                    .placeholder(R.drawable.no_image_p)
                    .error(R.drawable.no_image_p).listener(new RequestListener<Drawable>() {

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
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.productImage);
        }
        DecimalFormat decimalFormat = new DecimalFormat("##,##,###.##");
        if (!TextUtils.isEmpty(storeItems.get(position).getTitle())) {
            holder.title.setText(storeItems.get(position).getTitle());
        }
        if (storeItems.get(position).getDiscountPrice() != null) {
            holder.discountPriceTv.setText("₹" + decimalFormat.format(storeItems.get(position).getDiscountPrice()).toString());
        }

        if(storeItems.get(position).getActualPrice()!=null){
            holder.actualPriceTv.setText("₹" +decimalFormat.format(storeItems.get(position).getActualPrice()).toString());
            holder.actualPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }

        if(storeItems.get(position).getDiscountPercentage()!=null){
            holder.discount.setText(storeItems.get(position).getDiscountPercentage().toString()+"%");

        }
        holder.cLL.setOnClickListener(this);
        holder.cLL.setTag(storeItems.get(position));


    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof StoreItem) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }

    public static class StoreItemViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cLL;
        RoundedImageView productImage;
        TextView title, productDesc, discountPriceTv, actualPriceTv, discount;
        ShimmerFrameLayout shimmer_view_container;

        public StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cLL=itemView.findViewById(R.id.cLL);
            productImage = itemView.findViewById(R.id.productImage);
            productDesc = itemView.findViewById(R.id.productDesc);
            shimmer_view_container=itemView.findViewById(R.id.shimmer_view_container);
            title = itemView.findViewById(R.id.title);
            discountPriceTv = itemView.findViewById(R.id.discountPriceTv);
            actualPriceTv = itemView.findViewById(R.id.actualPriceTv);
            discount = itemView.findViewById(R.id.discount);
        }
    }
}
