package com.zr.video.ui.pip;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zr.video.controller.GestureVideoController;
import com.zr.video.ui.view.CustomCompleteView;
import com.zr.video.ui.view.CustomErrorView;

/**
 * 悬浮播放控制器
 */
public class CustomFloatController extends GestureVideoController {

    public CustomFloatController(@NonNull Context context) {
        super(context);
    }

    public CustomFloatController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        addControlComponent(new CustomCompleteView(getContext()));
        addControlComponent(new CustomErrorView(getContext()));
        addControlComponent(new CustomFloatView(getContext()));
    }

    @Override
    public void destroy() {

    }
}
