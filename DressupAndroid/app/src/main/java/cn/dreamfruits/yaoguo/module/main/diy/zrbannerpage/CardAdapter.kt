package cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.library.CardAdapterHelper
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/7/11
 * @TIME 16:59
 */
class CardAdapter(
    private var mContent: Context,
    private var dataList: MutableList<GetStyleVersionListByTypeBean.Item>
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    private val mCardAdapterHelper = CardAdapterHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_card_item, parent, false)
        mCardAdapterHelper.onCreateViewHolder(parent, itemView)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, itemCount)


        /**预加载回调*/
        if (dataList.size > 3 && position == dataList.size - 3) {
            mInterface!!.onPreload()
        }

        /**图片*/
        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList[position].coverUrl)
            .into(holder.mImageView)

        /**标题*/
        holder.mTv.text = dataList[position].name


        holder.mImageView.setOnClickListener { view ->
            mInterface!!.onclick(dataList[position].id, position)
            Log.e(
                "zqr176543",
                "适配器中 当前点击了banner的   ======" + position + "" + dataList[position].name
            )
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView = itemView.findViewById(R.id.imageView)
        val mTv: TextView = itemView.findViewById(R.id.zr_title)
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
            notifyItemRangeChanged(dataList.size, dataList.size + tempList.size)
        }
    }


    //回调
    interface InnerInterface {
        fun onclick(id: Long, position: Int)

        fun onPreload()
    }

    private var mInterface: InnerInterface? = null

    fun setCardAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

}