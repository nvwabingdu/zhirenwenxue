package cn.dreamfruits.selector


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DateUtils

/**
 * 相册适配器
 */
class PictureListAdapter : RecyclerView.Adapter<PictureListAdapter.PictureViewHolder>() {

    val dataList = mutableListOf<LocalMedia>()


    fun setData(data:List<LocalMedia>,isAdd: Boolean){
        if (!isAdd) dataList.clear()

        dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_view_item, parent, false)

        return PictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val data = dataList[position]

        if (PictureMimeType.isHasVideo(data.mimeType)){
            holder.duration.visibility = View.VISIBLE
            holder.duration.text = DateUtils.formatDurationTime(data.duration)
        }else{
            holder.duration.visibility = View.GONE
        }

        if (data.isChecked){
            holder.checkImage.setImageResource(R.drawable.ic_media_selected)
            holder.checkCount.text = data.num.toString()

        }else{
            holder.checkImage.setImageResource(R.drawable.ic_media_unselected)
            holder.checkCount.text = ""
        }

        GlideApp.with(holder.itemView.context)
            .load(data.path)
            .centerCrop()
            .into(holder.mediaImage)

        holder.checkImage.setOnClickListener {
            onPictureClickListener?.onCheck(data)
        }

        holder.itemView.setOnClickListener {
            onPictureClickListener?.onPreview(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }



    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkImage:ImageView = itemView.findViewById(R.id.check_image)
        val checkCount:TextView = itemView.findViewById(R.id.check_count)
        val mediaImage:ImageView = itemView.findViewById(R.id.media_image)

        val duration:TextView =itemView.findViewById(R.id.duration)
    }


    private var onPictureClickListener:OnPictureClickListener? =null

    interface OnPictureClickListener{

        fun onCheck(data:LocalMedia)

        fun onPreview(data:LocalMedia)
    }

    fun setOnPictureClickListener(listener:OnPictureClickListener){
        this.onPictureClickListener = listener
    }

}