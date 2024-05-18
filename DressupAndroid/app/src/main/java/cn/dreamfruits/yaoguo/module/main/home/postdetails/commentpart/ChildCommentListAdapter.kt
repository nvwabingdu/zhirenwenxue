package cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView2
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.*

/**
 * @Author qiwangi
 * @Date 2023/4/10
 * @TIME 18:02
 */
class ChildCommentListAdapter (
    private var postUserId:Long,
    private var mContent: Context,
    private var dataList: MutableList<ChildCommentBean.ChildItem>) : RecyclerView.Adapter<ChildCommentListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var commentIcon: ImageView = itemView.findViewById(R.id.comment_child_icon)
        var commentItem: View = itemView.findViewById(R.id.comment_item)//用于点击回复  长按举报等
        var commentAddress: TextView = itemView.findViewById(R.id.comment_address)//暂时没有地址
        var commentAuthor: TextView = itemView.findViewById(R.id.comment_author)
        var commentContent: ExpandableTextView2 = itemView.findViewById(R.id.comment_content2)
        var commentLaudLayout: View = itemView.findViewById(R.id.comment_laud_layout)
        var commentLaudCount: TextView = itemView.findViewById(R.id.comment_laud_count)
        var commentLaudImg: ImageView = itemView.findViewById(R.id.comment_laud_img)
        var commentName: TextView = itemView.findViewById(R.id.comment_name)
        var commentTime: TextView = itemView.findViewById(R.id.comment_time)
        var commentSonLayout: View = itemView.findViewById(R.id.comment_son_layout)//用于变色item的
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_comment_item_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(dataList[position].isChangeColor){
            holder.commentSonLayout.setBackgroundColor(Color.parseColor("#F0F0F0"))
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    holder.commentSonLayout.setBackgroundColor(Color.parseColor("#ffffff"))
                    dataList[position].isChangeColor=false
                }
            }, 2000) //高亮两秒
        }


        holder.commentIcon.setOnClickListener {//头像
            // TODO: 跳转个人中心
            Toast.makeText(mContent, "头像 跳转个人中心", Toast.LENGTH_SHORT).show()
        }
        holder.commentName.setOnClickListener {//名称
            // TODO: 跳转个人中心
            Toast.makeText(mContent, "头像 跳转个人中心", Toast.LENGTH_SHORT).show()
        }

        Glide.with(mContent)//头像
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList[position].commentUserInfo!!.avatarUrl)
            .into(holder.commentIcon)

        //是否是作者 做显示隐藏
        if (dataList[position].commentUserInfo!=null&&dataList[position].commentUserInfo!!.id==postUserId){
            holder.commentAuthor.visibility=View.VISIBLE
        }else{
            holder.commentAuthor.visibility=View.GONE
        }

        //内容富文本处理
        holder.commentContent.visibility=View.VISIBLE
        try {
            if (dataList[position].atUser!=null){
                if (dataList[position].content!=null&&dataList[position].content!=""){
                    if ( dataList[position].replyUserInfo!=null&&dataList[position].replyUserInfo!!.nickName.length>0){
                        Singleton.setRichStr2(
                            holder.commentContent,
                            165f,
                            dataList[position].content,
                            mContent,//上下文
                            dataList[position].atUser!!.toMutableList(),
                            null,
                            "",
                            "",
                            1000,
                            false,
                            dataList[position].replyUserInfo!!.nickName,
                            dataList[position].replyUserInfo!!.id,//点击使用
                        )
                    }else{
                        Singleton.setRichStr2(
                            holder.commentContent,
                            165f,
                            dataList[position].content,
                            mContent,//上下文
                            dataList[position].atUser!!.toMutableList(),
                            null,
                            "",
                            "",
                            1000,
                            false,
                            "",
                            -1L,//点击使用
                        )
                    }
                }
            }else{
                if (dataList[position].content!=null&&dataList[position].content!=""){
                    holder.commentContent.text=dataList[position].content
                }
            }
        }catch (e:Exception){
            holder.commentContent.text=dataList[position].content
        }

        //点赞逻辑
        Singleton.setNumRuler(dataList[position].laudCount,
            holder.commentLaudCount,
            Singleton.DEFAULT_LAUD
        )

        if (dataList[position].isLaud==1){
            holder.commentLaudImg.setImageResource(R.drawable.home_thumb_ed)
        }else{
            holder.commentLaudImg.setImageResource(R.drawable.home_thumb)
        }

        holder.commentLaudLayout.setOnClickListener {
            if (dataList[position].isLaud==1){
                Log.e("daddada","取消点赞了")
                holder.commentLaudImg.setImageResource(R.drawable.home_thumb)
                dataList[position].isLaud=0
                dataList[position].laudCount=dataList[position].laudCount-1
                mChildCommentInterface!!.onLaudClick(false,dataList[position].id)//传递此条动态的ID
            }else{
                Log.e("daddada","点赞了")
                holder.commentLaudImg.setImageResource(R.drawable.home_thumb_ed)
                dataList[position].isLaud=1
                dataList[position].laudCount=dataList[position].laudCount+1
                mChildCommentInterface!!.onLaudClick(true,dataList[position].id)//传递此条动态的ID
            }

            //刷新
            Singleton.setNumRuler(dataList[position].laudCount?:0,
                holder.commentLaudCount,
                Singleton.DEFAULT_LAUD
            )
        }
        //名字
        holder.commentName.text=dataList[position].commentUserInfo!!.nickName

        //评论时间
        holder.commentTime.text=Singleton.timeShowRule(dataList[position].createTime,true)

        //单击子评论
        holder.commentItem.setOnClickListener {
            mChildCommentInterface!!.onItemClick(
                false,
                dataList[position].commentUserInfo!!.nickName,
                dataList[position].id,
                dataList[position].content)
        }

        //长按子评论
        holder.commentItem.setOnLongClickListener {
            mChildCommentInterface!!.onItemClick(
                true,
                dataList[position].commentUserInfo!!.nickName,
                dataList[position].id,
                dataList[position].content)
            true
        }
    }





    /**
     * 删除item
     */
    fun delItems(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 添加数据  加载最后
     */
    fun addItems (oneData: ChildCommentBean.ChildItem){
        //末尾添加
        /*mChildCommentBean.list.add(oneData)
        notifyItemInserted(mChildCommentBean.list.size - 1)*/

        //首条添加
        dataList.add(0,oneData)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, dataList.size-1 )
    }

    //更新数据
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<ChildCommentBean.ChildItem>, isClear: Boolean){
        if (isClear) {
            dataList.clear()
            dataList = tempList
            notifyDataSetChanged()
        } else {
            dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }

    //子级回调
    interface ChildCommentInterface {
        fun onLaudClick(isLaud: Boolean, id: Long)//点赞回调

        fun onItemClick(//子评论item点击 长按 等 回调
            isLongClick: Boolean,
            replyUser: String,
            replyId: Long,
            replyContent: String
        )

    }

    private var mChildCommentInterface: ChildCommentInterface? = null

    fun setChildCommentCallBack(mChildCommentInterface: ChildCommentInterface) {
        this.mChildCommentInterface = mChildCommentInterface
    }
}