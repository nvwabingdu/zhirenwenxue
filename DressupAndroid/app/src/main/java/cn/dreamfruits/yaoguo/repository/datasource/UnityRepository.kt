package cn.dreamfruits.yaoguo.repository.datasource

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentDetailBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.unity.GetStyleVersionListBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.CommentRemoteDataSource
import cn.dreamfruits.yaoguo.repository.datasource.remote.UnityRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/4/8
 * @TIME 17:47
 */
class UnityRepository(dataSource: UnityRemoteDataSource) :
    BaseRemoteRepository<UnityRemoteDataSource>(dataSource) {

    /**
     * 获取版型列表 New
     */
    fun getStyleVersionList(
        size: Int,//每页数量
        lastTime: Long?,//分页用，传入接口返回的该值
    ): Observable<GetStyleVersionListBean> {
        return remoteDataSource.getStyleVersionList(size,lastTime).doOnNext {
        }
    }




}


