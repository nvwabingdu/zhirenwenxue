package cn.dreamfruits.yaoguo.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.NetworkUtilsJava
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.SaveImageOrVideo
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.singledress.SearchSingleDressAdapter
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView2
import cn.dreamfruits.yaoguo.module.main.mine.otherusercenter.UserCenterActivity
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean
import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean
import cn.dreamfruits.yaoguo.util.Singleton.fixTimeText
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.GsonUtils
import com.example.dragpopwindow.CustomDragPop
import com.example.dragpopwindow.MyRelativeLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zr.video.bridge.ControlWrapper
import java.util.*

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 10:35
 */
object Singleton {

    var mRecommendFeedList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//推荐页面的feed列表
    var mTempGetLabelFeedList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//装tab页面的逻辑

    var mCareFeedList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//关注页面的feed列表
    var mSearchPostList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//搜索帖子
    var mLabelDetailsList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//话题详情页的feed列表
    var mMessageVideoList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//互动消息页面的feed列表

    var mShareVideoList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//分享页面的

    var mFashionWearList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//潮流精选页面的feed列表


    var mMineFeedListSingle: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//个人中心页面的feed列表

    var videoTag: Int = 0//用来标记是否是推荐页面的视频 还是关注页面的 还是搜索页面的 还是话题详情页的 还是话题

    var isMine: Int = 0//是否是个人中心页面  在第一次的时候设置为true 避免之后刷新装入他人数据
    var searchWord: String = ""//搜索优化  用于搜索跳转到帖子页回显搜索词

    const val DEFAULT_COLLECT = "收藏"
    const val DEFAULT_LAUD = "点赞"
    const val DEFAULT_WEAR = "衣装"
    const val DEFAULT_COMMENT = "评论"

    const val CARE_TEXT = "已关注"
    const val UN_CARE_TEXT = "已取消关注"

    var isNewScene = false//是否是新场景

    var isFirstStartUnity = false//是否是第一次启动unity

    //图片  视频 映射表
    private val urlMap = mutableMapOf<String, String>()


    //服装单品详情页 相似推荐请求数据 总共30条
    var mSimilarWearList: MutableList<GetSimilarRecommendBean.Item>? = ArrayList()


    //给定一个图片 视频链接  返回鉴权后的视频图片链接
    fun getUrlX(url: String, isPic: Boolean,isZipNum:Int=30): String {
        if (url == "") return ""
        if (!urlMap.containsKey(url)) {
            urlMap[url] = Tool().decodePicUrls(url, "0", isPic,isZipNum)
            return urlMap[url]!!
        } else {
            // 存在 myKey，执行相应代码
            return urlMap[url]!!

        }
        return ""
    }

    /**
     * 首页推荐和话题返回数据 转化为 WaterfallFeedBean.Item.Info   的list
     */
    fun changeList(bean: WaterfallFeedBean?): MutableList<WaterfallFeedBean.Item.Info>? {
        var tempList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//推荐页面的feed列表
        if (bean != null) {
            for (i in 0 until bean.list.size) {
                tempList!!.add(bean.list[i].info)
            }
        } else {
            return tempList
        }

        return tempList
    }

    fun setMessageNum(tv: TextView, num: Int) {
        if (num > 0) {
            tv.visibility = View.VISIBLE
            if (num > 99) {
                tv.text = "99+"
            } else {
                tv.text = num.toString()
            }
        } else {
            tv.visibility = View.GONE
        }
    }

    /**
     * 图片比例类型
     */
    fun getImageType(width: Int, height: Int): Int {
        // 计算宽高比
        val aspectRatio = width.toDouble() / height.toDouble()
        Log.e("daddada2121", "图片宽高比" + aspectRatio)
        try {
            // 判断哪种类型
            return when {
                aspectRatio <= (3.0 / 4.0) -> 1//比例小于3：4   就为3：4
                aspectRatio > (3.0 / 4.0) && aspectRatio < (4.0 / 3.0) -> 2//比例大于3：4  小于4：3  就为1：1
                aspectRatio >= (4.0 / 3.0) -> 3//比例大于4：3  就为4：3
                else -> 2
            }
        } catch (e: Exception) {
            Log.e("zqr", "图片宽高比异常" + e.message)
            return 4
        }
    }

    /**
     * 关注页是否没有数据  仅显示为你推荐
     */
    var isCarePageNoData = false


    /**
     * 文本设置规则
     */
    fun setNumRuler(count: Int, tv: TextView, defaultStr: String) {
        if (count == 0) {
            tv.text = defaultStr
        } else if (count > 9999) {
            tv.text = "${ToolJava.getDecimalFormatOne(count)}万"
        } else {
            tv.text = count.toString()
        }
    }

    //重载方法  不用换TextView
    fun setNumRuler(count: Int, defaultStr: String): String {
        if (count == 0) {
            return defaultStr
        } else if (count > 9999) {
            return "${ToolJava.getDecimalFormatOne(count)}万"
        } else {
            return count.toString()
        }
    }

    /**
     * 文本设置规则2
     */
    fun setNumRuler2(tv: TextView, defaultStr: String, isAdd: Boolean) {
        var temp = 0
        if (tv.text.equals(defaultStr)) {
            temp = 0
        } else {
            temp = tv.text.toString().toInt()
        }
        if (isAdd) {
            setNumRuler(
                temp + 1,
                tv,
                defaultStr
            )
        } else {
            setNumRuler(
                temp - 1,
                tv,
                defaultStr
            )
        }

    }


    var lastRunTime = 0L // 上次运行时间
    val lock = Any() // 锁对象center

    /**
     * 判断是否有网络 toast提示
     */
    fun isNetConnectedToast(context: Context) {
        synchronized(lock) { // 加锁避免并发问题
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastRunTime >= 3000) { // 时间间隔超过3秒，可以运行方法
                lastRunTime = currentTime
                try {
                    if (!NetworkUtilsJava.isNetworkConnected(context)) {
                        //吐司
                        val toast = Toast(context)
                        val view: View =
                            LayoutInflater.from(context)
                                .inflate(cn.dreamfruits.yaoguo.R.layout.home_dialog_net_error, null)
                        toast.setView(view)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                } catch (e: Exception) {

                }
                // TODO: 在这里写下你要执行的方法
            }
        }
    }

    /**
     * 判断是否有网络 PopupWindow提示 考虑是否有内存泄露的风险
     */
    fun isNetConnectedPopupWindow(context: Context) {
        try {
            if (!NetworkUtilsJava.isNetworkConnected(context)) {
                var popupWindow: PopupWindow? = null
                val inflate = View.inflate(
                    context,
                    cn.dreamfruits.yaoguo.R.layout.home_dialog_net_error,
                    null
                )
                if (popupWindow !== null && popupWindow!!.isShowing) {

                } else {
                    popupWindow = PopupWindow(
                        inflate,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    popupWindow!!.isTouchable = true
                    popupWindow!!.isFocusable = true
                    popupWindow!!.showAtLocation(inflate, Gravity.CENTER, 0, 0)
                }
            }
        } catch (e: Exception) {

        }
    }


    /**
     * 中央吐司
     */
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    fun centerToast(context: Context, text: String) {
        val toast = Toast(context)
        val view: View =
            LayoutInflater.from(context)
                .inflate(cn.dreamfruits.yaoguo.R.layout.home_dialog_net_error, null)
        val tv: TextView = view.findViewById(R.id.center_toast_text)
        tv.text = text
        toast.setView(view)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    /**
     * 帖子时间规则  和  消息模块时间规则
     * ToolJava.timeStamp2Date(System.currentTimeMillis(), "yyyy_MM_dd HH_mm_ss_SS")//系统时间
     * ToolJava.timeStamp2Date(timestamp, "yyyy_MM_dd HH_mm_ss_SS")//帖子发布时间
     */
    fun timeShowRule(timestamp: Long, isPost: Boolean): String {
        try {
            val temp = System.currentTimeMillis() - timestamp //时间差值

            val second = temp / 1000
            val minute = temp / 1000 / 60
            val hour = temp / 1000 / 60 / 60
            val day = temp / 1000 / 60 / 60 / 24

            var isThisYear = false
            if (ToolJava.timeStamp2Date(System.currentTimeMillis(), "yyyy")
                    .equals(ToolJava.timeStamp2Date(timestamp, "yyyy"))
            ) {
                isThisYear = true
            }

            if (isPost) {
                if (day > 8) {//若日期在今年则展示xx月xx日   若日期在去年或去年之前则 展示xxxx年xx月xx日
                    return if (isThisYear) {
                        fixTimeText(ToolJava.timeStamp2Date(timestamp, "MM月dd日"))
                    } else {
                        ToolJava.timeStamp2Date(timestamp, "yyyy年MM月dd日")
                    }
                }

                if (day == 1L) {//1天<=t<8天
                    return "昨天"
                }
                if (day == 2L) {//1天<=t<8天
                    return "前天"
                }
                if (day in 1..7) {//1天<=t<8天
                    return "${day}天前"
                }

                if (hour in 1..24) {//1小时<=t<24小时
                    return "${hour}小时前"
                }


                if (minute in 1..60) {//1分钟<=t<60分钟
                    return "${minute}分钟前"
                }

                if (second < 60) {//t<1分钟
                    return "刚刚"
                }
            } else {
                if (isThisYear) {//今年
                    if (day.toInt() >= 3) {
                        return fixTimeText(ToolJava.timeStamp2Date(timestamp, "MM-dd HH:mm"))
                    }

                    if (day.toInt() == 0 && hour >= 1) {
                        return fixTimeText(ToolJava.timeStamp2Date(timestamp, "HH:mm"))
                    }

                    if (day.toInt() == 1 && hour >= 1) {
                        return "昨天 " + fixTimeText(ToolJava.timeStamp2Date(timestamp, "HH:mm"))
                    }

                    if (day.toInt() == 2 && hour >= 1) {
                        return "前天 " + fixTimeText(ToolJava.timeStamp2Date(timestamp, "HH:mm"))
                    }
                } else {
                    return fixTimeText(ToolJava.timeStamp2Date(timestamp, "yyyy-MM-dd HH:mm"))
                }
            }
            return ""
        } catch (e: Exception) {
            Log.e("zqr", e.toString())
            return ""
        }


    }

    /**
     * 去0逻辑 接上面时间文本
     */
    fun fixTimeText(temp: String): String {
        return try {
            var tempText = temp.replace(" 0", " ")//替换 04-04 09_45     ==》  04-04 9_45

            if (tempText[0].toString() == "0") {
                tempText = tempText.substring(1, tempText.length)// 04-04 9_45    ==>  4_04 9_45
            }
            tempText
        } catch (e: Exception) {
            temp
        }
    }


    /**
     * 随机图片
     */
    fun getRandomImageUrl(): String? {
        // 将多个图片链接存储在列表中
        val imageUrls: List<String> = Arrays.asList(
            "https://img2.woyaogexing.com/2023/04/09/55f771c75e6a4883c167aa6a6766cb22.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/7adb5986b2cd7ab25c9d2cba0e8af8e1.jpeg",
            "https://img2.woyaogexing.com/2023/04/08/16a5092608019b35f7172476bf48968e.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/08/c626834b97f48a3f00bda3cd1580db44.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/c0df9e9f51bf18ac6882fb1b9dffe3e2.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/09/2599eb37c7f6cbe0c52006270ec6cf4f.png",
            "https://img2.woyaogexing.com/2023/04/08/ab2c6520fb3d05a998ed0f2ce6d2d007.jpeg"
        )

        // 随机选择一个链接并返回
        val random = Random()
        return imageUrls[random.nextInt(imageUrls.size)]
    }


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val dm: DisplayMetrics = context.resources.displayMetrics
        val scale = dm.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    fun spToPx(sp: Float, context: Context): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return sp * scale
    }


    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels

    }

    /**
     * 透明度
     */
    fun bgAlpha(context: Activity, f: Float) { //透明函数
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = f
        context.window.attributes = lp
    }


    /**
     * 一、返回 富文本集合
     */
    fun setRichStr(
        expandableTextView: ExpandableTextView,//富文本控件
        diffDpValue: Float, //用于计算控件实际的宽  所以填写需要减去的距离dp
        contentStr: String,//原始文本
        mContent: Context,//上下文
        atUserList: MutableList<AtUser>? = null,//@ 集合
        configList: MutableList<Config>? = null,//#集合
        openStr: String,//展开文本   比如    展开  记得展开前加一个空格
        closeStr: String,//关闭文本  如上
        maxLines: Int,//最大行数
        isAnimation: Boolean,//是否显示动画
    ) {
        /**
         * 1.最后需要的集合
         */
        var strList: ArrayList<ExpandableTextView.RichTextBean> = ArrayList()//最后需要的集合

        /**
         * 2.组装集合
         */
        var tempList: ArrayList<ExpandableTextView.RichTextBean> = ArrayList()//临时集合
        tempList = getCombinationList(atUserList, configList)//关键代码  组装数据

        /**
         * 3.设置expandableTextView的共同属性
         */
        expandableTextView.setHasAnimation(isAnimation)
        expandableTextView.initWidth(
            getScreenWidth(mContent as Activity) - dp2px(
                mContent,
                diffDpValue
            )
        )
        expandableTextView.setOpenSuffix(openStr)
        expandableTextView.setCloseSuffix(closeStr)
        expandableTextView.maxLines = maxLines

        /**
         * 4.拼数据
         */
        if (tempList.size != 0) {//上面集合不为空 说明有@  或者 # 的数据
            for (i in contentStr.indices) {
                var isAdd = true
                for (tempIndex in 0 until tempList.size) {
                    if (i == tempList[tempIndex].index) {//文本角标==atuser所存的角标
                        strList.add(
                            ExpandableTextView.RichTextBean(
                                contentStr.split("")[i],
                                i,
                                tempList[tempIndex].name.length,
                                tempList[tempIndex].getmId(),
                                tempList[tempIndex].name,
                                tempList[tempIndex].type,
                                tempList[tempIndex].colorStr//颜色
                            )
                        )
                        isAdd = false
                    }
                }
                if (isAdd) {
                    strList.add(
                        ExpandableTextView.RichTextBean(
                            contentStr.split("")[i],
                            i,
                            0,
                            "",
                            "",
                            "",
                            ""//颜色
                        )
                    )
                }
            }
            /**
             * 5.设置点击事件
             */
            expandableTextView.setOnRichTextClickListener { contentStr, type ->
                if (type.equals("@")) {
                    Log.e("34543", "点击了：  $type   $contentStr")
                    Toast.makeText(mContent, "点击了：  $type   $contentStr", Toast.LENGTH_SHORT).show()
                }
                if (type.equals("#")) {
                    Log.e("34543", "点击了：  $type   $contentStr")
                    Toast.makeText(mContent, "点击了：  $type   $contentStr", Toast.LENGTH_SHORT).show()
                }
            }

            /**
             * 6.设置expandableTextView的基本属性
             */
            expandableTextView.setRichTextList(strList)//重要逻辑
            expandableTextView.setOriginalText(contentStr)
        } else {
            /**
             * 6.1没有富文本的情况
             */
            expandableTextView.setOriginalText(contentStr)
        }
    }

    /**
     * 一、返回 富文本集合 针对评论
     */
    fun setRichStr2(
        expandableTextView: ExpandableTextView2,//富文本控件
        diffDpValue: Float, //用于计算控件实际的宽  所以填写需要减去的距离dp
        contentStr: String,//原始文本
        mContent: Context,//上下文
        atUserList: MutableList<AtUser>? = null,//@ 集合
        configList: MutableList<Config>? = null,//#集合
        openStr: String,//展开文本   比如    展开  记得展开前加一个空格
        closeStr: String,//关闭文本  如上
        maxLines: Int,//最大行数
        isAnimation: Boolean,//是否显示动画
        replyUserName: String,
        replyUserId: Long,
    ) {
        /**
         * 1.最后需要的集合
         */
        var strList: ArrayList<ExpandableTextView.RichTextBean> = ArrayList()//最后需要的集合

        /**
         * 2.组装集合
         */
        var tempList: ArrayList<ExpandableTextView.RichTextBean> = ArrayList()//临时集合
        tempList = getCombinationList(atUserList, configList)//关键代码  组装数据

        /**
         * 3.设置expandableTextView的共同属性
         */
        expandableTextView.setHasAnimation(isAnimation)
        expandableTextView.initWidth(
            getScreenWidth(mContent as Activity) - dp2px(
                mContent,
                diffDpValue
            )
        )
        expandableTextView.setOpenSuffix(openStr)
        expandableTextView.setCloseSuffix(closeStr)
        expandableTextView.maxLines = maxLines

        /**
         * 4.拼数据
         */
        for (i in contentStr.indices) {
            var isAdd = true
            for (tempIndex in 0 until tempList.size) {
                if (i == tempList[tempIndex].index) {//文本角标==atuser所存的角标
                    strList.add(
                        ExpandableTextView.RichTextBean(
                            contentStr.split("")[i],
                            i,
                            tempList[tempIndex].name.length,
                            tempList[tempIndex].getmId(),
                            tempList[tempIndex].name,
                            tempList[tempIndex].type,
                            tempList[tempIndex].colorStr//颜色
                        )
                    )
                    isAdd = false
                }
            }
            if (isAdd) {
                strList.add(
                    ExpandableTextView.RichTextBean(
                        contentStr.split("")[i],
                        i,
                        0,
                        "",
                        "",
                        "",
                        ""//颜色
                    )
                )
            }
        }
        if (replyUserId != -1L) {//说明有回复人
            //添加富文本
            strList.add(0, ExpandableTextView.RichTextBean("回", 0, 0, "", "", "", ""))
            strList.add(1, ExpandableTextView.RichTextBean("复", 0, 0, "", "", "", ""))
            strList.add(2, ExpandableTextView.RichTextBean(" ", 0, 0, "", "", "", ""))

            for (i in 3 until replyUserName.length + 3) {
                strList.add(
                    i, ExpandableTextView.RichTextBean(
                        replyUserName.split("")[i - 3],
                        i,
                        replyUserName.length,
                        replyUserId.toString(),
                        replyUserName,
                        "user",
                        "#569DE4"//颜色
                    )
                )
            }
            strList.add(
                replyUserName.length + 3,
                ExpandableTextView.RichTextBean(":", 0, 0, "", "", "", "")
            )
            strList.add(
                replyUserName.length + 4,
                ExpandableTextView.RichTextBean(" ", 0, 0, "", "", "", "")
            )
        }
        /**
         * 5.设置点击事件
         */
        expandableTextView.setOnRichTextClickListener { contentStr, type ->
            if (type.equals("@")) {
                Log.e("34543", "点击了：  $type   $contentStr")
                Toast.makeText(mContent, "点击了：  $type   $contentStr", Toast.LENGTH_SHORT).show()
            }
            if (type.equals("#")) {
                Log.e("34543", "点击了：  $type   $contentStr")
                Toast.makeText(mContent, "点击了：  $type   $contentStr", Toast.LENGTH_SHORT).show()
            }
            if (type.equals("user")) {
                Log.e("34543", "点击了：  $type   $contentStr")
                Toast.makeText(mContent, "点击了：  $type   $contentStr", Toast.LENGTH_SHORT).show()
            }
        }
        /**
         * 6.设置expandableTextView的基本属性
         */
        if (replyUserId != -1L) {//说明有回复人
            expandableTextView.setRichTextList(strList)//重要逻辑
            expandableTextView.setOriginalText("回复 ${replyUserName}: " + contentStr)
        } else {
            expandableTextView.setRichTextList(strList)//重要逻辑
            expandableTextView.setOriginalText(contentStr)
        }
    }

    /**
     * 二.把AtUser  Config  拆分组装成一个集合 并返回
     */
    private fun getCombinationList(
        atUserList: MutableList<AtUser>? = null,
        configList: MutableList<Config>? = null,
    ): ArrayList<ExpandableTextView.RichTextBean> {

        var tempList: ArrayList<ExpandableTextView.RichTextBean> = ArrayList()//临时富文本集合

        if (atUserList != null && atUserList.size != 0) {
            //处理@集合
            for (i in 0 until atUserList.size) {
                var a = 0
                while (a != atUserList[i].len) {
                    if (a >= atUserList[i].len) {
                        break
                    }
                    tempList.add(
                        ExpandableTextView.RichTextBean(
                            "",
                            atUserList[i].index + a,//重要数据
                            atUserList[i].len,
                            atUserList[i].id.toString(),//重要数据
                            atUserList[i].name,//重要数据
                            "@",//重要数据
                            "#569DE4"
                        )
                    )
                    a++
                }
            }
        }

        if (configList != null && configList.size != 0) {
            //处理#集合
            for (i in 0 until configList.size) {
                var a = 0
                while (a != configList[i].len) {
                    if (a >= configList[i].len) {
                        break
                    }
                    tempList.add(
                        ExpandableTextView.RichTextBean(
                            "",
                            configList[i].index + a,
                            configList[i].len,
                            configList[i].id.toString(),
                            configList[i].name,
                            "#",
                            "#569DE4"
                        )
                    )
                    a++
                }
            }
        }
        return tempList
    }


    data class AtUser(
        val id: Long, // 0
        val index: Int, // 0
        val len: Int, // 0
        val name: String, // string
    )

    data class Config(
        val id: Long, // 0
        val index: Int, // 0
        val len: Int, // 0
        val name: String, // string
    )


    /**
     * 显示保存图片视频的pop 没有倍数版本
     */
    fun showSavePhotoAndVideo(
        url: String,
        mActivity: Activity,
        isImg: Boolean,
        mControlWrapper: ControlWrapper? = null,
    ) {
        val inflateView: View =
            mActivity.layoutInflater.inflate(R.layout.home_dialog_bottom_save_photo, null)
        val saveTv = inflateView.findViewById<TextView>(R.id.save_tv)//点击保存
        val cancelTv = inflateView.findViewById<TextView>(R.id.cancel_tv)

        if (isImg) {//图片
            saveTv.text = "保存至相册"
        } else {
            saveTv.text = "保存至相册"
        }

        /**
         * pop实例
         */
        var savePopupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        saveTv.setOnClickListener {
            if (isImg) {
                SaveImageOrVideo.saveImg(url, mActivity)
            } else {
                SaveImageOrVideo.saveMp4(url, mActivity)
            }
            savePopupWindow.dismiss()
        }

        cancelTv.setOnClickListener {
            savePopupWindow.dismiss()
        }

        //动画
        savePopupWindow.animationStyle = R.style.BottomDialogAnimation
        if (savePopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            savePopupWindow.dismiss()
        } else {
            savePopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            savePopupWindow.isOutsideTouchable = true
            savePopupWindow.isTouchable = true
            savePopupWindow.isFocusable = true
            savePopupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            savePopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }
        //在pause时关闭
        lifeCycleSet(mActivity, savePopupWindow)
    }


    /**
     * 有倍数版本的保存视频
     */
    fun showSaveVideo(
        url: String,
        mActivity: Activity,
        isImg: Boolean,
        mControlWrapper: ControlWrapper? = null,
    ) {
        val inflateView: View =
            mActivity.layoutInflater.inflate(R.layout.home_dialog_bottom_save_video, null)
        val saveTv = inflateView.findViewById<TextView>(R.id.save_tv)//点击保存
        val popVideoLayout = inflateView.findViewById<View>(R.id.pop_video_layout)
        saveTv.text = "保存到本地相册"
        if (mControlWrapper == null) {
            popVideoLayout.visibility = View.GONE
        } else {
            popVideoLayout.visibility = View.VISIBLE

            //倍数啥的
            val speed05xLayout = inflateView.findViewById<View>(R.id.speed05x_layout)
            val speed05xTv = inflateView.findViewById<TextView>(R.id.speed05x_tv)
            val speed05xTag = inflateView.findViewById<View>(R.id.speed05x_tag)

            val speed1xTv = inflateView.findViewById<TextView>(R.id.speed1x_tv)
            val speed1xTag = inflateView.findViewById<View>(R.id.speed1x_tag)
            val speed1xLayout = inflateView.findViewById<View>(R.id.speed1x_layout)

            val speed1_5xTv = inflateView.findViewById<TextView>(R.id.speed1_5x_tv)
            val speed1_5xTag = inflateView.findViewById<View>(R.id.speed1_5x_tag)
            val speed1_5xLayout = inflateView.findViewById<View>(R.id.speed1_5x_layout)

            val speed2xTv = inflateView.findViewById<TextView>(R.id.speed2x_tv)
            val speed2xTag = inflateView.findViewById<View>(R.id.speed2x_tag)
            val speed2xLayout = inflateView.findViewById<View>(R.id.speed2x_layout)

            showLog(this, mControlWrapper!!.speed.toString())
            when (mControlWrapper!!.speed.toString()) {//初始化
                "0.5" -> {
                    setSpeedInitView(
                        speed05xTv,
                        speed05xTag,
                        speed1xTv,
                        speed1xTag,
                        speed1_5xTv,
                        speed1_5xTag,
                        speed2xTv,
                        speed2xTag,
                        1
                    )
                }
                "1.0" -> {
                    setSpeedInitView(
                        speed05xTv,
                        speed05xTag,
                        speed1xTv,
                        speed1xTag,
                        speed1_5xTv,
                        speed1_5xTag,
                        speed2xTv,
                        speed2xTag,
                        2
                    )
                }
                "1.5" -> {
                    setSpeedInitView(
                        speed05xTv,
                        speed05xTag,
                        speed1xTv,
                        speed1xTag,
                        speed1_5xTv,
                        speed1_5xTag,
                        speed2xTv,
                        speed2xTag,
                        3
                    )
                }
                "2.0" -> {
                    setSpeedInitView(
                        speed05xTv,
                        speed05xTag,
                        speed1xTv,
                        speed1xTag,
                        speed1_5xTv,
                        speed1_5xTag,
                        speed2xTv,
                        speed2xTag,
                        4
                    )
                }
            }

            speed05xLayout.setOnClickListener {
                mControlWrapper!!.speed = 0.5f
                setSpeedInitView(
                    speed05xTv,
                    speed05xTag,
                    speed1xTv,
                    speed1xTag,
                    speed1_5xTv,
                    speed1_5xTag,
                    speed2xTv,
                    speed2xTag,
                    1
                )
            }
            speed1xLayout.setOnClickListener {
                mControlWrapper!!.speed = 1.0f
                setSpeedInitView(
                    speed05xTv,
                    speed05xTag,
                    speed1xTv,
                    speed1xTag,
                    speed1_5xTv,
                    speed1_5xTag,
                    speed2xTv,
                    speed2xTag,
                    2
                )
            }
            speed1_5xLayout.setOnClickListener {
                mControlWrapper!!.speed = 1.5f
                setSpeedInitView(
                    speed05xTv,
                    speed05xTag,
                    speed1xTv,
                    speed1xTag,
                    speed1_5xTv,
                    speed1_5xTag,
                    speed2xTv,
                    speed2xTag,
                    3
                )
            }
            speed2xLayout.setOnClickListener {
                mControlWrapper!!.speed = 2.0f
                setSpeedInitView(
                    speed05xTv,
                    speed05xTag,
                    speed1xTv,
                    speed1xTag,
                    speed1_5xTv,
                    speed1_5xTag,
                    speed2xTv,
                    speed2xTag,
                    4
                )
            }
        }
        /**
         * pop实例
         */
        var savePopupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        saveTv.setOnClickListener {
            SaveImageOrVideo.saveMp4(url, mActivity)
            savePopupWindow.dismiss()
        }


        //动画
        savePopupWindow.animationStyle = R.style.BottomDialogAnimation
        if (savePopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            savePopupWindow.dismiss()
        } else {
            savePopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            savePopupWindow.isOutsideTouchable = true
            savePopupWindow.isTouchable = true
            savePopupWindow.isFocusable = true
            savePopupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            savePopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }
        //在pause时关闭
        lifeCycleSet(mActivity, savePopupWindow)
    }


    //初始化
    fun setSpeedInitView(
        tv1: TextView, tag1: View,
        tv2: TextView, tag2: View,
        tv3: TextView, tag3: View,
        tv4: TextView, tag4: View, tag: Int,
    ) {

        tv1.setTextColor(Color.parseColor("#ffb3b3b3"))
        tv2.setTextColor(Color.parseColor("#ffb3b3b3"))
        tv3.setTextColor(Color.parseColor("#ffb3b3b3"))
        tv4.setTextColor(Color.parseColor("#ffb3b3b3"))
        tag1.visibility = View.GONE
        tag2.visibility = View.GONE
        tag3.visibility = View.GONE
        tag4.visibility = View.GONE

        when (tag) {
            1 -> {
                tv1.setTextColor(Color.parseColor("#ff000000"))
                tag1.visibility = View.VISIBLE
            }
            2 -> {
                tv2.setTextColor(Color.parseColor("#ff000000"))
                tag2.visibility = View.VISIBLE
            }
            3 -> {
                tv3.setTextColor(Color.parseColor("#ff000000"))
                tag3.visibility = View.VISIBLE
            }
            4 -> {
                tv4.setTextColor(Color.parseColor("#ff000000"))
                tag4.visibility = View.VISIBLE
            }
        }

    }


    /**
     * 获取app的生命周期，包括所有activity  找到现在使用的 并进行 防止内存泄露的操作  一般用于关闭pop
     */
    fun lifeCycleSet(mActivity: Activity, tempPop: PopupWindow) {
        (mActivity.application).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {
                    if (mActivity != null && mActivity === activity) {//判断是否是依附的pop
                        if (tempPop != null && tempPop.isShowing) {//如果正在显示，关闭弹窗。
                            tempPop.dismiss()
                        }
                    }
                }

                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
    }


    /**
     * 衣装弹窗
     */
    fun showDragPopDress(
        mActivity: Activity,
        dataList: MutableList<WaterfallFeedBean.Item.Info.Single>,
    ) {
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dragpop_dialog_dress, null)
        val popReLayout = inflate.findViewById<RelativeLayout>(R.id.pop_re_layout)
        val popMyRelativeLayout = inflate.findViewById<MyRelativeLayout>(R.id.pop_MyRelativeLayout)
        val popRecyclerView = inflate.findViewById<RecyclerView>(R.id.pop_recyclerview)

        /**
         * 装载适配器
         */
        val mStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mStaggeredGridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        popRecyclerView.layoutManager = mStaggeredGridLayoutManager
        //2.防止第一页出现空白 3.使用notifyItemRangeChanged
        popRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mStaggeredGridLayoutManager.invalidateSpanAssignments()
            }
        })
//        if(singleDressList==null){ singleDressList= ArrayList() }
//        //初始化数据
//        for (i in 0..20){
//            singleDressList.add(SearchSingleProductBean.Item("22222",2121L))
//        }

        //设置适配器
        val adapter = SearchSingleDressAdapter(mActivity, dataList)
        popRecyclerView.adapter = adapter

        var mCustomDragPop = CustomDragPop(
            mActivity,
            inflate,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            popReLayout,
            popMyRelativeLayout,
            popRecyclerView
        )

        //点击关闭
        val popCloseImgView = inflate.findViewById<ImageView>(R.id.pop_close)
        popCloseImgView.setOnClickListener {
            //销毁弹出框
            mCustomDragPop.dismiss()
        }


        //动画
        mCustomDragPop.animationStyle = R.style.BottomDialogAnimation
        if (mCustomDragPop.isShowing) {//如果正在显示，关闭弹窗。
            mCustomDragPop.dismiss()
        } else {
            mCustomDragPop.isOutsideTouchable = true
            mCustomDragPop.isTouchable = true
            mCustomDragPop.isFocusable = true
            mCustomDragPop.showAtLocation(
                inflate,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0,
                0
            )
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mCustomDragPop.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mCustomDragPop)
    }


    /**
     * 逛逛弹窗
     */
    fun showStrollPop(mActivity: Activity) {
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_stroll, null)

        val rl1 = inflate.findViewById<View>(R.id.rl_stroll_1)
        val rl2 = inflate.findViewById<View>(R.id.rl_stroll_2)
        val imgStrollBack = inflate.findViewById<ImageView>(R.id.img_stroll_back)

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        //点击晒穿搭
        rl1.setOnClickListener {

//            mPopupWindow!!.dismiss()
        }

        //点击DIY
        rl2.setOnClickListener {
//            androidBridgeViewModel.getStyleVersionList(10,null)//请求版型数据
            val intent = Intent(mActivity, AndroidBridgeActivity::class.java)
            intent.putExtra("entryType", "EnterDIY")
            intent.putExtra("data", "2")
            mActivity.startActivity(intent)
//            mPopupWindow!!.dismiss()
        }

        imgStrollBack.setOnClickListener {
            mPopupWindow!!.dismiss()
        }


        //动画
        if (mPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow!!.dismiss()
        } else {
            mPopupWindow!!.isOutsideTouchable = true
            mPopupWindow!!.isTouchable = true
            mPopupWindow!!.isFocusable = true
            mPopupWindow!!.showAtLocation(inflate, Gravity.CENTER_HORIZONTAL, 0, 0)
//            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow!!.setOnDismissListener {
//                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
    }

    /**
     * 取消和确认
     */
    fun showCancelConfirm(mActivity: Activity, str: String) {
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_comm_alert, null)

        val content = inflate.findViewById<TextView>(R.id.tv_alert_content)
        content.text = str
        val confirm = inflate.findViewById<TextView>(R.id.tv_alert_confirm)
        val cancel = inflate.findViewById<TextView>(R.id.tv_alert_cancel)

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //点击确定
        confirm.setOnClickListener {
            mPopupWindow.dismiss()

        }

        //点击取消
        cancel.setOnClickListener {
            mPopupWindow.dismiss()
        }


        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
    }


    /**点击取消关注*/
   fun showNoFollow(mActivity: Activity){
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_comm_alert, null)
        val confirm = inflate.findViewById<TextView>(R.id.tv_alert_confirm)
        val cancel = inflate.findViewById<TextView>(R.id.tv_alert_cancel)

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //点击确定
        confirm.setOnClickListener {
            mPopupWindow.dismiss()
        }

        //点击取消
        cancel.setOnClickListener {
            mPopupWindow.dismiss()
        }

        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
   }


    fun showLog(mClass: Any, content: String) {
        val className = mClass.javaClass.name
        Log.e(className, content)
    }

    /**
     * 把毫秒数 设置为  05：30格式
     */
    fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = (totalSeconds / 60).toString().padStart(2, '0')
        val seconds = (totalSeconds % 60).toString().padStart(2, '0')
        return "$minutes:$seconds"
    }


    /**
     * 跳转到其他用户中心
     */
    fun startOtherUserCenterActivity(context: Context, userId: Long) {
            val intent = Intent(context, UserCenterActivity::class.java)
            intent.putExtra("userId", userId)
            context.startActivity(intent)
    }


    /**
     * 设置刷新状态
     */
    fun setRefreshResult(refreshLayout: SmartRefreshLayout, state: Int) {
        when (state) {
            1 -> {//刷新成功
                refreshLayout.finishRefresh(true)
            }
            2 -> {//加载成功
                refreshLayout.finishLoadMore(true)
            }
            3 -> {//刷新失败
                refreshLayout.finishRefresh(false)
            }
            4 -> {//加载失败
                refreshLayout.finishLoadMore(false)
            }
            5 -> {//显示全部加载完成，并不再触发加载更事件
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }
    }

    /**
     * 返回随机 1-30 个字符（包括汉字）的字符串的方法
     */
    fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + ('一'..'龥')
        val randomString = (1..(1..20).random())
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        return randomString
    }

    /**
     * 如果字符为""  或者全部空格 返回true  此方法暂用不着
     */
    fun isNoTrimStrOrNullStr(key: String): Boolean {
        return !(key == "" || key.trim().length == 0)
    }


    /**
     * 向下 或者 向上动画
     * -90f,  // 结束角度（向下旋转）
     * 90f,  // 结束角度（向上旋转）
     */
    fun setRotateAnimation(img: ImageView, f1: Float, f2: Float) {
        // 创建旋转动画
//        val rotateAnimation = RotateAnimation(
//            0f,  // 起始角度
//            90f,  // 结束角度（向下旋转）
//            Animation.RELATIVE_TO_SELF, 1f,  // 中心点在 X 轴方向上的比例
//            Animation.RELATIVE_TO_SELF, 1f// 中心点在 Y 轴方向上的比例
//        )
//
//        rotateAnimation.duration = 500 // 动画持续时间为 500 毫秒
//        img.startAnimation(rotateAnimation)


        val rotationAnimator = ObjectAnimator.ofFloat(
            img,  // 要旋转的 View
            "rotation",  // 动画属性
            f1,  // 起始角度
            f2 // 结束角度
        )

        rotationAnimator.duration = 500 // 设置动画时长
        rotationAnimator.start()// 启动旋转动画

    }

    /**
     * 跳转到搜索详情页
     */
    fun shipSearchDetailsActivity(mContext: Context, str: String) {
        val intent = Intent(mContext, HomeSearchDetailsActivity::class.java)
        intent.putExtra("searchStr", str)
        mContext.startActivity(intent)
    }

    /**
     * 获取登录信息
     */
    fun getLoginInfo(): LoginResultBean {
        val str = MMKVRepository.getCommonMMKV().decodeString("key_cache_oauth_bean", "")
        return GsonUtils.fromJson(str, LoginResultBean::class.java)
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(): UserInfoBean {
        val str = MMKVRepository.getCommonMMKV().decodeString("get_user_info", "")
        return GsonUtils.fromJson(str, UserInfoBean::class.java)
    }

    var UserAvtarUrl = ""


    /**
     * 设置刷新状态
     */
    fun sdad(mHandler: Handler, refreshLayout: SmartRefreshLayout, isNoMoreData: Boolean) {
        /**
         * 1.创建Handler对象，重写handleMessage方法
         */
//             private var mHandler: Handler? = null

//        (mHandler as Handler).sendEmptyMessage(2)// 发送消息到UI线程

//        mHandler = object : Handler(Looper.getMainLooper()) {
//            override fun handleMessage(@NonNull msg: Message) {
//                when (msg.what) {
//                    1 -> {//刷新成功根据返回结果设置刷新结果
//                        refreshLayout!!.finishRefresh(true)
//                    }
//                    2 ->{//加载成功根据返回结果设置加载结果
//                        if (isNoMoreData) {//没有更多数据了
//                            refreshLayout!!.finishLoadMoreWithNoMoreData()//显示全部加载完成，并不再触发加载更事件
//                        } else {
//                            refreshLayout!!.finishLoadMore(true)
//                        }
//                    }
//                    3 -> {//刷新失败根据返回结果设置刷新结果
//                        refreshLayout!!.finishRefresh(false)
//                    }
//                    4 ->{//加载失败根据返回结果设置加载结果
//                        if (isNoMoreData) {//没有更多数据了
//                            refreshLayout!!.finishLoadMoreWithNoMoreData()//显示全部加载完成，并不再触发加载更事件
//                        } else {
//                            refreshLayout!!.finishLoadMore(false)
//                        }
//                    }
//                    else -> super.handleMessage(msg)
//                }
//            }
//        }
    }


    // //隐藏虚拟按键，并且全屏
    //        val decorView = window.decorView
    //        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    //                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    //                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //                or View.SYSTEM_UI_FLAG_FULLSCREEN
    //                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    //        decorView.systemUiVisibility = uiOptions


    //回调
    //    interface  InnerInterface{
    //        fun onclick(id: Long)
    //    }
    //    private var mInterface: InnerInterface? = null
    //
    //    fun setXXXCallBack(mInterface: InnerInterface){
    //        this.mInterface=mInterface
    //    }


    //回调   java
//    interface  InnerInterface{
//        void onclick(Long id);
//    }
//    private InnerInterface  mInterface= null;
//
//    public void  setXXXCallBack(InnerInterface mInterface){
//        this.mInterface=mInterface;
//    }


    // feedViewModel.homeGetLabelFeedListState.observe(this) {
    //            when (it) {
    //                is HomeGetLabelFeedListState.Success -> {
    //                    when (refreshState) {
    //                        0 -> {//刷新
    //                            mGetLabelFeedList!!.clear()//清空集合
    //                            mGetLabelFeedList=Singleton.changeList(feedViewModel.mGetLabelListBean)//加载请求后的数据
    //                            mAdapter!!.setData(mGetLabelFeedList!!,true)//设置数据 更新适配器
    //                            Singleton.setRefreshResult(refreshLayout!!, 1)
    //                        }
    //                        1, 2 -> {//加载 预加载
    //                            mAdapter!!.setData(Singleton.changeList(feedViewModel.mGetLabelListBean)!!, false)//设置数据 更新适配器
    //                            Singleton.setRefreshResult(refreshLayout!!, 2)
    //                        }
    //                    }
    //                    //设置缺省
    //                    showDefaultPage()
    //                }
    //                is HomeGetLabelFeedListState.Fail -> {
    //                    //1.是否有断网提示
    //                    Singleton.isNetConnectedToast(requireActivity())
    //                    //2.各自的失败处理逻辑
    //                    when (refreshState) {
    //                        0 -> {//刷新
    //                            Singleton.setRefreshResult(refreshLayout!!, 3)
    //                        }
    //                        1, 2 -> {//加载 预加载
    //                            Singleton.setRefreshResult(refreshLayout!!, 4)
    //                        }
    //                    }
    //                    //设置缺省
    //                    showDefaultPage()
    //                }
    //            }
    //        }


//           searchViewModel.getSearchWordBeanState.observe(this) {
//                when (it) {
//                    is GetSearchWordBeanState.Success -> {
//
//                    }
//                    is GetSearchWordBeanState.Fail -> {
//                             if (it.errorMsg!=null&&it.errorMsg!=""){
//                                    Singleton.centerToast(this, it.errorMsg.toString())
//                                }else{
//                                    //1.是否有断网提示
//                                    Singleton.isNetConnectedToast(this)
//                                }
//                    }
//                }
//            }

//    // 在适当的时机停止旋转动画
//    Handler handler = new Handler();
//    handler.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//            imageView.clearAnimation(); // 停止旋转动画
//        }
//    }, 3000); // 延迟3秒后停止动画


    //    private lateinit var handler: Handler
    //    override fun onWindowFocusChanged(hasFocus: Boolean) {
    //        super.onWindowFocusChanged(hasFocus)
    //        if (hasFocus) {
    //            if (popupWindow!=null){
    //
    //            }else{
    //
    //                handler = Handler(Looper.getMainLooper())
    //                val timer = Timer()
    //                timer.schedule(object : TimerTask() {
    //                    override fun run() {
    //                        // 发送消息到主线程
    //                        handler.post {
    //                            //弹出pop
    //                            initPop()
    //                        }
    //                    }
    //                }, 1000)
    //
    //            }
    //        }
    //    }


    // /**加入确认弹窗*/
    //                    val pop:ShowNoFollowPop=ShowNoFollowPop()
    //                    pop.showPop(mContent as Activity)
    //                    pop.setShowNoFollowPopCallBack(object :ShowNoFollowPop.InnerInterface{
    //                        override fun onclick(state: Int) {
    //                            when(state){
    //                                0->{}
    //                                1->{//操作在这里
    //                                    /**刷新当前item*/
    //                                    notifyItemChanged(position)
    //                                }
    //                            }
    //                        }
    //                    })

//    postDetailsViewModel.showLoading(this,"加载中……")
//    postDetailsViewModel.hideLoading()


//    val stu = StringBuffer()
//    stu.append("[")
//    bean.list.forEach {
//        stu.append("{\"name\":\"${it.name}\",\"phone\":\"${it.phone}\",\"type\":${it.type}}")
//    }
//    var tempStr = stu.toString().substring(0, stu.toString().length - 1)
//    tempStr = "$tempStr]"


    fun getFriendlyTimeSpanByNow(millis: Long): String? {
        val now = System.currentTimeMillis()

        val wee = getWeeOfToday()
        return if (millis >= wee) {
            String.format("今天%tR", millis)
        } else if (millis >= wee - TimeConstants.DAY) {
            String.format("昨天%tR", millis)
        } else if (millis >= wee - TimeConstants.DAY * 2) {
            String.format("昨天%tR", millis)
        } else {
            var isThisYear = false
            if (ToolJava.timeStamp2Date(System.currentTimeMillis(), "yyyy")
                    .equals(ToolJava.timeStamp2Date(millis, "yyyy"))
            ) {
                isThisYear = true
            }
            return if (isThisYear) {
                fixTimeText(ToolJava.timeStamp2Date(millis, "MM-dd HH:mm"))
            } else {
                ToolJava.timeStamp2Date(millis, "yyyy年MM月dd日 HH:mm")
            }

        }
    }  fun getMessageListTime(millis: Long): String? {
        val now = System.currentTimeMillis()
        val span = now - millis
        if (span < 0) // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
            return "刚刚"
        if (span < 1000) {
            return "刚刚"
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC)
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN)
        }
        val wee = getWeeOfToday()
        return if (millis >= wee) {
            String.format("今天%tR", millis)
        } else if (millis >= wee - TimeConstants.DAY) {
            String.format("昨天%tR", millis)
        } else if (millis >= wee - TimeConstants.DAY * 2) {
            String.format("昨天%tR", millis)
        } else {
            var isThisYear = false
            if (ToolJava.timeStamp2Date(System.currentTimeMillis(), "yyyy")
                    .equals(ToolJava.timeStamp2Date(millis, "yyyy"))
            ) {
                isThisYear = true
            }
            return if (isThisYear) {
                fixTimeText(ToolJava.timeStamp2Date(millis, "MM-dd HH:mm"))
            } else {
                ToolJava.timeStamp2Date(millis, "yyyy年MM月dd日 HH:mm")
            }

        }
    }

    private fun getWeeOfToday(): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis
    }
}