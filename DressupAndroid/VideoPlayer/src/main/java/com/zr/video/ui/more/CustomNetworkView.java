package com.zr.video.ui.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zr.video.bridge.ControlWrapper;
import com.zr.video.ui.view.InterControlView;

/**
 * 网络变化提示对话框。当网络由wifi变为移动流量的时候会显示
 */
public class CustomNetworkView extends FrameLayout implements InterControlView, View.OnClickListener {

    public CustomNetworkView(@NonNull Context context) {
        super(context);
    }

    public CustomNetworkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNetworkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {

    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {

    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }

    @Override
    public void onClick(View v) {

    }

}
