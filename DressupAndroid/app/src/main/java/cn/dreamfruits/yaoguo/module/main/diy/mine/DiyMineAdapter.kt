package cn.dreamfruits.yaoguo.module.main.diy.mine

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Tool
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class DiyMineAdapter (private var mContent: Context,
                       var dataList:MutableList<GetStyleVersionListByTypeBean.Item>) :
    RecyclerView.Adapter<DiyMineAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diy_mine, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // TODO: 2021/3/6  这里的图片地址需要处理
        try {
            Glide.with(mContent)
                .asBitmap()
                .dontAnimate()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.temp_icon)
//            .load(dataList[position].coverUrl)
                .load(Tool().decodePicUrls(dataList[position].coverUrl,"0",true) )
                .into(holder.diyImg)
        }catch (e:Exception){
            Log.e("diy页面我的====",e.toString())
        }

        when(dataList[position].status){
            //      1:审核中
            //      2:审核通过、
            //      3:审核失败
            //      4:制作中
            //      5:制作完成
            //      6:制作失败，
            1->{//审核中
                holder.imgUnderReview.visibility=View.VISIBLE
            }
            2,4->{//生成中
                holder.imgGeneration.visibility=View.VISIBLE
            }
            3->{
                holder.llCheck.visibility=View.VISIBLE
            }
            5->{
                when(dataList[position].isOpen){
                    0->{
                        holder.llPrivacy.visibility=View.VISIBLE//私密
                    }
                    else->{//公开

                    }
                }
            }
            6->{
                holder.llCreateFail.visibility=View.VISIBLE
            }
        }

//    (1) 先判断status字段：1:审核中 2、3:生成中 4:制作完成，当status为4时，判断isOpen字段：0-私密 1-公开跳转规则：
//    (1) 当status=4，点击才跳转单品详情页

        holder.diyTv.text= dataList[position].name

        //回调
        //      (1) 先判断status字段：
        //      1:审核中
        //      2:审核通过、
        //      3:审核失败
        //      4:制作中
        //      5:制作完成
        //      6:制作失败，
        //      当status为5时，判断isOpen字段：0-私密 1-公开跳转规则：
        //      (1) 当status=5，点击才跳转单品详情页
        //      (2) 当status=3时，点击弹窗 删除
        //      (3) 当status=6时，点击弹窗 重新制作|删除
        //      (4) 当status=1、2、4时，点击无响应

        holder.itemView.setOnClickListener {//用构造传值判断是diy  还是逛逛
            Log.e("diy页面我的====","点击了status:"+dataList[position].status.toString())

            when(dataList[position].status){
                //      1:审核中
                //      2:审核通过、
                //      3:审核失败
                //      4:制作中
                //      5:制作完成
                //      6:制作失败，
                1,2,4->{//审核中
                    //不做响应
                }
                3->{
                   //点击弹窗删除
                    mInterface?.delete(dataList[position].id,position)
                }
                5->{
                    mInterface?.onclick(dataList[position].id)
                }
                6->{
                    //点击弹窗 重新制作|删除
                    mInterface?.remake(dataList[position].id,position)
                }
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var diyImg: ImageView =itemView.findViewById(R.id.diy_right_img)
        var diyTv: TextView =itemView.findViewById(R.id.diy_right_tv)
        var imgGeneration: ImageView =itemView.findViewById(R.id.img_generation)
        var imgUnderReview: ImageView =itemView.findViewById(R.id.img_under_review)


        var llPrivacy: LinearLayout = itemView.findViewById(R.id.ll_privacy)
        var llCheck: LinearLayout = itemView.findViewById(R.id.ll_check)
        var llCreateFail: LinearLayout = itemView.findViewById(R.id.ll_create_fail)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 删除item
     */
    fun delItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size - position)
    }


    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetStyleVersionListByTypeBean.Item>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


    //回调
    interface InnerInterface {
        fun onclick(id: Long)

        fun delete(id: Long,position: Int)

        fun remake(id: Long,position: Int)
    }

    private var mInterface: InnerInterface? = null

    fun setDiyMineAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }
}