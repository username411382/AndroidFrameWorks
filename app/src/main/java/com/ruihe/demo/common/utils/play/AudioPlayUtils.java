package com.ruihe.demo.common.utils.play;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.ruihe.demo.common.utils.play.AudioPlayEntity.AudioPlaySourceType;

import java.io.File;
import java.io.IOException;

/**
 * 音频播放工具类
 * Created by RH on 2018/5/14
 */
public class AudioPlayUtils {

    private volatile static AudioPlayUtils mInstance = null;
    private long mClickPlayDate;

    private MediaPlayer mMediaPlayer;
    private boolean isPlaying;
    private OnAudioPlayListener mAudioListener;
    private AudioPlayEntity mLastAudioEntity;

    /**
     * 音频播放监听接口
     */
    public interface OnAudioPlayListener {
        /**
         * 播放完成
         */
        void audioPlayComplete(AudioPlayEntity audioPlayEntity);

        /**
         * 播放开始
         */
        void audioPlayStart(AudioPlayEntity audioPlayEntity);

        /**
         * 播放出错
         */
        void audioPlayError(AudioPlayEntity audioPlayEntity);

        /**
         * 播放重置
         */
        void audioPlayReset(AudioPlayEntity audioPlayEntity);
    }

    public static synchronized AudioPlayUtils getInstance() {
        if (mInstance == null) {
            synchronized (AudioPlayUtils.class) {
                if (mInstance == null) {
                    mInstance = new AudioPlayUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 音频播放工作线程
     */
    private class PlayAudio implements Runnable {
        private AudioPlayEntity audioPlayEntity;

        PlayAudio(AudioPlayEntity audioPlayEntity) {
            this.audioPlayEntity = audioPlayEntity;
        }

        @Override
        public void run() {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    mMediaPlayer.reset();
                    if (mAudioListener != null) {
                        mAudioListener.audioPlayComplete(audioPlayEntity);
                    }
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    mMediaPlayer.release();
                    isPlaying = false;
                    if (mAudioListener != null) {
                        mAudioListener.audioPlayError(audioPlayEntity);
                    }
                    return false;
                }
            });

            try {

                if (audioPlayEntity.getAudioUseType() == AudioPlaySourceType.AUDIO_PLAY_LOCAL_RESOURCE) {//本地res音频
                    AssetFileDescriptor assetFileDescriptor = audioPlayEntity.getAudioResPath();
                    mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset()
                            , assetFileDescriptor.getLength());
                    isPlaying = true;
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    if (mAudioListener != null) {
                        mAudioListener.audioPlayStart(audioPlayEntity);
                    }
                    return;
                }

                //文件音频或者线上音频
                if (!TextUtils.isEmpty(audioPlayEntity.getAudioPlayPath())) {
                    mMediaPlayer.setDataSource(audioPlayEntity.getAudioPlayPath());
                    isPlaying = true;
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();

                    if (mAudioListener != null) {
                        mAudioListener.audioPlayStart(audioPlayEntity);
                    }
                }


            } catch (IOException e) {
                mMediaPlayer.release();
                mMediaPlayer = null;
                isPlaying = false;
                if (mAudioListener != null) {
                    mAudioListener.audioPlayError(audioPlayEntity);
                }
            } catch (IllegalStateException e) {
                mMediaPlayer.release();
                mMediaPlayer = null;
                isPlaying = false;
            }
        }
    }

    /**
     * 开始播放
     *
     * @param audioPlayEntity 音频播放实体
     */
    public void startPlay(AudioPlayEntity audioPlayEntity) {
        if (System.currentTimeMillis() - mClickPlayDate < 1000) {
            mClickPlayDate = System.currentTimeMillis();
            return;
        }
        mClickPlayDate = System.currentTimeMillis();

        if (audioPlayEntity.getAudioUseType() == AudioPlaySourceType.AUDIO_PLAY_LOCAL_RESOURCE) {//本地res文件
            if (audioPlayEntity.getAudioResPath() == null) {
                mAudioListener.audioPlayError(audioPlayEntity);
                return;
            }
        } else {
            if (TextUtils.isEmpty(audioPlayEntity.getAudioPlayPath())) {//播放地址为空
                mAudioListener.audioPlayError(audioPlayEntity);
                return;
            }

            if (audioPlayEntity.getAudioUseType() == AudioPlaySourceType.AUDIO_TYPE_LOCAL_FILE
                    && !new File(audioPlayEntity.getAudioPlayPath()).exists()) {//本地播放路径不存在
                mAudioListener.audioPlayError(audioPlayEntity);
                return;
            }
        }

        if (isPlaying && mMediaPlayer != null) {//正在播放重置
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mAudioListener.audioPlayReset(mLastAudioEntity);
        }
        new Thread(new PlayAudio(audioPlayEntity)).start();
        mLastAudioEntity = audioPlayEntity;

    }

    /**
     * 停止播放释放资源
     */
    public void stopPlay() {
        if (isPlaying && mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mLastAudioEntity = null;
        }
    }

    /**
     * 重置资源
     */
    public void resetPlay() {
        if (isPlaying && mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    /**
     * 播放监听
     */
    public void setAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        if (mAudioListener != null) {
            mAudioListener = null;
        }
        mAudioListener = onAudioPlayListener;
    }

    /**
     * 获取上次播放的音频实体
     */
    public AudioPlayEntity getLastAudioEntity() {
        return mLastAudioEntity;
    }

}
