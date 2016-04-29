package com.ruihe.demo.common.utils.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.view.FlowLayout.LayoutParams;

/**
 * FlowLayout布局中tag的布局
 */
public class TagLayout extends TextView {

    public final static int TYPE_CHOOSE = 0;
    public final static int TYPE_EDIT = 1;
    public final static int TYPE_FITNESS_CIRCLE = 2;
    public final static int TYPE_PROFILE = 3;
    public final static int TYPE_ACTION_LIST_FILTER_HEAD = 4;
    public final static int TYPE_ACTION_LIST_FILTER_ITEM = 5;
    public final static int TYPE_ACTION_LIST_TAG = 6;

    private LayoutParams mLayoutParams;

    private int mNormalBgRes;
    private int mCheckedBgRes;
    private int mNormalTextColor;
    private int mCheckedTextColor;

    private boolean mIsChecked;

    public TagLayout(Context context) {
        super(context);
    }

    /**
     * 一定要在add到FlowLayout后再调用
     */
    public void initTag(int type, String text) {
        mLayoutParams = (LayoutParams) getLayoutParams();
        setText(text);

        switch (type) {
            case TYPE_CHOOSE:
            case TYPE_EDIT:
                initPadding(DensityUtil.dip2px(getContext(), 15), 0, DensityUtil.dip2px(getContext(), 15), 0);
                initMargin(0, 0, DensityUtil.dip2px(getContext(), 10), DensityUtil.dip2px(getContext(), 15));
                setBackground(R.drawable.shape_tags_normal, R.drawable.shape_tags_checked);
                setTextColor(Color.parseColor("#ffffff"), Color.parseColor("#4a9fff"));
                setGravity(Gravity.CENTER);
                setTextSize(15);
                mLayoutParams.height = DensityUtil.dip2px(getContext(), 32);
                mLayoutParams.width = LayoutParams.WRAP_CONTENT;
                break;
            default:
                break;
        }
    }

    public void initPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void initMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        mLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
    }

    public void setBackground(int normalRes, int checkedRes) {
        mNormalBgRes = normalRes;
        mCheckedBgRes = checkedRes;
        setBackgroundResource(mNormalBgRes);
    }

    public void setTextColor(int normalColor, int checkedColor) {
        mNormalTextColor = normalColor;
        mCheckedTextColor = checkedColor;
        setTextColor(mNormalTextColor);
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            setTextColor(mCheckedTextColor);
            setBackgroundResource(mCheckedBgRes);
        } else {
            setTextColor(mNormalTextColor);
            setBackgroundResource(mNormalBgRes);
        }
        mIsChecked = isChecked;
    }

}
