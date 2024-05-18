package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.module.share.bean.ShareUserListBean
import cn.dreamfruits.yaoguo.module.share.bean.ShortUrlEntity
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.ClothesApi
import cn.dreamfruits.yaoguo.repository.api.ShareApi
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import io.reactivex.rxjava3.core.Observable

/**
 * 单品相关的数据源
 */
class ShareRemoteDataSource : BaseRemoteDataSource() {

    private val shareApi = retrofitService(ShareApi::class.java)

    fun getShareUserList(
    ): Observable<ShareUserListBean> {
        return shareApi.getShareUserList()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun getRecommendShareUserList(
    ): Observable<BasePageResult<UserInfo>> {
        return shareApi.getRecommendShareUserList()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun getShortUrl(
        targetId: String,
        type: Int,
    ): Observable<ShortUrlEntity> {
        return shareApi.getShortUrl(targetId, type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


}