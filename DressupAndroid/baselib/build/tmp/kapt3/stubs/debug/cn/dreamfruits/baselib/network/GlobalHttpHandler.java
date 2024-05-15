package cn.dreamfruits.baselib.network;

import java.lang.System;

/**
 * 处理 Http 请求和响应结果的处理类
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u0000 \f2\u00020\u0001:\u0001\fJ\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H&J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\bH&\u00a8\u0006\r"}, d2 = {"Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "", "onHttpRequestBefore", "Lokhttp3/Request;", "chain", "Lokhttp3/Interceptor$Chain;", "request", "onHttpResultResponse", "Lokhttp3/Response;", "httpResult", "", "response", "Companion", "baselib_debug"})
public abstract interface GlobalHttpHandler {
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.GlobalHttpHandler.Companion Companion = null;
    
    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link okhttp3.Interceptor.Chain}
     * @param response   {@link Response}
     * @return {@link Response}
     */
    @org.jetbrains.annotations.NotNull
    public abstract okhttp3.Response onHttpResultResponse(@org.jetbrains.annotations.NotNull
    java.lang.String httpResult, @org.jetbrains.annotations.NotNull
    okhttp3.Interceptor.Chain chain, @org.jetbrains.annotations.NotNull
    okhttp3.Response response);
    
    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link okhttp3.Interceptor.Chain}
     * @param request {@link Request}
     * @return {@link Request}
     */
    @org.jetbrains.annotations.NotNull
    public abstract okhttp3.Request onHttpRequestBefore(@org.jetbrains.annotations.NotNull
    okhttp3.Interceptor.Chain chain, @org.jetbrains.annotations.NotNull
    okhttp3.Request request);
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcn/dreamfruits/baselib/network/GlobalHttpHandler$Companion;", "", "()V", "EMPTY", "Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "getEMPTY", "()Lcn/dreamfruits/baselib/network/GlobalHttpHandler;", "baselib_debug"})
    public static final class Companion {
        
        /**
         * 空实现
         */
        @org.jetbrains.annotations.NotNull
        private static final cn.dreamfruits.baselib.network.GlobalHttpHandler EMPTY = null;
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.GlobalHttpHandler getEMPTY() {
            return null;
        }
    }
}