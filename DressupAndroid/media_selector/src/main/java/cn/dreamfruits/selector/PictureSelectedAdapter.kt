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
import java.util.*

class PictureSelectedAdapter(
    val onCancelListener : ((LocalMedia) -> Unit)? =null,
    val onItemMoveListener:((fromPosition:Int, toPosition:Int) ->Unit)? =null
) : RecyclerView.Adapter<PictureSelectedAdapter.PictureViewHolder>(){


    val dataList = mutableListOf<LocalMedia>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_view_item2,parent,false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val data = dataList[position]
        GlideApp.with(holder.itemView.context)
            .load(data.path)
            .centerCrop()
            .into(holder.mediaImage)

        if (PictureMimeType.isHasVideo(data.mimeType)){
            holder.decoration.visibility = View.VISIBLE
            holder.decoration.text = DateUtils.formatDurationTime(data.duration)
        }else{
            holder.decoration.visibility = View.GONE
        }

        holder.cancelImage.setOnClickListener {
           onCancelListener?.invoke(data)
        }
    }

    /**
     * 交换item两个位置 由ItemTouchHelperCallback调用
     */
    fun itemMove(fromPosition:Int, toPosition:Int){
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataList ,i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        onItemMoveListener?.invoke(fromPosition, toPosition)
    }


    override fun getItemCount(): Int {
       return dataList.size
    }


    class PictureViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val mediaImage:ImageView = itemView.findViewById(R.id.media_image)
        val cancelImage:ImageView = itemView.findViewById(R.id.cancel_image)
        val decoration:TextView = itemView.findViewById(R.id.duration)
    }

}