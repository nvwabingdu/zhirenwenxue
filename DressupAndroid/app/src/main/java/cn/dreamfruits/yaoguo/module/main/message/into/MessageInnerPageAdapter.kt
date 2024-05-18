package cn.dreamfruits.yaoguo.module.main.message.into

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.GlideRoundTransform
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.message.GetMessageInnerPageListBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.LogUtils.IFileWriter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 12:29
 */
@RequiresApi(Build.VERSION_CODES.O)
class MessageInnerPageAdapter(private var mContent: Context,
                              private var dataList:MutableList<GetMessageInnerPageListBean.Item>,
                              private var tag:Int
                              ) : RecyclerView.Adapter<MessageInnerPageAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_inner_page3, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //=================相同操作=================
        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(dataList[position].userInfo!!.avatarUrl)
            .into( holder.messageIcon)

        //头像
        holder.messageIcon.setOnClickListener {
            startAct(true,position,3)//为true表示跳个人中心
        }

        holder.messageName.text= dataList[position].userInfo!!.nickName



        Log.e("adadadad",tag.toString())
        //=================不同操作=================
        when(tag){
            0->{
                setLaud(holder,position)
            }
            1->{
                setFollow(holder,position)
            }
            2->{
                setComment(holder,position)
            }
        }
    }

    /**
     * 1.赞和收藏和引用
     *{
    "id": 519,
    "userInfo": {
    "id": 323258433931472,
    "nickName": "家斌的爹",
    "avatarUrl": "https://testfile.dreamfruits.cn/app/323258433931472/bf9b33f3c6030b11c4234aa216d45464",
    "relation": 3
    },
    "feedId": 339587744098176,
    "subType": 1,
    "refId": 339587744098176,
    "refType": 0,
    "isRead": 0,
    "coverUrl": "https://testfile.dreamfruits.cn/app/332988983232048/93aca638b1f033e2cdfeed63ac9d5206",
    "content": null,
    "commentId": null,
    "state": 1,
    "createTime": 1665281477638,
    "updateTime": 1665281477638,
    "replyId": null,
    "isDelete": null
    },
     */
    private fun setLaud(holder: MyViewHolder, position: Int){

        when(dataList[position].userInfo!!.relation){//关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
            0->{
                holder.messageMyFriend.visibility=View.GONE
            }
            1->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "我的关注"
            }
            2->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "关注我的"
            }
            3->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "互相关注"
            }
        }

        holder.messageBottomLayout2.visibility=View.GONE
//        holder.messageCommentLayout.visibility=View.VISIBLE//点击事件
//        holder.messageLaudLayout.visibility=View.VISIBLE//点击事件
//        holder.messageLaudImg.visibility=View.VISIBLE

        when(dataList[position].subType){//子类型，0-赞，1-收藏，2-引用
            0->{
                when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                    0->{
                        holder.messageBottomLayout1.visibility=View.GONE
                        holder.messageDetails.text= "赞了你的帖子   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                    }
                    1->{
                        holder.messageDetails.text= "赞了你的评论   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                        holder.messageBottomLayout1.visibility=View.VISIBLE
                        //重要逻辑
                        holder.messageReplyTv.maxLines=1
                        holder.messageReplyTv.ellipsize=android.text.TextUtils.TruncateAt.END
                        if (dataList[position].isDelete!=null&&dataList[position].isDelete==1){//1删除，0没删除
                            holder.messageReplyTv.text= "该评论已删除"
                        }else{
                            holder.messageReplyTv.text= dataList[position].content
                        }

                    }
                    2->{
                        holder.messageBottomLayout1.visibility=View.GONE
                        holder.messageDetails.text= "赞了你的穿搭方案   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                    }
                }
            }
            1->{
                holder.messageBottomLayout1.visibility=View.GONE
                when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                    0->{
                        holder.messageDetails.text= "收藏了你的帖子   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                    }
                    1->{

                    }
                    2->{
                        holder.messageDetails.text= "收藏了你的穿搭方案   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                    }
                }
            }
            2->{

            }
        }


        holder.messageFollowTv.visibility=View.GONE//关注按钮 点击事件

        holder.messagePostImage.visibility=View.VISIBLE//显示帖子图片

        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(GlideRoundTransform(2))
            .load(dataList[position].coverUrl)
            .into(holder.messagePostImage)

        //中间部分布局
        holder.messageContentLayout.setOnClickListener {
            startAct(false,position,0)
        }

        //帖子图片
        holder.messagePostImage.setOnClickListener {
            startAct(false,position,0)
        }
    }

    /**
     * 2.关注格式
     * {
    "id": 4,
    "userInfo": {
    "id": 323944473208672,
    "userName": "yg323944473208672",
    "avatarUrl": "https://testfile.dreamfruits.cn/admin/10086/4d676bb32c55db9d02240fe72324015b.png",
    "relation": 2
    },
    "isRead": 0,
    "state": 1,
    "createTime": 1662023507922,
    "updateTime": 1662023507922
    }
     */
    private fun setFollow(holder: MyViewHolder, position: Int){
        holder.messageDetails.text="开始关注你了   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
        holder.messageMyFriend.visibility=View.GONE//我的关注隐藏
        holder.messageBottomLayout1.visibility=View.GONE//回复隐藏
        holder.messageBottomLayout2.visibility=View.GONE//回复 点赞隐藏

        holder.messageFollowTv.visibility=View.VISIBLE//关注按钮 点击事件

        //关注逻辑
        setCareUserLogic(dataList[position].userInfo!!.relation,holder.messageFollowTv,position)

        holder.messagePostImage.visibility=View.GONE//隐藏帖子图片

        //中间部分布局
        holder.messageContentLayout.setOnClickListener {
            startAct(true,position,1)//为true表示跳个人中心
        }

    }

    //接上面点击关注按钮
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
                    dataList[position].userInfo!!.relation=1
                    mMessageInnerPageAdapterInterface!!.onCareUser(dataList[position].userInfo!!.id,true)
                }
                1->{//我关注他
                    careUserTv.text="关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].userInfo!!.relation=0
                    mMessageInnerPageAdapterInterface!!.onCareUser(dataList[position].userInfo!!.id,false)
                }
                2->{//他关注我
                    careUserTv.text="互相关注"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention_ed)
                    dataList[position].userInfo!!.relation=3
                    mMessageInnerPageAdapterInterface!!.onCareUser(dataList[position].userInfo!!.id,true)
                }
                3->{//互相关注
                    careUserTv.text="回粉"
                    careUserTv.background=mContent.resources.getDrawable(R.drawable.home_shape_attention)
                    dataList[position].userInfo!!.relation=2
                    mMessageInnerPageAdapterInterface!!.onCareUser(dataList[position].userInfo!!.id,false)
                }
            }
            //刷新当前的item
            notifyItemChanged(position)
        }
    }

    /**
     * 3.评论格式
     * {
    "id": 11,
    "userInfo": {
    "id": 324647556115184,
    "nickName": "yg324647556115184",
    "avatarUrl": "https://testfile.dreamfruits.cn/app/324647556115184/09513a0a8a0d2eb631dc84255ceebb66",
    "relation": 0
    },
    "feedId": 323946011510144,
    "refType": 0,
    "isRead": 1,
    "coverUrl": "https://testfile.dreamfruits.cn/app/323944473208672/5cebb11749158614144bc02b4372371b",
    "commentId": 324668149385024,
    "content": "哈哈哈哈哈",
    "isLaud": 0,
    "commentIsDelete": 0,
    "replyUserInfo": {
    "id": 322590839375712,
    "nickName": "成都梦果传媒科技有限公司",
    "avatarUrl": "https://testfile.dreamfruits.cn/admin/10086/17c4e8e7a14e1368409c7cf8d97da40f.png",
    "relation": 3
    },
    "replyContent": "牛哇牛哇",
    "replyIsDelete": 0,
    "firstCommentId": 324606154451856,
    "createTime": 1662109142486,
    "updateTime": 1662623549674
    }
     */
    private fun setComment(holder: MyViewHolder, position: Int){

        when(dataList[position].userInfo!!.relation){//关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
            0->{
                holder.messageMyFriend.visibility=View.GONE
            }
            1->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "我的关注"
            }
            2->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "关注我的"
            }
            3->{
                holder.messageMyFriend.visibility=View.VISIBLE
                holder.messageMyFriend.text= "互相关注"
            }
        }

        when(dataList[position].refType){//引用类型,0-动态，1-评论
            0->{
                holder.messageDetails.text= "评论了你的帖子   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                holder.messageBottomLayout1.visibility=View.GONE
                //重要逻辑
                holder.messageReplyTv.maxLines=4
                holder.messageReplyTv.ellipsize=android.text.TextUtils.TruncateAt.END
                holder.messageContentStr.visibility=View.VISIBLE

                if (dataList[position].isDelete!=null&&dataList[position].isDelete==1){//1删除，0没删除
                    holder.messageContentStr.text= "该帖子已删除"
                }else{
                    holder.messageContentStr.text= dataList[position].content
                }

            }
            1->{
                holder.messageDetails.text= "回复了你的评论   ${Singleton.timeShowRule(dataList[position].createTime,true)}"
                holder.messageBottomLayout1.visibility=View.VISIBLE
                holder.messageContentStr.visibility=View.VISIBLE

                if (dataList[position].isDelete!=null&&dataList[position].commentIsDelete==1) {//1删除，0没删除
                    holder.messageContentStr.text= "该评论已删除"
                    holder.messageBottomLayout1.visibility=View.GONE

                }else{
                    holder.messageContentStr.text= dataList[position].content

                    //重要逻辑
                    holder.messageReplyTv.maxLines=4
                    holder.messageReplyTv.ellipsize=android.text.TextUtils.TruncateAt.END
                    if (dataList[position].isDelete!=null&&dataList[position].replyIsDelete==1){//1删除，0没删除
                        holder.messageReplyTv.text= "该评论已删除"
                    }else{
                        holder.messageReplyTv.text= dataList[position].replyContent
                    }
                }

            }
        }

        holder.messageBottomLayout2.visibility=View.VISIBLE


        /**
         * 重要逻辑 firstCommentId为空，一级评论
         */
        if (dataList[position].firstCommentId==null){
            Log.e("TAG12","================一级评论 帖子评论")
            /**
             * 重要逻辑 回复评论
             */
            holder.messageCommentLayout.setOnClickListener {
                mMessageInnerPageAdapterInterface!!.onPublishComment(
                    dataList[position].commentId,
                    dataList[position].userInfo!!.nickName,
                    0)
            }
        }else{
            /**
             * 重要逻辑 回复评论
             */
            Log.e("TAG12","================评论的评论")
            holder.messageCommentLayout.setOnClickListener {
                mMessageInnerPageAdapterInterface!!.onPublishComment(
                    dataList[position].firstCommentId!!,
                    dataList[position].userInfo!!.nickName,
                    dataList[position].commentId)
            }
        }

        //点赞逻辑 这里仅点赞和取消点赞
        if (dataList[position].isLaud==1){
            holder.messageLaudImg.setImageResource(R.drawable.home_thumb_ed)
        }else{
            holder.messageLaudImg.setImageResource(R.drawable.home_thumb)
        }

        holder.messageLaudLayout.setOnClickListener {
            if (dataList[position].isLaud==1){
                holder.messageLaudImg.setImageResource(R.drawable.home_thumb)
                dataList[position].isLaud=0
                mMessageInnerPageAdapterInterface!!.onLaud(false,dataList[position].commentId)//评论id
            }else{
                holder.messageLaudImg.setImageResource(R.drawable.home_thumb_ed)
                dataList[position].isLaud=1
                mMessageInnerPageAdapterInterface!!.onLaud(true,dataList[position].commentId)//评论id
            }
        }


        holder.messageFollowTv.visibility=View.GONE//关注按钮 这里不显示关注按钮

        holder.messagePostImage.visibility=View.VISIBLE//显示帖子图片

        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(GlideRoundTransform(2))
            .load(dataList[position].coverUrl)
            .into(holder.messagePostImage)

        //中间部分布局
        holder.messageContentLayout.setOnClickListener {
            startAct(false,position,2)
        }
        //帖子图片
        holder.messagePostImage.setOnClickListener {
            startAct(false,position,2)
        }
    }

    /**
     * 跳转到图片或者视频详情页
     */
    private fun startAct(isUserCenterAct:Boolean,position: Int,pageType:Int){
        if (isUserCenterAct){
            // TODO:  跳转个人中心
            Toast.makeText(mContent,"跳转到个人中心",Toast.LENGTH_SHORT).show()
        }else{
            when(dataList[position].feedType){
                0->{//图片
                    val intent = Intent(mContent, PostDetailsActivity::class.java)//帖子详情页
                    intent.putExtra("feedId", dataList[position].feedId)
                    intent.putExtra("isMessagePage", true)
                    intent.putExtra("coverHeight",0)
                    intent.putExtra("coverWidth", 0)
                    when(pageType){
                        0->{//赞
                            when(dataList[position].subType){//子类型，0-赞，1-收藏，2-引用
                                0->{
                                    when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                                        0->{//"赞了你的帖子

                                        }
                                        1->{//赞了你的评论

                                            intent.putExtra("isComment", true)
                                            if (dataList[position].replyId==null){//为空表示为帖子评论
                                                intent.putExtra("highlightId",dataList[position].commentId)//需要高亮的评论id

                                                intent.putExtra("messageIds", "${dataList[position].commentId}")//一级评论id
                                                intent.putExtra("messageCommentIds", "${dataList[position].commentId}")//一级评论，二级评论，二级评论，

                                                Log.e("TAG12","highlightId：${dataList[position].commentId}")
                                                Log.e("TAG12","messageIds：${dataList[position].commentId}")
                                                Log.e("TAG12","messageCommentIds：${dataList[position].commentId}")
                                            }else{
                                                intent.putExtra("highlightId",dataList[position].commentId)//需要高亮的评论id

                                                intent.putExtra("messageIds","${dataList[position].firstCommentId}")//一级评论id
                                                intent.putExtra("messageCommentIds", "${dataList[position].firstCommentId},${dataList[position].replyId},${dataList[position].commentId}")//一级评论，二级评论，二级评论，

                                                Log.e("TAG12","highlightId：${dataList[position].replyId}")
                                                Log.e("TAG121","messageIds：${dataList[position].firstCommentId}")
                                                Log.e("TAG12","messageCommentIds：${dataList[position].firstCommentId},${dataList[position].replyId},${dataList[position].commentId}")
                                            }


                                        }
                                        2->{//赞了你的穿搭方案

                                        }
                                    }
                                }
                                1->{
                                    when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                                        0->{//收藏了你的帖子

                                        }
                                        1->{

                                        }
                                        2->{//收藏了你的穿搭方案

                                        }
                                    }
                                }
                                2->{

                                }
                            }
                        }
                        1->{//关注

                        }
                        2->{//评论回复
                            intent.putExtra("isComment", true)
                            if (dataList[position].replyId==null){//为空表示为帖子评论
                                intent.putExtra("highlightId",dataList[position].commentId)//需要高亮的评论id

                                intent.putExtra("messageIds", "${dataList[position].commentId}")//一级评论id
                                intent.putExtra("messageCommentIds", "${dataList[position].commentId}")//一级评论，二级评论，二级评论，

                                Log.e("TAG12","highlightId：${dataList[position].commentId}")
                                Log.e("TAG12","messageIds：${dataList[position].commentId}")
                                Log.e("TAG12","messageCommentIds：${dataList[position].commentId}")
                            }else{
                                intent.putExtra("highlightId",dataList[position].commentId)//需要高亮的评论id

                                intent.putExtra("messageIds","${dataList[position].firstCommentId}")//一级评论id
                                intent.putExtra("messageCommentIds", "${dataList[position].firstCommentId},${dataList[position].replyId},${dataList[position].commentId}")//一级评论，二级评论，二级评论，

                                Log.e("TAG12","highlightId：${dataList[position].replyId}")
                                Log.e("TAG121","messageIds：${dataList[position].firstCommentId}")
                                Log.e("TAG12","messageCommentIds：${dataList[position].firstCommentId},${dataList[position].replyId},${dataList[position].commentId}")
                            }
                        }
                        3->{
                            //单用于头像跳转到个人中心
                        }
                    }

                    mContent.startActivity(intent)
                }
                1->{//视频
                    when(pageType){
                        0->{//赞
                            when(dataList[position].subType){//子类型，0-赞，1-收藏，2-引用
                                0->{
                                    when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                                        0->{//"赞了你的帖子
                                            mMessageInnerPageAdapterInterface!!.onLoadVideoData(dataList[position].feedId,false,0L,0L,0L)
                                        }
                                        1->{//赞了你的评论
                                            mMessageInnerPageAdapterInterface!!.onLoadVideoData(dataList[position].feedId,true,dataList[position].commentId,dataList[position].firstCommentId,dataList[position].replyId)
                                            return
                                        }
                                        2->{//赞了你的穿搭方案

                                        }
                                    }

                                }
                                1->{
                                    when(dataList[position].refType){//引用类型,0-动态，1-评论，2-穿搭方案
                                        0->{//收藏了你的帖子
                                            mMessageInnerPageAdapterInterface!!.onLoadVideoData(dataList[position].feedId,false,0L,0L,0L)
                                        }
                                        1->{

                                        }
                                        2->{//收藏了你的穿搭方案

                                        }
                                    }
                                }
                                2->{

                                }
                            }
                        }
                        1->{//关注

                        }
                        2->{//评论回复
                            mMessageInnerPageAdapterInterface!!.onLoadVideoData(dataList[position].feedId,true,dataList[position].commentId,dataList[position].firstCommentId,dataList[position].replyId)
                        }
                        3->{
                            //单用于头像跳转到个人中心
                        }
                    }
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageIcon: ImageView = itemView.findViewById(R.id.message_icon)

        var messageContentLayout: View = itemView.findViewById(R.id.message_content_layout)//中间部分

        var messageName: TextView = itemView.findViewById(R.id.message_name)
        var messageMyFriend: TextView = itemView.findViewById(R.id.message_my_friend)//我的好友 有可能是其他文本
        var messageDetails: TextView = itemView.findViewById(R.id.message_details)// 有可能在后面加入时间
        var messageContentStr: TextView = itemView.findViewById(R.id.message_content_str)//一级评论

        var messageBottomLayout1: View = itemView.findViewById(R.id.message_bottom_layout1)//包裹回复的布局
        var messageReplyTv: TextView = itemView.findViewById(R.id.message_reply_tv)//回复文本

        var messageBottomLayout2: View = itemView.findViewById(R.id.message_bottom_layout2)//包裹回复和点赞按钮的布局
        var messageCommentLayout: View = itemView.findViewById(R.id.message_comment_layout)//1.回复布局
        var messageLaudLayout: View = itemView.findViewById(R.id.message_laud_layout)//2.点赞布局
        var messageLaudImg: ImageView = itemView.findViewById(R.id.message_laud_img)//2.1点赞图标

        var messageFollowTv: TextView = itemView.findViewById(R.id.message_follow)//关注按钮
        var messagePostImage: ImageView = itemView.findViewById(R.id.message_post_image)//帖子图片

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetMessageInnerPageListBean.Item>, isClear: Boolean) {
        if (isClear) {
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
    interface MessageInnerPageAdapterInterface {
        fun onLaud(isLaud: Boolean, id: Long)
        fun onCareUser(id: Long, isCare: Boolean)
        fun onPublishComment(id: Long, replyUser: String, replyId: Long)
        fun onLoadVideoData(id: Long,isComment:Boolean,commentId:Long?,firstCommentId:Long?,replyId:Long?)
    }
    private var mMessageInnerPageAdapterInterface: MessageInnerPageAdapterInterface? = null
    open fun setMessageInnerPageAdapterListener(mMessageInnerPageAdapterInterface: MessageInnerPageAdapterInterface) {
        this.mMessageInnerPageAdapterInterface = mMessageInnerPageAdapterInterface
    }

}