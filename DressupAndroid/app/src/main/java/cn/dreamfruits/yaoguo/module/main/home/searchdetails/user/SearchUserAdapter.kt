package cn.dreamfruits.yaoguo.module.main.home.searchdetails.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.dialog.ShowNoFollowPop
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:23
 */
class SearchUserAdapter (var mContent: Context,  var dataList:MutableList<SearchUserBean.Item>) : RecyclerView.Adapter<SearchUserAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_search_user, parent, false)
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

        //关注用户逻辑
        setCareUserLogic(dataList[position].relation,holder.userCare,position)

        holder.itemView.setOnClickListener {
            Singleton.startOtherUserCenterActivity(mContent,dataList[position].id)
        }
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
                    val pop:ShowNoFollowPop=ShowNoFollowPop()
                    pop.showPop(mContent as Activity)
                    pop.setShowNoFollowPopCallBack(object :ShowNoFollowPop.InnerInterface{
                        override fun onclick(state: Int) {

                            when(state){
                                0->{}
                                1->{//操作在这里
                                    careUserTv.text="已关注"
                                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                                    dataList[position].relation=1
                                    mSearchUserAdapterInterface!!.onCareUser(dataList[position].id,true)
                                    //fans人数加一
                                    dataList[position].followerCount=dataList[position].followerCount+1
                                    /**刷新当前item*/
                                    notifyItemChanged(position)
                                }
                            }


                        }
                    })
                }
                1->{//我关注他
                    careUserTv.text="关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].relation=0
                    mSearchUserAdapterInterface!!.onCareUser(dataList[position].id,false)
                    //fans人数减一
                    dataList[position].followerCount=dataList[position].followerCount-1
                }
                2->{//他关注我
                    careUserTv.text="互相关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                    dataList[position].relation=3
                    mSearchUserAdapterInterface!!.onCareUser(dataList[position].id,true)
                    //fans人数加一
                    dataList[position].followerCount=dataList[position].followerCount+1
                }
                3->{//互相关注

                    /**加入确认弹窗*/
                    val pop:ShowNoFollowPop=ShowNoFollowPop()
                    pop.showPop(mContent as Activity)
                    pop.setShowNoFollowPopCallBack(object :ShowNoFollowPop.InnerInterface{
                        override fun onclick(state: Int) {
                            when(state){
                                0->{}
                                1->{//操作在这里
                                    careUserTv.text="回粉"
                                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                                    dataList[position].relation=2
                                    mSearchUserAdapterInterface!!.onCareUser(dataList[position].id,false)
                                    //fans人数减一
                                    dataList[position].followerCount=dataList[position].followerCount-1
                                    /**刷新当前item*/
                                    notifyItemChanged(position)
                                }
                            }
                        }
                    })

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


    /**
     * 回调
     */
    interface SearchUserAdapterInterface {
        fun onCareUser(id: Long, isCare: Boolean)
    }

    private var mSearchUserAdapterInterface: SearchUserAdapterInterface? = null

    open fun setSearchUserAdapterListener(mSearchUserAdapterInterface: SearchUserAdapterInterface) {
        this.mSearchUserAdapterInterface = mSearchUserAdapterInterface
    }


}