package cn.dreamfruits.baselib.network.imageloader.glide;

import java.lang.System;

/**
 * description: Glide加载图片的进度回调.
 *
 * @date 2019/5/25 11:47.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00102\u00020\u0001:\u0002\u0010\u0011B!\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0016J\n\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u000e\u001a\u00020\tH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody;", "Lokhttp3/ResponseBody;", "url", "", "internalProgressListener", "Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody$InternalProgressListener;", "responseBody", "(Ljava/lang/String;Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody$InternalProgressListener;Lokhttp3/ResponseBody;)V", "bufferedSource", "Lokio/BufferedSource;", "contentLength", "", "contentType", "Lokhttp3/MediaType;", "source", "Lokio/Source;", "Companion", "InternalProgressListener", "baselib_debug"})
public final class ProgressResponseBody extends okhttp3.ResponseBody {
    private final java.lang.String url = null;
    private final cn.dreamfruits.baselib.network.imageloader.glide.ProgressResponseBody.InternalProgressListener internalProgressListener = null;
    private final okhttp3.ResponseBody responseBody = null;
    private okio.BufferedSource bufferedSource;
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.imageloader.glide.ProgressResponseBody.Companion Companion = null;
    private static final android.os.Handler mainThreadHandler = null;
    
    public ProgressResponseBody(@org.jetbrains.annotations.NotNull
    java.lang.String url, @org.jetbrains.annotations.Nullable
    cn.dreamfruits.baselib.network.imageloader.glide.ProgressResponseBody.InternalProgressListener internalProgressListener, @org.jetbrains.annotations.NotNull
    okhttp3.ResponseBody responseBody) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public okhttp3.MediaType contentType() {
        return null;
    }
    
    @java.lang.Override
    public long contentLength() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public okio.BufferedSource source() {
        return null;
    }
    
    private final okio.Source source(okio.Source source) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b`\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H&\u00a8\u0006\t"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody$InternalProgressListener;", "", "onProgress", "", "url", "", "bytesRead", "", "totalBytes", "baselib_debug"})
    public static abstract interface InternalProgressListener {
        
        public abstract void onProgress(@org.jetbrains.annotations.NotNull
        java.lang.String url, long bytesRead, long totalBytes);
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody$Companion;", "", "()V", "mainThreadHandler", "Landroid/os/Handler;", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}