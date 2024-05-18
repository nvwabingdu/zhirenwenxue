package cn.dreamfruits.yaoguo.module.main.home.searchhistory.pop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.search.SearchWordBean
import cn.dreamfruits.yaoguo.util.Singleton

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class SearchPopAdapter (private var mContent: Context,
                        private var dataList:MutableList<SearchWordBean.Item>) :
    RecyclerView.Adapter<SearchPopAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_pop, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.searchHistoryPopTv.text=  boldAndColorSubstring(dataList[position].name,setRich(dataList[position].key,dataList[position].name)!!)
        }catch (e:Exception){
            holder.searchHistoryPopTv.text= dataList[position].name
        }

        if (dataList[position].viewCount.toInt()==0){
            holder.searchHistoryPopTvCount.visibility=View.GONE
        }else{
            holder.searchHistoryPopTvCount.visibility=View.VISIBLE
            holder.searchHistoryPopTvCount.text = "${Singleton.setNumRuler(dataList[position].viewCount.toInt(),"0")}"+"+篇笔记"
        }

        //通过回调设置
        holder.itemView.setOnClickListener {
            mInterface!!.onclick(dataList[position].name)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var searchHistoryPopTv: TextView =itemView.findViewById(R.id.search_history_pop_tv)
        var searchHistoryPopTvCount: TextView =itemView.findViewById(R.id.search_history_pop_tv_count)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * b中含有a 及a的子字符串  加黑加粗
     */
    private  fun boldAndColorSubstring(name: String,mList:MutableList<Int>): SpannableStringBuilder? {
        val builder = SpannableStringBuilder(name)

            // 对匹配到的子串设置样式
            for (i in name.indices) {
                if (mList.contains(i)){
                    val boldSpan = StyleSpan(Typeface.BOLD)
                    val colorSpan = ForegroundColorSpan(Color.BLACK)
                    builder.setSpan(
                        boldSpan,
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.setSpan(
                        colorSpan,
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
        }
        return builder
    }

    /**
     * 设置富文本
     */
    private fun setRich(key:String,name:String):MutableList<Int>?{

        val keyArray=getSubstrings(key)

        var pArrays:MutableList<Int>?=ArrayList()

        try {
            for (element in keyArray){//依次取出 并添加
                pArrays!!.addAll(findOccurrences(element,name))
            }
            val sortedArray = pArrays!!.sorted()
            val distinctArray = sortedArray.toSet().toTypedArray()
            Log.e("TAG12121", "setRich: "+distinctArray.toMutableList().toString())
            return distinctArray.toMutableList()
        }catch (e:Exception){
            return null
        }



    }

    /**
     * 夏季日     返回 [夏, 夏季, 夏季日]
     */
    private fun getSubstrings(str: String): Array<String> {
        val subStrings = mutableListOf<String>()
        for (i in str.indices) {
            val substring = str.substring(0, i + 1)
            subStrings.add(substring)
        }
        return subStrings.toTypedArray()
    }
    /**
    val str = "小鸡爱吃大米小鸡"
    val target = "小鸡"
    val occurrences = findOccurrences(str, target)
    println(occurrences.contentToString())
    这将输出：[0,1,  6,7]。
     */
    private fun findOccurrences( target: String,str: String): MutableList<Int> {
        val indices = mutableListOf<Int>()
        var index = str.indexOf(target)
        while (index != -1) {
            (target.indices).forEach {
                indices.add(index + it)
            }
            index = str.indexOf(target, index + 1)
        }

        Log.e("TAG12121", "findOccurrences: "+indices.toString())
        return indices
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<SearchWordBean.Item>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }

    //回调
        interface  InnerInterface{
            fun onclick(key: String)
        }
        private var mInterface: InnerInterface? = null

        fun setSearchPopAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }


}