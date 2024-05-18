package cn.dreamfruits.yaoguo.repository.bean.location

import com.amap.api.location.AMapLocation

/**
 * 定位结果的bean
 */
class LocationBean :AMapLocation{

    constructor(s:String) : super(s)

}