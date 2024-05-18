package cn.dreamfruits.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import com.luck.picture.lib.entity.LocalMediaFolder

/**
 * 相册列表适配器
 */
class FolderListAdapter:RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>(){

    val dataList = mutableListOf<LocalMediaFolder>()

    private var selectPosition = 0
    private var prePosition = -1


    fun setData(data:List<LocalMediaFolder>){
        dataList.addAll(data)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_item,parent,false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val data = dataList[position]

        GlideApp.with(holder.itemView.context)
            .load(data.firstImagePath)
            .centerCrop()
            .into(holder.firstImage)

        holder.folderName.text = data.folderName
        holder.folderNum.text = String.format("%s张",data.folderTotalNum)

        if (selectPosition == position)
            holder.checkFolder.visibility = View.VISIBLE
        else
            holder.checkFolder.visibility = View.GONE


        holder.itemView.setOnClickListener {

            onFolderClickListener?.onItemClick(position,data)

            holder.checkFolder.visibility = View.VISIBLE
            prePosition = selectPosition
            selectPosition = holder.layoutPosition

            notifyItemChanged(prePosition)
        }

    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    class FolderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val firstImage:ImageView = itemView.findViewById(R.id.first_image)
        val folderName:TextView = itemView.findViewById(R.id.folder_name)
        val folderNum:TextView = itemView.findViewById(R.id.folder_num)
        val checkFolder:ImageView = itemView.findViewById(R.id.check_folder)
    }


    private var onFolderClickListener: OnFolderClickListener? =null

    fun setOnIBridgeAlbumWidget(listener:OnFolderClickListener?){
        this.onFolderClickListener = listener
    }

}