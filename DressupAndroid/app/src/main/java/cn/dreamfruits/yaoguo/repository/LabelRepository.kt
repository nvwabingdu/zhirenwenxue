package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import cn.dreamfruits.yaoguo.repository.bean.label.LabelDetailsBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.LabelRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/20
 * @TIME 11:01
 */
class LabelRepository (dataSource: LabelRemoteDataSource) : BaseRemoteRepository<LabelRemoteDataSource>(dataSource) {


    /**
     *首页tab-推荐话题  发布
     */
    fun getRecommendLabelList(count:Int): Observable<GetRecommendLabelListBean> {
        return remoteDataSource.getRecommendLabelList(count).doOnNext {
            MMKVConstants.initData(MMKVConstants.RECOMMEND_LABEL_LIST,
                it)
        }
    }

    /**
     *首页tab-推荐话题
     */
    fun getRecommendLabelList2(count:Int): Observable<GetRecommendLabelListBean> {
        return remoteDataSource.getRecommendLabelList2(count).doOnNext {
            MMKVConstants.initData(MMKVConstants.RECOMMEND_LABEL_LIST,
                it)
        }
    }


    /**
     * 话题 详情页
     */
    fun getLabelDetail( id: Long?): Observable<LabelDetailsBean> {
        return remoteDataSource.getLabelDetail(id).doOnNext {
        }
    }

}