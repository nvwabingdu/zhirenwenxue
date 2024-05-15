package com.example.zrtool.ui.uibase;

import static com.blankj.utilcode.util.BarUtils.getNavBarHeight;
import static com.blankj.utilcode.util.BarUtils.getStatusBarHeight;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.BarUtils;
import com.example.zrtool.utils.LogUtils;
import com.example.zrtool.utils.ScreenUtils;

/**
 * @Author qiwangi
 * @Date 2023/8/16
 * @TIME 02:39
 */
public class BaseConstraintLayout extends ConstraintLayout {

    public BaseConstraintLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setPadding(
                getPaddingLeft(),
                getPaddingTop() + getStatusBarHeight(),
                getPaddingRight(),
                getPaddingBottom() + 30);
    }

    /**
     * 设置留出状态栏高度的padding 和底部导航栏的padding
     */
    public void setPaddingTopWithStatusBarHeight(int statusBarHeight,int navBarHeight) {
        setPadding(
                getPaddingLeft(),
                getPaddingTop() + statusBarHeight,
                getPaddingRight(),
                getPaddingBottom() + navBarHeight);
    }
}
