package in.eightfolds.winga.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.List;

import in.eightfolds.WingaApplication;

public class Foreground implements Application.ActivityLifecycleCallbacks  {

    private static final String TAG = Foreground.class.getSimpleName();
    public static final long CHECK_DELAY = 500;

    private static Foreground instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private Runnable check;



    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();


    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    public interface Listener {
        public void onBecameForeground();
        public void onBecameBackground();
    }


    public static void init(Application app){
        if (instance == null){
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get(){
        return instance;
    }

    private Foreground(){}
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground){
            Logg.i(TAG, "went foreground");
            WingaApplication.getInstance().mHandler.postDelayed(WingaApplication.getInstance().mUpdateTimeTask, 0);
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Logg.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            Logg.i(TAG, "still foreground");
        }


    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    WingaApplication.getInstance().mHandler.removeCallbacks(WingaApplication.getInstance().mUpdateTimeTask);
                    Logg.i(TAG, "went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Logg.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Logg.i(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
