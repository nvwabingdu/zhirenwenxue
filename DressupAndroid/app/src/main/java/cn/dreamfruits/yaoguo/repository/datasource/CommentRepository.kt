package cn.dreamfruits.yaoguo.repository.datasource

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentDetailBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.DelCommentItemBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.CommentRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/4/8
 * @TIME 17:47
 */
class CommentRepository(dataSource: CommentRemoteDataSource) :
    BaseRemoteRepository<CommentRemoteDataSource>(dataSource) {

    /**
     * 获取评论详情
     */
    fun getCommentDetail(
        commentId: Long,//评论id
        ids: String//子评论id，例如，11111,22222
    ): Observable<CommentDetailBean> {
        return remoteDataSource.getCommentDetail(commentId, ids).doOnNext {

        }
    }

    /**
     * 获取评论列表
     */
    fun getCommentList(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        size: Long,//每页条数
        lastTime: Long?,//上一页返回的lastTime，第一页不传
        ids: String?,//排除的id，例如：11111111,22222222
        commentIds: String?
    ): Observable<CommentBean> {
        return remoteDataSource.getCommentList(targetId, type, size, lastTime, ids,commentIds).doOnNext {

        }
    }


    /**
     * 发布评论
     */
    fun getPublishComment(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        content: String,//评论内容
        replyId: Long?,//回复的评论id
        atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        config: String//配置字符串
    ): Observable<CommentBean.Item> {
        return remoteDataSource.getPublishComment(targetId, type, content, replyId, atUser,config).doOnNext {

        }
    }

    /**
     * ===================================获取评论列表 子级
     */
    fun getCommentListChild(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        size: Long,//每页条数
        lastTime: Long?,//上一页返回的lastTime，第一页不传
        ids: String?//排除的id，例如：11111111,22222222
    ): Observable<ChildCommentBean> {
        return remoteDataSource.getCommentListChild(targetId, type, size, lastTime, ids).doOnNext {

        }
    }

    /**
     * ===================================发布评论 子级
     */
    fun getPublishCommentChild(
        targetId: Long,//动态id，一级评论id
        type: Int,//0-动态 1-评论
        content: String,//评论内容
        replyId: Long?,//回复的评论id
        atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        config: String//配置字符串
    ): Observable<ChildCommentBean.ChildItem> {
        return remoteDataSource.getPublishCommentChild(targetId, type, content, replyId, atUser,config).doOnNext {

        }
    }



    /**
     * 点赞评论
     */
    fun getLaudComment(
        id: Long//评论id
    ): Observable<Any> {
        return remoteDataSource.getLaudComment(id).doOnNext {
        }
    }

    /**
     * 取消点赞评论
     */
    fun getUnLaudComment(
        id: Long//评论id
    ): Observable<Any> {
        return remoteDataSource.getUnLaudComment(id).doOnNext {
        }
    }



    /**
     * 删除评论
     */
    fun getDeleteComment(
        commentId: String//评论id
    ): Observable<DelCommentItemBean> {
        return remoteDataSource.getDeleteComment(commentId).doOnNext {
        }
    }

}


