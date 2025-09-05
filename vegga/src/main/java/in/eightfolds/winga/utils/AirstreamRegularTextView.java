package in.eightfolds.winga.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sp on 07/05/18.
 */

public class AirstreamRegularTextView extends TextView {

    public AirstreamRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AirstreamRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AirstreamRegularTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        // Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
        // "font/" + Constants.OPTIMA_REGULAR_FONT);
        // setTypeface(tf, 1);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"
                + Constants.AIRSTREAM_REGULAR));

    }

}