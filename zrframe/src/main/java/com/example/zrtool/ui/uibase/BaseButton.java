package com.example.zrtool.ui.uibase;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 16:29
 */
public class BaseButton extends AppCompatButton {

    public BaseButton(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 在这里放置 BaseButton 的初始化代码
    }

}
