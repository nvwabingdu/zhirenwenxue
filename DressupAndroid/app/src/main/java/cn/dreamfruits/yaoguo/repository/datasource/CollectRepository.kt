package cn.dreamfruits.yaoguo.repository.datasource

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.CollectRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/4/7
 * @TIME 15:26
 */
class CollectRepository (dataSource: CollectRemoteDataSource) :
    BaseRemoteRepository<CollectRemoteDataSource>(dataSource) {

    /**
     * 收藏
     */
    fun getCollect( type: Long,//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
                    targetId: Long,//目标id(动态id | 单品id | 穿搭id)
                    outfitStr: String?//穿搭json，创建动态
         ): Observable<CollectBean> {
        return remoteDataSource.getCollect(type,targetId,outfitStr).doOnNext {

        }
    }



    /**
     * 取消收藏
     */
    fun getUnCollect( targetId: Long,//目标id(动态id | 单品id | 穿搭id)
                      type: Long//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
    ): Observable<UncollectBean> {
        return remoteDataSource.getUnCollect(targetId,type).doOnNext {

        }
    }

    /**
     * 获取收藏列表
     */
    fun getCollectList(
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        type: Long,//类型，0-动态，1-单品，2-穿搭方案，3-套装
        size: Long,//每页数量
        lastTime: Long?//第一页不传，第二页开始使用上一页返回的lastTime
    ): Observable<GetCollectListBean> {
        return remoteDataSource.getCollectList(targetId,type,size,lastTime).doOnNext {

        }
    }




}