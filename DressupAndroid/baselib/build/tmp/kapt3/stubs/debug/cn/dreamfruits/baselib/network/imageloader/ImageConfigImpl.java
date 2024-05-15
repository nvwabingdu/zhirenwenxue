package cn.dreamfruits.baselib.network.imageloader;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0015\n\u0002\b\t\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\b\u0016\u0018\u00002\u00020\u0001:\u0001oB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\b\"\u0004\b\u0019\u0010\nR\u001a\u0010\u001a\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\u001d\u001a\u00020\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\"\u0010#\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\'\"\u0004\b(\u0010)R(\u0010*\u001a\u0010\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020%\u0018\u00010+X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u001a\u00101\u001a\u000202X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001a\u00107\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u000e\"\u0004\b9\u0010\u0010R\u008b\u0001\u0010:\u001as\u0012\u0013\u0012\u00110<\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(?\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(@\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(A\u0012\u0013\u0012\u00110B\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(C\u0012\u0013\u0012\u00110B\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(D\u0012\u0004\u0012\u00020%\u0018\u00010;X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010F\"\u0004\bG\u0010HR*\u0010I\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010J\u0012\u0004\u0012\u00020%\u0018\u00010+X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bK\u0010.\"\u0004\bL\u00100R\u001e\u0010M\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010R\u001a\u0004\bN\u0010O\"\u0004\bP\u0010QR\u001a\u0010S\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bT\u0010\u000e\"\u0004\bU\u0010\u0010R\u001c\u0010V\u001a\u0004\u0018\u00010WX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bX\u0010Y\"\u0004\bZ\u0010[R9\u0010\\\u001a!\u0012\u0015\u0012\u0013\u0018\u00010J\u00a2\u0006\f\b=\u0012\b\b>\u0012\u0004\b\b(]\u0012\u0004\u0012\u00020%\u0018\u00010+X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b^\u0010.\"\u0004\b_\u00100R\u001a\u0010`\u001a\u00020aX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bb\u0010c\"\u0004\bd\u0010eR\u001c\u0010f\u001a\u0004\u0018\u00010gX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bh\u0010i\"\u0004\bj\u0010kR\u001a\u0010l\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bm\u0010\b\"\u0004\bn\u0010\n\u00a8\u0006p"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/ImageConfigImpl;", "Lcn/dreamfruits/baselib/network/imageloader/ImageConfig;", "builder", "Lcn/dreamfruits/baselib/network/imageloader/ImageConfigImpl$Builder;", "(Lcn/dreamfruits/baselib/network/imageloader/ImageConfigImpl$Builder;)V", "blur", "", "getBlur", "()Z", "setBlur", "(Z)V", "blurRadius", "", "getBlurRadius", "()I", "setBlurRadius", "(I)V", "cacheStrategy", "Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;", "getCacheStrategy", "()Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;", "setCacheStrategy", "(Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;)V", "corner", "getCorner", "setCorner", "cornerRadius", "getCornerRadius", "setCornerRadius", "cornerType", "Lcn/dreamfruits/baselib/network/imageloader/CornerType;", "getCornerType", "()Lcn/dreamfruits/baselib/network/imageloader/CornerType;", "setCornerType", "(Lcn/dreamfruits/baselib/network/imageloader/CornerType;)V", "failed", "Lkotlin/Function0;", "", "getFailed", "()Lkotlin/jvm/functions/Function0;", "setFailed", "(Lkotlin/jvm/functions/Function0;)V", "failedCallBack", "Lkotlin/Function1;", "Lcom/bumptech/glide/load/engine/GlideException;", "getFailedCallBack", "()Lkotlin/jvm/functions/Function1;", "setFailedCallBack", "(Lkotlin/jvm/functions/Function1;)V", "imageLoadScaleType", "Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;", "getImageLoadScaleType", "()Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;", "setImageLoadScaleType", "(Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;)V", "margin", "getMargin", "setMargin", "progress", "Lkotlin/Function5;", "", "Lkotlin/ParameterName;", "name", "url", "isComplete", "percentage", "", "bytesRead", "totalBytes", "getProgress", "()Lkotlin/jvm/functions/Function5;", "setProgress", "(Lkotlin/jvm/functions/Function5;)V", "requestSuccessCallBack", "Landroid/graphics/Bitmap;", "getRequestSuccessCallBack", "setRequestSuccessCallBack", "resourceId", "getResourceId", "()Ljava/lang/Integer;", "setResourceId", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "sampling", "getSampling", "setSampling", "size", "", "getSize", "()[I", "setSize", "([I)V", "success", "bitmap", "getSuccess", "setSuccess", "thumbnail", "", "getThumbnail", "()F", "setThumbnail", "(F)V", "uri", "Landroid/net/Uri;", "getUri", "()Landroid/net/Uri;", "setUri", "(Landroid/net/Uri;)V", "useCrossFade", "getUseCrossFade", "setUseCrossFade", "Builder", "baselib_debug"})
public class ImageConfigImpl extends cn.dreamfruits.baselib.network.imageloader.ImageConfig {
    @org.jetbrains.annotations.Nullable
    private android.net.Uri uri;
    @org.jetbrains.annotations.Nullable
    private java.lang.Integer resourceId;
    @org.jetbrains.annotations.NotNull
    private cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType cacheStrategy;
    @org.jetbrains.annotations.Nullable
    private int[] size;
    private boolean useCrossFade;
    private boolean blur;
    private int blurRadius;
    private int sampling;
    private boolean corner;
    private int cornerRadius;
    @org.jetbrains.annotations.NotNull
    private cn.dreamfruits.baselib.network.imageloader.CornerType cornerType;
    private int margin;
    @org.jetbrains.annotations.NotNull
    private cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType imageLoadScaleType;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> success;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function0<kotlin.Unit> failed;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> progress;
    private float thumbnail;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function1<? super com.bumptech.glide.load.engine.GlideException, kotlin.Unit> failedCallBack;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> requestSuccessCallBack;
    
    public ImageConfigImpl(@org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder builder) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri getUri() {
        return null;
    }
    
    public final void setUri(@org.jetbrains.annotations.Nullable
    android.net.Uri p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getResourceId() {
        return null;
    }
    
    public final void setResourceId(@org.jetbrains.annotations.Nullable
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType getCacheStrategy() {
        return null;
    }
    
    public final void setCacheStrategy(@org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final int[] getSize() {
        return null;
    }
    
    public final void setSize(@org.jetbrains.annotations.Nullable
    int[] p0) {
    }
    
    public final boolean getUseCrossFade() {
        return false;
    }
    
    public final void setUseCrossFade(boolean p0) {
    }
    
    public final boolean getBlur() {
        return false;
    }
    
    public final void setBlur(boolean p0) {
    }
    
    public final int getBlurRadius() {
        return 0;
    }
    
    public final void setBlurRadius(int p0) {
    }
    
    public final int getSampling() {
        return 0;
    }
    
    public final void setSampling(int p0) {
    }
    
    public final boolean getCorner() {
        return false;
    }
    
    public final void setCorner(boolean p0) {
    }
    
    public final int getCornerRadius() {
        return 0;
    }
    
    public final void setCornerRadius(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final cn.dreamfruits.baselib.network.imageloader.CornerType getCornerType() {
        return null;
    }
    
    public final void setCornerType(@org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.network.imageloader.CornerType p0) {
    }
    
    public final int getMargin() {
        return 0;
    }
    
    public final void setMargin(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType getImageLoadScaleType() {
        return null;
    }
    
    public final void setImageLoadScaleType(@org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function1<android.graphics.Bitmap, kotlin.Unit> getSuccess() {
        return null;
    }
    
    public final void setSuccess(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getFailed() {
        return null;
    }
    
    public final void setFailed(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function5<java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Long, java.lang.Long, kotlin.Unit> getProgress() {
        return null;
    }
    
    public final void setProgress(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> p0) {
    }
    
    public final float getThumbnail() {
        return 0.0F;
    }
    
    public final void setThumbnail(float p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function1<com.bumptech.glide.load.engine.GlideException, kotlin.Unit> getFailedCallBack() {
        return null;
    }
    
    public final void setFailedCallBack(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super com.bumptech.glide.load.engine.GlideException, kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function1<android.graphics.Bitmap, kotlin.Unit> getRequestSuccessCallBack() {
        return null;
    }
    
    public final void setRequestSuccessCallBack(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> p0) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0015\n\u0002\b\t\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00002\u0006\u0010}\u001a\u00020\n2\u0006\u0010]\u001a\u00020\nJ\u0006\u0010~\u001a\u00020\u007fJ\u000e\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010J\u0007\u0010\u0080\u0001\u001a\u00020\u0000J\u0007\u0010\u0081\u0001\u001a\u00020\u0000J\u0007\u0010\u0082\u0001\u001a\u00020\u0000J\"\u0010\u0015\u001a\u00020\u00002\u0006\u0010}\u001a\u00020\n2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010>\u001a\u00020\nJ\u0010\u0010!\u001a\u00020\u00002\b\b\u0001\u0010!\u001a\u00020\nJ\u0007\u0010\u0083\u0001\u001a\u00020\u0000J\u000e\u00108\u001a\u00020\u00002\u0006\u00108\u001a\u000209J\u0007\u0010\u0084\u0001\u001a\u00020\u0000J\u0015\u0010\u0085\u0001\u001a\u00020\u00002\f\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%J\u001c\u0010\u0086\u0001\u001a\u00020\u00002\u0013\u0010\u0087\u0001\u001a\u000e\u0012\u0004\u0012\u00020-\u0012\u0004\u0012\u00020&0,J~\u0010\u0088\u0001\u001a\u00020\u00002u\u0010D\u001aq\u0012\u0013\u0012\u00110F\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(I\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(J\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110L\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110L\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(N\u0012\u0004\u0012\u00020&0EJ\u001e\u0010\u0089\u0001\u001a\u00020\u00002\u0015\u0010\u0087\u0001\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010T\u0012\u0004\u0012\u00020&0,J,\u0010\u008a\u0001\u001a\u00020\u00002#\u0010f\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010T\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(g\u0012\u0004\u0012\u00020&0,J\u0019\u0010\u008b\u0001\u001a\u00020\u00002\u0007\u0010\u008c\u0001\u001a\u00020\n2\u0007\u0010\u008d\u0001\u001a\u00020\nJ\u0010\u0010A\u001a\u00020\u00002\b\b\u0001\u0010A\u001a\u00020\nJ\u0010\u0010W\u001a\u00020\u00002\b\b\u0001\u0010W\u001a\u00020\nJ\u000e\u0010j\u001a\u00020\u00002\u0006\u0010j\u001a\u00020kJ\u000e\u0010p\u001a\u00020\u00002\u0006\u0010p\u001a\u00020qJ\u000e\u0010I\u001a\u00020\u00002\u0006\u0010I\u001a\u00020FJ\u000e\u0010z\u001a\u00020\u00002\u0006\u0010z\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\f\"\u0004\b#\u0010\u000eR\"\u0010$\u001a\n\u0012\u0004\u0012\u00020&\u0018\u00010%X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010(\"\u0004\b)\u0010*R(\u0010+\u001a\u0010\u0012\u0004\u0012\u00020-\u0012\u0004\u0012\u00020&\u0018\u00010,X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u001a\u00102\u001a\u000203X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001c\u00108\u001a\u0004\u0018\u000109X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=R\u001a\u0010>\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\f\"\u0004\b@\u0010\u000eR\u001a\u0010A\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bB\u0010\f\"\u0004\bC\u0010\u000eR\u008b\u0001\u0010D\u001as\u0012\u0013\u0012\u00110F\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(I\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(J\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110L\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110L\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(N\u0012\u0004\u0012\u00020&\u0018\u00010EX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bO\u0010P\"\u0004\bQ\u0010RR*\u0010S\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010T\u0012\u0004\u0012\u00020&\u0018\u00010,X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bU\u0010/\"\u0004\bV\u00101R\u001e\u0010W\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\\\u001a\u0004\bX\u0010Y\"\u0004\bZ\u0010[R\u001a\u0010]\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b^\u0010\f\"\u0004\b_\u0010\u000eR\u001c\u0010`\u001a\u0004\u0018\u00010aX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bb\u0010c\"\u0004\bd\u0010eR9\u0010f\u001a!\u0012\u0015\u0012\u0013\u0018\u00010T\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(g\u0012\u0004\u0012\u00020&\u0018\u00010,X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bh\u0010/\"\u0004\bi\u00101R\u001a\u0010j\u001a\u00020kX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bl\u0010m\"\u0004\bn\u0010oR\u001c\u0010p\u001a\u0004\u0018\u00010qX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\br\u0010s\"\u0004\bt\u0010uR\u001c\u0010I\u001a\u0004\u0018\u00010FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bv\u0010w\"\u0004\bx\u0010yR\u001a\u0010z\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b{\u0010\u0006\"\u0004\b|\u0010\b\u00a8\u0006\u008e\u0001"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/ImageConfigImpl$Builder;", "", "()V", "blur", "", "getBlur", "()Z", "setBlur", "(Z)V", "blurRadius", "", "getBlurRadius", "()I", "setBlurRadius", "(I)V", "cacheStrategy", "Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;", "getCacheStrategy", "()Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;", "setCacheStrategy", "(Lcn/dreamfruits/baselib/network/imageloader/DiskCacheStrategyType;)V", "corner", "getCorner", "setCorner", "cornerRadius", "getCornerRadius", "setCornerRadius", "cornerType", "Lcn/dreamfruits/baselib/network/imageloader/CornerType;", "getCornerType", "()Lcn/dreamfruits/baselib/network/imageloader/CornerType;", "setCornerType", "(Lcn/dreamfruits/baselib/network/imageloader/CornerType;)V", "errorPic", "getErrorPic", "setErrorPic", "failed", "Lkotlin/Function0;", "", "getFailed", "()Lkotlin/jvm/functions/Function0;", "setFailed", "(Lkotlin/jvm/functions/Function0;)V", "failedCallBack", "Lkotlin/Function1;", "Lcom/bumptech/glide/load/engine/GlideException;", "getFailedCallBack", "()Lkotlin/jvm/functions/Function1;", "setFailedCallBack", "(Lkotlin/jvm/functions/Function1;)V", "imageLoadScaleType", "Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;", "getImageLoadScaleType", "()Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;", "setImageLoadScaleType", "(Lcn/dreamfruits/baselib/network/imageloader/ImageLoadScaleType;)V", "imageView", "Landroid/widget/ImageView;", "getImageView", "()Landroid/widget/ImageView;", "setImageView", "(Landroid/widget/ImageView;)V", "margin", "getMargin", "setMargin", "placeholder", "getPlaceholder", "setPlaceholder", "progress", "Lkotlin/Function5;", "", "Lkotlin/ParameterName;", "name", "url", "isComplete", "percentage", "", "bytesRead", "totalBytes", "getProgress", "()Lkotlin/jvm/functions/Function5;", "setProgress", "(Lkotlin/jvm/functions/Function5;)V", "requestSuccessCallBack", "Landroid/graphics/Bitmap;", "getRequestSuccessCallBack", "setRequestSuccessCallBack", "resourceId", "getResourceId", "()Ljava/lang/Integer;", "setResourceId", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "sampling", "getSampling", "setSampling", "size", "", "getSize", "()[I", "setSize", "([I)V", "success", "bitmap", "getSuccess", "setSuccess", "thumbnail", "", "getThumbnail", "()F", "setThumbnail", "(F)V", "uri", "Landroid/net/Uri;", "getUri", "()Landroid/net/Uri;", "setUri", "(Landroid/net/Uri;)V", "getUrl", "()Ljava/lang/String;", "setUrl", "(Ljava/lang/String;)V", "useCrossFade", "getUseCrossFade", "setUseCrossFade", "radius", "build", "Lcn/dreamfruits/baselib/network/imageloader/ImageConfigImpl;", "centerCrop", "centerInside", "circleCrop", "fitCenter", "noScaleType", "onFailed", "onFailedCallBack", "callBack", "onProgress", "onRequestSuccessCallBack", "onSuccess", "override", "width", "height", "baselib_debug"})
    public static final class Builder {
        @org.jetbrains.annotations.Nullable
        private java.lang.String url;
        @org.jetbrains.annotations.Nullable
        private android.net.Uri uri;
        @org.jetbrains.annotations.Nullable
        private java.lang.Integer resourceId;
        @org.jetbrains.annotations.Nullable
        private android.widget.ImageView imageView;
        private int placeholder = 0;
        private int errorPic = 0;
        @org.jetbrains.annotations.NotNull
        private cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType cacheStrategy = cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType.ALL;
        @org.jetbrains.annotations.Nullable
        private int[] size;
        private boolean useCrossFade = true;
        private boolean blur = false;
        private int blurRadius = 0;
        private int sampling = 0;
        private boolean corner = false;
        private int cornerRadius = 0;
        @org.jetbrains.annotations.NotNull
        private cn.dreamfruits.baselib.network.imageloader.CornerType cornerType = cn.dreamfruits.baselib.network.imageloader.CornerType.ALL;
        private int margin = 0;
        @org.jetbrains.annotations.NotNull
        private cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType imageLoadScaleType = cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType.CenterCrop;
        private float thumbnail = 1.0F;
        @org.jetbrains.annotations.Nullable
        private kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> success;
        @org.jetbrains.annotations.Nullable
        private kotlin.jvm.functions.Function0<kotlin.Unit> failed;
        @org.jetbrains.annotations.Nullable
        private kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> progress;
        @org.jetbrains.annotations.Nullable
        private kotlin.jvm.functions.Function1<? super com.bumptech.glide.load.engine.GlideException, kotlin.Unit> failedCallBack;
        @org.jetbrains.annotations.Nullable
        private kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> requestSuccessCallBack;
        
        public Builder() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getUrl() {
            return null;
        }
        
        public final void setUrl(@org.jetbrains.annotations.Nullable
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.net.Uri getUri() {
            return null;
        }
        
        public final void setUri(@org.jetbrains.annotations.Nullable
        android.net.Uri p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Integer getResourceId() {
            return null;
        }
        
        public final void setResourceId(@org.jetbrains.annotations.Nullable
        java.lang.Integer p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.widget.ImageView getImageView() {
            return null;
        }
        
        public final void setImageView(@org.jetbrains.annotations.Nullable
        android.widget.ImageView p0) {
        }
        
        public final int getPlaceholder() {
            return 0;
        }
        
        public final void setPlaceholder(int p0) {
        }
        
        public final int getErrorPic() {
            return 0;
        }
        
        public final void setErrorPic(int p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType getCacheStrategy() {
            return null;
        }
        
        public final void setCacheStrategy(@org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final int[] getSize() {
            return null;
        }
        
        public final void setSize(@org.jetbrains.annotations.Nullable
        int[] p0) {
        }
        
        public final boolean getUseCrossFade() {
            return false;
        }
        
        public final void setUseCrossFade(boolean p0) {
        }
        
        public final boolean getBlur() {
            return false;
        }
        
        public final void setBlur(boolean p0) {
        }
        
        public final int getBlurRadius() {
            return 0;
        }
        
        public final void setBlurRadius(int p0) {
        }
        
        public final int getSampling() {
            return 0;
        }
        
        public final void setSampling(int p0) {
        }
        
        public final boolean getCorner() {
            return false;
        }
        
        public final void setCorner(boolean p0) {
        }
        
        public final int getCornerRadius() {
            return 0;
        }
        
        public final void setCornerRadius(int p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.CornerType getCornerType() {
            return null;
        }
        
        public final void setCornerType(@org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.imageloader.CornerType p0) {
        }
        
        public final int getMargin() {
            return 0;
        }
        
        public final void setMargin(int p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType getImageLoadScaleType() {
            return null;
        }
        
        public final void setImageLoadScaleType(@org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.imageloader.ImageLoadScaleType p0) {
        }
        
        public final float getThumbnail() {
            return 0.0F;
        }
        
        public final void setThumbnail(float p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final kotlin.jvm.functions.Function1<android.graphics.Bitmap, kotlin.Unit> getSuccess() {
            return null;
        }
        
        public final void setSuccess(@org.jetbrains.annotations.Nullable
        kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final kotlin.jvm.functions.Function0<kotlin.Unit> getFailed() {
            return null;
        }
        
        public final void setFailed(@org.jetbrains.annotations.Nullable
        kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final kotlin.jvm.functions.Function5<java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Long, java.lang.Long, kotlin.Unit> getProgress() {
            return null;
        }
        
        public final void setProgress(@org.jetbrains.annotations.Nullable
        kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final kotlin.jvm.functions.Function1<com.bumptech.glide.load.engine.GlideException, kotlin.Unit> getFailedCallBack() {
            return null;
        }
        
        public final void setFailedCallBack(@org.jetbrains.annotations.Nullable
        kotlin.jvm.functions.Function1<? super com.bumptech.glide.load.engine.GlideException, kotlin.Unit> p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final kotlin.jvm.functions.Function1<android.graphics.Bitmap, kotlin.Unit> getRequestSuccessCallBack() {
            return null;
        }
        
        public final void setRequestSuccessCallBack(@org.jetbrains.annotations.Nullable
        kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> p0) {
        }
        
        /**
         * 图片地址
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder url(@org.jetbrains.annotations.NotNull
        java.lang.String url) {
            return null;
        }
        
        /**
         * 图片地址
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder uri(@org.jetbrains.annotations.NotNull
        android.net.Uri uri) {
            return null;
        }
        
        /**
         * 图片资源ID
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder resourceId(@androidx.annotation.DrawableRes
        int resourceId) {
            return null;
        }
        
        /**
         * 缓存策略:
         * DiskCacheStrategyType.ALL
         * DiskCacheStrategyType.NONE
         * DiskCacheStrategyType.RESOURCE
         * DiskCacheStrategyType.DATA
         * DiskCacheStrategyType.AUTOMATIC
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder cacheStrategy(@org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.imageloader.DiskCacheStrategyType cacheStrategy) {
            return null;
        }
        
        /**
         * 占位图
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder placeholder(@androidx.annotation.DrawableRes
        int placeholder) {
            return null;
        }
        
        /**
         * 错误占位图
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder errorPic(@androidx.annotation.DrawableRes
        int errorPic) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder imageView(@org.jetbrains.annotations.NotNull
        android.widget.ImageView imageView) {
            return null;
        }
        
        /**
         * 是否使用渐入效果
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder useCrossFade(boolean useCrossFade) {
            return null;
        }
        
        /**
         * 指定大小
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder override(int width, int height) {
            return null;
        }
        
        /**
         * 高斯模糊
         * @param radius 模糊度1-25
         * @param sampling 缩放
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder blur(int radius, int sampling) {
            return null;
        }
        
        /**
         * 圆角
         * @param radius 圆角px
         * @param cornerType 圆角类型默认四个角都有
         * @param margin
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder corner(int radius, @org.jetbrains.annotations.NotNull
        cn.dreamfruits.baselib.network.imageloader.CornerType cornerType, int margin) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder centerCrop() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder centerInside() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder fitCenter() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder circleCrop() {
            return null;
        }
        
        /**
         * 不需要ScaleType
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder noScaleType() {
            return null;
        }
        
        /**
         * 加载成功的回调
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder onSuccess(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> success) {
            return null;
        }
        
        /**
         * 加载失败的回调
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder onFailed(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<kotlin.Unit> failed) {
            return null;
        }
        
        /**
         * 加载进度的回调
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder onProgress(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> progress) {
            return null;
        }
        
        /**
         * 原始图像百分比的缩略图
         */
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder thumbnail(float thumbnail) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder onFailedCallBack(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.bumptech.glide.load.engine.GlideException, kotlin.Unit> callBack) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl.Builder onRequestSuccessCallBack(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit> callBack) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl build() {
            return null;
        }
    }
}