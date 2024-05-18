package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.CollectApi
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/4/7
 * @TIME 15:25
 */
class CollectRemoteDataSource : BaseRemoteDataSource(){
    private val collectApi: CollectApi = retrofitService(CollectApi::class.java)

    /**
     * 收藏
     */
    fun getCollect(
        type: Long,//类型，0-动态，1-单品，2-穿搭方案
       targetId: Long,//目标id(动态id | 单品id | 穿搭id)
        outfitStr: String?//穿搭json，创建动态
    ): Observable<CollectBean> {
        return collectApi.getCollect(type,targetId,outfitStr)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 取消收藏
     */
    fun getUnCollect(
        targetId: Long,//目标id(动态id | 单品id | 穿搭id)
       type: Long//类型，0-动态，1-单品，2-穿搭方案
    ): Observable<UncollectBean> {
        return collectApi.getUnCollect(targetId,type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 收藏列表
     */
    fun getCollectList(
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
       type: Long,//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
        size: Long,//每页数量
        lastTime: Long?//第一页不传，第二页开始使用上一页返回的lastTime
    ): Observable<GetCollectListBean> {
        return collectApi.getCollectList(targetId,type,size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

}