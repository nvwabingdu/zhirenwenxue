package cn.dreamfruits.yaoguo.repository.bean.editprofile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 个人资料-位置
 */
@Parcelize
class ProfileLocationBean(
    val country: MutableList<Country>
):Parcelable{

    @Parcelize
    class Result(
        val data: ProfileLocationBean
    ) : Parcelable

    /**
     * 国家
     */
    @Parcelize
    data class Country(
        var administrative_area: MutableList<Province>,
        var firstItem: Boolean,
        var isChose: Boolean,
        var isSelect: Boolean,
        var lastItem: Boolean,
        var name: String,
    ):Parcelable

    /**
     * 省
     */
    @Parcelize
    data class Province(
        var firstItem: Boolean,
        var isChose: Boolean,
        var isSelect: Boolean,
        var lastItem: Boolean,
        var name: String,
        var sub_administrative_area: MutableList<City>,
    ) : Parcelable

    /**
     * 市
     */
    @Parcelize
    data class City(
        var firstItem: Boolean,
        var isChose: Boolean,
        var isSelect: Boolean,
        var lastItem: Boolean,
        var name: String,
    ) : Parcelable
}





