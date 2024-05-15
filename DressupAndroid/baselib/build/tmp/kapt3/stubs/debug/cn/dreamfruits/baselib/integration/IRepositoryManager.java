package cn.dreamfruits.baselib.integration;

import java.lang.System;

/**
 * 用来管理网络请求层,以及数据缓存层
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J#\u0010\u0002\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u00032\u000e\b\u0001\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H&\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcn/dreamfruits/baselib/integration/IRepositoryManager;", "", "obtainRetrofitService", "T", "service", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "baselib_debug"})
public abstract interface IRepositoryManager {
    
    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service Retrofit service class
     * @param <T>     Retrofit service 类型
     * @return Retrofit service
     */
    public abstract <T extends java.lang.Object>T obtainRetrofitService(@org.jetbrains.annotations.NotNull
    @androidx.annotation.NonNull
    java.lang.Class<T> service);
}