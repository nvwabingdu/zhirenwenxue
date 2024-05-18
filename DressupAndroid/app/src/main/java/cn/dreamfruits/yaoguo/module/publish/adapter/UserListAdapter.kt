package cn.dreamfruits.yaoguo.module.publish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.baselib.network.imageloader.ImageConfigImpl
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Tool

/**
 * @用户列表
 */
class UserListAdapter(
    val onUserClick: ((SearchUserBean.Item) -> Unit)? =null
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>(){

    private var dataList = mutableListOf<SearchUserBean.Item>()


    fun setData(data:List<SearchUserBean.Item>){
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user,parent,false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = dataList[position]

        val url = try {
            Tool().decodePicUrls(data.avatarUrl,"0",true)
        }catch (e:Exception){
            "https://devfile.dreamfruits.cn/app/243242437692896/17c4e8e7a14e1368409c7cf8d97da40f.png"
        }

        val imageConfig = ImageConfigImpl.Builder()
            .imageView(holder.avatar)
            .url(url)
            .circleCrop()
            .build()
        ImageLoader.loadImage(imageConfig)

        holder.nickname.text = data.nickName

        holder.itemView.setOnClickListener {
            onUserClick?.invoke(data)
        }

    }

    override fun getItemCount(): Int {
       return dataList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val nickname : TextView = itemView.findViewById(R.id.tv_nick_name)
    }



}