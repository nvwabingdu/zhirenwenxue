package com.example.zrtool.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-12-25
 * Time: 9:46
 * 忘了用于什么东西了
 */
public class MyDrawerLayout extends DrawerLayout {
    public MyDrawerLayout(Context context) {
        super(context);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     这段代码是用于设置控件的宽度和高度的测量规格。
     首先，MeasureSpec.getSize(widthMeasureSpec)得到的是控件宽度的大小，将它作为参数传递给MeasureSpec.makeMeasureSpec()方法，再加上MeasureSpec.EXACTLY作为测量规格，就完成了对控件宽度的测量规格设置。
     同样地，MeasureSpec.getSize(heightMeasureSpec)得到的是控件高度的大小，将它作为参数传递给MeasureSpec.makeMeasureSpec()方法，再加上MeasureSpec.EXACTLY作为测量规格，就完成了对控件高度的测量规格设置。
     MeasureSpec.EXACTLY表示精确测量模式，即指定控件的大小为参数中指定的大小。
     通过以上操作，可以确保控件在布局中尺寸的精确匹配。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec),
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
