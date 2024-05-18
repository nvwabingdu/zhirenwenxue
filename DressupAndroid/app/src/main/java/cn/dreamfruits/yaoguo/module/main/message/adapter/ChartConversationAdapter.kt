package cn.dreamfruits.yaoguo.module.main.message.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseMultiItemQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareSingleEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareUserEntity
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.QuoteBean
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.QuoteMessageBean
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.TimMessageBaseElement
import cn.dreamfruits.yaoguo.module.main.message.chart.manage.FaceManager
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.util.*
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.tencent.imsdk.message.Message
import com.tencent.imsdk.message.MessageBaseElement
import com.tencent.imsdk.message.VideoElement
import com.tencent.imsdk.v2.*
import org.json.JSONObject
import java.io.File

/**
 * @author Lee
 * @createTime 2023-06-30 09 GMT+8
 * @desc :
 */
class ChartConversationAdapter(var mContext: Context) :
    BaseMultiItemQuickAdapter<ConversionEntity, BaseViewHolder>() {

    init {
        /**
         * 文本消息
         */
        addItemType(TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT, R.layout.item_chart_left_content)
        addItemType(TIMCommonConstants.MESSAGE_TYPE_RIGHT_TEXT, R.layout.item_chart_right_content)

        /**
         * 视频消息
         */
        addItemType(TIMCommonConstants.MESSAGE_TYPE_LEFT_VIDEO, R.layout.item_chart_left_img_video)
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_VIDEO,
            R.layout.item_chart_right_img_video
        )

        /**
         * 语音消息
         */
        addItemType(TIMCommonConstants.MESSAGE_TYPE_RIGHT_SOUND, R.layout.item_chart_right_voice)
        addItemType(TIMCommonConstants.MESSAGE_TYPE_LEFT_SOUND, R.layout.item_chart_left_voice)

        /**
         * 自定义消息：帖子，视频，话题
         */
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_POST,
            R.layout.item_chart_right_post
        )
        addItemType(TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_POST, R.layout.item_chart_left_post)

        /**
         * 自定义消息：发送个人名片
         */
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_USER,
            R.layout.item_chart_right_personal_file
        )
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_USER,
            R.layout.item_chart_left_personal_file
        )


        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_SINGLE,
            R.layout.item_chart_right_post
        )
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_SINGLE,
            R.layout.item_chart_left_post
        )


        /**
         * 图片消息
         */
        addItemType(TIMCommonConstants.MESSAGE_TYPE_LEFT_IMAGE, R.layout.item_chart_left_img_video)
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_IMAGE,
            R.layout.item_chart_right_img_video
        )

        /**
         * 消息撤回
         */
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED,
            R.layout.item_chart_center_hint
        )
        /**
         * 消息删除
         */
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_DELETE,
            R.layout.item_chart_center_hint
        )
        /**
         * 插入一条本地消息
         */
        addItemType(
            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_IMPORTED,
            R.layout.item_chart_center_hint
        )

        addChildClickViewIds(R.id.iv_fail, R.id.tv_voice_content)


    }

    override fun convert(holder: BaseViewHolder, item: ConversionEntity) {


        if (item.itemType != TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_DELETE &&
            item.itemType != TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_IMPORTED &&
            item.itemType != TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED
        ) {
            /**
             * 设置时间
             */
            holder.setText(
                R.id.tv_time,
                Singleton.getFriendlyTimeSpanByNow(item.message!!.timestamp * 1000)
            )

            if (holder.bindingAdapterPosition != data.size - 1) {
                setTimeGone(
                    item.message!!.timestamp,
                    data[holder.bindingAdapterPosition + 1].message!!.timestamp,
                    holder.getView(R.id.tv_time)
                )
            }
            /**
             * 加载头像
             */
            holder.getView<ImageView>(R.id.iv_head_img)
                .loadRoundImg(mContext, item.headerImgUrl)
        }


        when (item.itemType) {

            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_DELETE -> {
                holder.setText(R.id.tv_hint, "消息已删除")
            }

            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_IMPORTED -> {
//                导入到本地的消息
                holder.setText(R.id.tv_hint, item.message!!.textElem.text)
            }

            TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED -> {
                //消息被撤回
                if (item.message!!.isSelf)
                    holder.setText(R.id.tv_hint, "您撤回了一条消息")
                else {
                    holder.setText(R.id.tv_hint, "对方撤回了一条消息")
                }
            }

            TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT -> {

                /**
                 * 展示文本消息
                 */
                handleTextMsg(
                    holder.getView<TextView>(R.id.tv_content),
                    item.message!!.textElem.text
                )

                //引用消息
                if (!TextUtils.isEmpty(item.message!!.cloudCustomData)) {
                    var jsonObject: JSONObject = JSONObject(item.message!!.cloudCustomData)
                    //引用消息
                    quoteMsg(jsonObject, item, holder)
                } else {
                    holder.setGone(R.id.llayout_quote_content, true)
                }

                //设置消息状态
                setMsgStatus(item, holder)

            }
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_TEXT -> {

                handleTextMsg(
                    holder.getView<TextView>(R.id.tv_content),
                    item.message!!.textElem.text
                )

                //设置消息状态
                setMsgStatus(item, holder)

                //引用消息
                if (!TextUtils.isEmpty(item.message!!.cloudCustomData)) {
                    var jsonObject: JSONObject = JSONObject(item.message!!.cloudCustomData)
                    //引用消息
                    quoteMsg(jsonObject, item, holder)
                } else {
                    holder.setGone(R.id.llayout_quote_content, true)
                }

            }
            TIMCommonConstants.MESSAGE_TYPE_LEFT_VIDEO -> {

                //消息是否发送成功
                holder.setGone(R.id.iv_fail, true)
                //展示视频播放按钮
                holder.setGone(R.id.iv_video_play, false)

                handleVideoMsg(
                    0,
                    item.message!!,
                    holder.getView(R.id.view_video_download_mask),
                    holder.getView(R.id.iv_video_play),
                    holder.getView(R.id.progress),
                    holder.getView(R.id.iv_left_cover),
                    holder.getView(R.id.iv_head_img)
                )

            }
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_VIDEO -> {


                //消息是否发送成功
                holder.setGone(R.id.iv_fail, true)
                //展示视频播放按钮
                holder.setGone(R.id.iv_video_play, false)

                handleVideoMsg(
                    1,
                    item.message!!,
                    holder.getView(R.id.view_video_download_mask),
                    holder.getView(R.id.iv_video_play),
                    holder.getView(R.id.progress),
                    holder.getView(R.id.iv_right_cover),
                    holder.getView(R.id.iv_head_img)
                )

            }

            /**
             * 语音消息
             */
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_SOUND,
            TIMCommonConstants.MESSAGE_TYPE_LEFT_SOUND,
            -> {

                //设置消息状态
                setMsgStatus(item, holder)
                handleSoundMsg(holder, item)
            }

            /**
             * 自定义消息：帖子，视频，话题
             */
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_POST,
            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_POST,
            -> {
                //设置消息状态
                setMsgStatus(item, holder)
                var jsonObject: JSONObject = JSONObject(String(item.message!!.customElem.data))
                handleCustomPostMsg(jsonObject, item, holder)
            }
            /**
             * 自定义消息：用户名片
             */
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_USER,
            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_USER,
            -> {
                //设置消息状态
                setMsgStatus(item, holder)
                var jsonObject: JSONObject = JSONObject(String(item.message!!.customElem.data))
                handleCustomUsertMsg(jsonObject, item, holder)
            }

            /**
             * 自定义消息：单品
             */
            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_SINGLE,
            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_SINGLE,
            -> {
                //设置消息状态
                setMsgStatus(item, holder)
                var jsonObject: JSONObject = JSONObject(String(item.message!!.customElem.data))
                handleCustomPostMsg(jsonObject, item, holder)
            }
        }
    }

    /**
     * 自定义消息：用户名片
     */
    private fun handleCustomUsertMsg(
        jsonObject: JSONObject,
        item: ConversionEntity,
        holder: BaseViewHolder,
    ) {
        //
        if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)) {

            holder.getView<ImageView>(R.id.iv_cover)
                .loadRectImg(
                    mContext,
                    item.shareUserEntity!!.backgroundUrl
                )

            holder.getView<ImageView>(R.id.iv_user_head_img)
                .loadRoundImg(
                    mContext,
                    item.shareUserEntity!!.avatarUrl
                )
            holder.setText(R.id.tv_user_nick_name, item.shareUserEntity!!.nickName)
                .setText(
                    R.id.tv_post_fans_num,
                    "帖子：${item.shareUserEntity!!.feedCount} | 粉丝：${item.shareUserEntity!!.followerCount}"
                )
                .setText(R.id.tv_personal_sign, item.shareUserEntity!!.descript)
            holder.setGone(R.id.tv_personal_sign, TextUtils.isEmpty(item.shareUserEntity!!.descript))


        }
    }

    private fun handleCustomPostMsg(
        jsonObject: JSONObject,
        item: ConversionEntity,
        holder: BaseViewHolder,
    ) {
        //帖子
        if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)) {

            //视频
            if (item.sharePostEntity!!.type == 1) {
                holder.setGone(R.id.iv_video_play, false)
            } else {
                //帖子
                holder.setGone(R.id.iv_video_play, true)
            }
            holder.getView<ImageView>(R.id.iv_cover)
                .loadRectImg(
                    mContext,
                    item.sharePostEntity!!.picUrls.first().url
                )

            holder.getView<ImageView>(R.id.iv_post_head_img)
                .loadRoundImg(
                    mContext,
                    item.sharePostEntity!!.avatarUrl
                )
            holder.setText(R.id.tv_post_content, item.sharePostEntity!!.title)
                .setText(R.id.tv_post_nick_name, item.sharePostEntity!!.nickName)
            holder.setGone(R.id.tv_post_content, TextUtils.isEmpty(item.sharePostEntity!!.title))

        } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)) {

            //视频
            if (item.sharePostEntity!!.type == 1) {
                holder.setGone(R.id.iv_video_play, false)
            } else {
                //帖子
                holder.setGone(R.id.iv_video_play, true)
            }
            holder.getView<ImageView>(R.id.iv_cover)
                .loadRectImg(
                    mContext,
                    item.sharePostEntity!!.picUrls.first().url
                )

            holder.setText(R.id.tv_post_content, item.sharePostEntity!!.title)

            holder.setGone(R.id.tv_post_content, TextUtils.isEmpty(item.sharePostEntity!!.title))
            holder.setGone(R.id.iv_post_head_img, true)
            holder.setGone(R.id.tv_post_nick_name, true)


        } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)) {

            holder.setGone(R.id.iv_video_play, true)

            holder.getView<ImageView>(R.id.iv_cover)
                .loadRectImg(
                    mContext,
                    item.shareSingleEntity!!.coverUrl
                )

            holder.setText(R.id.tv_post_content, item.shareSingleEntity!!.name)
            holder.setGone(R.id.tv_post_content, TextUtils.isEmpty(item.shareSingleEntity!!.name))
            holder.setGone(R.id.iv_post_head_img, true)
            holder.setGone(R.id.tv_post_nick_name, true)


        }
    }

    private fun setMsgStatus(
        item: ConversionEntity,
        holder: BaseViewHolder,
    ) {
        //消息发送失败
        if (item.message!!.status == Message.V2TIM_MSG_STATUS_SEND_FAILED) {
            holder.setGone(R.id.iv_fail, false)
            holder.setGone(R.id.pr_is_send, true)
            //消息发送中
        } else if (item.message!!.status == Message.V2TIM_MSG_STATUS_SENDING) {
            holder.setGone(R.id.iv_fail, true)
            holder.setGone(R.id.pr_is_send, false)
            //消息发送成功
        } else {
            holder.setGone(R.id.iv_fail, true)
            holder.setGone(R.id.pr_is_send, true)
        }
    }

    fun handleSoundMsg(holder: BaseViewHolder, item: ConversionEntity) {
        var soundElem = item.message!!.soundElem
        soundElem.getUrl(object : V2TIMValueCallback<String?> {
            override fun onSuccess(p0: String?) {
                LogUtils.e(">>>> " + p0)
                holder.setText(R.id.tv_voice_content, "${soundElem.duration}''")

                val view = holder.getView<TextView>(R.id.tv_voice_content)

                var layoutParams = view.layoutParams as LinearLayout.LayoutParams

                layoutParams.width =
                    SizeUtils.dp2px(80f) +
                            ((ScreenUtils.getScreenWidth() / 2 - SizeUtils.dp2px(80f)) * soundElem.duration / 60)

                view.layoutParams = layoutParams

                if (TextUtils.isEmpty(soundElem.path) && TextUtils.isEmpty(p0)) {
                    holder.setText(R.id.tv_voice_content, "消息错误")
                    layoutParams.width =
                        SizeUtils.dp2px(140f)
                    view.layoutParams = layoutParams
                }
            }

            override fun onError(p0: Int, p1: String?) {
                LogUtils.e(">>>> p0 = " + p0 + "  p1 = " + p1)
            }
        })
    }


    private fun quoteMsg(
        jsonObject: JSONObject,
        item: ConversionEntity,
        holder: BaseViewHolder,
    ) {
        if (jsonObject.has(TIMCommonConstants.MESSAGE_REPLY_KEY)) {
            val gson = Gson()
            val replyHashMap: HashMap<*, *> = gson.fromJson<HashMap<*, *>>(
                item.message!!.cloudCustomData,
                HashMap::class.java
            )
            val replyContentObj =
                replyHashMap[TIMCommonConstants.MESSAGE_REPLY_KEY]
            var replyPreviewBean: QuoteMessageBean? = null
            if (replyContentObj is Map<*, *>) {
                replyPreviewBean = gson.fromJson(
                    gson.toJson(replyContentObj),
                    QuoteMessageBean::class.java
                )
                holder.setGone(R.id.llayout_quote_content, false)
                LogUtils.d(">>> " + replyPreviewBean.toString())
                if (replyPreviewBean.message == null) {
                    return
                }
                if (replyPreviewBean.message!!.messageBaseElements.isNullOrEmpty()) {
                    return
                }
                var timMessageBaseElement: TimMessageBaseElement =
                    replyPreviewBean.message!!.messageBaseElements.first()

                if (timMessageBaseElement.elementType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                    holder.setText(
                        R.id.tv_quote_content,
                        if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED)
                            "消息已删除"
                        else if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED)
                            "消息已撤回"
                        else
                            replyPreviewBean.message!!.nickName + ":" + String(
                                timMessageBaseElement.textContentBytes
                            )
                    )

                    holder.setGone(R.id.iv_quote_image, true)

                } else if (timMessageBaseElement.elementType == V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO) {

                    holder.setText(
                        R.id.tv_quote_content,
                        if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED)
                            "消息已删除"
                        else if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED)
                            "消息已撤回"
                        else
                            if (item.itemType == TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT) {
                                replyPreviewBean.message!!.nickName + " : "
                            } else {
                                " : " + replyPreviewBean.message!!.nickName
                            }
                    )

                    if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED
                        || replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED
                    ) {
                        holder.setGone(R.id.iv_quote_image, true)
                    } else {
                        holder.getView<ImageView>(R.id.iv_quote_image).loadRectImg(
                            mContext,
                            timMessageBaseElement.snapshotDownloadUrl
                        )
                        holder.setGone(R.id.iv_quote_image, false)
                    }


                } else if (timMessageBaseElement.elementType == V2TIMMessage.V2TIM_ELEM_TYPE_SOUND) {

                    holder.setText(
                        R.id.tv_quote_content,
                        if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED)
                            "消息已删除"
                        else if (replyPreviewBean.message!!.messageStatus == V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED)
                            "消息已撤回"
                        else
                            if (item.itemType == TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT) {
                                replyPreviewBean.message!!.nickName + " : "
                            } else {
                                " : " + replyPreviewBean.message!!.nickName
                            }
                    )

                    holder.setGone(R.id.iv_quote_image, true)


                } else if (timMessageBaseElement.elementType == V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE

                ) {

                }
            }
            //按需要后期增加
        } else {
            holder.setGone(R.id.llayout_quote_content, false)

        }
    }

    fun getQuoteMsg(
        jsonObject: JSONObject,
        cloudCustomData: String,
    ): QuoteMessageBean? {
        if (jsonObject.has(TIMCommonConstants.MESSAGE_REPLY_KEY)) {
            val gson = Gson()
            val replyHashMap: HashMap<*, *> = gson.fromJson<HashMap<*, *>>(
                cloudCustomData,
                HashMap::class.java
            )
            val replyContentObj =
                replyHashMap[TIMCommonConstants.MESSAGE_REPLY_KEY]
            var replyPreviewBean: QuoteMessageBean? = null
            if (replyContentObj is Map<*, *>) {
                replyPreviewBean = gson.fromJson(
                    gson.toJson(replyContentObj),
                    QuoteMessageBean::class.java
                )
            }
            return replyPreviewBean
        }
        return null

    }

    private fun handleTextMsg(mTextInput: TextView, editable: CharSequence) {
        FaceManager.handlerEmojiText(mTextInput, editable, false)
    }

    private fun setTimeGone(thisTime: Long, nextTime: Long, tvTime: TextView) {
        if (thisTime - nextTime > (5 * 60)) {
            tvTime.visibility = View.VISIBLE
        } else {
            tvTime.visibility = View.GONE
        }
    }

    private fun handleVideoMsg(
        isRight: Int = 0,
        msg: V2TIMMessage,
        maskView: View,
        payBtn: ImageView,
        progress: TextView,
        cover: ImageView,
        headImg: ImageView,
    ) {

        val v2TIMVideoElem: V2TIMVideoElem = msg.getVideoElem()

        // 视频截图 ID,内部标识，可用于外部缓存 key
        val snapshotUUID: String = v2TIMVideoElem.getSnapshotUUID()
        // 视频截图文件大小
        val snapshotSize: Int = v2TIMVideoElem.getSnapshotSize()
        // 视频截图宽
        val snapshotWidth: Int = v2TIMVideoElem.getSnapshotWidth()
        // 视频截图高
        val snapshotHeight: Int = v2TIMVideoElem.getSnapshotHeight()
        // 视频 ID,内部标识，可用于外部缓存 key
        val videoUUID: String = v2TIMVideoElem.getVideoUUID()
        // 视频文件大小
        val videoSize: Int = v2TIMVideoElem.getVideoSize()
        // 视频时长
        val duration: Int = v2TIMVideoElem.getDuration()
//        snapshot height:1920, snapshot width:1080,

        maskView.visibility = View.VISIBLE
        payBtn.visibility = View.GONE
        progress.text = "0%"
        progress.visibility = View.VISIBLE

        var layoutParams =
            cover.layoutParams as ConstraintLayout.LayoutParams

        if (isRight == 0) {
            layoutParams.leftToRight = R.id.iv_head_img
            layoutParams.rightToLeft = R.id.iv_head_img_fake
            layoutParams.topToTop = R.id.iv_head_img
            layoutParams.marginStart = DisplayUtils.dip2px(mContext, 10.0f)
            layoutParams.marginEnd = DisplayUtils.dip2px(mContext, 58.0f)
            layoutParams.dimensionRatio = "H,${snapshotWidth}:${snapshotHeight}"
            cover.layoutParams = layoutParams

        } else {
            layoutParams.rightToLeft = R.id.iv_head_img
            layoutParams.leftToRight = R.id.iv_head_img_fake
            layoutParams.topToTop = R.id.iv_head_img
            layoutParams.marginEnd = DisplayUtils.dip2px(mContext, 10.0f)
            layoutParams.marginStart = DisplayUtils.dip2px(mContext, 58.0f)
            layoutParams.dimensionRatio = "H,${snapshotWidth}:${snapshotHeight}"
            cover.layoutParams = layoutParams
        }
//534173445859712
//537086813760384
        downLoadVideoCover(snapshotUUID, v2TIMVideoElem, cover)
        // 设置视频文件路径，这里可以用 uuid 作为标识，避免重复下载
        val videoPath = createCacheDir(mContext) + "/video_url_" + videoUUID
        val videoFile = File(videoPath)

        if (!videoFile.exists()) {
            v2TIMVideoElem.downloadVideo(videoPath, object : V2TIMDownloadCallback {
                override fun onSuccess() {
                    maskView.visibility = View.GONE
                    payBtn.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }

                override fun onError(p0: Int, p1: String?) {

                }

                override fun onProgress(progressInfo: V2TIMElem.V2ProgressInfo) {
                    var pr = (progressInfo.currentSize / progressInfo.totalSize).toInt()
                    progress.text = "$pr%"
                }

            })
        } else {
            maskView.visibility = View.GONE
            payBtn.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }

    }

    private fun downLoadVideoCover(
        snapshotUUID: String,
        v2TIMVideoElem: V2TIMVideoElem,
        cover: ImageView,
    ) {
        val snapshotPath = createCacheDir(mContext) + "/video_cover_" + snapshotUUID
        val snapshotFile = File(snapshotPath)
        if (!snapshotFile.exists()) {
            v2TIMVideoElem.downloadSnapshot(snapshotPath, object : V2TIMDownloadCallback {
                override fun onSuccess() {
                    GlideApp.with(mContext)
                        .load(snapshotPath)
                        .into(cover)
                }

                override fun onError(p0: Int, p1: String?) {
                }

                override fun onProgress(p0: V2TIMElem.V2ProgressInfo?) {

                }
            })
            //已经下载，直接加载图片
        } else {
            //            height:1920, snapshot width:1080,
            GlideApp.with(mContext)
                .load(snapshotPath)
                .into(cover)
        }
    }

    fun createCacheDir(context: Context): String {
        var cacheDir = context.externalCacheDir
        if (cacheDir == null) {
            cacheDir = context.cacheDir
        }
        cacheDir = File(cacheDir, "video")
        return cacheDir.absolutePath
    }
}