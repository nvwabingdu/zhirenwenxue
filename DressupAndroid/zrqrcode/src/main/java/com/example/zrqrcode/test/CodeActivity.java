package com.example.zrqrcode.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zrqrcode.R;
import com.example.zrqrcode.zxing.util.CodeUtils;
import com.google.zxing.BarcodeFormat;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CodeActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_activity);
        ivCode = findViewById(R.id.ivCode);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getIntent().getStringExtra(MainActivity.KEY_TITLE));
        boolean isQRCode = getIntent().getBooleanExtra(MainActivity.KEY_IS_QR_CODE,false);

        if(isQRCode){
            createQRCode(getString(R.string.app_name));
        }else{
            createBarCode("1234567890");
        }
    }

    /**
     * 生成二维码
     * @param content
     */
    private void createQRCode(String content){
        new Thread(() -> {
            //生成二维码相关放在子线程里面
            Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            Bitmap bitmap =  CodeUtils.createQRCode(content,600,logo);
            runOnUiThread(()->{
                //显示二维码
                ivCode.setImageBitmap(bitmap);
            });
        }).start();

    }

    /**
     * 生成条形码
     * @param content
     */
    private void createBarCode(String content){
        new Thread(() -> {
            //生成条形码相关放在子线程里面
            Bitmap bitmap = CodeUtils.createBarCode(content, BarcodeFormat.CODE_128,800,200,null,true);
            runOnUiThread(()->{
                //显示条形码
                ivCode.setImageBitmap(bitmap);
            });
        }).start();
    }



    public void onClick(View v){
        if (v.getId() == R.id.ivLeft) {
            finish();
        }
    }
}
