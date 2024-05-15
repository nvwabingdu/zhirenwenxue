package cn.dreamfruits.baselib.network.imageloader.glide.transformations;

import java.lang.System;

/**
 * description: 高斯模糊处理.
 * @date 2018/10/12 10:18.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u001b\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J \u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016J0\u0010\r\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003H\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/BlurTransformation;", "Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/BitmapTransformation;", "radius", "", "sampling", "(II)V", "doBlur", "Landroid/graphics/Bitmap;", "context", "Landroid/content/Context;", "bitmap", "key", "", "transform", "pool", "Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "toTransform", "outWidth", "outHeight", "Companion", "baselib_debug"})
public final class BlurTransformation extends cn.dreamfruits.baselib.network.imageloader.glide.transformations.BitmapTransformation {
    private final int radius = 0;
    private final int sampling = 0;
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.imageloader.glide.transformations.BlurTransformation.Companion Companion = null;
    private static final int MAX_RADIUS = 25;
    private static final int DEFAULT_DOWN_SAMPLING = 1;
    
    @kotlin.jvm.JvmOverloads
    public BlurTransformation() {
        super();
    }
    
    @kotlin.jvm.JvmOverloads
    public BlurTransformation(int radius) {
        super();
    }
    
    @kotlin.jvm.JvmOverloads
    public BlurTransformation(int radius, int sampling) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String key() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected android.graphics.Bitmap transform(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool pool, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {android.renderscript.RSRuntimeException.class})
    private final android.graphics.Bitmap doBlur(android.content.Context context, android.graphics.Bitmap bitmap, int radius) throws android.renderscript.RSRuntimeException {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/BlurTransformation$Companion;", "", "()V", "DEFAULT_DOWN_SAMPLING", "", "MAX_RADIUS", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}