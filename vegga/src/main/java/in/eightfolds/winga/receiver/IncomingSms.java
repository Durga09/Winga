package in.eightfolds.winga.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;


public class IncomingSms extends BroadcastReceiver {

    private static String TAG = IncomingSms.class.getSimpleName();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if(extras != null && extras.containsKey(SmsRetriever.EXTRA_STATUS) ) {
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        filterOTP(message);
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }
    }
    private void filterOTP(String msg) {
        if (msg.contains("OTP")) {
            String otp = msg.replaceAll("[^0-9]", "");
            if (otp.length() >= 6) {
                msg = otp.substring(0, 6);
                Intent intent = new Intent(Constants.RECEIVED_OTP_ACTION);
                intent.putExtra(Constants.DATA, msg);
                context.sendBroadcast(intent);
            }
        }
    }
}