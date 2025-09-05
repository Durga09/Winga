package in.eightfolds.winga.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.AnouncementDetailActivity;
import in.eightfolds.winga.model.ExclusiveCategoryItem;
import in.eightfolds.winga.utils.Constants;

public class AnouncementsAdapter extends RecyclerView.Adapter<AnouncementsAdapter.AnouncementViewHolder> implements View.OnClickListener {

    private final ArrayList<ExclusiveCategoryItem> mExclusiveCategoryItemList;
    private final Context mContext;
    private final static int FADE_DURATION = 100;


    public AnouncementsAdapter(Context context, ArrayList<ExclusiveCategoryItem> exclusiveCategoryItemList) {
        mExclusiveCategoryItemList = exclusiveCategoryItemList;
        mContext = context;
    }

    @NonNull
    @Override
    public AnouncementsAdapter.AnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_anouncement, parent, false);

        return new AnouncementsAdapter.AnouncementViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull AnouncementViewHolder holder, int position) {
        try {
            ExclusiveCategoryItem exclusiveCategoryItem = mExclusiveCategoryItemList.get(position);
            holder.anouncementTitleTv.setText(!TextUtils.isEmpty(exclusiveCategoryItem.getTitle()) ? exclusiveCategoryItem.getTitle() : "");
            holder.anouncementDescriptionTv.setText(!TextUtils.isEmpty(exclusiveCategoryItem.getMessage()) ? exclusiveCategoryItem.getMessage() : "");

            //0,3,6,9



                holder.dateTv.setText(exclusiveCategoryItem.getCreatedTime());




                holder.anouncementBGIv.setBackgroundColor(mContext.getResources().getColor(R.color.notification_unread_color));



            holder.anouncementLL.setOnClickListener(this);
            holder.anouncementLL.setTag(exclusiveCategoryItem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mExclusiveCategoryItemList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.anouncementLL) {
            ExclusiveCategoryItem exclusiveCategoryItem = (ExclusiveCategoryItem) v.getTag();
            Intent intent = new Intent(mContext, AnouncementDetailActivity.class);
            intent.putExtra(Constants.DATA, exclusiveCategoryItem);
            mContext.startActivity(intent);
        }
    }


    public class AnouncementViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTv, anouncementTitleTv, anouncementDescriptionTv;
        public LinearLayout anouncementLL;
        private ImageView dotIv, anouncementBGIv;

        public AnouncementViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            anouncementTitleTv = view.findViewById(R.id.anouncementTitleTv);
            anouncementDescriptionTv = view.findViewById(R.id.anouncementDescriptionTv);
            anouncementLL = view.findViewById(R.id.anouncementLL);
            anouncementBGIv = view.findViewById(R.id.anouncementBGIv);
            dotIv = view.findViewById(R.id.dotIv);

        }
    }



}

