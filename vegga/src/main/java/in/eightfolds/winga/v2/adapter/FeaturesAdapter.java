package in.eightfolds.winga.v2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HomeBaseActivity;
import in.eightfolds.winga.activity.NoInternetActivity;
import in.eightfolds.winga.activity.V2GuessTheBrand;
import in.eightfolds.winga.activity.V2InstagramReelsActivity;
import in.eightfolds.winga.activity.V2OttActivity;
import in.eightfolds.winga.activity.V2SpinAndWinActivity;

import in.eightfolds.winga.activity.V2WingaStoreActivity;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.v2.constant.WingaConstants;
import in.eightfolds.winga.v2.model.Feature;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureItemViewHolder> implements View.OnClickListener  {

    private ArrayList<Feature> features;

    private Context context;
    private OnEventListener onEventListener;


    public FeaturesAdapter(Context context, ArrayList<Feature> features, OnEventListener onEventListener) {
        this.features = features;

        this.context = context;

        this.onEventListener = onEventListener;
    }



    @NonNull
    @Override
    public FeaturesAdapter.FeatureItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_feature, parent, false);

        return new FeatureItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturesAdapter.FeatureItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.featureName.setText(features.get(position).getTitle());

        if (features.get(position).getImageId() != null && features.get(position).getImageId() != 0) {
            Glide.with(context).load(WingaConstants.GET_FILE_URL_IMAGE + features.get(position).getImageId())
                    .placeholder(R.drawable.watch_and_win)
                    .error(R.drawable.watch_and_win)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.featureImage);
        } else if(!TextUtils.isEmpty(features.get(position).getImageUrl())) {
            Glide.with(context).load(features.get(position).getImageUrl())
                    .placeholder(R.drawable.watch_and_win)
                    .error(R.drawable.watch_and_win)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.featureImage);
        }else{
            if(features.get(position).getId()==1){
                holder.featureImage.setBackground(ContextCompat.getDrawable(context,R.drawable.image_background));
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.watch_and_win));
            }
            else if(features.get(position).getId()==2){
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.spin_and_win));
            }
            else if(features.get(position).getId()==3){
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_ott_youtube));
            }
            else if(features.get(position).getId()==4){
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.guess_the_brand));
            }
            else if(features.get(position).getId()==5){
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_instareels));
            }
            else if(features.get(position).getId()==6){
                holder.featureImage.setBackground(ContextCompat.getDrawable(context,R.drawable.image_background));
                holder.featureImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.store));
            }
        }


        holder.shimmer_view_container.stopShimmer();
        holder.shimmer_view_container.hideShimmer();
        holder.llayout.setOnClickListener(this);
        holder.llayout.setTag(features.get(position));

    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.linearLayout) {
                if(EightfoldsUtils.getInstance().isNetworkAvailable(context)) {


                    Feature feature = (Feature) v.getTag();

                    EightfoldsUtils.getInstance().saveToSharedPreference(context,WingaConstants.GET_INFO,feature.getInformation());
                    EightfoldsUtils.getInstance().saveToSharedPreference(context,WingaConstants.SAVE_TITLE,feature.getTitle());

                    if (feature.getId() == 3) {
                        Intent intent = new Intent(context, V2OttActivity.class);
                        intent.putExtra("title", feature.getTitle());
                        intent.putExtra("information", feature.getInformation());
                        context.startActivity(intent);
                    }  else if (feature.getId() == 6) {
                        Intent intent = new Intent(context, V2WingaStoreActivity.class);
                        intent.putExtra("title", feature.getTitle());
                        intent.putExtra("information", feature.getInformation());
                        context.startActivity(intent);
                    } else if (feature.getId() == 5) {
                        Intent intent = new Intent(context, V2InstagramReelsActivity.class);
                        intent.putExtra("title", feature.getTitle());
                        intent.putExtra("information", feature.getInformation());
                        context.startActivity(intent);
                    }
                    if (onEventListener != null) {
                        onEventListener.onEventListener(R.id.mainLL, v.getTag());
                    }

                }else{
                    Intent intent = new Intent(context, NoInternetActivity.class);

                    context.startActivity(intent);

                }


        }

    }

    public static class FeatureItemViewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        ShimmerFrameLayout shimmer_view_container;
        LinearLayout llayout;
        ImageView featureImage;
        public FeatureItemViewHolder(@NonNull View itemView) {
            super(itemView);

            featureName=itemView.findViewById(R.id.featureName);
            shimmer_view_container=itemView.findViewById(R.id.shimmer_view_container);
            llayout=itemView.findViewById(R.id.linearLayout);
            featureImage=itemView.findViewById(R.id.featureImage);

        }
    }
}




