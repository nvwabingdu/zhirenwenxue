package com.example.zrtool.ui.uibase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 16:33
 */
public class BaseRelativeLayout extends RelativeLayout {

    public BaseRelativeLayout(Context context) {
        super(context);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 在这里放置 BaseRelativeLayout 的初始化代码
    }



}
