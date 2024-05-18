package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.ClothesApi
import cn.dreamfruits.yaoguo.repository.bean.CommonStateIntBean
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import io.reactivex.rxjava3.core.Observable

/**
 * 单品相关的数据源
 */
class ClothesRemoteDataSource : BaseRemoteDataSource() {

    private val clothesApi = retrofitService(ClothesApi::class.java)


    fun getClothesCategory(
        position: Int,
        gender: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesCategoryBean>> {
        return clothesApi.getClothesCategory(position, gender, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    fun getClothesList(
        cvId: Long,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return clothesApi.getClothesList(cvId, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 根据类型获取版型列表
     */
    fun getStyleVersionListByType(
        type: Int,//1-上装，2-下装，3-连衣裙，4-袜子，5-鞋子，6-配饰
        size: Int,
        gender: Int?,
        lastTime: Long?,
        subType: Int?,//从逛逛页获取数据不用传，从DIY页获取数据传1
    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getStyleVersionListByType(type, size, gender, lastTime, subType)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 根据类型获取版型列表2  逛逛   远程仓库
     */
    fun getStyleVersionTypeListStroll(

        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?=null//上一级分类的id 可选

    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getStyleVersionTypeListStroll(type,id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 根据二级分类获取服装列表 逛逛   远程仓库
     */
    fun getClothesItemListByTypeStroll(

        gender: Int,//0-女，1-男
        type: Long,//二级分类id
        size: Int,
        lastTime: Long?

    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getClothesItemListByTypeStroll(gender,type,size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 根据版型id获取商品
     */
    fun getClothesItemListBySvId(
        svId: Long,
        size: Int,
        gender: Int,
        isOpen: Int,//0-私密，1-公开，2-全部
        lastTime: Long?,
    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getClothesItemListBySvId(svId, size, gender, isOpen, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取我的DIY列表
     */
    fun getMyDiyList(
        size: Int,
        isOpen: Int,//0-私密，1-公开，2-全部
        lastTime: Long?,
    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getMyDiyList(size, isOpen, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取服装单品详情
     */
    fun getClothesItemInfo(
        id: Long,//服装单品详情id
    ): Observable<GetClothesItemInfoBean> {
        return clothesApi.getClothesItemInfo(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 潮流精选
     */
    fun getFeedListByCvId(
        targetId: Long,//服装单品主键id
        size: Int,//请求数目，默认4
        lastTime: Long?,
    ): Observable<WaterfallFeedBean> {
        return clothesApi.getFeedListByCvId(targetId, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 相似穿搭
     */
    fun getSimilarRecommend(
        targetId: Long,//服装单品主键id
        size: Int,//请求数目，默认30
    ): Observable<GetSimilarRecommendBean> {
        return clothesApi.getSimilarRecommend(targetId, size)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 获取版型分类列表APP（目前用于diy）   远程仓库
     */
    fun getStyleVersionTypeList(
        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?,//上一级分类的id 可选
    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getStyleVersionTypeList(type, id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 根据分类id获取版型列表（目前用于diy）   远程仓库
     */
    fun getStyleVersionListByType(

        id: Long,//三级分类id
        size: Int,
        lastTime: Long?

    ): Observable<GetStyleVersionListByTypeBean> {
        return clothesApi.getStyleVersionListByType(id,size,lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取我的服装列表
     * <<BaseResult<BasePageResult<GetStyleVersionListByTypeBean.Item>>>>
     */
    fun getMyClothesItem(
        size: Int,
        type: Int,//0-我收藏的，1-我试穿的，2-我浏览的，3-我DIY的
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return clothesApi.getMyClothesItem(size, type, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取我的服装列表
     * <<BaseResult<BasePageResult<GetStyleVersionListByTypeBean.Item>>>>
     */
    fun searchClothesItem(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return clothesApi.searchClothesItem(key, page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 修改单品可见状态   远程仓库
     */
    fun changeClothesItemSeeLimit(

        id: Long,//单品id
        state: Int,//0-私密，1-公开

    ): Observable<CommonStateIntBean> {
        return clothesApi.changeClothesItemSeeLimit(id,state)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 修改单品名字   远程仓库
     */
    fun changeClothesItemName(

        id: Long,//单品id
        str: String,//单品名字

    ): Observable<CommonStateIntBean> {
        return clothesApi.changeClothesItemName(id,str)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 删除单品   远程仓库
     */
    fun deleteClothesItem(

        id: Long,//单品id

    ): Observable<CommonStateIntBean> {
        return clothesApi.deleteClothesItem(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 重新上传制作服装单品   远程仓库
     */
    fun remakeClothesItem(

        id: Long,//单品id

    ): Observable<CommonStateIntBean> {
        return clothesApi.remakeClothesItem(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


}