package cn.dreamfruits.yaoguo.module.main.home.cropbitmap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.player.UnityPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.constants.RouterConstants;
import cn.dreamfruits.yaoguo.global.FeedPublishGlobal;
import cn.dreamfruits.yaoguo.global.FeedPublishState;
import cn.dreamfruits.yaoguo.module.main.mine.IconCropActivity;
import cn.dreamfruits.yaoguo.module.publish.service.FeedPublishService;
import cn.dreamfruits.yaoguo.repository.bean.media.PicBean;
import cn.dreamfruits.yaoguo.repository.bean.thirdparty.TencentTmpSecretKeyBean;
import cn.dreamfruits.yaoguo.util.CosUpload;

/**
 * @Author qiwangi
 * @Date 2023/6/14
 * @TIME 14:20
 */
public class CropBitmapActivity2 extends AppCompatActivity implements View.OnClickListener{

    private LikeQQCropView likeView;
    ImageButton btClip;
    String originPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();//设置全屏 wq
        setContentView(R.layout.activity_crop_bitmap);
        initView();
    }
    private void initView() {
        likeView=findViewById(R.id.likeView);

        //返回
        findViewById(R.id.crop_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btClip=findViewById(R.id.btClip);
        btClip.setOnClickListener(this);

        Log.e("zqr", "裁剪这边收到的图片地址: " + getIntent().getStringExtra("path"));
        originPath=getIntent().getStringExtra("path");
        try {
            likeView.setBitmapForWidth(getIntent().getStringExtra("path"),1080);
            likeView.setRadius(0);//设置圆角为0
        }catch (Exception e){
            Toast.makeText(this,"裁剪图片失败",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btClip:
                //裁剪
                TestBean.bitmap=likeView.clip();
                //这里将bitmap保存在本地相册

                String url=saveCropImg(TestBean.bitmap);//转为图片地址
                //上传照片url

                finish();
                break;
        }
    }

    /**
     * 设置沉浸式 wq
     */
    private void setFullScreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        // 设置布局全屏显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 通过传递bitmap过来实现添加像素
     * @return
     */
    public String saveCropImg(Bitmap tempBitmap) {
        try {
            OutputStream stream = null;
            String fileName = "wei_suan_" + System.currentTimeMillis() + ".png";
            String path = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"安卓版本>=10.0");
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                stream = resolver.openOutputStream(uri);
                path = uri.toString();
            } else {
                Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"安卓版本<10.0");
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(directory, fileName);
                stream = new FileOutputStream(file);
                path = file.getAbsolutePath();
            }

            if (stream != null) {
                tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 获取文件的实际路径
                    Cursor cursor = getContentResolver().query(Uri.parse(path),
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
                    if (cursor != null && cursor.moveToFirst()) {
                        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                }

                Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"加入像素之后的地址："+path);
                return path;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"加入像素之后的地址：  出错返回11:");
            return "-1";
        }
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"加入像素之后的地址：  出错返回22:");
        return "-1";
    }



}
