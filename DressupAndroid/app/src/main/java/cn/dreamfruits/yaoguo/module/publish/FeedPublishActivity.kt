package cn.dreamfruits.yaoguo.module.publish


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.PublishPickerActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.global.FeedPublishGlobal
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.module.poisearch.PoiSearchActivity
import cn.dreamfruits.yaoguo.module.publish.adapter.*
import cn.dreamfruits.yaoguo.module.publish.state.TopicState
import cn.dreamfruits.yaoguo.module.publish.state.UserState
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesActivity
import cn.dreamfruits.yaoguo.module.selectclothes.adapter.SelectedClothesAdapter
import cn.dreamfruits.yaoguo.repository.bean.location.PoiItemBean
import cn.dreamfruits.yaoguo.repository.bean.media.PicBean
import cn.dreamfruits.yaoguo.repository.bean.media.VideoBean
import cn.dreamfruits.yaoguo.repository.bean.publish.FeedPubParamsBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.util.FirstChangeLineFilter
import cn.dreamfruits.yaoguo.util.GlideEngine
import cn.dreamfruits.yaoguo.util.MaxLengthFilter
import cn.dreamfruits.yaoguo.util.SizeUtil
import cn.dreamfruits.yaoguo.view.decoration.CommonItemDecoration
import cn.dreamfruits.yaoguo.view.dialog.AlertDialogType
import cn.dreamfruits.yaoguo.view.dialog.bottomMenu
import cn.dreamfruits.yaoguo.view.dialog.commAlertDialog
import cn.dreamfruits.yaoguo.view.mention.MentionEditText
import cn.dreamfruits.yaoguo.view.mention.bean.MentionTopic
import cn.dreamfruits.yaoguo.view.mention.bean.MentionUser
import cn.dreamfruits.yaoguo.view.mention.listener.EditDataListener
import com.blankj.utilcode.util.*
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.permissionx.guolindev.PermissionX
import java.io.File

/**
 * 发布动态
 */
class FeedPublishActivity : BaseActivity() {

    private lateinit var mImagesRv: RecyclerView
    private var mMediaAdapter: MediaListAdapter? = null

    private lateinit var mSelectedHint: TextView
    private lateinit var mTvTitleHint: TextView
    private lateinit var mClothesListRv: RecyclerView
    private lateinit var mTopBar: LinearLayout
    private lateinit var mBackIv: ImageView

    //标题
    private lateinit var mTitleEt: EditText

    //内容
    private lateinit var mMentionEdit: MentionEditText
    private lateinit var mHotTopicListRv: RecyclerView
    private lateinit var mScrollView: NestedScrollView


    private lateinit var mAddTopic: LinearLayout
    private lateinit var mAtUser: LinearLayout

    private lateinit var mContentCl: ConstraintLayout

    private lateinit var mTopicContainerRl: RelativeLayout
    private lateinit var mLocalTopicRl: RelativeLayout

    //添加位置
    private lateinit var mLocationLl: LinearLayout
    private lateinit var mLocationTv: TextView
    private lateinit var mLocationIconIv: ImageView

    private lateinit var mPublishTv: TextView


    private var mClothesAdapter: SelectedClothesAdapter? = null

    private lateinit var mSearchTopicRv: RecyclerView
    private var mHotTopicAdapter: HotTopicListAdapter? = null
    private var mSearchTopicAdapter: TopicListAdapter? = null
    private var mUserAdapter: UserListAdapter? = null

    private lateinit var mAtContainer: RelativeLayout
    private lateinit var mAtUserRv: RecyclerView


    private lateinit var mDynamicBlockLl: RelativeLayout
    private lateinit var mCompleteTv: TextView


    private lateinit var mNewTopicName: TextView
    private lateinit var mAddNewTopic: TextView

    //当前位置
    private var mLocation: PoiItemBean? = null


    private val feedPublishViewModel by viewModels<FeedPublishViewModel>()


    override fun layoutResId(): Int = R.layout.activity_feed_publish


    override fun initView() {

        mTopBar = findViewById(R.id.ll_top_bar)
        mImagesRv = findViewById(R.id.rv_media_list)
        mSelectedHint = findViewById(R.id.tv_selected_hint)
        mClothesListRv = findViewById(R.id.rv_selected_clothes_list)
        mTitleEt = findViewById(R.id.et_title)
        mMentionEdit = findViewById(R.id.rich_edit)
        mHotTopicListRv = findViewById(R.id.rv_hot_topic_list)
        // mScrollView = findViewById(R.id.nested_scroll_view)
        mContentCl = findViewById(R.id.cl_content)
        mAddTopic = findViewById(R.id.ll_add_topic)
        mAtUser = findViewById(R.id.ll_at_user)

        mTopicContainerRl = findViewById(R.id.rl_topic_container)
        mLocalTopicRl = findViewById(R.id.rl_local_topic)
        mSearchTopicRv = findViewById(R.id.rv_topic_list)
        mLocationLl = findViewById(R.id.ll_location)
        mLocationTv = findViewById(R.id.tv_location)
        mLocationIconIv = findViewById(R.id.iv_location_icon)

        mPublishTv = findViewById(R.id.tv_publish)
        mDynamicBlockLl = findViewById(R.id.ll_dynamic_block)
        mCompleteTv = findViewById(R.id.tv_complete)
        mAtContainer = findViewById(R.id.rl_at_container)
        mAtUserRv = findViewById(R.id.rv_at_user_list)
        mNewTopicName = findViewById(R.id.tv_topic_name)
        mAddNewTopic = findViewById(R.id.tv_add_topic)
        mBackIv = findViewById(R.id.iv_back)
        mTvTitleHint = findViewById(R.id.tv_title_hint)

        findViewById<ConstraintLayout>(R.id.cl_content)
            .setOnClickListener {
                KeyboardUtils.hideSoftInput(this)
            }


        mMentionEdit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 键盘处于显示状态
                if (KeyboardUtils.isSoftInputVisible(this)) {
                    val y = mMentionEdit.top

                    mContentCl.scrollTo(0, y)
                }
            }
        }

        mSelectedHint.setOnClickListener {
            val intent = Intent(this, SelectClothesActivity::class.java)
            intent.putParcelableArrayListExtra(
                RouterConstants.FeedPublish.KEY_CLOTHES_LIST,
                feedPublishViewModel.mClothesList as? ArrayList<ClothesBean>
            )

            Log.i("TAG11", "initView: ${feedPublishViewModel.mClothesList?.size}")
            startActivityForResult(intent, RouterConstants.FeedPublish.REQUEST_CODE_SELECT_CLOTHES)
        }

        mAddTopic.setOnClickListener {

            if (!KeyboardUtils.isSoftInputVisible(this)) {
                KeyboardUtils.showSoftInput(this)
                mMentionEdit.requestFocus()
            }

            val index: Int = mMentionEdit.selectionStart
            mMentionEdit.text?.insert(index, "#")
        }

        mAtUser.setOnClickListener {
            if (!KeyboardUtils.isSoftInputVisible(this)) {
                KeyboardUtils.showSoftInput(this)
                mMentionEdit.requestFocus()
            }

            val index: Int = mMentionEdit.selectionStart
            mMentionEdit.text?.insert(index, "@")

        }

        mLocationLl.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        startPioSearch()
                    } else {
                        ToastUtils.showShort("位置权限未开启")
                    }
                }

        }

        mPublishTv.setOnClickListener {
            handleFeedParamAndPublish()
        }

        //插入新话题
        mAddNewTopic.setOnClickListener {
            mMentionEdit.insert(" ")
            mTopicContainerRl.isVisible = false
        }

        mBackIv.setOnClickListener {
            onBackPressed()
        }

        mCompleteTv.setOnClickListener {
            //
            KeyboardUtils.hideSoftInput(this)
        }


        mTitleEt.addTextChangedListener {
            mTvTitleHint.text = "${it.toString().length}/20"
        }
//
        mTitleEt.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SPACE) && event.action == KeyEvent.ACTION_DOWN) {
                // 拦截换行符和空格输入
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
//

        mTitleEt.filters = arrayOf<InputFilter>(
            MaxLengthFilter(20, "最多只能输入20字")
        )
        mMentionEdit.filters = arrayOf<InputFilter>(
            MaxLengthFilter(500, "最多只能输入500字"),
            FirstChangeLineFilter(mTitleEt)
        )

        initMediaList()
        initClothesList()
        initRichEdit()
        initHotTopicList()
        initKeyboardListener()

    }

    /**
     * 初始化键盘监听
     */
    private fun initKeyboardListener() {
        KeyboardUtils.registerSoftInputChangedListener(this) { height ->

            if (height > 0) {
                if (mMentionEdit.isFocused) {
                    val y = mMentionEdit.top
                    mContentCl.scrollTo(0, y)

                }
                mDynamicBlockLl.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin = height
                    mDynamicBlockLl.visibility = View.VISIBLE
                }


            } else {
                if (mMentionEdit.isFocused) {
                    mContentCl.scrollTo(0, 0)
                    //隐藏搜索话题列表
                    mTopicContainerRl.visibility = View.GONE
                    //隐藏@用户列表
                    mAtContainer.visibility = View.GONE
                }
                mDynamicBlockLl.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin = 0
                    mDynamicBlockLl.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 跳转到定位页面
     */
    private fun startPioSearch() {
        val intent = Intent(this, PoiSearchActivity::class.java)
        startActivityForResult(intent, RouterConstants.FeedPublish.REQUEST_CODE_POI_SEARCH)
    }


    /**
     * 初始化已选择的 照片 或视频 列表
     */
    private fun initMediaList() {
        mMediaAdapter = MediaListAdapter()
        mMediaAdapter?.setOnItemClickListener(mOnItemClickListener)

        mImagesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mImagesRv.addItemDecoration(

            CommonItemDecoration(5, 16, 16)
        )


        var result: ArrayList<LocalMedia> =
            intent.getParcelableArrayListExtra(RouterConstants.FeedPublish.KEY_SEL_MEDIA_LIST)!!
        setMediaData(result)


//        PictureSelector.create(this)
//            .openGallery(SelectMimeType.ofAll())
//            .setImageEngine(GlideEngine.instance)
//            .setVideoThumbnailListener(VideoThumbnailEventListener(getVideoThumbnailDir()))
//            .forResult(object : OnResultCallbackListener<LocalMedia> {
//
//                override fun onResult(result: ArrayList<LocalMedia>?) {
//
//                    setMediaData(result)
//
//                }
//
//                override fun onCancel() {
//                    this@FeedPublishActivity.finish()
//                }
//            })

    }

    private fun setMediaData(result: ArrayList<LocalMedia>?) {
        result?.let { media ->
            val mimeType = media.first().mimeType

            //如果是图片 最大展示9个
            if (PictureMimeType.isHasImage(mimeType)) {
                mMediaAdapter?.setSelectMax(9)

                //如果是视频 最大展示1个
            } else if (PictureMimeType.isHasVideo(mimeType)) {
                mMediaAdapter?.setSelectMax(1)
                feedPublishViewModel.video = result.first()

            }

            mImagesRv.adapter = mMediaAdapter
            val itemTouchHelper =
                ItemTouchHelper(ItemTouchHelperCallback(mMediaAdapter!!))
            itemTouchHelper.attachToRecyclerView(mImagesRv)
            mMediaAdapter?.getData()?.addAll(media)
            mMediaAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 初始化单品列表
     */
    private fun initClothesList() {
        mClothesListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mClothesListRv.addItemDecoration(CommonItemDecoration(14, 16, 16))

        mClothesAdapter = SelectedClothesAdapter(onDeleteClick = { clothesBean ->
            feedPublishViewModel.mClothesList?.remove(clothesBean)
        }, isShowDel = true)
        mClothesListRv.adapter = mClothesAdapter


    }

    /**
     * 初始化 话题 @用户 编辑框
     */
    private fun initRichEdit() {
        mMentionEdit.editDataListener = object : EditDataListener {

            override fun onEditAddAt(str: String?) {
                showUserList(str)
            }

            override fun onEditAddHashtag(str: String?) {
                showTopicList(str)

            }

            override fun onCloseSearchView() {
                mAtContainer.isVisible = false
                mTopicContainerRl.isVisible = false
            }

        }
    }


    /**
     * 显示话题列表
     */
    private fun showTopicList(str: String?) {
        //如果@用户列表正在展示 则先隐藏
        if (mAtContainer.isVisible) {
            mAtContainer.isVisible = false
        }
        //如果话题列表已经处于展示状态 则进行搜索操操作
        if (mTopicContainerRl.isVisible) {
            str?.let { keyword ->
                feedPublishViewModel.searchTopic(keyword)
            }

            return
        }

        if (mSearchTopicAdapter == null) {

            mSearchTopicAdapter = TopicListAdapter(onTopicClick = { topicBean ->
                mMentionEdit.insert(MentionTopic(topicBean.id.toString(), topicBean.name.trim()))
                mTopicContainerRl.visibility = View.GONE
            })
            mSearchTopicRv.layoutManager = LinearLayoutManager(this)
            mSearchTopicRv.adapter = mSearchTopicAdapter
        }
        mTopicContainerRl.visibility = View.VISIBLE


        mTopicContainerRl.postDelayed({
            mTopicContainerRl.updateLayoutParams<ConstraintLayout.LayoutParams> {
                //修改搜索话题列表高度
                height =
                    mDynamicBlockLl.top - (mAddTopic.bottom - mContentCl.scrollY) - mTopBar.height
            }
        }, 200)


        mHotTopicAdapter?.dataList?.let {
            mSearchTopicAdapter?.setData(it)
        }
    }


    /**
     *显示@用户列表
     */
    private fun showUserList(keyWord: String?) {
        //如果话题列表处于显示状态 则先隐藏
        if (mTopicContainerRl.isVisible) {
            mTopicContainerRl.isVisible = false
        }

        if (mAtContainer.isVisible) {
            keyWord?.let {
                feedPublishViewModel.searchUser(it)
            }
            return
        }

        if (mUserAdapter == null) {
            mUserAdapter = UserListAdapter(onUserClick = { user ->
                mMentionEdit.insert(MentionUser(user.id.toString(), user.nickName))
                mAtContainer.visibility = View.GONE

            })
            mAtUserRv.layoutManager = LinearLayoutManager(this)
            mAtUserRv.adapter = mUserAdapter

        }
        mAtContainer.isVisible = true


        feedPublishViewModel.getAtUserList()

    }


    /**
     * 初始化热门话题
     */
    private fun initHotTopicList() {
        mHotTopicAdapter = HotTopicListAdapter(onItemClickListener = { topicBean ->
            val index: Int = mMentionEdit.selectionStart
            mMentionEdit.editableText.insert(index, "#")
            mMentionEdit.insert(MentionTopic("123456", topicBean.name))
        })
        mHotTopicListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mHotTopicListRv.addItemDecoration(CommonItemDecoration(14, 10, 10))
        mHotTopicListRv.adapter = mHotTopicAdapter


    }


    /**
     *
     * @return
     */
    private fun getVideoThumbnailDir(): String {
        val customFile = File(PathUtils.getExternalAppFilesPath(), "Thumbnail")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }


    /**
     * 图片视频列表 点击监听
     */
    private val mOnItemClickListener = object : MediaListAdapter.OnItemClickListener {

        override fun onItemClick(position: Int) {

            if (feedPublishViewModel.video == null) {
                bottomMenu(supportFragmentManager) {
                    menuList = listOf("查看大图", "删除")
                    menuItemClick = {
                        when (it) {
                            0 -> {
                                PictureSelector.create(this@FeedPublishActivity)
                                    .openPreview()
                                    .setImageEngine(GlideEngine.instance)
                                    .startActivityPreview(
                                        position,
                                        false,
                                        mMediaAdapter?.getData()!!
                                    )
                            }
                            1 -> {
                                mMediaAdapter?.let {
                                    if (it.getData().size <= 1) {
                                        commAlertDialog(supportFragmentManager) {
                                            type = AlertDialogType.SINGLE_BUTTON
                                            content = "至少发布一张照片哦"
                                            cancelText = "确认"
                                            cancelCallback = {

                                            }
                                        }
                                        return@let
                                    }
                                    commAlertDialog(supportFragmentManager) {
                                        content = "确定要删除吗?"
                                        confirmText = "确认"
                                        cancelText = "取消"
                                        cancelCallback = {

                                        }
                                        confirmCallback = {
                                            mMediaAdapter?.delete(position)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

//                val intent = Intent(this@FeedPublishActivity, PublishPickerActivity::class.java)
//                intent.putExtra("KEY_SEL_MEDIA_LIST", mMediaAdapter!!.getData())
//                intent.putExtra("IS_CHOOSE_VIDEO_COVER", 1)
//                startActivityForResult(intent, PictureConstants.VIDEO_RESULT_CODE)

                PictureSelector.create(this@FeedPublishActivity)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.instance)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>?) {
                            mMediaAdapter?.let { adapter ->
                                adapter.isVideoCover(true)
                                adapter.getData().clear()
                                adapter.getData().addAll(result!!)
                                adapter.notifyDataSetChanged()
                            }
                        }

                        override fun onCancel() {

                        }

                    })

            }
        }

        override fun onVideoPreview(position: Int) {
            PictureSelector.create(this@FeedPublishActivity)
                .openPreview()
                .setImageEngine(GlideEngine.instance)
                .isVideoPauseResumePlay(true)
                .startActivityPreview(position, false, arrayListOf(feedPublishViewModel.video))
        }

        override fun onAddClick() {

            val intent = Intent(this@FeedPublishActivity, PublishPickerActivity::class.java)
            intent.putExtra("KEY_SEL_MEDIA_LIST", mMediaAdapter!!.getData())
            startActivityForResult(intent, PictureConstants.IMAGE_RESULT_CODE)

        }
    }


    override fun initData() {
        feedPublishViewModel.mClothesList =
            intent.getParcelableArrayListExtra(RouterConstants.FeedPublish.KEY_CLOTHES_LIST)
        feedPublishViewModel.gender =
            intent.getIntExtra(RouterConstants.FeedPublish.KEY_SELECT_GENDER, -1)

        feedPublishViewModel.mClothesList?.let {
            mClothesAdapter?.setData(it)
        }

        feedPublishViewModel.hotTopicState.observe(this) { topicState ->
            when (topicState) {
                is TopicState.Success -> {
                    topicState.list?.let {
                        mHotTopicAdapter?.addData(it)
                    }
                }

                is TopicState.Fail -> {
                    ToastUtils.showShort("获取热门话题失败")
                }
            }
        }

        feedPublishViewModel.searchTopicState.observe(this) { topicState ->
            when (topicState) {
                is TopicState.Success -> {
                    topicState.list?.let {
                        if (it.isEmpty()) {
                            //不是推荐话题才 显示添加新话题
                            if (!TextUtils.isEmpty(topicState.localTopic)) {
                                mLocalTopicRl.isVisible = true
                                mNewTopicName.text = topicState.localTopic
                            } else {
                                mLocalTopicRl.isVisible = false
                            }
                        } else {
                            mLocalTopicRl.isVisible = false
                        }
                        mSearchTopicAdapter?.setData(it)
                    }
                }

                is TopicState.Fail -> {
//                    ToastUtils.showShort("搜索话题失败")
                }
            }
        }

        feedPublishViewModel.searchUserState.observe(this) { userState ->
            when (userState) {
                is UserState.Success -> {
                    userState.list?.let {
                        mUserAdapter?.setData(it)
                    }

                }
                is UserState.Fail -> {
                    ToastUtils.showShort("搜索用户出现错误")
                }
            }
        }


        feedPublishViewModel.getHotTopicList()

    }

    /**
     * 处理发布帖子的参数
     */
    @SuppressWarnings("all")
    private fun handleFeedParamAndPublish() {
        val feedPubParamsBean = FeedPubParamsBean()
        //帖子类型 如果视频为空 那么就是图文 不然就是视频
        val type = if (feedPublishViewModel.video == null) 0 else 1

        feedPubParamsBean.title = mTitleEt.text.toString()
        feedPubParamsBean.content = mMentionEdit.text.toString()
        feedPubParamsBean.type = type
        feedPubParamsBean.taskId = System.currentTimeMillis()

        //位置
        feedPubParamsBean.address = mLocation?.title
        feedPubParamsBean.provinceAdCode = mLocation?.provinceCode?.toLong()
        feedPubParamsBean.cityAdCode = mLocation?.cityCode?.toLong()
        feedPubParamsBean.longitude = mLocation?.longitude
        feedPubParamsBean.latitude = mLocation?.latitude

        feedPubParamsBean.topicIds = mMentionEdit.formatResult.topicList?.joinToString(
            separator = ",",
            prefix = "[",
            postfix = "]"
        ) { topic ->
            "{\"index\":${topic.fromIndex},\"len\":${topic.length},\"name\":\"${topic.name}\"}"
        }

        feedPubParamsBean.atUser = mMentionEdit.formatResult.userList?.joinToString(
            separator = ",",
            prefix = "[",
            postfix = "]"
        ) { user ->
            "{\"index\":${user.fromIndex},\"len\":${user.length},\"id\":${user.id},\"name\":\"${user.name}\"}"
        }

        if (type == 0) {
            val picBeans = mMediaAdapter?.getData()?.map { localMedia ->

                PicBean(
                    "",
                    localMedia.path,
                    localMedia.width,
                    localMedia.height
                )
            }
            feedPubParamsBean.picBean = picBeans?.toMutableList()
        } else if (type == 1) {
            //如果修改过视频封面
            if (mMediaAdapter!!.getIsVideoCover()) {
                mMediaAdapter?.getData()?.first()?.let { localMedia ->
                    feedPubParamsBean.picBean =
                        mutableListOf(
                            PicBean(
                                "",
                                localMedia.path,
                                localMedia.width,
                                localMedia.height
                            )
                        )
                }
                feedPublishViewModel.video?.let { video ->
                    feedPubParamsBean.videoBean = VideoBean(video.path, video.width, video.height)
                }
                //没有修改过封面
            } else {
                feedPublishViewModel.video?.let { video ->
                    feedPubParamsBean.picBean =
                        mutableListOf(
                            PicBean(
                                "",
                                video.videoThumbnailPath,
                                video.width,
                                video.height
                            )
                        )
                    feedPubParamsBean.videoBean = VideoBean(video.path, video.width, video.height)
                }
            }

        }

        feedPubParamsBean.seeLimit = 0
        feedPubParamsBean.singleIds = feedPublishViewModel.mClothesList?.joinToString { "${it.id}" }

        FeedPublishGlobal.feedPublish(feedPubParamsBean)
//        ActivityUtils.finishToActivity(MainActivity::class.java, false)
        MainActivity.start(mContext, 1, 0)
        finish()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RouterConstants.FeedPublish.REQUEST_CODE_POI_SEARCH
            && resultCode == RouterConstants.FeedPublish.RESULT_CODE_POI_SEARCH
        ) {

            mLocation = data?.getParcelableExtra("PoiItemBean")
            if (mLocation == null) {
                mLocationTv.setTextColor(Color.BLACK)
                mLocationIconIv.setBackgroundResource(R.drawable.ic_location)
                mLocationTv.text = "添加地址"
            } else {
                mLocationTv.setTextColor(Color.parseColor("#0D99FF"))
                mLocationIconIv.setBackgroundResource(R.drawable.ic_blue_location)
                mLocationTv.text = mLocation?.title
            }
        } else if (requestCode == RouterConstants.FeedPublish.REQUEST_CODE_SELECT_CLOTHES &&
            resultCode == RouterConstants.FeedPublish.RESULT_SELECT_CLOTHES
        ) {

            feedPublishViewModel.mClothesList =
                data?.getParcelableArrayListExtra(RouterConstants.FeedPublish.KEY_CLOTHES_LIST)
            feedPublishViewModel.gender =
                data?.getIntExtra(RouterConstants.FeedPublish.KEY_SELECT_GENDER, -1) ?: -1

            feedPublishViewModel.mClothesList?.let {
                mClothesAdapter?.setData(it)
            }

        } else if (requestCode == PictureConstants.IMAGE_RESULT_CODE) {

            val resultList =
                data?.getParcelableArrayListExtra<LocalMedia>(PictureConstants.MEDIA_RESULT_LIST)
                    ?: return
            setReData(resultList)
//            mMediaAdapter?.let {
//                it.getData().clear()
//                it.getData().addAll(resultList)
//                it.notifyDataSetChanged()
//
//            }
        }
    }


    private fun setReData(result: ArrayList<LocalMedia>?) {
        result?.let { media ->
            val mimeType = media.first().mimeType

            //如果是图片 最大展示9个
            if (PictureMimeType.isHasImage(mimeType)) {
                mMediaAdapter?.setSelectMax(9)
                //如果是视频 最大展示1个
            } else if (PictureMimeType.isHasVideo(mimeType)) {
                mMediaAdapter?.setSelectMax(1)
                feedPublishViewModel.video = result.first()
            }
            mMediaAdapter?.getData()?.clear()
            mMediaAdapter?.getData()?.addAll(media)
            mMediaAdapter?.notifyDataSetChanged()
        }
    }


    override fun onBackPressed() {
//        val strings = arrayOf("返回编辑", "保存并退出")
//        XPopup.Builder(this).asBottomList(
//            "确定要返回编辑吗", strings
//        ) { position, text ->
//            if (position == 0) {
                super.onBackPressed()
//            } else if (position == 1) {
//                ToastUtils.showShort("保存并退出")
//                super.onBackPressed()
//            }
//        }.show()
    }


}