package com.example.zrwenxue.moudel.main.home.led;//package com.example.zrword.moudel.main.home.led;
//
//import android.app.Activity;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.AnimationSet;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.example.zrword.R;
//
///**
// * Created by Android Studio.
// * User: 86182
// * Date: 2022-02-03
// * Time: 14:57
// */
//class LEDPopupWindow{
//    View animaView;
//
//
//    /**
//     * LED的dialog弹窗
//     * @param activity
//     * @param popUpWindowLayout
//     * @param referenceView
//     */
//    public LEDPopupWindow(View mView,Activity activity, int popUpWindowLayout, View referenceView) {
//        this.animaView=mView;
//        /**
//         * 加载poplayout布局
//         */
//        View contentLayout = LayoutInflater.from(activity).inflate(popUpWindowLayout, null);
//
//        /**
//         * 子控件
//         */
//        setView(contentLayout);
//
//
//        PopupWindow mPopWindow = new PopupWindow(//构造
//                contentLayout,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                true);
//
//
//        /**
//         *1.设置PopupWindow的背景
//         * mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//可设置为透明
//         */
//        mPopWindow.setBackgroundDrawable(null);
//        mPopWindow.setOutsideTouchable(true);//设置PopupWindow是否能响应外部点击事件
//
//        /**
//         *2.设置外部背景变暗
//         */
//        Window mWindow = activity.getWindow();
//        WindowManager.LayoutParams params = mWindow.getAttributes();
//        params.alpha = 0.3f;
//        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        mWindow.setAttributes(params);
//
//        /**
//         *3.设置PopupWindow是否能响应点击事件
//         */
//        mPopWindow.setTouchable(true);
//
//        /**
//         *4.下面两行防止和软键盘冲突
//         */
//        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//
//        /**
//         * 5.参照view
//         *  1.直接显示在参照View 的左下方
//         *  mPopWindow.showAsDropDown(View anchor);
//         *
//         *  2.xoff yoff相对于锚点的左下角,坐标系是android传统坐标系（即左上角）
//         *  mPopWindow.showAsDropDown(View anchor, int xoff, int off)
//         *
//         *  3.对于整个屏幕的window而言，通过gravity调整显示在左、上、右、下、中. x,y调整两个方向的偏移
//         *  mPopWindow.showAsDropDown(View parent, int gravity, int x, int y)
//         */
//        mPopWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
//
//        /**
//         * 7.设置动画
//         */
//        mPopWindow.setAnimationStyle(R.style.animTranslate);
//
//        /**
//         *8.消失监听
//         */
//        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                if (mWindow != null) {
//                    WindowManager.LayoutParams params = mWindow.getAttributes();
//                    params.alpha = 1.0f;
//                    mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                    mWindow.setAttributes(params);
//                }
//            }
//        });
//    }
//
//    /**
//     * 子控件的点击等
//     * @param view
//     */
//    private void setView(View view) {
//        TextView tempView=view.findViewById(R.id.led_dialog_temp);
//
//        view.findViewById(R.id.led_dialog_slide).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //滑动
//                setAnima(1,tempView);
//                setAnima2(1);
//            }
//        });
//
//        view.findViewById(R.id.led_dialog_static).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //静止
//                setAnima(2,tempView);
//                setAnima2(2);
//            }
//        });
//
//        view.findViewById(R.id.led_dialog_twinkle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //闪烁
//                setAnima(3,tempView);
//                setAnima2(3);
//            }
//        });
//        view.findViewById(R.id.led_dialog_slide_twinkle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //滑闪
//                setAnima(4,tempView);
//                setAnima2(4);
//            }
//        });
//    }
//
//
//   void  setAnima(int tag,View view){
//        switch (tag){
//            case 1:
//                view.clearAnimation();//清除动画
//                TranslateAnimation mTranslateAnimation = new TranslateAnimation(
//                        TranslateAnimation.RELATIVE_TO_PARENT, -1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
//                mTranslateAnimation.setInterpolator(new LinearInterpolator());
//                mTranslateAnimation.setDuration(5000);
//                mTranslateAnimation.setRepeatCount(Integer.MAX_VALUE);
//                view.setAnimation(mTranslateAnimation);
//                break;
//            case 2:
//                view.clearAnimation();//清除动画
//                break;
//            case 3:
//                view.clearAnimation();//清除动画
//
//                AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1f);
//                mAlphaAnimation.setDuration(500);
//                mAlphaAnimation.setRepeatCount(Integer.MAX_VALUE);
//                view.setAnimation(mAlphaAnimation);
//                break;
//            case 4:
//                view.clearAnimation();//清除动画
//
//                AnimationSet mAnimationSet = new AnimationSet(false);//设置共同属性
//
//                AlphaAnimation mAlphaAnimation2 = new AlphaAnimation(0.0f, 1f);
//                mAlphaAnimation2.setDuration(500);
//                mAlphaAnimation2.setRepeatCount(Integer.MAX_VALUE);
//                mAnimationSet.addAnimation(mAlphaAnimation2);//添加进组合动画
//
//                TranslateAnimation mTranslateAnimation2 = new TranslateAnimation(
//                        TranslateAnimation.RELATIVE_TO_PARENT, -1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
//                mTranslateAnimation2.setDuration(5000);
//                mTranslateAnimation2.setRepeatCount(Integer.MAX_VALUE);
//                mAnimationSet.addAnimation(mTranslateAnimation2);//添加进组合动画
//
//                view.setAnimation(mAnimationSet);//开始动画
//                break;
//
//        }
//    }
//
//    void  setAnima2(int tag){
//        switch (tag){
//            case 1:
//                animaView.clearAnimation();
//                TranslateAnimation mTranslateAnimation = new TranslateAnimation(
//                        TranslateAnimation.RELATIVE_TO_PARENT,0.0f ,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, -1f);
//                mTranslateAnimation.setInterpolator(new LinearInterpolator());
//                mTranslateAnimation.setDuration(5000);
//                mTranslateAnimation.setRepeatCount(Integer.MAX_VALUE);
//                animaView.setAnimation(mTranslateAnimation);
//                break;
//            case 2:
//                animaView.clearAnimation();
//                break;
//            case 3:
//                animaView.clearAnimation();
//
//                AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1f);
//                mAlphaAnimation.setDuration(500);
//                mAlphaAnimation.setRepeatCount(Integer.MAX_VALUE);
//                animaView.setAnimation(mAlphaAnimation);
//                break;
//            case 4:
//                animaView.clearAnimation();
//
//                AnimationSet mAnimationSet = new AnimationSet(false);//设置共同属性
//
//                AlphaAnimation mAlphaAnimation2 = new AlphaAnimation(0.0f, 1f);
//                mAlphaAnimation2.setDuration(500);
//                mAlphaAnimation2.setRepeatCount(Integer.MAX_VALUE);
//                mAnimationSet.addAnimation(mAlphaAnimation2);//添加进组合动画
//
//                TranslateAnimation mTranslateAnimation2 = new TranslateAnimation(
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, -1f);
//                mTranslateAnimation2.setDuration(5000);
//                mTranslateAnimation2.setRepeatCount(Integer.MAX_VALUE);
//                mAnimationSet.addAnimation(mTranslateAnimation2);//添加进组合动画
//
//                animaView.setAnimation(mAnimationSet);//开始动画
//                break;
//        }
//    }
//
//
//}
