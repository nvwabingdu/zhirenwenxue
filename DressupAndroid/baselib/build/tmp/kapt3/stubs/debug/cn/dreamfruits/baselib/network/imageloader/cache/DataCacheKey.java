package cn.dreamfruits.baselib.network.imageloader.cache;

import java.lang.System;

/**
 * description: Glide缓存的key.
 * @date 2019/5/25 11:41.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016R\u000e\u0010\u0003\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/cache/DataCacheKey;", "Lcom/bumptech/glide/load/Key;", "sourceKey", "signature", "(Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/Key;)V", "getSourceKey", "()Lcom/bumptech/glide/load/Key;", "equals", "", "other", "", "hashCode", "", "toString", "", "updateDiskCacheKey", "", "messageDigest", "Ljava/security/MessageDigest;", "baselib_debug"})
public final class DataCacheKey implements com.bumptech.glide.load.Key {
    @org.jetbrains.annotations.NotNull
    private final com.bumptech.glide.load.Key sourceKey = null;
    private final com.bumptech.glide.load.Key signature = null;
    
    public DataCacheKey(@org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.Key sourceKey, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.Key signature) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.bumptech.glide.load.Key getSourceKey() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override
    public void updateDiskCacheKey(@org.jetbrains.annotations.NotNull
    java.security.MessageDigest messageDigest) {
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
}