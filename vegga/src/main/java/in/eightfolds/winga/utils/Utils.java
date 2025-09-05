package in.eightfolds.winga.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;

import androidx.ads.identifier.AdvertisingIdClient;
import androidx.ads.identifier.AdvertisingIdInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import in.eightfolds.WingaApplication;
import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.LoginActivity;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AgeRange;
import in.eightfolds.winga.model.CategoryHomePageResponse;
import in.eightfolds.winga.model.ContentResponse;
import in.eightfolds.winga.model.HomePageAd;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.Language;
import in.eightfolds.winga.model.LoginData;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.Notification;
import in.eightfolds.winga.model.RedemptionOption;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.State;
import in.eightfolds.winga.model.User;
import in.eightfolds.winga.model.UserDevice;
import in.eightfolds.winga.model.UserLevel;
import in.eightfolds.winga.model.UserLoggedDevice;

/**
 * Created by Swapnika on 26-Apr-18.
 */

@SuppressWarnings("ALL")
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static String GetShortName(String name) {
        try {
            if (name.length() < 0) {
                return "";
            }
            String shortName = "";

            String[] splitName = name.split(" ");
            for (String names : splitName
            ) {
                if (names.length() > 0) {
                    shortName += names.substring(0, 1);
                }
            }

            if (shortName.length() >= 2) {
                return shortName.substring(0, 1);
            } else {
                return shortName.toUpperCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return name.substring(0, 1).toUpperCase();
        }
    }

    public static String GetShortName(String firstName, String lastName) {
        String shortName = "";
        shortName += firstName.trim().substring(0, 1);
        shortName += lastName.trim().substring(0, 1);
        return shortName;
    }

    public static void hideKeyboard(Activity activity, View view) {
        // Check if no view has focus:
        if (view == null) {
            view = activity.getCurrentFocus();
        }
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void regSmsReceiver(Activity activity) {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(activity);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.d("", "");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.d("", "");
            }
        });
    }

    public static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;

        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    public static SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);

        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public static void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

    }

    public static void goForLogIn(Context context) {
        EightfoldsUtils.getInstance().saveToSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN, null);
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.LOGIN_DATA, null);
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.USER_DETAILS, null);
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.MOBILE_NUMBER, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.LOGIN_TYPE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.FIRST_GAME_PLAYED, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.DEVICE_INFO_LAST_UPDATED_TIME, "");
        String activityName = "";
        try {
            activityName = EightfoldsUtils.getInstance().getTopmostActivity(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(activityName.equals("in.eightfolds.winga.activity.LoginActivity"))) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }


    public static String getCurrentLangId() {
        String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_ID);
        if (TextUtils.isEmpty(selectedlLangId)) {
            selectedlLangId = Constants.DEFAULT_ENGLISH_LANG_ID;
        }
        return selectedlLangId;
    }

    public static Locale getCurrentLocale() {
        String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_ID);
        String selectedlLangLocale = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_LOCALE);

        if (TextUtils.isEmpty(selectedlLangLocale)) {
            selectedlLangLocale = Constants.DEFAULT_LOCALE;
        }
        return new Locale(selectedlLangLocale);
    }

    public static Locale getDefualtLocale() {
        return new Locale(Constants.DEFAULT_LOCALE);
    }


    public static boolean isTablet(Context context) {
        boolean isTablet = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        Logg.v(TAG, "******* isTablet >> " + isTablet);
        return isTablet;
    }


    public static String getCurrentLanguage(Context context) {
        String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_ID);
        String selectedlLangLocale = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_LOCALE);

        if (TextUtils.isEmpty(selectedlLangLocale)) {
            selectedlLangLocale = Constants.DEFAULT_LOCALE;
        }

        return selectedlLangLocale;
    }

    public static void deleteAdInSetupAfterViewing(Context context) {

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        setup.setHomePageAd(null);
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.SET_UP_DETAILS, setup);
    }

    public static HomePageAd getHomePageAdFromSetUp(Context context) {

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);

        if (setup != null && setup.getHomePageAd() != null) {
            return setup.getHomePageAd();
        }
        return null;
    }


    public static ArrayList<RedemptionOption> getRedemptionOptionsFromSetUp(Context context) {

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);

        if (setup != null && setup.getRedemptionOptions() != null) {
            return new ArrayList<>(setup.getRedemptionOptions());
        }
        return null;
    }


    public static String getCurrentISO(Context context) {
        String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_ID);

        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        for (Language language : setup.getLanguages()) {
            if ((language.getLangId() + "").equalsIgnoreCase(selectedlLangId + "")) {
                return language.getIsoCode();
            }
        }


        return "eng";
    }

    public static Configuration setAppLanguage(Context context) {
        try {
            String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.SELECTED_LANGUAGE_ID);
            String selectedlLangLocale = EightfoldsUtils.getInstance().getFromSharedPreference(WingaApplication.getInstance().getApplicationContext(), Constants.SELECTED_LANGUAGE_LOCALE);
            if (TextUtils.isEmpty(selectedlLangLocale)) {
                selectedlLangLocale = Constants.DEFAULT_LOCALE;
            }


            if (!TextUtils.isEmpty(selectedlLangLocale)) {
                Resources res = context.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                WingaApplication.getInstance().setConfiguration(conf);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    conf.setLocale(new Locale(selectedlLangLocale)); // API 17+ only.s
                }

                Logg.v(TAG, " ###&&&***$$$### Set Locale >> " + selectedlLangLocale);
                res.updateConfiguration(conf, dm);
                return conf;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public static void handleCommonErrors(Context context, Object object) {
        if (object instanceof String) {
            MyDialog.showToast(context, object.toString());
        } else if (object instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) object;
            MyDialog.showToast(context, commonResponse.getMessage());
        }
    }


   /* public static String getAddressTypeFromId(Context context, long addressTypeId) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        for (AddressType addressType : setup.getAddressTypes()) {
            if (addressType.getAdreTypeId() == addressTypeId) {
                return addressType.getTitle();
            }
        }
        return "";
    }
      public static String getSupportStateFromId(Context context, Integer supportState) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);

        if(setup.getSupportRequestStates().containsKey(supportState)) {
            String supportStateValue = setup.getSupportRequestStates().get(supportState);
            if(!TextUtils.isEmpty(supportStateValue)){
                return supportStateValue;
            }
        }

        return "";
    }
    */

    public static String getStateNameFromId(Context context, int stateId) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        for (State state : setup.getStates()) {
            if ((state.getId() + "").equalsIgnoreCase(stateId + "")) {
                return state.getName();
            }
        }
        return "";
    }

    public static String getUserLevelFromId(Context context, Long userLevelId) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        for (UserLevel userLevel : setup.getUserLevels()) {
            if ((userLevel.getLevelId() + "").equalsIgnoreCase(userLevelId + "")) {
                return userLevel.getTitle();
            }
        }
        return "";
    }


    public static String getAgeRangeFromId(Context context, Long ageRange) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        for (AgeRange ageRange1 : setup.getAgeRanges()) {
            if ((ageRange1.getAgeRangeId() + "").equalsIgnoreCase(ageRange + "")) {
                return ageRange1.getTitle();
            }
        }
        return "";
    }


    public static NewGameResponse getSelectedLangContents(Context context, NewGameResponse newGameResponse) {

        String currentLangIso = "";
        String defaultIso = Constants.DEFAULT_ISO;

        String selectedLangId = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.SELECTED_LANGUAGE_ID);
        currentLangIso = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.SELECTED_LANGUAGE_ISO);


        ArrayList<ContentResponse> contentResponses = new ArrayList<>();
        if (newGameResponse != null && newGameResponse.getContents() != null && newGameResponse.getContents().size() > 0) {
            for (int i = 0; i < newGameResponse.getContents().size(); i++) {
                Map<String, ContentResponse> allLangContent = newGameResponse.getContents().get(i);
                ContentResponse contentResponse = new ContentResponse();
                if (allLangContent.containsKey(currentLangIso)) {
                    contentResponse = allLangContent.get(currentLangIso);
                } else if (allLangContent.containsKey(defaultIso)) {
                    contentResponse = allLangContent.get(defaultIso);
                }
                contentResponses.add(contentResponse);
            }
            newGameResponse.setAppcontents(contentResponses);
            newGameResponse.setContents(null);
        }

        return newGameResponse;

    }


    public static void loadGifResourceToImageView(Context context, ImageView imageView, int resourceDrawable) {
        Glide.with(context)
                .load(resourceDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadGifResourceToImageViewWithCount(Context context, ImageView imageView, int resourceDrawable, int loopCount) {
        Glide.with(context)
                .load(resourceDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new GifDrawableImageViewTarget(imageView, loopCount));
    }


    public static String getSuffixForNumber(int number) {
        if (number == 1) {
            return "st";
        } else if (number == 2) {
            return "nd";
        } else if (number == 3) {
            return "rd";
        } else {
            return "th";
        }
    }


    public static String getMobileNumber(Context context) {
        User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.USER_DETAILS, User.class);
        if (!TextUtils.isEmpty(userDetails.getMobile())) {
            return userDetails.getMobile();
        }
        return "";
    }

    public static long getUserId(Context context) {
        User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.USER_DETAILS, User.class);
        if (userDetails != null) {
            return userDetails.getUserId();
        } else {
            return 0;
        }
    }

    public static String getUserName(Context context) {
        User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.USER_DETAILS, User.class);
        if (!TextUtils.isEmpty(userDetails.getName())) {
            return userDetails.getName();
        }
        return "";
    }

    public static int getDpAsPixels(Context context, int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
        return dpAsPixels;
    }

    private static ImageView getTempImageView(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.temp_imge_layout, null);
        return (ImageView) convertView;
    }

    public static String getReferralCode(Context context) {
        String referralCode = "";
        User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.USER_DETAILS, User.class);
        if (!TextUtils.isEmpty(userDetails.getReferralCode())) {
            referralCode = userDetails.getReferralCode();
        }
        return referralCode;
    }


    public static void setWinGaBanner(final Context context, final long fileId) {
//        String filePath = Constants.GET_ADD_BANNER_URL;
        String filePath = Constants.GET_FOOTER_IMAGE_URL;
        if (filePath.contains("{footerAddId}")) {
            filePath = filePath.replace("{footerAddId}", fileId + "");
        }
        Logg.d(TAG, "** Banner >> " + filePath);
        Glide.with(context)
                .asBitmap()
                .load(filePath)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            // isFirstTime = false;
                            //  bannerIv.setImageBitmap(resource);
                            Bitmap bmp2 = resource.copy(resource.getConfig(), true);
                            WingaApplication.getInstance().setAdBannerBitmap(fileId);

                            Intent intent = new Intent();
                            intent.setAction(Constants.REFRESH_BANNER_ACTION);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }

                });
    }

    public static void shareCodeUsingFCMGeneratedURL(final Context context, final String invitationUrl) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        String description = "";
        if (setup != null) {
            description = setup.getAppShareText();
        }


        final String url = setup.getAppShareImage();
        if (description.contains("ref_code")) {
            description = description.replace("ref_code", getReferralCode(context));
        }
        final String finalDescription = description;

        String msgHtml = "";
        String PACKAGE_NAME = context.getApplicationContext().getPackageName();
        Log.v(TAG, "Package Name >> " + PACKAGE_NAME);

        if (!TextUtils.isEmpty(invitationUrl)) {
            msgHtml = finalDescription + " " + invitationUrl;
        } else {
            msgHtml = finalDescription
                    + "\n" + "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
        }
        Log.v(TAG, "msgHtml >> " + msgHtml);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msgHtml);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share) + " " + context.getString(R.string.app_name)));
    }


    public static void shareImageUsingGlide(final Context context, final String invitationUrl) {
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.SET_UP_DETAILS, Setup.class);
        String description = "";
        if (setup != null) {
            description = setup.getAppShareText();
        }


        final String url = setup.getAppShareImage();
        if (description.contains("ref_code")) {
            description = description.replace("ref_code", getReferralCode(context));
        }
        final String finalDescription = description;
        final Dialog progressDialog = MyDialog.showProgress(context);

        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        String directoryPath = Environment.getExternalStorageDirectory() + "/"
                                + context.getResources().getString(R.string.app_name) + "/img/";


                        try {
                            File imageDir = new File(directoryPath);
                            String imageFilePath = imageDir.getAbsolutePath() + "shareimage.png";
                            File imageFile = new File(imageFilePath);
                            if (!imageDir.exists()) {
                                imageDir.mkdirs();
                            }
                            if (!imageFile.exists()) {
                                imageFile.createNewFile();
                            }

                            resource = (resource == null ? BitmapFactory.decodeResource(context.getResources(), R.drawable.logo) : resource);
                            FileOutputStream outPutStream = new FileOutputStream(imageFile);
                            resource.compress(Bitmap.CompressFormat.PNG, 100, outPutStream);
                            outPutStream.flush();
                            outPutStream.close();
                            imageFile.setReadable(true, false);

                            Uri outputFileUri;
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);
                            } else {
                                outputFileUri = Uri.fromFile(imageFile);
                            }
                            progressDialog.dismiss();
                            String msgHtml = "";
                            String PACKAGE_NAME = context.getApplicationContext().getPackageName();
                            Log.v(TAG, "Package Name >> " + PACKAGE_NAME);
                            if (!TextUtils.isEmpty(invitationUrl)) {
                                msgHtml = finalDescription + " " + invitationUrl;
                            } else {
                                msgHtml = finalDescription
                                        + "\n" + "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
                            }

                            Logg.v(TAG, "msgHtml >> " + msgHtml);
                            Intent template = new Intent(android.content.Intent.ACTION_SEND);
                            template.setType("text/plain");


                            PackageManager pm = context.getPackageManager();
                            List<Intent> targets = new ArrayList<>();

                            List<ResolveInfo> resInfo = pm.queryIntentActivities(template, 0);
                            for (int i = 0; i < resInfo.size(); i++) {
                                ResolveInfo ri = resInfo.get(i);
                                String packageName = ri.activityInfo.packageName;
                                Log.i("Package Name", packageName);


                                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msgHtml);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                shareIntent.setType("text/plain");
                                shareIntent.setPackage(packageName);
                                targets.add(shareIntent);


                               /* if (packageName.contains("facebook") || packageName.contains("skype")) {
                                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msgHtml);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.setType("text/plain");
                                    shareIntent.setPackage(packageName);
                                    targets.add(shareIntent);
                                } else {
                                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    shareIntent.setType("image/*");
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
                                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msgHtml);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.setPackage(packageName);
                                    targets.add(shareIntent);
                                }*/
                            }

                            Intent chooser = Intent.createChooser(targets.remove(0), context.getString(R.string.share));
                            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
                            context.startActivity(chooser);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            MyDialog.showToast(context, context.getString(R.string.something_wrong));
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressDialog.dismiss();
                        MyDialog.showToast(context, context.getString(R.string.something_wrong));
                    }


                });
    }


    public static void sendPushRegId(Context context, VolleyResultCallBack volleyResultCallBack) {
        String refreshedToken = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.FIRE_BASE_TOKEN);
        String imei = "";
        imei = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.IMEI);
        Logg.v(TAG, "**sendPushRegId() > refreshedToken: " + refreshedToken);
        if (TextUtils.isEmpty(refreshedToken)) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();

        }
        User userDetails = (User) EightfoldsUtils.getInstance().getObjectFromSharedPreference(context, Constants.USER_DETAILS, User.class);

        if (userDetails != null && !TextUtils.isEmpty(refreshedToken)) {
            UserDevice userDevice = new UserDevice();
            userDevice.setPlatformId(Constants.ANDROID_PLATFORM_ID);
            if (!TextUtils.isEmpty(imei)) {
                userDevice.setImei(imei);
            } else {
                userDevice.setImei("");
            }
            userDevice.setPushRegId(refreshedToken);
            userDevice.setUserId(userDetails.getUserId() + "");
            if (EightfoldsUtils.getInstance().isNetworkAvailable(context)) {
                EightfoldsVolley.getInstance().makingJsonRequest(volleyResultCallBack, CommonResponse.class, Request.Method.POST, Constants.POST_DEVICE_INFO, userDevice);
            }
        }
    }



//    private void determineAdvertisingInfo(Context context) {
//        if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(context)) {
//            ListenableFuture<AdvertisingIdInfo> advertisingIdInfoListenableFuture =
//                    AdvertisingIdClient.getAdvertisingIdInfo(context);
//            Futures.addCallback(advertisingIdInfoListenableFuture,
//                    new FutureCallback<AdvertisingIdInfo>() {
//                        @Override
//                        public void onSuccess(AdvertisingIdInfo adInfo) {
//                            String id = adInfo.getId();
//                            String providerPackageName =
//                                    adInfo.getProviderPackageName();
//                            boolean isLimitTrackingEnabled =
//                                    adInfo.isLimitTrackingEnabled();
//
//                            // Any exceptions thrown by getAdvertisingIdInfo()
//                            // cause this method to get called.
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//                    });
//
//        }else {
//                // The Advertising ID client library is unavailable. Use a different
//                // library to perform any required ads use cases.
//            }
//        }

    private static void callLogout(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        EightfoldsUtils.getInstance().saveToSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN, null);
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.LOGIN_DATA, "");
        EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.USER_DETAILS, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.MOBILE_NUMBER, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.LOGIN_TYPE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.FIRST_GAME_PLAYED, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.INVITED_BY_REF_CODE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.READ_NOTIFICATION_IDS, "");

        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.APP_FIRST_LOGGEDIN_DATE, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.LOYALITY_POINTS, "");
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.DEVICE_INFO_LAST_UPDATED_TIME, "");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyDialog.showToast(context, context.getString(R.string.logged_out));
        context.startActivity(intent);
        //  ((Activity) context).finish();
    }

    public static void logOutUser(Context context, VolleyResultCallBack volleyResultCallBack) {
        String imei = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.IMEI);
        String url = Constants.LOG_OUT;
        if (!TextUtils.isEmpty(imei) && url.contains("{imei}")) {
            url = url.replace("{imei}", imei);
        } else {
            url = url.replace("{imei}", "");
        }
        if (EightfoldsUtils.getInstance().isNetworkAvailable(context)) {
            EightfoldsVolley.getInstance().makingStringRequest(null, volleyResultCallBack, CommonResponse.class, Request.Method.GET, url);
        }
        callLogout(context);
    }

    public static boolean isNotificationIsRead(Context context, Notification notification) {

        try {
            String firstLoggedInDate = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.APP_FIRST_LOGGEDIN_DATE);
            Date today = new Date();
            if (!TextUtils.isEmpty(firstLoggedInDate)) {
                long currentTimeMills = 0;
                try {
                    currentTimeMills = DateTime.getInMillies(firstLoggedInDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                today = new Date(currentTimeMills);
            }

            String notificationDate = notification.getCreatedTime();
            Date date = null;
            try {
                date = new Date(DateTime.getInMilliesFromUTC(notificationDate));

                if (date.before(today)) {
                    return true;

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String notificationsReadIds = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.READ_NOTIFICATION_IDS);

            String[] ids = new String[0];

            if (!TextUtils.isEmpty(notificationsReadIds)) {
                ids = notificationsReadIds.split(",");
            }
            for (String id : ids) {
                if (id.equals(notification.getNotificationId().toString())) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static ArrayList<Notification> setNotificationsReadStatus(Context context, ArrayList<Notification> notifications) {

        String newIds = "";
        try {
            String firstLoggedInDate = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.APP_FIRST_LOGGEDIN_DATE);
            Date today = new Date();
            if (!TextUtils.isEmpty(firstLoggedInDate)) {
                long currentTimeMills = 0;
                try {
                    currentTimeMills = DateTime.getInMillies(firstLoggedInDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                today = new Date(currentTimeMills);
            }

            String notificationsReadIds = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.READ_NOTIFICATION_IDS);
            newIds = notificationsReadIds;
            String[] ids = new String[0];

            if (!TextUtils.isEmpty(notificationsReadIds)) {
                ids = notificationsReadIds.split(",");
            }
            for (Notification notification : notifications) {
                String notificationDate = notification.getCreatedTime();
                Date date = new Date(DateTime.getInMilliesFromUTC(notificationDate));

                boolean isIDAlreadySaved = false;
                if (date.before(today)) {
                    notification.setRead(true);

                }
                for (String id : ids) {
                    if (notification.getNotificationId().toString().equalsIgnoreCase(id)) {
                        notification.setRead(true);
                        isIDAlreadySaved = true;
                        break;
                    }
                }

                if (!isIDAlreadySaved && notification.isRead()) {
                    if (!TextUtils.isEmpty(newIds)) {
                        newIds = newIds + "," + String.valueOf(notification.getNotificationId());
                    } else {
                        newIds = String.valueOf(notification.getNotificationId());
                    }
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.READ_NOTIFICATION_IDS, newIds);

        return notifications;
    }

    public static void makeNotificationAsRead(Context context, Long notificationId) {
        String notificationsReadIds = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.READ_NOTIFICATION_IDS);
        String[] ids = new String[0];

        if (!TextUtils.isEmpty(notificationsReadIds)) {
            ids = notificationsReadIds.split(",");
        }

        boolean idAlreadyAdded = false;
        for (String id : ids) {
            if (id.equals(notificationId.toString())) {
                idAlreadyAdded = true;
                break;
            }
        }

        if (!idAlreadyAdded) {
            String both = "";
            if (!TextUtils.isEmpty(notificationsReadIds)) {
                both = notificationsReadIds + "," + String.valueOf(notificationId);
            } else {
                both = String.valueOf(notificationId);
            }
            EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.READ_NOTIFICATION_IDS, both);
        }
    }


    public static void RefreshSetUpDetails(final Context context) {
        Logg.v(TAG, "***  RefreshBoardcast received");
        if (EightfoldsUtils.getInstance().isNetworkAvailable(context)) {
            String url = Constants.GET_SETUP;
            url = url.replace("{langId}", Utils.getCurrentLangId());

            EightfoldsVolley.getInstance().makingStringRequest(new VolleyResultCallBack() {
                @Override
                public void onVolleyResultListener(Object object, String requestType) {
                    Setup registerSetUpDetails = (Setup) object;
                    EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.SET_UP_DETAILS, registerSetUpDetails);
                }

                @Override
                public void onVolleyErrorListener(Object object) {

                }
            }, Setup.class, Request.Method.GET, url);


        }


    }

    public static void setProfileBGBasedOnLevel(Context context, int level, View profileRL) {
        switch (level) {
            case 1:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_one));
                break;
            case 2:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_two));
                break;
            case 3:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_three));
                break;
            case 4:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_four));
                break;
            case 5:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_five));
                break;
            case 6:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_six));
                break;
            case 7:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_seven));
                break;
            case 8:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_eight));
                break;
            case 9:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_eight));
                break;
            case 10:
                profileRL.setBackground(context.getResources().getDrawable(R.drawable.level_eight));
                break;

        }
    }

    public static void getDynamicLink(final Context context) {
        EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.INVITED_BY_REF_CODE, "");
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(((Activity) context).getIntent())
                .addOnSuccessListener((Activity) context, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            //((TextView)view.findViewById(R.id.tv_receivedLink)).setText(deepLink.toString());
                            //https://dynamiclinktest.eightfolds.in/?invitedby=OYWQN4
                            Logg.v(TAG, "deepLink>> " + deepLink);

                            String protocol = deepLink.getScheme();
                            String server = deepLink.getAuthority();
                            String path = deepLink.getPath();
                            Set<String> args = deepLink.getQueryParameterNames();

                            String invitationCode = deepLink.getQueryParameter("invitedby");
                            Logg.v(TAG, "invitationCode>> " + invitationCode);

                            if (!TextUtils.isEmpty(invitationCode)) {
                                EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.INVITED_BY_REF_CODE, invitationCode);
                            }

                        }
                    }
                });

    }

    public static void generateDynamicLink(String invitedBy, OnSuccessListener onSuccessListener) {
        String link = BuildConfig.DYNAMIC_LINK + "invitedby=" + invitedBy; //BuildConfig.DYNAMIC_LINK
        String PACKAGE_NAME = WingaApplication.getInstance().getPackageName();
        Logg.v(TAG, "link >> " + link);

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDynamicLinkDomain(Constants.DYNAMIC_LINK_DOMAIN)
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(PACKAGE_NAME)
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("in.eightfolds.WinGaLive")
                                .setAppStoreId("1424931697")
                                .setMinimumVersion("1")
                                .setFallbackUrl(Uri.parse(Constants.VEGGA_WEB_SITE_URL))
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Logg.v(TAG, "** generateDynamicLink >> onFailure ");
                    }
                });
    }


    public static void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_NAME_WINGA)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Logg.d(TAG, "topic subscribed succesfully");
                        } else {
                            Logg.d(TAG, "topic subscription failed");
                        }
                    }
                });

    }

    public static void subscribeToTopic(String topicName, OnCompleteListener<Void> onCompleteListener) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnCompleteListener(onCompleteListener);
    }

    public static void unSubscribeToTopic(String topicName, OnCompleteListener<Void> onCompleteListener) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName).addOnCompleteListener(onCompleteListener);
    }

    public static void subscribeToTopic(String topicName) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Logg.d(TAG, "topic subscribed succesfully");
                } else {
                    Logg.d(TAG, "topic subscription failed");
                }
            }
        });
    }

    public static void unSubscribeToTopic(String topicName) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Logg.d(TAG, "topic Unsubscribed succesfully");
                } else {
                    Logg.d(TAG, "topic Unsubscription failed");
                }
            }
        });
    }

    public static void subscribeToAlertsPromosUpdates(LoginData userResponse) {
        Utils.subscribeToTopic(Constants.TOPIC_NAME_WINGA);
        if (userResponse.isAlert()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_ALERTS);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_ALERTS);
        }

        if (userResponse.isPromotion()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
        }

        if (userResponse.isUpdates()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_UPDATES);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_UPDATES);
        }
    }

    public static void subscribeToAlertsPromosUpdates(User userResponse) {
        Utils.subscribeToTopic(Constants.TOPIC_NAME_WINGA);
        if (userResponse.isAlert()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_ALERTS);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_ALERTS);
        }

        if (userResponse.isPromotion()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_PROMOTIONS);
        }

        if (userResponse.isUpdates()) {
            Utils.subscribeToTopic(Constants.TOPIC_NAME_UPDATES);
        } else {
            Utils.unSubscribeToTopic(Constants.TOPIC_NAME_UPDATES);
        }
    }

    private static int getDpInPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public static void adjustPointsFontSizeAndBg(Context context, String points, RelativeLayout pointsRL, TextView pointTv) {


       /* RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
               context.getResources().getDimension(R.dimen.profile_tab_width),  context.getResources().getDimension(R.dimen.profile_tab_height));*/
        //rel_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //pointsRL.setLayoutParams(rel_btn);
        pointTv.setTextSize(15);

        if (points.length() == 6) {
            pointTv.setPadding(0, 0, getDpInPixels(context, 15), 0);
        } else if (points.length() == 7) {
            pointTv.setPadding(0, 0, getDpInPixels(context, 8), 0);
        } else if (points.length() == 8) {
            pointTv.setTextSize(13);
            pointTv.setPadding(0, 0, getDpInPixels(context, 3), 0);
        } else if (points.length() == 9) {

            pointTv.setTextSize(12);
            pointTv.setPadding(0, 0, getDpInPixels(context, 3), 0);

        } else if (points.length() == 10) {
          /*  rel_btn = new RelativeLayout.LayoutParams(
                    getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_width)), getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_height)));
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            pointsRL.setLayoutParams(rel_btn);*/
            pointTv.setTextSize(10);
            pointTv.setPadding(0, 0, getDpInPixels(context, 3), 0);
        } else if (points.length() > 10 && points.length() < 15) {
            /*rel_btn = new RelativeLayout.LayoutParams(
                    getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_width)), getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_height)));
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            pointsRL.setLayoutParams(rel_btn);*/
            pointTv.setTextSize(9);
            pointTv.setPadding(0, 0, getDpInPixels(context, 3), 0);
        } else if (points.length() >= 15) {
          /*  rel_btn = new RelativeLayout.LayoutParams(
                    getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_width)), getDpInPixels(context, context.getResources().getDimension(R.dimen.profile_tab_height)));
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            pointsRL.setLayoutParams(rel_btn);*/
            pointTv.setTextSize(8);
            pointTv.setPadding(0, 0, getDpInPixels(context, 3), 0);
        }
        pointTv.setText(points);
    }


    public static long getDifferenceBetweenDatesInMins(String startDate, String endDate) {
        long sessionEndTimeMillis = 0;
        try {
            sessionEndTimeMillis = DateTime.getInMillies(endDate);
            long currentTimeMills = DateTime.getInMillies(startDate);
            return Utils.getDifferenceBetweenDatesInMins(currentTimeMills, sessionEndTimeMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static long getDifferenceBetweenDatesInMills(String startDate, String endDate) {
        long sessionEndTimeMillis = 0;
        try {
            sessionEndTimeMillis = DateTime.getInMillies(endDate);
            long currentTimeMills = DateTime.getInMillies(startDate);
            return sessionEndTimeMillis - currentTimeMills;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDifferenceBetweenDatesInMins(long startDate, long endDate) {

        long different = endDate - startDate;

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;


        long publishTimeRemainingMinutes = (elapsedDays * 24 * 60) + (elapsedHours * 60) + elapsedMinutes;
        return publishTimeRemainingMinutes;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    public static void openPlayStoreToUpdateYoutubeApp(Context context) {
        final String appPackageName = Constants.Youtube_Package_Name;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    public static void saveAppFirstLoggedInDate(Context context, CategoryHomePageResponse homePageResponse) {
        //This is to Maintain Notifications ReadState

        if (homePageResponse != null && !TextUtils.isEmpty(homePageResponse.getCurrentTime())) {
            String savedCurrentTime = EightfoldsUtils.getInstance().getFromSharedPreference(context, Constants.APP_FIRST_LOGGEDIN_DATE);
            if (TextUtils.isEmpty(savedCurrentTime)) {
                EightfoldsUtils.getInstance().saveToSharedPreference(context, Constants.APP_FIRST_LOGGEDIN_DATE, homePageResponse.getCurrentTime());
            }
        }
    }

    public static void refreshAccessToken(final Context context) {
        String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN);
        if (!TextUtils.isEmpty(accessToken) && accessToken.contains(".")) {
            String[] splitToken = accessToken.split("\\.");
            if (splitToken.length == 3) {
                String tokenJson = splitToken[1];
                if (!TextUtils.isEmpty(tokenJson)) {
                    byte[] data = Base64.decode(tokenJson, Base64.DEFAULT);
                    try {
                        String base64DecodedJsonToken = new String(data, "UTF-8");
                        Logg.v(TAG, "base64DecodedJsonToken>> " + base64DecodedJsonToken);
                        if (!TextUtils.isEmpty(base64DecodedJsonToken)) {

                            JSONObject jsonObject = new JSONObject(base64DecodedJsonToken);
                            if (jsonObject.has("exp")) {
                                long exp = jsonObject.getLong("exp");

                                long systemMills = System.currentTimeMillis();
                                long difference = exp - systemMills;

                                Logg.v(TAG, "Expiry >> " + difference);

                                long millsInOneDay = 2 * 24 * 60 * 60 * 1000;

                                if (difference < millsInOneDay) {
                                    String url = Constants.LOGIN_URL;
                                    url = url.replace("{langId}", Utils.getCurrentLangId());
                                    EightfoldsVolley.getInstance().makingStringRequest(new VolleyResultCallBack() {
                                        @Override
                                        public void onVolleyResultListener(Object object, String requestType) {
                                            LoginData loginData = (LoginData) object;

                                            EightfoldsUtils.getInstance().saveToSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN, loginData.getAccessToken());
                                            EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.LOGIN_DATA, loginData);
                                            EightfoldsUtils.getInstance().saveObjectToSharedPreference(context, Constants.USER_DETAILS, loginData.getUser());
                                        }

                                        @Override
                                        public void onVolleyErrorListener(Object object) {

                                        }
                                    }, LoginData.class, Request.Method.GET, url);

                                }
                            }

                            /*{
  "iss": "7013755614",
  "exp": 1538298506, //86400000
  "jti": "Lqy96f3ljuwrroToIeeUYg",
  "iat": 1535706506,
  "userName": "7013755614",
  "userId": 283
}*/
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.v(TAG, "*&*  Resolution > width : " + width + " , height:  " + height);


        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Log.v(TAG, "*&* Large screen");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Log.v(TAG, "*&* Normal screen");
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Log.v(TAG, "*&* Small screen");
                break;
            default:
                Log.v(TAG, "*&* Screen size is neither large, normal or small");

        }
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                Log.v(TAG, "*&* Screen: LDPI");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.v(TAG, "*&* Screen: MDPI");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.v(TAG, "*&* Screen: HDPI");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.v(TAG, "*&* Screen: XHDPI");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.v(TAG, "*&* Screen: XXHIGH");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.v(TAG, "*&* Screen: XXXHIGH");
                break;
            case DisplayMetrics.DENSITY_340:
                Log.v(TAG, "*&* Screen: DENSITY_340");
                break;
            case DisplayMetrics.DENSITY_400:
                Log.v(TAG, "*&* Screen: DENSITY_400");
                break;
            case DisplayMetrics.DENSITY_420:
                Log.v(TAG, "*&* Screen: DENSITY_420");
                break;
            case DisplayMetrics.DENSITY_560:
                Log.v(TAG, "*&* Screen: DENSITY_560");
                break;
            default:
                Log.v(TAG, "*&* Screen: **");
                break;
        }

        int densityDpi = (int) (metrics.density * 160f);
        Log.v(TAG, "*&* metric density> : **" + metrics.density);

        Log.v(TAG, "*&* densityDpi> : **" + densityDpi);

    }


    public static ArrayList<AgeRange> getGendersList(Context context) {
        ArrayList<AgeRange> genders = new ArrayList<>();
        AgeRange maleGender = new AgeRange();
        maleGender.setAgeRangeId((long) 1);
        maleGender.setTitle(context.getString(R.string.male));
        genders.add(maleGender);

        AgeRange femaleGender = new AgeRange();
        femaleGender.setAgeRangeId((long) 2);
        femaleGender.setTitle(context.getString(R.string.female));
        genders.add(femaleGender);
        return genders;
    }

    public static void openInBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static UserLoggedDevice getUserDeviceInfo(Context context) {
        Context mContext = context;
        if (mContext == null) mContext = WingaApplication.getInstance().getApplicationContext();
        UserLoggedDevice userDeviceInfo = new UserLoggedDevice();
        String imei = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        userDeviceInfo.setImei(imei);
        userDeviceInfo.setPlatform(Constants.ANDROID_PLATFORM_ID + "");
        userDeviceInfo.setAppVersion(BuildConfig.VERSION_NAME);
        userDeviceInfo.setManufacturer(Build.MANUFACTURER);
        userDeviceInfo.setModelNumber(Build.MODEL);
        userDeviceInfo.setOsVersion(Build.VERSION.RELEASE);

        return userDeviceInfo;
    }
}




