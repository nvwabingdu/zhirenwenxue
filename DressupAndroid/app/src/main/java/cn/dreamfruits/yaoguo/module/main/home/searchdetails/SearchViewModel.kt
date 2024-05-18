package cn.dreamfruits.yaoguo.module.main.home.searchdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.publish.state.TopicState
import cn.dreamfruits.yaoguo.repository.FindRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.api.SearchApi
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.search.GetGuessHotWordsBean
import cn.dreamfruits.yaoguo.repository.bean.search.GetYgHotFeedListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchFeedBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchSingleProductBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchWordBean
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/5/17
 * @TIME 10:57
 */
class SearchViewModel : BaseViewModel() {
    private val searchRepository by inject<SearchRepository>()

    /**
     * 搜索用户 comment里面有一份 之后再优化为一个
     */
    var mSearchUserBean: SearchUserBean? = null
    private val _searchUserBeanState = MutableLiveData<SearchUserBeanState>()
    val searchUserBeanState: MutableLiveData<SearchUserBeanState> get() = _searchUserBeanState
    fun getSearchUser(
        key: String,//搜索词
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long?,//用于分页，传入上一次接口返回lastTime
        withoutIds: String?//过滤的用户的ids，例如：id1,id2,id3
    ) {
        val disposable = searchRepository.getSearchUser(key, page, size, lastTime, withoutIds)
            .subscribe({
                mSearchUserBean = Tool().toUrlSearchUserBean(it,"0")
                _searchUserBeanState.value = SearchUserBeanState.Success
                Log.e("zqr", "_searchUserBeanState请求成功：$it")
            }, {

                _searchUserBeanState.value = SearchUserBeanState.Fail(it.message)
                Log.e("zqr", "_searchUserBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }



    /**
     * 搜索单品
     */
    var mSearchSingleProductBean: SearchSingleProductBean? = null
    private val _searchSingleProductBeanState = MutableLiveData<SearchSingleProductBeanState>()
    val searchSingleProductBeanState: MutableLiveData<SearchSingleProductBeanState> get() = _searchSingleProductBeanState
    fun getSearchSingleProduct(
        key: String,//搜索词
        page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int,//数量
        lastTime: Long?//用于分页，传入上一次接口返回lastTime
    ) {
        val disposable = searchRepository.getSearchSingleProduct(key, page, size, lastTime)
            .subscribe({
                mSearchSingleProductBean = it
                _searchSingleProductBeanState.value = SearchSingleProductBeanState.Success
                Log.e("zqr", "_searchSingleProductBeanState请求成功：$it")
            }, {
                _searchSingleProductBeanState.value = SearchSingleProductBeanState.Fail(it.message)
                Log.e("zqr", "_searchSingleProductBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取腰果热榜  viewModel
     */
    var mGetYgHotFeedListBean: GetYgHotFeedListBean? = null
    private val _getYgHotFeedListState = MutableLiveData<GetYgHotFeedListState>()
    val getYgHotFeedListState: MutableLiveData<GetYgHotFeedListState> get() = _getYgHotFeedListState
    fun getYgHotFeedList(
    ) {
        val disposable = searchRepository.getYgHotFeedList()
            .subscribe({
                mGetYgHotFeedListBean= it
                _getYgHotFeedListState.value = GetYgHotFeedListState.Success
                Log.e("zqr", "_getYgHotFeedListState请求成功：$it")
            }, {

                _getYgHotFeedListState.value = GetYgHotFeedListState.Fail(it.message)
                Log.e("zqr", "_getYgHotFeedListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 猜你想搜  viewModel
     */
    var mGetGuessHotWordsBean: GetGuessHotWordsBean? = null
    private val _getGuessHotWordsState = MutableLiveData<GetGuessHotWordsState>()
    val getGuessHotWordsState: MutableLiveData<GetGuessHotWordsState> get() = _getGuessHotWordsState
    fun getGuessHotWords(

        size: Int,

        ) {
        val disposable = searchRepository.getGuessHotWords(size)
            .subscribe({
                mGetGuessHotWordsBean= it
                _getGuessHotWordsState.value = GetGuessHotWordsState.Success
                Log.e("zqr", "_getGuessHotWordsState请求成功：$it")
            }, {

                _getGuessHotWordsState.value = GetGuessHotWordsState.Fail(it.message)
                Log.e("zqr", "_getGuessHotWordsState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 搜索动态
     */
    var mSearchFeedBean: WaterfallFeedBean? = null
    private val _searchFeedBeanState = MutableLiveData<SearchFeedBeanState>()
    val searchFeedBeanState: MutableLiveData<SearchFeedBeanState> get() = _searchFeedBeanState
    fun getSearchFeed(
        key: String,//搜索词
        type: Int,//1-综合 2-最新 3-最热
        page: Int,//页码 第一页为1  后面传入接口返回的page字段值
        size: Int,//每页数量
        lastTime: Long?//分页用，传入接口返回的该值
    ) {
        val disposable = searchRepository.getSearchFeed(key, type, page, size, lastTime)
            .subscribe({
                mSearchFeedBean = Tool().toUrlWaterfallFeedBean(it,"0")
                _searchFeedBeanState.value = SearchFeedBeanState.Success
                Log.e("zqr", "_searchFeedBeanState请求成功：$it")
            }, {
                _searchFeedBeanState.value = SearchFeedBeanState.Fail(it.message)
                Log.e("zqr", "_searchFeedBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 搜索模糊匹配  viewModel
     */
    var mSearchWordBean: SearchWordBean? = null
    private val _getSearchWordBeanState = MutableLiveData<GetSearchWordBeanState>()
    val getSearchWordBeanState: MutableLiveData<GetSearchWordBeanState> get() = _getSearchWordBeanState
    fun searchWord(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?
    ) {
        val disposable = searchRepository.searchWord(key,page,size,lastTime)
            .subscribe({
                mSearchWordBean= it
                _getSearchWordBeanState.value = GetSearchWordBeanState.Success
                Log.e("zqr", "_getSearchWordBeanState请求成功：$it")
            }, {

                _getSearchWordBeanState.value = GetSearchWordBeanState.Fail(it.message)
                Log.e("zqr", "_getSearchWordBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }



}