package in.eightfolds.winga.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.eightfolds.utils.Api;
import in.eightfolds.utils.DateTime;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.CodesAndInvitesActivity;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.HomeBaseActivity;
import in.eightfolds.winga.activity.NotificationDetailsActivity;
import in.eightfolds.winga.activity.SessionWinnerActivity;
import in.eightfolds.winga.activity.WinLuckyDrawActivity;
import in.eightfolds.winga.activity.RecentWinnersActivity;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 27-Apr-18.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> implements View.OnClickListener {

    private ArrayList<Notification> mNotificationsList;
    private Context mContext;
    private final static int FADE_DURATION = 100;


    public NotificationsAdapter(Context context, ArrayList<Notification> notificationsList) {
        mNotificationsList = notificationsList;
        mContext = context;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_lay, parent, false);

        return new NotificationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        try {
            Notification notification = mNotificationsList.get(position);
            holder.notificationTitleTv.setText(!TextUtils.isEmpty(notification.getTitle()) ? notification.getTitle() : "");
            holder.notificationDescriptionTv.setText(!TextUtils.isEmpty(notification.getMessage()) ? notification.getMessage() : "");

            //0,3,6,9


            if (notification.getCreatedTime() != null) {
                String date = notification.getCreatedTime();
                String formattedDate = DateTime.getDateFromUTC(date, "yyyy-MM-dd HH:mm:ss", "EEE, MMM dd, hh:mm a");
                holder.dateTv.setText(!TextUtils.isEmpty(formattedDate) ? formattedDate : "");

            }

            if (notification.isRead()) {
                holder.notificationBGIv.setBackgroundColor(mContext.getResources().getColor(R.color.winga_bg_color));
                holder.dotIv.setVisibility(View.INVISIBLE);

            } else {
                holder.notificationBGIv.setBackgroundColor(mContext.getResources().getColor(R.color.notification_unread_color));
                holder.dotIv.setVisibility(View.VISIBLE);
            }

            holder.notificationLL.setOnClickListener(this);
            holder.notificationLL.setTag(notification);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.notificationLL) {
                Notification notification = (Notification) v.getTag();
                Utils.makeNotificationAsRead(mContext, notification.getNotificationId());
                if (notification.getType() != null && notification.getType() == Notification.NOTIFICATION_TYPE_LUCKY_DRAW) {
                    Intent intent = new Intent(mContext, WinLuckyDrawActivity.class);
                    if (!TextUtils.isEmpty(notification.getJsonData())) {
                        try {
                            PrizeWin prizeWin = (PrizeWin) Api.fromJson(notification.getJsonData(), PrizeWin.class);
                            intent.putExtra(Constants.OTHER_DATA, prizeWin);
                            intent.putExtra(Constants.DATA, notification);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mContext.startActivity(intent);
                }else if(notification.getType() != null && notification.getType() == Notification.NOTIFICATION_TYPE_TEN){
                    Intent intent = new Intent(mContext, SessionWinnerActivity.class);
                    intent.putExtra(Constants.DATA, notification);
                    mContext.startActivity(intent);
                }


                else if (notification.getType() != null && notification.getType() == Notification.NOTIFICATION_TYPE_REFERRAL) {
                    Intent intent = new Intent(mContext, CodesAndInvitesActivity.class);
                    intent.putExtra("toInvitesTab", true);
                    mContext.startActivity(intent);
                } else if (notification.getType() != null && notification.getType() == Notification.NOTIFICATION_TYPE_SESSION_END
                        || notification.getType() == Notification.NOTIFICATION_TYPE_SESSION_START) {
                    Intent intent = new Intent(mContext, HomeBaseActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                } else if (notification.getType() != null && notification.getType() == Notification.NOTIFICATION_TYPE_RESULT_DECLARED) {
                    Intent intent = new Intent(mContext, RecentWinnersActivity.class);
                    intent.putExtra(Constants.DATA, notification);

                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, NotificationDetailsActivity.class);
                    intent.putExtra(Constants.DATA, notification);
                    mContext.startActivity(intent);
                }

                for (Notification notification1 : mNotificationsList) {
                    if (notification1.getNotificationId().equals(notification.getNotificationId())) {
                        if (!notification1.isRead()) {
                            notification1.setRead(true);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                }


        }
    }

    public void refreshData(List<Notification> notificationsList) {
        mNotificationsList.addAll(notificationsList);
        notifyDataSetChanged();

    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTv, notificationTitleTv, notificationDescriptionTv;
        public LinearLayout notificationLL;
        private ImageView dotIv, notificationBGIv;

        public NotificationsViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            notificationTitleTv = view.findViewById(R.id.notificationTitleTv);
            notificationDescriptionTv = view.findViewById(R.id.notificationDescriptionTv);
            notificationLL = view.findViewById(R.id.notificationLL);
            notificationBGIv = view.findViewById(R.id.notificationBGIv);
            dotIv = view.findViewById(R.id.dotIv);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

}
