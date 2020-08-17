package com.ruihe.demo.fragment.random;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.CaculateUtils;
import com.ruihe.demo.common.utils.LogUtil;
import com.ruihe.demo.fragment.BaseFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 随机事件硬币页面
 * Created by RH on 2018/9/13
 */
public class FragmentCoin extends BaseFragment {

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;

    private MediaPlayer mMediaPlayer;
    private String mVideoPath = "coininit.mp4";

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        setUnBinder(ButterKnife.bind(this, view));

        initView();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_coin;
    }

    protected void initView() {
        mMediaPlayer = new MediaPlayer();
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceViewLis());
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Override
    public void onDetached() {

    }

    /**
     * 点击硬币
     */
    @OnClick(R.id.tv_click_coin)
    void onClickCoin(View view) {
        if (mMediaPlayer.isPlaying()) {
            return;
        }
        long random = System.currentTimeMillis();
        LogUtil.d("ruihe", "random:" + random);
        mVideoPath = random % 2 == 0 ? "coinvideo1.mp4" : "coinvideo2.mp4";
        mMediaPlayer.reset();
        try {
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtil.d("ruihe", "surfaceChanged");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.d("ruihe", "surfaceCreated");
            try {
                play();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtil.d("ruihe", "surfaceDestroyed");
        }
    }

    private void play() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor fd = holder.getAssets().openFd(mVideoPath);
        mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        //mMediaPlayer.setLooping(true);
        mMediaPlayer.setDisplay(surfaceView.getHolder());
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    private void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
