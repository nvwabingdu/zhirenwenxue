package cn.dreamfruits.yaoguo.module.main.home.child

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.*
import cn.dreamfruits.yaoguo.repository.bean.attention.FollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.attention.UnfollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import cn.dreamfruits.yaoguo.repository.bean.feed.*
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.UninterestedBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.find.IgnoreRecommendUserBean
import cn.dreamfruits.yaoguo.repository.bean.label.GetLabelListBean
import cn.dreamfruits.yaoguo.repository.datasource.CollectRepository
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/3/11
 * @TIME 15:29
 */
class FeedViewModel : BaseViewModel(){

    private val feedBackRepository by inject<FeedBackRepository>()
    private val feedRepository by inject<FeedRepository>()

    private val findRepository by inject<FindRepository>()




    /**
     * 获取关注动态列表
     */
     var mGetCareFeedListBean: WaterfallFeedBean? = null
    private val _homeGetCareFeedListState = MutableLiveData<HomeGetCareFeedListState>()
    val homeGetCareFeedListState: MutableLiveData<HomeGetCareFeedListState> get() = _homeGetCareFeedListState
    fun getCareFeedList(size: Int,lastTime: Long) {
        val disposable = feedRepository.getCareFeedList(size,lastTime)
            .subscribe({
                mGetCareFeedListBean=Tool().toUrlWaterfallFeedBean(it,"0")

                _homeGetCareFeedListState.value = HomeGetCareFeedListState.Success
                Log.e("zqr", "_homeGetCareFeedListState请求成功：$it")
            }, {

                _homeGetCareFeedListState.value = HomeGetCareFeedListState.Fail(it.message)
                Log.e("zqr", "_homeGetCareFeedListState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 感兴趣的人
     */
     var mHomeRecommendUserListBean: HomeRecommendUserListBean? = null
    private val _homeRecommendUserListState = MutableLiveData<HomeRecommendUserListState>()
    val homeRecommendUserListState: MutableLiveData<HomeRecommendUserListState> get() = _homeRecommendUserListState
    fun getHomeRecommendUserList(size: Int) {
        val disposable = findRepository.getHomeRecommendUserList(size)
            .subscribe({
                //取得的集合需要对图片地址处理鉴权，1、这里需要处理， 2、另外需要在对应的本地仓库处理 此处不做缓存不用处理
                mHomeRecommendUserListBean=Tool().toUrlHomeRecommendUserListBean(it,"0")


                _homeRecommendUserListState.value = HomeRecommendUserListState.Success
                Log.e("zqr", "_homeRecommendUserListState请求成功：$it")
            }, {

                _homeRecommendUserListState.value = HomeRecommendUserListState.Fail(it.message)
                Log.e("zqr", "_homeRecommendUserListState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 忽略感兴趣的人
     */
     var mIgnoreRecommendUserBean: IgnoreRecommendUserBean? = null
    private val _ignoreRecommendUserState = MutableLiveData<IgnoreRecommendUserState>()
    val ignoreRecommendUserState: MutableLiveData<IgnoreRecommendUserState> get() = _ignoreRecommendUserState
    fun ignoreRecommendUser(id: Long, type: Int) {
        val disposable = findRepository.ignoreRecommendUser(id,type)
            .subscribe({
                mIgnoreRecommendUserBean=it


                _ignoreRecommendUserState.value = IgnoreRecommendUserState.Success
                Log.e("zqr", "_ignoreRecommendUserState请求成功：$it")
            }, {

                _ignoreRecommendUserState.value = IgnoreRecommendUserState.Fail(it.message)
                Log.e("zqr", "_ignoreRecommendUserState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取用户动态列表  viewModel
     */
    var mGetUserFeedListBean: WaterfallFeedBean? = null
    private val _getUserFeedListState = MutableLiveData<GetUserFeedListState>()
    val getUserFeedListState: MutableLiveData<GetUserFeedListState> get() = _getUserFeedListState
    fun getUserFeedList(

        type: Int,//0-用户动态列表 1-用户收藏动态列表 2-用户浏览过得动态列表
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        size: Int,
        lastTime: Long?

    ) {
        val disposable = feedRepository.getUserFeedList(type,targetId,size,lastTime)
            .subscribe({
                mGetUserFeedListBean=Tool().toUrlWaterfallFeedBean(it,"0")
                _getUserFeedListState.value = GetUserFeedListState.Success
                Log.e("zqr", "_getUserFeedListState请求成功：$it")
            }, {

                _getUserFeedListState.value = GetUserFeedListState.Fail(it.message)
                Log.e("zqr", "_getUserFeedListState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     *获取动态列表
     */
     var mWaterfallFeedBean: WaterfallFeedBean? = null
    private val _homeRecommendFeedListState = MutableLiveData<HomeRecommendFeedListState>()
    val homeRecommendFeedListState: MutableLiveData<HomeRecommendFeedListState> get() = _homeRecommendFeedListState
    fun getRecommendFeedList(size: Int,type:Int) {
        val disposable = feedRepository.getRecommendFeedList(size,type)
            .subscribe({
                //取得的集合需要对图片地址处理鉴权，1、这里需要处理， 2、另外需要在对应的本地仓库处理
                mWaterfallFeedBean=Tool().toUrlWaterfallFeedBean(it,"0")

                _homeRecommendFeedListState.value = HomeRecommendFeedListState.Success
                Log.e("zqr", "_homeRecommendFeedListState请求成功：$it")
            }, {

                _homeRecommendFeedListState.value = HomeRecommendFeedListState.Fail(it.message)
                Log.e("zqr", "_homeRecommendFeedListState请求失败：$it")
            })
        addDisposable(disposable)
    }




    /**
     * 获取话题动态列表
     */
    var mGetLabelListBean: WaterfallFeedBean? = null
    private val _homeGetLabelFeedListState = MutableLiveData<HomeGetLabelFeedListState>()
    val homeGetLabelFeedListState: MutableLiveData<HomeGetLabelFeedListState> get() = _homeGetLabelFeedListState
    fun getLabelFeedList(labelId: Long, size: Int, lastTime: Long) {
        val disposable = feedRepository.getLabelFeedList(labelId,size,lastTime)
            .subscribe({
                mGetLabelListBean=Tool().toUrlWaterfallFeedBean(it,"0")

                _homeGetLabelFeedListState.value = HomeGetLabelFeedListState.Success
                Log.e("zqr", "_homeGetLabelFeedListState请求成功：$it")
            }, {

                _homeGetLabelFeedListState.value = HomeGetLabelFeedListState.Fail(it.message)
                Log.e("zqr", "_homeGetLabelFeedListState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 动态反馈
     */
     var mFeedbackFeedBean: FeedbackFeedBean? = null
    private val _homeFeedbackFeedState = MutableLiveData<HomeFeedbackFeedState>()
    val homeFeedbackFeedState: MutableLiveData<HomeFeedbackFeedState> get() = _homeFeedbackFeedState
    fun getFeedbackFeed(feedId: Long, type: Int) {
        val disposable = feedBackRepository.getFeedbackFeed(feedId,type)
            .subscribe({
                mFeedbackFeedBean=it

                _homeFeedbackFeedState.value = HomeFeedbackFeedState.Success
                Log.e("zqr", "_homeFeedbackFeedState请求成功：$it")
            }, {

                _homeFeedbackFeedState.value = HomeFeedbackFeedState.Fail(it.message)
                Log.e("zqr", "_homeFeedbackFeedState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 不感兴趣
     */
     var mUninterestedBean: UninterestedBean? = null
    private val _homeUninterestedState = MutableLiveData<HomeUninterestedState>()
    val homeUninterestedState: MutableLiveData<HomeUninterestedState> get() = _homeUninterestedState
    fun getUninterested(targetId: Long, type: Int) {
        val disposable = feedBackRepository.getUninterested(targetId,type)
            .subscribe({
                mUninterestedBean=it

                _homeUninterestedState.value = HomeUninterestedState.Success
                Log.e("zqr", "_homeUninterestedState请求成功：$it")
            }, {

                _homeUninterestedState.value = HomeUninterestedState.Fail(it.message)
                Log.e("zqr", "_homeUninterestedState请求失败：$it")
            })
        addDisposable(disposable)
    }







}