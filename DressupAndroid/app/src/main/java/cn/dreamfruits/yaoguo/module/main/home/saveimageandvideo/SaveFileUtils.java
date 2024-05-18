package cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @anthor:njb
 * @date: 2020-04-21 04:36
 * @desc: 文件保存工具类
 **/
public class SaveFileUtils {
    /**
     * 保存图片
     * @param context
     * @param file
     */
    public static void saveImage(Context context, File file) {


        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);

        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        final Uri localUri = Uri.fromFile(file);
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);
    }

    public static ContentValues getImageContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "image/jpeg");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("orientation", Integer.valueOf(0));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }


    /**
     * 保存视频
     * @param context
     * @param file
     */
    public static void saveVideo(Context context, File file) {
        //是否添加到相册
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.Q) {
            // 拷贝到指定uri,如果没有这步操作，android11不会在相册显示
            try {
                OutputStream out = context.getContentResolver().openOutputStream(localUri);
                copyFile(String.valueOf(file), out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
    }


    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    /**
     * 拷贝文件
     * @param oldPath
     * @param out
     * @return
     */
    public static boolean copyFile(String oldPath, OutputStream out) {
        Log.d("TAG", "oldPath = " + oldPath);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                // 读入原文件
                InputStream inStream = new FileInputStream(oldPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    out.write(buffer, 0, byteread);
                }
                inStream.close();
                out.close();
                return true;
            }
            else {
                Log.w("TAG", String.format("文件(%s)不存在。", oldPath));
            }
        }
        catch (Exception e) {
            Log.e("TAG", "复制单个文件操作出错");
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 拷贝文件
     * @param sourcePath 原始文件路径
     * @param targetPath 目标存放文件路径
     */
    public static boolean copyFile(String sourcePath, String targetPath) {

        //文件非空判断
        if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(targetPath)) {
            return false;
        }

        File source = new File(sourcePath);
        File target = new File(targetPath);

        //源文件，和目标文件是同一个文件，并且目标文件存在，直接返回
        if (sourcePath.equals(targetPath) && target.exists()) {
            return false;
        }

        if (!target.exists()) {
            String path = targetPath.substring(0, targetPath.lastIndexOf("/"));
            File s = new File(path);
            s.mkdirs();
        }


        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf = new byte[8192];
            int i;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            LogWrite.writeMsg(e);
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String getSDcardDCIMFile(String suffix){
        Log.e("adsdadada",Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + System.currentTimeMillis() +suffix);
//        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + getTimeName() +suffix;
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + System.currentTimeMillis() +suffix;
//        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" +suffix;
    }

}
