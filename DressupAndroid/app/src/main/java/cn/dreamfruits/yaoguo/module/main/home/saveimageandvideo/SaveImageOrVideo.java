package cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;

import java.io.File;

/**
 * @Author qiwangi
 * @Date 2023/5/5
 * @TIME 17:05
 */
public class SaveImageOrVideo {
    static boolean flag = true;
    private static String mUrl = "";
    private static int mType = 0;

    /**
     * 保存图片
     *
     * @param mActivity
     */
    public static void saveImg(String url, Activity mActivity) {
        mUrl = url;
        mType = 1;
        int permissionState = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE);
        int permissionRead = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionRead == PackageManager.PERMISSION_GRANTED && permissionWrite == PackageManager.PERMISSION_GRANTED && permissionState == PackageManager.PERMISSION_GRANTED) {
            // 已经获得了读取外部存储权限
            //判断是否取得权限了
            Toast.makeText(mActivity, "图片开始下载……", Toast.LENGTH_SHORT).show();
            //保存图片
            downLoadImage(url, mActivity);
        } else {
            // 没有读取外部存储权限
            loadPermission(mActivity);
        }
    }


    /**
     * 保存视频
     *
     * @param url
     * @param mActivity "https://www.w3school.com.cn/example/html5/mov_bbb.mp4"
     */
    public static void saveMp4(String url, Activity mActivity) {
        mUrl = url;
        mType = 2;

        int permissionState = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE);
        int permissionRead = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionRead == PackageManager.PERMISSION_GRANTED && permissionWrite == PackageManager.PERMISSION_GRANTED && permissionState == PackageManager.PERMISSION_GRANTED) {
            // 已经获得了读取外部存储权限
            Toast.makeText(mActivity, "视频开始下载……", Toast.LENGTH_SHORT).show();
            downLoadVideo(url, mActivity);
        } else {
            // 没有读取外部存储权限
            loadPermission(mActivity);
        }
    }

    /**
     * 动态请求权限
     */
    private static void loadPermission(Activity mActivity) {
        PermissionsUtil.requestCamera((FragmentActivity) mActivity, aBoolean -> {
                    Log.e("TAG212121", "权限申请之后: " + aBoolean);
                    if (aBoolean) {//请求权限成功
                        switch (mType) {
                            case 1:
                                downLoadImage(mUrl, mActivity);
                                break;
                            case 2:
                                downLoadVideo(mUrl, mActivity);
                                break;
                        }
                    } else {
                        Toast.makeText(mActivity, "开启权限之后才能下载和保存哦……", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * 保存图片到相册
     *
     * @param path
     */
    private static void downLoadImage(String path, Activity mActivity) {
        new Thread(() -> new AndroidDownloadManager(mActivity, path)
                .setListener(new AndroidDownloadManagerListener() {
                    @Override
                    public void onPrepare() {
                        Log.d("downloadVideo", "onPrepare");
                    }

                    @Override
                    public void onSuccess(String path) {
//                        String tempFile = SaveFileUtils.getSDcardDCIMFile(".png");
//                        SaveFileUtils.copyFile(new File(path).getAbsolutePath(), tempFile);
//                        SaveFileUtils.saveImage(mActivity, new File(path)); //保存图片逻辑

                        ImageUtils.save2Album(ImageUtils.getBitmap(path), Bitmap.CompressFormat.JPEG);
                        FileUtils.delete(path);
                        Toast.makeText(mActivity, "已保存到系统图库", Toast.LENGTH_SHORT).show();
                        flag = true;
                        Log.d("downloadVideo", "onSuccess >>>>" + path);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Toast.makeText(mActivity, "图片下载失败，请重新下载！", Toast.LENGTH_SHORT).show();
                        Log.e("downloadVideo", "onFailed", throwable);
                        flag = true;
                    }
                }).download()).start();
    }

    /**
     * 保存视频到相册
     *
     * @param path
     */
    private static void downLoadVideo(String path, Activity mActivity) {
        new Thread(() -> new AndroidDownloadManager(mActivity, path)
                .setListener(new AndroidDownloadManagerListener() {
                    @Override
                    public void onPrepare() {
                        Log.d("downloadVideo", "onPrepare");
                    }

                    @Override
                    public void onSuccess(String path) {
                        Toast.makeText(mActivity, "视频已保存到相册", Toast.LENGTH_SHORT).show();
                        SaveFileUtils.saveVideo(mActivity, new File(path));
                        flag = true;
                        Log.d("downloadVideo", "onSuccess >>>>" + path);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Toast.makeText(mActivity, "视频下载失败，请重新下载！", Toast.LENGTH_SHORT).show();
                        Log.e("downloadVideo", "onFailed", throwable);
                        flag = true;
                    }
                }).download()).start();
    }

}
