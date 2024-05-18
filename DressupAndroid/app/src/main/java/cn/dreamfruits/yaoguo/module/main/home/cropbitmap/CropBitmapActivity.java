package cn.dreamfruits.yaoguo.module.main.home.cropbitmap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.player.UnityPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.constants.MMKVConstants;

/**
 * @Author qiwangi
 * @Date 2023/6/14
 * @TIME 14:20
 */
public class CropBitmapActivity extends AppCompatActivity implements View.OnClickListener{

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
            Log.e("zqr", "裁剪这边收到的图片地址进行裁剪时出错: " + e.getMessage());
            //这里直接发送到unity
            String path = "{\"path\": \""+originPath+"\"}";
            /**1.发送广播的方式*/
//            Intent intent = new Intent("crop_url_broadcast");
//            intent.putExtra("cropUrl", path);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            /**2.调用unity原生方法 需要在同一个进程*/
            UnityPlayer.UnitySendMessage("AppManager", "ReceiveChangeFacePic", path);//A->U 返回照片路径
            /**3.用共享参数的方式*/
//            SharedPreferences sharedPreferences = getSharedPreferences("unitySp", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("cropUrl", path);
//            editor.apply();
            /**4.用MMKV的方式*/
//            MMKVConstants.Companion.initData(MMKVConstants.UNITY_CROP_URL, path);

            finish();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btClip:
                //裁剪
                TestBean.bitmap=likeView.clip();

                //这里就失去了透明像素
                String str=addTransparentBorderAndSaveToGallery2(TestBean.bitmap,10);//添加透明像素
                if (str.equals("-1")){
                    //返回相册传递过来的路径
                    String temp = "{\"path\": \""+str+"\"}";
                    /**1.发送广播的方式*/
//                    Intent intent = new Intent("crop_url_broadcast");
//                    intent.putExtra("cropUrl", temp);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    /**2.调用unity原生方法 需要在同一个进程*/
                    UnityPlayer.UnitySendMessage("AppManager", "ReceiveChangeFacePic", temp);//A->U 返回照片路径
                    /**3.用共享参数的方式*/
//                    SharedPreferences sharedPreferences = getSharedPreferences("unitySp", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("cropUrl", temp);
//                    editor.apply();
                    /**4.用MMKV的方式*/
//                    MMKVConstants.Companion.initData(MMKVConstants.UNITY_CROP_URL, temp);
                }else {
                    //返回裁剪后的路径
                    String temp = "{\"path\": \""+str+"\"}";
                    /**1.发送广播的方式*/
//                    Intent intent = new Intent("crop_url_broadcast");
//                    intent.putExtra("cropUrl", temp);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    /**2.调用unity原生方法 需要在同一个进程*/
                    UnityPlayer.UnitySendMessage("AppManager", "ReceiveChangeFacePic", temp);//A->U 返回照片路径
                    /**3.用共享参数的方式*/
//                    SharedPreferences sharedPreferences = getSharedPreferences("unitySp", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("cropUrl", temp);
//                    editor.commit();
                    /**4.用MMKV的方式*/
//                    MMKVConstants.Companion.initData(MMKVConstants.UNITY_CROP_URL, temp);
                }
                finish();
                break;
        }
    }

    /**
     * 获取屏幕宽度
     * @param activity
     */
    private int getScreenWidth(Activity activity){
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }catch (Exception e){
            return 1080;
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

//    /**
//     * 将图片地址转为Bitmap对象
//     * @param imagePath
//     * @return
//     */
//    public Bitmap getBitmapFromPath(String imagePath) {
//        File imgFile = new  File(imagePath);
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            return myBitmap;
//        }
//        return null;
//    }
//
//
//    /**
//     * 将图片地址转为图片流
//     * @param imagePath
//     * @return
//     */
//    public byte[] getImageBytesFromPath(String imagePath) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        Bitmap bitmap = getBitmapFromPath(imagePath);
//        if (bitmap != null) {
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            return stream.toByteArray();
//        }
//        return null;
//    }

    /**
     * 给图片添加一圈透明像素 然后返回路径
     * @param imagePath /storage/emulated/0/DCIM/Camera/myImage.jpg
     * @param borderSize 20
     * @return
     *
     * 获取存储在 MediaStore 中的文件的实际路径。如果运行环境是 Android 10 及更高版本，则会执行该条件语句，并使用查询操作从 Uri 中检索实际路径。如果不是，则直接返回 File 对象的绝对路径。
     *
     */
    public String addTransparentBorderAndSaveToGallery(String imagePath, int borderSize) {
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"imagePath=="+imagePath);
        // 从文件加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // 计算新的图片尺寸
        int width = bitmap.getWidth() + borderSize * 2;
        int height = bitmap.getHeight() + borderSize * 2;
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"width=="+width);
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"height=="+height);
        // 创建新的Bitmap对象
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 创建画布
        Canvas canvas = new Canvas(newBitmap);

        // 设置画布颜色为透明
        canvas.drawColor(Color.TRANSPARENT);

        // 将原始图像绘制到新的Bitmap上
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);

        // 将带有透明边框的图片保存到相册，并返回保存到相册的带有透明边框的PNG图片路径
        try {
            OutputStream stream = null;
            String fileName = "bordered_image_" + System.currentTimeMillis() + ".png";
            Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"fileName=="+fileName);
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
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
            Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"加入像素之后的地址：  出错返回11:"+imagePath);
            return imagePath;
        }
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"加入像素之后的地址：  出错返回22:"+imagePath);
        return imagePath;
    }


    /**
     * 通过传递bitmap过来实现添加像素
     * @param borderSize
     * @return
     */
    public String addTransparentBorderAndSaveToGallery2(Bitmap tempBitmap, int borderSize) {
//        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"imagePath=="+imagePath);
        // 从文件加载图片
        Bitmap bitmap = tempBitmap;

        // 计算新的图片尺寸
        int width = bitmap.getWidth() + borderSize * 2;
        int height = bitmap.getHeight() + borderSize * 2;
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"width=="+width);
        Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"height=="+height);
        // 创建新的Bitmap对象
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 创建画布
        Canvas canvas = new Canvas(newBitmap);

        // 设置画布颜色为透明
        canvas.drawColor(Color.TRANSPARENT);

        // 将原始图像绘制到新的Bitmap上
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);

        // 将带有透明边框的图片保存到相册，并返回保存到相册的带有透明边框的PNG图片路径
        try {
            OutputStream stream = null;
            String fileName = "bordered_image_" + System.currentTimeMillis() + ".png";
            Log.e("zqr","addTransparentBorderAndSaveToGallery()"+"fileName=="+fileName);
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
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
