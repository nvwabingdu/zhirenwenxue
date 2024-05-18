package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.bean.feed.*
import cn.dreamfruits.yaoguo.repository.bean.label.GetLabelListBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.FeedRemoteDataSource
import cn.dreamfruits.yaoguo.util.Tool
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/14
 * @TIME 15:55
 */
class FeedRepository(dataSource: FeedRemoteDataSource) : BaseRemoteRepository<FeedRemoteDataSource>(dataSource) {

    /**
     *获取推荐动态列表
     */
    fun getRecommendFeedList(size: Int, type: Int): Observable<WaterfallFeedBean> {
        return remoteDataSource.getRecommendFeedList(size,type)
            .doOnNext {
                MMKVConstants.initData(MMKVConstants.RECOMMEND_FEED_LIST,it)
            }
    }

    /**
     * 删除动态  本地仓库
     */
    fun deleteFeed(

        id: Long,//目标id 必须

    ): Observable<DeleteFeedBean> {
        return remoteDataSource.deleteFeed(id)
    }

    /**
     * 获取话题动态列表
     */
    fun getLabelFeedList( labelId: Long, size: Int, lastTime: Long): Observable<WaterfallFeedBean> {
        return remoteDataSource.getLabelFeedList(labelId,size,lastTime)
            .doOnNext {
//                MMKVConstants.initData(MMKVConstants.LABEL_FEED_LIST,Tool().toUrlWaterfallFeedBean(it,"0"))
            }
    }

    /**
     * 获取用户动态列表  本地仓库
     */
    fun getUserFeedList(

        type: Int,//0-用户动态列表 1-用户收藏动态列表 2-用户浏览过得动态列表
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        size: Int,//0-私密，1-公开，2-全部
        lastTime: Long?

    ): Observable<WaterfallFeedBean> {
        return remoteDataSource.getUserFeedList(type,targetId,size,lastTime)
    }

    /**
     * 获取关注动态列表
     */
    fun getCareFeedList( size: Int, lastTime: Long): Observable<WaterfallFeedBean> {
        return remoteDataSource.getCareFeedList(size,lastTime)
            .doOnNext {
                MMKVConstants.initData(MMKVConstants.GET_CARE_FEED_LIST,it)
            }
    }


    /**
     * 点赞
     */
    fun getLaudFeed( feedId: Long): Observable<LaudFeedBean> {
        return remoteDataSource.getLaudFeed(feedId)
            .doOnNext {
            //MMKVConstants.initData(MMKVConstants.,it)
            }
    }


    /**
     * 取消点赞
     */
    fun getUnLudFeed( feedId: Long): Observable<UnLudFeedBean> {
        return remoteDataSource.getUnLudFeed(feedId)
            .doOnNext {
            //MMKVConstants.initData(MMKVConstants.,it)
            }
    }


    /**
     * 动态详情
     */
    fun getFeedDetail(id: Long): Observable<WaterfallFeedBean.Item.Info> {
        return remoteDataSource.getFeedDetail(id)
            .doOnNext {
                //MMKVConstants.initData(MMKVConstants.,it)
            }
    }

    /**
     * 创建动态
     */
    fun createFeed(
        title:String?,
        content:String?,
        type:Int,
        picUrls:String?,
        seeLimit:Int,
        videoUrls:String?,
        topicIds:String?,
        atUser:String?,
        provinceAdCode:String?,
        cityAdCode:String?,
        longitude:Double?,
        latitude:Double?,
        address:String?,
        singleIds:String?
    ):Observable<FeedDetailsBean>{
        return remoteDataSource.createFeed(
            title= title,
            content= content,
            type = type,
            picUrls = picUrls,
            seeLimit = seeLimit,
            videoUrl = videoUrls,
            topicIds = topicIds,
            atUser = atUser,
            provinceAdCode = provinceAdCode,
            cityAdCode = cityAdCode,
            longitude = longitude,
            latitude = latitude,
            address =address,
            singleIds = singleIds
        )
    }



}




