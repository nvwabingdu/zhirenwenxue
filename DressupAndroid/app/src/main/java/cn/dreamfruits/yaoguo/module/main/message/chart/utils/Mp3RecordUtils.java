package cn.dreamfruits.yaoguo.module.main.message.chart.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;

import cn.dreamfruits.baselib.integration.AppManager;

/**
 * Created by dulee on 2018/9/21.
 * packageName com.english365.learn.tools
 * Desc :
 */

public class Mp3RecordUtils {
    private static volatile Mp3RecordUtils mp3RecordUtils;

    Context mContext;

    private String mDir;

    private int time = 1;

    private OnRecordListener mOnRecordListener;

    private Mp3RecordUtils(Context context) {
        mContext = context;
        init();
    }

    public static Mp3RecordUtils getInstence(Context context) {
        if (mp3RecordUtils == null)
            synchronized (Mp3RecordUtils.class) {
                if (mp3RecordUtils == null)
                    mp3RecordUtils = new Mp3RecordUtils(context);
            }
        return mp3RecordUtils;
    }

    public void setmOnRecordListener(OnRecordListener mOnRecordListener) {
        this.mOnRecordListener = mOnRecordListener;
    }

    private void init() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        mDir = (sdCardExist ? mContext.getExternalFilesDir(null) : mContext.getFilesDir()).getPath()
                + File.separator + "sound" + File.separator;
        File dir = new File(mDir);
        if (!dir.exists()) dir.mkdirs();

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    Log.e(">>", "" + time);
                    if (time == 60) {
                        time = 1;
                        handler.sendEmptyMessageDelayed(2, 200);
                        finishRecording();
                    } else {
                        time++;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 2:
                    handler.removeMessages(1);
                    handler.removeCallbacksAndMessages(null);
                    break;
            }
        }
    };


    private File mFile;
    private Long mTime = 0l;
    private MP3Recorder mMP3Recorder;

    /**
     * 开始录音
     */
    public void startRecording() {
        handler.sendEmptyMessageDelayed(1, 1000);
        mTime = System.currentTimeMillis();
        time = 1;
        mFile = new File(mDir + File.separator + String.valueOf(System.currentTimeMillis()) + ".mp3");
        if (mOnRecordListener != null) {
            if (!mOnRecordListener.onStart(mFile.getPath())) {
                cancelRecording();
                return;
            }
        }
        try {
            mMP3Recorder = new MP3Recorder(mFile);
            mMP3Recorder.start();
            mTime = System.currentTimeMillis();
        } catch (Exception e) {
            if (mOnRecordListener != null) {
                mOnRecordListener.onFailure();
                handler.removeMessages(1);
                handler.removeCallbacksAndMessages(null);
            }
            mMP3Recorder.stop();
            mMP3Recorder = null;

        }
    }

    public MP3Recorder getmMP3Recorder() {
        return mMP3Recorder;
    }

    /**
     * 完成录音
     */
    public void finishRecording() {
//        if (ContextCompat.checkSelfPermission(AppManager.getInstance().getTopActivity(),
//                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        if (ContextCompat.checkSelfPermission(AppManager.getInstance().getTopActivity(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        long time = (System.currentTimeMillis() - mTime);
        if (mMP3Recorder != null) {
            mMP3Recorder.stop();
            mMP3Recorder = null;
            if (mOnRecordListener != null) {
                handler.removeMessages(1);
                handler.removeCallbacksAndMessages(null);
                if (!mOnRecordListener.onFinish(mFile.getPath(), time)) {
                    mFile.delete();
                }
            }
        }

    }


    /**
     * 取消录音
     */
    public void cancelRecording() {
        if (mMP3Recorder != null) {
            mMP3Recorder.stop();
            mMP3Recorder = null;
        }
        if (mFile != null) {
            mFile.delete();
        }
        if (mOnRecordListener != null) {
            mOnRecordListener.onCancel();
            handler.removeMessages(1);
            handler.removeCallbacksAndMessages(null);
        }

    }

    public interface OnRecordListener {

        /**
         * 开始录音
         *
         * @param path
         * @return 是否继续
         */
        boolean onStart(String path);

        /**
         * 完成录音
         *
         * @param path
         * @param time
         * @return 是否保留
         */
        boolean onFinish(String path, long time);

        /**
         * 取消录音
         */
        void onCancel();

        /**
         * 录音失败
         */
        void onFailure();
    }


}
