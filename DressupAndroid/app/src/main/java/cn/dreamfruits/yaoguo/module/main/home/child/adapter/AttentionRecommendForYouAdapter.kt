package cn.dreamfruits.yaoguo.module.main.home.child.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader.loadImage2
import cn.dreamfruits.yaoguo.module.main.home.dialog.ShowNoFollowPop
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


/**
 * @Author qiwangi
 * @Date 2023/3/19
 * @TIME 17:10
 */
open class AttentionRecommendForYouAdapter(
    mContent: Context,
    dataList: MutableList<HomeRecommendUserListBean.Item>
) : RecyclerView.Adapter<AttentionRecommendForYouAdapter.MyViewHolder>() {
    private var mContent:Context?= mContent
    private var dataList: MutableList<HomeRecommendUserListBean.Item>?= dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttentionRecommendForYouAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(cn.dreamfruits.yaoguo.R.layout.home_item_attention_wntj, parent, false)
        return MyViewHolder(view)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AttentionRecommendForYouAdapter.MyViewHolder, position: Int) {
        /**
         * 忽略感兴趣的人
         */
        holder.attentionRecommendForYouDel.setOnClickListener {
            mForYouInterface!!.onclick(dataList!![position].userId,dataList!![position].type,0)

            dataList!!.removeAt(position)//集合移除
            if(dataList!!.size==0){
                mForYouInterface!!.onclick(900,1,-1)//数据无效 仅做通知关闭列表
            }else{
                notifyItemRemoved(position)//适配器移除
                notifyItemRangeChanged(0,dataList!!.size)//适配器更新
            }
        }

        holder.attentionRecommendForYouUserName.text= dataList!![position].nickName
        holder.attentionRecommendForYouIntroduction.text= dataList!![position].reason

        loadImage2(
            dataList?.get(position)!!.avatarUrl,
            holder.attentionRecommendForYouIcon
        )

        Glide.with(mContent!!)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList?.get(position)!!.avatarUrl)
            .into(holder.attentionRecommendForYouIcon)


        /**
         * 关注为你推荐的人逻辑
         */
        if (dataList!![position].relation==1){
            holder.attentionRecommendForYouIsAttention.setImageResource(cn.dreamfruits.yaoguo.R.drawable.home_attention_ed)
        }else{
            holder.attentionRecommendForYouIsAttention.setImageResource(cn.dreamfruits.yaoguo.R.drawable.home_attention)
        }

        holder.attentionRecommendForYouIsAttention.setOnClickListener {
            if (dataList?.get(position)!!.relation==1){

                // /**加入确认弹窗*/
                                    val pop: ShowNoFollowPop =ShowNoFollowPop()
                                    pop.showPop(mContent as Activity)
                                    pop.setShowNoFollowPopCallBack(object :ShowNoFollowPop.InnerInterface{
                                        override fun onclick(state: Int) {
                                            when(state){
                                                0->{}
                                                1->{//操作在这里

                                                    dataList?.get(position)!!.relation=0
                                                    holder.attentionRecommendForYouIsAttention.setImageResource(cn.dreamfruits.yaoguo.R.drawable.home_attention_ed)
                                                    mForYouInterface!!.onclick(dataList!![position].userId,dataList!![position].type,1)
                                                    /**刷新当前item*/
                                                    notifyItemChanged(position)
                                                }
                                            }
                                        }
                                    })

            }else{
                dataList?.get(position)!!.relation=1
                holder.attentionRecommendForYouIsAttention.setImageResource(cn.dreamfruits.yaoguo.R.drawable.home_attention)
                mForYouInterface!!.onclick(dataList!![position].userId,dataList!![position].type,2)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = dataList!!.size



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var attentionRecommendForYouDel: ImageView = itemView.findViewById(cn.dreamfruits.yaoguo.R.id.attention_recommend_for_you_del)
        var attentionRecommendForYouUserName: TextView = itemView.findViewById(cn.dreamfruits.yaoguo.R.id.attention_recommend_for_you_username)
        var attentionRecommendForYouIntroduction: TextView = itemView.findViewById(cn.dreamfruits.yaoguo.R.id.attention_recommend_for_you_introduction)
        var attentionRecommendForYouIsAttention: ImageView = itemView.findViewById(cn.dreamfruits.yaoguo.R.id.attention_recommend_for_you_is_attention)
        var attentionRecommendForYouIcon: ImageView = itemView.findViewById(cn.dreamfruits.yaoguo.R.id.attention_recommend_for_you_icon)
    }


    /**
     * type 0  表示删除
     * type 1  表示关注
     * type 2  表示取消关注
     */
    interface MForYouInterface { fun onclick(id: Long, type: Int,flag: Int) }
    private var mForYouInterface: MForYouInterface? = null
    open fun setOnItemClickListener(mForYouInterface: MForYouInterface?) {
        this.mForYouInterface = mForYouInterface
    }

}