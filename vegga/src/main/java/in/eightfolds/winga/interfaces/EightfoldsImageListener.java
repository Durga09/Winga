package in.eightfolds.winga.interfaces;

/**
 * Created by sp on 14/05/18.
 */

public interface EightfoldsImageListener {
    void onEvent(int requestType);
    void onEvent(int requestType, Object object);
}
