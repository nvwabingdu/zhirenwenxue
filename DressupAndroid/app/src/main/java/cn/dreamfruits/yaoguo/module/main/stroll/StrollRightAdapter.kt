package cn.dreamfruits.yaoguo.module.main.stroll

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Tool
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class StrollRightAdapter (private var mContent: Context,
                          private var dataList:MutableList<String>) :
    RecyclerView.Adapter<StrollRightAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stroll_right, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        Glide.with(mContent)
//            .asBitmap()
//            .dontAnimate()
//            .skipMemoryCache(false)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .error(R.drawable.temp_icon)
////            .load(dataList[position].coverUrl)
//            .load(Tool().decodePicUrls(dataList[position].coverUrl,"0",true) )
//            .into(holder.StrollImg)

//        holder.StrollTv.text= dataList[position].name


//        //点击事件
//        holder.itemView.setOnClickListener {
//            mStrollRightAdapterInterface?.onclick(dataList[position].id)
//        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var StrollImg: ImageView =itemView.findViewById(R.id.stroll_img)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<String>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }



    interface  StrollRightAdapterInterface{
        fun onclick(id: Long)
    }

    private var mStrollRightAdapterInterface:StrollRightAdapterInterface? = null

    fun setStrollRightAdapterCallBack(mStrollRightAdapterInterface:StrollRightAdapterInterface){
        this.mStrollRightAdapterInterface=mStrollRightAdapterInterface
    }

}