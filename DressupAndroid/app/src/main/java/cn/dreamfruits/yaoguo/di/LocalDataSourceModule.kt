package cn.dreamfruits.yaoguo.di

import cn.dreamfruits.yaoguo.constants.ModuleConstants
import org.koin.core.qualifier.named
import org.koin.dsl.module


/**
 * 本地数据来源 注册
 */
val localDataSourceModule = module {
    named(ModuleConstants.TAG_LOCAL_DATA_SOURCE_MODULE)



}