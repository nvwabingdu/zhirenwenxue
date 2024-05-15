package com.example.zrtool.ui.uibase;

import static com.blankj.utilcode.util.BarUtils.getStatusBarHeight;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.zrtool.utils.LogUtils;

/**
 * @Author qiwangi
 * @Date 2023/8/16
 * @TIME 02:39
 */
public class BaseConstraintLayoutSetBottom extends ConstraintLayout {

    public BaseConstraintLayoutSetBottom(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseConstraintLayoutSetBottom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseConstraintLayoutSetBottom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseConstraintLayoutSetBottom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setPadding(
                getPaddingLeft(),
                getPaddingTop(),
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
