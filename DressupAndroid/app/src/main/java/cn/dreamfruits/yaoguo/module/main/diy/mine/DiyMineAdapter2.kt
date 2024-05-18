package cn.dreamfruits.yaoguo.module.main.diy.mine

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
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Tool
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
// TODO: 临时处理 
class DiyMineAdapter2 (private var mContent: Context,
                       private var dataList:MutableList<GetStyleVersionListByTypeBean.Item>) :
    RecyclerView.Adapter<DiyMineAdapter2.MyViewHolder>() {

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


//        when(dataList[position].state){
//            1->{//审核中
//                holder.imgUnderReview.visibility=View.VISIBLE
//            }
//            2,3->{//生成中
//                holder.imgGeneration.visibility=View.VISIBLE
//            }
//            4->{
//                holder.imgGeneration.visibility=View.GONE
//                if (dataList[position].isOpen==0){//私密
//                    holder.llPrivacy.visibility=View.VISIBLE
//                }else{
//                    //公开
//                }
//            }
//        }

//    (1) 先判断status字段：1:审核中 2、3:生成中 4:制作完成，当status为4时，判断isOpen字段：0-私密 1-公开跳转规则：
//    (1) 当status=4，点击才跳转单品详情页

        holder.diyTv.text= dataList[position].name

        //回调
        holder.itemView.setOnClickListener {//用构造传值判断是diy  还是逛逛
                mInterface?.onclick(dataList[position].id)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var diyImg: ImageView =itemView.findViewById(R.id.diy_right_img)
        var diyTv: TextView =itemView.findViewById(R.id.diy_right_tv)
        var imgGeneration: ImageView =itemView.findViewById(R.id.img_generation)
        var imgUnderReview: ImageView =itemView.findViewById(R.id.img_under_review)
        var llPrivacy: View =itemView.findViewById(R.id.ll_privacy)
    }

    override fun getItemCount(): Int {
        return dataList.size
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
    }

    private var mInterface: InnerInterface? = null

    fun setDiyMineAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }
}