package in.eightfolds.winga.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {
	private static final String TAG = CustomImageView.class.getSimpleName();

	public CustomImageView(Context context) {
		super(context);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		if (drawable != null) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = width * drawable.getIntrinsicHeight()
					/ drawable.getIntrinsicWidth();
			Logg.v(TAG, "Width >> "+ width + "  Heidht>> "+height) ;
			setMeasuredDimension(width, height);
		} else {
			setMeasuredDimension(0, 0);
		}
	}

	Object key;

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

}
