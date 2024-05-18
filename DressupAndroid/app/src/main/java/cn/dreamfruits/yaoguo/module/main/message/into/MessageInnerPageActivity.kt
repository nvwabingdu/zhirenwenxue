package cn.dreamfruits.yaoguo.module.main.message.into


import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.commentreplay.AtUserAdapter
import cn.dreamfruits.yaoguo.module.main.home.commentreplay.ShowKeyboardAndPop
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsViewModel
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentViewModel
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.home.vlistvideo.ListVideoActivity
import cn.dreamfruits.yaoguo.module.main.message.MessageViewModel
import cn.dreamfruits.yaoguo.repository.bean.message.GetMessageInnerPageListBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

@RequiresApi(Build.VERSION_CODES.O)
class MessageInnerPageActivity :BaseActivity(){
    override fun layoutResId(): Int =R.layout.activity_message_inner_page

    private var mRecyclerView: RecyclerView?=null
    private var mList: MutableList<GetMessageInnerPageListBean.Item>?=ArrayList()
    private var mAdapter: MessageInnerPageAdapter? = null
    private var mBack: ImageView? = null
    private var mTitle: TextView? = null

    private val messageViewModel by viewModels<MessageViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel

    private val commentViewModel by viewModels<CommentViewModel>()//评论的viewmodel

    private  val postDetailsViewModel by viewModels<PostDetailsViewModel>()

    private var tag = 0//0 点赞  1 关注   2 评论

    override fun initView() {
        tag=intent.getIntExtra("tag",0)
        initTop()
        initViews()
        setCallback()
        when(tag){
            0->{
                setRefreshLaud()
            }
            1->{
                setRefreshFollow()
            }
            2->{
                setRefreshComment()
            }
        }
        refreshLayout!!.autoRefresh()//自动刷新
    }
    override fun initData() {}
    private fun initTop(){
        mBack=findViewById(R.id.message_top_back)
        mBack!!.setOnClickListener {
            finish()
        }
        mTitle=findViewById(R.id.message_top_title)
        mTitle!!.text=intent.getStringExtra("title")
    }

    private fun initViews(){
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView!!.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = MessageInnerPageAdapter(this,mList!!,tag)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun  setCallback(){
        //设置回调
        mAdapter!!.setMessageInnerPageAdapterListener(object :MessageInnerPageAdapter.MessageInnerPageAdapterInterface{
            override fun onLaud(isLaud: Boolean, id: Long) {
                if (isLaud){
                    commentViewModel.getLaudComment(id)//点赞
                }else{
                    commentViewModel.getUnLaudComment(id)//取消点赞
                }
            }

            override fun onCareUser(id: Long, isCare: Boolean) {
                if (isCare){//关注
                    commonViewModel.getFollowUser(id)
                }else{//取消关注
                    commonViewModel.getUnfollowUser(id)
                }
            }

            override fun onPublishComment(id: Long, replyUser: String, replyId: Long) {
                mShowKeyboardAndPop.showKeyboard(this@MessageInnerPageActivity,this@MessageInnerPageActivity ,id, 1, "回复 @${replyUser}:", replyId,true)
            }

            override fun onLoadVideoData(id: Long, isComment: Boolean,commentId:Long?,firstCommentId:Long?,replyId:Long?) {//跳转视频详情页
                postDetailsViewModel.feedDetailsBeanState.observe(this@MessageInnerPageActivity) { it ->
                    when (it) {
                        is FeedDetailsBeanState.Success -> {//先请求本条视频数据
                            Singleton.mMessageVideoList!!.clear()
                            Singleton.mMessageVideoList!!.add(postDetailsViewModel.mFeedDetailsBean!!)//装入集合

                            //再跳转
                            val intent = Intent(this@MessageInnerPageActivity, ListVideoActivity::class.java)//视频列表页面
                            intent.putExtra("feedId", id)
                            intent.putExtra("position", 0)//位置
                            if (isComment){
                                intent.putExtra("isComment", true)//是否是评论
                                if (replyId==null){//为空表示为帖子评论
                                    intent.putExtra("highlightId",commentId)//需要高亮的评论id

                                    intent.putExtra("messageIds", "${commentId}")//一级评论id
                                    intent.putExtra("messageCommentIds", "${commentId}")//一级评论，二级评论，二级评论，

                                    Log.e("TAG12","highlightId：${commentId}")
                                    Log.e("TAG12","messageIds：${commentId}")
                                    Log.e("TAG12","messageCommentIds：${commentId}")
                                }else{
                                    intent.putExtra("highlightId",commentId)//需要高亮的评论id

                                    intent.putExtra("messageIds","${firstCommentId}")//一级评论id
                                    intent.putExtra("messageCommentIds", "${firstCommentId},${replyId},${commentId}")//一级评论，二级评论，二级评论，

                                    Log.e("TAG12","highlightId：${replyId}")
                                    Log.e("TAG121","messageIds：${firstCommentId}")
                                    Log.e("TAG12","messageCommentIds：${firstCommentId},${replyId},${commentId}")
                                }
                            }
                            Singleton.videoTag=5
                            //还需要传递一条数据过去
                            startActivity(intent)
                        }
                        is FeedDetailsBeanState.Fail -> {
                            //1.是否有断网提示
                            Singleton.isNetConnectedToast(this@MessageInnerPageActivity)
                        }
                    }
                }
                //请求帖子详情
                postDetailsViewModel.getFeedDetail(id)
            }
        })

        /**
         * 请求结果   关注
         */
        commonViewModel.followUserBeanState.observe(this){
            when (it) {
                is FollowUserBeanState.Success -> {
                    Singleton.centerToast(this,Singleton.CARE_TEXT)
                }
                is FollowUserBeanState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        /**
         * 请求结果  取消关注
         */
        commonViewModel.unfollowUserBeanState.observe(this) {
            when (it) {
                is UnfollowUserBeanState.Success -> {
                    Singleton.centerToast(this,Singleton.UN_CARE_TEXT)
                }
                is UnfollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        /**
         * 点赞请求结果
         */
        commentViewModel.laudCommentState.observe(this){
            when (it) {
                is LaudCommentState.Success -> {

                }
                is LaudCommentState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        /**
         * 取消点赞请求结果
         */
        commentViewModel.unLaudCommentState.observe(this){
            when (it) {
                is UnLaudCommentState.Success -> {

                }
                is UnLaudCommentState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    private var refreshLayout: SmartRefreshLayout?= null
    private var refreshState:Int=0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false

    //点赞
    private fun  setRefreshLaud(){
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))

        /**
         * 2.请求结果
         */
        messageViewModel.getLaudMessageListBeanState.observe(this){
            when (it) {
                is GetLaudMessageListBeanState.Success -> {
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                            mList!!.clear()//清空集合
                            mList= messageViewModel.mGetLaudMessageListBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                            mAdapter!!.setData(messageViewModel.mGetLaudMessageListBean!!.list, false)//设置数据 更新适配器
                        }
                    }
                }
                is GetLaudMessageListBeanState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }
                }
            }
        }

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState=0 //1.设置标签为刷新
            messageViewModel.getLaudMessageList(10,null)
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState=1 //设置标签为加载（不刷新）
            getLoadMoreLaud(messageViewModel.mGetLaudMessageListBean!!)
        }
    }

    //关注
    private fun  setRefreshFollow(){
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))

        /**
         * 2.请求结果
         */
        messageViewModel.getFollowMessageListBeanState.observe(this){
            when (it) {
                is GetFollowMessageListBeanState.Success -> {
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                            mList!!.clear()//清空集合
                            mList= messageViewModel.mGetFollowMessageListBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                            mAdapter!!.setData(messageViewModel.mGetFollowMessageListBean!!.list, false)//设置数据 更新适配器
                        }
                    }
                }
                is GetFollowMessageListBeanState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }
                }
            }
        }

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState=0 //1.设置标签为刷新
            messageViewModel.getFollowMessageList(10,null)
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState=1 //设置标签为加载（不刷新）
            getLoadMoreFollow(messageViewModel.mGetFollowMessageListBean!!)
        }
    }

    //评论
    private fun  setRefreshComment(){
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))

        /**
         * 2.请求结果
         */
        messageViewModel.getCommentMessageListBeanState.observe(this){
            when (it) {
                is GetCommentMessageListBeanState.Success -> {
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                            mList!!.clear()//清空集合
                            mList= messageViewModel.mGetCommentMessageListBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                            mAdapter!!.setData(messageViewModel.mGetCommentMessageListBean!!.list, false)//设置数据 更新适配器
                        }
                    }
                }
                is GetCommentMessageListBeanState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }
                }
            }
        }

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState=0 //1.设置标签为刷新
            messageViewModel.getCommentMessageList(10,null)
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState=1 //设置标签为加载（不刷新）
            getLoadMoreComment(messageViewModel.mGetCommentMessageListBean!!)
        }

        /**
         * 艾特用户逻辑
         */
        commentAtUser()

        /**
         * 发布评论逻辑
         */
        commentPublish()
    }

    /**
     * =================================================================发评论
    targetId,//动态id，一级评论id
    type,//0-动态 1-评论
    content!!,//评论内容
    null,//回复的评论id
    atUser!!,//at的用户字符串,[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
    config!!//配置字符串
     */

    //获取其实例  重要对象
    private val mShowKeyboardAndPop = ShowKeyboardAndPop()
    private fun commentPublish(){
        //发评论的回调
        mShowKeyboardAndPop.setSendCommentClickListener { targetId,type,content,replyId,atUser,config,isChild ->
            if(isChild){
                if (replyId==0L){
                    commentViewModel.getPublishCommentChild(targetId, type, content, null, atUser, config)
                }else{
                    commentViewModel.getPublishCommentChild(targetId, type, content, replyId, atUser, config)
                }
            }else{
                if (replyId==0L){
                    commentViewModel.getPublishComment(targetId, type, content, null, atUser, config)
                }else{
                    commentViewModel.getPublishComment(targetId, type, content, replyId, atUser, config)
                }
            }

        }

        //帖子评论发布接口返回 单条数据
        commentViewModel.commentPublishBeanState.observe(this) { it ->
            when (it) {
                is CommentPublishBeanState.Success -> {
                    //弹窗
                    Singleton.centerToast(this, "回复成功")
                }
                is CommentPublishBeanState.Fail -> {
                    Singleton.centerToast(this, "评论遇到了一点小问题……")
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        //子发布评论请求返回 单挑数据
        commentViewModel.commentChildPublishBeanState.observe(this) { it ->
            when (it) {
                is CommentChildPublishBeanState.Success -> {
                    //弹窗
                    Singleton.centerToast(this, "回复成功")
                }
                is CommentChildPublishBeanState.Fail -> {
                    Singleton.centerToast(this, "评论遇到了一点小问题……")
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    /**
     * @用户
     */
    //at用户适配器
    private var tempAtUserAdapter: AtUserAdapter?=null
    private var isAtUserFirst:Boolean=false
    private var isSearchUserFirst:Boolean=false
    private fun commentAtUser(){
        //请求艾特用户接口  回调
        mShowKeyboardAndPop.setSearchUserClickListener(object : ShowKeyboardAndPop.SearchUserInterface {
            override fun onclick(
                atUserAdapter: AtUserAdapter,
                isGetAtUserList: Boolean?,
                key: String,
                isKeyChange: Boolean,//所搜索的内容是否变化
                atUser: String?,
                config: String?
            ) {

                tempAtUserAdapter = atUserAdapter//取到适配器实例

                if (isGetAtUserList == true) {//请求at用户列表
                    if (isKeyChange) {
                        isAtUserFirst = true
                        commentViewModel.getAtUserList(10, 0, null)
                    } else {
                        isAtUserFirst = false
                        commentViewModel.getAtUserList(10, commentViewModel.mAtUserListBean!!.lastTime, atUser!!)//加载更多
                    }
                    //分页
                } else {//请求搜索用户接口
                    if (isKeyChange) {//请求词变化了  页码不变  集合清空  设置适配器
                        isSearchUserFirst = true
                        commentViewModel.getSearchUser(
                            key,//搜索词
                            1,//页码，第一次穿1，后面传入上一次接口返回得page
                            10,//数量
                            null,//用于分页，传入上一次接口返回lastTime
                            null//过滤的用户的ids，例如：id1,id2,id3
                        )
                    } else {//请求词没有变  说明需要继续请求  页码使用上次的页码
                        isSearchUserFirst = false
                        commentViewModel.getSearchUser(
                            key,//搜索词
                            commentViewModel.mSearchUserBean!!.page,//页码，第一次穿1，后面传入上一次接口返回得page
                            10,//数量
                            commentViewModel.mSearchUserBean!!.lastTime,//用于分页，传入上一次接口返回lastTime
                            atUser//过滤的用户的ids，例如：id1,id2,id3
                        )
                    }
                }
            }
        })


        //请求at用户列表 接口返回
        commentViewModel.atUserListBeanState.observe(this) { it ->
            when (it) {
                is AtUserListBeanState.Success -> {
                    tempAtUserAdapter!!.setData(commentViewModel.mAtUserListBean!!.list.toMutableList(), isAtUserFirst)
                }
                is AtUserListBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        //请求搜索用户接口  接口返回
        commentViewModel.searchUserBeanState.observe(this) { it ->
            when (it) {
                is SearchUserBeanState.Success -> {
                    tempAtUserAdapter!!.setData(commentViewModel.mSearchUserBean!!.list.toMutableList(), isSearchUserFirst)
                }
                is SearchUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    /**
     * 加载更多
     * messageViewModel.mGetLaudMessageListBean
     */
    private fun getLoadMoreLaud(bean: GetMessageInnerPageListBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            messageViewModel.getLaudMessageList(10,null)
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    messageViewModel.getLaudMessageList(10,bean.lastTime)
                }
            }
        }
    }

    /**
     * 加载更多
     * messageViewModel.mGetLaudMessageListBean
     */
    private fun getLoadMoreFollow(bean: GetMessageInnerPageListBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            messageViewModel.getFollowMessageList(10,null)
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    messageViewModel.getFollowMessageList(10,bean.lastTime)
                }
            }
        }
    }

    /**
     * 加载更多
     * messageViewModel.mGetLaudMessageListBean
     */
    private fun getLoadMoreComment(bean: GetMessageInnerPageListBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            messageViewModel.getCommentMessageList(10,null)
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    messageViewModel.getCommentMessageList(10,bean.lastTime)
                }
            }
        }
    }
}