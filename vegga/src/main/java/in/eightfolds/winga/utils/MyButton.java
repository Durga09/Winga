package in.eightfolds.winga.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class MyButton  extends androidx.appcompat.widget.AppCompatButton {

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context) {
        super(context);
        init();
    }

    public void init() {

        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"
                + Constants.LATO_BOLD));

    }

}