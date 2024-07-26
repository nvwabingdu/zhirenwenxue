package com.example.zrwenxue.moudel.main.center.zr.dapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.main.word.MyStatic

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class WaterfallAdapter(private var mContent: Context,
                            private var space: Float,
                            private var dataList: MutableList<WaterfallBean>
                            ) : RecyclerView.Adapter<WaterfallAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_zrb_doodle, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {




        /**预加载回调*/
        if (dataList.size > 6 && position == dataList.size - 6) {
            mWaterfallAdapterInterface!!.onPreload()
        }
//
//        Log.e("21212121",dataList[position].toString())
//        if (dataList[position].type==1){
//            Log.e("21212121",dataList[position].picUrls[0].url)
//            Log.e("21212121====",dataList[position].videoUrls[0].url)
//        }
//
        //视频图片逻辑
        val displayMetrics = mContent.resources.displayMetrics
        val w2 = (displayMetrics.widthPixels - space) / 2 //需要占的宽度
        val w = dataList[position].bitmap.width
        val h = dataList[position].bitmap.height
        var h2:Float = 0.0f
        try {
            when (Single.getImageType(w, h)) {
                1 -> {//比例小于3：4   就为3：4
                    h2 = w2 / (3.0f / 4.0f)
                }
                2 -> {//比例大于3：4  小于4：3  就为1：1
                    h2 = w2
                }
                3 -> {//比例大于4：3  就为4：3
                    h2 = w2 / (4.0f / 3.0f)
                }
                4 -> {//图片宽高比异常
                    h2 = w2
                }
            }
            val params: ViewGroup.LayoutParams = holder.attentionImg.layoutParams
            params.height = h2.toInt()
            holder.attentionImg.layoutParams = params
        } catch (e: Exception) {
            val params: ViewGroup.LayoutParams = holder.attentionImg.layoutParams
            params.height = w2.toInt()
            holder.attentionImg.layoutParams = params
        }

        // 设置 Bitmap 到 ImageView
        holder.attentionImg.setImageBitmap( dataList[position].bitmap)


//        ImageLoader.loadImage2(dataList[position].picUrls[0].url, holder.attentionImg)
//
//                Glide.with(mContent)
//                    .asBitmap()
//                    .dontAnimate()
//                    .skipMemoryCache(false)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .transform(GlideRoundTransform(5))
//                    .error(ColorDrawable(Color.WHITE))
//                    .load(dataList[position].picUrls[0].url)
//                    .into(holder.attentionImg)
//        //是否是视频对应显示视频的tag
//        when (dataList[position].type) {
//            0 -> {//图片
//                holder.recommendVideoPlayImg.visibility = View.GONE//视频图标隐藏
//            }
//            1->{//视频
//                holder.recommendVideoPlayImg.visibility = View.VISIBLE//视频图标显示
//            }
//            2->{//广告
//
//            }
//        }
//
//        /**
//         * 点击图片跳转到详情页面
//         */
//        holder.attentionImg.setOnClickListener {
//            Log.e("dadsdsda","1111"+dataList[position].type)
//            if (dataList[position].type==1) {
//                val intent = Intent(mContent, ListVideoActivity::class.java)//视频列表页面
//                intent.putExtra("feedId", dataList[position].id)
//                intent.putExtra("position", position)//位置
//                mWaterfallAdapterInterface!!.onVideo()
//                startActivity(intent)
//            } else {
//                Log.e("dadsdsda","3333")
//                val intent = Intent(mContent, PostDetailsActivity::class.java)//帖子详情页
//                intent.putExtra("feedId", dataList[position].id)
//                intent.putExtra("coverHeight", dataList[position].picUrls[0].height)
//                intent.putExtra("coverWidth", dataList[position].picUrls[0].width)
//                startActivity(intent)
//            }
//        }
        /**
         * 长按弹出删除按钮
         */
        holder.attentionImg.setOnLongClickListener {
            mWaterfallAdapterInterface!!.onCallBack(position,MyStatic.getBase64String(dataList[position].bitmap))//回调
            true
        }
//
//        /**头像*/
//        Glide.with(mContent)
//            .asBitmap()
//            .dontAnimate()
//            .skipMemoryCache(false)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .circleCrop()
//            .error(R.drawable.temp_icon)
//            .load(dataList[position].userInfo.avatarUrl)
//            .into(holder.attentionUserIcon)
//
//        /**进入个人中心*/
//        holder.attentionUserIcon.setOnClickListener {
//            Singleton.startOtherUserCenterActivity(mContent, dataList[position].userInfo.id)
//        }
//
//        /**进入个人中心*/
//        holder.attentionUsername.setOnClickListener {
//            Singleton.startOtherUserCenterActivity(mContent, dataList[position].userInfo.id)
//        }
//
//        /**
//         * 你的关注显示与否
//         */
//        if (dataList[position].relation==1){
//            holder.attentionYours.visibility=View.VISIBLE
//        }else{
//            holder.attentionYours.visibility=View.GONE
//        }

        holder.tvDoodleName.text=dataList[position].doodleName
        holder.attentionLaudCount.text=dataList[position].value.toString()


        if (dataList[position].doodleDescription != ""){
            holder.attentionContent.visibility=View.VISIBLE
            holder.attentionContent.text = dataList[position].doodleDescription
        }else{
            holder.attentionContent.visibility=View.GONE
        }
//
        if (dataList[position].owner == ""){
            holder.attentionUsername.visibility=View.GONE
        }else{
            holder.attentionUsername.visibility=View.VISIBLE
            holder.attentionUsername.text = dataList[position].owner
        }
//        /**
//         * 点赞逻辑
//         */
//        Singleton.setNumRuler(dataList[position].laudCount,
//            holder.attentionLaudCount,
//            Singleton.DEFAULT_LAUD
//        )
//
//        if (dataList[position].isLaud==1){
//            holder.attentionLaudImg.setImageResource(R.drawable.home_thumb_ed)
//        }else{
//            holder.attentionLaudImg.setImageResource(R.drawable.home_thumb)
//        }
//
//        holder.laudLayout.setOnClickListener {
//            if (dataList[position].isLaud==1){
//                Log.e("daddada","取消点赞了")
//                holder.attentionLaudImg.setImageResource(R.drawable.home_thumb)
//                dataList[position].isLaud=0
//                dataList[position].laudCount=dataList[position].laudCount-1
//                mWaterfallAdapterInterface!!.onLaud(false,dataList[position].id)//传递此条动态的ID
//            }else{
//                Log.e("daddada","点赞了")
//                holder.attentionLaudImg.setImageResource(R.drawable.home_thumb_ed)
//                dataList[position].isLaud=1
//                dataList[position].laudCount=dataList[position].laudCount+1
//                mWaterfallAdapterInterface!!.onLaud(true,dataList[position].id)//传递此条动态的ID
//            }
//
//            //相当于刷新
//            Singleton.setNumRuler(dataList[position].laudCount,
//                holder.attentionLaudCount,
//                Singleton.DEFAULT_LAUD
//            )
//        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var attentionImg: ImageView = itemView.findViewById(R.id.attention_img)//图片
        var tvDoodleName: TextView = itemView.findViewById(R.id.tv_doodle_name)//涂鸦名字
        var attentionContent: TextView = itemView.findViewById(R.id.recommend_content)//原来这就是30岁的温柔和气质……
//        var attentionUserIcon: ImageView = itemView.findViewById(R.id.recommend_usericon)//头像
        var attentionUsername: TextView = itemView.findViewById(R.id.recommend_username)//陈阿姨smile
//        var attentionLaudImg: ImageView = itemView.findViewById(R.id.recommend_laud)//点赞
//        var recommendVideoPlayImg: ImageView = itemView.findViewById(R.id.recommend_video_play_img)//视频图标
        var attentionLaudCount: TextView = itemView.findViewById(R.id.recommend_laud_count)//价值
//        var laudLayout: View = itemView.findViewById(R.id.laud_layout)//点赞数量
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


    interface WaterfallAdapterInterface {

        fun onCallBack(position: Int, txt: String)

        fun onPreload()
    }
    private var mWaterfallAdapterInterface: WaterfallAdapterInterface? = null

    open fun setWaterfallAdapterListener(mWaterfallAdapterInterface: WaterfallAdapterInterface?) {
        this.mWaterfallAdapterInterface = mWaterfallAdapterInterface
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<WaterfallBean>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }
}
