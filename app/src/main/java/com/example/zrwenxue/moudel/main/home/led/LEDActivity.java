package com.example.zrwenxue.moudel.main.home.led;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.newzr.R;
import com.example.zrtool.ui.noslidingconflictview.MaxRecyclerView;
import com.example.zrtool.utilsjava.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-20
 * Time: 17:52
 */
public class LEDActivity extends AppCompatActivity {
    HorizontalScrollView ledHorizontalScrollView;
    TextView ledTv;
    View faceView;

    String[] quotes = new String[]{
            "伤心不是哭的理由，傻才是。",
            "心怀不惧，才能翱翔于天际。",
            "血红的月光，映照着我的生命，以及，你的死期。",
            "身为男人，无法让步的有两件事：胜利和小乔！",
            "花会枯萎，爱永不凋零。",
            "少女心，破碎了。",
            "朕会用宽广的心胸包容美女们的大不敬。",
            "教学生，顺便拯救世界。",
            "贫僧自东土大唐而来，要去往西方取经之地。",
            "啊，被玩坏了。",
            "风会带走你曾经存在的证明。",
            "以陛下的名义你被捕了！",
            "原来，新的历史不再有我。",
            "闪到腰了，先撤了。",
            "朕，很中意你。",
            "狄大人，下月的工资评定，请对我温柔一点。",
            "没有心，就不会受伤。",
            "我们的灵魂总算起共鸣了，虽说偶尔还是会跑调。",
            "俗说说得好，有钱男子汉，无钱汉子难。",
            "暖男，主要看气质。",
            "到达胜利之前，无法回头。",
            "不是你记忆中的荆轲，但致命的程度没两样。",
            "我爹都没打过我。",
            "生亦何哀，死亦何苦。",
            "生命就像人家的魔法书，涂涂改改又是一年。",
            "故乡的梅花开了吗？",
            "小小少年，没有烦恼，万事都有老爹罩。",
            "素质呢，拿出素质来，停止广场舞，关掉噪音。",
            "说好的四保一呢。",
            "花有再开的那天，人有重逢的时候吗？",
            "生在黑暗，心向光明。",
            "整容手术都是骗人的，青春美貌全靠把你吸干。",
            "离家太远会忘记故乡，杀人太多会忘掉自己。",
            "静静，我想要静静。",
            "给我点血，让我忘记疯狂！",
            "天地与我并生，万物与我为一。",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        init2();
    }

    private void init2() {
        //设置屏幕常亮
        ScreenUtils.setScreenBright(this);
        //找控件
        ledHorizontalScrollView = findViewById(R.id.led_HorizontalScrollView);
        ledTv = findViewById(R.id.led_tv);

        faceView = findViewById(R.id.face_view);
        //点击事件  用于弹出弹窗并进行设置
        faceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**弹出设置弹窗*/
                showPopupWindow();
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*沉浸式标题栏*/
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 弹窗设置
     */
    private PopupWindow popupWindow = null;

    private boolean isShowPop=false;
    private void showPopupWindow() {
        if (isShowPop){
            return;
        }
        isShowPop=true;

        //先关闭
        if (animator != null) {
            animator.cancel();
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }


        View inflateView = LinearLayout.inflate(this, R.layout.dialog_led_set, null);
        popViewInit(inflateView);

        /**
         * pop实例
         */
        popupWindow = new PopupWindow(
                inflateView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        //动画
        popupWindow.setAnimationStyle(R.style.BottomDialogAnimation);

        if (popupWindow.isShowing()) {//如果正在显示，关闭弹窗。
            popupWindow.dismiss();
        } else {
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0);
            bgAlpha(this, 0.5f);//设置透明度0.5
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    bgAlpha(LEDActivity.this, 1f);//设置透明度0.5

                    setCurrentAnim();

                    isShowPop=false;
                }
            });
        }
    }


    private LinearLayout popClose;
    private LinearLayout ledPopBg;
    private TextView ledPopTv;
    private AppCompatSeekBar seekBarBg1;
    private TextView seekBarBg1Tv;
    private AppCompatSeekBar seekBarBg2;
    private TextView seekBarBg2Tv;
    private AppCompatSeekBar seekBarBg3;
    private TextView seekBarBg3Tv;
    private AppCompatSeekBar seekBarT1;
    private TextView seekBarT1Tv;
    private AppCompatSeekBar seekBarT2;
    private TextView seekBarT2Tv;
    private AppCompatSeekBar seekBarT3;
    private TextView seekBarT3Tv;
    private AppCompatSeekBar seekBarSize;
    private TextView seekBarSizeTv;
    private AppCompatSeekBar seekBarSpeed;
    private TextView seekBarSpeedTv;
    private Button ledDialogSlide;
    private Button ledDialogStatic;
    private Button ledDialogTwinkle;
    private Button ledDialogSlideTwinkle;

    private MaxRecyclerView maxRecyclerView;

    private void popViewInit(View rootView) {
        /**
         * Getting Colors from resources and add to ArrayList
         */

        popClose = rootView.findViewById(R.id.pop_close);
        popClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ledPopBg = rootView.findViewById(R.id.led_pop_bg);
        ledPopBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置文字
                showEditDialog();
            }
        });
        ledPopBg.setBackgroundColor(
                Color.argb(
                        255,
                        LEDSingle.INSTANCE.getMBgColorR(),
                        LEDSingle.INSTANCE.getMBgColorG(),
                        LEDSingle.INSTANCE.getMBgColorB()
                )
        );
        ledPopTv = rootView.findViewById(R.id.led_pop_tv);
        ledPopTv.setText(LEDSingle.INSTANCE.getMTvStr());
        //文本颜色
        ledPopTv.setTextColor(
                Color.argb(
                        255,
                        LEDSingle.INSTANCE.getMTvColorR(),
                        LEDSingle.INSTANCE.getMTvColorG(),
                        LEDSingle.INSTANCE.getMTvColorB()
                )
        );
        setAnima(LEDSingle.INSTANCE.getMTvSpeed(), LEDSingle.INSTANCE.getMTvAnimaType(), ledPopTv);

        //背景
        seekBarBg1 = rootView.findViewById(R.id.seekBar_Bg_1);
        seekBarBg1Tv = rootView.findViewById(R.id.seekBar_Bg_1_tv);
        setSeekBarsss(seekBarBg1, seekBarBg1Tv, LEDSingle.INSTANCE.getMBgColorR(), 0);
        seekBarBg2 = rootView.findViewById(R.id.seekBar_Bg_2);
        seekBarBg2Tv = rootView.findViewById(R.id.seekBar_Bg_2_tv);
        setSeekBarsss(seekBarBg2, seekBarBg2Tv, LEDSingle.INSTANCE.getMBgColorG(), 1);
        seekBarBg3 = rootView.findViewById(R.id.seekBar_Bg_3);
        seekBarBg3Tv = rootView.findViewById(R.id.seekBar_Bg_3_tv);
        setSeekBarsss(seekBarBg3, seekBarBg3Tv, LEDSingle.INSTANCE.getMBgColorB(), 2);

        //文字
        seekBarT1 = rootView.findViewById(R.id.seekBar_t_1);
        seekBarT1Tv = rootView.findViewById(R.id.seekBar_t_1_tv);
        setSeekBarsss(seekBarT1, seekBarT1Tv, LEDSingle.INSTANCE.getMTvColorR(), 3);
        seekBarT2 = rootView.findViewById(R.id.seekBar_t_2);
        seekBarT2Tv = rootView.findViewById(R.id.seekBar_t_2_tv);
        setSeekBarsss(seekBarT2, seekBarT2Tv, LEDSingle.INSTANCE.getMTvColorG(), 4);
        seekBarT3 = rootView.findViewById(R.id.seekBar_t_3);
        seekBarT3Tv = rootView.findViewById(R.id.seekBar_t_3_tv);
        setSeekBarsss(seekBarT3, seekBarT3Tv, LEDSingle.INSTANCE.getMTvColorB(), 5);

        //大小
        seekBarSize = rootView.findViewById(R.id.seekBar_size);
        seekBarSizeTv = rootView.findViewById(R.id.seekBar_size_tv);
        setSeekBarsss(seekBarSize, seekBarSizeTv, LEDSingle.INSTANCE.getMTvSize(), 6);


        //速度
        seekBarSpeed = rootView.findViewById(R.id.seekBar_speed);
        seekBarSpeedTv = rootView.findViewById(R.id.seekBar_speed_tv);
        setSeekBarsss(seekBarSpeed, seekBarSpeedTv, LEDSingle.INSTANCE.getMTvSpeed(), 7);

        //动画效果
        ledDialogSlide = rootView.findViewById(R.id.led_dialog_slide);
        ledDialogSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //滑动
                setAnima(LEDSingle.INSTANCE.getMTvSpeed(),1, ledPopTv);
                LEDSingle.INSTANCE.setMTvAnimaType(1);
            }
        });
        ledDialogStatic = rootView.findViewById(R.id.led_dialog_static);
        ledDialogStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnima(LEDSingle.INSTANCE.getMTvSpeed(),2, ledPopTv);
                LEDSingle.INSTANCE.setMTvAnimaType(2);
            }
        });
        ledDialogTwinkle = rootView.findViewById(R.id.led_dialog_twinkle);
        ledDialogTwinkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnima(LEDSingle.INSTANCE.getMTvSpeed(),3, ledPopTv);
                LEDSingle.INSTANCE.setMTvAnimaType(3);
            }
        });
        ledDialogSlideTwinkle = rootView.findViewById(R.id.led_dialog_slide_twinkle);
        ledDialogSlideTwinkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnima(LEDSingle.INSTANCE.getMTvSpeed(),4, ledPopTv);
                LEDSingle.INSTANCE.setMTvAnimaType(4);
            }
        });

        //设置双色推荐色
        maxRecyclerView = rootView.findViewById(R.id.led_double_color_recyclerview);
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        maxRecyclerView.setLayoutManager(layoutManager);

        mList.clear();



        mList.add(new LedDoubleColorBean(85, 72, 127, 175, 209, 209, quotes[0]));
        mList.add(new LedDoubleColorBean(190, 178, 148, 79, 66, 55, quotes[1]));
        mList.add(new LedDoubleColorBean(122, 65, 203, 117, 183, 224, quotes[2]));
        mList.add(new LedDoubleColorBean(0, 0, 0, 255, 255, 255, "区区致命伤……"));
        mList.add(new LedDoubleColorBean(117, 115, 71, 172, 180, 148, quotes[3]));
        mList.add(new LedDoubleColorBean(167, 126, 96, 227, 179, 205, quotes[4]));
        mList.add(new LedDoubleColorBean(52, 14, 98, 107, 23, 209, quotes[5]));
        mList.add(new LedDoubleColorBean(69, 60, 56, 117, 133, 122, quotes[6]));
        mList.add(new LedDoubleColorBean(206, 51, 87, 215, 133, 176, quotes[7]));
        mList.add(new LedDoubleColorBean(96, 164, 231, 238, 230, 211, quotes[8]));
        mList.add(new LedDoubleColorBean(46, 96, 68, 120, 151, 60, quotes[9]));
        mList.add(new LedDoubleColorBean(205, 106, 103, 255, 255, 255, quotes[10]));
        mList.add(new LedDoubleColorBean(54, 76, 102, 105, 189, 151, quotes[11]));
        mList.add(new LedDoubleColorBean(181, 188, 175, 218, 221, 214, quotes[12]));
        mList.add(new LedDoubleColorBean(101, 174, 230, 211, 78, 61, quotes[13]));
        mList.add(new LedDoubleColorBean(44, 35, 102, 107, 186, 201, quotes[14]));
        mList.add(new LedDoubleColorBean(65, 83, 40, 236, 224, 230, quotes[15]));
        mList.add(new LedDoubleColorBean(88, 13, 168, 236, 179, 109, quotes[16]));
        mList.add(new LedDoubleColorBean(207, 199, 162, 190, 164, 175, quotes[17]));
        mList.add(new LedDoubleColorBean(106, 199, 147, 40, 75, 95, quotes[18]));
        mList.add(new LedDoubleColorBean(30, 56, 88, 204, 105, 115, quotes[19]));
        mList.add(new LedDoubleColorBean(14, 34, 65, 89, 167, 98, quotes[20]));
        mList.add(new LedDoubleColorBean(200, 155, 68, 243, 243, 219, quotes[21]));
        mList.add(new LedDoubleColorBean(182, 39, 38, 129, 43, 42, quotes[22]));
        mList.add(new LedDoubleColorBean(238, 178, 61, 224, 94, 41, quotes[23]));
        mList.add(new LedDoubleColorBean(54, 80, 133, 97, 177, 227, quotes[24]));
        mList.add(new LedDoubleColorBean(231, 183, 69, 199, 83, 64, quotes[25]));
        mList.add(new LedDoubleColorBean(52, 37, 72, 132, 193, 215, quotes[26]));
        mList.add(new LedDoubleColorBean(223, 93, 87, 144, 149, 210, quotes[27]));
        mList.add(new LedDoubleColorBean(73, 31, 161, 73, 120, 195,quotes[28]));
        mList.add(new LedDoubleColorBean(67, 21, 130, 234, 185, 63, quotes[29]));



        mAdapter = new LEDdoubleAdapter(this, mList);
        maxRecyclerView.setAdapter(mAdapter);


        mAdapter.setLEDdoubleAdapterCallBack(new LEDdoubleAdapter.InnerInterface() {
            @Override
            public void onclick(@NonNull String str, int bg1, int bg2, int bg3, int tv1, int tv2, int tv3) {

                ledPopTv.setText(str);

                LEDSingle.INSTANCE.setMBgColorR(bg1);
                LEDSingle.INSTANCE.setMBgColorG(bg2);
                LEDSingle.INSTANCE.setMBgColorB(bg3);

                LEDSingle.INSTANCE.setMTvColorR(tv1);
                LEDSingle.INSTANCE.setMTvColorG(tv2);
                LEDSingle.INSTANCE.setMTvColorB(tv3);

                LEDSingle.INSTANCE.setMTvStr(str);


                ledPopBg.setBackgroundColor(
                        Color.argb(
                                255,
                                bg1,
                                bg2,
                                bg3
                        )
                );

                //文本颜色
                ledPopTv.setTextColor(
                        Color.argb(
                                255,
                                tv1,
                                tv2,
                                tv3
                        )
                );
            }
        });
    }

    private LEDdoubleAdapter mAdapter;
    private ArrayList<LedDoubleColorBean> mList = new ArrayList<>();

    //拖动监听等
    private void setSeekBarsss(SeekBar mSeekBar, TextView tv, int getProgress, int singleType) {
        //初始化 进度条
        mSeekBar.setProgress(getProgress);
        //初始化 进度条后面文本
        switch (singleType) {
            case 0:
                tv.setText(LEDSingle.INSTANCE.getMBgColorR() + "");
                break;
            case 1:
                tv.setText(LEDSingle.INSTANCE.getMBgColorG() + "");
                break;
            case 2:
                tv.setText(LEDSingle.INSTANCE.getMBgColorB() + "");
                break;
            case 3:
                tv.setText(LEDSingle.INSTANCE.getMTvColorR() + "");
                break;
            case 4:
                tv.setText(LEDSingle.INSTANCE.getMTvColorG() + "");
                break;
            case 5:
                tv.setText(LEDSingle.INSTANCE.getMTvColorB() + "");
                break;
            case 6:
                tv.setText(LEDSingle.INSTANCE.getMTvSize() + "");//文本大小
                break;
            case 7:
                tv.setText(LEDSingle.INSTANCE.getMTvSpeed() + "");//文本速度
                break;
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText(String.valueOf(progress));//进度监听
                switch (singleType) {
                    case 0:
                        LEDSingle.INSTANCE.setMBgColorR(progress);
                        ledPopBg.setBackgroundColor(
                                Color.argb(
                                        255,
                                        progress,
                                        LEDSingle.INSTANCE.getMBgColorG(),
                                        LEDSingle.INSTANCE.getMBgColorB()
                                )
                        );
                        break;
                    case 1:
                        LEDSingle.INSTANCE.setMBgColorG(progress);
                        ledPopBg.setBackgroundColor(
                                Color.argb(
                                        255,
                                        LEDSingle.INSTANCE.getMBgColorR(),
                                        progress,
                                        LEDSingle.INSTANCE.getMBgColorB()
                                )
                        );
                        break;
                    case 2:
                        LEDSingle.INSTANCE.setMBgColorB(progress);
                        ledPopBg.setBackgroundColor(
                                Color.argb(
                                        255,
                                        LEDSingle.INSTANCE.getMBgColorR(),
                                        LEDSingle.INSTANCE.getMBgColorG(),
                                        progress
                                )
                        );
                        break;
                    case 3:
                        LEDSingle.INSTANCE.setMTvColorR(progress);
                        ledPopTv.setTextColor(
                                Color.argb(
                                        255,
                                        progress,
                                        LEDSingle.INSTANCE.getMTvColorG(),
                                        LEDSingle.INSTANCE.getMTvColorB()
                                )
                        );
                        break;
                    case 4:
                        LEDSingle.INSTANCE.setMTvColorG(progress);
                        ledPopTv.setTextColor(
                                Color.argb(
                                        255,
                                        LEDSingle.INSTANCE.getMTvColorR(),
                                        progress,
                                        LEDSingle.INSTANCE.getMTvColorB()
                                )
                        );
                        break;
                    case 5:
                        LEDSingle.INSTANCE.setMTvColorB(progress);
                        ledPopTv.setTextColor(
                                Color.argb(
                                        255,
                                        LEDSingle.INSTANCE.getMTvColorR(),
                                        LEDSingle.INSTANCE.getMTvColorG(),
                                        progress
                                )
                        );
                        break;
                    case 6:
                        LEDSingle.INSTANCE.setMTvSize(progress + 1);
                        ledPopTv.setTextSize(dip2px(progress + 1) / 10);
                        break;
                    case 7:
                        LEDSingle.INSTANCE.setMTvSpeed(progress + 1);
                        setAnima(progress + 1,LEDSingle.INSTANCE.getMTvAnimaType(),ledPopTv);
                        break;
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //pop预览动画
    void setAnima(int duration,int tag, View view) {
        switch (tag) {
            case 1:
                view.clearAnimation();//清除动画
                TranslateAnimation mTranslateAnimation = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
                        TranslateAnimation.RELATIVE_TO_PARENT, -1f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
                mTranslateAnimation.setInterpolator(new LinearInterpolator());
                mTranslateAnimation.setDuration(duration*1000);
                mTranslateAnimation.setRepeatCount(Integer.MAX_VALUE);
                view.setAnimation(mTranslateAnimation);
                break;
            case 2:
                view.clearAnimation();//清除动画
                break;
            case 3:
                view.clearAnimation();//清除动画

                AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1f);
                mAlphaAnimation.setDuration(500);
                mAlphaAnimation.setRepeatCount(Integer.MAX_VALUE);
                mAlphaAnimation.setInterpolator(new LinearInterpolator());
                view.setAnimation(mAlphaAnimation);
                break;
            case 4:
                view.clearAnimation();//清除动画

                AnimationSet mAnimationSet = new AnimationSet(false);//设置共同属性

                AlphaAnimation mAlphaAnimation2 = new AlphaAnimation(0.0f, 1f);
                mAlphaAnimation2.setDuration(500);
                mAlphaAnimation2.setRepeatCount(Integer.MAX_VALUE);
                mAlphaAnimation2.setInterpolator(new LinearInterpolator());
                mAnimationSet.addAnimation(mAlphaAnimation2);//添加进组合动画

                TranslateAnimation mTranslateAnimation2 = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_PARENT, 1f,
                        TranslateAnimation.RELATIVE_TO_PARENT, -1f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
                mTranslateAnimation2.setDuration(duration*1000);
                mTranslateAnimation2.setRepeatCount(Integer.MAX_VALUE);
                mTranslateAnimation2.setInterpolator(new LinearInterpolator());
                mAnimationSet.addAnimation(mTranslateAnimation2);//添加进组合动画

                view.setAnimation(mAnimationSet);//开始动画
                break;

        }
    }

    public void bgAlpha(Activity context, float f) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = f;
        context.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {//如果正在显示，关闭弹窗。
            popupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {//如果正在显示，关闭弹窗。
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置动画
        setCurrentAnim();
    }

    /**
     * 设置动画
     */
    private void setCurrentAnim() {
        //文字
        ledTv.setText(LEDSingle.INSTANCE.getMTvStr());
        //文本颜色
        ledTv.setTextColor(
                Color.argb(
                        255,
                        LEDSingle.INSTANCE.getMTvColorR(),
                        LEDSingle.INSTANCE.getMTvColorG(),
                        LEDSingle.INSTANCE.getMTvColorB()
                )
        );
        //文字大小
        ledTv.setTextSize(
                dip2px(LEDSingle.INSTANCE.getMTvSize())
        );

        //背景颜色
        ledHorizontalScrollView.setBackgroundColor(
                Color.argb(
                        255,
                        LEDSingle.INSTANCE.getMBgColorR(),
                        LEDSingle.INSTANCE.getMBgColorG(),
                        LEDSingle.INSTANCE.getMBgColorB()
                )
        );

        //文字速度
        setObjectAnim(LEDSingle.INSTANCE.getMTvSpeed(), ledTv, LEDSingle.INSTANCE.getMTvAnimaType());

    }

    /**
     * @param duration  最低不低于单位1
     * @param mTextView
     */
    ObjectAnimator animator;
    AnimatorSet animatorSet;

    private void setObjectAnim(int duration, TextView mTextView, int animType) {
//        if (animatorSet != null) {
//            animatorSet.end();
//        }
//
//        if(animator!=null){
//            animator.end();
//        }
//
//        // 获取文本的宽度
//        mTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int textWidth = mTextView.getMeasuredWidth();
//
//        // 计算滚动距离
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        switch (animType) {
            case 1:
                setAnima(duration,1,mTextView);
//                // 创建属性动画
//                animator = ObjectAnimator.ofFloat(mTextView, "translationX", screenWidth, -textWidth);
//                animator.setDuration(duration * 1000); // 设置动画时长，单位为毫秒
//                animator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数，这里设置为无限重复
//                animator.setInterpolator(new LinearInterpolator()); // 设置插值器为线性插值器，实现匀速效果
//                animator.setRepeatMode(ValueAnimator.RESTART); // 设置重复模式，这里设置为重新开始
//                // 开始动画
//                animator.start();
                break;
            case 2:
                setAnima(duration,2,mTextView);

                break;
            case 3:
                setAnima(duration,3,mTextView);
//                // 渐隐动画
//                animator = ObjectAnimator.ofFloat(mTextView, "alpha", 0f, 1f);
//                animator.setDuration(500); // 设置动画时长，单位为毫秒
//                animator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数，这里设置为无限重复
//                animator.setInterpolator(new LinearInterpolator()); // 设置插值器为线性插值器，实现匀速效果
//                animator.setRepeatMode(ValueAnimator.RESTART); // 设置重复模式，这里设置为重新开始
//
//                //开始动画
//                animator.start();
                break;
            case 4:
                setAnima(duration,4,mTextView);
//                // 渐隐动画
//                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mTextView, "alpha", 0f, 1f);
//                alphaAnimator.setDuration(500);
//                alphaAnimator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数，这里设置为无限重复
//                alphaAnimator.setInterpolator(new LinearInterpolator()); // 设置插值器为线性插值器，实现匀速效果
//                alphaAnimator.setRepeatMode(ValueAnimator.RESTART); // 设置重复模式，这里设置为重新开始
//
//                // 创建属性动画
//                ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mTextView, "translationX", screenWidth, -textWidth);
//                translationAnimator.setDuration(duration * 1000); // 设置动画时长，单位为毫秒
//                translationAnimator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数，这里设置为无限重复
//                translationAnimator.setInterpolator(new LinearInterpolator()); // 设置插值器为线性插值器，实现匀速效果
//                translationAnimator.setRepeatMode(ValueAnimator.RESTART); // 设置重复模式，这里设置为重新开始
//
//                // 创建组合动画
//                animatorSet = new AnimatorSet();
//                animatorSet.playTogether(alphaAnimator, translationAnimator);
//                animatorSet.start();
                break;
        }
    }

    /***
     在Android中，要停止正在运行的属性动画，可以使用以下方法之一：

     使用 cancel() 方法：如果你有对属性动画对象的引用，可以调用 cancel() 方法来停止该动画。例如：
     ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
     animator.setDuration(1000);
     animator.start();

     // 在需要停止动画的地方调用 cancel() 方法
     animator.cancel();
     使用 end() 方法：如果你想让属性动画立即停止并将目标属性的值设置为动画的最终值，可以使用 end() 方法。例如：
     ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
     animator.setDuration(1000);
     animator.start();

     // 在需要停止动画的地方调用 end() 方法
     animator.end();
     移除动画监听器：如果你在属性动画中添加了监听器（AnimatorListener），你可以在监听器的回调方法中调用 cancel() 或 end() 方法来停止动画。例如：
     ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
     animator.setDuration(1000);
     animator.addListener(new AnimatorListenerAdapter() {
    @Override public void onAnimationEnd(Animator animation) {
    // 动画结束时调用 cancel() 或 end() 方法停止动画
    animation.cancel();
    }
    });
     animator.start();
     使用上述方法之一，你可以停止正在运行的属性动画。根据你的需求，选择适合的方法来停止动画。




     //只有控件的情况下   ViewPropertyAnimator
     可以通过控件对象来停止该控件的属性动画。在Android中，控件的属性动画是通过 ViewPropertyAnimator 实现的。你可以使用以下方法来停止控件的属性动画：

     使用 animate().cancel() 方法：通过调用控件对象的 animate() 方法获取 ViewPropertyAnimator 对象，然后调用 cancel() 方法来停止属性动画。例如：
     View view = findViewById(R.id.your_view_id);
     view.animate().cancel();

     使用 animate().end() 方法：类似地，你也可以使用 end() 方法来立即停止属性动画并将目标属性的值设置为动画的最终值。例如：
     View view = findViewById(R.id.your_view_id);
     view.animate().end();
     通过以上方法之一，你可以停止控件的属性动画。请注意，这些方法仅适用于使用 ViewPropertyAnimator 实现的属性动画。如果你使用其他方式实现属性动画，可能需要采取不同的方法来停止动画。
     */

    /**
     * 工具
     *
     * @param dpValue
     * @return
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 可输入弹窗
     */
    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LED");
        builder.setMessage("请输入字幕内容：");
        // 创建一个 EditText 控件
        final EditText inputEditText = new EditText(this);
        inputEditText.setInputType(InputType.TYPE_CLASS_TEXT); // 设置输入类型
        // 设置弹窗的视图为 EditText 控件
        builder.setView(inputEditText);

        //设置LED字幕
        inputEditText.setText(LEDSingle.INSTANCE.getMTvStr());
        // 设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = inputEditText.getText().toString().replaceAll("\\s+", "");
                // 在确定按钮点击事件中处理输入的文本
                LEDSingle.INSTANCE.setMTvStr(inputText);
                ledPopTv.setText(inputText);
            }
        });
        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 创建并显示弹窗
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
