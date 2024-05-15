package cn.dreamfruits.baselib.network.imageloader.glide;

import java.lang.System;

/**
 * 解决cdn 拼接url导致重复缓存
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\b\u001a\u00020\u0003H\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\t"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/CdnGlideUrl;", "Lcom/bumptech/glide/load/model/GlideUrl;", "url", "", "(Ljava/lang/String;)V", "getUrl", "()Ljava/lang/String;", "setUrl", "getCacheKey", "baselib_debug"})
public final class CdnGlideUrl extends com.bumptech.glide.load.model.GlideUrl {
    @org.jetbrains.annotations.NotNull
    private java.lang.String url;
    
    public CdnGlideUrl(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUrl() {
        return null;
    }
    
    public final void setUrl(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String getCacheKey() {
        return null;
    }
}