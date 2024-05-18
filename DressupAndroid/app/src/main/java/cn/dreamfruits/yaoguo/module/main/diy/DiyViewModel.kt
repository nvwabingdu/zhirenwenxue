package cn.dreamfruits.yaoguo.module.main.diy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionListByTypeState
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListBeanStateLeft
import cn.dreamfruits.yaoguo.module.main.home.state.MyDiyListBeanBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.RemakeClothesItem
import cn.dreamfruits.yaoguo.repository.ClothesRepository
import cn.dreamfruits.yaoguo.repository.bean.CommonStateIntBean
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Tool
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

/**
 * @Author qiwangi
 * @Date 2023/6/5
 * @TIME 14:07
 */
class DiyViewModel: BaseViewModel() {
    private val clothesRepository by inject<ClothesRepository>()
    /**
     * 根据分类id获取版型列表（目前用于diy）  viewModel
     */
    var mGetStyleVersionListByTypeBean: GetStyleVersionListByTypeBean? = null
    private val _getStyleVersionListByTypeState = MutableLiveData<GetStyleVersionListByTypeState>()
    val getStyleVersionListByTypeState: MutableLiveData<GetStyleVersionListByTypeState> get() = _getStyleVersionListByTypeState
    fun getStyleVersionListByType(

        id: Long,//三级分类id
        size: Int,
        lastTime: Long?

    ) {
        val disposable = clothesRepository.getStyleVersionListByType(id,size,lastTime)
            .subscribe({
                try {
                    it.list.forEach {
                        Log.e("zqr121210000", "_myDiyListBeanBean：${it.coverUrl}")
                        it.coverUrl=Singleton.getUrlX(it.coverUrl,true,100)
                        Log.e("zqr12121", "_myDiyListBeanBean：${it.coverUrl}")
                    }
                }catch (e:Exception){
                    Log.e("zqr", "_myDiyListBeanBeanState请求成功 但链接鉴权有错：$e")
                }
                mGetStyleVersionListByTypeBean= it
                _getStyleVersionListByTypeState.value = GetStyleVersionListByTypeState.Success
                Log.e("zqr", "_getStyleVersionListByTypeState请求成功：$it")
            }, {

                _getStyleVersionListByTypeState.value = GetStyleVersionListByTypeState.Fail(it.message)
                Log.e("zqr", "_getStyleVersionListByTypeState请求失败：$it")
            })
        addDisposable(disposable)
    }

//    /**
//     * 根据类型获取版型列表
//     */
//    var mGetStyleVersionListByTypeBean: GetStyleVersionListByTypeBean? = null
//    private val _styleVersionListByTypeBeanState = MutableLiveData<StyleVersionListByTypeBeanState>()
//    val styleVersionListByTypeBeanState: MutableLiveData<StyleVersionListByTypeBeanState> get() = _styleVersionListByTypeBeanState
//    fun getStyleVersionListByType(
//        type: Int,//1-上装，2-下装，3-连衣裙，4-袜子，5-鞋子，6-配饰
//        size: Int,
//        gender:Int?,
//        lastTime: Long?,
//        subType: Int//从逛逛页获取数据不用传，从DIY页获取数据传1
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
     * 根据版型id获取商品
     */
    var mMyDiyListBean: GetStyleVersionListByTypeBean? = null
    private val _myDiyListBeanBeanState = MutableLiveData<MyDiyListBeanBeanState>()
    val myDiyListBeanBeanState: MutableLiveData<MyDiyListBeanBeanState> get() = _myDiyListBeanBeanState
    fun getMyDiyList(
        size: Int,
        isOpen: Int,//0-私密，1-公开，2-全部
        lastTime: Long?,
    ) {
        val disposable = clothesRepository.getMyDiyList(size, isOpen, lastTime)
            .subscribe({
                try {
                    it.list.forEach {
                        Log.e("zqr121210000", "_myDiyListBeanBean：${it.coverUrl}")
                        it.coverUrl=Singleton.getUrlX(it.coverUrl,true,100)
                        Log.e("zqr12121", "_myDiyListBeanBean：${it.coverUrl}")
                    }
                }catch (e:Exception){
                    Log.e("zqr", "_myDiyListBeanBeanState请求成功 但链接鉴权有错：$e")
                }

                mMyDiyListBean= it
                _myDiyListBeanBeanState.value = MyDiyListBeanBeanState.Success
                Log.e("zqr", "_myDiyListBeanBeanState请求成功：$it")
            }, {

                _myDiyListBeanBeanState.value = MyDiyListBeanBeanState.Fail(it.message)
                Log.e("zqr", "_myDiyListBeanBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }



    /**
     * 获取版型分类列表APP（目前用于diy）  viewModel
     */
    var mGetStyleVersionTypeListBean: GetStyleVersionListByTypeBean? = null //通用一个bean类
    private val _getStyleVersionTypeListBeanState = MutableLiveData<GetStyleVersionTypeListBeanState>()
    val getStyleVersionTypeListBeanState: MutableLiveData<GetStyleVersionTypeListBeanState> get() = _getStyleVersionTypeListBeanState
    fun getStyleVersionTypeList(
        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?//上一级分类的id 可选
    ) {
        val disposable = clothesRepository.getStyleVersionTypeList(type,id)
            .subscribe({

                try {
                    it.list.forEach {
                        Log.e("zqr121210000", "_myDiyListBeanBean：${it.coverUrl}")
                        it.coverUrl=Singleton.getUrlX(it.coverUrl,true,100)
                        Log.e("zqr12121", "_myDiyListBeanBean：${it.coverUrl}")
                    }
                }catch (e:Exception){
                    Log.e("zqr", "_myDiyListBeanBeanState请求成功 但链接鉴权有错：$e")
                }

                mGetStyleVersionTypeListBean= it
                _getStyleVersionTypeListBeanState.value = GetStyleVersionTypeListBeanState.Success
                Log.e("zqr", "_getStyleVersionTypeListBeanState请求成功：$it")
            }, {

                _getStyleVersionTypeListBeanState.value = GetStyleVersionTypeListBeanState.Fail(it.message)
                Log.e("zqr", "_getStyleVersionTypeListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 获取版型分类列表APP（目前用于diy）  viewModel
     */
    var mGetStyleVersionTypeListBeanLeft: GetStyleVersionListByTypeBean? = null //通用一个bean类
    private val _getStyleVersionTypeListBeanStateLeft = MutableLiveData<GetStyleVersionTypeListBeanStateLeft>()
    val getStyleVersionTypeListBeanStateLeft: MutableLiveData<GetStyleVersionTypeListBeanStateLeft> get() = _getStyleVersionTypeListBeanStateLeft
    fun getStyleVersionTypeListLeft(
        type: Long,//1-一级分类，2-二级分类，3-三级分类
        id: Long?//上一级分类的id 可选
    ) {
        val disposable = clothesRepository.getStyleVersionTypeList(type,id)
            .subscribe({

                try {
                    it.list.forEach {
                        Log.e("zqr121210000", "_myDiyListBeanBean：${it.coverUrl}")
                        it.coverUrl=Singleton.getUrlX(it.coverUrl,true,100)
                        Log.e("zqr12121", "_myDiyListBeanBean：${it.coverUrl}")
                    }
                }catch (e:Exception){
                    Log.e("zqr", "_myDiyListBeanBeanState请求成功 但链接鉴权有错：$e")
                }

                mGetStyleVersionTypeListBeanLeft= it
                _getStyleVersionTypeListBeanStateLeft.value = GetStyleVersionTypeListBeanStateLeft.Success
                Log.e("zqr", "_getStyleVersionTypeListBeanState请求成功：$it")
            }, {

                _getStyleVersionTypeListBeanStateLeft.value = GetStyleVersionTypeListBeanStateLeft.Fail(it.message)
                Log.e("zqr", "_getStyleVersionTypeListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    /**
     * 重新上传制作服装单品  viewModel
     */
    var mRemakeClothesItemBean: CommonStateIntBean? = null
    private val _remakeClothesItemState = MutableLiveData<RemakeClothesItem>()
    val remakeClothesItemState: MutableLiveData<RemakeClothesItem> get() = _remakeClothesItemState
    fun remakeClothesItem(

        id: Long,//单品id

    ) {
        val disposable = clothesRepository.remakeClothesItem(id)
            .subscribe({
                mRemakeClothesItemBean= it
                _remakeClothesItemState.value = RemakeClothesItem.Success
                Log.e("zqr", "_remakeClothesItemState请求成功：$it")
            }, {

                _remakeClothesItemState.value = RemakeClothesItem.Fail(it.message)
                Log.e("zqr", "_remakeClothesItemState请求失败：$it")
            })
        addDisposable(disposable)
    }



}