package in.eightfolds.winga.v2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Banner;


public class BannersAdapter extends SliderViewAdapter<BannersAdapter.BannersAdapterViewHolder> implements View.OnClickListener {

    private ArrayList<Banner> mBannerList;
    private Context context;
    private OnEventListener onEventListener;
    private Integer isFromhomePage;


    private LinearLayoutManager layoutManager;

    public BannersAdapter(ArrayList<Banner> mBannerList, Context context, OnEventListener onEventListener, Integer isFromhomePage) {
        this.mBannerList = mBannerList;
        this.context = context;
        this.onEventListener = onEventListener;
        this.isFromhomePage = isFromhomePage;
    }


//    @NonNull
//    @Override
//    public BannersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//    }

    @Override
    public BannersAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_banner, parent, false);

        return new BannersAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BannersAdapterViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //    holder.shimmer_view_container.startShimmer();

        if (isFromhomePage == 0) {
            if (mBannerList.get(position).getImageId() != null && mBannerList.get(position).getImageId() != 0) {
                Glide.with(context)
                        .load(WingaConstants.GET_FILE_URL_IMAGE + mBannerList.get(position).getImageId())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.shimmer_view_container.stopShimmer();
                                holder.shimmer_view_container.hideShimmer();
                                return false;
                            }
                        })
                        .placeholder(R.drawable.no_image_p)
                        .error(R.drawable.no_image_p)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.imageView);

                ;

            } else if (mBannerList.get(position).getImageUrl() != null) {
                Glide.with(context)
                        .load(mBannerList.get(position).getImageUrl())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.shimmer_view_container.stopShimmer();
                                holder.shimmer_view_container.hideShimmer();
                                return false;
                            }
                        })
                        .placeholder(R.drawable.no_image_p)
                        .error(R.drawable.no_image_p)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.imageView);
            }
        } else {
            if (mBannerList.get(position).getThumbnailId() != null && mBannerList.get(position).getThumbnailId() != 0) {
                Glide.with(context)
                        .load(WingaConstants.GET_FILE_URL_IMAGE + mBannerList.get(position).getThumbnailId())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.shimmer_view_container.stopShimmer();
                                holder.shimmer_view_container.hideShimmer();
                                return false;
                            }
                        })
                        .placeholder(R.drawable.no_image_p)
                        .error(R.drawable.no_image_p)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.imageView);

                ;

            } else if (mBannerList.get(position).getThumbnailUrl() != null) {
                Glide.with(context)
                        .load(mBannerList.get(position).getThumbnailUrl())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.shimmer_view_container.stopShimmer();
                                holder.shimmer_view_container.hideShimmer();
                                return false;
                            }
                        })
                        .placeholder(R.drawable.no_image_p)
                        .error(R.drawable.no_image_p)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.imageView);
            }
        }

        holder.shimmer_view_container.stopShimmer();
        holder.shimmer_view_container.hideShimmer();
        holder.linearLayout.setTag(mBannerList.get(position));
        holder.linearLayout.setOnClickListener(this);
    }


    @Override
    public int getCount() {
        return mBannerList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Banner) {
            if (onEventListener != null) {
                onEventListener.onEventListener(R.id.mainLL, v.getTag());
            }
        }
    }

    public static class BannersAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        RoundedImageView imageView;
        LinearLayout linearLayout;
        ShimmerFrameLayout shimmer_view_container;

        public BannersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
            linearLayout = itemView.findViewById(R.id.lLayout);
            shimmer_view_container = itemView.findViewById(R.id.shimmer_view_container);
        }
    }


}
