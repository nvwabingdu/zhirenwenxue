package cn.dreamfruits.yaoguo.module.main.diy.pagebanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean;

/**
 * @Author qiwangi
 * @Date 2023/6/15
 * @TIME 17:45
 */
public class ZrView extends FrameLayout {
    private ImageView mImageView;
    private TextView mTextView;
    private Context mContext;

    public ZrView(@NonNull Context context) {
        super(context);
        mContext=context;
        View view = LayoutInflater.from(context).inflate(R.layout.zr_banner_item, this, true);
        mImageView = view.findViewById(R.id.zr_img);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTextView = view.findViewById(R.id.zr_title);
    }

    public ZrView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;

    }

    public ZrView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    public ZrView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
    }


    public void setData(GetStyleVersionListByTypeBean.Item data) {
        // 加载图片
        Glide.with(mContext)
                .load(data.getCoverUrl())
                .into(mImageView);

        mTextView.setText(data.getName());
    }


}
