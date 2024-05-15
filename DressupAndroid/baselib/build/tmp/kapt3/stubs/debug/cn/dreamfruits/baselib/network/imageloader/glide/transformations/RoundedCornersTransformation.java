package cn.dreamfruits.baselib.network.imageloader.glide.transformations;

import java.lang.System;

/**
 * description: 描述.
 * @date 2018/10/12 10:21.
 * @author: YangYang.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0013\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J(\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0017\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0018\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u0019\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u001a\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u001c\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\u0002J(\u0010 \u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010!\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J(\u0010\"\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010#\u001a\u00020$H\u0016J0\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020&2\u0006\u0010,\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u0003H\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/RoundedCornersTransformation;", "Lcn/dreamfruits/baselib/network/imageloader/glide/transformations/BitmapTransformation;", "radius", "", "margin", "cornerType", "Lcn/dreamfruits/baselib/network/imageloader/CornerType;", "(IILcn/dreamfruits/baselib/network/imageloader/CornerType;)V", "diameter", "drawBottomLeftRoundRect", "", "canvas", "Landroid/graphics/Canvas;", "paint", "Landroid/graphics/Paint;", "right", "", "bottom", "drawBottomRightRoundRect", "drawBottomRoundRect", "drawCircle", "drawDiagonalFromTopLeftRoundRect", "drawDiagonalFromTopRightRoundRect", "drawLeftRoundRect", "drawOtherBottomLeftRoundRect", "drawOtherBottomRightRoundRect", "drawOtherTopLeftRoundRect", "drawOtherTopRightRoundRect", "drawRightRoundRect", "drawRoundRect", "width", "height", "drawTopLeftRoundRect", "drawTopRightRoundRect", "drawTopRoundRect", "key", "", "transform", "Landroid/graphics/Bitmap;", "context", "Landroid/content/Context;", "pool", "Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "toTransform", "outWidth", "outHeight", "baselib_debug"})
public final class RoundedCornersTransformation extends cn.dreamfruits.baselib.network.imageloader.glide.transformations.BitmapTransformation {
    private final int radius = 0;
    private final int margin = 0;
    private final cn.dreamfruits.baselib.network.imageloader.CornerType cornerType = null;
    private final int diameter = 0;
    
    @kotlin.jvm.JvmOverloads
    public RoundedCornersTransformation(int radius, int margin) {
        super();
    }
    
    @kotlin.jvm.JvmOverloads
    public RoundedCornersTransformation(int radius, int margin, @org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.network.imageloader.CornerType cornerType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected android.graphics.Bitmap transform(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool pool, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }
    
    private final void drawRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float width, float height) {
    }
    
    private final void drawCircle(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawTopLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawTopRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawBottomLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawBottomRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawTopRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawBottomRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawOtherTopLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawOtherTopRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawOtherBottomLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawOtherBottomRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawDiagonalFromTopLeftRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    private final void drawDiagonalFromTopRightRoundRect(android.graphics.Canvas canvas, android.graphics.Paint paint, float right, float bottom) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String key() {
        return null;
    }
}