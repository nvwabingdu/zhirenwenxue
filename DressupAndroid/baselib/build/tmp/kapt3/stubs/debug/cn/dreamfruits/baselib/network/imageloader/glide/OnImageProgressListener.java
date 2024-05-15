package cn.dreamfruits.baselib.network.imageloader.glide;

import java.lang.System;

/**
 * description: Glide加载图片的进度回调.
 *
 * @date 2019/5/25 11:47.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH&\u00a8\u0006\r"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/OnImageProgressListener;", "", "onProgress", "", "url", "", "isComplete", "", "percentage", "", "bytesRead", "", "totalBytes", "baselib_debug"})
public abstract interface OnImageProgressListener {
    
    public abstract void onProgress(@org.jetbrains.annotations.NotNull
    java.lang.String url, boolean isComplete, int percentage, long bytesRead, long totalBytes);
}