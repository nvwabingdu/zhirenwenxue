package cn.dreamfruits.yaoguo.module.pop

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemClickListener
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.impl.BottomListPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import java.util.*

/**
 * @author Lee
 * @createTime 2023-07-14 10 GMT+8
 * @desc :
 */
class BottomPop(
    var mContext: Context,
    var title: String = "",
    var cancelText: String = "",
    var itemTextColor :Int = R.color.color_ec6262,
    var data: List<String> = arrayListOf<String>(),
) :
    BottomPopupView(mContext) {

    var recyclerView: RecyclerView? = null
    var tv_title: TextView? = null
    var tv_cancel: TextView? = null

    override fun getImplLayoutId(): Int = R.layout.layout_bottom_pop

    override fun onCreate() {

        super.onCreate()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        tv_title = findViewById(R.id.tv_title)
        tv_cancel = findViewById(R.id.tv_cancel)

        tv_cancel!!.setOnClickListener { dismiss() }

        if (TextUtils.isEmpty(title)) {
            tv_title!!.visibility = GONE
            if (findViewById<View?>(R.id.xpopup_divider) != null) findViewById<View>(R.id.xpopup_divider).visibility =
                GONE
        } else {
            tv_title!!.setText(title)
        }

        val adapter: BaseQuickAdapter<String, BaseViewHolder> =
            object : BaseQuickAdapter<String, BaseViewHolder>(
                R.layout.item_bottom_pop_text
            ) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    holder.setText(R.id.tv_text, item)
                }
            }

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter1: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (selectListener != null) {
                    selectListener!!.onSelect(position, adapter.data.get(position))
                }
                if (popupInfo.autoDismiss) dismiss()
            }
        })

        recyclerView!!.adapter = adapter

        adapter.setList(data)

    }

    private var selectListener: OnSelectListener? = null

    fun setOnSelectListener(selectListener: OnSelectListener?): BottomPop {
        this.selectListener = selectListener
        return this
    }


}