//package cn.dreamfruits.yaoguo.util
//
//import android.app.Activity
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.lifecycle.LifecycleOwner
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import cn.dreamfruits.yaoguo.R
//import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView
//import cn.dreamfruits.yaoguo.module.main.home.commentreplay.AtUserAdapter
//import cn.dreamfruits.yaoguo.module.main.home.commentreplay.ShowKeyboardAndPop
//import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.ChildCommentListAdapter
//import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentListAdapter
//import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentViewModel
//import cn.dreamfruits.yaoguo.module.main.home.state.*
//import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
//import cn.dreamfruits.yaoguo.util.Singleton.isLoadMore
//import cn.dreamfruits.yaoguo.util.Singleton.isLoadMoreSucceed
//import cn.dreamfruits.yaoguo.util.Singleton.isNoMoreData
//import cn.dreamfruits.yaoguo.util.Singleton.mFirstCommentList
//import cn.dreamfruits.yaoguo.util.Singleton.mFirstPosition
//import com.example.dragpopwindow.CustomDragPop
//import com.example.dragpopwindow.MyRelativeLayout
//import com.scwang.smart.refresh.footer.ClassicsFooter
//import com.scwang.smart.refresh.layout.api.RefreshLayout
//import java.util.ArrayList
//
///**
// * @Author qiwangi
// * @Date 2023/5/10
// * @TIME 09:40
// */
//class CommentUtils {
//    /**
//     * =================================================评论弹窗相关逻辑  改为通过广播实现的底部布局
//     */
//    fun showDragPopComment(mActivity: Activity, feedId: Long, mLifecycleOwner: LifecycleOwner, commentViewModel: CommentViewModel){
//        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dragpop_dialog_comment, null)
//        var commentDefaultLayout = inflate.findViewById<View>(R.id.comment_default_layout) //评论缺省
//        var commentEnd= inflate.findViewById<View>(R.id.comment_end)//末尾标记
//        var mRecyclerview = inflate.findViewById<MaxRecyclerView>(R.id.comment_recyclerview) //评论集合
////        val commentViewModel by viewModels<CommentViewModel>()
//        var mShowKeyboardAndPop = ShowKeyboardAndPop()//获取其实例  重要对象
//        var refreshLayout: RefreshLayout = inflate.findViewById(R.id.refreshLayout)//刷新控件
//
//        //评论 初始化
//        initViewComment(refreshLayout,commentDefaultLayout!!,mRecyclerview!!,commentEnd!!,feedId,mLifecycleOwner, mActivity,commentViewModel,mShowKeyboardAndPop)
//
//        //请求
//        getCommentList(feedId,commentViewModel, commentDefaultLayout, commentEnd)
//
//        val popReLayout=inflate.findViewById<RelativeLayout>(R.id.pop_re_layout)
//        val popMyRelativeLayout=inflate.findViewById<MyRelativeLayout>(R.id.pop_MyRelativeLayout)
//        val commentBottomPart=inflate.findViewById<LinearLayout>(R.id.comment_bottom_part)
//        commentBottomPart.visibility= View.VISIBLE//显示底部评论输入框
//
//        val popReplyTv=inflate.findViewById<TextView>(R.id.pop_reply_tv)
//        popReplyTv.setOnClickListener {
//            //帖子发布评论
//            mShowKeyboardAndPop.showKeyboard(mActivity, mActivity, feedId, 0,"",0,false)
//        }
//
//        var  mCustomDragPop = CustomDragPop(mActivity,
//            inflate,
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            popReLayout,
//            popMyRelativeLayout,
//            mRecyclerview)
//
//        //点击关闭
//        val popCloseImgView=inflate.findViewById<ImageView>(R.id.pop_close)
//        popCloseImgView.setOnClickListener {
//            //销毁弹出框
//            mCustomDragPop.dismiss()
//        }
//
//
//        //动画
//        mCustomDragPop.animationStyle= R.style.BottomDialogAnimation
//        if (mCustomDragPop.isShowing) {//如果正在显示，关闭弹窗。
//            mCustomDragPop.dismiss()
//        } else {
//            mCustomDragPop.isOutsideTouchable = true
//            mCustomDragPop.isTouchable = true
//            mCustomDragPop.isFocusable = true
//            mCustomDragPop.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
//            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
//            mCustomDragPop.setOnDismissListener {
//                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
//            }
//        }
//
//        //在pause时关闭
//        Singleton.lifeCycleSet(mActivity, mCustomDragPop)
//    }
//
//    /**
//     * =================================================================评论
//     * 初始化
//     */
//    fun initViewComment(refreshLayout: RefreshLayout, commentDefaultLayout: View, mRecyclerview: MaxRecyclerView, commentEnd: View, feedId: Long, mLifecycleOwner: LifecycleOwner, mActivity: Activity, commentViewModel: CommentViewModel, mShowKeyboardAndPop: ShowKeyboardAndPop){
//        var commentListAdapter = CommentListAdapter(mActivity, mFirstCommentList)//评论适配器
//        mRecyclerview.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
//        val mLinearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
//        mRecyclerview.layoutManager = mLinearLayoutManager
//        mRecyclerview.adapter = commentListAdapter//设置适配器
//
//        /**
//         * 刷新控件
//         */
//        refreshLayout.setRefreshFooter(ClassicsFooter(mActivity))
//        refreshLayout.setEnableRefresh(false)//不用下拉刷新
//        //只用加载方法
//        refreshLayout.setOnLoadMoreListener { refreshLayout ->
//            //设置标签为加载（不刷新）
//            isLoadMore = true
//
//            //2.请求
//            getCommentList(feedId,commentViewModel, commentDefaultLayout, commentEnd)
//
//            //4.根据返回结果设置加载结果
//            if (isNoMoreData) {//没有更多数据了
//                refreshLayout.finishLoadMoreWithNoMoreData()//显示全部加载完成，并不再触发加载更事件
//            } else {
//                refreshLayout.finishLoadMore(isLoadMoreSucceed)
//            }
//        }
//
//        /**
//         * 评论适配器回调
//         */
//        commentListAdapter.setCommentCallBack(object : CommentListAdapter.CommentInterface{
//            //1.点赞回调
//            override fun onLaudClick(isLaud: Boolean, id: Long) {
//                if (isLaud){
//                    commentViewModel.getLaudComment(id)//点赞
//                }else{
//                    commentViewModel.getUnLaudComment(id)//取消点赞
//                }
//            }
//            //2.点击item 长按item 回调
//            override fun onItemClick(isLongClick: Boolean, Id: Long, isChild: Boolean, replyUser: String, replyId: Long, replyContent: String, mChildCommentListAdapter: ChildCommentListAdapter?, position: Int) {
//                mFirstPosition = position
//
//                if (isLongClick) {//长按
//                    commonPopup(mActivity,Id, replyUser, replyId, replyContent,mShowKeyboardAndPop)//弹出举报 回复等弹窗
//                } else {//短按
//                    //评论的评论的评论  3.点击子级item的弹窗
//                    mShowKeyboardAndPop.showKeyboard(mActivity, mActivity,Id, 1, "回复 @${replyUser}:", replyId,true)
//                }
//            }
//
//            //3.父评论暂无操作    子评论展开 收起回调
//            override fun onLoadMoreClick(ids:String,position:Int) {
//                mFirstPosition=position//角标赋值
//                getChildCommentList(ids,mFirstCommentList,commentViewModel)//子级接口请求  展开更多   这里回来的就是子级的点击   展开逻辑   收起逻辑
//            }
//        })
//
//        /**
//         * 点赞请求结果
//         */
//        commentViewModel.laudCommentState.observe(mLifecycleOwner){
//            when (it) {
//                is LaudCommentState.Success -> {
//
//                }
//                is LaudCommentState.Fail->{
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//
//        /**
//         * 取消点赞请求结果
//         */
//        commentViewModel.unLaudCommentState.observe(mLifecycleOwner){
//            when (it) {
//                is UnLaudCommentState.Success -> {
//
//                }
//                is UnLaudCommentState.Fail->{
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//
//        //评论列表请求返回注册
//        commentList(feedId,mRecyclerview,commentDefaultLayout,mShowKeyboardAndPop,mLifecycleOwner, mActivity, commentListAdapter, commentViewModel)
//        /**
//         * 艾特用户逻辑
//         */
//        commentAtUser(mLifecycleOwner,mActivity,mShowKeyboardAndPop,commentViewModel)
//
//        /**
//         * 发布评论逻辑
//         */
//        commentPublish(commentDefaultLayout,mRecyclerview,feedId,mLifecycleOwner,mShowKeyboardAndPop,mActivity,mFirstCommentList,commentListAdapter,commentViewModel)
//    }
//
//
//    /**
//     * @用户
//     */
//    private fun commentAtUser(mLifecycleOwner: LifecycleOwner, mActivity: Activity, mShowKeyboardAndPop: ShowKeyboardAndPop, commentViewModel: CommentViewModel){
//        var tempAtUserAdapter: AtUserAdapter?=null
//
//        //at用户适配器
//        var isAtUserFirst=false
//        var isSearchUserFirst=false
//
//        //请求艾特用户接口  回调
//        mShowKeyboardAndPop.setSearchUserClickListener(object : ShowKeyboardAndPop.SearchUserInterface {
//            override fun onclick(
//                atUserAdapter: AtUserAdapter,
//                isGetAtUserList: Boolean?,
//                key: String,
//                isKeyChange: Boolean,//所搜索的内容是否变化
//                atUser: String?,
//                config: String?
//            ) {
//
//                tempAtUserAdapter = atUserAdapter//取到适配器实例
//
//                if (isGetAtUserList == true) {//请求at用户列表
//                    if (isKeyChange) {
//                        isAtUserFirst = true
//                        commentViewModel.getAtUserList(10, 0, null)
//                    } else {
//                        isAtUserFirst = false
//                        commentViewModel.getAtUserList(10, commentViewModel.mAtUserListBean!!.lastTime, atUser!!)//加载更多
//                    }
//                    //分页
//                } else {//请求搜索用户接口
//                    if (isKeyChange) {//请求词变化了  页码不变  集合清空  设置适配器
//                        isSearchUserFirst = true
//                        commentViewModel.getSearchUser(
//                            key,//搜索词
//                            1,//页码，第一次穿1，后面传入上一次接口返回得page
//                            10,//数量
//                            null,//用于分页，传入上一次接口返回lastTime
//                            null//过滤的用户的ids，例如：id1,id2,id3
//                        )
//                    } else {//请求词没有变  说明需要继续请求  页码使用上次的页码
//                        isSearchUserFirst = false
//                        commentViewModel.getSearchUser(
//                            key,//搜索词
//                            commentViewModel.mSearchUserBean!!.page,//页码，第一次穿1，后面传入上一次接口返回得page
//                            10,//数量
//                            commentViewModel.mSearchUserBean!!.lastTime,//用于分页，传入上一次接口返回lastTime
//                            atUser//过滤的用户的ids，例如：id1,id2,id3
//                        )
//                    }
//                }
//            }
//        })
//
//
//        //请求at用户列表 接口返回
//        commentViewModel.atUserListBeanState.observe(mLifecycleOwner) { it ->
//            when (it) {
//                is AtUserListBeanState.Success -> {
//                    tempAtUserAdapter!!.setData(commentViewModel.mAtUserListBean!!.list.toMutableList(), isAtUserFirst)
//                }
//                is AtUserListBeanState.Fail -> {
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//
//        //请求搜索用户接口  接口返回
//        commentViewModel.searchUserBeanState.observe(mLifecycleOwner) { it ->
//            when (it) {
//                is SearchUserBeanState.Success -> {
//                    tempAtUserAdapter!!.setData(commentViewModel.mSearchUserBean!!.list.toMutableList(), isSearchUserFirst)
//                }
//                is SearchUserBeanState.Fail -> {
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//    }
//
//    /**
//     * =================================================================请求评论列表：帖子评论通过加载    子级评论通过点击加载更多
//     *
//    id,//动态id，一级评论id
//    0,//0-动态 1-评论
//    20,//每页条数
//    null,//第一次请求传个0
//    null//排除的id，例如：11111111,22222222 //随便传
//     */
//    fun getCommentList(id: Long, commentViewModel: CommentViewModel, commentDefaultLayout: View, commentEnd: View) {
//        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
//        if (commentViewModel.mCommentListBean == null) {//等于空 说明没有请求过，发出第一次请求
//            commentViewModel.getCommentList(id, 0, 20, null, null)
//        } else if (commentViewModel.mCommentListBean != null && commentViewModel.mCommentListBean!!.hasNext == 1) {
//            commentViewModel.getCommentList(id, 0, 20, commentViewModel.mCommentListBean!!.lastTime, null)
//        } else if (commentViewModel.mCommentListBean != null && commentViewModel.mCommentListBean!!.hasNext == 0) {//表示没有更多数据了
//            isNoMoreData = true
//            if (commentDefaultLayout.visibility== View.VISIBLE){
//                commentEnd.visibility= View.GONE
//            }else{
//                commentEnd.visibility= View.VISIBLE
//            }
//        }
//    }
//
//    //子评论 通过展开按钮
//    fun getChildCommentList(ids:String, mFirstCommentList: MutableList<CommentBean.Item>, commentViewModel: CommentViewModel){
//        if (mFirstCommentList[mFirstPosition].lastTimeChild == 0L) {//等于0 说明没有请求过，发出第一次请求   第一次必然为零  当时间戳为零的时候
//            commentViewModel.getCommentListChild(mFirstCommentList[mFirstPosition].id, 1, 5, null, ids)
//        } else if (mFirstCommentList[mFirstPosition].hasNextChild == 1) {
//            commentViewModel.getCommentListChild(mFirstCommentList[mFirstPosition].id, 1, 5, mFirstCommentList[mFirstPosition].lastTimeChild, ids)
//        } else if (mFirstCommentList[mFirstPosition].hasNextChild == 0) {//表示没有更多数据了
//            //没有数据了就设置
//            mFirstCommentList[mFirstPosition].isChildNoMoreData=true
//        }
//    }
//
//    //评论列表加载
//    private fun commentList(feedId:Long, mRecyclerview: RecyclerView, commentDefaultLayout: View, mShowKeyboardAndPop: ShowKeyboardAndPop, mLifecycleOwner: LifecycleOwner, mActivity: Activity, commentListAdapter: CommentListAdapter, commentViewModel: CommentViewModel){
//        //帖子评论列表
//        commentViewModel.commentListBeanState.observe(mLifecycleOwner) {
//            when (it) {
//                is CommentListBeanState.Success -> {
//
//                    Log.e("TAG121", "mFirstCommentList0: "+commentViewModel.mCommentListBean!!.list.size )
//                    if (mFirstCommentList.size == 0) {//说明之前没有加载数据
//                        mFirstCommentList=commentViewModel.mCommentListBean!!.list
//                        commentListAdapter.setData(mFirstCommentList, true)
//                        Log.e("TAG121", "mFirstCommentList1: "+mFirstCommentList.size )
//                    } else {//有数据
//                        commentListAdapter.setData(commentViewModel.mCommentListBean!!.list, false)//把装好的临时集合设置进去
//                    }
//                    Log.e("TAG121", "mFirstCommentList2: "+mFirstCommentList.size )
//                    //2.是否显示缺省
//                    isShowDefault(feedId,mActivity,mFirstCommentList,mRecyclerview,commentDefaultLayout,mShowKeyboardAndPop)
//                    //3.状态初始化
//                    isLoadMore = false
//                    isLoadMoreSucceed = true
//                }
//                is CommentListBeanState.Fail -> {
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                    //2.是否显示缺省
//                    isShowDefault(feedId,mActivity,mFirstCommentList,mRecyclerview,commentDefaultLayout,mShowKeyboardAndPop)
//                    //3.状态初始化
//                    isLoadMore = false
//                    isLoadMoreSucceed = false
//                }
//            }
//        }
//
//        //子级评论列表
//        commentViewModel.childCommentBeanState.observe(mLifecycleOwner) {
//            when (it) {
//                is ChildCommentBeanState.Success -> {
//                    mFirstCommentList[mFirstPosition].hasNextChild=commentViewModel.mChildCommentBean!!.hasNext
//                    mFirstCommentList[mFirstPosition].lastTimeChild=commentViewModel.mChildCommentBean!!.lastTime
//                    mFirstCommentList[mFirstPosition].totalCountChild=commentViewModel.mChildCommentBean!!.totalCount
//                    mFirstCommentList[mFirstPosition].isChildNoMoreData=(commentViewModel.mChildCommentBean!!.hasNext==0)//重要逻辑
//                    mFirstCommentList[mFirstPosition].mChildCommentListAdapter!!.setData(
//                        commentViewModel.mChildCommentBean!!.list,
//                        false
//                    )//把装好的临时集合设置进去
//                    commentListAdapter.updateItem(mFirstPosition)//更新此条数据
//                }
//                is ChildCommentBeanState.Fail -> {
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//    }
//
//    /**
//     * =================================================================发评论
//    targetId,//动态id，一级评论id
//    type,//0-动态 1-评论
//    content!!,//评论内容
//    null,//回复的评论id
//    atUser!!,//at的用户字符串,[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
//    config!!//配置字符串
//     */
//    private fun commentPublish(commentDefaultLayout: View, mRecyclerview: RecyclerView, feedId:Long, mLifecycleOwner: LifecycleOwner, mShowKeyboardAndPop: ShowKeyboardAndPop, mActivity: Activity, mFirstCommentList: MutableList<CommentBean.Item>, commentListAdapter: CommentListAdapter, commentViewModel: CommentViewModel){
//        //发评论的回调
//        mShowKeyboardAndPop.setSendCommentClickListener { targetId,type,content,replyId,atUser,config,isChild ->
//            if(isChild){
//                if (replyId==0L){
//                    commentViewModel.getPublishCommentChild(targetId, type, content, null, atUser, config)
//                }else{
//                    commentViewModel.getPublishCommentChild(targetId, type, content, replyId, atUser, config)
//                }
//            }else{
//                if (replyId==0L){
//                    commentViewModel.getPublishComment(targetId, type, content, null, atUser, config)
//                }else{
//                    commentViewModel.getPublishComment(targetId, type, content, replyId, atUser, config)
//                }
//            }
//        }
//
//        //帖子评论发布接口返回 单条数据
//        commentViewModel.commentPublishBeanState.observe(mLifecycleOwner) { it ->
//            when (it) {
//                is CommentPublishBeanState.Success -> {
//                    // TODO:  需要修改
////                    //评论成功数量加1
////                    mCommentCount += 1
////                   setNumRuler(mCommentCount, commentTv, DEFAULT_COMMENT)
//
//                    //弹窗
//                    Singleton.centerToast(mActivity, "评论已发送")
//
//                    //添加一条数据
//                    commentListAdapter.addItem(commentViewModel.mCommentPublishBean!!)
//
//                    //缺省逻辑
//                    isShowDefault(feedId,mActivity,mFirstCommentList,mRecyclerview,commentDefaultLayout,mShowKeyboardAndPop)
//                }
//                is CommentPublishBeanState.Fail -> {
//                    Log.e("23131231231", "评论遇到了一点小问题……")
//                    Singleton.centerToast(mActivity, "评论遇到了一点小问题……")
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//
//
//        //子发布评论请求返回 单挑数据
//        commentViewModel.commentChildPublishBeanState.observe(mLifecycleOwner) { it ->
//            when (it) {
//                is CommentChildPublishBeanState.Success -> {
//                    // TODO: 需要修改
//                    //评论成功数量加1
////                    mCommentCount+=1
////                   setNumRuler(mCommentCount, commentTv, DEFAULT_COMMENT)
//
//                    //弹窗
//                    Singleton.centerToast(mActivity, "评论已发送")
//
//                    //添加子评论数据
//                    mFirstCommentList[mFirstPosition].mChildCommentListAdapter!!.addItems(commentViewModel.mChildCommentPublishBean!!)//调用添加
//                }
//                is CommentChildPublishBeanState.Fail -> {
//                    Log.e("23131231231","评论遇到了一点小问题……")
//                    Singleton.centerToast(mActivity, "评论遇到了一点小问题……")
//                    //1.是否有断网提示
//                    Singleton.isNetConnectedToast(mActivity)
//                }
//            }
//        }
//    }
//
//
//    /**
//     * ===================================无关紧要==============================缺省页面
//     */
//    private fun isShowDefault(feedId:Long, mActivity: Activity, mFirstCommentList: MutableList<CommentBean.Item>, mRecyclerview: RecyclerView, commentDefaultLayout: View, mShowKeyboardAndPop: ShowKeyboardAndPop){
//        Log.e("isShowDefault","mFirstCommentList.size=="+mFirstCommentList.size)
//
//        if (mFirstCommentList.size==0){
//            mRecyclerview.visibility= View.GONE
//            commentDefaultLayout.visibility= View.VISIBLE //评论缺省
//            //点击之后弹出弹窗
//            commentDefaultLayout.setOnClickListener {
//                //动态评论
//                mShowKeyboardAndPop.showKeyboard(mActivity, mActivity, feedId, 0,"",0,false)
//            }
//        }else{
//            mRecyclerview.visibility= View.VISIBLE
//            commentDefaultLayout.visibility= View.GONE //评论缺省
//        }
//    }
//
//    /**
//     * =================================================================弹窗
//     */
//    fun commonPopup(mActivity: Activity, commentId: Long, replyUser:String, replyId:Long, replyContent:String, mShowKeyboardAndPop: ShowKeyboardAndPop){
//        val inflateView: View = mActivity.layoutInflater.inflate(R.layout.dialog_comment_all, null)
//
//        val replyTips = inflateView.findViewById<TextView>(R.id.dialog_comment_reply_tips)
//        replyTips.text="@${replyUser}: ${replyContent}"
//        val reply = inflateView.findViewById<TextView>(R.id.dialog_comment_reply)
//        /**
//         * pop实例
//         */
//        val commonPopupWindow= PopupWindow(
//            inflateView,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            true
//        )
//
//        reply.setOnClickListener{
//            //评论的评论的
//            mShowKeyboardAndPop.showKeyboard(mActivity, mActivity, commentId, 1,"回复 @${replyUser}:",replyId,true)
//            if (commonPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//                commonPopupWindow.dismiss()
//            }
//        }
//        val copy = inflateView.findViewById<TextView>(R.id.dialog_comment_copy)
//        copy.setOnClickListener{
//            val clipboard: ClipboardManager = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("label", replyContent)
//            clipboard.setPrimaryClip(clip)
//            Singleton.centerToast(mActivity, "评论已复制")
//        }
//        val report = inflateView.findViewById<TextView>(R.id.dialog_comment_report)
//        report.setOnClickListener{
//            if (commonPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//                commonPopupWindow.dismiss()
//            }
//            replayPopup(mActivity)//举报子弹窗
//        }
//        val deleteLayout = inflateView.findViewById<View>(R.id.dialog_comment_delete_layout)
//
//        // TODO: 需要设置
//        if (false){//如果是自己的帖子 显示隐藏
//            deleteLayout.visibility= View.VISIBLE
//
//            val delete = inflateView.findViewById<TextView>(R.id.dialog_comment_delete)
//            delete.setOnClickListener{
//
//            }
//        }else{
//            deleteLayout.visibility= View.GONE
//        }
//
//        val cancel = inflateView.findViewById<TextView>(R.id.dialog_comment_cancel)
//        cancel.setOnClickListener{
//            if (commonPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//                commonPopupWindow.dismiss()
//            }
//        }
//
//        //动画
//        commonPopupWindow.animationStyle= R.style.BottomDialogAnimation
//
//        if (commonPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//            commonPopupWindow.dismiss()
//        } else {
//            commonPopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
//            commonPopupWindow.isOutsideTouchable = true
//            commonPopupWindow.isTouchable = true
//            commonPopupWindow.isFocusable = true
//            commonPopupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
//            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
//            commonPopupWindow.setOnDismissListener {
//                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
//            }
//        }
//
//        //在pause时关闭
//        Singleton.lifeCycleSet(mActivity, commonPopupWindow)
//    }
//
//    /**
//     * 2.举报子级弹窗
//     */
//    private fun replayPopup(mActivity: Activity){
//        val inflateView: View = mActivity.layoutInflater.inflate(R.layout.dialog_comment_all, null)
//
//        val dialogCommentCommon = inflateView.findViewById<View>(R.id.dialog_comment_common)
//        dialogCommentCommon.visibility= View.GONE
//        val dialogCommentReportLayout = inflateView.findViewById<View>(R.id.dialog_comment_report_layout)
//        dialogCommentReportLayout.visibility= View.VISIBLE
//
//
//        val breakTheLaw = inflateView.findViewById<TextView>(R.id.dialog_comment_break_the_law)//违法
//        breakTheLaw.setOnClickListener {
//            Toast.makeText(mActivity, "违法信息", Toast.LENGTH_SHORT).show()
//        }
//        val withPornographicContent = inflateView.findViewById<TextView>(R.id.dialog_comment_with_pornographic_content)//涉黄
//        withPornographicContent.setOnClickListener {
//            Toast.makeText(mActivity, "涉黄信息", Toast.LENGTH_SHORT).show()
//        }
//        val personalAttack = inflateView.findViewById<TextView>(R.id.dialog_comment_personal_attack)//人身攻击
//        personalAttack.setOnClickListener {
//            Toast.makeText(mActivity, "人身攻击", Toast.LENGTH_SHORT).show()
//        }
//        val rumourFraud = inflateView.findViewById<TextView>(R.id.dialog_comment_rumour_fraud)//谣言 欺诈
//        rumourFraud.setOnClickListener {
//            Toast.makeText(mActivity, "谣言 欺诈", Toast.LENGTH_SHORT).show()
//        }
//
//
//        /**
//         * pop实例
//         */
//        val replayPopupWindow = PopupWindow(
//            inflateView,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            true
//        )
//
//        val cancel = inflateView.findViewById<TextView>(R.id.dialog_comment_cancel)//取消
//        cancel.setOnClickListener{
//            if (replayPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//                replayPopupWindow.dismiss()
//            }
//        }
//
//
//
//        //动画
//        replayPopupWindow.animationStyle= R.style.BottomDialogAnimation
//
//        if (replayPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
//            replayPopupWindow.dismiss()
//        } else {
//            replayPopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
//            replayPopupWindow.isOutsideTouchable = true
//            replayPopupWindow.isTouchable = true
//            replayPopupWindow.isFocusable = true
//            replayPopupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
//            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
//            replayPopupWindow.setOnDismissListener {
//                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
//            }
//        }
//
//        //在pause时关闭
//        Singleton.lifeCycleSet(mActivity, replayPopupWindow)
//    }
//
//    /**
//     * =================================================评论弹窗相关逻辑bottom
//     */
//}