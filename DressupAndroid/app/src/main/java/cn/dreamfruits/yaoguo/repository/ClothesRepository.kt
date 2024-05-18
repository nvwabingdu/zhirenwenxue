package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.repository.bean.CommonStateIntBean
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.ClothesRemoteDataSource
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Observable


/***
 * 单品相关仓库
 */
class ClothesRepository(dataSource: ClothesRemoteDataSource) :
    BaseRemoteRepository<ClothesRemoteDataSource>(dataSource) {


    fun getClothesCategory(
        position: Int,
        gender: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesCategoryBean>> {
        return remoteDataSource.getClothesCategory(position, gender, size, lastTime)
    }


    fun getClothesList(
        cvId: Long,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return remoteDataSource.getClothesList(cvId, size, lastTime)
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
        return remoteDataSource.getStyleVersionListByType(type, size, gender, lastTime, subType)
    }

    /**
     * 根据类型获取版型列表2  逛逛  本地仓库
     */
    fun getStyleVersionTypeListStroll(

        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?=null//上一级分类的id 可选

    ): Observable<GetStyleVersionListByTypeBean> {
        return remoteDataSource.getStyleVersionTypeListStroll(type,id)
    }

    /**
     * 根据二级分类获取服装列表 逛逛  本地仓库
     */
    fun getClothesItemListByTypeStroll(

        gender: Int,//0-女，1-男
        type: Long,//二级分类id
        size: Int,
        lastTime: Long?

    ): Observable<GetStyleVersionListByTypeBean> {
        return remoteDataSource.getClothesItemListByTypeStroll(gender,type,size,lastTime)
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
        return remoteDataSource.getClothesItemListBySvId(svId, size, gender, isOpen, lastTime)
    }


    /**
     * 获取我的DIY列表
     */
    fun getMyDiyList(
        size: Int,
        isOpen: Int,//0-私密，1-公开，2-全部
        lastTime: Long?,
    ): Observable<GetStyleVersionListByTypeBean> {
        return remoteDataSource.getMyDiyList(size, isOpen, lastTime)
    }


    /**
     * 获取服装单品详情
     */
    fun getClothesItemInfo(
        id: Long,//服装单品详情id
    ): Observable<GetClothesItemInfoBean> {
        return remoteDataSource.getClothesItemInfo(id)
    }


    /**
     * 潮流精选
     */
    fun getFeedListByCvId(
        targetId: Long,//服装单品主键id
        size: Int,//请求数目，默认4
        lastTime: Long?,
    ): Observable<WaterfallFeedBean> {
        return remoteDataSource.getFeedListByCvId(targetId, size, lastTime)
    }

    /**
     * 相似穿搭
     */
    fun getSimilarRecommend(
        targetId: Long,//服装单品主键id
        size: Int,//请求数目，默认30
    ): Observable<GetSimilarRecommendBean> {
        return remoteDataSource.getSimilarRecommend(targetId, size)
    }

    /**
     * 获取版型分类列表APP（目前用于diy）  本地仓库
     */
    fun getStyleVersionTypeList(

        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?,//上一级分类的id 可选

    ): Observable<GetStyleVersionListByTypeBean> {
        return remoteDataSource.getStyleVersionTypeList(type, id)
    }

    fun getMyClothesItem(
        size: Int,
        type: Int,//0-我收藏的，1-我试穿的，2-我浏览的，3-我DIY的
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return remoteDataSource.getMyClothesItem(size, type, lastTime)
    }

    fun searchClothesItem(
        key: String,
        page: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<ClothesBean>> {
        return remoteDataSource.searchClothesItem(key, page, size, lastTime)
    }


    /**
     * 根据分类id获取版型列表（目前用于diy）  本地仓库
     */
    fun getStyleVersionListByType(

        id: Long,//三级分类id
        size: Int,
        lastTime: Long?

    ): Observable<GetStyleVersionListByTypeBean> {
        return remoteDataSource.getStyleVersionListByType(id,size,lastTime)
    }

    /**
     * 修改单品可见状态  本地仓库
     */
    fun changeClothesItemSeeLimit(

        id: Long,//单品id
        state: Int,//0-私密，1-公开

    ): Observable<CommonStateIntBean> {
        return remoteDataSource.changeClothesItemSeeLimit(id,state)
    }

    /**
     * 修改单品名字  本地仓库
     */
    fun changeClothesItemName(

        id: Long,//单品id
        str: String,//单品名字

    ): Observable<CommonStateIntBean> {
        return remoteDataSource.changeClothesItemName(id,str)
    }



    /**
     * 删除单品  本地仓库
     */
    fun deleteClothesItem(

        id: Long,//单品id

    ): Observable<CommonStateIntBean> {
        return remoteDataSource.deleteClothesItem(id)
    }

    /**
     * 重新上传制作服装单品  本地仓库
     */
    fun remakeClothesItem(

        id: Long,//单品id

    ): Observable<CommonStateIntBean> {
        return remoteDataSource.remakeClothesItem(id)
    }

}