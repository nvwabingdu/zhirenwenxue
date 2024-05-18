package cn.dreamfruits.yaoguo.module.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.AttentionRepository
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.bean.attention.FollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.attention.UnfollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import cn.dreamfruits.yaoguo.repository.bean.feed.DeleteFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feed.LaudFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feed.UnLudFeedBean
import cn.dreamfruits.yaoguo.repository.datasource.CollectRepository
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/4/17
 * @TIME 14:57
 */
class CommonViewModel : BaseViewModel(){//公共的一些数据操作  比如点赞 收藏 后续再迁移
private val attentionRepository by inject<AttentionRepository>()
    private val collectRepository by inject<CollectRepository>()
    private val feedRepository by inject<FeedRepository>()


    /**
     * 关注用户  关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
     */
    var mFollowUserBean: FollowUserBean? = null
    private val _followUserBeanState = MutableLiveData<FollowUserBeanState>()
    val followUserBeanState: MutableLiveData<FollowUserBeanState> get() = _followUserBeanState
    fun getFollowUser(targetId: Long) {
        val disposable = attentionRepository.getFollowUser(targetId)
            .subscribe({
                mFollowUserBean=it
                _followUserBeanState.value = FollowUserBeanState.Success
                Log.e("zqr", "_followUserBeanState请求成功：$it")
            }, {

                _followUserBeanState.value = FollowUserBeanState.Fail(it.message)
                Log.e("zqr", "_followUserBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 取消关注用户
     */
    var mUnfollowUserBean: UnfollowUserBean? = null
    private val _unfollowUserBeanState = MutableLiveData<UnfollowUserBeanState>()
    val unfollowUserBeanState: MutableLiveData<UnfollowUserBeanState> get() = _unfollowUserBeanState
    fun getUnfollowUser(targetId: Long) {
        val disposable = attentionRepository.getUnfollowUser(targetId)
            .subscribe({
                mUnfollowUserBean=it
                _unfollowUserBeanState.value = UnfollowUserBeanState.Success
                Log.e("zqr", "_unfollowUserBeanState请求成功：$it")
            }, {

                _unfollowUserBeanState.value = UnfollowUserBeanState.Fail(it.message)
                Log.e("zqr", "_unfollowUserBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }



    /**
     * 点赞
     */
    var mLaudFeedBean: LaudFeedBean? = null
    private val _laudFeedBeanState = MutableLiveData<LaudFeedBeanState>()
    val laudFeedBeanState: MutableLiveData<LaudFeedBeanState> get() = _laudFeedBeanState
    fun getLaudFeed(feedId: Long) {
        val disposable = feedRepository.getLaudFeed(feedId)
            .subscribe({
                mLaudFeedBean=it

                _laudFeedBeanState.value = LaudFeedBeanState.Success
                Log.e("zqr", "_laudFeedBeanState请求成功：$it")
            }, {

                _laudFeedBeanState.value = LaudFeedBeanState.Fail(it.message)
                Log.e("zqr", "_laudFeedBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 取消点赞
     */
    var mUnLudFeedBean: UnLudFeedBean? = null
    private val _unLudFeedBeanState = MutableLiveData<UnLudFeedBeanState>()
    val unLudFeedBeanState: MutableLiveData<UnLudFeedBeanState> get() = _unLudFeedBeanState
    fun getUnLudFeed(feedId: Long) {
        val disposable = feedRepository.getUnLudFeed(feedId)
            .subscribe({
                mUnLudFeedBean=it

                _unLudFeedBeanState.value = UnLudFeedBeanState.Success
                Log.e("zqr", "_unLudFeedBeanState请求成功：$it")
            }, {

                _unLudFeedBeanState.value = UnLudFeedBeanState.Fail(it.message)
                Log.e("zqr", "_unLudFeedBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }




    /**
     * 收藏
     */
    var mCollectBean: CollectBean? = null
    private val _collectBeanState = MutableLiveData<CollectBeanState>()
    val collectBeanState: MutableLiveData<CollectBeanState> get() = _collectBeanState
    fun getCollect( type: Long,targetId: Long,outfitStr: String?) {
        val disposable = collectRepository.getCollect(type,targetId,outfitStr)
            .subscribe({
                mCollectBean=it

                _collectBeanState.value = CollectBeanState.Success
                Log.e("zqr", "_collectBeanState请求成功：$it")
            }, {

                _collectBeanState.value = CollectBeanState.Fail(it.message)
                Log.e("zqr", "_collectBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 取消收藏
     */
    var mUnCollectBean: UncollectBean? = null
    private val _unCollectBeanState = MutableLiveData<UnCollectBeanState>()
    val unCollectBeanState: MutableLiveData<UnCollectBeanState> get() = _unCollectBeanState
    fun getUnCollect( targetId: Long,type: Long) {
        val disposable = collectRepository.getUnCollect(targetId,type)
            .subscribe({
                mUnCollectBean=it

                _unCollectBeanState.value = UnCollectBeanState.Success
                Log.e("zqr", "_unCollectBeanState请求成功：$it")
            }, {

                _unCollectBeanState.value = UnCollectBeanState.Fail(it.message)
                Log.e("zqr", "_unCollectBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 删除动态  viewModel
     */
    var mDeleteFeedBean: DeleteFeedBean? = null
    private val _deleteFeedState = MutableLiveData<DeleteFeedState>()
    val deleteFeedState: MutableLiveData<DeleteFeedState> get() = _deleteFeedState
    fun deleteFeed(
        id: Long,//目标id 必须
    ) {
        val disposable = feedRepository.deleteFeed(id)
            .subscribe({
                mDeleteFeedBean= it
                _deleteFeedState.value = DeleteFeedState.Success

                Log.e("zqr", "_deleteFeedState请求成功：$it")
            }, {

                _deleteFeedState.value = DeleteFeedState.Fail(it.message)
                Log.e("zqr", "_deleteFeedState请求失败：$it")
            })
        addDisposable(disposable)
    }


}