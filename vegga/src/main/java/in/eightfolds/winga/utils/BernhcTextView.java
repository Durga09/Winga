package in.eightfolds.winga.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Swapnika on 18-Jun-18.
 */
public class BernhcTextView extends TextView {

    public BernhcTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BernhcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BernhcTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        // Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
        // "font/" + Constants.OPTIMA_REGULAR_FONT);
        // setTypeface(tf, 1);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"
                + Constants.BERNHC));

    }

}