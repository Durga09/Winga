package in.eightfolds.winga.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import in.eightfolds.utils.DateTime;

public class Logg {

    //public static boolean enabled = false;

    public static void d(String tag, String msg) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.d(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.w(tag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.v(tag, msg, tr);
        }
    }

    public static void append(String tag, String msg, Throwable tr) {
        appendLog(tag,msg);
        if (!Constants.DontShowLogs) {
            Log.v(tag, msg, tr);
        }
    }

    public static void appendLog(String TAG, String text)
    {
        if(Constants.WRITELOGS_TO_TEXTFILE) {
//            File logFile = new File("sdcard/wingalog.txt");
//            if (!logFile.exists()) {
//                try {
//                    logFile.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                //BufferedWriter for performance, true to set append to file flag
//                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//                buf.append(DateTime.getNow() + ":  " + TAG + ":   " + text);
//                buf.newLine();
//                buf.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }

}
