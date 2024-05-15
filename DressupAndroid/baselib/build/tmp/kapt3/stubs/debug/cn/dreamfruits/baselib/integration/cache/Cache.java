package cn.dreamfruits.baselib.integration.cache;

import java.lang.System;

/**
 * 用于缓存框架中所必需的组件,开发者可通过 GlobalConfigModule.Builder#cacheFactory(Factory) 为框架提供缓存策略
 * 开发者也可以用于自己日常中的使用
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2 = {"Lcn/dreamfruits/baselib/integration/cache/Cache;", "", "Factory", "baselib_debug"})
public abstract interface Cache {
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&\u00a8\u0006\u0004"}, d2 = {"Lcn/dreamfruits/baselib/integration/cache/Cache$Factory;", "", "build", "", "baselib_debug"})
    public static abstract interface Factory {
        
        public abstract void build();
    }
}