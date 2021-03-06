package org.xiaoxingqi.gmdoc.wegidt;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class SqureRelativeLayout extends RelativeLayout {
    public SqureRelativeLayout(Context context) {
        super(context);
    }

    public SqureRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqureRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
