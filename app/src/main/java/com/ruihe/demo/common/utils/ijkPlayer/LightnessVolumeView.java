package com.ruihe.demo.common.utils.ijkPlayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DensityUtil;


/**
 * 亮度，声音调节
 */
public class LightnessVolumeView extends LinearLayout {

    public final static int ADD_FLAG = 1;
    public final static int SUB_FLAG = -1;

    private Handler handler;
    private Activity activity;
    private AudioManager mAudioManager;

    private ImageView icon;
    private TextView text;
    private int maxVolume;
    private int showLightness = 0;// 0-255
    private int showVolume;

    public LightnessVolumeView(Context context) {
        super(context);
        initView(context);
    }

    public LightnessVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LightnessVolumeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        LayoutParams iconParams = new LayoutParams(
                DensityUtil.dip2px(context, 20), DensityUtil.dip2px(context, 20));
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        iconParams.gravity = Gravity.CENTER_HORIZONTAL;
        textParams.gravity = Gravity.CENTER_HORIZONTAL;
        icon = new ImageView(context);
        icon.setImageResource(R.drawable.video_lightness_icon);
        icon.setLayoutParams(iconParams);
        text = new TextView(context);
        text.setLayoutParams(textParams);
        text.setMaxLines(1);
        text.setTextSize(15);
        text.setTextColor(Color.parseColor("#ffffff"));
        text.setText("10%");
        addView(icon);
        addView(text);
    }

    /**
     * 设置activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
        showLightness = getScreenBrightness();
        mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//max = 15
        initVolume();
        this.handler = new Handler();
        this.setVisibility(View.GONE);
    }

    /**
     * 设置音量初始值
     */
    public void initVolume() {
        showVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 100 / maxVolume;
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(
                    getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 仅修改当前应用亮度
     */
    public void changeLightness(int flag) {
        if (activity != null) {
            showLightness = showLightness + flag;
            if (showLightness > 255) {
                showLightness = 255;
            } else if (showLightness < 15) {
                showLightness = 15;
            }
            icon.setImageResource(R.drawable.video_lightness_icon);
            text.setText(showLightness * 100 / 255 + "%");
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.screenBrightness = showLightness / 255f;
            window.setAttributes(lp);
            handler.removeCallbacks(hiddenCenterSetViewThread);
            this.setVisibility(View.VISIBLE);
            handler.postDelayed(hiddenCenterSetViewThread, 1000);
        }
    }

    /**
     * 设置音量，系统的也修改
     */
    public void changeVolume(int flag) {
        showVolume = showVolume + flag;
        if (showVolume > 100) {
            showVolume = 100;
        } else if (showVolume < 0) {
            showVolume = 0;
        }
        icon.setImageResource(R.drawable.ic_video_volume);
        text.setText(showVolume + "%");
        int tagVolume = showVolume * maxVolume / 100;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tagVolume, 0); //tagVolume:音量绝对值
        handler.removeCallbacks(hiddenCenterSetViewThread);
        this.setVisibility(View.VISIBLE);
        handler.postDelayed(hiddenCenterSetViewThread, 1000);
    }

    public void setIconSize(int width, int height) {
        LayoutParams lp = (LayoutParams) icon.getLayoutParams();
        lp.width = width;
        lp.height = height;
        icon.setLayoutParams(lp);
    }

    public void setTextSize(float size) {
        text.setTextSize(size);
    }

    /**
     * color #00000000
     */
    public void setTextColor(String color) {
        text.setTextColor(Color.parseColor(color));
    }

    /**
     * 隐藏音量和亮度提示View
     */
    private Runnable hiddenCenterSetViewThread = new Runnable() {

        @Override
        public void run() {
            LightnessVolumeView.this.setVisibility(View.GONE);
        }
    };
}
