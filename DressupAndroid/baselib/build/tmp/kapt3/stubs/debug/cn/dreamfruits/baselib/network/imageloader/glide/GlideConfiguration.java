package cn.dreamfruits.baselib.network.imageloader.glide;

import java.lang.System;

/**
 * description: GlideConfiguration.
 * @date 2018/10/11 18:47.
 * @author: YangYang.
 */
@com.bumptech.glide.annotation.GlideModule
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J \u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u00a8\u0006\u0011"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/GlideConfiguration;", "Lcom/bumptech/glide/module/AppGlideModule;", "()V", "applyOptions", "", "context", "Landroid/content/Context;", "builder", "Lcom/bumptech/glide/GlideBuilder;", "isManifestParsingEnabled", "", "registerComponents", "glide", "Lcom/bumptech/glide/Glide;", "registry", "Lcom/bumptech/glide/Registry;", "Companion", "baselib_debug"})
public final class GlideConfiguration extends com.bumptech.glide.module.AppGlideModule {
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.network.imageloader.glide.GlideConfiguration.Companion Companion = null;
    public static final long IMAGE_DISK_CACHE_MAX_SIZE = 209715200L;
    
    public GlideConfiguration() {
        super();
    }
    
    @java.lang.Override
    public void applyOptions(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.GlideBuilder builder) {
    }
    
    @java.lang.Override
    public void registerComponents(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.Glide glide, @org.jetbrains.annotations.NotNull
    com.bumptech.glide.Registry registry) {
    }
    
    @java.lang.Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcn/dreamfruits/baselib/network/imageloader/glide/GlideConfiguration$Companion;", "", "()V", "IMAGE_DISK_CACHE_MAX_SIZE", "", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}