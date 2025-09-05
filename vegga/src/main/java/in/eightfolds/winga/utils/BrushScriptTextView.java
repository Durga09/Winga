package in.eightfolds.winga.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BrushScriptTextView  extends TextView {

    public BrushScriptTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BrushScriptTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrushScriptTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        // Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
        // "font/" + Constants.OPTIMA_REGULAR_FONT);
        // setTypeface(tf, 1);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"
                + Constants.BRUSH_SCRIPT_STANDARD));

    }

}