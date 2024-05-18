package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.AttentionApi
import cn.dreamfruits.yaoguo.repository.api.UnityApi
import cn.dreamfruits.yaoguo.repository.bean.attention.*
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.unity.GetStyleVersionListBean
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 17:49
 */
class UnityRemoteDataSource : BaseRemoteDataSource(){
    private val unityApi: UnityApi = retrofitService(UnityApi::class.java)


    /**
     * 获取版型列表 New
     */
    fun getStyleVersionList(
        size: Int,//每页数量
        lastTime: Long?,//分页用，传入接口返回的该值
    ): Observable<GetStyleVersionListBean> {
        return unityApi.getStyleVersionList(size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }




}