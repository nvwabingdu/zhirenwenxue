package cn.dreamfruits.baselib.network.imageloader.glide.transformations;

import java.lang.System;

/**
 * description: Glide图片Bitmap转换的基类.
 * @date 2018/10/12 10:16.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J4\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J0\u0010\u0006\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH$J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J(\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0004\u00a8\u0006\u0017"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/BitmapTransformation;", "Lcom/bumptech/glide/load/Transformation;", "Landroid/graphics/Bitmap;", "()V", "key", "", "transform", "Lcom/bumptech/glide/load/engine/Resource;", "context", "Landroid/content/Context;", "resource", "outWidth", "", "outHeight", "pool", "Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "toTransform", "updateDiskCacheKey", "", "messageDigest", "Ljava/security/MessageDigest;", "zoomBitmap", "bitmap", "baselib_debug"})
public abstract class BitmapTransformation implements com.bumptech.glide.load.Transformation<android.graphics.Bitmap> {
    
    public BitmapTransformation() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract java.lang.String key();
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> transform(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> resource, int outWidth, int outHeight) {
        return null;
    }
    
    /**
     * 获取一个缩放到给定宽高比的图片
     */
    @org.jetbrains.annotations.NotNull
    protected final android.graphics.Bitmap zoomBitmap(@org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool pool, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap bitmap, int outWidth, int outHeight) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    protected abstract android.graphics.Bitmap transform(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool pool, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap toTransform, int outWidth, int outHeight);
    
    @java.lang.Override
    public void updateDiskCacheKey(@org.jetbrains.annotations.NotNull
    java.security.MessageDigest messageDigest) {
    }
}