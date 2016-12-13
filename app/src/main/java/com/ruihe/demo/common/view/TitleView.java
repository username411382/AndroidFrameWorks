package com.ruihe.demo.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.DensityUtil;

/**
 * 描述：标题栏
 * Created by ruihe on 2016/4/29.
 */
public class TitleView extends FrameLayout {

    private TextView txTitle;
    private LinearLayout layLeft, layRight;
    private TextView textLeft, textRight;

    private static int menuId = Integer.MAX_VALUE / 2;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 组合控件
        LayoutInflater.from(context).inflate(R.layout.view_title, this);
        txTitle = (TextView) findViewById(R.id.tx_title);
        layLeft = (LinearLayout) findViewById(R.id.layout_left);
        layRight = (LinearLayout) findViewById(R.id.layout_right);
        textLeft = (TextView) findViewById(R.id.text_left);
        textRight = (TextView) findViewById(R.id.text_right);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TitleView);
        String title = ta.getString(R.styleable.TitleView_mytitle);
        txTitle.setText(title);
        ta.recycle();
    }

    public void setTitle(int textId) {
        txTitle.setText(textId);
    }

    public void setTitle(String text) {
        txTitle.setText(text);
    }


    //添加标题栏左侧图标
    public View addLeftDrawableMenu(Context context, int drawableId, int width,
                                    int height, OnClickListener onMenuClickListener) {
        View view = createDrawableMenu(context, drawableId, width, height,
                onMenuClickListener);
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int left = DensityUtil.dip(getContext(), 10);
        int right = DensityUtil.dip(getContext(), 0);
        view.setPadding(left, 0, right, 0);
        view.setLayoutParams(LP);
        layLeft.addView(view);
        setIdTagForMenu(view);
        return view;
    }

    //标题栏右侧图标
    public View addRightDrawableMenu(Context context, int drawableId,
                                     int width, int height, OnClickListener onMenuClickListener) {
        View view = createDrawableMenu(context, drawableId, width, height,
                onMenuClickListener);
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int left = DensityUtil.dip(getContext(), 20);
        int right = DensityUtil.dip(getContext(), 10);
        view.setPadding(left, 0, right, 0);
        view.setLayoutParams(LP);
        layRight.addView(view);
        setIdTagForMenu(view);
        return view;
    }


    // 标题左边结束当前activity按钮
    public View addLeftDrawableFinish(final Activity context) {
        int size = DensityUtil.dip(context, 10);
        View view = createDrawableMenu(context, R.drawable.view_title_btn_back, size,
                size, new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        context.finish();
                    }
                });
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int left = DensityUtil.dip(getContext(), 10);
        int right = DensityUtil.dip(getContext(), 20);
        view.setPadding(left, 0, right, 0);
        view.setLayoutParams(LP);
        layLeft.addView(view);
        setIdTagForMenu(view);
        return view;
    }


    //标题栏右侧按钮
    public void addRightTextMenu(Context context, int textId, int padding,
                                 OnClickListener onMenuClickListener) {
        Button button = createTextMenu(context, textId, padding,
                onMenuClickListener);
        layRight.addView(button);
    }

    //标题栏左侧按钮
    public void addLeftTextMenu(Context context, int textId, int padding, OnClickListener onMenuClickListener) {
        Button button = createTextMenu(context, textId, padding, onMenuClickListener);
        layLeft.addView(button);
    }


    //标题栏右侧加文字
    public void addRightText(OnClickListener onMenuClickListener, String text) {
        textRight.setVisibility(View.VISIBLE);
        textRight.setOnClickListener(onMenuClickListener);
        textRight.setText(text);
    }

    //标题栏左侧添加View
    public View addLeftMenu(Context context, View view,
                            OnClickListener onMenuClickListener) {
        view.setOnClickListener(onMenuClickListener);
        layLeft.addView(view);
        setIdTagForMenu(view);
        return view;
    }

    //标题栏右侧添加View
    public View addRightMenu(Context context, View view,
                             OnClickListener onMenuClickListener) {
        view.setOnClickListener(onMenuClickListener);
        layRight.addView(view);
        setIdTagForMenu(view);
        return view;
    }

    public FrameLayout createDrawableMenu(Context context, int drawableId,
                                          int width, int height, OnClickListener onMenuClickListener) {
        FrameLayout layF = (FrameLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.view_title_part_drawable_menu, null);
        ImageView imageView = (ImageView) layF.findViewById(R.id.imageView);
        imageView.setBackgroundResource(drawableId);
        LayoutParams btnLP = new LayoutParams(
                DensityUtil.dip(context, width), DensityUtil.dip(context, height));
        imageView.setLayoutParams(btnLP);
        btnLP.gravity = Gravity.CENTER_VERTICAL;
        layF.setOnClickListener(onMenuClickListener);
        return layF;
    }

    //创建一个按钮
    public Button createTextMenu(Context context, int textId, int padding,
                                 OnClickListener onMenuClickListener) {
        Button button = new Button(context);
        button.setText(textId);
        button.setTextSize(16);
        button.setGravity(Gravity.CENTER);
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        padding = DensityUtil.dip(context, padding);
        button.setPadding(padding, padding, padding, padding);
        button.setLayoutParams(btnLP);

        button.setOnClickListener(onMenuClickListener);
        return button;
    }


    public void removeAllMenu() {
        removeAllLeftMenu();
        removeAllRightMenu();
    }

    public void removeAllLeftMenu() {

        if (textRight.getVisibility() == VISIBLE) {
            textRight.setVisibility(INVISIBLE);
        }
        layLeft.removeAllViews();
    }

    public void removeAllRightMenu() {
        layRight.removeAllViews();
    }

    private void setIdTagForMenu(View view) {
        view.setId(menuId);
        menuId++;
    }
}
