package cn.dreamfruits.baselib.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcn/dreamfruits/baselib/network/RequestIntercept;", "Lokhttp3/Interceptor;", "mHandler", "Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "(Lcn/dreamfruits/baselib/network/GlobalHttpHandler;)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "Companion", "baselib_debug"})
public final class RequestIntercept implements okhttp3.Interceptor {
    private final cn.dreamfruits.baselib.network.GlobalHttpHandler mHandler = null;
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.RequestIntercept.Companion Companion = null;
    
    public RequestIntercept(@org.jetbrains.annotations.Nullable
    cn.dreamfruits.baselib.network.GlobalHttpHandler mHandler) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public okhttp3.Response intercept(@org.jetbrains.annotations.NotNull
    okhttp3.Interceptor.Chain chain) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcn/dreamfruits/baselib/network/RequestIntercept$Companion;", "", "()V", "convertCharset", "", "charset", "Ljava/nio/charset/Charset;", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String convertCharset(@org.jetbrains.annotations.NotNull
        java.nio.charset.Charset charset) {
            return null;
        }
    }
}