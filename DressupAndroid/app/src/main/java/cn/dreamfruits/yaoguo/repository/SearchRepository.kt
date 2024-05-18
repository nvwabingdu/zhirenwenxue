package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.search.*
import cn.dreamfruits.yaoguo.repository.datasource.remote.SearchRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/10
 * @TIME 20:47
 */
class SearchRepository(dataSource: SearchRemoteDataSource) :
    BaseRemoteRepository<SearchRemoteDataSource>(dataSource) {

    /**
     * 获取滚动热搜词
     */
    fun getScrollHotWords(size: Int): Observable<SearchScrollHotWordsBean> {
        return remoteDataSource.getScrollHotWords(size).doOnNext {
            MMKVConstants.initData(MMKVConstants.SEARCH_SCROLL_HOT_WORDS_BEAN,it)
        }
    }


    /**
     * 搜索用户
     */
    fun getSearchUser(
        key: String,//搜索词
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long?,//用于分页，传入上一次接口返回lastTime
        withoutIds: String?//过滤的用户的ids，例如：id1,id2,id3
    ): Observable<SearchUserBean> {
        return remoteDataSource.getSearchUser(
            key,
            page,
            size,
            lastTime,
            withoutIds
        ).doOnNext {

        }
    }
    /**
     * 搜索模糊匹配  本地仓库
     */
    fun searchWord(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?
    ): Observable<SearchWordBean> {
        return remoteDataSource.searchWord(key,page,size,lastTime)
    }


    /**
     * 搜索话题
     */
    fun searchTopic(
        key: String,
        page: Int = 1,
        size: Int = 10,
        lastTime: Long? = null
    ):Observable<BasePageResult<TopicBean>>{
       return remoteDataSource.searchTopic(key, page, size, lastTime)
    }


    /**
     * 搜索动态
     */
    fun getSearchFeed(
        key: String,//搜索词
        type: Int,//1-综合 2-最新 3-最热
        page: Int,//页码 第一页为1  后面传入接口返回的page字段值
        size: Int,//每页数量
        lastTime: Long?//分页用，传入接口返回的该值
    ):Observable<WaterfallFeedBean>{

        return remoteDataSource.getSearchFeed(key, type, page, size, lastTime)
    }


    /**
     * 搜索单品
     */
    fun getSearchSingleProduct(
        key: String,//搜索词
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long?//用于分页，传入上一次接口返回lastTime
    ):Observable<SearchSingleProductBean>{

        return remoteDataSource.getSearchSingleProduct(key, page, size, lastTime)
    }

    /**
     * 获取腰果热榜  本地仓库
     */
    fun getYgHotFeedList(

    ): Observable<GetYgHotFeedListBean> {
        return remoteDataSource.getYgHotFeedList()
    }

    /**
     * 猜你想搜  本地仓库
     */
    fun getGuessHotWords(

        size: Int,

        ): Observable<GetGuessHotWordsBean> {
        return remoteDataSource.getGuessHotWords(size)
    }

    /**
     * 搜索单品
     */
    fun getSearchOutfit(
        key: String,//搜索词
        type: Int,//1-综合 2-最新 3-最热
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long//用于分页，传入上一次接口返回lastTime
    ):Observable<SearchOutfitBean>{

        return remoteDataSource.getSearchOutfit(key, type, page, size, lastTime)
    }




}