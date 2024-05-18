package cn.dreamfruits.yaoguo.module.main.home.phonebook;

import android.content.Context;
import android.graphics.Bitmap;
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
import cn.dreamfruits.yaoguo.util.Singleton;

/**
 * @Author qiwangi
 * @Date 2023/6/24
 * @TIME 03:00
 */
public class ZrQrCodeView extends FrameLayout {
    private ImageView mUserImg;
    private TextView mUserNameTv;
    private ImageView mQrCodeImg;
    private Context mContext;

    public ZrQrCodeView(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    public ZrQrCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        View view = LayoutInflater.from(context).inflate(R.layout.zr_qr_code_custom_view, this, true);
        mQrCodeImg = view.findViewById(R.id.ivCode);//二维码
        mUserImg = view.findViewById(R.id.qr_code_user_img);//用户头像
        mUserNameTv = view.findViewById(R.id.qr_code_username_tv);//用户名
    }

    public ZrQrCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    public ZrQrCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
    }


    public void setData(String url, String userName, Bitmap qrCodeBitmap) {
        String urlx= Singleton.INSTANCE.getUrlX(url,true,100);
        //头像
        Glide.with(mContext)
                .load(urlx)
                .circleCrop()
                .into(mUserImg);

        //二维码
        mQrCodeImg.setImageBitmap(qrCodeBitmap);

        //姓名
        mUserNameTv.setText(userName);
    }


}
