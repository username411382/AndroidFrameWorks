package com.ruihe.demo.common.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.ruihe.demo.MyApplication;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.LogUtil;
import com.ruihe.demo.common.utils.ToastUtil;

/**
 * 滑动View通过Scroller
 * Created by RuiHe on 2017/9/21.
 */

public class MoveViewByScroller extends TextView {

    private static int WIDTH_SCREEN = DensityUtil.getScreenWidth(MyApplication.getInstance());
    private Scroller mScroller;
    private int mLastTouchX;
    private int mLastTouchY;
    private Context mContext;
    private int marginRight;
    private int marginLeft;
    private int marginTop;
    private int marginBottom;

    public MoveViewByScroller(Context context) {
        this(context, null);
    }

    public MoveViewByScroller(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveViewByScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mContext = context;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                mLastTouchX = touchX;
                mLastTouchY = touchY;
                marginRight = WIDTH_SCREEN - getRight();
                marginLeft = getLeft();
                marginTop = getTop();
                marginBottom = DensityUtil.getScreenHeight(mContext) - getBottom() - DensityUtil.getStatusBarHeight((Activity) mContext);
                break;
            case MotionEvent.ACTION_MOVE:
                int offTouchX = touchX - mLastTouchX;
                int offTouchY = touchY - mLastTouchY;
                //getParentView().scrollTo(getParentView().getScrollX() - offTouchX, getParentView().getScrollY() - offTouchY);

                LogUtil.d("marginRight: " + marginRight + "  marginLeft: " + marginLeft);
                if (offTouchX > 0) {
                    if (Math.abs(getParentScrollX() - offTouchX) >= marginRight) {
                        ToastUtil.show("触到右边界");
                        return true;
                    }
                }

                if (offTouchX < 0) {
                    if (getParentScrollX() >= 0 && getParentScrollX() >= marginLeft) {
                        ToastUtil.show("触到左边界");
                        return true;
                    }
                }


                LogUtil.d("marginTop: " + marginTop + "  marginBottom: " + marginBottom);
                if (offTouchY > 0) {
                    if (Math.abs(getParentScrollY() - offTouchY) >= marginBottom) {
                        ToastUtil.show("触到下边界");

                        return true;
                    }
                }

                if (offTouchY < 0) {
                    if (getParentScrollY() >= 0 && getParentScrollY() >= marginTop) {
                        ToastUtil.show("触到上边界");
                        return true;
                    }
                }

                getParentView().scrollBy(-offTouchX, -offTouchY);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private View getParentView() {
        return (View) getParent();
    }

    private int getParentScrollX() {
        return getParentView().getScrollX();
    }

    private int getParentScrollY() {
        return getParentView().getScrollY();
    }

}
