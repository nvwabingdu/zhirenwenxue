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
import com.example.zrprint.extractTextBeforeDelimiter
import com.example.zrprint.extractTextBeforeDelimiter2
import com.example.zrwenxue.app.Single
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

        //用拼音设置 是否开启


        if (dataList[position].pinyin == "open") {

            holder.recyclerView_dic.visibility = View.VISIBLE

//            val mColor= MyStatic.getContrastingColors()
//            if (position!=0&&position!=1){
//                holder.itemLayout.setBackgroundColor(mColor[0])
//                holder.hanzi.setTextColor(mColor[0])
//                holder.num.setTextColor(mColor[0])
//            }

            //设置适配器
            val m = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
            holder.recyclerView_dic.layoutManager = m
//        holder.recyclerView_dic.isNestedScrollingEnabled = false//解决滑动冲突
            val mAdapter = DicAdapter(dataList[position].list)
            holder.recyclerView_dic.adapter = mAdapter

            //回调
            mAdapter.setDicAdapterCallBack(object : DicAdapter.InnerInterface {
                override fun onclick(hanzi: String) {

                    var isHave = true
                    val assetManager = mActivity.assets
                    try {
                        val inputStream = assetManager.open("dict/字典.txt")
                        val reader = BufferedReader(InputStreamReader(inputStream))

                        var line: String?
                        while (reader.readLine().also { line = it } != null) {

                            if (extractTextBeforeDelimiter(line!!, "<").contains(hanzi)) {

                                Single.showHtml(
                                    mActivity,
                                    extractTextBeforeDelimiter2(line!!, "<")
                                )

                                isHave = false
                                break
                            }
                        }
                        reader.close()

                        if (isHave) {
                            Single.showHtml(mActivity, "暂未收录")
                        }
                    } catch (e: Exception) {
                        Log.e("tag11", "DicAdapter_four" + e.toString())
                    }
                }
            })

        } else {
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.theme_up))
//            holder.hanzi.setTextColor(Color.BLACK)
//            holder.num.setTextColor(Color.BLACK)

            holder.recyclerView_dic.visibility = View.GONE
        }


        //点击事件
        holder.itemLayout.setOnClickListener {

            if(position!=0&&position!=1&&position!=2){
                if (dataList[position].pinyin == "open") {
                    dataList[position].pinyin = "close"
                } else {
                    dataList[position].pinyin = "open"
                }

                dataList[0].pinyin = "open"
                dataList[1].pinyin = "open"
                dataList[2].pinyin = "open"
                notifyDataSetChanged()
            }else{
//                MyStatic.showToast(mActivity,"固定[a][ai]为打开状态")
            }

        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.tv1)
        var num: TextView = itemView.findViewById(R.id.tv2)
        var itemLayout: View = itemView.findViewById(R.id.item_layout)
        var recyclerView_dic: RecyclerView = itemView.findViewById(R.id.recyclerView_dic)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
