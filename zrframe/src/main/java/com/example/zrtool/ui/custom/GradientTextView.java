package com.example.zrtool.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * 渐变字
 */
public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {
    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed,
                            int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getPaint().setShader(
                    new LinearGradient(0, 0, getWidth(), 0,
                            0xffE65C00, 0xffF9D423,
                    Shader.TileMode.CLAMP));
        }
    }

    /**
     * #颜色转16进制颜色
     * @param str {String} 颜色 转化颜色值有误
     * @return
     */
    private  int StringToColor(String str) {
        return 0xff000000 | Integer.parseInt(str.substring(2), 16);
    }
}

