package in.eightfolds.commons;

/**
 * Created by Sanjay on 12/14/2015.
 */
public interface EightfoldsImageListener {
    void onEvent(int requestType);

    void onEvent(int requestType, Object object);
}
