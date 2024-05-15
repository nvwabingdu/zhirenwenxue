package cn.dreamfruits.baselib.integration;

import java.lang.System;

/**
 * 用来管理网络请求层,
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J!\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\nH\u0016\u00a2\u0006\u0002\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2 = {"Lcn/dreamfruits/baselib/integration/RepositoryManager;", "Lcn/dreamfruits/baselib/integration/IRepositoryManager;", "mRetrofit", "Lretrofit2/Retrofit;", "(Lretrofit2/Retrofit;)V", "getMRetrofit", "()Lretrofit2/Retrofit;", "obtainRetrofitService", "T", "serviceClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "baselib_debug"})
public final class RepositoryManager implements cn.dreamfruits.baselib.integration.IRepositoryManager {
    @org.jetbrains.annotations.NotNull
    private final retrofit2.Retrofit mRetrofit = null;
    
    public RepositoryManager(@org.jetbrains.annotations.NotNull
    retrofit2.Retrofit mRetrofit) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final retrofit2.Retrofit getMRetrofit() {
        return null;
    }
    
    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     */
    @kotlin.jvm.Synchronized
    @java.lang.Override
    public synchronized <T extends java.lang.Object>T obtainRetrofitService(@org.jetbrains.annotations.NotNull
    java.lang.Class<T> serviceClass) {
        return null;
    }
}