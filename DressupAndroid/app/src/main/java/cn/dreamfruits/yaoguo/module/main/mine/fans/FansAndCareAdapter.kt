package cn.dreamfruits.yaoguo.module.main.mine.fans

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
import cn.dreamfruits.yaoguo.util.Singleton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:23
 */
class FansAndCareAdapter (var mContent: Context, private var dataList:MutableList<SearchUserBean.Item>) : RecyclerView.Adapter<FansAndCareAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_fans_user, parent, false)
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

        holder.userFans.text="粉丝数： ${dataList[position].followerCount}"

        holder.userDetails.text=dataList[position].ygId

        holder.userContent.text=dataList[position].descript

        holder.itemView.setOnClickListener {
            Singleton.startOtherUserCenterActivity(mContent,dataList[position].userId)
        }

        //关注用户逻辑
        setCareUserLogic(dataList[position].relation,holder.userCare,position)
    }


    private fun setCareUserLogic(relation:Int,careUserTv:TextView,position: Int){
        //关注逻辑
        when(relation){//关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
            0->{
                careUserTv.text="关注"
                careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
            }
            1->{
                careUserTv.text="已关注"
                careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
            }
            2->{
                careUserTv.text="回粉"//等价于关注
                careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
            }
            3->{
                careUserTv.text="互相关注"//等价于已关注
                careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
            }
        }
        careUserTv.setOnClickListener {

            when(relation){//关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
                0->{//未关注
                    careUserTv.text="已关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                    dataList[position].relation=1
                    mInterface!!.onCareUser(dataList[position].userId,true)
                    //fans人数加一
                    dataList[position].followerCount=dataList[position].followerCount+1
                }
                1->{//我关注他
                    careUserTv.text="关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].relation=0
                    mInterface!!.onCareUser(dataList[position].userId,false)
                    //fans人数减一
                    dataList[position].followerCount=dataList[position].followerCount-1
                }
                2->{//他关注我
                    careUserTv.text="互相关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                    dataList[position].relation=3
                    mInterface!!.onCareUser(dataList[position].userId,true)
                    //fans人数加一
                    dataList[position].followerCount=dataList[position].followerCount+1
                }
                3->{//互相关注
                    careUserTv.text="回粉"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].relation=2
                    mInterface!!.onCareUser(dataList[position].userId,false)
                    //fans人数减一
                    dataList[position].followerCount=dataList[position].followerCount-1
                }
            }
            //刷新当前的item
            notifyItemChanged(position)
        }
    }

     class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userIcon: ImageView =itemView.findViewById(R.id.user_icon)
        var userName: TextView =itemView.findViewById(R.id.user_name)
        var userFans: TextView =itemView.findViewById(R.id.user_fans)
        var userDetails: TextView =itemView.findViewById(R.id.user_details)
        var userCare: TextView =itemView.findViewById(R.id.user_care)
        var userContent: TextView =itemView.findViewById(R.id.user_content)
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
            fun onCareUser(id: Long, isCare: Boolean)
        }
        private var mInterface: InnerInterface? = null

        fun setFansAndCareAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}