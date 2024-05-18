package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.SearchApi
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.search.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/10
 * @TIME 20:43
 */
class SearchRemoteDataSource : BaseRemoteDataSource() {

    private val searchApi: SearchApi = retrofitService(SearchApi::class.java)


    /**
     * 搜索动态
     */
    fun getSearchFeed(
        key: String,//搜索词
        type: Int,//1-综合 2-最新 3-最热
        page: Int,//页码 第一页为1  后面传入接口返回的page字段值
        size: Int,//每页数量
        lastTime: Long?//分页用，传入接口返回的该值
    ): Observable<WaterfallFeedBean> {
        return searchApi.getSearchFeed(key, type, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
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
        return searchApi.getSearchUser(key, page, size, lastTime, withoutIds)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 搜索单品
     */
    fun getSearchSingleProduct(
        key: String,//搜索词
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long?//用于分页，传入上一次接口返回lastTime
    ): Observable<SearchSingleProductBean> {
        return searchApi.getSearchSingleProduct(key, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取腰果热榜   远程仓库
     */
    fun getYgHotFeedList(

    ): Observable<GetYgHotFeedListBean> {
        return searchApi.getYgHotFeedList()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 猜你想搜   远程仓库
     */
    fun getGuessHotWords(

        size: Int,

        ): Observable<GetGuessHotWordsBean> {
        return searchApi.getGuessHotWords(size)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 搜索穿搭
     */
    fun getSearchOutfit(
        key: String,//搜索词
        type: Int,//1-综合 2-最新 3-最热
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long//用于分页，传入上一次接口返回lastTime
    ): Observable<SearchOutfitBean> {
        return searchApi.getSearchOutfit(key, type, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 搜索滚动词
     */
    fun getScrollHotWords(size: Int): Observable<SearchScrollHotWordsBean> {
        return searchApi.getScrollHotWords(size).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 搜索模糊匹配   远程仓库
     */
    fun searchWord(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?
    ): Observable<SearchWordBean> {
        return searchApi.searchWord(key,page,size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 搜索话题
     */
    fun searchTopic(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?
    ):Observable<BasePageResult<TopicBean>>{

        return searchApi.searchTopic(
            key,
            page,
            size,
            lastTime
        ).compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }
}