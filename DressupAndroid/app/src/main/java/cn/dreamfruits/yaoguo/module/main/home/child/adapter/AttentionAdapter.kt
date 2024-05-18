package cn.dreamfruits.yaoguo.module.main.home.child.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.*
import cn.dreamfruits.yaoguo.module.main.home.phonebook.FindUserActivity
import cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.onlyimg.MyPagerAdapter
import cn.dreamfruits.yaoguo.module.main.home.vlistvideo.ListVideoActivity
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zr.video.ui.view.CustomPrepareView

/**
 * @Author wq
 * @Date 2023/2/22-17:21
 */
class AttentionAdapter(
    private var mContent: Context,
    private var dataList: MutableList<WaterfallFeedBean.Item.Info>,
) :
    RecyclerView.Adapter<AttentionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_attention, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {

        /**预加载回调*/
        if (dataList.size > 3 && position == dataList.size - 3) {
            mInterface!!.onPreLoad()
        }
        // TODO: 隐藏关注页的点赞动效 因为设置全屏，会影响item的高度
//        if (holder.loveView!=null){
//            holder.loveView!!.visibility=View.GONE
//        }


        if (dataList[position].address != null && dataList[position].address != "") {
            holder.homeAttentionAddress!!.text = dataList[position].address
            holder.homeAttentionAddressImg!!.visibility = View.VISIBLE
        } else {
            holder.homeAttentionAddressImg!!.visibility = View.GONE
        }

        holder.homeAttentionCollectCount!!.text = dataList[position].collectCount.toString() ?: ""
        if (dataList[position].title.isNullOrEmpty()) {
            holder.homeAttentionTitle!!.visibility = View.GONE
        } else {
            holder.homeAttentionTitle!!.visibility = View.VISIBLE
            holder.homeAttentionTitle!!.text = dataList[position].title ?: ""
        }


        //评论数字
        Singleton.setNumRuler(
            dataList[position].commentCount,
            holder.homeAttentionCommentCount!!,
            Singleton.DEFAULT_COMMENT
        )


        holder.homeAttentionCreateTime!!.text =
            Singleton.timeShowRule(dataList[position].createTime, true)
        holder.homeAttentionLaudCount!!.text = dataList[position].laudCount.toString() ?: ""

        if (dataList[position].userInfo != null) {
            holder.homeAttentionNickName!!.text = dataList[position].userInfo.nickName ?: ""
        }

        /**点击姓名*/
        holder.homeAttentionNickName!!.setOnClickListener {
            /**跳转到个人中心*/
            Singleton.startOtherUserCenterActivity(mContent, dataList[position].userInfo.id)
        }

        /**
         * 评论两条
         */
        if (dataList[position].commentList != null && dataList[position].commentList.size >= 2) {//有评论
            holder.homeAttentionCommentLayout!!.visibility = View.VISIBLE
            holder.homeAttentionCommentLayoutT1!!.text =
                dataList[position].commentList[0].commentUserInfo.nickName + ": "
            holder.homeAttentionCommentLayoutT11!!.text = dataList[position].commentList[0].content
            holder.homeAttentionCommentLayoutT2!!.text =
                dataList[position].commentList[1].commentUserInfo.nickName + ": "
            holder.homeAttentionCommentLayoutT21!!.text = dataList[position].commentList[1].content
            holder.homeAttentionCommentLayout!!.setOnClickListener {
                mInterface!!.onComment(dataList[position].id)
            }
        } else {
            holder.homeAttentionCommentLayout!!.visibility = View.GONE
        }

        /**
         * 评论弹窗
         */
        holder.commentLayout!!.setOnClickListener {
            mInterface!!.onComment(dataList[position].id)
        }
        /**
         * 头像
         */
        if (dataList[position].userInfo != null) {
            Glide.with(mContent)
                .asBitmap()
                .dontAnimate()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .error(R.drawable.temp_icon)
                .load(dataList[position].userInfo.avatarUrl)
                .into(holder.homeAttentionIcon!!)
        }
        holder.homeAttentionIcon!!.setOnClickListener {
            /**跳转到个人中心*/
            Singleton.startOtherUserCenterActivity(mContent, dataList[position].userInfo.id)
        }


        /**
         * 为你推荐 查看更多 逻辑
         */
        holder.mForYouLookMore!!.setOnClickListener {
            val intent = Intent(mContent, FindUserActivity::class.java)//详情页
            ActivityUtils.startActivity(intent)
        }

        /**
         * 点赞逻辑
         */
        Singleton.setNumRuler(
            dataList[position].laudCount ?: 0,
            holder.homeAttentionLaudCount!!,
            Singleton.DEFAULT_LAUD
        )

        if (dataList[position].isLaud == 1) {
            holder.homeAttentionLaudImg!!.setImageResource(R.drawable.home_thumb_ed)
        } else {
            holder.homeAttentionLaudImg!!.setImageResource(R.drawable.home_thumb)
        }

        holder.laudLayout!!.setOnClickListener {
            if (dataList[position].isLaud == 1) {
                Log.e("daddada", "取消点赞了")
                holder.homeAttentionLaudImg!!.setImageResource(R.drawable.home_thumb)
                dataList[position].isLaud = 0
                dataList[position].laudCount = dataList[position].laudCount - 1
                mInterface!!.onLaud(false, dataList[position].id)//传递此条动态的ID
            } else {
                Log.e("daddada", "点赞了")
                holder.homeAttentionLaudImg!!.setImageResource(R.drawable.home_thumb_ed)
                dataList[position].isLaud = 1
                dataList[position].laudCount = dataList[position].laudCount + 1
                mInterface!!.onLaud(true, dataList[position].id)//传递此条动态的ID
            }

            //刷新
            Singleton.setNumRuler(
                dataList[position].laudCount ?: 0,
                holder.homeAttentionLaudCount!!,
                Singleton.DEFAULT_LAUD
            )
        }

        /**
         * 收藏逻辑
         */
        Singleton.setNumRuler(
            dataList[position].collectCount,
            holder.homeAttentionCollectCount!!,
            Singleton.DEFAULT_COLLECT
        )

        if (dataList[position].isCollect == 1) {
            holder.homeAttentionCollectImg!!.setImageResource(R.drawable.home_collect_ed)
        } else {
            holder.homeAttentionCollectImg!!.setImageResource(R.drawable.home_collect)
        }

        holder.collectLayout!!.setOnClickListener {
            if (dataList[position].isCollect == 1) {
                Log.e("daddada", "取消收藏了")
                holder.homeAttentionCollectImg!!.setImageResource(R.drawable.home_collect)
                dataList[position].isCollect = 0
                dataList[position].collectCount = dataList[position].collectCount - 1
                mInterface!!.onCollect(false, 0, dataList[position].id, "")//传递此条动态的ID
            } else {
                Log.e("daddada", "收藏了")
                holder.homeAttentionCollectImg!!.setImageResource(R.drawable.home_collect_ed)
                dataList[position].isCollect = 1
                dataList[position].collectCount = dataList[position].collectCount + 1
                mInterface!!.onCollect(true, 0, dataList[position].id, "")//传递此条动态的ID
            }

            //刷新
            Singleton.setNumRuler(
                dataList[position].collectCount,
                holder.homeAttentionCollectCount!!,
                Singleton.DEFAULT_COLLECT
            )
        }

        /**
         * 评论逻辑
         */

        /**
         *分享逻辑
         */

        /**
         * 视频逻辑
         */
        when (dataList[position].type) {//0-图文动态，1-视频动态，2-广告
            0 -> {
                holder.bannerLayout!!.visibility = View.VISIBLE
                holder.viewPager!!.visibility = View.VISIBLE
                holder.mPlayerContainer!!.visibility = View.GONE
                holder.homeAttentionMute!!.visibility = View.GONE

                if (dataList[position].picUrls != null) {
                    val pagerAdapter = MyPagerAdapter(
                        mContent,
                        dataList!![position].id,
                        dataList!![position].picUrls[0].height,
                        dataList!![position].picUrls[0].width
                    )
                    pagerAdapter.setImages(dataList[position].picUrls)//设置图片url

                    Log.e("daddada", "图片数量" + dataList[position].picUrls.size)
                    dataList[position].picUrls.forEach {
                        Log.e("daddada", "图片地址" + it.url)
                    }

                    holder.viewPager!!.adapter = pagerAdapter

                    //图片比例
                    val displayMetrics = mContent.resources.displayMetrics
                    val w2 = displayMetrics.widthPixels.toFloat() //需要占的宽度 关注页面是屏幕宽度
                    val w = dataList[position].picUrls[0].width
                    val h = dataList[position].picUrls[0].height
                    var h2: Float = 0.0f
                    try {
                        when (Singleton.getImageType(w, h)) {
                            1 -> {//比例小于3：4   就为3：4
                                h2 = w2 / (3.0f / 4.0f)
                            }
                            2 -> {//比例大于3：4  小于4：3  就为1：1
                                h2 = w2
                            }
                            3 -> {//比例大于4：3  就为4：3
                                h2 = w2 / (4.0f / 3.0f)
                            }
                            4 -> {//图片宽高比异常
                                h2 = w2
                            }
                        }
                        val params: ViewGroup.LayoutParams = holder.viewPager!!.layoutParams
                        params.height = h2.toInt()
                        holder.viewPager!!.layoutParams = params
                    } catch (e: Exception) {
                        val params: ViewGroup.LayoutParams = holder.viewPager!!.layoutParams
                        params.height = w2.toInt()
                        holder.viewPager!!.layoutParams = params
                    }

                    val count = dataList[position].picUrls.size
                    holder.dotIndicatorView!!.setCount1(count)//图片指示器  需要设置小点数量
                    if (count <= 1) {
                        holder.imgNumber!!.visibility = View.GONE
                        holder.dotIndicatorView!!.visibility = View.INVISIBLE
                    } else {
                        holder.imgNumber!!.text = "${1}/${count}"
                        holder.imgNumber!!.visibility = View.VISIBLE
                        holder.dotIndicatorView!!.visibility = View.VISIBLE
                    }
                    holder.viewPager!!.addOnPageChangeListener(object :
                        ViewPager.OnPageChangeListener {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int,
                        ) {

                        }

                        override fun onPageSelected(position: Int) {
                            /**
                             * 绑定指示器
                             */
                            holder.dotIndicatorView!!.setSelectedIndex(position)
                            holder.imgNumber!!.text = "${position + 1}/${count}"
                        }

                        override fun onPageScrollStateChanged(state: Int) {

                        }
                    })
                }
            }
            1 -> {//视频
                if (dataList[position].videoUrls != null) {
                    holder.mPlayerContainer!!.visibility = View.VISIBLE
                    holder.homeAttentionMute!!.visibility = View.VISIBLE
                    holder.viewPager!!.visibility = View.GONE
                    holder.bannerLayout!!.visibility = View.GONE

                    //图片比例
                    val displayMetrics = mContent.resources.displayMetrics
                    val w2 = displayMetrics.widthPixels.toFloat() //需要占的宽度 关注页面是屏幕宽度
                    val w = dataList[position].videoUrls[0].width
                    val h = dataList[position].videoUrls[0].height

                    Log.e("daddada2121", "视频宽高" + w + "  " + h)
                    Log.e("daddada2121", "w2" + w2)
                    var h2: Float = 0.0f
                    try {
                        when (Singleton.getImageType(w, h)) {
                            1 -> {//比例小于3：4   就为3：4
                                h2 = w2 / (3.0f / 4.0f)
                                Log.e("daddada2121", "h2" + h2)
                            }
                            2 -> {//比例大于3：4  小于4：3  就为1：1
                                h2 = w2
                            }
                            3 -> {//比例大于4：3  就为4：3
                                h2 = w2 / (4.0f / 3.0f)
                            }
                            4 -> {//图片宽高比异常
                                h2 = w2
                            }
                        }
                        val params: ViewGroup.LayoutParams = holder.mPlayerContainer!!.layoutParams
                        params.height = h2.toInt()
                        Log.e("daddada2121", "h2h2" + h2)

                        holder.mPlayerContainer!!.layoutParams = params
                    } catch (e: Exception) {
                        val params: ViewGroup.LayoutParams = holder.mPlayerContainer!!.layoutParams
                        params.height = w2.toInt()
                        holder.mPlayerContainer!!.layoutParams = params
                    }

                    //视频长按保存
                    holder.mPlayerContainer!!.setOnLongClickListener {
                        Singleton.showSavePhotoAndVideo(
                            dataList[position].videoUrls[0].url,
                            mContent as Activity,
                            false
                        )
                        true
                    }

                    //点击跳转视频播放页面
                    holder.mPlayerContainer!!.setOnClickListener {
                        val intent = Intent(mContent, ListVideoActivity::class.java)//视频列表页面
                        intent.putExtra("feedId", dataList[position].id)
                        intent.putExtra("position", position)//位置
                        Singleton.videoTag = 1
                        ActivityUtils.startActivity(intent)
                    }

                    // TODO: 视频封面
                    Glide.with(mContent)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(dataList[position].picUrls[0].url)
                        .into(holder.mThumb!!)
                }
            }
            2 -> {

            }
        }

        /**
         * 静音逻辑
         */
        if (dataList[position].muteFlag) {
            holder.homeAttentionMute!!.setImageResource(R.drawable.home_open_sound)
        } else {
            holder.homeAttentionMute!!.setImageResource(R.drawable.home_close_sound)
        }

        holder.homeAttentionMute!!.setOnClickListener {
            if (dataList[position].muteFlag) {
                mInterface!!.onMute(false)//静音回调
                holder.homeAttentionMute!!.setImageResource(R.drawable.home_close_sound)
                dataList.forEach {
                    it.muteFlag = false
                }
            } else {
                mInterface!!.onMute(true)//静音回调
                holder.homeAttentionMute!!.setImageResource(R.drawable.home_open_sound)
                dataList.forEach {
                    it.muteFlag = true
                }
            }
            notifyItemRangeChanged(0, dataList.size)//此种方式刷新不会影响播放视频
        }

        /**
         * 文本展开逻辑
         */
        if (dataList[position].content != null && dataList[position].content != "") {
            holder.homeAttentionContent!!.visibility = View.VISIBLE
            Singleton.setRichStr(
                holder.homeAttentionContent!!,
                14f,
                dataList[position].content,
                mContent,//上下文
                dataList[position].atUser.toMutableList(),
                dataList[position].config.toMutableList(),
                " 展开",
                "",
                1,
                false
            )
            Log.e("daddada2121", "atUser" + dataList[position].atUser.toMutableList())
            Log.e("daddada2121", "config" + dataList[position].config.toMutableList())
            Log.e("daddada2121", "content" + dataList[position].content)
        } else {
            holder.homeAttentionContent!!.visibility = View.GONE
        }

        /**
         * 判断1 是否显示为你推荐，设置判断条件
         */
        if (Singleton.isCarePageNoData && dataList.size == 1) {//关注没有数据  把下部分隐藏 上面部分显示
            holder.mHomeAttentionLayout!!.visibility = View.GONE
            holder.recyclerViewForYou!!.visibility = View.VISIBLE
        } else {//其他情况
            holder.mHomeAttentionLayout!!.visibility = View.VISIBLE
        }


        if (dataList[position].isShowForYouView) {
            holder.recyclerViewForYou!!.visibility = View.VISIBLE
            //为你推荐recyclerview
            var mLayoutManager =
                LinearLayoutManager(mContent, LinearLayoutManager.HORIZONTAL, false); //在此处修改水平or垂直
            holder.attentionRecommendForYouRecyclerview!!.layoutManager = mLayoutManager
            attentionRecommendForYouAdapter = AttentionRecommendForYouAdapter(
                mContent,
                dataList[position].homeRecommendUserListBean.list.toMutableList()
            )
            /**
             * 为你推荐的回调
             */
            attentionRecommendForYouAdapter!!.setOnItemClickListener(object :
                AttentionRecommendForYouAdapter.MForYouInterface {
                @SuppressLint("NotifyDataSetChanged")
                override fun onclick(id: Long, type: Int, flag: Int) {
                    mInterface!!.onForYou(id, type, flag)
                    if (flag == -1) {//-1标记表示 为你推荐已经全部忽略完 需要隐藏该列表全部显示区域
                        dataList.forEach {
                            it.isShowForYouView = false
                        }
                        notifyItemChanged(position)//仅刷新本条
                    }
                }
            })
            holder.attentionRecommendForYouRecyclerview!!.adapter = attentionRecommendForYouAdapter
        } else {
            holder.recyclerViewForYou!!.visibility = View.GONE
        }

        /**
         * 衣装适配器
         */
        if (dataList[position].singleList != null) {
            holder.attentionDressMaxRecyclerView!!.visibility = View.VISIBLE
            dress(holder, position)
        } else {
            holder.attentionDressMaxRecyclerView!!.visibility = View.GONE
        }
        /**
         * 绑定position
         */
        holder.mPosition = position
        if (position == 0) {
            holder.viewTop!!.visibility = View.VISIBLE
        } else {
            holder.viewTop!!.visibility = View.GONE
        }

        /**
         * 点击分享
         */
        holder.shareImg!!.setOnClickListener {

            mInterface!!.onShare(
                position,
                if (dataList[position].userInfo.id == Singleton.getLoginInfo().userId) 0 else 1,
                dataList[position]
            )

        }
    }

    var attentionRecommendForYouAdapter: AttentionRecommendForYouAdapter? = null

    /**
     * 衣装列表
     */
    private fun dress(holder: MyViewHolder, position: Int) {
        var mLayoutManager = LinearLayoutManager(mContent, LinearLayoutManager.HORIZONTAL, false)
        holder.attentionDressMaxRecyclerView!!.layoutManager = mLayoutManager

        var attentionDressAdapter =
            AttentionDressAdapter(mContent, dataList[position].singleList.toMutableList())
        holder.attentionDressMaxRecyclerView!!.adapter = attentionDressAdapter
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
            //静音逻辑处理  以保持一致
            // TODO: 临时处理 之后修改 
            try {
                tempList.forEach {
                    it.muteFlag = dataList[0].muteFlag
                }
            } catch (e: Exception) {

            }


            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size + tempList.size)//避免播放视频停止
        }

    }

    class MyViewHolder : RecyclerView.ViewHolder {
        var mPosition = 0
        var mPlayerContainer: FrameLayout? = null
        var mPrepareView: CustomPrepareView? = null
        var mThumb: ImageView? = null
        var attentionRecommendForYouRecyclerview: MaxRecyclerView? = null
        var attentionDressMaxRecyclerView: MaxRecyclerView? = null
        var homeAttentionAddress: TextView? = null
        var homeAttentionAddressImg: ImageView? = null
        var homeAttentionContent: ExpandableTextView? = null
        var homeAttentionCollectCount: TextView? = null
        var homeAttentionTitle: TextView? = null
        var homeAttentionCommentCount: TextView? = null
        var homeAttentionCreateTime: TextView? = null
        var homeAttentionLaudCount: TextView? = null
        var homeAttentionNickName: TextView? = null
        var recyclerViewForYou: View? = null
        var homeAttentionMute: ImageView? = null
        var mHomeAttentionLayout: View? = null
        var mForYouLookMore: View? = null
        var homeAttentionCommentLayout: View? = null
        var homeAttentionCommentLayoutT1: TextView? = null
        var homeAttentionCommentLayoutT11: TextView? = null
        var homeAttentionCommentLayoutT2: TextView? = null
        var homeAttentionCommentLayoutT21: TextView? = null
        var homeAttentionLaudImg: ImageView? = null

        var commentLayout: View? = null
        var collectLayout: View? = null
        var laudLayout: View? = null
//        var loveView: Love? = null

        //图片
        var viewPager: ChildViewPager? = null
        var bannerLayout: View? = null
        var imgNumber: TextView? = null//图片翻页数字
        var dotIndicatorView: DotIndicatorView? = null//banner小圆点
        var homeAttentionCollectImg: ImageView? = null
        var homeAttentionIcon: ImageView? = null

        var viewTop: View? = null

        var shareImg: ImageView? = null

        constructor(itemView: View) : super(itemView) {
            /**
             * 图片banner
             */
            bannerLayout = itemView.findViewById(R.id.banner_layout)
            viewPager = itemView.findViewById(R.id.viewPager)
            imgNumber = itemView.findViewById(R.id.img_number)
            dotIndicatorView = itemView.findViewById(R.id.dot_indicator_view)

            /**
             * 点赞
             */
//            loveView=itemView.findViewById(R.id.love_view)

            /**
             * 视频
             */
            mPlayerContainer = itemView.findViewById(R.id.player_container)
            mPrepareView = itemView.findViewById(R.id.prepare_view)
            mThumb = mPrepareView!!.thumb

            attentionRecommendForYouRecyclerview =
                itemView.findViewById(R.id.home_attention_recommend_for_you_recyclerview)
            attentionDressMaxRecyclerView =
                itemView.findViewById(R.id.attention_dress_MaxRecyclerView)

            homeAttentionAddress = itemView.findViewById(R.id.home_attention_address)
            homeAttentionAddressImg = itemView.findViewById(R.id.home_attention_address_img)
            homeAttentionContent = itemView.findViewById(R.id.expanded_text)
            viewTop = itemView.findViewById(R.id.view_top)





            commentLayout = itemView.findViewById(R.id.comment_layout) //评论布局
            collectLayout = itemView.findViewById(R.id.collect_layout)//收藏
            laudLayout = itemView.findViewById(R.id.laud_layout)//点赞

            homeAttentionTitle = itemView.findViewById(R.id.home_attention_title)
            homeAttentionCommentCount = itemView.findViewById(R.id.home_attention_commentCount)
            homeAttentionCreateTime = itemView.findViewById(R.id.home_attention_createTime)

            homeAttentionNickName = itemView.findViewById(R.id.home_attention_nickName)
            homeAttentionMute = itemView.findViewById(R.id.video_mute)//静音键
            recyclerViewForYou = itemView.findViewById(R.id.recyclerView_forYou)//为你推荐列表
            mForYouLookMore = itemView.findViewById(R.id.forYou_look_more)//为你推荐查看更多
            homeAttentionIcon = itemView.findViewById(R.id.home_attention_icon)//头像

            homeAttentionLaudImg = itemView.findViewById(R.id.home_attention_laud_img)//点赞
            homeAttentionLaudCount = itemView.findViewById(R.id.home_attention_laudCount)

            homeAttentionCollectImg = itemView.findViewById(R.id.home_attention_collect_img)//收藏
            homeAttentionCollectCount = itemView.findViewById(R.id.home_attention_collectCount)

            homeAttentionCommentLayout =
                itemView.findViewById(R.id.home_attention_comment_layout)//评论布局
            homeAttentionCommentLayoutT1 =
                itemView.findViewById(R.id.home_attention_comment_layout_t1)//评论1姓名
            homeAttentionCommentLayoutT11 =
                itemView.findViewById(R.id.home_attention_comment_layout_t2)//评论1评论
            homeAttentionCommentLayoutT2 =
                itemView.findViewById(R.id.home_attention_comment_layout_t3)//评论2姓名
            homeAttentionCommentLayoutT21 =
                itemView.findViewById(R.id.home_attention_comment_layout_t4)//评论2评论


            shareImg = itemView.findViewById(R.id.share)//


            mHomeAttentionLayout = itemView.findViewById(R.id.home_attention_layout)
            itemView.tag = this //通过tag将ViewHolder和itemView绑定
        }
    }


    //回调
    interface InnerInterface {
        fun onComment(id: Long)//评论弹窗回调
        fun onForYou(id: Long, type: Int, flag: Int)// 为你推荐回调
        fun onMute(flag: Boolean)//静音回调
        fun onSave(position: Int, id: Int)//长按回调 保存图片
        fun onCollect(isCollect: Boolean, type: Long, targetId: Long, outfitStr: String)//收藏
        fun onLaud(isLaud: Boolean, id: Long)//点赞
        fun onPreLoad()//预加载
        fun onShare(id: Int, tag: Int, info: WaterfallFeedBean.Item.Info)//分享

    }

    private var mInterface: InnerInterface? = null

    fun setAttentionAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

    fun delItem(position: Int) {
        if (dataList.size != 0) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }

    }
}




