package cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.AttentionRepository
import cn.dreamfruits.yaoguo.repository.FeedRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentDetailBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.datasource.CommentRepository
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/5/3
 * @TIME 18:33
 */
class CommentViewModel : BaseViewModel(){
    private val commentRepository by inject<CommentRepository>()
    private val feedRepository by inject<FeedRepository>()

    private val searchRepository by inject<SearchRepository>()
    private val attentionRepository by inject<AttentionRepository>()


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
     * 获取评论列表
     */
    var mCommentListBean: CommentBean? = null
    private val _commentListBeanState = MutableLiveData<CommentListBeanState>()
    val commentListBeanState: MutableLiveData<CommentListBeanState> get() = _commentListBeanState
    fun getCommentList( targetId: Long,type: Int,size: Long,lastTime: Long?,ids: String?,commentIds: String?) {
        val disposable = commentRepository.getCommentList(targetId, type, size, lastTime, ids,commentIds)
            .subscribe({
                mCommentListBean= Tool().toUrlCommentListBean(it,"0")

                _commentListBeanState.value = CommentListBeanState.Success
                Log.e("zqr", "_commentListBeanState请求成功：$it")
            }, {

                _commentListBeanState.value = CommentListBeanState.Fail(it.message)
                Log.e("zqr", "_commentListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 发布评论
     */
    var mCommentPublishBean: CommentBean.Item? = null
    private val _commentPublishBeanState = MutableLiveData<CommentPublishBeanState>()
    val commentPublishBeanState: MutableLiveData<CommentPublishBeanState> get() = _commentPublishBeanState
    fun getPublishComment(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        content: String,//评论内容
        replyId: Long?,//回复的评论id
        atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        config: String//配置字符串
    ) {
        val disposable =
            commentRepository.getPublishComment(targetId, type, content, replyId, atUser, config)
                .subscribe({
                    mCommentPublishBean = Tool().toUrlCommentBeanItemBean(it,"0")
                    _commentPublishBeanState.value = CommentPublishBeanState.Success
                    Log.e("zqr", "_commentPublishBeanState请求成功：$it")
                }, {

                    _commentPublishBeanState.value = CommentPublishBeanState.Fail(it.message)
                    Log.e("zqr", "_commentPublishBeanState请求失败：$it")
                })
        addDisposable(disposable)
    }



    /**
     * ==========================获取评论列表 子级
     */
    var mChildCommentBean: ChildCommentBean? = null
    private val _childCommentBeanState = MutableLiveData<ChildCommentBeanState>()
    val childCommentBeanState: MutableLiveData<ChildCommentBeanState> get() = _childCommentBeanState
    fun getCommentListChild( targetId: Long,type: Int,size: Long,lastTime: Long?,ids: String?) {
        val disposable = commentRepository.getCommentListChild(targetId, type, size, lastTime, ids)
            .subscribe({

                mChildCommentBean= Tool().toUrlChildCommentListBean(it,"0")

                _childCommentBeanState.value = ChildCommentBeanState.Success
                Log.e("zqr", "_commentListBeanState请求成功：$it")
            }, {

                _childCommentBeanState.value = ChildCommentBeanState.Fail(it.message)
                Log.e("zqr", "_commentListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * ==============================发布评论 子级
     */
    var mChildCommentPublishBean: ChildCommentBean.ChildItem? = null
    private val _commentChildPublishBeanState = MutableLiveData<CommentChildPublishBeanState>()
    val commentChildPublishBeanState: MutableLiveData<CommentChildPublishBeanState> get() = _commentChildPublishBeanState
    fun getPublishCommentChild(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        content: String,//评论内容
        replyId: Long?,//回复的评论id
        atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        config: String//配置字符串
    ) {
        val disposable = commentRepository.getPublishCommentChild(targetId, type, content, replyId, atUser, config)
                .subscribe({
                    mChildCommentPublishBean = Tool().toUrlChildCommentBeanChildItem(it,"0")
                    _commentChildPublishBeanState.value = CommentChildPublishBeanState.Success
                    Log.e("zqr", "_commentPublishBeanState请求成功：$it")
                }, {

                    _commentChildPublishBeanState.value = CommentChildPublishBeanState.Fail(it.message)
                    Log.e("zqr", "_commentPublishBeanState请求失败：$it")
                })
        addDisposable(disposable)
    }



    /**
     * 第一次显示@用户 为请求@用户  获取at列表
     */
    var mAtUserListBean: SearchUserBean? = null //用搜索用户同一个结构
    private val _atUserListBeanState = MutableLiveData<AtUserListBeanState>()
    val atUserListBeanState: MutableLiveData<AtUserListBeanState> get() = _atUserListBeanState
    fun getAtUserList(
        size: Int,// 大于等于1
        lastTime: Long,//第一页不传，第二页开始使用上一页返回的lastTime    api上面是number类型？？？
        withoutIds: String?//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
    ) {
        val disposable =
            attentionRepository.getAtUserList(size, lastTime, withoutIds)
                .subscribe({
                    mAtUserListBean = it
                    _atUserListBeanState.value = AtUserListBeanState.Success
                    Log.e("zqr", "_atUserListBeanState请求成功：$it")
                }, {

                    _atUserListBeanState.value = AtUserListBeanState.Fail(it.message)
                    Log.e("zqr", "_atUserListBeanState请求失败：$it")
                })
        addDisposable(disposable)
    }

    /**
     * 用于评论时  输入@后  搜索用户
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
                mSearchUserBean = it
                _searchUserBeanState.value = SearchUserBeanState.Success
                Log.e("zqr", "_searchUserBeanState请求成功：$it")
            }, {

                _searchUserBeanState.value = SearchUserBeanState.Fail(it.message)
                Log.e("zqr", "_searchUserBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }




    /**
     * 点赞评论
     */
    private val _laudCommentState = MutableLiveData<LaudCommentState>()
    val laudCommentState: MutableLiveData<LaudCommentState> get() = _laudCommentState
    fun getLaudComment( id: Long) {
        val disposable = commentRepository.getLaudComment(id)
            .subscribe({
                _laudCommentState.value = LaudCommentState.Success
                Log.e("zqr", "_laudCommentState请求成功：$it")
            }, {

                _laudCommentState.value = LaudCommentState.Fail(it.message)
                Log.e("zqr", "_laudCommentState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 取消点赞评论
     */
    private val _unLaudCommentState = MutableLiveData<UnLaudCommentState>()
    val unLaudCommentState: MutableLiveData<UnLaudCommentState> get() = _unLaudCommentState
    fun getUnLaudComment( id: Long) {
        val disposable = commentRepository.getUnLaudComment(id)
            .subscribe({
                _unLaudCommentState.value = UnLaudCommentState.Success
                Log.e("zqr", " _unLaudCommentState请求成功：$it")
            }, {

                _unLaudCommentState.value = UnLaudCommentState.Fail(it.message)
                Log.e("zqr", " _unLaudCommentState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 删除评论
     */
    private val _deleteCommentState = MutableLiveData<DeleteCommentState>()
    val deleteCommentState: MutableLiveData<DeleteCommentState> get() = _deleteCommentState
    fun getDeleteComment( commentId: String) {
        val disposable = commentRepository.getDeleteComment(commentId)
            .subscribe({
                _deleteCommentState.value = DeleteCommentState.Success
                Log.e("zqr", " _deleteCommentState请求成功：$it")
            }, {

                _deleteCommentState.value = DeleteCommentState.Fail(it.message)
                Log.e("zqr", " _deleteCommentState请求失败：$it")
            })
        addDisposable(disposable)
    }
}