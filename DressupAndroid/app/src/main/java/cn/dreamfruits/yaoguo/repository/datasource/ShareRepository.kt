package cn.dreamfruits.yaoguo.repository.datasource

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.module.share.bean.ShareUserListBean
import cn.dreamfruits.yaoguo.module.share.bean.ShortUrlEntity
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.ClothesRemoteDataSource
import cn.dreamfruits.yaoguo.repository.datasource.remote.ShareRemoteDataSource
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Observable


/***
 * 分享相关仓库
 */
class ShareRepository(dataSource: ShareRemoteDataSource) :
    BaseRemoteRepository<ShareRemoteDataSource>(dataSource) {

    fun getShareUserList(
    ): Observable<ShareUserListBean> {
        return remoteDataSource.getShareUserList()
    }

    fun getRecommendShareUserList(
    ): Observable<BasePageResult<UserInfo>> {
        return remoteDataSource.getRecommendShareUserList()
    }

    fun getShortUrl(
        targetId: String,
        type: Int,
    ): Observable<ShortUrlEntity> {
        return remoteDataSource.getShortUrl(targetId, type)
    }

}