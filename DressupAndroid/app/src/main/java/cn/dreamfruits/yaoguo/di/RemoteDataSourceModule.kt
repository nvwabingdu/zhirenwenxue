package cn.dreamfruits.yaoguo.di

import cn.dreamfruits.yaoguo.constants.ModuleConstants
import cn.dreamfruits.yaoguo.repository.datasource.remote.OauthRemoteDataSource
import cn.dreamfruits.yaoguo.repository.datasource.remote.UserRemoteDataSource
import cn.dreamfruits.yaoguo.repository.datasource.remote.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * 远端数据来源 注册
 */
val remoteDataSourceModule = module {
    named(ModuleConstants.TAG_REMOTE_DATA_SOURCE_MODULE)
    single { OauthRemoteDataSource() }
    single { SearchRemoteDataSource() }
    single { FeedRemoteDataSource() }
    single { LabelRemoteDataSource() }
    single { FeedBackRemoteDataSource() }
    single { AttentionRemoteDataSource() }
    single { ThridpartyRemoteDataSource() }
    single { FindRemoteDataSource() }
    single { UserRemoteDataSource() }
    single { CollectRemoteDataSource() }
    single { ClothesRemoteDataSource() }
    single { CommentRemoteDataSource() }
    single { MessageRemoteDataSource()}
    single { UnityRemoteDataSource() }
    single { ShareRemoteDataSource() }
}