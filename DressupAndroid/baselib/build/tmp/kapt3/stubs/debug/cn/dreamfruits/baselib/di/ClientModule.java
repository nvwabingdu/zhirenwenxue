package cn.dreamfruits.baselib.di;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcn/dreamfruits/baselib/di/ClientModule;", "", "()V", "Companion", "baselib_debug"})
public final class ClientModule {
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.di.ClientModule.Companion Companion = null;
    private static boolean httpDebug = true;
    private static final long TIME_OUT = 30L;
    @org.jetbrains.annotations.NotNull
    private static final org.koin.core.module.Module clientModule = null;
    
    public ClientModule() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J2\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00142\u0006\u0010\u0016\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u0012H\u0002J \u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0011\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0010H\u0002J\b\u0010 \u001a\u00020\u001cH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcn/dreamfruits/baselib/di/ClientModule$Companion;", "", "()V", "TIME_OUT", "", "clientModule", "Lorg/koin/core/module/Module;", "getClientModule", "()Lorg/koin/core/module/Module;", "httpDebug", "", "getHttpDebug", "()Z", "setHttpDebug", "(Z)V", "provideClient", "Lokhttp3/OkHttpClient;", "builder", "Lokhttp3/OkHttpClient$Builder;", "interceptors", "", "Lokhttp3/Interceptor;", "interceptor", "handler", "Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "provideClientBuilder", "provideRetrofit", "Lretrofit2/Retrofit;", "Lretrofit2/Retrofit$Builder;", "httpUrl", "Lokhttp3/HttpUrl;", "client", "provideRetrofitBuilder", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean getHttpDebug() {
            return false;
        }
        
        public final void setHttpDebug(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final org.koin.core.module.Module getClientModule() {
            return null;
        }
        
        private final okhttp3.OkHttpClient.Builder provideClientBuilder() {
            return null;
        }
        
        private final retrofit2.Retrofit.Builder provideRetrofitBuilder() {
            return null;
        }
        
        private final okhttp3.OkHttpClient provideClient(okhttp3.OkHttpClient.Builder builder, java.util.List<okhttp3.Interceptor> interceptors, okhttp3.Interceptor interceptor, cn.dreamfruits.baselib.network.GlobalHttpHandler handler) {
            return null;
        }
        
        private final retrofit2.Retrofit provideRetrofit(retrofit2.Retrofit.Builder builder, okhttp3.HttpUrl httpUrl, okhttp3.OkHttpClient client) {
            return null;
        }
    }
}