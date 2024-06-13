package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.Single.extractTextBetweenTags
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DicAdapter_four(
    private var dataList: List<DicBean>,
    private val mActivity: Activity
) : RecyclerView.Adapter<DicAdapter_four.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dic_four, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hanzi.text = dataList[position].hanzi
        holder.num.text = dataList[position].num.toString()


        //设置适配器
        val m = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
        holder.recyclerView_dic.layoutManager = m
        holder.recyclerView_dic.isNestedScrollingEnabled = false//解决滑动冲突
        val mAdapter = DicAdapter(dataList[position].list)
        holder.recyclerView_dic.adapter = mAdapter

        //回调
        mAdapter.setDicAdapterCallBack(object : DicAdapter.InnerInterface {
            override fun onclick(hanzi: String) {

                var isHave=true
                val assetManager = mActivity.assets
                try {
                    val inputStream = assetManager.open("dict/新华字典-整理后.txt")
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        if (extractTextBetweenTags(line!!,"<1>","<2>").contains(hanzi)){
                            Single.showHtml(mActivity,extractTextBetweenTags(line!!,"<2>","<3>"))
                            isHave=false
                            break
                        }
                    }
                    reader.close()

                    if(isHave){
                        Single.showHtml(mActivity,"暂未收录")
                    }
                } catch (e: Exception) {
                    Log.e("tag11", "DicAdapter_four"+e.toString())
                }




            }
        })
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.tv1)
        var num: TextView = itemView.findViewById(R.id.tv2)
        var recyclerView_dic: RecyclerView = itemView.findViewById(R.id.recyclerView_dic)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
