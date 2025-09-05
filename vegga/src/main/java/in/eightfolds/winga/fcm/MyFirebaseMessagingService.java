package in.eightfolds.winga.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.CodesAndInvitesActivity;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.NotificationDetailsActivity;
import in.eightfolds.winga.activity.SplashScreenActivity;
import in.eightfolds.winga.activity.TotalRewardsActivity;
import in.eightfolds.winga.activity.WinLuckyDrawActivity;
import in.eightfolds.winga.activity.RecentWinnersActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.Utils;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements VolleyResultCallBack {

    private static final String TAG = "MyFirebaseMsgService";
    private RemoteMessage remoteMessage;
    String description = null;
    String title;
    private int type;
    private Notification notification;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Logg.v("FCM ", "*** Token >> " + s);
        if (!TextUtils.isEmpty(s)) {
            EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.FIRE_BASE_TOKEN, s);
            Utils.sendPushRegId(this, this);
        }
    }


    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof CommonResponse) {
            CommonResponse responce = (CommonResponse) object;
            if (responce.getCode() == Constants.SUCCESS) {
                Logg.i("onVolleyResultListener", "token updated successfully");
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logg.v(TAG, "**onMessageReceived()");
        this.remoteMessage = remoteMessage;
        Logg.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Logg.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Logg.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getBody() != null)
                description = remoteMessage.getNotification().getBody();
            if (remoteMessage.getNotification().getTitle() != null)
                title = remoteMessage.getNotification().getTitle();
        }
        Long notificationId = null;
        Long imageId = null;
        if (remoteMessage.getData() != null) {
            String imageUrl = remoteMessage.getData().get("image");
            if (remoteMessage.getData().containsKey("type")) {
                type = Integer.valueOf(remoteMessage.getData().get("type"));
            }
            if (remoteMessage.getData().containsKey("notificationId")) {
                String notId = remoteMessage.getData().get("notificationId");
                if (!TextUtils.isEmpty(notId)) {
                    try {
                        notificationId = Long.valueOf(notId);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
                Logg.v(TAG, "notificationId : " + notificationId);
            }

            if (remoteMessage.getData().containsKey("imageId")) {
                String imgId = remoteMessage.getData().get("imageId");
                if (!TextUtils.isEmpty(imgId)) {
                    try {
                        imageId = Long.valueOf(imgId);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
                Logg.v(TAG, "imageId : " + imageId);
            }
        }
        notification = new Notification();
        notification.setTitle(title != null ? title : getString(R.string.app_name));
        notification.setMessage(description != null ? description : "");
        notification.setType(type);
        notification.setImageId(imageId);
        notification.setNotificationId(notificationId);
        if (type == Notification.NOTIFICATION_TYPE_SETUP_CHANGE) {
            Utils.RefreshSetUpDetails(this);
            Intent homePageRefreshIntent = new Intent();
            homePageRefreshIntent.setAction(Constants.HOME_REFRESH_ACTION);
            sendBroadcast(homePageRefreshIntent);
        } else {
            sendNotification(remoteMessage, null);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage, Bitmap bitmap) {
        Logg.v(TAG, "**sendNotification()");
        Map<String, String> params = remoteMessage.getData();
        String targetUrl = params.get("targetUrl");
        if (remoteMessage.getData().containsKey("jsonData")) {
            String data = remoteMessage.getData().get("jsonData");
            notification.setJsonData(data);
        }else if(remoteMessage.getData().containsKey("prizeObj")){
            String data = remoteMessage.getData().get("prizeObj");
            notification.setJsonData(data);
        }
        Intent intent = null;
        String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN);
        if (!TextUtils.isEmpty(accessToken)) {
            if (type == Notification.NOTIFICATION_TYPE_LUCKY_DRAW ) {
                PrizeWin prizeWin = null;
                if (remoteMessage.getData().containsKey("jsonData")) {
                    String prizeWinString = remoteMessage.getData().get("jsonData");
                    try {
                        prizeWin = (PrizeWin) Api.fromJson(prizeWinString, PrizeWin.class);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (remoteMessage.getData().containsKey("prizeObj")) {
                    String prizeWinString = remoteMessage.getData().get("prizeObj");
                    try {
                        prizeWin = (PrizeWin) Api.fromJson(prizeWinString, PrizeWin.class);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if( prizeWin != null) {
                    intent = new Intent(this, WinLuckyDrawActivity.class);
                    intent.putExtra(Constants.OTHER_DATA, prizeWin);
                }
            } else if (type == Notification.NOTIFICATION_TYPE_SESSION_END
                    || type == Notification.NOTIFICATION_TYPE_SESSION_START) {

                Intent homePageRefreshIntent = new Intent();
                homePageRefreshIntent.setAction(Constants.HOME_REFRESH_ACTION);
                sendBroadcast(homePageRefreshIntent);

                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (type == Notification.NOTIFICATION_TYPE_REFERRAL) {
                intent = new Intent(this, CodesAndInvitesActivity.class);
                intent.putExtra("toInvitesTab", true);
            }
            else if (type == Notification.NOTIFICATION_TYPE_RESULT_DECLARED) {
                intent = new Intent(this, RecentWinnersActivity.class);
            }
            else if(type == Notification.NOTIFICATION_TYPE_DISBURSEMENT){
                intent = new Intent(this, TotalRewardsActivity.class);
            }
            else {
                intent = new Intent(this, NotificationDetailsActivity.class);
            }
        } else {
            intent = new Intent(this, SplashScreenActivity.class);
        }
        intent.putExtra(Constants.DATA, notification);
        intent.putExtra(Constants.FROM_NOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getService(this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);
        }
        else{
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        android.app.Notification notification = bitmap != null ? getNotificationWithImage(
                pendingIntent, bitmap) : getNotification(remoteMessage,
                pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


    private android.app.Notification getNotificationWithImage(PendingIntent pendingIntent, Bitmap bitmap) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
            return new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title != null ? title : "")
                    .setAutoCancel(true).setContentText(description != null ? description : "")
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.ic_launcher_foreground
                            : R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon).setStyle(
                            new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(description != null ? description : ""))
                    .build();
        } else {
            return new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title != null ? title : "")
                    .setAutoCancel(true).setContentText(description != null ? description : "")
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(R.mipmap.ic_launcher).setStyle(
                            new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap)).build();
        }

    }

    private android.app.Notification getNotification(RemoteMessage remoteMessage,
                                                     PendingIntent pendingIntent) {


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
            return new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id))
                    .setContentTitle(title != null ? title : "")
                    .setAutoCancel(true).setContentText(description != null ? description : "")
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(description != null ? description : ""))
                            .setLargeIcon(largeIcon).build();
        } else {
            return new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id))
                    .setContentTitle(title != null ? title : "")
                    .setAutoCancel(true).setContentText(description != null ? description : "")
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(description != null ? description : ""))
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        }
    }
}

