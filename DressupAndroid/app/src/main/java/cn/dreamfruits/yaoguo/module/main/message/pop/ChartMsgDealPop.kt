package cn.dreamfruits.yaoguo.module.main.message.pop

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.BubbleAttachPopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.util.XPopupUtils
import com.tencent.imsdk.v2.V2TIMMessage


/**
 * @author Lee
 * @createTime 2023-06-29 14 GMT+8
 * @desc :
 */
class ChartMsgDealPop(
    var mContext: Context,
    var mConversation: ConversionEntity,
    var clickPop: OnChartPopClick,
) :
    BubbleAttachPopupView(mContext) {

    private lateinit var tv_chart_copy: TextView
    private lateinit var tv_chart_transpond: TextView
    private lateinit var tv_chart_quote: TextView
    private lateinit var tv_chart_rollback: TextView
    private lateinit var tv_chart_delete: TextView

    override fun getImplLayoutId(): Int {
        return R.layout.layout_chart_msg_deal_pop
    }


    override fun onCreate() {
        super.onCreate()

        tv_chart_copy = findViewById(R.id.tv_chart_copy)
        tv_chart_transpond = findViewById(R.id.tv_chart_transpond)
        tv_chart_quote = findViewById(R.id.tv_chart_quote)
        tv_chart_rollback = findViewById(R.id.tv_chart_rollback)
        tv_chart_delete = findViewById(R.id.tv_chart_delete)


        //超过两分钟不展示消息撤回按钮, 不是自己 发送的消息也不展示撤回按钮
        if (System.currentTimeMillis() - mConversation.message!!.timestamp * 1000 > 2 * 60 * 1000
            || !mConversation.message!!.isSelf
        ) {
            tv_chart_rollback.visibility = View.GONE
        } else {
            tv_chart_rollback.visibility = View.VISIBLE
        }

        //只有文本消息时，可以展示复制按钮
        if (mConversation.message!!.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
            tv_chart_copy.visibility = View.VISIBLE
        } else {
            tv_chart_copy.visibility = View.GONE
        }


        tv_chart_copy.setOnClickListener {
            ClipboardUtils.copyText(mConversation.message!!.textElem.text)
            ToastUtils.showShort("复制成功")
            dismiss()
        }

        tv_chart_delete.setOnClickListener {
            clickPop.onDelete(mConversation)
            dismiss()
        }
        tv_chart_quote.setOnClickListener {
            clickPop.onQuote(mConversation)
            dismiss()
        }
        tv_chart_transpond.setOnClickListener {
            clickPop.onTranspond(mConversation)
            dismiss()
        }
        tv_chart_rollback.setOnClickListener {
            clickPop.onRollBack(mConversation)
            dismiss()
        }






        setBubbleBgColor(Color.parseColor("#222222"))
        setBubbleShadowSize(XPopupUtils.dp2px(context, 0f))
        setBubbleShadowColor(Color.parseColor("#222222"))
        setArrowWidth(XPopupUtils.dp2px(context, 8f))
        setArrowHeight(XPopupUtils.dp2px(context, 9f))
        setArrowRadius(XPopupUtils.dp2px(context, 2f))

    }

    interface OnChartPopClick {
        fun onDelete(mConversation: ConversionEntity)
        fun onQuote(mConversation: ConversionEntity)
        fun onTranspond(mConversation: ConversionEntity)
        fun onRollBack(mConversation: ConversionEntity)
    }


}