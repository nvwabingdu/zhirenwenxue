package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.FindApi
import cn.dreamfruits.yaoguo.repository.api.LabelApi
import cn.dreamfruits.yaoguo.repository.bean.find.*
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/30
 * @TIME 14:33
 */
class FindRemoteDataSource : BaseRemoteDataSource(){


    private val findApi: FindApi = retrofitService(FindApi::class.java)

    /**
     * 发现用户
     */
    fun getDiscoverUserList(
        temp: Int
    ): Observable<DiscoverUserListBean> {
        return findApi.getDiscoverUserList(temp).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 感兴趣的人
     */
    fun getHomeRecommendUserList(
        size: Int
    ): Observable<HomeRecommendUserListBean> {
        return findApi.getHomeRecommendUserList(size).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }




    /**
     * 忽略感兴趣的人
     */
    fun ignoreRecommendUser(
        id: Long,//忽略的用户ID
        type: Int//推荐用户的类型
    ): Observable<IgnoreRecommendUserBean> {
        return findApi.ignoreRecommendUser(id,type).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 发现穿搭
     */
    fun findOutfit(
        temp: Int//无字段 拼接
    ): Observable<FindOutfitBean> {
        return findApi.findOutfit(temp).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 发现话题
     */
    fun findLabel(
        temp: Int//无字段 拼接
    ): Observable<FindLabelBean> {
        return findApi.findLabel(temp).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

}