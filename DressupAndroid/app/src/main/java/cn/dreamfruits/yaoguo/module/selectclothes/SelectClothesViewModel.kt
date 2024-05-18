package cn.dreamfruits.yaoguo.module.selectclothes


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.home.state.HomeGetCareFeedListState
import cn.dreamfruits.yaoguo.module.main.home.state.StyleVersionListByTypeBeanState
import cn.dreamfruits.yaoguo.module.selectclothes.fragment.DataCallBack
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesCategoryState
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesItemState
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesState
import cn.dreamfruits.yaoguo.module.selectclothes.state.SearchClothesItemState
import cn.dreamfruits.yaoguo.repository.ClothesRepository
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesRootCategoryBean
import cn.dreamfruits.yaoguo.util.Tool
import com.blankj.utilcode.util.LogUtils
import org.koin.core.component.inject

class SelectClothesViewModel() : BaseViewModel() {

    private val clothesRepository by inject<ClothesRepository>()


    private val _clothesCategory = MutableLiveData<ClothesCategoryState>()
    val clothesCategory: LiveData<ClothesCategoryState> get() = _clothesCategory

    private val _clothesList = MutableLiveData<ClothesState>()
    val clothesList: LiveData<ClothesState> get() = _clothesList

    private val _clothesItem = MutableLiveData<ClothesItemState>()
    val clothesItem: LiveData<ClothesItemState> get() = _clothesItem

    private val _searchClothesItem = MutableLiveData<SearchClothesItemState>()
    val searchClothesItem: LiveData<SearchClothesItemState> get() = _searchClothesItem

    /**
     * 点击版型
     */
    private val _clothesCategoryId = MutableLiveData<Long>()
    val clothesCategoryId: LiveData<Long> get() = _clothesCategoryId

    /**
     * 添加或删除单品
     */
    private val _changeData = MutableLiveData<ChangeData>()
    val changeData: LiveData<ChangeData> get() = _changeData

    /**
     * 选择的单品
     */
    private val _selectedClothesList = mutableListOf<ClothesBean>()
    val selectedClothesList: List<ClothesBean> get() = _selectedClothesList


    //上一次获取版型分类的时间
    var mClothesCategoryLastTime: Long? = null

    //上一次获取单品列表的时间
    var mClothesListLastTime: Long? = null

    //上一次获取获取我的服装列表的时间
    var mClothesItemLastTime: Long = 0

    //上一次获取获取搜索服装列表的时间
    var mSearchClothesItemLastTime: Long = 0


//    //当前性别
//    var currentGender: Int = -1

//    //是否是重新选择
//    var reSelect: Boolean = false

    /**
     * 获取部位服装的分类
     */
    fun getRootClothesCategory(): MutableList<ClothesRootCategoryBean> {
        val nameArray = arrayOf("内搭", "上衣", "外套", "下装", "袜子", "鞋子")
        val positionArray = arrayOf(1, 2, 3, 4, 5, 6)

        val categoryList = mutableListOf<ClothesRootCategoryBean>()
        for (index in nameArray.indices) {
            categoryList.add(
                ClothesRootCategoryBean(
                    name = nameArray[index],
                    position = positionArray[index]
                )
            )
        }

        return categoryList
    }

    /**
     * 获取版型分类
     */
    fun getClothesCategoryList(
        position: Int,
        gender: Int,
        size: Int = 10,
    ) {
        val disposable =
            clothesRepository.getClothesCategory(position, gender, size, mClothesCategoryLastTime)
                .subscribe({
                    mClothesCategoryLastTime = it.lastTime
                    _clothesCategory.value =
                        ClothesCategoryState.Success(it)
                }, {
                    _clothesCategory.value = ClothesCategoryState.Fail(it.message)
                })

        addDisposable(disposable)
    }


    /**
     * 获取单品列表
     */
    fun getClothesList(cvId: Long, size: Int = 9) {

        val disposable = clothesRepository.getClothesList(cvId, size, mClothesListLastTime)
            .subscribe({
                mClothesListLastTime = it.lastTime
                _clothesList.value = ClothesState.Success(it)
            }, {
                _clothesList.value = ClothesState.Fail(it.message)
            })

        addDisposable(disposable)
    }

    /**
     * 设置选择的版型id
     */
    fun setClothesCategoryId(id: Long) {
        _clothesCategoryId.value = id
    }

    /**
     * 选择单品
     */
    fun select(clothesBean: ClothesBean) {
//        if (!_selectedClothesList.contains(clothesBean)) {
            var item = _selectedClothesList.find { it.id == clothesBean.id }

            if (item == null) {
                _selectedClothesList.add(clothesBean)
                _changeData.value = ChangeData(2, clothesBean)
            }
//        }
    }
    fun selectRest(list: List<ClothesBean>) {
        if (list.isEmpty()){
            _selectedClothesList.clear()
            _changeData.value = ChangeData(2, null)
            return
        }

        _selectedClothesList.clear()
        _selectedClothesList.addAll(list)
        _changeData.value = ChangeData(2, list[0])
//        if (!_selectedClothesList.contains(clothesBean)) {
//            var item = _selectedClothesList.find { it.id == clothesBean.id }
//
//            if (item == null) {
//                _selectedClothesList.add(clothesBean)
//                _changeData.value = ChangeData(2, clothesBean)
//            }
//        }
    }

    /**
     * 取消选择单品
     * 529965977888640
     *
     * 529960452400224
     */
    fun cancel(clothesBean: ClothesBean) {
//        if (_selectedClothesList.contains(clothesBean)) {
            var item = _selectedClothesList.find { it.id == clothesBean.id }

            if (item != null) {
                _selectedClothesList.remove(item)
                _changeData.value = ChangeData(1, clothesBean)
            }
//        }
    }


//    /**
//     *
//     */
//    fun getMyClothesItem(type: Int, size: Int = 9) {
//        val disposable = clothesRepository.getMyClothesItem(size, type, mClothesItemLastTime)
//            .subscribe({
//                mClothesListLastTime = it.lastTime
//                _clothesItem.value = ClothesItemState.Success(it, type)
//            }, {
//                _clothesItem.value = ClothesItemState.Fail(it.message)
//            })
//
//        addDisposable(disposable)
//    }

    /**
     *搜素服装列表
     */
    fun searchClothesItem(key: String, page: Int, size: Int = 9) {
        val disposable =
            clothesRepository.searchClothesItem(key, page, size, mSearchClothesItemLastTime)
                .subscribe({
                    mSearchClothesItemLastTime = it.lastTime
                    _searchClothesItem.value = SearchClothesItemState.Success(it)
                }, {
                    _searchClothesItem.value = SearchClothesItemState.Fail(it.message)
                })

        addDisposable(disposable)
    }


}

/**
 * 改变的单品
 * @param type 1= 删除 2 =添加
 * @param data 单品
 */
data class ChangeData(
    val type: Int,
    val data: ClothesBean?,
)

