package com.example.zrwenxue.others.zrdrawingboard;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.blankj.utilcode.util.BarUtils;

import com.example.newzr.R;
import com.example.zrdrawingboard.PaintSingle;
import com.example.zrtool.ui.noslidingconflictview.NoScrollGridView;
import com.example.zrwenxue.app.Single;
import com.example.zrwenxue.app.TitleBarView;
import com.example.zrwenxue.moudel.main.center.crypt.database.MyDatabaseHelper;
import com.example.zrwenxue.moudel.main.home.led.LEDSingle;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.others.zrdrawingboard.brushviewdemo.AdapterColors;
import com.example.zrwenxue.others.zrdrawingboard.brushviewdemo.BrushView;
import com.example.zrwenxue.others.zrdrawingboard.brushviewdemo.ModelColors;
import com.example.zrwenxue.others.zrdrawingboard.doodleview.ActionType;
import com.example.zrwenxue.others.zrdrawingboard.doodleview.DoodleView;

import java.util.ArrayList;


/**
 * 用于展示 DoodleView 功能的 Activity
 * <p>
 * Created by developerHaoz on 2017/7/14.
 */

public class DoodleViewActivity extends AppCompatActivity {
    private DoodleView mDoodleView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /**base功能1：实现沉浸式状态栏*/
        BarUtils.transparentStatusBar(this);
        BarUtils.transparentNavBar(this);
        BarUtils.setStatusBarLightMode(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodleview);

        mDoodleView = (DoodleView) findViewById(R.id.doodle_doodleview);
        mDoodleView.setSize(dip2px(5));

        setBrush();
        setTopView("涂鸦板");

        //数据库


    }

    private View topSet;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;

    private void setBrush() {
        topSet = findViewById(R.id.top_set);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetColorPopupWindow();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //形状
                showShapeDialog();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //擦除
                mDoodleView.reset();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //撤销
                mDoodleView.back();
            }
        });


        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Activity mActivity=this;



        //保存
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Single.INSTANCE.showSetNameAndDescriptionPop(mActivity,MyStatic.getBase64String(mDoodleView.getBitmap()));
            }
        });
    }


    private TitleBarView topView;

    private void setTopView(String title) {
        topView = findViewById(R.id.title_view);
        topView.setTitle(title);
        topView.setOnclickLeft(View.VISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        topView.setOnclickRight(View.VISIBLE, getResources().getDrawable(R.drawable.show_yb2), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showShapeDialog();
                if (topSet != null && topSet.getVisibility() == View.GONE) {
                    topSet.setVisibility(View.VISIBLE);
                } else {
                    topSet.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDoodleView.onTouchEvent(event);
    }


    /**
     * 弹窗设置
     */
    private PopupWindow popupWindow = null;

    private void showSetColorPopupWindow() {
        View inflateView = LinearLayout.inflate(this, R.layout.brush_view, null);
        brushViewInit(inflateView);

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
                    bgAlpha(DoodleViewActivity.this, 1f);//设置透明度0.5
                }
            });
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
        mDoodleView.setBgColor(PaintSingle.INSTANCE.getMDoodleViewBgColor());
        mDoodleView.setType(PaintSingle.INSTANCE.getMDoodleViewType());

        mDoodleView.setSize(dip2px(PaintSingle.INSTANCE.getMDoodleViewSize() + 1));
        mDoodleView.setColor(
                toHexString(Color.argb(
                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                        LEDSingle.INSTANCE.getMTvColorR(),
                        LEDSingle.INSTANCE.getMTvColorG(),
                        LEDSingle.INSTANCE.getMTvColorB()
                ))
        );
    }


    private BrushView brushView;
    private AppCompatSeekBar seekBarSize, seekBarAlpha;
    private TextView txtSize, txtAlpha;
    private View popClose;
    private NoScrollGridView gridView;
    private ModelColors modelColors;
    private AdapterColors adapter;
    private ArrayList<ModelColors> itemsColors;
    private View v1;


    private AppCompatSeekBar seekBarT1;
    private TextView seekBarT1Tv;
    private AppCompatSeekBar seekBarT2;
    private TextView seekBarT2Tv;
    private AppCompatSeekBar seekBarT3;
    private TextView seekBarT3Tv;

    private void brushViewInit(View rootView) {
        /**
         * Initialize Views
         */
        popClose = (View) rootView.findViewById(R.id.pop_close);

        popClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        //预览画笔
        brushView = (BrushView) rootView.findViewById(R.id.brushView);


        seekBarSize = (AppCompatSeekBar) rootView.findViewById(R.id.seekBarSize);
        txtSize = (TextView) rootView.findViewById(R.id.textViewBrushSize);


        seekBarAlpha = (AppCompatSeekBar) rootView.findViewById(R.id.seekBarAlpha);
        txtAlpha = (TextView) rootView.findViewById(R.id.textViewBrushAlpha);

//        gridView = (NoScrollGridView) rootView.findViewById(R.id.gridViewColors);

        //颜色  文字
        seekBarT1 = rootView.findViewById(R.id.seekBar_t_1);
        seekBarT1Tv = rootView.findViewById(R.id.seekBar_t_1_tv);
        setSeekBarsss(seekBarT1, seekBarT1Tv, LEDSingle.INSTANCE.getMTvColorR(), 3);
        seekBarT2 = rootView.findViewById(R.id.seekBar_t_2);
        seekBarT2Tv = rootView.findViewById(R.id.seekBar_t_2_tv);
        setSeekBarsss(seekBarT2, seekBarT2Tv, LEDSingle.INSTANCE.getMTvColorG(), 4);
        seekBarT3 = rootView.findViewById(R.id.seekBar_t_3);
        seekBarT3Tv = rootView.findViewById(R.id.seekBar_t_3_tv);
        setSeekBarsss(seekBarT3, seekBarT3Tv, LEDSingle.INSTANCE.getMTvColorB(), 5);

        //初始化
        setInitSeekBarsss();

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtSize.setText(progress + "");

                brushView.setRadius(progress);
                mDoodleView.setSize(dip2px(progress + 1));

                PaintSingle.INSTANCE.setMDoodleViewSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                txtAlpha.setText(progress + "");
                brushView.setAlphaValue(progress);
                mDoodleView.setColor(toHexString(Color.argb(
                                        progress,
                                        LEDSingle.INSTANCE.getMTvColorR(),
                                        LEDSingle.INSTANCE.getMTvColorG(),
                                        LEDSingle.INSTANCE.getMTvColorB()
                                )
                        )
                );

                LEDSingle.INSTANCE.setMDoodleViewColorAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

//                Log.e("tag124654",LEDSingle.INSTANCE.getMDoodleViewColorAlpha()+
//                        " ===="+LEDSingle.INSTANCE.getMTvColorR()+
//                        "===="+LEDSingle.INSTANCE.getMTvColorG()+
//                "===="+LEDSingle.INSTANCE.getMTvColorB());

//                Log.e("tag124654", toHexString(Color.argb(
//                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
//                        LEDSingle.INSTANCE.getMTvColorR(),
//                        LEDSingle.INSTANCE.getMTvColorG(),
//                        LEDSingle.INSTANCE.getMTvColorB()
//                )));


            }
        });
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                brushView.setColor(itemsColors.get(position).getColor());
//                Log.e("taggg111",itemsColors.get(position).getColor()+"");
//                mDoodleView.setColor("#" + Integer.toHexString(itemsColors.get(position).getColor()));
//                Log.e("taggg222","#" + Integer.toHexString(itemsColors.get(position).getColor()));
//            }
//        });
//
//        adapter = new AdapterColors(this, itemsColors);
//        gridView.setAdapter(adapter);
    }

    private void setSeekBarsss(SeekBar mSeekBar, TextView tv, int getProgress, int singleType) {
        //初始化 进度条
        mSeekBar.setProgress(getProgress);
        //初始化 进度条后面文本
        switch (singleType) {
            case 3:
                tv.setText(LEDSingle.INSTANCE.getMTvColorR() + "");
                break;
            case 4:
                tv.setText(LEDSingle.INSTANCE.getMTvColorG() + "");
                break;
            case 5:
                tv.setText(LEDSingle.INSTANCE.getMTvColorB() + "");
                break;
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText(String.valueOf(progress));//进度监听
                switch (singleType) {
                    case 3:
                        LEDSingle.INSTANCE.setMTvColorR(progress);
                        brushView.setColor(
                                Color.argb(
                                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                        progress,
                                        LEDSingle.INSTANCE.getMTvColorG(),
                                        LEDSingle.INSTANCE.getMTvColorB()
                                )
                        );
                        mDoodleView.setColor(toHexString(Color.argb(
                                                LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                                progress,
                                                LEDSingle.INSTANCE.getMTvColorG(),
                                                LEDSingle.INSTANCE.getMTvColorB()
                                        )
                                )
                        );
                        break;
                    case 4:
                        LEDSingle.INSTANCE.setMTvColorG(progress);
                        brushView.setColor(
                                Color.argb(
                                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                        LEDSingle.INSTANCE.getMTvColorR(),
                                        progress,
                                        LEDSingle.INSTANCE.getMTvColorB()
                                )
                        );
                        mDoodleView.setColor(
                                toHexString(Color.argb(
                                                LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                                LEDSingle.INSTANCE.getMTvColorR(),
                                                progress,
                                                LEDSingle.INSTANCE.getMTvColorB()
                                        )
                                )
                        );

                        break;
                    case 5:
                        LEDSingle.INSTANCE.setMTvColorB(progress);
                        brushView.setColor(
                                Color.argb(
                                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                        LEDSingle.INSTANCE.getMTvColorR(),
                                        LEDSingle.INSTANCE.getMTvColorG(),
                                        progress
                                )
                        );
                        mDoodleView.setColor(
                                toHexString(Color.argb(
                                                LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                                                LEDSingle.INSTANCE.getMTvColorR(),
                                                LEDSingle.INSTANCE.getMTvColorG(),
                                                progress
                                        )
                                )
                        );
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


    /**
     * 设置初始化逻辑
     */
    private void setInitSeekBarsss() {
        txtSize.setText(PaintSingle.INSTANCE.getMDoodleViewSize() + "");
        seekBarSize.setProgress(PaintSingle.INSTANCE.getMDoodleViewSize());


        txtAlpha.setText(LEDSingle.INSTANCE.getMDoodleViewColorAlpha() + "");
        seekBarAlpha.setProgress(LEDSingle.INSTANCE.getMDoodleViewColorAlpha());


        brushView.setRadius(PaintSingle.INSTANCE.getMDoodleViewSize());
        brushView.setColor(
                Color.argb(
                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                        LEDSingle.INSTANCE.getMTvColorR(),
                        LEDSingle.INSTANCE.getMTvColorG(),
                        LEDSingle.INSTANCE.getMTvColorB()
                )
        );


        mDoodleView.setSize(dip2px(PaintSingle.INSTANCE.getMDoodleViewSize() + 1));
        mDoodleView.setColor(
                toHexString(Color.argb(
                        LEDSingle.INSTANCE.getMDoodleViewColorAlpha(),
                        LEDSingle.INSTANCE.getMTvColorR(),
                        LEDSingle.INSTANCE.getMTvColorG(),
                        LEDSingle.INSTANCE.getMTvColorB()
                ))
        );
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private AlertDialog mShapeDialog;

    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
//                    .setSingleChoiceItems(new String[]{"点", "线", "矩形", "圆", "实心矩形", "实心圆","三角形","实心三角形"},
                    .setSingleChoiceItems(new String[]{"点", "线", "长方形","圆环","方块", "圆形"},
                            0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setType(ActionType.Path);
                                            break;
                                        case 1:
                                            mDoodleView.setType(ActionType.Line);
                                            break;
                                        case 2:
                                            mDoodleView.setType(ActionType.Rect);
                                            break;
                                        case 3:
                                            mDoodleView.setType(ActionType.Circle);
                                            break;
                                        case 4:
                                            mDoodleView.setType(ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            mDoodleView.setType(ActionType.FilledCircle);
                                            break;
                                        case 6:
                                            mDoodleView.setType(ActionType.Triangle);
                                            break;
                                        case 7:
                                            mDoodleView.setType(ActionType.FillTriangle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }

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


    public static String toHexString(int color) {
        return String.format("#%08X", color);
    }

}














