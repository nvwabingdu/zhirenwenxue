package cn.dreamfruits.yaoguo.di

import cn.dreamfruits.yaoguo.constants.ModuleConstants
import cn.dreamfruits.yaoguo.repository.*
import cn.dreamfruits.yaoguo.repository.OauthRepository
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.FeedBackRepository
import cn.dreamfruits.yaoguo.repository.LabelRepository
import cn.dreamfruits.yaoguo.repository.AttentionRepository
import cn.dreamfruits.yaoguo.repository.datasource.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * 仓库注册
 */
val repositoryModule = module {
    named(ModuleConstants.TAG_REPOSITORY_MODULE)
    single { OauthRepository(get()) }
    single { SearchRepository(get()) }
    single { FeedRepository(get()) }
    single { LabelRepository(get()) }
    single { FeedBackRepository(get()) }
    single { AttentionRepository(get()) }
    single { ThridpartyRepository(get()) }
    single { FindRepository(get()) }
    single { UserRepository(get()) }
    single { CollectRepository(get()) }
    single { ClothesRepository(get()) }
    single { CommentRepository(get()) }
    single { MessageRepository(get()) }
    single { UnityRepository(get()) }
    single { ShareRepository(get()) }
}