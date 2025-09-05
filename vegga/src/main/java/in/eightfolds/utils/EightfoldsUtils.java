package in.eightfolds.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.eightfolds.winga.BuildConfig;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;


/**
 * Created by Sanjay on 11/3/2015.
 */
public class EightfoldsUtils {
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS_STATE = 8 ;
    public static int REQUEST_CHECK_SETTINGS = 2002;
    private static String TAG = "EightfoldsUtils";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_READ_PHONE_STATE = 3;
    public static final int REQUEST_READ_SMS = 4;
    public static final int PERMISSION_REQUEST_CODE = 2;
    public static final int WRITE_STORA_PERMISSION_REQ_CODE = 5;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static String[] READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE
    };

    public static String[] READ_PHONE_STATE_AND_SMS = {
            Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };
    public static String[] READ_SMS = {
            Manifest.permission.READ_SMS
    };
    public static final int REQUEST_EXTERNAL_LOCATION = 592;

    private static EightfoldsUtils utils;

    private EightfoldsUtils() {
    }

    public static EightfoldsUtils getInstance() {
        if (utils == null) {
            utils = new EightfoldsUtils();
        }
        return utils;
    }

    /**
     * Check for network availability
     *
     * @param context Activity context
     * @return true if network available else false.Ei
     */
    public boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(context, false);
    }

    public boolean isNetworkAvailable(Context context, boolean hideToast) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        try {
            if (EightfoldsUtils.isAppOnForeground(context) && !hideToast) {
                MyDialog.showToast(context, "No network available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }















    public String saveObjectToSharedPreference(Context context, String key, Object object) {
        try {
            String json = Api.toJson(object);
            EightfoldsUtils.getInstance().saveToSharedPreference(context, key, json);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObjectFromSharedPreference(Context context, String key, Class<?> clazz) {
        try {
            String json = EightfoldsUtils.getInstance().getFromSharedPreference(context, key);
            if (json != null) {
                Object object = Api.fromJson(json, clazz);
                return object;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save object as String to Shared Preference
     *
     * @param context
     * @param key     The name of the preference.
     * @param value   The value for the preference.
     */
    public void saveToSharedPreference(Context context, String key,
                                       Object value) {

        String localValue;
        if (context == null) {
            return;
        }
        if (value == null) {
            localValue = "";
        } else {
            localValue = value.toString();
        }

        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(Constants.MY_SUPER_SECRET_PASSWORD).build();

        preferences.edit()
                .putString(key, localValue).apply();

    }


    /**
     * Save boolean as String to Shared Preference
     *
     * @param context
     * @param key     The name of the preference.
     * @param value   The value for the preference.
     */
    public void saveBooleanToSharedPreference(Context context, String key,
                                       boolean value) {

        String localValue;
        if (context == null) {
            return;
        }

        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(Constants.MY_SUPER_SECRET_PASSWORD).build();

        preferences.edit()
                .putBoolean(key, value).apply();

    }


    /**
     * Clear every thing from Shared Preference
     *
     * @param context
     */
    public void clearSharedPreferences(Context context) {

        if (context != null) {
            EncryptedPreferences preferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(Constants.MY_SUPER_SECRET_PASSWORD).build();
            preferences.forceDeleteExistingPreferences();
        }
    }

    /**
     * Get String value from Shared Preference
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return Value for the given preference if exist else null.
     */
    public String getFromSharedPreference(Context context, String key) {
        if (context == null) {
            return null;
        }

        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(Constants.MY_SUPER_SECRET_PASSWORD).build();

        return preferences.getString(key, null);
    }

    /**
     * Get String value from Shared Preference
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return Value for the given preference if exist else null.
     */
    public boolean getBooleanFromSharedPreference(Context context, String key) {
        if (context == null) {
            return false;
        }

        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(Constants.MY_SUPER_SECRET_PASSWORD).build();

        return preferences.getBoolean(key, false);
    }


    /**
     * @param path Image path OR Image file id
     * @param url  File url
     * @return file path to show the image.
     */
    public String getImageFullPath(Object path, String url) {
        String imagePath = null;
        if (path instanceof String) {
            imagePath = (String) path;
            if (!imagePath.contains("file:///")) {
                imagePath = "file://" + imagePath;
            }
        } else if (path instanceof Long) {
            imagePath = url + path;
        }
        return imagePath;
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    public void showKeyboard(final Activity activity, final EditText editText) {
        if (editText.requestFocus()) {
            editText.post(new Runnable() {
                public void run() {
                    editText.requestFocusFromTouch();
                    InputMethodManager lManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(editText, 0);
                }
            });
           /* InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            editText.requestFocus();
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);*/

//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard(Activity activity) {
        hideKeyboard(activity, null);
    }

    /**
     * @param activity
     * @param view     Current focus view
     */
    public void hideKeyboard(Activity activity, View view) {
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

    /**
     * @param applicationContext
     * @param serviceClassName
     * @return true if service for the given class name is running else false.
     */
    public boolean isServiceRunning(Context applicationContext,
                                    String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) applicationContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(
                    serviceClassName)
                    && runningServiceInfo.started) {
                return true;
            }
        }

        return false;
    }


    /**
     * Convert Dp to Pixel
     */
    public int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * @param text
     * @return MD5 string
     */
    public String MD5(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    //

    public static boolean isValidPostalCode(String zipCode){
        return  zipCode.matches("(?i)^[a-z0-9][a-z0-9\\- ]{0,10}[a-z0-9]$");
    }


    public static boolean isAlphabetsAndSpaceOnly(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }

        return true;
        // or simply use below , but above one is faster
        // return name.matches("[a-zA-Z]+");
    }

    public static boolean isAlphaNumeric(String name){
        return  name.matches("[a-zA-Z0-9]+");
    }

    public static boolean isAllZeros(String name){
        return  name.matches("[0]");
    }

    /**
     * @param emailId Value to validate
     * @return true if its a valid email id else false
     */
    public boolean isValidEmail(CharSequence emailId) {
        if (!TextUtils.isEmpty(emailId)) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId)
                    .matches();
        }
        return false;
    }

    /**
     * Check availability of SD card
     *
     * @param context   Activity context
     * @param showToast
     * @return true if its a valid SD card
     */
    public static boolean isSDCardValid(Context context, boolean showToast) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        if (Environment.MEDIA_REMOVED.equals(state)) {
            if (showToast) {
                MyDialog.showToast(context, "SD card not present");
            }

            return false;
        }

        if (Environment.MEDIA_UNMOUNTED.equals(state)) {
            if (showToast) {
                MyDialog.showToast(context, "SD card not mounted");

            }

            return false;
        }

        if (showToast) {
            Toast.makeText(
                    context,
                    "The SD card in the device is in '" + state
                            + "' state, and cannot be used.", Toast.LENGTH_LONG)
                    .show();
        }

        return false;
    }

    /**
     * @param context
     * @param to      email id
     * @param subject
     * @param body
     */
    public void sendEmail(Context context, String to, String subject, String body) {
        if (TextUtils.isEmpty(subject)) {
            subject = "";
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", to, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    /**
     * @param context Activity context
     * @return true if location is ON else false
     */
    public boolean isLocationServiceOn(final Context context) {

        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;
        if (locationManager == null)
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (gps_enabled || network_enabled) {
            return true;
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Location service");

        // Setting Dialog Message
        alertDialog.setMessage("Please enable your location service.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
        return false;
    }

    /**
     * @param context
     * @param uri     The URI, using the content:// scheme, for the content to retrieve.
     * @return Absolute file path
     */
    @SuppressLint("Range")
    public String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return filePath;
    }

    /**
     * @param value
     * @return
     */
    public String getTwoDiditAfterPoint(double value) {
        String realValue = new DecimalFormat("##.##").format(value);
        if (("" + realValue).contains(".")) {
            return getTwoDiditAnyWayAfterPoint(value);
        } else {
            return realValue;
        }

//        return getTwoDiditAnyWayAfterPoint(value);
    }

    public String getTwoDiditAnyWayAfterPoint(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        return nf.format(value);
    }

    /**
     * Copy a file to another destination.
     *
     * @param context
     * @param src     source path
     * @param dst     destination path
     */
    public void copyFile(Context context, String src, String dst) {
        if (TextUtils.equals(src, dst)) {
            if (BuildConfig.DEBUG) {
                Logg.w(TAG, "Source (" + src + ") and destination (" + dst
                        + ") are the same. Skipping file copying.");
            }
            return;
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            Toast.makeText(
                    context,
                    "Failed to copy " + src + " to " + dst + ": "
                            + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    if (BuildConfig.DEBUG) {
                        Logg.w(TAG,
                                "Ignored the exception caught while closing input stream for "
                                        + src + ": " + e.getMessage(), e);
                    }
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    if (BuildConfig.DEBUG) {
                        Logg.w(TAG,
                                "Ignored the exception caught while closing output stream for "
                                        + dst + ": " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * @param context
     * @return
     */
    public String getTopmostActivity(Context context) {
        ActivityManager am = (ActivityManager) context.
                getSystemService(Activity.ACTIVITY_SERVICE);
        if (am != null) {
            String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
            String className = am.getRunningTasks(1).get(0).topActivity.getClassName();
            return className;
        }

        return "";
    }

    /**
     * Call to a given phone number
     *
     * @param context
     * @param phoneNo
     */
   /* public void callANumber(Context context, String phoneNo) {
//        <uses-permission android:name="android.permission.CALL_PHONE" />
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    2);
            return;
        }
        context.startActivity(callIntent);
    }*/


    /**
     * @param context
     * @param number
     * @param message
     */
    public void sendSMS(Context context, String number, String message) {
//        <uses-permission android:name="android.permission.SEND_SMS"/>
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Share image to other application
     *
     * @param context
     * @param imageUri
     * @param title    popup title.
     */
    public void shareImageToOtherApplication(Context context, Uri imageUri, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }


    /**
     * Share link to other application.
     *
     * @param context
     * @param url
     * @param title
     */
    public void shareLinkToOtherApplication(Context context, String url, String title) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, url);

        context.startActivity(Intent.createChooser(share, title));
    }

    /**
     * Set sound file as ring tone.
     *
     * @param context
     * @param filePath
     */
    public void setSoundFileAsRingtone(Context context, String filePath) throws Exception {
        File ringFile = new File(filePath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ringFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, ringFile.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.MediaColumns.SIZE, 215454);
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringFile.getAbsolutePath());
        context.getContentResolver().delete(
                uri,
                MediaStore.MediaColumns.DATA + "=\""
                        + ringFile.getAbsolutePath() + "\"", null);
        Uri newUri = context.getContentResolver().insert(uri, values);


        try {
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
           // Toast.makeText(context, "Ringtone set successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Coppy resource to a given location in phone
     *
     * @param context
     * @param resourceId
     * @param path
     * @throws IOException
     */
    public void copyRAWtoPhone(Context context, int resourceId, String path) throws IOException {
        InputStream in = context.getResources().openRawResource(resourceId);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Get facebook profile pic path
     *
     * @param userId
     * @return
     */
    public String getFacebookProfilePic(String userId) {
        return "http://graph.facebook.com/" + userId + "/picture?type=large";
    }

    public void cancelNotification(Context context, int notifyId) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nMgr.cancel(notifyId);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPerMission= ActivityCompat.checkSelfPermission(activity,Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED||cameraPerMission!=PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }
    }

    public boolean readSMSPermission(Activity context) {
        int readSMS = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        int receiveSMS = ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);

        if (readSMS != PackageManager.PERMISSION_GRANTED || receiveSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS },
                    MY_PERMISSIONS_REQUEST_READ_SMS_STATE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param context
     * @param uri
     * @return
     */
    public String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validatePassword(String password) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!'\\\"#$%&'()*+,-./:;<=>?@['\\']^_`{|}~]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }


    public boolean giveExternalStoragePermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORA_PERMISSION_REQ_CODE);
            return false;
        } else {
            return true;
        }
    }


    public boolean giveMapPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get device IMEI
     *
     * @param activity
     * @return Device IMEI
     */

    public String getDeviceIMEIWIthSMSPermission(Activity activity) {
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        READ_PHONE_STATE_AND_SMS ,
                        REQUEST_READ_PHONE_STATE
                );
                return null;
            } else {
                TelephonyManager mTelephonyMgr = (TelephonyManager) activity
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return mTelephonyMgr.getImei();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "";
    }



    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    public String getDeviceIMEI(Activity activity) {
//        try {
//            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                        activity,
//                        READ_PHONE_STATE ,
//                        REQUEST_READ_PHONE_STATE
//                );
//                return null;
//            } else {
//                TelephonyManager mTelephonyMgr = (TelephonyManager) activity
//                        .getSystemService(Context.TELEPHONY_SERVICE);
//                return mTelephonyMgr.getDeviceId();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        String imei=getDeviceIMEIWIthSMSPermission(activity);
        if(TextUtils.isEmpty(imei)) {
            imei = getDeviceId(activity);
        }else if(TextUtils.isEmpty(imei)){
            imei= Api.getRandomString(10);
            EightfoldsUtils.getInstance().saveToSharedPreference(activity,Constants.IMEI,imei);
        }
        return imei;
    }

    public boolean getSMSPermission(Activity activity) {
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        READ_SMS,
                        REQUEST_READ_SMS
                );
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean giveLocationPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_EXTERNAL_LOCATION
            );
            return false;
        } else {
            return true;
        }
    }


    /*public static void displayLocationSettingsRequest(final Context context, final OnEventListener onEventListener) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        onEventListener.onEventListener(LocationSettingsStatusCodes.SUCCESS);
                        Logg.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Logg.i(TAG, "Location settings are not satisfied. Show the user a progressDialog to upgrade location settings ");

                        try {
                            // Show the progressDialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Logg.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Logg.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }*/

    public boolean isValidMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile) && (mobile.length() < 10 || !mobile.matches("[0-9]+"))) {
            return false;
        }
        return true;
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    public static UserDeviceDetail buildUserDeviceDetail(Context context) {

        UserDeviceDetail userDeviceDetail = new UserDeviceDetail();

        String pushRegId= getInstance().getFromSharedPreference(context,Constants.FIRE_BASE_TOKEN);

        userDeviceDetail.setDeviceName(Build.DEVICE);
        userDeviceDetail.setDeviceMake(Build.MANUFACTURER);
        userDeviceDetail.setDeviceModel(Build.MODEL);
        userDeviceDetail.setBuildNumber(Build.ID);
        userDeviceDetail.setAppVersion(BuildConfig.VERSION_NAME);
        userDeviceDetail.setAndroidVersion(Build.VERSION.BASE_OS +" :: "+ Build.VERSION.CODENAME +" :: "+ Build.VERSION.SDK_INT +" :: "+ Build.VERSION.RELEASE);
        userDeviceDetail.setGaid(getAdId(context));
        userDeviceDetail.setIpv4(getIpv4Network());
        userDeviceDetail.setIpv6(getIpv6Network());
        userDeviceDetail.setPushRegId(pushRegId);

        userDeviceDetail.setUserAgentPhone(getJsonBuildObject());
        return userDeviceDetail;
    }



    public static  String getJsonBuildObject() {
        StringBuilder builder = new StringBuilder();
        builder.append("FINGERPRINT : " + Build.FINGERPRINT);
        builder.append(",");
        builder.append("BRAND : " + Build.BRAND);
        builder.append(",");
        builder.append("HOST : " + Build.HOST);
        builder.append(",");
        builder.append("PRODUCT : " + Build.PRODUCT);
        builder.append(",");
        builder.append("SERIAL : " + Build.SERIAL);
        builder.append(",");
        builder.append("DISPLAY : " + Build.DISPLAY);
        return builder.toString();
    }

    public static String getIpv4Network() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                       /* boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (true) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getIpv6Network() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                     /*   boolean isIPv4 = InetAddressUtils.isIPv6Address(sAddr);
                        if (true) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String address = info.getMacAddress();
        return address;


    }
  public static   String  getAdId(Context context){
        String adId="";
        try {


            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
            adId=advertisingIdInfo.getId();

        }catch (Exception e){
            e.printStackTrace();
        }
        return adId;
    }


}
