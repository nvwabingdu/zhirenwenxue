package cn.dreamfruits.yaoguo.module.main.home.commentreplay

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
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/4/17
 * @TIME 14:07
 */
class AtUserAdapter (var mContent: Context, private var dataList: MutableList<SearchUserBean.Item>) :
    RecyclerView.Adapter<AtUserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AtUserAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_comment_edittext_user, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AtUserAdapter.MyViewHolder, position: Int) {
        Log.e("AtUserAdapter", "onBindViewHolder$position")
        if(position==(dataList.size-2)){//划到倒数第二个 开始加载更多

            //拼ids
            val ids=StringBuffer("")
            for (i in 0 until dataList.size){
                ids.append(dataList[position].id.toString()+",")
            }
            var str=ids.toString()
            str=str.substring(0,str.length-1)//ids 排除的id，例如：11111111,22222222
            Log.e("AtUserAdapter", "排除的用户ids=$str")

            //加载更多
            mAtUserAdapterInterface!!.onLoadMore(str)
        }
        /**
         * 点击事件
         */
        holder.atUserLayout.setOnClickListener {
            if (dataList[position].isSelected){
                //回调  取消@此人
                mAtUserAdapterInterface!!.onclick(dataList[position].id,dataList[position].nickName.length,dataList[position].nickName,true)
                dataList[position].isSelected=false
                holder.atUserSelectLayout.visibility=View.GONE
            }else{
                //回调  @此人
                mAtUserAdapterInterface!!.onclick(dataList[position].id,dataList[position].nickName.length,dataList[position].nickName,false)
                dataList[position].isSelected=true
                holder.atUserSelectLayout.visibility=View.VISIBLE
            }
        }

        //设置状态
        if (dataList[position].isSelected){
            holder.atUserSelectLayout.visibility=View.VISIBLE
        }else{
            holder.atUserSelectLayout.visibility=View.GONE
        }

        /**
         * 名称
         */
        holder.atUserName.text=dataList[position].nickName

        /**
         * 头像
         */
        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList[position].avatarUrl)
            .into(holder.atUserIcon)

    }
    override fun getItemCount(): Int = dataList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var atUserLayout: View = itemView.findViewById(R.id.at_user_layout)
        var atUserIcon: ImageView = itemView.findViewById(R.id.at_user_icon)
        var atUserName: TextView = itemView.findViewById(R.id.at_user_name)
        var atUserSelectLayout: View = itemView.findViewById(R.id.at_user_select_layout)
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<SearchUserBean.Item>, isClear: Boolean) {
        if (isClear) {
            if (tempList==null||tempList.size==0){
                mAtUserAdapterInterface!!.showDefaultView(true)//传过来的数据为空 显示默认视图
            }else{
                mAtUserAdapterInterface!!.showDefaultView(false)//传过来的数据为空 显示默认视图
                this.dataList.clear()
                this.dataList = mAtUserAdapterInterface!!.setSelectAtUser(tempList)//设置选中状态
                notifyDataSetChanged()
            }

        } else {
            this.dataList += mAtUserAdapterInterface!!.setSelectAtUser(tempList)//设置选中状态
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }

    /**
     * 点击事件回调
     */
    interface AtUserAdapterInterface {

        fun onclick(id: Long,len:Int,userName:String,isCancel: Boolean)
        fun onLoadMore(atStr:String?)

        fun showDefaultView(isShow:Boolean)
        fun setSelectAtUser(tempList: MutableList<SearchUserBean.Item>):MutableList<SearchUserBean.Item>
    }
    private var mAtUserAdapterInterface: AtUserAdapterInterface? = null
    open fun setAtUserAdapterListener(mAtUserAdapterInterface: AtUserAdapterInterface?) {
        this.mAtUserAdapterInterface = mAtUserAdapterInterface
    }

}