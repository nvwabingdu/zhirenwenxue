package cn.dreamfruits.baselib.network.imageloader;

import java.lang.System;

/**
 * ================================================
 * 图片加载策略,实现 {@link BaseImageLoaderStrategy}
 * 并通过 {@link ImageLoader#setLoadImgStrategy(BaseImageLoaderStrategy)} 配置后,才可进行图片请求
 * ================================================
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003J\u001d\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\tJ\u001d\u0010\n\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u000b"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/BaseImageLoaderStrategy;", "T", "Lcn/dreamfruits/baselib/network/imageloader/ImageConfig;", "", "clear", "", "ctx", "Landroid/content/Context;", "config", "(Landroid/content/Context;Lcn/dreamfruits/baselib/network/imageloader/ImageConfig;)V", "loadImage", "baselib_debug"})
public abstract interface BaseImageLoaderStrategy<T extends cn.dreamfruits.baselib.network.imageloader.ImageConfig> {
    
    /**
     * 加载图片
     *
     * @param ctx    {@link Context}
     * @param config 图片加载配置信息
     */
    public abstract void loadImage(@org.jetbrains.annotations.NotNull
    android.content.Context ctx, @org.jetbrains.annotations.NotNull
    T config);
    
    /**
     * 停止加载
     *
     * @param ctx    {@link Context}
     * @param config 图片加载配置信息
     */
    public abstract void clear(@org.jetbrains.annotations.NotNull
    android.content.Context ctx, @org.jetbrains.annotations.NotNull
    T config);
}