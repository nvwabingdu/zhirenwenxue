package com.example.zrwenxue.moudel.main.home.anims;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newzr.R;
import com.example.zrwenxue.moudel.BaseActivity;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-21
 * Time: 17:04
 */
public class MyAnimActivity extends BaseActivity {
    @Override
    protected int layoutResId() {
        return R.layout.activity_anim;
    }

    TextView textView;
    TextView textView2;
    ImageView imageView;


    @Override
    protected void init() {
        textView=findViewById(R.id.activity_anim_tv);



//        textView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_input));

        textView2=findViewById(R.id.activity_anim_tv2);
        imageView=findViewById(R.id.img);
        //开始帧动画
//        AnimStatic.animFrame(imageView,getBaseContext());

        //开始补间动画
//        AnimStatic.animTweened(textView,getBaseContext(),"Set");




//        RotateAnimation mRotateAnimation= new RotateAnimation(
//                360,
//                0,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        mRotateAnimation.setFillAfter(true);
//        mRotateAnimation.setInterpolator(new LinearInterpolator());
//        mRotateAnimation.setDuration(3000);
//        mRotateAnimation.setRepeatCount(Integer.MAX_VALUE);
//        imageView.setAnimation(mRotateAnimation);


//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.timg1);
//
//        imageView.setImageBitmap(BitmapUtils.rsBlur(this,bitmap,0,8));




//        mm();




        //通过工具类，获取虚化的bitmap


//        Bitmap bitmap = BitmapUtils.getBlurBackground(MyAnimActivity.this);




//        //将虚化的bitmap 设置为背景
//        CustDialog dialog = new CustDialog(MainActivity.this, R.style.Dialog_Fullscreen, bitmap);
//        dialog.setTitle("blur behind dialog");
//        dialog.show();

    }











    private void mm(){

        //25dp转化为px
        float dp = 25;
        final float scale = getResources().getDisplayMetrics().density;
        //由25dp转化来的px
        int px = (int) (dp * scale + 0.5f);

        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("小明回复小红：你在干嘛呀。");

        /**
         * 设置小明
         * 颜色和点击事件
         */
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0090FF"));
        spannableString.setSpan(colorSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF0090FF"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(MyAnimActivity.this, "点击小明", Toast.LENGTH_SHORT).show();
//                ToastUtil.show(mContext, "点击小明");
            }
        };
        spannableString.setSpan(clickableSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        /**
         * 设置小红
         * 颜色和点击事件
         */
        //设置字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF0090FF"));
        spannableString.setSpan(colorSpan1, 4, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF0090FF"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(MyAnimActivity.this, "点击小红", Toast.LENGTH_SHORT).show();
            }
        };
        spannableString.setSpan(clickableSpan1, 4, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        /**
         * 内容最后添加图片
         * 将文字最后的。替换为了图片
         */
//        Drawable drawable = getResources().getDrawable(R.drawable.face_1);
//        drawable.setBounds(0, 0, px, px);
//        ImageSpan imageSpan1 = new ImageSpan(drawable);
//        spannableString.setSpan(imageSpan1, spannableString.length() - 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        /**
         * 内容点击事件
         */
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF151515"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(MyAnimActivity.this, "点击内容", Toast.LENGTH_SHORT).show();

            }
        };
        spannableString.setSpan(clickableSpan2, 7, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }



}
