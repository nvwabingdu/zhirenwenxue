/*
 * Copyright (C) 2018 Jenly Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.zrqrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.example.zrqrcode.test.CaptureFragmentActivity;
import com.example.zrqrcode.test.CodeActivity;
import com.example.zrqrcode.test.CustomCaptureActivity;
import com.example.zrqrcode.test.CustomFullScanActivity;
import com.example.zrqrcode.test.EasyCaptureActivity;
import com.example.zrqrcode.test.QRCodeActivity;
import com.example.zrqrcode.zxing.CameraScan;
import com.example.zrqrcode.zxing.CaptureActivity;
import com.example.zrqrcode.zxing.util.CodeUtils;
import com.example.zrqrcode.zxing.util.LogUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 扫码Demo示例说明
 *
 * 快速实现扫码识别有以下几种方式：
 *
 * 1、直接使用CaptureActivity或者CaptureFragment。(默认的扫码实现)
 *
 * 2、通过继承CaptureActivity或者CaptureFragment并自定义布局。（适用于大多场景，并无需关心扫码相关逻辑，自定义布局时需覆写getLayoutId方法）
 *
 * 3、在你项目的Activity或者Fragment中实例化一个CameraScan即可。（适用于想在扫码界面写交互逻辑，又因为项目架构或其它原因，无法直接或间接继承CaptureActivity或CaptureFragment时使用）
 *
 * 4、继承CameraScan自己实现一个，可参照默认实现类DefaultCameraScan，其它步骤同方式3。（扩展高级用法，谨慎使用）
 *
 */
public class ZrQrCodeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_QR_CODE = "key_code";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    public static final int REQUEST_CODE_SCAN = 0X01;
    public static final int REQUEST_CODE_PHOTO = 0X02;

    public static final int RC_CAMERA = 0X01;

    public static final int RC_READ_PHOTO = 0X02;

    private Class<?> cls;
    private String title;
    private boolean isContinuousScan;

    private Toast toast;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zr_qr_code_main);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case REQUEST_CODE_SCAN:
                    String result = CameraScan.parseScanResult(data);
                    showToast(result);
                    break;
                case REQUEST_CODE_PHOTO:
                    parsePhoto(data);
                    break;
            }

        }
    }

    private void showToast(String text){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
    }

    private void parsePhoto(Intent data){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            //异步解析
            asyncThread(() -> {
                final String result = CodeUtils.parseCode(bitmap);
                runOnUiThread(() -> {
                    LogUtils.d("result:" + result);
                    Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                });

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Context getContext(){
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions(){
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限
            startScan(cls,title);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }

    private void asyncThread(Runnable runnable){
        executor.execute(runnable);
    }

    /**
     * 扫码
     * @param cls
     * @param title
     */
    private void startScan(Class<?> cls,String title){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.in,R.anim.out);
        Intent intent = new Intent(this, cls);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_IS_CONTINUOUS,isContinuousScan);
        ActivityCompat.startActivityForResult(this,intent,REQUEST_CODE_SCAN,optionsCompat.toBundle());
    }

    /**
     * 生成二维码/条形码
     * @param isQRCode
     */
    private void startCode(boolean isQRCode){
        Intent intent = new Intent(this, CodeActivity.class);
        intent.putExtra(KEY_IS_QR_CODE,isQRCode);
        intent.putExtra(KEY_TITLE,isQRCode ? getString(R.string.qr_code) : getString(R.string.bar_code));
        startActivity(intent);
    }

    private void startPhotoCode(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_PHOTO);
    }

    @AfterPermissionGranted(RC_READ_PHOTO)
    private void checkExternalStoragePermissions(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限
            startPhotoCode();
        }else{
            EasyPermissions.requestPermissions(this, getString(R.string.permission_external_storage),
                    RC_READ_PHOTO, perms);
        }
    }

    public void onClick(View v){
        isContinuousScan = false;
        int id = v.getId();
        if (id == R.id.btn0) {
            this.cls = CaptureActivity.class;
            this.title = ((Button) v).getText().toString();
            checkCameraPermissions();
        } else if (id == R.id.btn1) {
            this.cls = CustomCaptureActivity.class;
            this.title = ((Button) v).getText().toString();
            isContinuousScan = true;
            checkCameraPermissions();
        } else if (id == R.id.btn2) {
            this.cls = CaptureFragmentActivity.class;
            this.title = ((Button) v).getText().toString();
            checkCameraPermissions();
        } else if (id == R.id.btn3) {
            this.cls = EasyCaptureActivity.class;
            this.title = ((Button) v).getText().toString();
            checkCameraPermissions();
        } else if (id == R.id.btn4) {
            this.cls = CustomFullScanActivity.class;
            this.title = ((Button) v).getText().toString();
            checkCameraPermissions();
        } else if (id == R.id.btn5) {
            startCode(false);
        } else if (id == R.id.btn6) {
            startCode(true);
        } else if (id == R.id.btn7) {
            checkExternalStoragePermissions();
        } else if (id == R.id.btn8) {
            this.cls = QRCodeActivity.class;
            this.title = ((Button) v).getText().toString();
            checkCameraPermissions();
        }

    }
}
