package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.AttentionApi
import cn.dreamfruits.yaoguo.repository.bean.attention.*
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 17:49
 */
class AttentionRemoteDataSource : BaseRemoteDataSource(){
    private val recommendApi: AttentionApi = retrofitService(AttentionApi::class.java)


    /**
     * 关注用户
     */
    fun getFollowUser(
        targetId: Long
    ): Observable<FollowUserBean> {
        return recommendApi.getFollowUser(targetId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 取消关注
     */
    fun getUnfollowUser(
        targetId: Long
    ): Observable<UnfollowUserBean> {
        return recommendApi.getUnfollowUser(targetId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 获取关注列表
     */
    fun getUserFollowList(
        size: Int,
        lastTime: Long,
        targetUid: Long
    ): Observable<UserFollowListBean> {
        return recommendApi.getUserFollowList(size,lastTime,targetUid)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }





    /**
     * 获取粉丝列表
     */
    fun getUserFollowerList(
        size: Int,
        lastTime: Long,
        targetUid: Long
    ): Observable<UserFollowFansListBean> {
        return recommendApi.getUserFollowerList(size,lastTime,targetUid)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 推荐关注
     */
    fun getRecommendUserList(
        page: Int,
        size: Int
    ): Observable<RecommendUserListBean> {
        return recommendApi.getRecommendUserList(page,size)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 获取at列表
     */
    fun getAtUserList(
        size: Int,
        lastTime: Long?,
        withoutIds: String?
    ): Observable<SearchUserBean> {
        return recommendApi.getAtUserList(size,lastTime,withoutIds)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

}