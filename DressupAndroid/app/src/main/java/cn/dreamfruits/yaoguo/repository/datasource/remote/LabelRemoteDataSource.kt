package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.LabelApi
import cn.dreamfruits.yaoguo.repository.api.OauthApi
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import cn.dreamfruits.yaoguo.repository.bean.label.LabelDetailsBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchFeedBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/20
 * @TIME 10:56
 */
class LabelRemoteDataSource: BaseRemoteDataSource(){


    private val labelApi: LabelApi = retrofitService(LabelApi::class.java)

    /**
     * 首页tab-推荐话题  发布用
     */
    fun getRecommendLabelList(
        count: Int
    ): Observable<GetRecommendLabelListBean> {
        return labelApi.getRecommendLabelList(count).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 首页tab-推荐话题
     */
    fun getRecommendLabelList2(
        count: Int
    ): Observable<GetRecommendLabelListBean> {
        return labelApi.getRecommendLabelList2(count).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 话题 详情页
     */
    fun getLabelDetail(
        id: Long?
    ): Observable<LabelDetailsBean> {
        return labelApi.getLabelDetail(id).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }




}