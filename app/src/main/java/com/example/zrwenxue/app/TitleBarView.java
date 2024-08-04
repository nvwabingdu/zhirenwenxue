package com.example.zrwenxue.app;


import static com.example.zrwenxue.app.ZrApp.context;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.newzr.R;

/**
 * Created by Android Studio.     填充修改式自定义控件
 * User: 86182
 * Date: 2022-01-20
 * Time: 13:04
 */
public class TitleBarView extends RelativeLayout {
    private RelativeLayout top_left_layout;
    private ImageView top_left;
    private TextView top_title;
    private RelativeLayout top_right_layout;
    private ImageView top_right;

    public TitleBarView(Context context) {
        super(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.include_toolbar, this);
        // 获取控件
        top_left_layout = (RelativeLayout) findViewById(R.id.toolbar_img1_layout);
        top_left = (ImageView) findViewById(R.id.toolbar_img1);
        top_title = (TextView) findViewById(R.id.toolbar_title);
        top_right_layout = (RelativeLayout) findViewById(R.id.toolbar_img2_layout);
        top_right = (ImageView) findViewById(R.id.toolbar_img2);
    }

    /**
     * toolbar左侧back返回点击事件
     * @param listener
     */
    public void setOnclickLeft(int visible,OnClickListener listener) {
        top_left.setVisibility(visible);
        top_left_layout.setOnClickListener(listener);
    }

    /**
     * toolbar左侧back返回点击事件
     * @param listener
     */
    public void setOnclickLeft(int visible,Drawable drawable,OnClickListener listener) {
        top_left.setVisibility(visible);
        if(drawable!=null){
            top_left.setImageDrawable(drawable);
        }
        top_left_layout.setOnClickListener(listener);
    }

    /**
     * 标题的点击事件
     * @param listener
     */
    public void setOnclickTitle(OnClickListener listener) {
        top_title.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        top_title.setText(title);
    }

    public String getTitle(){
        if(!top_title.getText().toString().equals("")){
            return top_title.getText().toString();
        }
        return "";
    }
    /**
     * 右侧more点击事件
     * @param listener
     */
    public void setOnclickRight(int visible,OnClickListener listener) {
        top_right.setVisibility(visible);
        top_right_layout.setOnClickListener(listener);
    }

    /**
     * 右侧more点击事件
     * @param listener
     */
    public void setOnclickRight(int visible, Drawable drawable, OnClickListener listener) {
        top_right.setVisibility(visible);
        if(drawable!=null){
            //Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), bitmap);
            //top_right.setImageDrawable(MyStatic.BitmapToDrawable(bitmap2));
            // top_right.setBackgroundResource(bitmap);

            //Drawable drawable = getResources().getDrawable(R.drawable.your_drawable); // 获取Drawable实例
            //imageView.setImageDrawable(drawable); // 设置ImageView的图片为Drawable对象
            top_right.setImageDrawable(drawable);
        }
        top_right_layout.setOnClickListener(listener);
    }

}
