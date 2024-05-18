package cn.dreamfruits.yaoguo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import cn.dreamfruits.yaoguo.R;

/**
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class SideBarView extends View {

    private List<String> sideTextList;

    private Paint paint;

    private int textHeight;

    private int width, height;

    private float touchX, touchY;

    private int currentIndex;

    private int textSize;

    private int mTouchSlop;

    private float downX, downY;

    private boolean scrolling = false;

    private int defaultColor = 0xff999999, selectColor = 0xff585858;

    private int popTextColor = 0xffffffff;

    private OnSideItemSelectListener onSideItemSelectListener;

    public SideBarView(Context context) {
        super(context);
        init();
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width < textSize * 10) {
            width = textSize * 10;
        }
        setMeasuredDimension(width, height);
        if (sideTextList != null && sideTextList.size() > 0) {
//            textHeight = height / sideTextList.size();
            textHeight = dp2px(getContext(), 14);
        }
        textSize = dp2px(getContext(), 10);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < getWidth() - textSize * 3) {
            if (touchX != 0 || touchY != 0) {
                touchY = touchX = 0;
                invalidate();
            }
            return super.onTouchEvent(event);
        }
        touchX = event.getX();
        touchY = event.getY();
        int currentIndex = ((int) touchY) / textHeight;
        if (this.currentIndex != currentIndex) {
            this.currentIndex = currentIndex;
            if (onSideItemSelectListener != null) {
                int index = this.currentIndex - 1;
                if (index >= 0 && currentIndex >= 0 && currentIndex < sideTextList.size() - 1) {
                    onSideItemSelectListener.onSelectItem(index, sideTextList.get(currentIndex));
                }
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                scrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchY - downY) > mTouchSlop) {
                    scrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchX = touchY = 0;
                scrolling = false;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawPop(canvas);
    }

    private void drawText(Canvas canvas) {
        if (sideTextList != null && sideTextList.size() > 0) {
            paint.setTextAlign(Paint.Align.CENTER);
            for (int i = 0; i < sideTextList.size(); i++) {
                if (sideTextList.get(i).equals("互关好友") || sideTextList.get(i).equals("最近联系")) {
                    paint.setTextSize(textSize);
                    int drawX = width - textSize;
                    int drawY = textHeight * (i + 1) - (textHeight / 2);
                    paint.setColor(Color.TRANSPARENT);
                    canvas.drawText("互", drawX, drawY, paint);
                } else {
                    paint.setTextSize(textSize);
                    int drawX = width - textSize;
                    int drawY = textHeight * (i + 1) - (textHeight / 2);
                    if (!scrolling && currentIndex == i) {
                        paint.setColor(selectColor);
                    } else {
                        paint.setColor(defaultColor);
                    }
                    canvas.drawText(sideTextList.get(i), drawX, drawY, paint);
                }

            }
        }
    }

    private void drawPop(Canvas canvas) {
        if (touchX > 0 && touchY > 0 && currentIndex < sideTextList.size()) {
            String text = sideTextList.get(currentIndex);
            if (TextUtils.isEmpty(text)) {
                return;
            }

            Bitmap bitmap = residToBitmap(R.drawable.icon_side_bar_sel);

            int px = width - textSize * 3 - bitmap.getWidth();
            int py = (int) touchY - bitmap.getHeight() / 2;
            if (text.equals("互关好友") || text.equals("最近联系")) {

            } else
                canvas.drawBitmap(bitmap, px, py, paint);

            int pxText = width - textSize * 3 - bitmap.getWidth() / 2 - textSize / 2;
            int pyText = (int) touchY + textSize * 2 / 3;

            paint.setColor(popTextColor);
            paint.setTextSize(textSize * 2);
            paint.setTextAlign(Paint.Align.CENTER);
            if (text.equals("互关好友") || text.equals("最近联系")) {

            } else
                canvas.drawText(text, pxText, pyText, paint);
        }
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置item字体默认颜色
     *
     * @param defaultColor
     */
    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
        refreshView();
    }

    /**
     * 设置被选中的时候的颜色
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
        refreshView();
    }

    /**
     * 设置pop字体颜色
     *
     * @param popTextColor
     */
    public void setPopTextColor(int popTextColor) {
        this.popTextColor = popTextColor;
        refreshView();
    }

    /**
     * 设置当前位置
     *
     * @param currentIndex
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex + 1;
        refreshView();
    }

    /**
     * 设置side item字符
     *
     * @param sideList
     */
    public void setSideText(List sideList) {
        sideTextList = new ArrayList<>();
        sideTextList.add("");
        sideTextList.addAll(sideList);
        sideTextList.add("");
        refreshView();
    }

    public void setOnSideItemSelectListener(OnSideItemSelectListener onSideItemSelectListener) {
        this.onSideItemSelectListener = onSideItemSelectListener;
    }

    private void refreshView() {
        if (isAttachedToWindow()) {
            requestLayout();
            invalidate();
        }
    }

    public interface OnSideItemSelectListener {
        /**
         * 选择side item的时候回调
         *
         * @param position
         * @param title
         */
        public void onSelectItem(int position, String title);
    }

    public Bitmap residToBitmap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId, null);
        return bitmap;
    }
}