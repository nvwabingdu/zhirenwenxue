package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.bean.attention.*
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.AttentionRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 17:49
 */
class AttentionRepository(dataSource: AttentionRemoteDataSource) :
    BaseRemoteRepository<AttentionRemoteDataSource>(dataSource) {


    /**
     *关注用户
     */
    fun getFollowUser(targetId: Long): Observable<FollowUserBean> {
        return remoteDataSource.getFollowUser(targetId).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.FOLLOW_USER,
                it
            )
        }
    }


    /**
     *取消关注
     */
    fun getUnfollowUser(targetId: Long): Observable<UnfollowUserBean> {
        return remoteDataSource.getUnfollowUser(targetId).doOnNext {
//            MMKVConstants.initData(
//                MMKVConstants.UN_FOLLOW_USER,
//                it
//            )
        }
    }


    /**
     *获取关注列表
     */
    fun getUserFollowList(
        size: Int,
        lastTime: Long,
        targetUid: Long
    ): Observable<UserFollowListBean> {
        return remoteDataSource.getUserFollowList(size, lastTime, targetUid).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.USER_FOLLOW_LIST,
                it
            )
        }
    }



    /**
     *获取粉丝列表
     */
    fun getUserFollowerList(
        size: Int,
        lastTime: Long,
        targetUid: Long
    ): Observable<UserFollowFansListBean> {
        return remoteDataSource.getUserFollowerList(size, lastTime, targetUid).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.USER_FOLLOW_FANS_LIST,
                it
            )
        }
    }


    /**
     *推荐关注
     */
    fun getRecommendUserList(
        page: Int,
        size: Int
    ): Observable<RecommendUserListBean> {
        return remoteDataSource.getRecommendUserList(page, size).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.RECOMMEND_USER_LIST,
                it
            )
        }
    }


    /**
     *获取at列表
     */
    fun getAtUserList(
        size: Int,
        lastTime: Long?,
        withoutIds: String?
    ): Observable<SearchUserBean> {
        return remoteDataSource.getAtUserList(size, lastTime,withoutIds).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.AT_USER_LIST,
                it
            )
        }
    }

}