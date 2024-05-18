package cn.dreamfruits.yaoguo.module.main.home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import cn.dreamfruits.yaoguo.R;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/**
 * @Author wq
 * @Date 2023/3/1-10:57
 */
public final class DotIndicatorView extends LinearLayout {
    public int normalSize;
    public int f67645b;
    public int realPos;
    public int curPos;
    public final int MAX_DOT_SIZE;
    public int imageSize;
    public ArrayList<ImageView> dotList;
    public int res;
    public Map<Integer, View> f67652i;
    @JvmOverloads
    public DotIndicatorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @JvmOverloads
    public DotIndicatorView(Context context, AttributeSet attributeSet, int i15) {
        super(context, attributeSet, i15);
        Intrinsics.checkNotNullParameter(context, "context");
        this.f67652i = new LinkedHashMap();
        Resources system = Resources.getSystem();
        Intrinsics.checkExpressionValueIsNotNull(system, "Resources.getSystem()");
        // 圆点的大小
        this.normalSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, system.getDisplayMetrics());
        Resources system2 = Resources.getSystem();
        Intrinsics.checkExpressionValueIsNotNull(system2, "Resources.getSystem()");
        // 小圆点的大小，边距
        this.f67645b = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, system2.getDisplayMetrics());
        // 最多5个圆点
        this.MAX_DOT_SIZE = 5;
        this.dotList = new ArrayList<>();

        this.res= R.drawable.indicator_transition_v2;

    }

    /**
     * 小圆点变大
     * @param i15
     */
    public final void animaMax(int i15) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.dotList.get(i15), SCALE_X, 0.6f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.dotList.get(i15), SCALE_Y, 0.6f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.setDuration(200L);
        animatorSet.start();
    }

    /**
     * 设置边距
     * @param i15
     * @return
     */
    public final ImageView b(int i15) {
        int i16 = this.normalSize;
        LayoutParams layoutParams = new LayoutParams(i16, i16);
        if (i15 > 0) {
            layoutParams.setMargins(this.f67645b, 0, 0, 0);
        }
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(this.res);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }


    /**
     * 小圆点变大变小逻辑
     * @param i15
     */
    public final void c(int i15) {
        int i16;
        if (i15 != this.realPos) {
            boolean z15 = true;
            if (i15 >= 0 && i15 < this.imageSize) {
                int i17 = this.imageSize;
                if (i17 <= 5) {
                    this.curPos = i15;
                } else {
                    if (i15 < i17 && i17 + (-4) <= i15) {
                        i16 = (i17 - 5) * (this.normalSize + this.f67645b);
                        this.curPos = i15 - (i17 - 5);
                        this.dotList.get(i17 - 5).setScaleX(0.6f);
                        this.dotList.get(this.imageSize - 5).setScaleY(0.6f);
                        int i18 = this.imageSize;
                        for (int i19 = i18 - 4; i19 < i18; i19++) {
                            this.dotList.get(i19).setScaleX(1.0f);
                            this.dotList.get(i19).setScaleY(1.0f);
                        }
                    } else if (2 <= i15 && i15 < i17 - 4) {
                        int i25 = i15 - 1;
                        int i26 = (this.normalSize + this.f67645b) * i25;
                        this.curPos = 1;
                        this.dotList.get(i25).setScaleX(0.6f);
                        this.dotList.get(i25).setScaleY(0.6f);
                        int i27 = i15 + 3;
                        this.dotList.get(i27).setScaleX(0.6f);
                        this.dotList.get(i27).setScaleY(0.6f);
                        for (int i28 = i15; i28 < i27; i28++) {
                            this.dotList.get(i28).setScaleX(1.0f);
                            this.dotList.get(i28).setScaleY(1.0f);
                        }
                        i16 = i26;
                    } else {
                        if ((i15 < 0 || i15 >= 2) ? false : false) {
                            this.curPos = i15;
                            for (int i29 = 0; i29 < 4; i29++) {
                                this.dotList.get(i29).setScaleX(1.0f);
                                this.dotList.get(i29).setScaleY(1.0f);
                            }
                            this.dotList.get(4).setScaleX(0.6f);
                            this.dotList.get(4).setScaleY(0.6f);
                        }
                        i16 = 0;
                    }
                    float x15 = (-i16) - this.dotList.get(0).getX();
                    int i35 = this.imageSize;
                    for (int i36 = 0; i36 < i35; i36++) {
                        ImageView imageView = this.dotList.get(i36);
                        imageView.setX(imageView.getX() + x15);
                    }
                }
                Drawable drawable = this.dotList.get(this.realPos).getDrawable();
                Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
                ((TransitionDrawable) drawable).reverseTransition(0);
                Drawable drawable2 = this.dotList.get(i15).getDrawable();
                Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
                ((TransitionDrawable) drawable2).startTransition(0);
                this.realPos = i15;
            }
        }
    }


    /**
     * 右滑  2222211  小圆点居中
     *    //1122121  小圆点在倒数第二个
     */
    public final void stepBack() {
        Drawable drawable = this.dotList.get(this.realPos).getDrawable();
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) drawable).reverseTransition(200);
        Drawable drawable2 = this.dotList.get(this.realPos - 1).getDrawable();
        Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) drawable2).startTransition(200);
        int i15 = this.curPos;
        if (i15 == 2 && this.realPos != 2) {
            playAnimation(false);
            int i16 = this.realPos;
            if (i16 != 2) {
                animaMin(i16 - 2);
            }
            animaMax(this.realPos - 2);
            animaMin(this.realPos + 1);
        } else {
            this.curPos = i15 - 1;
        }
        this.realPos--;
    }


    /**
     * 左滑 2343211 小圆点居中
     *   //3232121  小圆点在倒数第二个
     */
    public final void stepNext() {
        Drawable drawable = this.dotList.get(this.realPos).getDrawable();
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) drawable).reverseTransition(200);
        Drawable drawable2 = this.dotList.get(this.realPos + 1).getDrawable();
        Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) drawable2).startTransition(200);
        int i15 = this.curPos;
        if (i15 == 2 && this.realPos != this.imageSize - 3) {
            playAnimation(true);
            int i16 = this.realPos;

            if (i16 != this.imageSize - 4) {
                animaMin(i16 + 3);
            }
            animaMax(this.realPos + 2);
            animaMin(this.realPos - 1);
        } else {
            this.curPos = i15 + 1;
        }
        this.realPos++;
    }


    /**
     * 变小动画
     * @param i15
     */
    public final void animaMin(int i15) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.dotList.get(i15), SCALE_X, 1.0f, 0.6f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.dotList.get(i15), SCALE_Y, 1.0f, 0.6f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.setDuration(200L);
        animatorSet.start();
    }

    public final void g(int i15, int i16) {
        this.res = i15;
        this.normalSize = i16;
    }

    /**
     * 平移动画
     * @param z15
     */
    public final void playAnimation(boolean z15) {
        AnimatorSet animatorSet = new AnimatorSet();
        int i15 = z15 ? (-this.f67645b) - this.normalSize : this.f67645b + this.normalSize;
        int i16 = this.imageSize;
        //这里是所有小圆点平移动画
        for (int i17 = 0; i17 < i16; i17++) {
            //平移属性动画
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.dotList.get(i17), "translationX", this.dotList.get(i17).getTranslationX(), this.dotList.get(i17).getTranslationX() + i15));
        }
        animatorSet.setDuration(200L);
        animatorSet.start();
    }

    public final void setCount1(int i15) {
        int i16;
        if (i15 <= 1) {
            this.setVisibility(View.GONE);

            return;
        }
        this.setVisibility(View.VISIBLE);
        if (i15 == this.imageSize) {
            c(0);
            return;
        }
        //初始化变量
        removeAllViews();
        this.dotList.clear();
        this.curPos = 0;
        this.realPos = 0;
        this.imageSize = i15;
        int i17 = this.MAX_DOT_SIZE;
        // 设置控件的宽度，分超出最多点或最多点以内
        if (i15 >= i17) {
            i16 = (this.normalSize * i17) + ((i17 - 1) * this.f67645b);
        } else {
            i16 = ((i15 - 1) * this.f67645b) + (this.normalSize * i15);
        }
        getLayoutParams().width = i16;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        ((LayoutParams) layoutParams).gravity = 1;
        for (int i18 = 0; i18 < i15; i18++) {
            ImageView b15 = b(i18);
            addView(b15);
            this.dotList.add(b15);
        }
        Drawable drawable = this.dotList.get(0).getDrawable();
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) drawable).startTransition(0);
        int i19 = this.MAX_DOT_SIZE;
        if (i15 > i19) {
            this.dotList.get(i19 - 1).setScaleX(0.6f);
            this.dotList.get(this.MAX_DOT_SIZE - 1).setScaleY(0.6f);
        }
    }

    // 方法2，设置当前所在的位置
    public final void setSelectedIndex(int i15) {
        int i16 = this.realPos;
        if (i15 != i16) {
            boolean z15 = false;
            if (i15 >= 0 && i15 < this.imageSize) {
                z15 = true;
            }
            if (z15) {
                if (Math.abs(i15 - i16) > 1) {
                    c(i15);
                } else if (this.imageSize <= this.MAX_DOT_SIZE) {
                    Drawable drawable = this.dotList.get(this.curPos).getDrawable();
                    Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
                    ((TransitionDrawable) drawable).reverseTransition(200);
                    Drawable drawable2 = this.dotList.get(i15).getDrawable();
                    Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
                    ((TransitionDrawable) drawable2).startTransition(200);
                    int i17 = this.realPos;
                    if (i15 > i17) {
                        this.realPos = i17 + 1;
                        this.curPos++;
                        return;
                    }
                    this.realPos = i17 - 1;
                    this.curPos--;
                } else if (i15 > this.realPos) {
                    stepNext();
                } else {
                    stepBack();
                }
            }
        }
    }

    public /* synthetic */ DotIndicatorView(Context context, AttributeSet attributeSet, int i15, int i16, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i16 & 2) != 0 ? null : attributeSet, (i16 & 4) != 0 ? 0 : i15);
    }
}
