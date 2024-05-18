package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.module.share.bean.ShareUserListBean
import cn.dreamfruits.yaoguo.module.share.bean.ShortUrlEntity
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.repository.bean.attention.*
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 17:29
 */
interface ShareApi {
    /**
     * 获取分享用户列表
     */

    @POST("/usercenter/api/getShareUserList")
    fun getShareUserList(
    ): Observable<BaseResult<ShareUserListBean>>


    @POST("/usercenter/api/getRecommendShareUserList")
    fun getRecommendShareUserList(
    ): Observable<BaseResult<BasePageResult<UserInfo>>>

    /**
     * targetId integer 目标id 必需
     * type integer 必需 0-用户，1-帖子，2-单品，3-话题
     */
    @FormUrlEncoded
    @POST("/app/api/getShortUrl")
    fun getShortUrl(
        @Field("targetId") targetId: String,
        @Field("type") type: Int,
    ): Observable<BaseResult<ShortUrlEntity>>


}

