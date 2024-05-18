package cn.dreamfruits.baselib.integration.cache

/**
 * 用于缓存框架中所必需的组件,开发者可通过 GlobalConfigModule.Builder#cacheFactory(Factory) 为框架提供缓存策略
 * 开发者也可以用于自己日常中的使用
 */
interface Cache {

    interface Factory{


      fun build()
    }




}