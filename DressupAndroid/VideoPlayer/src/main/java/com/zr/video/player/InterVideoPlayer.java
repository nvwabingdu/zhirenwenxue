package com.zr.video.player;

import android.graphics.Bitmap;

import com.zr.video.config.ConstantKeys;

/**
 * VideoPlayer抽象接口  播放器基础属性获取和设置属性接口
 */
public interface InterVideoPlayer {

    /**
     * 设置链接
     * @param url                           url
     */
    void setUrl(String url);

    /**
     * 获取播放链接
     * @return                              链接
     */
    String getUrl();

    /**
     * 开始播放
     */
    void start();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 获取视频总时长
     * @return                              long类型
     */
    long getDuration();


    /**
     * 获取当前播放的位置
     * @return                              long类型
     */
    long getCurrentPosition();

    /**
     * 调整播放进度
     * @param pos                           位置
     */
    void seekTo(long pos);

    /**
     * 是否处于播放状态
     * @return                              是否处于播放状态
     */
    boolean isPlaying();

    /**
     * 获取当前缓冲百分比
     * @return                              百分比
     */
    int getBufferedPercentage();


    /**
     * 开始全屏
     */
    void startFullScreen();

    /**
     * 停止全屏
     */
    void stopFullScreen();

    /**
     * 是否全屏
     * @return
     */
    boolean isFullScreen();

    /**
     * 设置静音
     * @param isMute
     */
    void setMute(boolean isMute);

    /**
     * 是否静音
     * @return
     */
    boolean isMute();

    /**
     * 设置屏幕缩放角度
     * @param screenScaleType
     */
    void setScreenScaleType(@ConstantKeys.ScreenScaleType int screenScaleType);

    /**
     * 设置倍数播放
     * @param speed
     */
    void setSpeed(float speed);

    /**
     * 获取倍数
     * @return
     */
    float getSpeed();

    /**
     * 这个是是啥？
     * @return
     */
    long getTcpSpeed();

    /**
     * 重播
     * @param resetPosition
     */
    void replay(boolean resetPosition);

    /**
     * 设置镜像旋转
     * @param enable
     */
    void setMirrorRotation(boolean enable);

    /**
     *
     * @return 屏幕截图 bitmap 是否可用于缩略图？
     */
    Bitmap doScreenShot();

    /**
     * 获取视频大小
     * @return
     */
    int[] getVideoSize();

    /**
     * 设置旋转角度
     * @param rotation
     */
    void setRotation(float rotation);

    /**
     * 设置小屏幕
     */
    void startTinyScreen();

    /**
     * 停止小屏幕
     */
    void stopTinyScreen();

    /**
     * 是否小屏播放
     * @return
     */
    boolean isTinyScreen();

}
