package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.bean.find.*
import cn.dreamfruits.yaoguo.repository.datasource.remote.FindRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/30
 * @TIME 14:39
 */
class FindRepository (dataSource: FindRemoteDataSource) :
    BaseRemoteRepository<FindRemoteDataSource>(dataSource) {

    /**
     * 发现用户
     */
    fun getDiscoverUserList(temp: Int): Observable<DiscoverUserListBean> {
        return remoteDataSource.getDiscoverUserList(temp).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.GET_DISCOVER_USER_LIST,
                it
            )

        }
    }


    /**
     * 感兴趣的人
     */
    fun getHomeRecommendUserList( size: Int): Observable<HomeRecommendUserListBean> {
        return remoteDataSource.getHomeRecommendUserList(size).doOnNext {
//            MMKVConstants.initData(
//                MMKVConstants.GET_HOME_RECOMMEND_USERLIST,
//                it
//            )

        }
    }


    /**
     * 忽略感兴趣的人
     */
    fun ignoreRecommendUser( id: Long, type: Int): Observable<IgnoreRecommendUserBean> {
        return remoteDataSource.ignoreRecommendUser(id,type).doOnNext {
//            MMKVConstants.initData(
//                MMKVConstants.IGNORE_RECOMMEND_USER,
//                it
//            )
        }
    }

    /**
     * 发现穿搭
     */
    fun findOutfit( temp: Int): Observable<FindOutfitBean> {
        return remoteDataSource.findOutfit(temp).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.FIND_OUTFIT,
                it
            )
        }
    }


    /**
     * 发现话题
     */
    fun findLabel( temp: Int): Observable<FindLabelBean> {
        return remoteDataSource.findLabel(temp).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.FIND_LABEL,
                it
            )
        }
    }

}