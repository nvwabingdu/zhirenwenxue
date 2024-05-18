package com.zr.video.old.listener;


import com.zr.video.config.ConstantKeys;

/**
 * 视频播放模式监听
 */
@Deprecated
public interface OnPlayerTypeListener {

    /**
     * 视频播放模式监听
     * int FULL_SCREEN = 101; 切换到全屏播放监听
     * int TINY_WINDOW = 102; 切换到小窗口播放监听
     * int NORMAL = 103; 切换到正常播放监听
     * @param type                              类型
     */
    void onPlayerPattern(@ConstantKeys.PlayMode int type);

}
