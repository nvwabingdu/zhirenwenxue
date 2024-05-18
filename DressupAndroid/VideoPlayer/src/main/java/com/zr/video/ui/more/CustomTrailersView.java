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
 * 试看
 */
public class CustomTrailersView extends FrameLayout implements InterControlView, View.OnClickListener {

    public CustomTrailersView(@NonNull Context context) {
        super(context);
    }

    public CustomTrailersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTrailersView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
