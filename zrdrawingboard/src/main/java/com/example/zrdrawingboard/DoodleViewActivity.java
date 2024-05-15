package com.example.zrdrawingboard;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.blankj.utilcode.util.BarUtils;
import com.example.zrdrawingboard.brushviewdemo.AdapterColors;
import com.example.zrdrawingboard.brushviewdemo.BrushView;
import com.example.zrdrawingboard.brushviewdemo.ModelColors;
import com.example.zrdrawingboard.doodleview.ActionType;
import com.example.zrdrawingboard.doodleview.DoodleView;
import com.example.zrtool.ui.custom.TitleBarView;
import com.example.zrtool.ui.noslidingconflictview.NoScrollGridView;
import com.example.zrtool.utils.ToastUtils;

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
    }

    private View topSet;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private void setBrush() {
        topSet = findViewById(R.id.top_set);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetColorPopupWindow();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存
//                String path = mDoodleView.saveBitmap(mDoodleView);
//                ToastUtils.INSTANCE.showCenterToast(getApplicationContext(),"保存路径:" + path);
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
        topView.setOnclickRight(View.VISIBLE, getResources().getDrawable(com.example.zrframe.R.drawable.icon_set), new View.OnClickListener() {
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
        mDoodleView.setColor(PaintSingle.INSTANCE.getMDoodleViewColor());
        mDoodleView.setBgColor(PaintSingle.INSTANCE.getMDoodleViewBgColor());
        mDoodleView.setType(PaintSingle.INSTANCE.getMDoodleViewType());
        mDoodleView.setSize(dip2px(PaintSingle.INSTANCE.getMDoodleViewSize()));
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
    private void brushViewInit(View rootView) {
        /**
         * Getting Colors from resources and add to ArrayList
         */
        v1=rootView.findViewById(R.id.v1);
        v1.setBackground(DrawableUtils.createCircleDrawable(R.color.ac_splash_bg_color));

        int[] col = getResources().getIntArray(R.array.colors);
        itemsColors = new ArrayList<ModelColors>();
        for (int i = 0; i < col.length; i++) {
            ModelColors model = new ModelColors();
            model.setColor(col[i]);
            itemsColors.add(model);
        }

        /**
         * Initialize Views
         */
        brushView = (BrushView) rootView.findViewById(R.id.brushView);
        popClose = (View) rootView.findViewById(R.id.pop_close);

        popClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        seekBarSize = (AppCompatSeekBar) rootView.findViewById(R.id.seekBarSize);
        seekBarAlpha = (AppCompatSeekBar) rootView.findViewById(R.id.seekBarAlpha);

        txtSize = (TextView) rootView.findViewById(R.id.textViewBrushSize);
        txtAlpha = (TextView) rootView.findViewById(R.id.textViewBrushAlpha);

        gridView = (NoScrollGridView) rootView.findViewById(R.id.gridViewColors);

        /**
         * SeekBar Listeners
         */
        txtSize.setText(PaintSingle.INSTANCE.getProgress()+"");
        seekBarSize.setProgress(PaintSingle.INSTANCE.getProgress());

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtSize.setText(String.valueOf(progress));
                brushView.setRadius(progress);
                mDoodleView.setSize(dip2px(progress+1));
                PaintSingle.INSTANCE.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtAlpha.setText(String.valueOf(progress));
                brushView.setAlphaValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brushView.setColor(itemsColors.get(position).getColor());
                mDoodleView.setColor("#" + Integer.toHexString(itemsColors.get(position).getColor()));
            }
        });

        adapter = new AdapterColors(this, itemsColors);
        gridView.setAdapter(adapter);
    }






    /**
     * 显示选择画笔形状的对话框
     */
    private AlertDialog mShapeDialog;
    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"点", "线", "矩形", "圆", "实心矩形", "实心圆"}, 0,
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
     * @param dpValue
     * @return
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}














