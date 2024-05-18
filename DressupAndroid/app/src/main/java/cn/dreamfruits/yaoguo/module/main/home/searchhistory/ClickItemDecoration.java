package cn.dreamfruits.yaoguo.module.main.home.searchhistory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import cn.dreamfruits.yaoguo.R;

/**
 * @Author qiwangi
 * @Date 2023/7/9
 * @TIME 13:01
 */
public class ClickItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable clickBackground;

    public ClickItemDecoration(Context context) {
        clickBackground = ContextCompat.getDrawable(context, R.drawable.recyclerview_click_background); // 点击效果的背景
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.isPressed()) { // 如果项被按下
                clickBackground.setBounds(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                clickBackground.draw(canvas);
            }
        }
    }
}

