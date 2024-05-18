package com.example.zrsinglephoto.easyphotos.utils.bitmap;

import java.io.File;
import java.io.IOException;

/**
 * 保存图片到本地的回调
 * Created by huan on 2017/12/6.
 */

public interface SaveBitmapCallBack {
    void onSuccess(File file) throws IOException;

    void onIOFailed(IOException exception);

    void onCreateDirFailed();
}
