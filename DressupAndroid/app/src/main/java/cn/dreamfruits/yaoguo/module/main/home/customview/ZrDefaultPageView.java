package cn.dreamfruits.yaoguo.module.main.home.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.dreamfruits.yaoguo.R;

/**
 * @Author qiwangi
 * @Date 2023/6/29
 * @TIME 10:52
 */
public class ZrDefaultPageView extends FrameLayout {
    private View mll;
    private View mllImgTv;
    private ImageView mImageView;
    private TextView mTextView;

    private Context mContext;

    public ZrDefaultPageView(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    public ZrDefaultPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        View view = LayoutInflater.from(context).inflate(R.layout.common_default_pages, this, true);
        mll = view.findViewById(R.id.ll);
        mllImgTv = view.findViewById(R.id.ll_img_tv);


        mImageView = view.findViewById(R.id.img);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTextView = view.findViewById(R.id.tv);
    }

    public ZrDefaultPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    public ZrDefaultPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
    }

    //设置数据
    public void setData(String text, int imgRes) {
        mImageView.setImageDrawable(mContext.getResources().getDrawable(imgRes));
        mTextView.setText(text);
    }

    //当前item
    public void setOnThisItemClickListener(OnClickListener listener) {
        mllImgTv.setOnClickListener(listener);
    }

    //图片点击监听
    public void setOnImageClickListener(OnClickListener listener) {
        mImageView.setOnClickListener(listener);
    }

    //文字点击监听
    public void setOnTextClickListener(OnClickListener listener) {
        mTextView.setOnClickListener(listener);
    }
}
