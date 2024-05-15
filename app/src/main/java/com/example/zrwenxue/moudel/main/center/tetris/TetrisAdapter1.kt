package com.example.zrwenxue.moudel.main.center.tetris

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrtool.ui.uibase.BaseTextView


/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 15:27
 */
class TetrisAdapter1(private var mContent: Context, private var dataList: MutableList<TetrisBean>) :
    RecyclerView.Adapter<TetrisAdapter1.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_tetris_item, parent, false)
        var viewHolder = MyViewHolder(view)

        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
//        val bean = dataList[position]//获取一个实体对象
//        holder.contentTextView.text = bean.content

        //例如，如果视频的纵横比为16:9，而屏幕宽度为360像素，则视频高度可以按照以下方式计算： 高 / 宽 * 控件宽度
        val imgHeight = dataList[position].height //设置控件高度
        val params: ViewGroup.LayoutParams = holder.mCard.layoutParams
        params.height = imgHeight
        holder.mCard.layoutParams = params
        //设置背景颜色
        if (dataList[position].color!="#00ffffff"){
            holder.contentTextView.setTextColor(Color.parseColor(dataList[position].color))
        }else{
            holder.contentTextView.setBackgroundColor(Color.parseColor(dataList[position].color))
        }


        /**头像*/
//        Glide.with(mContent)
//            .asBitmap()
//            .dontAnimate()
//            .skipMemoryCache(false)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .load(dataList[position].content)
//            .into(holder.contentTextView)

        holder.contentTextView.text = dataList[position].content

        if (dataList[position].content == "") {
            holder.contentTextView.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            mTetrisAdapterInterface!!.onRecyclerViewItemClick(
                it,
                dataList[position].mPosition,
                dataList[position].content
            )
        }

    }


    var mTetrisAdapterInterface: TetrisAdapterInterface? = null
    fun setOnItemClickListener(mTetrisAdapterInterface: TetrisAdapterInterface) {
        this.mTetrisAdapterInterface = mTetrisAdapterInterface
    }

    interface TetrisAdapterInterface {
        fun onRecyclerViewItemClick(view: View?, position: Int, contentStr: String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentTextView: BaseTextView = itemView.findViewById(R.id.tv)
        var mCard: LinearLayout = itemView.findViewById(R.id.card)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
