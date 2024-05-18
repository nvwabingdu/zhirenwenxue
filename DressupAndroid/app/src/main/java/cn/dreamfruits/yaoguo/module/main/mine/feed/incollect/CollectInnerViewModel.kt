package cn.dreamfruits.yaoguo.module.main.mine.feed.incollect

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
class CollectInnerViewModel : BaseViewModel(){
    private val feedRepository by inject<FeedRepository>()
    private val collectRepository by inject<CollectRepository>()


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
     * 获取收藏列表  viewModel
     */
    var mGetCollectListBean: GetCollectListBean? = null
    private val _getCollectListState = MutableLiveData<GetCollectListState>()
    val getCollectListState: MutableLiveData<GetCollectListState> get() = _getCollectListState
    fun getCollectList(
        targetId: Long?,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        type: Long,//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
        size: Long,//每页数量
        lastTime: Long?//第一页不传，第二页开始使用上一页返回的lastTime
    ) {
        val disposable = collectRepository.getCollectList(targetId,type,size,lastTime)
            .subscribe({
                it.list.forEach {
                    it.feedPics.forEach {
                        it.url=Singleton.getUrlX(it.url,true,100)
                    }
                }
                mGetCollectListBean= it
                _getCollectListState.value = GetCollectListState.Success
                Log.e("zqr", "_getCollectListState请求成功：$it")
            }, {

                _getCollectListState.value = GetCollectListState.Fail(it.message)
                Log.e("zqr", "_getCollectListState请求失败：$it")
            })
        addDisposable(disposable)
    }



}