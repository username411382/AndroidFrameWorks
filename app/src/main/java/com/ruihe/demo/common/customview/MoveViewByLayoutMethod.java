package com.ruihe.demo.common.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 可滚动的View(layout)
 * Created by RuiHe on 2017/9/20.
 */

public class MoveViewByLayoutMethod extends TextView {


    private int lastX;
    private int lastY;

    public MoveViewByLayoutMethod(Context context) {
        this(context, null);
    }

    public MoveViewByLayoutMethod(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveViewByLayoutMethod(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = touchX;
                lastY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = touchX - lastX;
                int distanceY = touchY - lastY;
                layout(getLeft() + distanceX, getTop() + distanceY, getRight() + distanceX, getBottom() + distanceY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }


        return true;
    }

}
