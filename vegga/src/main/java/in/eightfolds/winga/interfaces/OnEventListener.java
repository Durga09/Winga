package in.eightfolds.winga.interfaces;

/**
 * Created by Swapnika on 04-May-18.
 */

public interface OnEventListener {
    public void onEventListener();
    public void onEventListener(int type);

    public void onEventListener(int type, Object object);
}
