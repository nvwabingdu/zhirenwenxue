package com.example.zrqrcode.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import com.example.zrqrcode.R;
import com.example.zrqrcode.test.util.StatusBarUtils;
import com.example.zrqrcode.zxing.CameraScan;
import com.example.zrqrcode.zxing.DecodeConfig;
import com.example.zrqrcode.zxing.DecodeFormatManager;
import com.example.zrqrcode.zxing.DefaultCameraScan;
import com.example.zrqrcode.zxing.ViewfinderView;
import com.example.zrqrcode.zxing.analyze.MultiFormatAnalyzer;
import com.example.zrqrcode.zxing.config.ResolutionCameraConfig;
import com.google.zxing.Result;


/**
 * 自定义扫码：当直接使用CaptureActivity
 * 自定义扫码，切记自定义扫码需在{@link Activity}或者{@link Fragment}相对应的生命周期里面调用{@link #mCameraScan}对应的生命周期
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CustomFullScanActivity extends AppCompatActivity implements CameraScan.OnScanResultCallback {

    private boolean isContinuousScan;

    private CameraScan mCameraScan;

    private PreviewView previewView;

    private ViewfinderView viewfinderView;

    private View ivFlash;

    private Toast toast;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.custom_activity);

        initUI();
    }

    private void initUI(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        StatusBarUtils.immersiveStatusBar(this,toolbar,0.2f);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getIntent().getStringExtra(MainActivity.KEY_TITLE));


        previewView = findViewById(R.id.previewView);
        viewfinderView = findViewById(R.id.viewfinderView);
        ivFlash = findViewById(R.id.ivFlash);
        ivFlash.setVisibility(View.INVISIBLE);

        isContinuousScan = getIntent().getBooleanExtra(MainActivity.KEY_IS_CONTINUOUS,false);

        //初始化解码配置
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
                .setFullAreaScan(true);//设置是否全区域识别，默认false

        mCameraScan = new DefaultCameraScan(this,previewView);
        mCameraScan.setOnScanResultCallback(this)
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig))
                .setVibrate(true)
                .setCameraConfig(new ResolutionCameraConfig(this, ResolutionCameraConfig.IMAGE_QUALITY_720P))
                .startCamera();

    }

    @Override
    protected void onDestroy() {
        mCameraScan.release();
        super.onDestroy();
    }

    /**
     * 扫码结果回调
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        if(isContinuousScan){
            showToast(result.getText());
        }
        //如果需支持连扫，返回true即可
        return isContinuousScan;
    }

    private void showToast(String text){
        if(toast == null){
            toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        }else{
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(text);
        }
        toast.show();
    }



    public void onClick(View v){
        if (v.getId() == R.id.ivLeft) {
            finish();
        }
    }
}