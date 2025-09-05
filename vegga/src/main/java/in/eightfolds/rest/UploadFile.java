package in.eightfolds.rest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

/*import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.eightfolds.utils.Api;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.AppFile;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;

/*import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.TOTAL_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOADED_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;*/


public class UploadFile {
//        implements UploadStatusDelegate {


   /* private static UploadFile uploadFile;
    Context context;

    MultipartUploadRequest multipartUploadRequest = null;

    List<Long> fileIds = new ArrayList<>();
    int count, uploadType;
    UploadStatusDelegate uploadStatusDelegate = null;
    List<String> pathsList = new ArrayList<>();
    Dialog dialog;
    VolleyResultCallBack volleyResultCallBack;


    public static UploadFile getInstance() {

        if (uploadFile == null) {
            uploadFile = new UploadFile();

        }

        return uploadFile;
    }

    private static UploadNotificationConfig getNotificationConfig(Context context) {
        UploadNotificationConfig config = new UploadNotificationConfig();


        config.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                + " at " + UPLOAD_RATE + " - " + PROGRESS;
        config.getProgress().iconResourceID = android.R.drawable.stat_sys_upload;
        config.getProgress().iconColorResourceID = context.getResources().getColor(R.color.app_theme_red);

        config.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
        config.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;
        config.getCompleted().iconColorResourceID = Color.GREEN;

        return config;
    }


    public static void uploadMultipart(final Context context, String path, final VolleyResultCallBack volleyResultCallBack, boolean showProgress) {

        final Dialog myProgressDialog =  MyDialog.showProgress(context);
        try {

            if (showProgress) {
                myProgressDialog.show();
            }
            Logg.v("upload","Path>> "+path);

            String uploadId =
                    new MultipartUploadRequest(context, Constants.UPLOAD_FILE_URL)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(path, "file")

                            .setNotificationConfig(getNotificationConfig(context))   // @android:drawable/stat_sys_upload  //android.R.drawable.stat_sys_download
                            .setMaxRetries(2)
                            .setMethod("POST")
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    Logg.i("onProgress", "onProgress");

                                    uploadInfo.getProgressPercent();
                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                   Logg.i("onError", "onError" + exception.toString());

                                    volleyResultCallBack.onVolleyErrorListener(context.getString(R.string.something_wrong));
                                    if (myProgressDialog != null)
                                        myProgressDialog.dismiss();
                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Logg.i("onCompleted", "onCompleted");

//                                    count++;
//                                    uploadMultipart(MainActivity.this);
                                    AppFile appFile = null;
                                    if (myProgressDialog != null)
                                        myProgressDialog.dismiss();

                                    try {
                                        appFile = (AppFile) Api.fromJson(serverResponse.getBodyAsString(), AppFile.class);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    volleyResultCallBack.onVolleyResultListener(appFile, Constants.UPLOAD_FILE_URL);

                                    Logg.i("onCompleted", "" + serverResponse.getBodyAsString());



                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                    Logg.i("onCancelled", "onCancelled");

                                    if (myProgressDialog != null)
                                        myProgressDialog.dismiss();

                                    volleyResultCallBack.onVolleyErrorListener("Upload cancelled");
                                }
                            })
                            .startUpload();

        } catch (Exception exc) {
            Logg.e("AndroidUploadService", exc.getMessage(), exc);
            if (myProgressDialog != null)
                myProgressDialog.dismiss();
            volleyResultCallBack.onVolleyErrorListener(context.getString(R.string.something_wrong));
        }
    }


    public static void uploadMultipartWithOutNotification(final Context context, String path, final VolleyResultCallBack volleyResultCallBack, boolean showProgress,UploadStatusDelegate uploadStatusDelegate) {

        //final Dialog myProgressDialog =  MyDialog.showProgress(context);
        try {

           *//* if (showProgress) {
                myProgressDialog.show();
            }*//*

            String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN);

            String uploadId =
                    new MultipartUploadRequest(context, Constants.UPLOAD_FILE_URL)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(path, "file")
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .setMaxRetries(2)
                            .setMethod("POST")
                            .setDelegate(uploadStatusDelegate)
                            .startUpload();

        } catch (Exception exc) {
           *//* if (myProgressDialog != null)
                myProgressDialog.dismiss();*//*
            Logg.e("AndroidUploadService", exc.getMessage(), exc);
            volleyResultCallBack.onVolleyErrorListener(context.getString(R.string.something_wrong));
        }
    }





  *//*  public void uploadMultipleFilestFromPath(final Context context, List<String> pathsList, final VolleyResultCallBack volleyResultCallBack, boolean showProgress, int uploadType) {

        count = pathsList.size();
        this.pathsList = pathsList;
        this.volleyResultCallBack = volleyResultCallBack;
        dialog =  MyProgressDialogNew.getIconDialog(context);
        this.context = context;
        this.uploadType = uploadType;

        fileIds.clear();


        if (showProgress) {
            dialog.show();
        }

        if (count == pathsList.size()) {
            count--;
            getMultipartUploadRequest(context, this, pathsList.get(count), uploadType).startUpload();
        }


    }*//*

    *//*public void uploadMultipleFilesFromUri(final Context context, List<Uri> pathsUri, final VolleyResultCallBack volleyResultCallBack, boolean showProgress, int uploadType) {


        this.pathsList = EightfoldsUtils.getInstance().getPathsListFromUriList(context, pathsUri);
        this.volleyResultCallBack = volleyResultCallBack;
        dialog =  MyProgressDialogNew.getIconDialog(context);
        count = pathsList.size();
        this.context = context;
        this.uploadType = uploadType;

        fileIds.clear();

        if (showProgress) {
            dialog.show();
        }

        if (count == pathsList.size() && count > 0) {
            count--;
            getMultipartUploadRequest(context, this, pathsList.get(count), uploadType).startUpload();
        }


    }


   *//*


    public MultipartUploadRequest getMultipartUploadRequest(Context context, UploadStatusDelegate uploadStatusDelegate, String path, int uploadType) {

        String url;
        String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(context, EightfoldsVolley.ACCESS_TOKEN);

        url = Constants.UPLOAD_FILE_URL;



        try {
            MultipartUploadRequest myMultipartUploadRequest =
                    new MultipartUploadRequest(context, url)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(path, "file")
                            .addParameter("type", "1")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .setMaxRetries(2)
                            .setMethod("POST")
                            .setDelegate(uploadStatusDelegate);

            return myMultipartUploadRequest;
        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null)
                dialog.dismiss();
//            Logg.e("AndroidUploadService", e.getMessage(), e);
            volleyResultCallBack.onVolleyErrorListener(context.getString(R.string.something_wrong));
            return null;
        }

    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {

        Logg.i("onProgress", "onProgress");
        uploadInfo.getProgressPercent();

    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

//        volleyResultCallBack.onVolleyErrorListener(context.getString(R.string.something_wrong));
//        if (dialog != null)
//            dialog.dismiss();


        if (count > 0) {

            count--;
            getMultipartUploadRequest(context, this, pathsList.get(count), uploadType).startUpload();

        } else {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            volleyResultCallBack.onVolleyResultListener(fileIds, Constants.UPLOAD_FILE_URL);
        }


    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {


        AppFile appFile = null;

        try {
            appFile = (AppFile) Api.fromJson(serverResponse.getBodyAsString(), AppFile.class);
            fileIds.add(appFile.getFileId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (count > 0) {

            count--;
            getMultipartUploadRequest(context, this, pathsList.get(count), uploadType).startUpload();

        } else {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            volleyResultCallBack.onVolleyResultListener(fileIds, Constants.UPLOAD_FILE_URL);
        }


        Logg.i("onCompleted", "" + serverResponse.getBodyAsString());


    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        Logg.i("onCancelled", "onCancelled");

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        volleyResultCallBack.onVolleyErrorListener("Upload cancelled");
    }*/
}
