package cn.dreamfruits.yaoguo.module.main.home.postdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentDetailBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.datasource.CommentRepository
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/4/8
 * @TIME 18:28
 */
class PostDetailsViewModel : BaseViewModel(){
    private val commentRepository by inject<CommentRepository>()
    private val feedRepository by inject<FeedRepository>()

    /**
     * 获取关注动态列表
     */
    var mCommentDetailBean: CommentDetailBean? = null
    private val _commentDetailBeanState = MutableLiveData<CommentDetailBeanState>()
    val commentDetailBeanState: MutableLiveData<CommentDetailBeanState> get() = _commentDetailBeanState
    fun getCommentDetail( commentId: Long,ids: String) {
        val disposable = commentRepository.getCommentDetail(commentId,ids)
            .subscribe({
                mCommentDetailBean= it

                _commentDetailBeanState.value = CommentDetailBeanState.Success
                Log.e("zqr", "_commentDetailBeanState请求成功：$it")
            }, {

                _commentDetailBeanState.value = CommentDetailBeanState.Fail(it.message)
                Log.e("zqr", "_commentDetailBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 获取帖子详情
     */
    var mFeedDetailsBean: WaterfallFeedBean.Item.Info? = null
    private val _feedDetailsBeanState = MutableLiveData<FeedDetailsBeanState>()
    val feedDetailsBeanState: MutableLiveData<FeedDetailsBeanState> get() = _feedDetailsBeanState
    fun getFeedDetail( id: Long) {
        val disposable = feedRepository.getFeedDetail(id)
            .subscribe({
                mFeedDetailsBean= Tool().toUrlFeedDetailsBean(it,"0")

                _feedDetailsBeanState.value = FeedDetailsBeanState.Success
                Log.e("zqr", "_feedDetailsBeanState请求成功：$it")
            }, {

                _feedDetailsBeanState.value = FeedDetailsBeanState.Fail(it.message)
                Log.e("zqr", "_feedDetailsBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

}