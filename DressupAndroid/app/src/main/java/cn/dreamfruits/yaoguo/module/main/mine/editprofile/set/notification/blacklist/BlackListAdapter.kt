package cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.notification.blacklist

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
 * @Date 2023/3/5
 * @TIME 23:23
 */
class BlackListAdapter (var mContent: Context, private var dataList:MutableList<SearchUserBean.Item>) : RecyclerView.Adapter<BlackListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_black_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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
            .into(holder.userIcon!!)

        holder.userName.text=dataList[position].nickName

        //关注用户逻辑
        setCareUserLogic(dataList[position].relation,holder.tvBlack!!,position)
    }


    private fun setCareUserLogic(relation:Int,tv:TextView,position: Int){
        //关注逻辑
        when(relation){//关系 0:未拉黑 1:拉黑
            0->{
                tv.text="拉黑"
                tv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
            }
            1->{
                tv.text="取消拉黑"
                tv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
            }

        }
        tv.setOnClickListener {
            when(relation){//关系 0:未拉黑 1:拉黑
                0->{
                    tv.text="取消拉黑"
                    tv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                    dataList[position].relation=1
                    mInterface!!.onBlackUser(dataList[position].id,true)
                }
                1->{//我关注他
                    tv.text="拉黑"
                    tv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].relation=0
                    mInterface!!.onBlackUser(dataList[position].id,false)
                }
            }
            //刷新当前的item
            notifyItemChanged(position)
        }
    }

     class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var userIcon: ImageView= itemView.findViewById(R.id.iv_user_head_img)
      var userName: TextView =itemView.findViewById(R.id.tv_name)
      var tvBlack: TextView? =itemView.findViewById(R.id.tv_black)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<SearchUserBean.Item>, isClear: Boolean) {
        if (isClear) {
            Log.e("a1212",tempList.size.toString());
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


        interface  InnerInterface{
            fun onBlackUser(id: Long, isCare: Boolean)
        }
        private var mInterface: InnerInterface? = null

        fun setBlackListAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}