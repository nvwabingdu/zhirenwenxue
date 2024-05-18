package cn.dreamfruits.yaoguo.module.publish.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.yaoguo.R
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

/**
 * 已选择的图片 或视频 adapter
 */
class MediaListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList = arrayListOf<LocalMedia>()

    private var selectMax = 9

    /**
     * 是否修改视频封面图
     */
    private var isVideoCover = false


    //设置最大选择数量 不显示add选项
    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    companion object {

        /**
         * 图片
         */
        const val ADAPTER_TYPE_IMAGE = 1

        /**
         * 视频
         */
        const val ADAPTER_TYPE_VIDEO = 2

        /**
         * 添加
         */
        const val ADAPTER_TYPE_ADD = 3

    }

    /**
     * 删除
     */
    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && dataList.size > position) {
                dataList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, dataList.size)
            }
        } catch (e: Exception) {

        }

    }

    /**
     * 修改视频封面
     */
    fun isVideoCover(isVideoCover: Boolean) {
        this.isVideoCover = isVideoCover
    }

    /**
     * 是否视频封面
     */
    fun getIsVideoCover() : Boolean{
        return this.isVideoCover
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ADAPTER_TYPE_VIDEO -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_card_video, parent, false)
                VideoViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_card_image, parent, false)
                ImageViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ADAPTER_TYPE_ADD) {
            if (holder is ImageViewHolder) {
                holder.imageIv.setImageResource(R.drawable.add_image)
                holder.imageIv.setOnClickListener {
                    mItemClickListener?.onAddClick()
                }
                holder.coverTv.visibility = View.GONE

                holder.imageIv.setOnClickListener {
                    mItemClickListener?.onAddClick()
                }
            }

        } else {
            val data = dataList[position]

            if (getItemViewType(position) == ADAPTER_TYPE_IMAGE) {
                if (holder is ImageViewHolder) {
                    if (position == 0) {
                        holder.coverTv.visibility = View.VISIBLE
                    } else {
                        holder.coverTv.visibility = View.GONE
                    }
                    GlideApp.with(holder.imageIv.context)
                        .load(data.path)
                        .centerCrop()
                        .placeholder(R.color.gray_F6F6F6)
                        .into(holder.imageIv)

                    holder.imageIv.setOnClickListener {
                        mItemClickListener?.onItemClick(position)
                    }
                }
            } else if (getItemViewType(position) == ADAPTER_TYPE_VIDEO) {
                if (holder is VideoViewHolder) {

                    GlideApp.with(holder.imageIv.context)
                        .load(data.path)
                        .centerCrop()
                        .placeholder(R.color.gray_F6F6F6)
                        .into(holder.imageIv)

                    holder.imageIv.setOnClickListener {
                        mItemClickListener?.onItemClick(position)
                    }

                    holder.previewVideo.setOnClickListener {
                        mItemClickListener?.onVideoPreview(position)
                    }
                }
            }
        }
    }

    fun getData(): ArrayList<LocalMedia> = dataList


    override fun getItemCount(): Int {
        return if (dataList.size < selectMax) {
            dataList.size + 1
        } else {
            return dataList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == dataList.size) {
            return ADAPTER_TYPE_ADD
        } else {
            val data = dataList[position]
            if (isVideoCover || PictureMimeType.isHasVideo(data.mimeType)) {
                return ADAPTER_TYPE_VIDEO
            }
            return ADAPTER_TYPE_IMAGE
        }
    }


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIv: ImageView = itemView.findViewById(R.id.iv_image)
        val coverTv: TextView = itemView.findViewById(R.id.tv_cover)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIv: ImageView = itemView.findViewById(R.id.iv_image)
        val changeCover: View = itemView.findViewById(R.id.change_cover)
        val previewVideo: View = itemView.findViewById(R.id.preview_video)
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)

        fun onVideoPreview(position: Int)

        fun onAddClick()
    }

    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) {
        this.mItemClickListener = l
    }

}