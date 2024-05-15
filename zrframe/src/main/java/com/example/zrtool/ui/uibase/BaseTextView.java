package com.example.zrtool.ui.uibase;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 14:51
 */
public class BaseTextView extends AppCompatTextView {

    public BaseTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 在这里放置 BaseTextView 的初始化代码
        /*setupFont(getContext());*/
//        setupFont(getContext(),2);
    }

    /**
     * 上述代码中，我们继承了AppCompatTextView类，并在构造函数中调用了setupFont方法来设置自定义字体。您需要将"your_font_name.ttf"替换为您想要使用的字体文件名，将其放置在assets文件夹中。
     * @param context
     */
    public void setupFont(Context context,int type) {
        switch (type){
            case 1:
                setupFont(context,"fonts/华文彩云字体.TTF");
                break;
            case 2:
                setupFont(context,"fonts/方正少儿字体.TTF");
                break;
            case 3:
                setupFont(context,"fonts/your_font_name.ttf");
                break;
            case 4:
                setupFont(context,"fonts/your_font_name.ttf");
                break;
            default:
                break;
        }
    }

    private void setupFont(Context context,String path_) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path_);
        setTypeface(typeface);
    }

}

