package com.zr.video.surface;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.zr.kernel.inter.AbstractVideoPlayer;

/**
 * 自定义渲染view接口
 */
public interface InterSurfaceView {

    /**
     * 关联AbstractPlayer
     * @param player                        player
     */
    void attachToPlayer(@NonNull AbstractVideoPlayer player);

    /**
     * 设置视频宽高
     * @param videoWidth                    宽
     * @param videoHeight                   高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频旋转角度
     * @param degree                        角度值
     */
    void setVideoRotation(int degree);

    /**
     * 设置screen scale type
     * @param scaleType                     类型
     */
    void setScaleType(int scaleType);

    /**
     * 获取真实的RenderView
     * @return                              view
     */
    View getView();

    /**
     * 截图
     * @return                              bitmap
     */
    Bitmap doScreenShot();

    /**
     * 释放资源
     */
    void release();

}