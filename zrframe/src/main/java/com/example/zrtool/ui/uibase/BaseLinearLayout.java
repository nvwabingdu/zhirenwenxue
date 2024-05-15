package com.example.zrtool.ui.uibase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.zrframe.R;

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 16:19
 */
public class BaseLinearLayout extends LinearLayout{
    public BaseLinearLayout(Context context) {
        super(context);
        init();
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 在这里放置 BaseLinearLayout 的初始化代码
//        setBackgroundColor(getResources().getColor(R.color.white));
    }
}
