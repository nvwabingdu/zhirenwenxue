package cn.dreamfruits.yaoguo.module.main.message.adapter

import android.content.Context
import android.view.View
import android.widget.*
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.module.main.message.chart.manage.FaceManager
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.loadRoundImg
import cn.dreamfruits.yaoguo.util.singleClick
import cn.dreamfruits.yaoguo.view.swipe.SwipeMenuLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE
import org.json.JSONObject


/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 12:29
 */
class MessageAdapter(var mContext: Context, var onChildItemClick: OnChildItemClick) :
    BaseQuickAdapter<ConversionEntity, BaseViewHolder>(R.layout.item_message_) {

    init {
    }

    override fun convert(holder: BaseViewHolder, item: ConversionEntity) {

        var sml_all = holder.getView<SwipeMenuLayout>(R.id.sml_all)
        sml_all.isSwipeEnable = item.type == 0

        holder.getView<ImageView>(R.id.message_icon)
            .loadRoundImg(mContext, item.headerImgUrl)

        holder.setText(R.id.message_name, item.conversation!!.showName)

        buildMsg(holder.getView<TextView>(R.id.message_details), item.conversation!!.lastMessage)


        holder.setText(
            R.id.message_time,
//            TimeUtils.getFriendlyTimeSpanByNow(item.conversation!!.lastMessage.timestamp * 1000)
            Singleton.getMessageListTime(item.conversation!!.lastMessage.timestamp * 1000)
        )

//        可以通过 V2TIMConversation 对象的 isPinned 字段，检查会话有没有置顶。
        holder.setGone(R.id.message_set_top_img, !item.conversation!!.isPinned)

        if (item.conversation!!.isPinned) {
            holder.setGone(R.id.tv_to_top, true)
            holder.setGone(R.id.tv_delete, true)
            holder.setGone(R.id.tv_confim_delete, true)
            holder.setGone(R.id.tv_cancel_top, false)
        } else {
            holder.setGone(R.id.tv_to_top, false)
            holder.setGone(R.id.tv_delete, false)
            holder.setGone(R.id.tv_confim_delete, true)
            holder.setGone(R.id.tv_cancel_top, true)
        }

        //消息未读数


//        获取消息接收选项（接收 | 接收但不提醒 | 不接收）
        //接收但不提醒
        if (item.conversation!!.recvOpt == V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE) {
            holder.setGone(R.id.message_mute_img, false)
            holder.setGone(R.id.view_red_dot, item.conversation!!.unreadCount == 0)
//       接收
        } else {
            holder.setText(
                R.id.message_message_count,
                if (item.conversation!!.unreadCount > 99) "99+" else item.conversation!!.unreadCount.toString()
            )

            holder.setGone(
                R.id.message_message_count,
                item.conversation!!.unreadCount == 0
            )

            holder.setGone(R.id.view_red_dot, true)
            holder.setGone(R.id.message_mute_img, true)
        }

        var tv_to_top = holder.getView<TextView>(R.id.tv_to_top)
        var tv_delete = holder.getView<TextView>(R.id.tv_delete)
        var tv_confim_delete = holder.getView<TextView>(R.id.tv_confim_delete)
        var tv_cancel_top = holder.getView<TextView>(R.id.tv_cancel_top)

        holder.getView<ImageView>(R.id.message_icon)
            .setOnClickListener { onChildItemClick.onContent(item, holder.bindingAdapterPosition) }
        holder.getView<TextView>(R.id.message_time)
            .setOnClickListener { onChildItemClick.onContent(item, holder.bindingAdapterPosition) }
        holder.getView<TextView>(R.id.message_name)
            .setOnClickListener { onChildItemClick.onContent(item, holder.bindingAdapterPosition) }
        holder.getView<TextView>(R.id.message_details)
            .setOnClickListener { onChildItemClick.onContent(item, holder.bindingAdapterPosition) }
        holder.getView<LinearLayout>(R.id.ll_right)
            .setOnClickListener { onChildItemClick.onContent(item, holder.bindingAdapterPosition) }

        tv_delete.setOnClickListener {
            tv_to_top.visibility = View.GONE
            tv_delete.visibility = View.GONE
            tv_confim_delete.visibility = View.VISIBLE
            tv_cancel_top.visibility = View.GONE
        }

        tv_confim_delete.singleClick {
            onChildItemClick.onDelete(item, holder.bindingAdapterPosition)
        }

        tv_cancel_top.singleClick {
            holder.getView<SwipeMenuLayout>(R.id.sml_all).smoothClose()
            onChildItemClick.onCancelTop(item, holder.bindingAdapterPosition)
        }

        tv_to_top.singleClick {
            holder.getView<SwipeMenuLayout>(R.id.sml_all).smoothClose()
            onChildItemClick.onToTop(item, holder.bindingAdapterPosition)
        }

        holder.getView<SwipeMenuLayout>(R.id.sml_all)
            .setOnSwipeClose {

                if (item.conversation!!.isPinned) {
                    holder.setGone(R.id.tv_to_top, true)
                    holder.setGone(R.id.tv_delete, true)
                    holder.setGone(R.id.tv_confim_delete, true)
                    holder.setGone(R.id.tv_cancel_top, false)
                } else {
                    tv_to_top.visibility = View.VISIBLE
                    tv_delete.visibility = View.VISIBLE
                    tv_confim_delete.visibility = View.GONE
                    tv_cancel_top.visibility = View.GONE
                }


            }
    }

    interface OnChildItemClick {
        fun onDelete(item: ConversionEntity, position: Int)
        fun onToTop(item: ConversionEntity, position: Int)
        fun onCancelTop(item: ConversionEntity, position: Int)
        fun onContent(item: ConversionEntity, position: Int)
    }

    fun buildMsg(textView: TextView, msg: V2TIMMessage) {

        when (msg.status) {
            //导入到本地的消息,插入的一条本地消息
            V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_IMPORTED -> {
                textView.text = msg.textElem.text

            } //消息被删除
            V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED -> {
                textView.text = "消息已删除"
            }//消息已撤回
            V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED -> {
                if (msg.isSelf)
                    textView.text = "对方撤回了一条消息"
                else
                    textView.text = "消息已撤回"
            } //正常消息
            else -> {
                if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                    FaceManager.handlerEmojiText(textView, msg.textElem.text, false)
                } else if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO) {
                    textView.text = "[视频]"
                } else if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE) {
                    textView.text = "[图片]"
                } else if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_SOUND) {
                    textView.text = "[语音]"
                } else if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {

                    if (msg.userID == "administrator") {
                        textView.text = String(msg.customElem.data)
                        return
                    }

                    // TODO:  需要修改
                    LogUtils.e(">>> "+msg.userID)

                    if (true){
                        return
                    }
                    var jsonObject: JSONObject = JSONObject(String(msg.customElem.data))
                    if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)) {
                        textView.text = "[帖子]"
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)) {
                        textView.text = "[用户名片]"
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)) {
                        textView.text = "[单品]"
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)) {
                        textView.text = "[话题]"
                    } else {

                    }
                }
            }
        }
    }
}


//class MessageAdapter(var onChildItemClick: OnChildItemClick) :
//    RecyclerSwipeAdapter<MessageAdapter.SimpleViewHolder>(),
//    SwipeItemMangerInterface, SwipeAdapterInterface {
//
//
//    var mItemManger: SwipeItemMangerImpl = SwipeItemMangerImpl(this)
//
//    private var clickEnable = true
//
//    private var mContext: Context? = null
//    private var mDataset: MutableList<GetMessageInnerPageListBean.Item> = arrayListOf()
//
//
//    fun setData(mDataset: MutableList<GetMessageInnerPageListBean.Item>) {
//        this.mDataset = mDataset
//        notifyDataSetChanged()
//    }
//
//
//    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var swipeLayout: SwipeMenuLayout
//        var tv_to_top: TextView
//        var tv_delete: TextView
//        var tv_confim_delete: TextView
//        var tv_cancel_top: TextView
//        var message_icon: ImageView
//
//        init {
//            swipeLayout = itemView.findViewById<View>(R.id.sml_all) as SwipeMenuLayout
//            tv_to_top = itemView.findViewById<TextView>(R.id.tv_to_top)
//            tv_delete = itemView.findViewById<TextView>(R.id.tv_delete)
//            tv_confim_delete = itemView.findViewById<TextView>(R.id.tv_confim_delete)
//            tv_cancel_top = itemView.findViewById<TextView>(R.id.tv_cancel_top)
//            message_icon = itemView.findViewById<ImageView>(R.id.message_icon)
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
//        val view: View =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_message_, parent, false)
//        return SimpleViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return mDataset.size
//    }
//
//    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
//        val item: GetMessageInnerPageListBean.Item = mDataset.get(position)
//
////        viewHolder.swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
////        viewHolder.swipeLayout.addSwipeListener(object : SimpleSwipeListener() {
////            override fun onOpen(layout: SwipeLayout?) {
////
////            }
////
////            override fun onClose(layout: SwipeLayout?) {
////                viewHolder.tv_to_top.visibility = View.VISIBLE
////                viewHolder.tv_delete.visibility = View.VISIBLE
////                viewHolder.tv_confim_delete.visibility = View.GONE
////                viewHolder.tv_cancel_top.visibility = View.GONE
////            }
////        })
//        viewHolder.itemView.setOnClickListener {
//            onChildItemClick.onContent(item, position)
//        }
//        viewHolder.swipeLayout.setOnClickListener {
//            onChildItemClick.onContent(item, position)
//        }
//        viewHolder.message_icon.setOnClickListener {
//            onChildItemClick.onContent(item, position)
//        }
//
//        viewHolder.tv_confim_delete.setOnClickListener {
//            onChildItemClick.onDelete(item, position)
//        }
//
//        viewHolder.tv_cancel_top.setOnClickListener {
//            onChildItemClick.onCancelTop(item, position)
//        }
//
//        viewHolder.tv_to_top.setOnClickListener {
//            onChildItemClick.onToTop(item, position)
//        }
//
//        viewHolder.tv_delete.setOnClickListener {
//            viewHolder.tv_to_top.visibility = View.GONE
//            viewHolder.tv_delete.visibility = View.GONE
//            viewHolder.tv_confim_delete.visibility = View.VISIBLE
//            viewHolder.tv_cancel_top.visibility = View.GONE
//        }
//
//
////        mItemManger.bind(viewHolder.itemView, position)
//    }
//
//    interface OnChildItemClick {
//        fun onDelete(item: GetMessageInnerPageListBean.Item, position: Int)
//        fun onToTop(item: GetMessageInnerPageListBean.Item, position: Int)
//        fun onCancelTop(item: GetMessageInnerPageListBean.Item, position: Int)
//        fun onContent(item: GetMessageInnerPageListBean.Item, position: Int)
//    }
//
//    override fun getSwipeLayoutResourceId(position: Int): Int {
//        return R.id.sml_all
//    }
//
//}