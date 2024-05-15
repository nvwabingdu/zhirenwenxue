package cn.dreamfruits.baselib.di;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00132\u00020\u0001:\u0002\u0012\u0013B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcn/dreamfruits/baselib/di/GlobalConfigModule;", "", "builder", "Lcn/dreamfruits/baselib/di/GlobalConfigModule$Builder;", "(Lcn/dreamfruits/baselib/di/GlobalConfigModule$Builder;)V", "globalConfigModule", "Lorg/koin/core/module/Module;", "getGlobalConfigModule", "()Lorg/koin/core/module/Module;", "mApiUrl", "Lokhttp3/HttpUrl;", "mCacheFile", "Ljava/io/File;", "mHandler", "Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "mInterceptors", "", "Lokhttp3/Interceptor;", "Builder", "Companion", "baselib_debug"})
public final class GlobalConfigModule {
    private okhttp3.HttpUrl mApiUrl;
    private cn.dreamfruits.baselib.network.GlobalHttpHandler mHandler;
    private java.util.List<okhttp3.Interceptor> mInterceptors;
    private java.io.File mCacheFile;
    @org.jetbrains.annotations.NotNull
    private final org.koin.core.module.Module globalConfigModule = null;
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.di.GlobalConfigModule.Companion Companion = null;
    
    private GlobalConfigModule(cn.dreamfruits.baselib.di.GlobalConfigModule.Builder builder) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.koin.core.module.Module getGlobalConfigModule() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u0017J\u000e\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020 J\u0006\u0010!\u001a\u00020\"J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010#\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\"\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\u00a8\u0006$"}, d2 = {"Lcn/dreamfruits/baselib/di/GlobalConfigModule$Builder;", "", "()V", "apiUrl", "Lokhttp3/HttpUrl;", "getApiUrl", "()Lokhttp3/HttpUrl;", "setApiUrl", "(Lokhttp3/HttpUrl;)V", "cacheFile", "Ljava/io/File;", "getCacheFile", "()Ljava/io/File;", "setCacheFile", "(Ljava/io/File;)V", "handler", "Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "getHandler", "()Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "setHandler", "(Lcn/dreamfruits/baselib/network/GlobalHttpHandler;)V", "interceptors", "", "Lokhttp3/Interceptor;", "getInterceptors", "()Ljava/util/List;", "setInterceptors", "(Ljava/util/List;)V", "addInterceptor", "interceptor", "baseurl", "baseUrl", "", "build", "Lcn/dreamfruits/baselib/di/GlobalConfigModule;", "globalHttpHandler", "baselib_debug"})
    public static final class Builder {
        @org.jetbrains.annotations.Nullable
        private okhttp3.HttpUrl apiUrl;
        @org.jetbrains.annotations.Nullable
        private cn.dreamfruits.baselib.network.GlobalHttpHandler handler;
        @org.jetbrains.annotations.Nullable
        private java.util.List<okhttp3.Interceptor> interceptors;
        @org.jetbrains.annotations.Nullable
        private java.io.File cacheFile;
        
        public Builder() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final okhttp3.HttpUrl getApiUrl() {
            return null;
        }
        
        public final void setApiUrl(@org.jetbrains.annotations.Nullable
        okhttp3.HttpUrl p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final cn.dreamfruits.baselib.network.GlobalHttpHandler getHandler() {
            return null;
        }
        
        public final void setHandler(@org.jetbrains.annotations.Nullable
        cn.dreamfruits.baselib.network.GlobalHttpHandler p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<okhttp3.Interceptor> getInterceptors() {
            return null;
        }
        
        public final void setInterceptors(@org.jetbrains.annotations.Nullable
        java.util.List<okhttp3.Interceptor> p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.io.File getCacheFile() {
            return null;
        }
        
        public final void setCacheFile(@org.jetbrains.annotations.Nullable
        java.io.File p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule.Builder baseurl(@org.jetbrains.annotations.NotNull
        java.lang.String baseUrl) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule.Builder globalHttpHandler(@org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.GlobalHttpHandler handler) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule.Builder addInterceptor(@org.jetbrains.annotations.NotNull
        okhttp3.Interceptor interceptor) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule.Builder cacheFile(@org.jetbrains.annotations.NotNull
        java.io.File cacheFile) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule build() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcn/dreamfruits/baselib/di/GlobalConfigModule$Companion;", "", "()V", "builder", "Lcn/dreamfruits/baselib/di/GlobalConfigModule$Builder;", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.di.GlobalConfigModule.Builder builder() {
            return null;
        }
    }
}