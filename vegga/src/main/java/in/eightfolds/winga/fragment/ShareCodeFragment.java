package in.eightfolds.winga.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class ShareCodeFragment extends Fragment implements View.OnClickListener, OnSuccessListener<ShortDynamicLink> {

    private LinearLayout shareCodeLL;
    private TextView shareCodeTv;
    private TextView pointsToBeEarnedTv;
    private TextView referralCodeTv;
    String mInvitationUrl;
    private static String TAG = ShareCodeFragment.class.getSimpleName();
    private String referralCode;
    private Activity myContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share_code, container, false);

        initialize(rootView);
        // askPermissions();
        //setUid();

        return rootView;
    }


    private void initialize(View rootView) {
        try {
            shareCodeTv = rootView.findViewById(R.id.shareCodeTv);
            pointsToBeEarnedTv = rootView.findViewById(R.id.pointsToBeEarnedTv);
            referralCodeTv = rootView.findViewById(R.id.referralCodeTv);
            (rootView.findViewById(R.id.shareBtn)).setOnClickListener(this);
            (rootView.findViewById(R.id.tapAreaLL)).setOnClickListener(this);

            User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(myContext, Constants.USER_DETAILS, User.class);
            if (!TextUtils.isEmpty(userDetails.getReferralCode())) {
                referralCode = userDetails.getReferralCode();
                referralCodeTv.setText(referralCode);
            }

            Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(myContext, Constants.SET_UP_DETAILS, Setup.class);
            Double bonusReferralAmt = setup.getBonusReferralAmt();
            Double friendBenefitAmt = setup.getFirstGameWinAmt();

            String bonusReferralAmountValue = "0";
            if (bonusReferralAmt != null) {
                bonusReferralAmountValue = String.valueOf(bonusReferralAmt.intValue());
            }

            String friendBenefitAmountValue = "0";
            if (friendBenefitAmt != null) {
                friendBenefitAmountValue = String.valueOf(friendBenefitAmt.intValue());
            }


            pointsToBeEarnedTv.setText(setup.getReferralText());

            //getDynamicLink(rootView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tapAreaLL) {
            ClipboardManager clipboardManager = (ClipboardManager) myContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("referralcode", referralCodeTv.getText());
            clipboardManager.setPrimaryClip(clipData);
            MyDialog.showToast(myContext, getString(R.string.code_copied));
        } else if (v.getId() == R.id.shareBtn) {
            //if (verifyCameraStoragePermissions(myContext)) {
                shareApp();
            //}
        }
    }


    public boolean verifyCameraStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(

                    EightfoldsImage.PERMISSIONS_STORAGE,
                    EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_SHARE
            );
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == EightfoldsImage.REQUEST_EXTERNAL_STORAGE_FOR_SHARE)

        {
            int writePermission = ActivityCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (writePermission == PackageManager.PERMISSION_GRANTED) {

                shareApp();
            }
        }


    }

    private void shareApp() {
        if (!TextUtils.isEmpty(referralCode)) {
            if (TextUtils.isEmpty(mInvitationUrl)) {
                Utils.generateDynamicLink(referralCode, this);
            } else {
              //  Utils.shareImageUsingGlide(myContext, mInvitationUrl);
                Utils.shareCodeUsingFCMGeneratedURL(myContext, mInvitationUrl);

            }

        } else {
            MyDialog.showToast(myContext, getString(R.string.service_not_available));
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    @Override
    public void onSuccess(ShortDynamicLink shortDynamicLink) {
        try {
            mInvitationUrl = shortDynamicLink.getShortLink().toString();
           // Utils.shareImageUsingGlide(myContext, mInvitationUrl);
            Utils.shareCodeUsingFCMGeneratedURL(myContext, mInvitationUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
