package com.example.zrwenxue.others.zrdrawingboard;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * @Author qiwangi
 * @Date 2023/8/22
 * @TIME 09:50
 * 使用

Drawable roundRectDrawable = DrawableUtils.createRoundRectDrawable(0xFF0000FF, 20);
view.setBackground(roundRectDrawable);


 */
public class DrawableUtils {
    // 创建一个圆角矩形Drawable
    public static Drawable createRoundRectDrawable(int color, int cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        return drawable;
    }

    // 创建一个带描边的圆角矩形Drawable
    public static Drawable createRoundRectDrawableWithStroke(int color, int cornerRadius, int strokeWidth, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    // 创建一个圆形Drawable
    public static Drawable createCircleDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);
        return drawable;
    }

}
