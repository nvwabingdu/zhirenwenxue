package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.util.Tool
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class SingleClothesAdapter (private var mContent: Context,
                            private var dataList:MutableList<GetClothesItemInfoBean.Clothes>) :
    RecyclerView.Adapter<SingleClothesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_84dp, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.temp_icon)
            .load(dataList[position].picUrl)
            .into(holder.img)


//        //点击事件
//        holder.itemView.setOnClickListener {
//            mInterface?.onclick(dataList[position].id)
//        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView =itemView.findViewById(R.id.img_84dp)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetClothesItemInfoBean.Clothes>, isClear: Boolean) {
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
            fun onclick(id: Long)
        }
        private var mInterface: InnerInterface? = null

        fun setSingleClothesAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}