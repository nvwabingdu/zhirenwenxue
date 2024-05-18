package cn.dreamfruits.yaoguo.module.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.HomeRecommendLabelListState
import cn.dreamfruits.yaoguo.module.main.home.state.HomeScrollHotWordsState
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchScrollHotWordsBean
import cn.dreamfruits.yaoguo.repository.LabelRepository
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/3/20
 * @TIME 11:32
 */
class HomeViewModel  : BaseViewModel(){
    private val searchRepository by inject<SearchRepository>()
    private val feedRepository by inject<FeedRepository>()
    private val labelRepository by inject<LabelRepository>()




    /**
     * 获取滚动词接口
     */
    var mSearchScrollHotWordsBean: SearchScrollHotWordsBean? = null
    private val _homeScrollHotWordsState = MutableLiveData<HomeScrollHotWordsState>()
    val homeScrollHotWordsState: MutableLiveData<HomeScrollHotWordsState> get() = _homeScrollHotWordsState
    fun getScrollHotWords(size: Int) {
        val disposable = searchRepository.getScrollHotWords(size)
            .subscribe({

                mSearchScrollHotWordsBean=it

                _homeScrollHotWordsState.value = HomeScrollHotWordsState.Success
                Log.e("zqr", "_homeScrollHotWordsState请求成功：$it")
            }, {
                _homeScrollHotWordsState.value = HomeScrollHotWordsState.Fail(it.message)
                Log.e("zqr", "_homeScrollHotWordsState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 首页tab-推荐话题
     */
    var mGetRecommendLabelListBean: GetRecommendLabelListBean? = null
    private val _getRecommendLabelListBean = MutableLiveData<HomeRecommendLabelListState>()
    val getRecommendLabelListBean: MutableLiveData<HomeRecommendLabelListState> get() = _getRecommendLabelListBean
    fun getRecommendLabelList(count: Int) {
        val disposable = labelRepository.getRecommendLabelList2(count)
            .subscribe({
                mGetRecommendLabelListBean=it

                _getRecommendLabelListBean.value = HomeRecommendLabelListState.Success
                Log.e("zqr", "_getRecommendLabelListBean请求成功：$it")
            }, {

                _getRecommendLabelListBean.value = HomeRecommendLabelListState.Fail(it.message)
                Log.e("zqr", "_getRecommendLabelListBean请求失败：$it")
            })
        addDisposable(disposable)
    }

}