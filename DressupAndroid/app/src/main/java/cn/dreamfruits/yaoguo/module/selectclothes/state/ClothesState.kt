package cn.dreamfruits.yaoguo.module.selectclothes.state

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean

sealed class ClothesCategoryState {

    data class Success(val clothesCategoryList: BasePageResult<ClothesCategoryBean>?) :
        ClothesCategoryState()

    class Fail(val msg: String?) : ClothesCategoryState()
}

sealed class ClothesState {

    data class Success(val clothesList: BasePageResult<ClothesBean>?) : ClothesState()

    class Fail(val msg: String?) : ClothesState()
}

sealed class ClothesItemState {

    data class Success(val clothesList: BasePageResult<ClothesBean>?,val type :Int) : ClothesItemState()

    class Fail(val msg: String?) : ClothesItemState()
}
sealed class SearchClothesItemState {

    data class Success(val clothesList: BasePageResult<ClothesBean>?) : SearchClothesItemState()

    class Fail(val msg: String?) : SearchClothesItemState()
}
sealed class FinleClothesItemState {

    data class Success(val clothesList: BasePageResult<ClothesBean>?) : FinleClothesItemState()

}
