package com.ruihe.demo.common.utils.play;

import android.content.res.AssetFileDescriptor;

/**
 * 音频播放实体
 * Created by RH on 2018/9/18
 */
public class AudioPlayEntity {
    /**
     * 音频播放的使用类型
     */
    public enum AudioPlaySourceType {
        AUDIO_PLAY_LOCAL_RESOURCE,//播放resource资源文件
        AUDIO_TYPE_LOCAL_FILE,//播放本地文件
        AUDIO_TYPE_SERVER_URL//播放线上url
    }

    private String audioPlayPath;//播放地址 file或者url

    private AssetFileDescriptor audioResPath;//播放本地res路径

    private int audioClickPosition;//列表中点击播放音频的位置

    private AudioPlaySourceType audioUseType;//音频使用类型


    public String getAudioPlayPath() {
        return audioPlayPath;
    }

    public void setAudioPlayPath(String audioPlayPath) {
        this.audioPlayPath = audioPlayPath;
    }

    public int getAudioClickPosition() {
        return audioClickPosition;
    }

    public void setAudioClickPosition(int audioClickPosition) {
        this.audioClickPosition = audioClickPosition;
    }

    public AudioPlaySourceType getAudioUseType() {
        return audioUseType;
    }

    public void setAudioUseType(AudioPlaySourceType audioUseType) {
        this.audioUseType = audioUseType;
    }

    public AssetFileDescriptor getAudioResPath() {
        return audioResPath;
    }

    public void setAudioResPath(AssetFileDescriptor audioResPath) {
        this.audioResPath = audioResPath;
    }

    /**
     * 创建音频播放实体
     *
     * @param audioPlaySourceType 播放类型
     * @param audioPlayPath       音频播放路径（url或本地路径）
     * @param positionInList      在列表中的位置
     */
    public static AudioPlayEntity createAudioEnity(AudioPlaySourceType audioPlaySourceType, String audioPlayPath, int positionInList) {
        AudioPlayEntity audioPlayEntity = new AudioPlayEntity();
        audioPlayEntity.setAudioUseType(audioPlaySourceType);
        audioPlayEntity.setAudioPlayPath(audioPlayPath);
        audioPlayEntity.setAudioClickPosition(positionInList);
        return audioPlayEntity;
    }

    /**
     * 创建本地res音频
     *
     * @param assetFileDescriptor 本地Res音频路径
     */
    public static AudioPlayEntity createLocalResAudio(AssetFileDescriptor assetFileDescriptor) {
        AudioPlayEntity audioPlayEntity = new AudioPlayEntity();
        audioPlayEntity.setAudioUseType(AudioPlaySourceType.AUDIO_PLAY_LOCAL_RESOURCE);
        audioPlayEntity.setAudioResPath(assetFileDescriptor);
        return audioPlayEntity;
    }
}
