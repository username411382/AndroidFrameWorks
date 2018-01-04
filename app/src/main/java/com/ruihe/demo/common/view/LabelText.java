package com.ruihe.demo.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 标签text
 * Created by RuiHe on 2017/9/25.
 */

public class LabelText extends TextView {
    public LabelText(Context context) {
        this(context, null);
    }

    public LabelText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }


}
