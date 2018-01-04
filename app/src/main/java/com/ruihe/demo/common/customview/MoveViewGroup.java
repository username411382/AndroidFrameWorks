package com.ruihe.demo.common.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by RuiHe on 2017/9/21.
 */

public class MoveViewGroup extends RelativeLayout {
    public MoveViewGroup(Context context) {
        super(context);
    }

    public MoveViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }


}
