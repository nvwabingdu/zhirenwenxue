package cn.dreamfruits.baselib.network.imageloader.glide;

import java.lang.System;

/**
 * description: Glide配置的OkHttpClick.
 *
 * @date 2019/5/25 11:47.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00072\b\u0010\u0012\u001a\u0004\u0018\u00010\tJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0011\u001a\u00020\u0007J\u000e\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000RN\u0010\u0005\u001aB\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u0007\u0012\f\u0012\n \b*\u0004\u0018\u00010\t0\t \b* \u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u0007\u0012\f\u0012\n \b*\u0004\u0018\u00010\t0\t\u0018\u00010\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0015"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/GlideHttpClientManager;", "", "()V", "LISTENER", "Lcn/dreamfruits/baselib/network/imageloader/glide/ProgressResponseBody$InternalProgressListener;", "listenersMap", "", "", "kotlin.jvm.PlatformType", "Lcn/dreamfruits/baselib/network/imageloader/glide/OnImageProgressListener;", "", "okHttpClient", "Lokhttp3/OkHttpClient;", "getOkHttpClient", "()Lokhttp3/OkHttpClient;", "addListener", "", "url", "listener", "getProgressListener", "removeListener", "baselib_debug"})
public final class GlideHttpClientManager {
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.imageloader.glide.GlideHttpClientManager INSTANCE = null;
    private static final java.util.Map<java.lang.String, cn.dreamfruits.baselib.network.imageloader.glide.OnImageProgressListener> listenersMap = null;
    private static final cn.dreamfruits.baselib.network.imageloader.glide.ProgressResponseBody.InternalProgressListener LISTENER = null;
    
    private GlideHttpClientManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final okhttp3.OkHttpClient getOkHttpClient() {
        return null;
    }
    
    public final void addListener(@org.jetbrains.annotations.NotNull
    java.lang.String url, @org.jetbrains.annotations.Nullable
    cn.dreamfruits.baselib.network.imageloader.glide.OnImageProgressListener listener) {
    }
    
    public final void removeListener(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final cn.dreamfruits.baselib.network.imageloader.glide.OnImageProgressListener getProgressListener(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
        return null;
    }
}