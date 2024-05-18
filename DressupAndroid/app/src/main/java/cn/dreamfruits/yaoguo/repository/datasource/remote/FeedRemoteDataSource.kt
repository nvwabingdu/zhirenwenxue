package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.FeedApi
import cn.dreamfruits.yaoguo.repository.bean.feed.*
import cn.dreamfruits.yaoguo.repository.bean.label.GetLabelListBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/14
 * @TIME 15:54
 */
class FeedRemoteDataSource : BaseRemoteDataSource() {

    private val feedApi: FeedApi = retrofitService(FeedApi::class.java)

    /**
     * 创建动态
     */
    fun createFeed(
        title: String?,//示例值： 测试创建动态标题1 动态标题
        content: String?,//示例值： 测试创建动态标题1 动态内容
        type: Int,//类型：0-图文动态；1-视频动态
        picUrls: String?,//[{"url":"https://devfile.dreamfruits.cn/admin/10002/89f6999043d7c19aec1b6c7186422757.JPG","width":1000,"height":800},{"url":"https://devfile.dreamfruits.cn/admin/10002/de3afd4ccbda949ee8edec6816640d7a.JPG","width":1000,"height":800}]
        seeLimit: Int,//动态私密性：0-公开，1-好友可见，2-私密
        videoUrl: String?,//视频
        topicIds: String?,//示例值： [282184828546528,282231699115488] 话题ids
        atUser: String?,//at用户ids
        provinceAdCode: String?,//省码
        cityAdCode: String?,//城市码
        longitude: Double?,//经度
        latitude: Double?,//维度
        address: String?,//地址
        singleIds: String?,
    ): Observable<FeedDetailsBean> {
        return feedApi.createFeed(
            title ?: "",
            content ?: "",
            type,
            picUrls ?: "",
            seeLimit,
            videoUrl ?: "",
            topicIds ?: "",
            atUser ?: "",
            provinceAdCode ?: "",
            cityAdCode ?: "",
            longitude,
            latitude,
            address ?: "",
            singleIds ?: ""
        ).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }




    /**
     * 动态详情
     */
    fun getFeedDetail(
        id: Long,//动态id
    ): Observable<WaterfallFeedBean.Item.Info> {
        return feedApi.getFeedDetail(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 删除动态   远程仓库
     */
    fun deleteFeed(

        id: Long,//目标id 必须

    ): Observable<DeleteFeedBean> {
        return feedApi.deleteFeed(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取推荐动态列表
     */
    fun getRecommendFeedList(
        size: Int,//请求数目，默认10
        type: Int,//类型：0-图文动态；1-视频动态
    ): Observable<WaterfallFeedBean> {
        return feedApi.getRecommendFeedList(size, type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     *获取关注动态列表
     */
    fun getCareFeedList(
        size: Int,//请求数目，默认10
        lastTime: Long,//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入
    ): Observable<WaterfallFeedBean> {
        return feedApi.getCareFeedList(size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取用户动态列表   远程仓库
     */
    fun getUserFeedList(

        type: Int,//0-用户动态列表 1-用户收藏动态列表 2-用户浏览过得动态列表
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        size: Int,//0-私密，1-公开，2-全部
        lastTime: Long?,

        ): Observable<WaterfallFeedBean> {
        return feedApi.getUserFeedList(type, targetId, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     *获取话题动态列表
     */
    fun getLabelFeedList(
        labelId: Long,
        size: Int,
        lastTime: Long,//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入 不传查询自己的动态列表
    ): Observable<WaterfallFeedBean> {
        return feedApi.getLabelFeedList(labelId, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 指定单品层级相关帖子
     */
    fun getFeedListBySpId(
        singleProductId: Long,//单品id
        floor: Int,//层级
        page: Int,
        size: Int,
        lastTime: Long,//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入 不传查询自己的动态列表
    ): Observable<GetFeedListBySpIdBean> {
        return feedApi.getFeedListBySpId(singleProductId, floor, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取穿搭方案同款帖子
     */
    fun getOutfitFeedList(
        outfitId: Long,//穿搭id
        page: Int,
        size: Int,//一页条数
        lastTime: Long,//用于分页
    ): Observable<GetOutfitFeedListBean> {
        return feedApi.getOutfitFeedList(outfitId, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 点赞
     */
    fun getLaudFeed(
        feedId: Long,
    ): Observable<LaudFeedBean> {
        return feedApi.getLaudFeed(feedId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 取消点赞
     */
    fun getUnLudFeed(
        feedId: Long,
    ): Observable<UnLudFeedBean> {
        return feedApi.getUnLudFeed(feedId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取赞过的帖子
     */
    fun getLaudList(
        size: Int,//一页条数
        lastTime: Long,//用于分页
        targetUid: Long,//他人的用户id
    ): Observable<GetLaudListBean> {
        return feedApi.getLaudList(size, lastTime, targetUid)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 更改动态的可见状态
     */
    fun getChangeFeedSeeLimit(
        id: Long,//一页条数
        seeLimit: Int,//0-公开，1-好友可见，2-私密
    ): Observable<ChangeFeedSeeLimitBean> {
        return feedApi.getChangeFeedSeeLimit(id, seeLimit)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


}