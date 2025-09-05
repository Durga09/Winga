package in.eightfolds.winga.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.RedemptionOptionsAdapter;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.PrizeWin;
import in.eightfolds.winga.model.RedemptionOption;
import in.eightfolds.winga.v2.model.BannerResponse;
import in.eightfolds.winga.v2.model.GuessAndWinResponse;
import in.eightfolds.winga.v2.model.SpinWinResponse;
import in.eightfolds.winga.v2.model.StreamingWinResponse;

/**
 * Created by Swapnika on 04-May-18.
 */
public class MyDialog {

    public static ProgressDialog showProgress(Context context) {
        try {
            ProgressDialog mdialog = ProgressDialog.show(context, "", "", true);
            mdialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View localView = inflater.inflate(R.layout.progress_bar, null);
            mdialog.setContentView(localView);
            return mdialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void showAlertDialog(final Context context, final OnEventListener onEventListener, String title, String body, String yesButtonName) {
        final Dialog commonDialog = new Dialog(context);
        commonDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = commonDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        commonDialog.setContentView(R.layout.alert_popup);
        TextView titletext = commonDialog.findViewById(R.id.titleExit);
        TextView bodyText = commonDialog.findViewById(R.id.body);
        TextView no = commonDialog.findViewById(R.id.noText);
        TextView yes = commonDialog.findViewById(R.id.yesTv);
        commonDialog.setCancelable(true);
        commonDialog.setCanceledOnTouchOutside(true);
        yes.setText(yesButtonName);
        titletext.setText(title);
        bodyText.setText(body);
        no.setVisibility(View.GONE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onEventListener != null) {
                    onEventListener.onEventListener(R.id.yesTv);
                }
                commonDialog.dismiss();
            }
        });


        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        if (displayMetrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            width = display.getWidth() - 100;
        } else {
            width = display.getWidth() - 250;
        }

        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        commonDialog.show();
    }

    public static void showCommonDialog(final Context context, final OnEventListener onEventListener, String title, String body, String yesButtonName) {
        final Dialog commonDialog = new Dialog(context);
        commonDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = commonDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        commonDialog.setContentView(R.layout.alert_popup);
        TextView titletext = commonDialog.findViewById(R.id.titleExit);
        TextView bodyText = commonDialog.findViewById(R.id.body);
        TextView no = commonDialog.findViewById(R.id.noText);
        TextView yes = commonDialog.findViewById(R.id.yesTv);
        commonDialog.setCancelable(false);
        commonDialog.setCanceledOnTouchOutside(false);
        yes.setText(yesButtonName);
        titletext.setText(title);
        bodyText.setText(body);
        no.setVisibility(View.GONE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onEventListener != null) {
                    onEventListener.onEventListener(R.id.yesTv);
                }
                commonDialog.dismiss();
            }
        });


        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        if (displayMetrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            width = display.getWidth() - 100;
        } else {
            width = display.getWidth() - 250;
        }

        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        commonDialog.show();
    }

    public static void showCommonDialogWithType(final Context context, final OnEventListener onEventListener, String title, String body, String yesButtonName, final int type) {
        final Dialog commonDialog = new Dialog(context);
        commonDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = commonDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        commonDialog.setContentView(R.layout.alert_popup);
        TextView titletext = commonDialog.findViewById(R.id.titleExit);
        TextView bodyText = commonDialog.findViewById(R.id.body);
        TextView no = commonDialog.findViewById(R.id.noText);
        TextView yes = commonDialog.findViewById(R.id.yesTv);
        commonDialog.setCancelable(false);
        commonDialog.setCanceledOnTouchOutside(false);
        yes.setText(yesButtonName);
        titletext.setText(title);
        bodyText.setText(body);
        no.setVisibility(View.GONE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEventListener.onEventListener(R.id.yesTv, type);
                commonDialog.dismiss();
            }
        });


        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        commonDialog.show();
    }


    public static void showTwoButtonDialog(final Context context, final Object object, final OnEventListener onEventListener, String title, String body, String yesButtonName, String noButtonName) {
        final Dialog exitDialog = new Dialog(context);
        exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = exitDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        exitDialog.setContentView(R.layout.alert_popup);
        TextView titletext = exitDialog.findViewById(R.id.titleExit);
        TextView bodyText = exitDialog.findViewById(R.id.body);
        TextView no = exitDialog.findViewById(R.id.noText);
        TextView yes = exitDialog.findViewById(R.id.yesTv);
        yes.setText(yesButtonName);
        no.setText(noButtonName);
        titletext.setText(title);
        bodyText.setText(body);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEventListener.onEventListener(R.id.yesTv, object);
                exitDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.noText);
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        exitDialog.show();
    }

    public static void showTwoButtonDialog(final Context context, final OnEventListener onEventListener, String title, String body, String yesButtonName, String noButtonName) {
        final Dialog exitDialog = new Dialog(context);
        exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = exitDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        exitDialog.setContentView(R.layout.alert_popup);
        TextView titletext = exitDialog.findViewById(R.id.titleExit);
        TextView bodyText = exitDialog.findViewById(R.id.body);
        TextView no = exitDialog.findViewById(R.id.noText);
        TextView yes = exitDialog.findViewById(R.id.yesTv);
        yes.setText(yesButtonName);
        no.setText(noButtonName);
        titletext.setText(title);
        bodyText.setText(body);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEventListener.onEventListener(R.id.yesTv);
                exitDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.noText);
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;

        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        exitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        exitDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showButtonDialog(final Context context, final OnEventListener onEventListener, String title, String body, String yesButtonName, String noButtonName) {
        final Dialog exitDialog = new Dialog(context);
        exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = exitDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        exitDialog.setContentView(R.layout.alert_popup);
        TextView titletext = exitDialog.findViewById(R.id.titleExit);
        TextView bodyText = exitDialog.findViewById(R.id.body);
        TextView no = exitDialog.findViewById(R.id.noText);
        TextView yes = exitDialog.findViewById(R.id.yesTv);

        View view = exitDialog.findViewById(R.id.title);
        yes.setText(yesButtonName);
        no.setText(noButtonName);
        titletext.setVisibility(View.GONE);
        bodyText.setText(Html.fromHtml(body, HtmlCompat.FROM_HTML_MODE_LEGACY));
        view.setVisibility(View.GONE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEventListener.onEventListener(R.id.yesTv);
                exitDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.noText);
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        exitDialog.setCancelable(false);
        exitDialog.show();
    }

    public static void showVersionUpdateDialog(final Context context, final OnEventListener onEventListener, boolean isForcefull) {
        final Dialog exitDialog = new Dialog(context);
        exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = exitDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.rectangle_bg);
        exitDialog.setContentView(R.layout.alert_popup);
        TextView titletext = exitDialog.findViewById(R.id.titleExit);
        TextView bodyText = exitDialog.findViewById(R.id.body);
        TextView no = exitDialog.findViewById(R.id.noText);
        TextView yes = exitDialog.findViewById(R.id.yesTv);
        // yes.setText(yesButtonName);
        // no.setText(noButtonName);
        titletext.setText(context.getString(R.string.update));
        bodyText.setText(context.getString(R.string.you_have_an_update));

        if (isForcefull) {
            no.setVisibility(View.GONE);
            yes.setText(context.getString(R.string.update));
            exitDialog.setCancelable(false);
            exitDialog.setCanceledOnTouchOutside(false);
        } else {
            no.setText(context.getString(R.string.skip));
            yes.setText(context.getString(R.string.update));
            exitDialog.setCancelable(false);
            exitDialog.setCanceledOnTouchOutside(false);
        }
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PACKAGE_NAME = Utils.getPackageName(context);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                // onEventListener.onEventListener(R.id.yesTv);
                // exitDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.noText);
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        exitDialog.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        /*Snackbar snackbar = Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), message, Toast.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.transcolorBlack_eighty));
        snackbar.show();*/

    }

    private static Calendar myCalendar;

    public static void showDatePicker(Context context, long yesterdayMillis, final Calendar selectedDate, final OnEventListener onEventListener) {
        myCalendar = Calendar.getInstance();
        if (selectedDate != null) {
            myCalendar = selectedDate;
        }


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (onEventListener != null) {
                    onEventListener.onEventListener(0, myCalendar);
                }
                // updateLabel();
            }

        };

//AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(yesterdayMillis);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.show();
    }


    public static Dialog logoutDialog(final Context context, final OnEventListener onEventListener, String title, String body, final boolean isForLogout) {
        final Dialog exitDialog = new Dialog(context);
        exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = exitDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        exitDialog.setContentView(R.layout.alert_popup);
        TextView titletext = exitDialog.findViewById(R.id.titleExit);
        TextView bodyText = exitDialog.findViewById(R.id.body);
        TextView no = exitDialog.findViewById(R.id.noText);
        TextView yes = exitDialog.findViewById(R.id.yesTv);
        titletext.setText(title);
        bodyText.setText(body);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.noText);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                onEventListener.onEventListener(R.id.yesTv);

            }
        });


        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        if (displayMetrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            width = display.getWidth() - 100;
        } else {
            width = display.getWidth() - 250;
        }


        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        try {
            exitDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitDialog;
    }


    public static void NotificationsDialog(final Context context, final OnEventListener onEventListener, String title, String body, final boolean isChecked) {
        final Dialog notificationsDialog = new Dialog(context);
        notificationsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = notificationsDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        notificationsDialog.setContentView(R.layout.alert_popup);
        TextView titletext = notificationsDialog.findViewById(R.id.titleExit);
        TextView bodyText = notificationsDialog.findViewById(R.id.body);
        TextView no = notificationsDialog.findViewById(R.id.noText);
        TextView yes = notificationsDialog.findViewById(R.id.yesTv);
        titletext.setText(title);
        bodyText.setText(body);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventListener.onEventListener(R.id.noText, !isChecked);
                notificationsDialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEventListener.onEventListener(R.id.yesTv, isChecked);
                notificationsDialog.dismiss();
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);

        notificationsDialog.show();
    }

    public static void uploadPhotosDialog(final Context context, final OnEventListener onEventListener, boolean hasProfilePic) {

        final Dialog uploadPhotosDialog = new Dialog(context);
        uploadPhotosDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = uploadPhotosDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.roundcorner_bg);
        uploadPhotosDialog.setContentView(R.layout.upload_photos_dialog);
        LinearLayout galleryLL = uploadPhotosDialog.findViewById(R.id.galleryLL);
        LinearLayout cameraLL = uploadPhotosDialog.findViewById(R.id.cameraLL);
        LinearLayout removePhotoLL = uploadPhotosDialog.findViewById(R.id.removePhotoLL);
        cameraLL.setVisibility(View.GONE);
        if (!hasProfilePic) {
            removePhotoLL.setVisibility(View.GONE);
        }
        galleryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventListener.onEventListener(R.id.galleryLL);
                uploadPhotosDialog.dismiss();
            }
        });
        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventListener.onEventListener(R.id.cameraLL);
                uploadPhotosDialog.dismiss();
            }
        });
        removePhotoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventListener.onEventListener(R.id.removePhotoLL);
                uploadPhotosDialog.dismiss();
            }
        });


        uploadPhotosDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }


    public static void showLoseDialog(final Context context, final OnEventListener onEventListener) {

        final Dialog loseDialog = new Dialog(context);
        loseDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = loseDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        loseDialog.setContentView(R.layout.lose_dialog);
        loseDialog.setCanceledOnTouchOutside(false);
        loseDialog.setCancelable(false);
        loseDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = loseDialog.findViewById(R.id.continueBtn);
        TextView exitTv = loseDialog.findViewById(R.id.exitTv);
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });

        loseDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        // int width = display.getWidth() - 80;
        //int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }


    public static Dialog showWonDialog(final Context context, String pointsWon, final OnEventListener onEventListener) {


        final Dialog winDialog = new Dialog(context);

        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        winDialog.setContentView(R.layout.won_dialog);
        winDialog.setCanceledOnTouchOutside(false);
        winDialog.setCancelable(false);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = winDialog.findViewById(R.id.continueBtn);
        TextView pointsDescTv = winDialog.findViewById(R.id.pointsDescTv);
        TextView pointsTv = winDialog.findViewById(R.id.pointsTv);
        TextView exitTv = winDialog.findViewById(R.id.exitTv);

        pointsTv.setText(pointsWon);
        if (Integer.parseInt(pointsWon) == 1) {
            pointsDescTv.setText(context.getString(R.string.point));
        }
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // winDialog.dismiss();
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });
        winDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return winDialog;
    }

    public static Dialog showStreamingWonDialog(final Context context, StreamingWinResponse streamingWinResponse, final OnEventListener onEventListener) {


        final Dialog winDialog = new Dialog(context);

        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        winDialog.setContentView(R.layout.v2_won_dailog);
        winDialog.setCanceledOnTouchOutside(false);
        winDialog.setCancelable(false);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = winDialog.findViewById(R.id.continueBtn);
        TextView pointsDescTv = winDialog.findViewById(R.id.pointsDescTv);
        TextView pointsTv = winDialog.findViewById(R.id.pointsTv);
        TextView exitTv = winDialog.findViewById(R.id.exitTv);

        TextView title = winDialog.findViewById(R.id.title);

        if (streamingWinResponse.getTitle() != null && !TextUtils.isEmpty(streamingWinResponse.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(streamingWinResponse.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title.setText(Html.fromHtml(streamingWinResponse.getTitle()));
            }
        } else {
            title.setVisibility(View.GONE);
        }


        if (streamingWinResponse.getPoints() != null) {
            pointsTv.setText(streamingWinResponse.getPoints().toString());

        } else {
            pointsTv.setVisibility(View.GONE);
            pointsDescTv.setVisibility(View.GONE);
        }

        if (streamingWinResponse.getButtonText() != null) {
            continueBtn.setText(streamingWinResponse.getButtonText());
        } else {
            continueBtn.setVisibility(View.GONE);
        }

//        if (Integer.parseInt(pointsWon) == 1) {
//            pointsDescTv.setText(context.getString(R.string.point));
//        }
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // winDialog.dismiss();
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });
        winDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return winDialog;
    }

    public static Dialog showBannerWonDialog(final Context context, BannerResponse spinWinResponse, final OnEventListener onEventListener) {


        final Dialog winDialog = new Dialog(context);

        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        winDialog.setContentView(R.layout.v2_won_dailog);
        winDialog.setCanceledOnTouchOutside(false);
        winDialog.setCancelable(false);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = winDialog.findViewById(R.id.continueBtn);
        TextView pointsDescTv = winDialog.findViewById(R.id.pointsDescTv);
        TextView pointsTv = winDialog.findViewById(R.id.pointsTv);
        TextView exitTv = winDialog.findViewById(R.id.exitTv);
        TextView title = winDialog.findViewById(R.id.title);

        if (!TextUtils.isEmpty(spinWinResponse.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(spinWinResponse.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title.setText(Html.fromHtml(spinWinResponse.getTitle()));
            }
        } else {
            title.setVisibility(View.GONE);
        }


        if (spinWinResponse.getPoints() != null) {
            pointsTv.setText(spinWinResponse.getPoints().toString());

        } else {
            pointsTv.setVisibility(View.GONE);
            pointsDescTv.setVisibility(View.GONE);
        }

        if (spinWinResponse.getButtomText() != null) {
            continueBtn.setText(spinWinResponse.getButtomText());
        } else {
            continueBtn.setVisibility(View.GONE);
        }

//        if (Integer.parseInt(pointsWon) == 1) {
//            pointsDescTv.setText(context.getString(R.string.point));
//        }
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // winDialog.dismiss();
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });
        winDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return winDialog;
    }

    public static Dialog showGuessWonDialog(final Context context, GuessAndWinResponse spinWinResponse, final OnEventListener onEventListener) {


        final Dialog winDialog = new Dialog(context);

        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        winDialog.setContentView(R.layout.v2_won_dailog);
        winDialog.setCanceledOnTouchOutside(false);
        winDialog.setCancelable(false);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = winDialog.findViewById(R.id.continueBtn);
        TextView pointsDescTv = winDialog.findViewById(R.id.pointsDescTv);
        TextView pointsTv = winDialog.findViewById(R.id.pointsTv);
        TextView exitTv = winDialog.findViewById(R.id.exitTv);
        TextView title = winDialog.findViewById(R.id.title);

        if (spinWinResponse.getTitle() != null && !TextUtils.isEmpty(spinWinResponse.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(spinWinResponse.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title.setText(Html.fromHtml(spinWinResponse.getTitle()));
            }
        } else {
            title.setVisibility(View.GONE);
        }




        if (spinWinResponse.getPoints() != null) {
            pointsTv.setText(spinWinResponse.getPoints().toString());

        } else {
            pointsTv.setVisibility(View.GONE);
            pointsDescTv.setVisibility(View.GONE);
        }

        if (spinWinResponse.getButtonText() != null) {
            continueBtn.setText(spinWinResponse.getButtonText());
        } else {
            continueBtn.setVisibility(View.GONE);
        }

//        if (Integer.parseInt(pointsWon) == 1) {
//            pointsDescTv.setText(context.getString(R.string.point));
//        }
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // winDialog.dismiss();
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });
        winDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return winDialog;
    }


    @SuppressLint("SetTextI18n")
    public static Dialog showSpinWonDialog(final Context context, SpinWinResponse spinWinResponse, final OnEventListener onEventListener) {


        final Dialog winDialog = new Dialog(context);

        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        winDialog.setContentView(R.layout.v2_won_dailog);
        winDialog.setCanceledOnTouchOutside(false);
        winDialog.setCancelable(false);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button continueBtn = winDialog.findViewById(R.id.continueBtn);
        TextView pointsDescTv = winDialog.findViewById(R.id.pointsDescTv);
        TextView pointsTv = winDialog.findViewById(R.id.pointsTv);
        TextView exitTv = winDialog.findViewById(R.id.exitTv);
        TextView title = winDialog.findViewById(R.id.title);

        if (spinWinResponse.getTitle() != null && !TextUtils.isEmpty(spinWinResponse.getTitle())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(spinWinResponse.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title.setText(Html.fromHtml(spinWinResponse.getTitle()));
            }
        } else {
            title.setVisibility(View.GONE);
        }
        if (spinWinResponse.getPoints() != null) {


            pointsTv.setText(spinWinResponse.getPoints().toString());
            pointsDescTv.setText("Points");
        } else {
            pointsTv.setVisibility(View.GONE);
            pointsDescTv.setVisibility(View.GONE);
        }

        if (spinWinResponse.getButtonText() != null) {
            continueBtn.setText(spinWinResponse.getButtonText());
        } else {
            continueBtn.setVisibility(View.GONE);
        }

//        if (Integer.parseInt(pointsWon) == 1) {
//            pointsDescTv.setText(context.getString(R.string.point));
//        }
        SpannableString exitSpan = Utils.makeLinkSpan(context.getString(R.string.exit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // winDialog.dismiss();
                onEventListener.onEventListener(R.id.exitTv);
            }
        });

        exitSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite)), 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan iss = new StyleSpan(Typeface.BOLD);
        exitSpan.setSpan(iss, 0, exitSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exitTv.setText(exitSpan);
        Utils.makeLinksFocusable(exitTv);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventListener.onEventListener(R.id.continueBtn);
            }
        });
        winDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 80;
        int height = display.getHeight() - 200;
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return winDialog;
    }


    public static Dialog showPrizeDetailsDialog(final Context context, final PrizeWin prizeDetails, final String mobileNumber, final OnEventListener onEventListener) {
        final Dialog winDialog = new Dialog(context);
        winDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = winDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transcolorBlack_sixty);
        ;//R.drawable.rectangle_bg);
        winDialog.setContentView(R.layout.prize_details_popup);
        winDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView exitIv = winDialog.findViewById(R.id.exitIv);
        ImageView youWonIv = winDialog.findViewById(R.id.youWonIv);
        ImageView voucherIV = winDialog.findViewById(R.id.voucherIV);
        TextView codeTV = winDialog.findViewById(R.id.codeTV);
        TextView validTillTV = winDialog.findViewById(R.id.validTillTV);
        TextView pinTV = winDialog.findViewById(R.id.pinTV);
        TextView giftTv = winDialog.findViewById(R.id.giftTv);
        TextView poinstOrAmountTv = winDialog.findViewById(R.id.poinstOrAmountTv);
        TextView descTv = winDialog.findViewById(R.id.descTv);
        TextView dateTv = winDialog.findViewById(R.id.dateTv);
        TextView prizeAmountTv = winDialog.findViewById(R.id.prizeAmountTv);
        TextView prizeDeliveryStatus = winDialog.findViewById(R.id.prizeDeliveryStatus);
        TextView prizeDeliveryStatusMessage = winDialog.findViewById(R.id.prizeDeliveryStatusMessage);
        LinearLayout voucherLL = winDialog.findViewById(R.id.voucherLL);
        LinearLayout voucherDetailLL = winDialog.findViewById(R.id.voucherDetailLL);
        Button redeemButton = winDialog.findViewById(R.id.redeemButton);
        LinearLayout paytmLL = winDialog.findViewById(R.id.paytmLL);
        TextView redeemToMobTv = winDialog.findViewById(R.id.redeemToMobTv);

        // if(prizeDetails.getAmount() !=0){


        if (!TextUtils.isEmpty(prizeDetails.getRedemptTo()) && prizeDetails.getPaymentPlatform() == Constants.PAYTM_ID) {
            paytmLL.setVisibility(View.VISIBLE);
            redeemToMobTv.setText(prizeDetails.getRedemptTo());
            voucherLL.setVisibility(View.GONE);
        } else {
            paytmLL.setVisibility(View.GONE);
            voucherLL.setVisibility(View.VISIBLE);


            codeTV.setVisibility(View.GONE);
            pinTV.setVisibility(View.GONE);
            validTillTV.setVisibility(View.GONE);
            if (prizeDetails.getVoucherDetails() != null) {
                if (prizeDetails.getVoucherDetails().getCode() != null) {
                    codeTV.setVisibility(View.VISIBLE);
                    codeTV.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.code_s), prizeDetails.getVoucherDetails().getCode()));
                }
                if (prizeDetails.getVoucherDetails().getPin() != null) {
                    pinTV.setVisibility(View.VISIBLE);
                    pinTV.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.pin), prizeDetails.getVoucherDetails().getPin()));
                }
                if (prizeDetails.getVoucherDetails().getValidity() != null) {
                    validTillTV.setVisibility(View.VISIBLE);
                    validTillTV.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.valid_till), prizeDetails.getVoucherDetails().getValidity()));
                }

                Glide.with(context)
                        .load(Constants.VOUCHER_FILE_URL + prizeDetails.getVoucherDetails().getProductId())
                        .placeholder(R.drawable.voucher_default)
                        .error(R.drawable.voucher_default)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(voucherIV);


            } else {
                voucherLL.setVisibility(View.GONE);
            }
        }
//        if (prizeDetails.getStatus() == 3) {
//            //success
//            youWonIv.setImageResource(R.drawable.rewards_green);
//        } else if (prizeDetails.getStatus() < 0) {
//            //failure
//            youWonIv.setImageResource(R.drawable.rewards_failure);
//        } else if (prizeDetails.getStatus() >= 0 && prizeDetails.getStatus() <= 2) {
//            //Pending
//            youWonIv.setImageResource(R.drawable.rewards_yellow);
//        }

        if (prizeDetails.getStatus() == 3 || prizeDetails.getStatus() == -10 || prizeDetails.getStatus() == 1) {
            //success
            youWonIv.setImageResource(R.drawable.rewards_green);
        } else if (prizeDetails.getStatus() == -1 || prizeDetails.getStatus() == -2) {
            //failure
            youWonIv.setImageResource(R.drawable.rewards_failure);
        } else if (prizeDetails.getStatus() == 0) {
            //Pending
            youWonIv.setImageResource(R.drawable.rewards_yellow);
        }

        if (!TextUtils.isEmpty(prizeDetails.getTitle())) {

            String title = prizeDetails.getTitle();
            int length = title.length();

            if (length <= 18) {
                giftTv.setTextSize(22);
            } else if (length <= 25) {
                giftTv.setTextSize(18);
            } else if (length <= 40) {
                giftTv.setTextSize(14);
            } else if (length < 48) {
                giftTv.setTextSize(12);
            } else {
                giftTv.setTextSize(12);
                title = title.substring(0, 46) + "..";
            }
            if (prizeDetails.getAmount() != 0.0) {
                poinstOrAmountTv.setText(context.getString(R.string.rs) + String.valueOf(prizeDetails.getAmount()) + " /-");
            } else if (prizeDetails.getPoints() != 0) {
                poinstOrAmountTv.setText(String.valueOf(prizeDetails.getPoints()) + " pts");
            }

            giftTv.setText(title);
        }
        if (!TextUtils.isEmpty(prizeDetails.getDesc())) {
            descTv.setVisibility(View.VISIBLE);
            descTv.setText(prizeDetails.getDesc());
        } else {
            descTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(prizeDetails.getWinDate())) {
            String date = prizeDetails.getWinDate();
            try {
                long mills = DateTime.getInMilliesFromUTC(date);

                String formattedDate = DateTime.getFromMilliesInUTC(mills, "MMM-dd, yyyy");
                dateTv.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (prizeDetails.getAmount() > 0) {
            prizeAmountTv.setText(context.getString(R.string.rs) + prizeDetails.getAmount() + " /-");
        } else if (prizeDetails.getPoints() != 0) {
            prizeAmountTv.setText(String.valueOf(prizeDetails.getPoints()) + " pts");
        }


        if (!TextUtils.isEmpty(prizeDetails.getStatusText())) {
            prizeDeliveryStatus.setText(prizeDetails.getStatusText());
            prizeDeliveryStatus.setVisibility(View.VISIBLE);
        } else {
            prizeDeliveryStatus.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(prizeDetails.getStatusMsg())) {
            prizeDeliveryStatusMessage.setText(prizeDetails.getStatusMsg());
            prizeDeliveryStatusMessage.setVisibility(View.VISIBLE);
        } else {
            prizeDeliveryStatusMessage.setVisibility(View.GONE);
        }

        if (prizeDetails.getStatus() == -1) {
            if (prizeDetails.getAmount() > 0) {
                redeemButton.setVisibility(View.VISIBLE);
            } else {
                redeemButton.setVisibility(View.GONE);
            }

        } else {
            redeemButton.setVisibility(View.GONE);
        }

        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog dialog =   showPayTmTransferDialog(context,mobileNumber,prizeDetails.getAmount(),onEventListener);
                onEventListener.onEventListener(R.id.redeemButton, prizeDetails);
            }
        });

        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                winDialog.dismiss();
            }
        });

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        winDialog.show();
        return winDialog;
    }


    public static Dialog showPayTmTransferDialog(final Context context, String mobileNumber, float rupees, final OnEventListener onEventListener, final PrizeWin prizeWinDetails) {
        final Dialog redeemPointsDialog = new Dialog(context);
        redeemPointsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = redeemPointsDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.trasparant);
        ;//R.drawable.rectangle_bg);
        redeemPointsDialog.setContentView(R.layout.paytm_transfer_popup);
        redeemPointsDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final EditText mobileNumET = redeemPointsDialog.findViewById(R.id.mobileNumET);
        TextView redeemedRupeesTv = redeemPointsDialog.findViewById(R.id.redeemedRupeesTv);
        final Button continueBtn = redeemPointsDialog.findViewById(R.id.continueBtn);
        mobileNumET.setText(mobileNumber);

        // rs %s transferring to your paytm account
        redeemedRupeesTv.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.x_rupees_transfer_to_paytm), rupees + ""));

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = mobileNumET.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    Utils.handleCommonErrors(context, context.getString(R.string.enter_mobile));
                } else if (!EightfoldsUtils.getInstance().isValidMobile(phoneNum)) {
                    Utils.handleCommonErrors(context, context.getString(R.string.enter_valid_phone));
                } else {
                    if (prizeWinDetails != null) {
                        prizeWinDetails.setUserName(phoneNum);
                        onEventListener.onEventListener(R.id.continueBtn, prizeWinDetails);
                    } else {
                        onEventListener.onEventListener(R.id.continueBtn, phoneNum);
                    }
                }

            }
        });
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        // window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        redeemPointsDialog.show();
        return redeemPointsDialog;
    }


    public static void showRedeemPointsPopUp(final Context context, final OnEventListener onEventListener, final ArrayList<RedemptionOption> redemptionOptions) {

      /*  AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Title");
        builder.setItems(new CharSequence[]
                        {"button 1", "button 2", "button 3", "button 4"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Toast.makeText(context, "clicked 1", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(context, "clicked 2", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "clicked 3", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(context, "clicked 4", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        builder.create().show();*/


        final Dialog redeemPointsOptionDialog = new Dialog(context);
        redeemPointsOptionDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = redeemPointsOptionDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.yellow_round_corner_bg);
        redeemPointsOptionDialog.setContentView(R.layout.redeem_options_dialog);
        final RecyclerView redemptionOptionsListView = redeemPointsOptionDialog.findViewById(R.id.redemptionOptionsListView);
        RedemptionOptionsAdapter redemptionOptionsAdapter = new RedemptionOptionsAdapter(context, redemptionOptions, new OnEventListener() {
            @Override
            public void onEventListener() {

            }

            @Override
            public void onEventListener(int type) {

            }

            @Override
            public void onEventListener(int type, Object object) {
                if (object instanceof RedemptionOption) {
                    RedemptionOption redemptionOption = (RedemptionOption) object;
                    redeemPointsOptionDialog.dismiss();
                    if (onEventListener != null) {
                        onEventListener.onEventListener(R.id.redemptionOptionsListView, redemptionOption);
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, GridLayoutManager.VERTICAL, false);
        redemptionOptionsListView.setLayoutManager(layoutManager);
        redemptionOptionsListView.setAdapter(redemptionOptionsAdapter);


        // Button paytmBtn = redeemPointsOptionDialog.findViewById(R.id.paytmBtn);
        // Button vouchersBtn = redeemPointsOptionDialog.findViewById(R.id.vouchersBtn);

       /* paytmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEventListener != null) {
                    onEventListener.onEventListener(R.id.paytmBtn);
                }
                redeemPointsOptionDialog.dismiss();
            }
        });

        vouchersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEventListener != null) {
                    onEventListener.onEventListener(R.id.vouchersBtn);
                }
                redeemPointsOptionDialog.dismiss();
            }
        });*/

        redeemPointsOptionDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public static void showVoucherTermsConditionsPopUp(final Context context, String description) {

        final Dialog voucherTermsPopUp = new Dialog(context);
        voucherTermsPopUp.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = voucherTermsPopUp.getWindow();
        window.setBackgroundDrawableResource(R.drawable.yellow_round_corner_bg);
        voucherTermsPopUp.setContentView(R.layout.voucher_info_lay);
        TextView voucherDescription = voucherTermsPopUp.findViewById(R.id.voucherDescription);
        TextView closeBtn = voucherTermsPopUp.findViewById(R.id.closeBtn);

        //            description = URLDecoder.decode(description, "UTF-8");
        //  Log.v("HTML B", description);
        //  description = Html.fromHtml(description).toString();
        // Log.v("HTML A", description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            voucherDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            voucherDescription.setText(Html.fromHtml(description));
        }
        voucherDescription.setMovementMethod(LinkMovementMethod.getInstance());

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voucherTermsPopUp.dismiss();
            }
        });

        voucherTermsPopUp.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public static void showPayTmFailureReasonsPopUp(final Context context) {

        final Dialog paytmFailureReasonPopup = new Dialog(context);
        paytmFailureReasonPopup.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = paytmFailureReasonPopup.getWindow();
        window.setBackgroundDrawableResource(R.drawable.yellow_round_corner_bg);
        paytmFailureReasonPopup.setContentView(R.layout.paytm_info_lay);
        TextView closeBtn = paytmFailureReasonPopup.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmFailureReasonPopup.dismiss();
            }
        });

        paytmFailureReasonPopup.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public static void showInformationPopUp(final Context context, String information, String title) {

        final Dialog featureInformationPopupDialog = new Dialog(context);
        featureInformationPopupDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = featureInformationPopupDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.yellow_round_corner_bg);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        featureInformationPopupDialog.setContentView(R.layout.v2_feature_info);
        ImageView closeBtn = featureInformationPopupDialog.findViewById(R.id.cross);
        TextView about = featureInformationPopupDialog.findViewById(R.id.about);
//        about.setText("About " +title);
        TextView webView = featureInformationPopupDialog.findViewById(R.id.webView);

        if (information != null && !TextUtils.isEmpty(information)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                webView.setText(Html.fromHtml(information.trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                webView.setText(Html.fromHtml(information.trim()));
            }
        }
//        webView.requestFocus();
//        webView.getSettings().setLightTouchEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setSoundEffectsEnabled(true);
//        webView.loadData(information,
//                "text/html", "UTF-8");
//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100) {
//                    progressDialog.show();
//                }
//                if (progress == 100) {
//                    progressDialog.dismiss();
//                }
//            }
//        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featureInformationPopupDialog.dismiss();
            }
        });

        featureInformationPopupDialog.show();
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth() - 100;
        int height = display.getHeight() - 200;
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }


}
