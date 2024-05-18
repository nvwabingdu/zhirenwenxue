package cn.dreamfruits.yaoguo.module.main.home.vlistvideo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.viewpager.widget.PagerAdapter
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.InterceptTouchRelativeLayout
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.setRichStr
import cn.dreamfruits.yaoguo.util.Singleton.showDragPopDress
import cn.dreamfruits.yaoguo.util.Singleton.showLog
import cn.dreamfruits.yaoguo.util.singleClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zr.video.tool.PlayerUtils
import com.zr.videocache.cache.PreloadManager

/**
 * @Author qiwangi
 * @Date 2023/5/11
 * @TIME 10:10
 * 修改为kotlin实现
 */
class ListVideoAdapter(
    var mActivity: Activity,
    private var dataList: MutableList<WaterfallFeedBean.Item.Info>,
) : PagerAdapter() {
    //View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
    private val mViewPool: MutableList<View> = ArrayList()

    override fun getCount(): Int {
        return dataList.size
    }

    override fun isViewFromObject(view: View, mObject: Any): Boolean {
        return view === mObject
    }

    //填充布局 并返回该布局实例
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        var view: View? = null
        if (mViewPool.size > 0) { //取第一个进行复用
            view = mViewPool[0]
            mViewPool.removeAt(0)
        }
        val viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.home_listvideo_item_listvideo_item, container, false)
            viewHolder = ViewHolder(view)
        } else {
            viewHolder = view.tag as ViewHolder
        }

        /**
         *  开始预加载   重要逻辑
         */
        // TODO: 考究
        Singleton.showLog(this, "mVideoListFeedList!!.size-1========" + position)
        try {
            PreloadManager.getInstance(context)
                .addPreloadTask(dataList[position].videoUrls[0].url, position)
        } catch (e: Exception) {
            Singleton.showLog(this, e.toString())
        }

        /**
         * 加载回调
         */
        if (dataList.size > position && dataList.size - 1 == position) {
            mListVideoAdapterInterface!!.onLoadMore()
        }

        //视频封面
        Glide.with(context)
            .load(dataList[position].picUrls[0].url)
            .placeholder(R.color.white)
            .into(viewHolder.mThumb)

        //视频标题


        //进度条相关设置
        viewHolder.mListVideoView.setInitView(
            viewHolder.mSeekBar,
            viewHolder.mTvCurrTime,
            viewHolder.mTvTotalTime,
            viewHolder.mPbBottomProgress,
        )

        /**
         * ==============================设置缩略图   重要逻辑
         */
        viewHolder.mSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            //进度条监听
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                val mControlWrapper = viewHolder.mListVideoView.controlWrapper
                val duration: Long = mControlWrapper.duration
                val newPosition: Long = duration * progress / viewHolder.mListVideoView.max
                if (viewHolder.mTvCurrTime != null) {
                    viewHolder.mTvCurrTime!!.setText(PlayerUtils.formatTime(newPosition))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                val mControlWrapper = viewHolder.mListVideoView.controlWrapper
//                mIsDragging = true
                viewHolder.mListVideoView.setIsDragging(true)
                mControlWrapper.stopProgress()
                mControlWrapper.stopFadeOut()

                viewHolder.mThumbnailImageView!!.visibility = View.VISIBLE
                viewHolder.timeLayout!!.visibility = View.VISIBLE
                viewHolder.mSeekBar.visibility = View.VISIBLE

                //拖动时隐藏多余视图
                viewHolder.videoBottom1.visibility = View.GONE
                viewHolder.videoBottomPart1.visibility = View.GONE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val mControlWrapper = viewHolder.mListVideoView.controlWrapper
                val duration: Long = mControlWrapper.duration
                val newPosition: Long = duration * seekBar.progress / viewHolder.mListVideoView.max
                mControlWrapper.seekTo(newPosition.toInt().toLong())
//                mIsDragging = false
                viewHolder.mListVideoView.setIsDragging(false)
                mControlWrapper.startProgress()
                mControlWrapper.startFadeOut()

                viewHolder.mThumbnailImageView!!.visibility = View.INVISIBLE
                viewHolder.timeLayout!!.visibility = View.GONE
                viewHolder.mSeekBar.visibility = View.INVISIBLE

                //停止拖动时显示隐藏的视图
                viewHolder.videoBottom1.visibility = View.VISIBLE
                viewHolder.videoBottomPart1.visibility = View.VISIBLE
            }
        })

        //进度条包裹布局触摸
        viewHolder.mllBottomContainer!!.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                var startX: Float = 0f
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 记录触摸事件开始时手指的横坐标
                        startX = event.x
                        showLog(this, "90000000000000点击了底部布局")
                        viewHolder.mSeekBar.visibility = View.VISIBLE//显示进度条
                    }
                    MotionEvent.ACTION_UP -> {
                        // 计算手指移动的距离
                        var distanceX: Float = event.x - startX
                        // 判断是向左还是向右滑动
                        if (distanceX > 0) {
                            // 向右滑动
                            Log.e("MainActivity", "向右滑动" + distanceX + "像素");
//                            viewHolder.mSeekBar.visibility = View.VISIBLE//显示进度条

                        } else {
                            // 向左滑动
                            Log.e("MainActivity", "向左滑动" + (-distanceX) + "像素");
//                            viewHolder.mSeekBar.visibility = View.VISIBLE//显示进度条
                        }
                    }
                }
                return true
            }
        })


        //时间显示
//        viewHolder.mMediaMetadataRetriever = MediaMetadataRetriever()
//        // TODO: anr 可能性  需要修改
//        viewHolder.mMediaMetadataRetriever!!.setDataSource(dataList[position].videoUrls[0].url, HashMap())
//        viewHolder.mVideoDuration = viewHolder.mMediaMetadataRetriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong() //获取视频时长
//        viewHolder.mVideoDuration = viewHolder.mListVideoView.duration //获取视频时长
//        viewHolder.mTvTotalTime!!.text = formatTime(viewHolder.mListVideoView.duration) //设置总时长

//        showLog(this, "视频时长===$viewHolder.mVideoDuration")


        /**
         * 子线程解析
         */
//        Thread {
//            try {
//                //大量计算 把关键帧存入 集合 缩略图逻辑
//                for (i in 0..viewHolder.inFactTime) { // 多少秒取多少张 不大于1000张 因为进度条为1000份
//                    val timeUs: Long = (1000 / viewHolder.inFactTime) * i * 1000L * viewHolder.mVideoDuration / viewHolder.mSeekBar.max
//                    val bitmap: Bitmap? = viewHolder.mMediaMetadataRetriever!!.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)
//                    viewHolder.bitmapMap[i] = bitmap!!
//                    // 通过进度条的位置设置bitmap 存入bitmap
//                }
//            } catch (e: IllegalArgumentException) {
//                e.printStackTrace()
//            }
//        }.start()


        //=================================================缩略图 进度条等==================================================




        //没有内容时  隐藏布局
        if(dataList[position].content!=null&&dataList[position].content!=""){
            //富文本布局1  点击展开全文操作
            try { //设置两行数据
                setRichStr(
                    viewHolder.videoRichContent,
                    106f,
                    dataList[position].content,
                    mActivity,
                    dataList[position].atUser,
                    dataList[position].config,
                    " 展开全文",
                    "",
                    1,
                    false
                )
            } catch (e: Exception) {
                viewHolder.videoRichContent.text = dataList[position].content
            }

            viewHolder.videoRich1.visibility = View.VISIBLE
        }else{
            viewHolder.videoRich1.visibility = View.GONE
        }

        viewHolder.videoRichContent.setOpenAndCloseCallback(object :
            ExpandableTextView.OpenAndCloseCallback {
            override fun onOpen() {
                viewHolder.videoRich1.visibility = View.GONE
                viewHolder.videoRich2.visibility = View.VISIBLE
                viewHolder.videoOpenLayout.setBackgroundColor(0x66000000)

                try {
                    setRichStr(
                        viewHolder.videoRichContent2,
                        28f,
                        dataList[position].content,
                        mActivity,
                        dataList[position].atUser,
                        dataList[position].config,
                        " 展开全文",
                        "",
                        1000,
                        false
                    )
                } catch (e: Exception) {
                    viewHolder.videoRichContent2.text = dataList[position].content
                }
            }

            override fun onClose() {

            }
        })

        //头像
        Glide.with(mActivity)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList[position].userInfo.avatarUrl)
            .into(viewHolder.videoListIcon)

        //名字
        viewHolder.videoListUserName.text = dataList[position].userInfo.nickName

        viewHolder.videoListUserName.setOnClickListener {
            /**
             * 跳转到个人中心
             */
            Singleton.startOtherUserCenterActivity(
                mActivity,
                dataList[position].userInfo.id ?: 0L
            )

        }

        viewHolder.videoListIcon.setOnClickListener {
            /**
             * 跳转到个人中心
             */
            Singleton.startOtherUserCenterActivity(
                mActivity,
                dataList[position].userInfo.id ?: 0L
            )
        }
//        //模拟点击外部收起
//        viewHolder.videoOpenLayout.onInterceptTouchEvent(new InterceptTouchRelativeLayout.OnInterceptTouchEventListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@Nullable MotionEvent ev) {
//                //拦截与否只与返回值有关，返回false表示不拦截，否则拦截此次事件。
//                if (viewHolder.videoRich1.getVisibility()==View.GONE){//如果第一层是隐藏状态
//
//                    Log.e("dada112","消费此次事件22");
//
//                    assert ev != null;
//                    if (ev.getAction()==MotionEvent.ACTION_DOWN){//如果按下了
//                        viewHolder.videoRich1.setVisibility(View.VISIBLE);//显示
//                        viewHolder.videoRich2.setVisibility(View.GONE);
//                        viewHolder.videoOpenLayout.setBackgroundColor(0x00000000);
//
//                        try {//设置两行数据
//                            Singleton.INSTANCE.setRichStr(
//                                    viewHolder.videoRichContent,
//                                    106f,
//                                    "美国生态健康联盟主席、病毒专家彼得·达萨克近日撰文驳斥了“新冠病毒并非自然产生，而是人为制造”的有关言论。美国艾奥瓦大学微生物学和免疫学教授斯坦利·珀尔曼表示，将病毒来源问题政治化是“非常不幸的”，病毒溯源是科学问题，需要科学家和医学专家进行研究，并基于事实和证据得出科学结论。近日，美国史密斯菲尔德、泰森等多家肉类加工厂因新冠病毒接连关闭，食品供应链面临断裂。史密斯菲尔德CEO在宣布工厂停工时曾警告说，美国的肉类供应将受到“严重甚至是灾难性的影响”。美国生态健康联盟主席、病毒专家彼得·达萨克近日撰文驳斥了“新冠病毒并非自然产生，而是人为制造”的有关言论。美国艾奥瓦大学微生物学和免疫学教授斯坦利·珀尔曼表示，将病毒来源问题政治化是“非常不幸的”，病毒溯源是科学问题，需要科学家的研究病毒溯源是科学问题，需要科学家的研美国生态健康联盟主席、病毒专家彼得·达萨克近日撰文驳斥了“新冠病毒并非自然产生，而是人为制造”的有关言论。美国艾奥瓦大学微生物学和免疫学教授斯坦利·珀尔曼表示，将病毒来",
//                                    mActivity,
//                                    null,
//                                    null,
//                                    " 展开全文",
//                                    "",
//                                    2,
//                                    false
//                            );
//                        }catch (Exception e){
//                            // TODO: 2023/5/8 需要修改为真实文本
//                            viewHolder.videoRichContent.setText(e.toString());
//                        }
//                        Log.e("dada112","消费此次事件");
//                        return true;//消费此次事件
//                    }else {
//                        Log.e("dada112","不消费此次事件");
//                        return false;//不消费 由子级实现
//                    }
//                }else {
//                    Log.e("dada112","不消费此次事件");
//                    return false;//不消费 由子级实现
//                }
//            }
//        });

        /**
         * 衣装
         */
        if (dataList[position].singleList != null && dataList[position].singleList.size > 0) {
            viewHolder.videoDressImg.visibility = View.VISIBLE
            Glide.with(mActivity)
                .asBitmap()
                .dontAnimate()
                .skipMemoryCache(false)
                .error(R.color.white)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(dataList[position].singleList[0].coverUrl)
                .into(viewHolder.videoDressImg!!)


            /**衣装跳转*/
            viewHolder.dressTry.setOnClickListener {

                val str: StringBuffer = StringBuffer()
                dataList[position].singleList.forEach {
                    str.append(it.id).append(",")
                }

                var str2: String =
                    "[" + str.toString().substring(0, str.toString().length - 1) + "]"

                Log.e("str2", str2)

                Singleton.isNewScene = true
                val intent = Intent(mActivity, AndroidBridgeActivity::class.java)
                intent.putExtra("entryType", "EnterTryMatchPlan")
                intent.putExtra("tryClothes", str2)
                mActivity.startActivity(intent)

            }

        } else {
            viewHolder.videoDressImg.visibility = View.GONE//列表隐藏

            viewHolder.dressTry.visibility=View.GONE//一键试穿隐藏

            viewHolder. commentEditLayout.visibility=View.VISIBLE//评论框显示
            /**弹出评论*/
            viewHolder.commentEditLayout.setOnClickListener {
                mListVideoAdapterInterface!!.onShowCommentPop(dataList[position].id, position)
            }

        }

        viewHolder.videoDressImg.setOnClickListener {
            showDragPopDress(mActivity, dataList[position].singleList)
        }

        //时间
        viewHolder.videoTimeTv.text = Singleton.timeShowRule(dataList[position].createTime, true)

        //收起逻辑
        viewHolder.videoRichCloseTv.setOnClickListener {
            viewHolder.videoRich1.visibility = View.VISIBLE
            viewHolder.videoRich2.visibility = View.GONE
            viewHolder.videoOpenLayout.setBackgroundColor(0x00000000)
            try {
                setRichStr(
                    viewHolder.videoRichContent,
                    106f,
                    dataList[position].content,
                    mActivity,
                    dataList[position].atUser,
                    dataList[position].config,
                    " 展开全文",
                    "",
                    1,
                    false
                )
            } catch (e: Exception) {
                viewHolder.videoRichContent.text = dataList[position].content
            }
        }

        //标题
        if (!dataList[position].title.equals("")) {
            viewHolder.title.text = dataList[position].title
            viewHolder.title.visibility = View.VISIBLE
        } else {
            viewHolder.title.visibility = View.GONE
        }


        //back键
        viewHolder.videoBack.setOnClickListener { mActivity.finish() }

        //分享
        viewHolder.videoShare.singleClick {
            mListVideoAdapterInterface!!.onShare(dataList[position])
        }

        /**显示分享  还是三个点*/
        if (dataList[position].userInfo.id == Singleton.getUserInfo().userId) {//如果当前id==自己的id
            viewHolder.videoShare.setImageResource(R.drawable.home_three_points)
            viewHolder.videoShare.setOnClickListener {
                mListVideoAdapterInterface!!.onShare(dataList[position])
            }
        } else {
            viewHolder.videoShare.setImageResource(R.drawable.video_icon_share_white)
            viewHolder.videoShare.singleClick {
                mListVideoAdapterInterface!!.onShare(dataList[position])
            }
        }


        if (viewHolder.videoPureModeTopPart.visibility == View.VISIBLE) {
            viewHolder.videoPureModeImg.setImageResource(R.drawable.video_pure_mode_open)
        } else {
            viewHolder.videoPureModeImg.setImageResource(R.drawable.video_pure_mode_close)
        }


        viewHolder.videoPureModeImg.setOnClickListener {
            //如果视频宽度大于高度 就可以纯净模式
            if ((dataList[position].videoUrls[0].width > dataList[position].videoUrls[0].height)) {
                mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                viewHolder.mListVideoView.setFull()
            } else {
                if (viewHolder.videoPureModeTopPart.visibility == View.VISIBLE) {
                    viewHolder.videoPureModeImg.setImageResource(R.drawable.video_pure_mode_close)
                    viewHolder.videoRichContent.visibility = View.INVISIBLE
                    viewHolder.title.visibility = View.INVISIBLE
                    viewHolder.videoPureModeTopPart.visibility = View.GONE
                } else {
                    viewHolder.videoPureModeImg.setImageResource(R.drawable.video_pure_mode_open)
                    viewHolder.videoRichContent.visibility = View.VISIBLE
                    viewHolder.title.visibility = View.VISIBLE
                    viewHolder.videoPureModeTopPart.visibility = View.VISIBLE
                }
            }
        }

        //地址
        if (dataList[position].address != null && !dataList[position].address.equals("")) {
            viewHolder.videoAddressLayout.visibility = View.VISIBLE
            viewHolder.videoAddressTv.text = dataList[position].address
        } else {
            viewHolder.videoAddressLayout.visibility = View.GONE
        }


        //点赞
        Singleton.setNumRuler(
            dataList[position].laudCount,
            viewHolder.laudTv,
            Singleton.DEFAULT_LAUD
        )

        if (dataList[position].isLaud == 1) {
            viewHolder.laudPic.setImageResource(R.drawable.home_laud_ed_white)
        } else {
            viewHolder.laudPic.setImageResource(R.drawable.home_laud_white)
        }

        viewHolder.laudPic.setOnClickListener {
            if (dataList[position].isLaud == 1) {
                Log.e("daddada", "取消点赞了")
                viewHolder.laudPic.setImageResource(R.drawable.home_laud_white)
                dataList[position].isLaud = 0
                dataList[position].laudCount = dataList[position].laudCount - 1
                mListVideoAdapterInterface!!.onLaud(false, dataList[position].id)//传递此条动态的ID
            } else {
                Log.e("daddada", "点赞了")
                viewHolder.laudPic.setImageResource(R.drawable.home_laud_ed_white)
                dataList[position].isLaud = 1
                dataList[position].laudCount = dataList[position].laudCount + 1
                mListVideoAdapterInterface!!.onLaud(true, dataList[position].id)//传递此条动态的ID
            }

            //相当于刷新
            Singleton.setNumRuler(
                dataList[position].laudCount,
                viewHolder.laudTv,
                Singleton.DEFAULT_LAUD
            )
        }
        //收藏
        Singleton.setNumRuler(
            dataList[position].collectCount,
            viewHolder.collectTv,
            Singleton.DEFAULT_COLLECT
        )

        if (dataList[position].isCollect == 1) {
            viewHolder.collectPic.setImageResource(R.drawable.home_collect_ed)
        } else {
            viewHolder.collectPic.setImageResource(R.drawable.home_collect_white)
        }

        viewHolder.collectPic.setOnClickListener {
            if (dataList[position].isCollect == 1) {
                Log.e("daddada", "取消收藏了")
                viewHolder.collectPic.setImageResource(R.drawable.home_collect_white)
                dataList[position].isCollect = 0
                dataList[position].collectCount = dataList[position].collectCount - 1
                mListVideoAdapterInterface!!.onCollect(false, dataList[position].id)//传递此条动态的ID
            } else {
                Log.e("daddada", "收藏了")
                viewHolder.collectPic.setImageResource(R.drawable.home_collect_ed)
                dataList[position].isCollect = 1
                dataList[position].collectCount = dataList[position].collectCount + 1
                mListVideoAdapterInterface!!.onCollect(true, dataList[position].id)//传递此条动态的ID
            }

            //相当于刷新
            Singleton.setNumRuler(
                dataList[position].collectCount,
                viewHolder.collectTv,
                Singleton.DEFAULT_COLLECT
            )
        }

        //评论
        Singleton.setNumRuler(
            dataList[position].commentCount,
            viewHolder.commentTv,
            Singleton.DEFAULT_COMMENT
        )

        //弹出评论 回调  通过activity设置
        viewHolder.commentPic.setOnClickListener {
            mListVideoAdapterInterface!!.onShowCommentPop(dataList[position].id, position)
        }

        if (dataList[position].userInfo!=null&&dataList[position].userInfo.id==Singleton.getUserInfo().userId){
            viewHolder.videoCareLayout.visibility = View.GONE
        }else{
            //关注
            if (dataList[position].relation != null ) {
                when(dataList[position].relation){
                    0,2->{
                        viewHolder.videoCareLayout.visibility = View.VISIBLE
                    }
                    1,4->{
                        viewHolder.videoCareLayout.visibility = View.GONE
                    }
                }
            } else {
                viewHolder.videoCareLayout.visibility = View.GONE
            }
        }

        // TODO: 2020/12/3 0003 待修改
        viewHolder.videoCareLayout.setOnClickListener {
            viewHolder.videoCareLayout.visibility = View.GONE
            dataList[position].relation = 1
            mListVideoAdapterInterface!!.onCareUser(dataList[position].userInfo.id)
        }

        viewHolder.mPosition = position
        container.addView(view)
        return view!!
    }

    //销毁条目  viewpager自动调用
    override fun destroyItem(container: ViewGroup, position: Int, mObject: Any) {
        val itemView = mObject as View
        container.removeView(itemView)
        //取消预加载
        PreloadManager.getInstance(container.context)
            .removePreloadTask(dataList[position].videoUrls[0].url)
        //保存起来用来复用
        mViewPool.add(itemView)
    }

    class ViewHolder internal constructor(itemView: View?) {
        var mPosition = 0
        var mThumb: ImageView//封面图
        var videoDressImg: ImageView//衣装
        var videoPureModeTopPart: View
        var videoBack: ImageView
        var videoShare: ImageView
        var videoOpenLayout: InterceptTouchRelativeLayout //根布局 //可拦截事件
        var videoRich1: View//两段布局1
        var videoRichContent: ExpandableTextView//富文本
        var videoPureModeImg: ImageView
        var videoRich2: View//两段布局2
        var videoRichContent2: ExpandableTextView
        var videoAddressLayout: View
        var videoAddressTv: TextView
        var videoTimeTv: TextView
        var videoRichCloseTv: TextView
        var mListVideoView: VideoView
        var mPlayerContainer: FrameLayout
        var videoListIcon: ImageView
        var videoListUserName: TextView

        //显示缩略图需要隐藏的控件
        var videoBottom1: View
        var videoBottomPart1: View

        //底部逻辑
        var laudPic: ImageView
        var laudTv: TextView
        var collectPic: ImageView
        var collectTv: TextView
        var commentPic: ImageView
        var commentTv: TextView
        var dressPic: ImageView
        var dressTv: TextView
        var dressTry: View//一键试穿
        var videoCareLayout: View//关注
        var title: TextView//关注
        var commentEditLayout: View//评论弹窗


        /**
         * 缩略图
         */
        var mSeekBar: SeekBar
        var mPbBottomProgress: ProgressBar//关注
        var mThumbnailImageView: ImageView? = null
        var timeLayout: View? = null
        var mTvCurrTime: TextView? = null
        var mTvTotalTime: TextView? = null
        var mMediaMetadataRetriever: MediaMetadataRetriever? = null
        var mVideoDuration: Long = 0
        var inFactTime = 1 //实际要取的张数
        var mllBottomContainer: View? = null

        //        val bitmapMap: Map<Int, Bitmap> = HashMap()
        val bitmapMap = HashMap<Int, Bitmap>()
        var tempIndex = -1
        var progressTime = 0

        init {
            mListVideoView = itemView!!.findViewById<VideoView>(R.id.listVideoView_View)
            mPlayerContainer = itemView.findViewById<FrameLayout>(R.id.container)

            mThumb = mListVideoView.findViewById<ImageView>(R.id.iv_thumb)
            videoDressImg = itemView.findViewById<ImageView>(R.id.video_dress_img)
            title = itemView.findViewById<TextView>(R.id.title)

            videoBack = itemView.findViewById<ImageView>(R.id.video_back)
            videoShare = itemView.findViewById<ImageView>(R.id.video_share)
            videoBottom1 = itemView.findViewById<View>(R.id.video_bottom_1) //底部bottom 用于确定弹窗位置
            videoBottomPart1 =
                itemView.findViewById<View>(R.id.video_bottom_part_1) //底部bottom 用于确定弹窗位置

            videoPureModeImg = itemView.findViewById<ImageView>(R.id.video_pure_mode_img) //纯净模式按钮
            videoPureModeTopPart =
                itemView.findViewById<View>(R.id.video_pure_mode_top_part) //纯净模式需要隐藏的
            videoRichContent =
                itemView.findViewById<ExpandableTextView>(R.id.video_rich_content) //纯净模式需要invisible
            commentPic = itemView.findViewById<ImageView>(R.id.comment_pic) //纯净模式需要invisible
            videoOpenLayout =
                itemView.findViewById<InterceptTouchRelativeLayout>(R.id.video_open_layout)
            videoRich1 = itemView.findViewById<View>(R.id.video_rich_1)//没有内容时隐藏这个
            videoRich2 = itemView.findViewById<View>(R.id.video_rich_2)
            videoRichContent2 = itemView.findViewById<ExpandableTextView>(R.id.video_rich_content2)
            videoTimeTv = itemView.findViewById<TextView>(R.id.video_time_tv)
            videoAddressTv = itemView.findViewById<TextView>(R.id.video_address_tv)
            videoAddressLayout = itemView.findViewById<View>(R.id.video_address_layout)
            videoRichCloseTv = itemView.findViewById<TextView>(R.id.video_rich_close_tv)
            videoListIcon = itemView.findViewById<ImageView>(R.id.video_list_icon)
            videoListUserName = itemView.findViewById<TextView>(R.id.video_list_username)
            //底部逻辑
            laudPic = itemView.findViewById<ImageView>(R.id.laud_pic)
            laudTv = itemView.findViewById<TextView>(R.id.laud_tv)
            collectPic = itemView.findViewById<ImageView>(R.id.collect_pic)
            collectTv = itemView.findViewById<TextView>(R.id.collect_tv)
            commentTv = itemView.findViewById<TextView>(R.id.comment_tv)
            commentPic = itemView.findViewById<ImageView>(R.id.comment_pic)
            dressPic = itemView.findViewById<ImageView>(R.id.dress_pic)
            dressTv = itemView.findViewById<TextView>(R.id.dress_tv)
            dressTry = itemView.findViewById<View>(R.id.dress_try)

            videoCareLayout = itemView.findViewById<View>(R.id.video_care_layout)

            commentEditLayout = itemView.findViewById<View>(R.id.comment_edit_layout)


            //缩略图
            mSeekBar = itemView.findViewById<SeekBar>(R.id.video_seek_bar)//进度条
            timeLayout = itemView.findViewById<View>(R.id.time_layout)
            mTvCurrTime = itemView.findViewById<TextView>(R.id.tv_curr_time)
            mTvTotalTime = itemView.findViewById<TextView>(R.id.tv_total_time)
            mThumbnailImageView = itemView.findViewById<ImageView>(R.id.thumbnail_image_view)
            mllBottomContainer = itemView.findViewById<View>(R.id.ll_bottom_container)
            mPbBottomProgress = itemView.findViewById<ProgressBar>(R.id.pb_bottom_progress)//底部加载进度条

            itemView.tag = this
        }
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<WaterfallFeedBean.Item.Info>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyDataSetChanged()
        }
    }

    /**
     * 回调
     */
    interface ListVideoAdapterInterface {
        fun onShowCommentPop(id: Long?, position: Int)
        fun onLaud(isLaud: Boolean, id: Long?)
        fun onCollect(isCollect: Boolean, id: Long?)
        fun onLoadMore()
        fun onCareUser(id: Long?)
        fun onShare(id: WaterfallFeedBean.Item.Info)
    }

    fun setListVideoAdapterCallBack(mListVideoAdapterInterface: ListVideoAdapterInterface?) {
        this.mListVideoAdapterInterface = mListVideoAdapterInterface
    }

    private var mListVideoAdapterInterface: ListVideoAdapterInterface? = null

}