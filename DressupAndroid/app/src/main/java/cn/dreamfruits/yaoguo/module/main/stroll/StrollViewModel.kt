package cn.dreamfruits.yaoguo.module.main.stroll

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.repository.ClothesRepository
import cn.dreamfruits.yaoguo.repository.bean.CommonStateIntBean
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject

/**
 * @Author qiwangi
 * @Date 2023/6/5
 * @TIME 14:07
 */
class StrollViewModel: BaseViewModel() {

    private val clothesRepository by inject<ClothesRepository>()

//    /**
//     * 根据类型获取版型列表
//     */
//    var mGetStyleVersionListByTypeBean: GetStyleVersionListByTypeBean? = null
//    private val _styleVersionListByTypeBeanState = MutableLiveData<StyleVersionListByTypeBeanState>()
//    val styleVersionListByTypeBeanState: MutableLiveData<StyleVersionListByTypeBeanState> get() = _styleVersionListByTypeBeanState
//    fun getStyleVersionListByType(
//        type: Int,//1-上装，2-下装，3-连衣裙，4-袜子，5-鞋子，6-配饰
//        size: Int,
//        gender: Int?,
//        lastTime: Long?,
//        subType: Int?//从逛逛页获取数据不用传，从DIY页获取数据传1
//    ) {
//        val disposable = clothesRepository.getStyleVersionListByType(type, size,gender, lastTime, subType)
//            .subscribe({
//                mGetStyleVersionListByTypeBean= it
//                _styleVersionListByTypeBeanState.value = StyleVersionListByTypeBeanState.Success
//                Log.e("zqr", "_styleVersionListByTypeBeanState请求成功：$it")
//            }, {
//
//                _styleVersionListByTypeBeanState.value = StyleVersionListByTypeBeanState.Fail(it.message)
//                Log.e("zqr", "_styleVersionListByTypeBeanState请求失败：$it")
//            })
//        addDisposable(disposable)
//    }


    /**
     * 根据类型获取版型列表2  逛逛  viewModel
     */
    var mGetStyleVersionListByTypeStrollBean: GetStyleVersionListByTypeBean? = null
    private val _getStyleVersionTypeListStrollState = MutableLiveData<GetStyleVersionTypeListStrollState>()
    val getStyleVersionTypeListStrollState: MutableLiveData<GetStyleVersionTypeListStrollState> get() = _getStyleVersionTypeListStrollState
    fun getStyleVersionTypeListStroll(
        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?=null//上一级分类的id 可选

    ) {
        val disposable = clothesRepository.getStyleVersionTypeListStroll(type,id)
            .subscribe({
                mGetStyleVersionListByTypeStrollBean= it
                _getStyleVersionTypeListStrollState.value = GetStyleVersionTypeListStrollState.Success
                Log.e("zqr", "_getStyleVersionTypeListStrollState请求成功：$it")
            }, {
                _getStyleVersionTypeListStrollState.value = GetStyleVersionTypeListStrollState.Fail(it.message)
                Log.e("zqr", "_getStyleVersionTypeListStrollState请求失败：$it")
            })
        addDisposable(disposable)
    }



    /**
     * 根据类型获取版型列表2  逛逛  viewModel   用于左侧一级列表 和上面接口相同
     */
    var mGetStyleVersionListByTypeStrollBeanLeft: GetStyleVersionListByTypeBean? = null
    private val _getStyleVersionTypeListStrollStateLeft = MutableLiveData<GetStyleVersionTypeListStrollStateLeft>()
    val getStyleVersionTypeListStrollStateLeft: MutableLiveData<GetStyleVersionTypeListStrollStateLeft> get() = _getStyleVersionTypeListStrollStateLeft
    fun getStyleVersionTypeListStrollLeft(
        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?=null//上一级分类的id 可选
    ) {
        val disposable = clothesRepository.getStyleVersionTypeListStroll(type,id)
            .subscribe({
                mGetStyleVersionListByTypeStrollBeanLeft= it
                _getStyleVersionTypeListStrollStateLeft.value = GetStyleVersionTypeListStrollStateLeft.Success
                Log.e("zqr", "_getStyleVersionTypeListStrollState请求成功：$it")
            }, {
                _getStyleVersionTypeListStrollStateLeft.value = GetStyleVersionTypeListStrollStateLeft.Fail(it.message)
                Log.e("zqr", "_getStyleVersionTypeListStrollState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 根据二级分类获取服装列表 逛逛  viewModel
     */
    var mGetStyleVersionListByTypeBean: GetStyleVersionListByTypeBean? = null
    private val _getClothesItemListByTypeStrollStrollState = MutableLiveData<GetClothesItemListByTypeStrollStrollState>()
    val getClothesItemListByTypeStrollStrollState: MutableLiveData<GetClothesItemListByTypeStrollStrollState> get() = _getClothesItemListByTypeStrollStrollState
    fun getClothesItemListByTypeStroll(
        gender: Int,//0-女，1-男
        type: Long,//二级分类id
        size: Int,
        lastTime: Long?
    ) {
        val disposable = clothesRepository.getClothesItemListByTypeStroll(gender,type,size,lastTime)
            .subscribe({
                mGetStyleVersionListByTypeBean= it
                _getClothesItemListByTypeStrollStrollState.value = GetClothesItemListByTypeStrollStrollState.Success
                Log.e("zqr", "_getClothesItemListByTypeStrollStrollState请求成功：$it")
            }, {

                _getClothesItemListByTypeStrollStrollState.value = GetClothesItemListByTypeStrollStrollState.Fail(it.message)
                Log.e("zqr", "_getClothesItemListByTypeStrollStrollState请求失败：$it")
            })
        addDisposable(disposable)
    }


//    /**
//     * 根据版型id获取商品  逛逛页面接口
//     */
//    var mGetClothesItemListBySvIdBean: GetStyleVersionListByTypeBean? = null
//    private val _clothesItemListBySvIdBeanState = MutableLiveData<ClothesItemListBySvIdBeanState>()
//    val clothesItemListBySvIdBeanState: MutableLiveData<ClothesItemListBySvIdBeanState> get() = _clothesItemListBySvIdBeanState
//    fun getClothesItemListBySvId(
//        svId: Long,
//        size: Int,
//        gender: Int,
//        isOpen: Int,//0-私密，1-公开，2-全部
//        lastTime: Long?,
//    ) {
//        val disposable = clothesRepository.getClothesItemListBySvId(svId, size,gender, isOpen, lastTime)
//            .subscribe({
//                mGetClothesItemListBySvIdBean= it
//                _clothesItemListBySvIdBeanState.value = ClothesItemListBySvIdBeanState.Success
//                Log.e("zqr", "_clothesItemListBySvIdBeanState请求成功：$it")
//            }, {
//
//                _clothesItemListBySvIdBeanState.value = ClothesItemListBySvIdBeanState.Fail(it.message)
//                Log.e("zqr", "_clothesItemListBySvIdBeanState请求失败：$it")
//            })
//        addDisposable(disposable)
//    }


    /**
     * 获取服装单品详情  viewModel
     */
    var mGetClothesItemInfoBean: GetClothesItemInfoBean? = null
    private val _getClothesItemInfoBeanState = MutableLiveData<GetClothesItemInfoBeanState>()
    val getClothesItemInfoBeanState: MutableLiveData<GetClothesItemInfoBeanState> get() = _getClothesItemInfoBeanState
    fun getClothesItemInfo(
        id: Long//服装单品详情id
    ) {
        val disposable = clothesRepository.getClothesItemInfo(id)
            .subscribe({
                mGetClothesItemInfoBean= it
                _getClothesItemInfoBeanState.value = GetClothesItemInfoBeanState.Success
                Log.e("zqr", "_getClothesItemInfoBeanState请求成功：$it")
            }, {

                _getClothesItemInfoBeanState.value = GetClothesItemInfoBeanState.Fail(it.message)
                Log.e("zqr", "_getClothesItemInfoBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 潮流精选  viewModel
     */
    var mGetFeedListByCvIdBean: WaterfallFeedBean? = null
    private val _getFeedListByCvIdBeanState = MutableLiveData<GetFeedListByCvIdBeanState>()
    val getFeedListByCvIdBeanState: MutableLiveData<GetFeedListByCvIdBeanState> get() = _getFeedListByCvIdBeanState
    fun getFeedListByCvId(

        targetId: Long,//服装单品主键id
        size: Int,//请求数目，默认4
        lastTime: Long?

    ) {
        val disposable = clothesRepository.getFeedListByCvId(targetId,size,lastTime)
            .subscribe({
                mGetFeedListByCvIdBean= Tool().toUrlWaterfallFeedBean(it,"0")
                _getFeedListByCvIdBeanState.value = GetFeedListByCvIdBeanState.Success
                Log.e("zqr", "_getFeedListByCvIdBeanState请求成功：$it")
            }, {

                _getFeedListByCvIdBeanState.value = GetFeedListByCvIdBeanState.Fail(it.message)
                Log.e("zqr", "_getFeedListByCvIdBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 相似穿搭  viewModel
     */
    var mGetSimilarRecommendBean: GetSimilarRecommendBean? = null
    private val _getSimilarRecommendBeanState = MutableLiveData<GetSimilarRecommendBeanState>()
    val getSimilarRecommendBeanState: MutableLiveData<GetSimilarRecommendBeanState> get() = _getSimilarRecommendBeanState
    fun getSimilarRecommend(
        targetId: Long,//服装单品主键id
        size: Int//请求数目，默认30
    ) {
        val disposable = clothesRepository.getSimilarRecommend(targetId,size)
            .subscribe({
                it.list.forEach {
                    it.picUrl=Tool().decodePicUrls(it.picUrl,"0",true)
                }
                mGetSimilarRecommendBean=it
                _getSimilarRecommendBeanState.value = GetSimilarRecommendBeanState.Success
                Log.e("zqr", "_getSimilarRecommendBeanState请求成功：$it")
            }, {

                _getSimilarRecommendBeanState.value = GetSimilarRecommendBeanState.Fail(it.message)
                Log.e("zqr", "_getSimilarRecommendBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 修改单品可见状态  viewModel
     */
    var mChangeClothesItemSeeLimitBean: CommonStateIntBean? = null
    private val _changeClothesItemSeeLimitState = MutableLiveData<ChangeClothesItemSeeLimitState>()
    val changeClothesItemSeeLimitState: MutableLiveData<ChangeClothesItemSeeLimitState> get() = _changeClothesItemSeeLimitState
    fun changeClothesItemSeeLimit(

        id: Long,//单品id
        state: Int,//0-私密，1-公开

    ) {
        val disposable = clothesRepository.changeClothesItemSeeLimit(id,state)
            .subscribe({
                mChangeClothesItemSeeLimitBean= it
                _changeClothesItemSeeLimitState.value = ChangeClothesItemSeeLimitState.Success
                Log.e("zqr", "_changeClothesItemSeeLimitState请求成功：$it")
            }, {

                _changeClothesItemSeeLimitState.value = ChangeClothesItemSeeLimitState.Fail(it.message)
                Log.e("zqr", "_changeClothesItemSeeLimitState请求失败：$it")
            })
        addDisposable(disposable)
    }




    /**
     * 修改单品名字  viewModel
     */
    var mChangeClothesItemNameBean: CommonStateIntBean? = null
    private val _changeClothesItemNameState = MutableLiveData<ChangeClothesItemNameState>()
    val changeClothesItemNameState: MutableLiveData<ChangeClothesItemNameState> get() = _changeClothesItemNameState
    fun changeClothesItemName(

        id: Long,//单品id
        str: String,//单品名字

    ) {
        val disposable = clothesRepository.changeClothesItemName(id,str)
            .subscribe({
                mChangeClothesItemNameBean= it
                _changeClothesItemNameState.value = ChangeClothesItemNameState.Success
                Log.e("zqr", "_changeClothesItemNameState请求成功：$it")
            }, {

                _changeClothesItemNameState.value = ChangeClothesItemNameState.Fail(it.message)
                Log.e("zqr", "_changeClothesItemNameState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 删除单品  viewModel
     */
    var mDeleteClothesItemBean: CommonStateIntBean? = null
    private val _deleteClothesItemState = MutableLiveData<DeleteClothesItemState>()
    val deleteClothesItemState: MutableLiveData<DeleteClothesItemState> get() = _deleteClothesItemState
    fun deleteClothesItem(

        id: Long,//单品id

    ) {
        val disposable = clothesRepository.deleteClothesItem(id)
            .subscribe({
                mDeleteClothesItemBean= it
                _deleteClothesItemState.value = DeleteClothesItemState.Success
                Log.e("zqr", "_deleteClothesItemState请求成功：$it")
            }, {

                _deleteClothesItemState.value = DeleteClothesItemState.Fail(it.message)
                Log.e("zqr", "_deleteClothesItemState请求失败：$it")
            })
        addDisposable(disposable)
    }

}