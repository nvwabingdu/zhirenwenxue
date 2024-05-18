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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author qiwangi
 * @Date 2023/3/22
 * @TIME 17:11
 */
class CommentListAdapter (
    private var postUserId:Long,
    private var mContent: Context,
    private var dataList: MutableList<CommentBean.Item>) : RecyclerView.Adapter<CommentListAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var commentIcon: ImageView = itemView.findViewById(R.id.comment_icon)
        var commentItem: View = itemView.findViewById(R.id.comment_item)//用于点击回复  长按举报等
        var commentAddress: TextView = itemView.findViewById(R.id.comment_address)//暂时没有地址
        var commentAuthor: TextView = itemView.findViewById(R.id.comment_author)
        var commentContent: ExpandableTextView = itemView.findViewById(R.id.comment_content1)
        var commentLaudLayout: View = itemView.findViewById(R.id.comment_laud_layout)
        var commentLaudCount: TextView = itemView.findViewById(R.id.comment_laud_count)
        var commentLaudImg: ImageView = itemView.findViewById(R.id.comment_laud_img)
        var commentName: TextView = itemView.findViewById(R.id.comment_name)
        var commentTime: TextView = itemView.findViewById(R.id.comment_time)
        var commentChildRecyclerview: MaxRecyclerView = itemView.findViewById(R.id.comment_child_recyclerview)
        var commentMoreLayoutOne: View = itemView.findViewById(R.id.comment_more_layout_one)//展开   收起  按钮显示与否
        var commentMoreLayoutTwo: View = itemView.findViewById(R.id.comment_more_layout_two)//加载圈
        var commentOpen: TextView = itemView.findViewById(R.id.comment_open)//展开文本
//        var commentCloseLayout: View = itemView.findViewById(R.id.comment_close_layout)//关闭
        var commentParentLayout: View = itemView.findViewById(R.id.comment_parent_layout)//用于变色item的
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_comment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        if(dataList[position].isChangeColor){
            holder.commentParentLayout.setBackgroundColor(Color.parseColor("#F0F0F0"))
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    holder.commentParentLayout.setBackgroundColor(Color.parseColor("#ffffff"))
                    dataList[position].isChangeColor=false
                }
            }, 2000) //高亮两秒
        }

        holder.commentItem.setOnClickListener {//单个评论点击事件
            mCommentInterface!!.onItemClick(
                false,
                dataList[position].id,
                false,
                dataList[position].commentUserInfo!!.nickName,
                0,
                dataList[position].content,
                dataList[position].mChildCommentListAdapter,
                position
            )
        }

        holder.commentItem.setOnLongClickListener {//单个评论长按事件
            mCommentInterface!!.onItemClick(
                true,
                dataList[position].id,
                false,
                dataList[position].commentUserInfo!!.nickName,
                0,
                dataList[position].content,
                dataList[position].mChildCommentListAdapter,
                position
            )
            true
        }

        holder.commentIcon.setOnClickListener {//头像
            /**
             * 跳转到个人中心
             */
            Singleton.startOtherUserCenterActivity(
                mContent,
                dataList[position]!!.commentUserInfo!!.id ?: 0L
            )
        }

        holder.commentName.setOnClickListener { //名称
            /**
             * 跳转到个人中心
             */
            Singleton.startOtherUserCenterActivity(
                mContent,
                dataList[position]!!.commentUserInfo!!.id ?: 0L
            )
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

        //地址
        /*holder.commentAddress.text=*/

        //是否是作者 做显示隐藏
       if (dataList[position].commentUserInfo!=null&&dataList[position].commentUserInfo!!.id==postUserId){
            holder.commentAuthor.visibility=View.VISIBLE
        }else{
            holder.commentAuthor.visibility=View.GONE
        }


        holder.commentContent.visibility=View.VISIBLE
        if (dataList[position].atUser!=null){//富文本
            if (dataList[position].content!=null&&dataList[position].content!=""){
                Singleton.setRichStr(
                    holder.commentContent,
                    165f,
                    dataList[position].content,
                    mContent,//上下文
                    dataList[position].atUser!!.toMutableList(),
                    null,
                    "",
                    "",
                    1000,
                    false
                )
            }
        }else{
            if (dataList[position].content!=null&&dataList[position].content!=""){
                holder.commentContent.text=dataList[position].content
            }
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
                Log.e("zqr","取消点赞了")
                holder.commentLaudImg.setImageResource(R.drawable.home_thumb)
                dataList[position].isLaud=0
                dataList[position].laudCount=dataList[position].laudCount-1
                mCommentInterface!!.onLaudClick(false,dataList[position].id)//传递此条动态的ID
            }else{
                Log.e("zqr","点赞了")
                holder.commentLaudImg.setImageResource(R.drawable.home_thumb_ed)
                dataList[position].isLaud=1
                dataList[position].laudCount=dataList[position].laudCount+1
                mCommentInterface!!.onLaudClick(true,dataList[position].id)//传递此条动态的ID
            }

            //刷新
            Singleton.setNumRuler(dataList[position].laudCount?:0,
                holder.commentLaudCount,
                Singleton.DEFAULT_LAUD
            )
        }

        holder.commentName.text=dataList[position].commentUserInfo!!.nickName

        //评论时间
        holder.commentTime.text=Singleton.timeShowRule(dataList[position].createTime,true)


        //设置子列表
        setChild(holder,position)


        //展开 收起 加载圈 显示规则
        if (dataList[position].isChildNoMoreData){//如果没有更多数据 就不显示
            holder.commentMoreLayoutOne.visibility=View.GONE
            holder.commentMoreLayoutTwo.visibility=View.GONE
        }else{//有数据的情况下
            if (dataList[position].replys==null){
                holder.commentMoreLayoutOne.visibility=View.GONE
                holder.commentMoreLayoutTwo.visibility=View.GONE
            }else{
                if(dataList[position].replyCount<=2){//有数据的情况下  小于等于两条也不用显示
                    holder.commentMoreLayoutOne.visibility=View.GONE
                    holder.commentMoreLayoutTwo.visibility=View.GONE
                } else if (dataList[position].replyCount>dataList[position].replys!!.size){

                    holder.commentMoreLayoutOne.visibility=View.VISIBLE//1.还有更多数据的情况下 先把展开收起设置为显示
                    holder.commentMoreLayoutTwo.visibility=View.GONE//隐藏加载圈

                    holder.commentOpen.text = "——展开${dataList[position].replyCount - dataList[position].replys!!.size}条评论"

                    /**
                     * 点击展开
                     */
                    holder.commentOpen.setOnClickListener {
                        //点击后把收起设置为显示 因为展开后就可收起
//                        holder.commentCloseLayout.visibility=View.VISIBLE

                        holder.commentMoreLayoutOne.visibility=View.GONE   //点击时隐藏展开收起
                        holder.commentMoreLayoutTwo.visibility=View.VISIBLE //显示加载圈

                        //拼ids
                        val ids=StringBuffer("")
                        for (i in 0 until dataList[position].replys!!.size){
                            ids.append(dataList[position].replys!![i].id.toString()+",")
                        }
                        var str=ids.toString()
                        str=str.substring(0,str.length-1)//ids 排除的id，例如：11111111,22222222
                        Log.e("zqr", "子评论ids=$str")

                        // TODO: 此条评论的id   此子列表的lasttime ids 本条的角标
                        //子列表请求
                        mCommentInterface!!.onLoadMoreClick(str,position)
                    }

//                    /**
//                     * 点击收起
//                     */
//                    holder.commentCloseLayout.setOnClickListener {
//                        holder.commentMoreLayoutTwo.visibility=View.GONE //隐藏加载圈
//                        holder.commentCloseLayout.visibility=View.GONE//收起之后不可以再收起  所以隐藏
//                        dataList[position].replys!!.take(2)//保留前两个数据
//
//                        updateItem(position)//更新此条数据
//                    }
                }else{
                    holder.commentMoreLayoutOne.visibility=View.GONE
                    holder.commentMoreLayoutTwo.visibility=View.GONE
                }
            }
        }
    }

    //设置子列表
    private fun setChild(holder: MyViewHolder, position: Int){
        holder.commentChildRecyclerview.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        val mLayoutManager = LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false)
        holder.commentChildRecyclerview.layoutManager = mLayoutManager


        if (dataList[position].replys!=null){//有数据
            if(dataList[position].replys!!.size>0){//replay有数据
                dataList[position].mChildCommentListAdapter = ChildCommentListAdapter(postUserId,mContent, dataList[position].replys!!)
            }else{//没有数据 装一个空的集合
                val tempChildData: MutableList<ChildCommentBean.ChildItem> = ArrayList()//临时数据
                dataList[position].replys=tempChildData
                dataList[position].mChildCommentListAdapter = ChildCommentListAdapter(postUserId,mContent, dataList[position].replys!!)
            }
        }else{//等于空的情况先装一个适配器
            val tempChildData: MutableList<ChildCommentBean.ChildItem> = ArrayList()//临时数据
            dataList[position].replys=tempChildData
            dataList[position].mChildCommentListAdapter = ChildCommentListAdapter(postUserId,mContent, dataList[position].replys!!)
        }

        //设置适配器
        holder.commentChildRecyclerview.adapter = dataList[position].mChildCommentListAdapter

        //子级回调
        dataList[position].mChildCommentListAdapter!!.setChildCommentCallBack(object : ChildCommentListAdapter.ChildCommentInterface {

            override fun onLaudClick(isLaud: Boolean, id: Long) {
                mCommentInterface!!.onLaudClick(isLaud, id)//点赞 通过父级回调传递
            }

            override fun onItemClick(isLongClick: Boolean,replyUser:String,replyId:Long,replyContent:String) {
                mCommentInterface!!.onItemClick(
                    isLongClick,
                    dataList[position].id, //子级调用时 永远都是本条的id
                    true,
                    replyUser,
                    replyId, //此id为回复谁 就是点击的这条id
                    replyContent,
                    dataList[position].mChildCommentListAdapter,
                    position)
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<CommentBean.Item>, isClear: Boolean) {//更新
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }

    fun updateItem(position: Int){
        notifyItemChanged(position)
    }

    fun delItem(position: Int,isChild: Boolean) {//删除item
        if (isChild){

        }else{
            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    /**
     * 添加item
     */
    fun addItem(oneData: CommentBean.Item){//添加item
        dataList.add(0,oneData)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, dataList.size-1)
    }

    /**
     * 回调
     */
    interface CommentInterface {
        fun onLaudClick(isLaud: Boolean, id: Long)//点赞回调
        fun onItemClick(//item单击 长按
            isLongClick: Boolean,
            targetId: Long,
            isChild: Boolean,
            replyUser: String,
            replyId: Long,
            replyContent: String,
            mChildCommentListAdapter: ChildCommentListAdapter?,
            position: Int
        )

        fun onLoadMoreClick(//不采用预加载的方式 采用点击展开文本方式加载
            ids: String,
            position: Int
        )
    }

    private var mCommentInterface: CommentInterface? = null
    fun setCommentCallBack(mCommentInterface: CommentInterface) {
        this.mCommentInterface = mCommentInterface
    }
}