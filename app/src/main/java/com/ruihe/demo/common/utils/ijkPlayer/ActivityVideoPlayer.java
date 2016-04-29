package com.ruihe.demo.common.utils.ijkPlayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ruihe.demo.R;
import com.ruihe.demo.activity.BaseActivity;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频播放
 * Created by ruihe on 2016/4/8.
 */
public class ActivityVideoPlayer extends BaseActivity implements View.OnClickListener, GestureDetector.OnDoubleTapListener, VideoView.OnHideVideoLoadingStatus {

    private final int HIDE_INTERVAL = 5000;// 隐藏控制View时间间隔
    public static final String VIDEO_PLAY_URL = "videoPlayUrl";


    private VideoView mVideoView;
    private View mBufferingIndicator;
    private MediaController mMediaController;
    private PlayerGesture mPlayerGesture;
    private GestureDetector mDetector;// 手势
    private RelativeLayout ryRootView;
    private View titleTopView;

    private String mVideoPath;
    private String mTitle;

    /**
     * 跳转到视频播放界面
     *
     * @param videoLink 视频链接
     */
    public static void redirectToActivity(Context context, String videoLink) {
        Intent intent = new Intent(context, ActivityVideoPlayer.class);
        intent.putExtra(VIDEO_PLAY_URL, videoLink);
        context.startActivity(intent);
    }

    @Override
    public int getViewId() {
        return R.layout.activity_player;
    }

    @Override
    public void onActivityViewCreated() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        initVariable();
        initView();
        initListener();
        bindData();
    }


    private void initVariable() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoPath = getIntent().getStringExtra(VIDEO_PLAY_URL);
        mMediaController = new MediaController(this);
    }

    private void initView() {
        mBufferingIndicator = findViewById(R.id.buffering_indicator);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        titleTopView = findViewById(R.id.common_title_view);
        ryRootView = (RelativeLayout) findViewById(R.id.player_root);
        mPlayerGesture = new PlayerGesture(this, mVideoView, ryRootView);
        mDetector = new GestureDetector(this, mPlayerGesture);
    }

    private void initListener() {
        mDetector.setOnDoubleTapListener(this);
    }

    private void bindData() {
        mTitleView.setTitle("测试视频");
        mMediaController.setTitleTopView(titleTopView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator, this);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.requestFocus();
        mVideoView.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            default:
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int action = event.getAction();
        if (pointerCount != 2) {
            if (action == MotionEvent.ACTION_DOWN) {
                mPlayerGesture.onFingerDown();
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mPlayerGesture.onFingerUp();
            }
            return mDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {  //横屏
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { //竖屏
        }
        decorView.setSystemUiVisibility(uiOptions);

        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE); // 每次切换屏幕方向完成，需要重新计算VideoView宽高，故重新设置VideoLayout
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        if (titleTopView.getVisibility() == View.VISIBLE) {
            mMediaController.hide();
        } else {
            mMediaController.show(HIDE_INTERVAL);
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void hideVideoLoadingStatus() {
        mBufferingIndicator.setVisibility(View.GONE);
    }

}
