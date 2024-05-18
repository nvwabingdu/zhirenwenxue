package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class SingleSimilarWearAdapter (private var mContent: Context,
                                private var dataList:MutableList<GetSimilarRecommendBean.Item>,
                                private var weightW:Float,
                                private var weightScreen:Float
                                ) :
    RecyclerView.Adapter<SingleSimilarWearAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scale_image2, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val displayMetrics = mContent.resources.displayMetrics
        val w2 = displayMetrics.widthPixels * (weightW / weightScreen) //需要占的宽度  //关键代码
        try {
            val params: ViewGroup.LayoutParams = holder.scaleImageView.layoutParams
            params.width = w2.toInt()
            params.height = w2.toInt()
            holder.scaleImageView.layoutParams = params
        } catch (e: Exception) {
            val params: ViewGroup.LayoutParams = holder.scaleImageView.layoutParams
            params.width = w2.toInt()
            params.height = w2.toInt()
            holder.scaleImageView.layoutParams = params
        }

        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.temp_icon)
//            .load(dataList[position].coverUrl)
            .load(dataList[position].picUrl)
            .into(holder.scaleImageView)

        //回调
//        holder.itemView.setOnClickListener {
//            mInterface?.onclick(dataList[position].id)
//        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var scaleImageView: ImageView =itemView.findViewById(R.id.scale_imageView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetSimilarRecommendBean.Item>, isClear: Boolean) {
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
    interface InnerInterface {
        fun onclick(id: Long)
    }

    private var mInterface: InnerInterface? = null

    fun setSingleSimilarWearAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

}