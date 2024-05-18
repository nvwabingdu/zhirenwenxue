package cn.dreamfruits.yaoguo.module.main.diy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.diy.bean.DiyLeftBean
import cn.dreamfruits.yaoguo.module.main.stroll.inner.UnderlinedTextView
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class DiyLeftAdapter (private var mContent: Context,
                      private var dataList:MutableList<GetStyleVersionListByTypeBean.Item>) :
    RecyclerView.Adapter<DiyLeftAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diy_left, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.diyTv.text=dataList[position].name

        if (dataList[position].isSelect){
            holder.diyTv.setTextColor(Color.parseColor("#000000"))
            holder.diyTv.setTypeface(null, Typeface.BOLD)
//            holder.diyTv.setUnderlineDistance(mContent.getResources().getDisplayMetrics().density * 2)
            holder.diyTv.setUnderlineDistance(0.0f)//都设置为0 不然会有下划线
            holder.diyLeftLl.setBackgroundColor(Color.parseColor("#EFEFEF"))
        }else{
            holder.diyTv.setTextColor(Color.parseColor("#80222222"))
            holder.diyTv.setTypeface(null, Typeface.NORMAL)
            holder.diyTv.setUnderlineDistance(0.0f)
            holder.diyLeftLl.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        holder.diyLeftLl.setOnClickListener {
            for (i in dataList.indices){
                dataList[i].isSelect=false
            }
            dataList[position].isSelect=true
            notifyDataSetChanged()
            mDiyLeftAdapterInterface!!.onclick(position)//设置回调
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var diyTv: UnderlinedTextView =itemView.findViewById(R.id.diy_tv)
//        var diyLine: View =itemView.findViewById(R.id.diy_line)
        var diyLeftLl: View =itemView.findViewById(R.id.diy_left_ll)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetStyleVersionListByTypeBean.Item>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


    interface  DiyLeftAdapterInterface{
        fun onclick(position: Int)
    }

    var mDiyLeftAdapterInterface: DiyLeftAdapterInterface? = null

    fun setDiyLeftAdapterCallBack(mDiyLeftAdapterInterface: DiyLeftAdapterInterface){
        this.mDiyLeftAdapterInterface=mDiyLeftAdapterInterface
    }


}