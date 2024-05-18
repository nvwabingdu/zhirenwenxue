package cn.dreamfruits.yaoguo.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.application.AppLifeCycleImpl
import cn.dreamfruits.yaoguo.repository.bean.location.LocationBean
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * 高德定位
 */
class AMapLocationHelper {

    /**
     * 是否正在定位中
     */
    private var mIsLocationing = false

    /**
     * 上一次定位的时间
     */
    private var mLastTime: Long = 0

    /**
     * 重新定位的最小间隔时间，默认为5分钟
     */
    private val mReLocationMinTime = 1000 * 60 * 5

    /**
     * 上一次的定位结果
     */
    private var mLastResult: LocationBean? = null


    /**
     * 定位结果
     */
    private val _locationState = MutableLiveData<LocationState>()
    val locationState: LiveData<LocationState> get() = _locationState

    /**
     * 定位
     */
    fun location(
        isUseCache: Boolean = false,
        mode: AMapLocationClientOption.AMapLocationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
    ) {
        //是否在定位中
        if (mIsLocationing) {
            return
        }
        //使用缓存数据
        if (isUseCache && mLastResult != null && (System.currentTimeMillis() - mLastTime < mReLocationMinTime)) {
            _locationState.value = LocationState.LocationSuccess(mLastResult)
        }

        AMapLocationClient.updatePrivacyAgree(AppLifeCycleImpl.INSTANCE,true)
        AMapLocationClient.updatePrivacyShow(AppLifeCycleImpl.INSTANCE,true,true)
        val client = initAMapLocationClient(AppLifeCycleImpl.INSTANCE, mode)
        mIsLocationing = true
        client.disableBackgroundLocation(true)
        client.startLocation()

        val listener = AMapLocationListener { aMapLocation ->
            aMapLocation?.let {
                printLogInfo(it)
            }
            mIsLocationing = false
            //如果定位结果为null或者errorCode不为0和12。
            if (aMapLocation == null || aMapLocation.errorCode != 0 && aMapLocation.errorCode != 12) {
                //定位失败
                _locationState.value = LocationState.LocationFailed
                return@AMapLocationListener
            }
            //定位成功
            if (aMapLocation.errorCode == 0) {
                mLastTime = System.currentTimeMillis()
                val result = convertAMapLocationToBean(aMapLocation)
                _locationState.value = LocationState.LocationSuccess(result)
                return@AMapLocationListener
            }
            //缺少定位权限
            if (aMapLocation.errorCode == 12) {
                _locationState.value = LocationState.LocationNoPermission
            }
        }
        client.setLocationListener(listener)
    }

    /**
     * 初始化高德地图的定位Client
     */
    private fun initAMapLocationClient(
        context: Context,
        mode: AMapLocationClientOption.AMapLocationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
    ): AMapLocationClient {
        val client = AMapLocationClient(context)
        val mapLocationClientOption = AMapLocationClientOption()

        //设置定位超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mapLocationClientOption.httpTimeOut = 8000
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mapLocationClientOption.locationMode = mode
        mapLocationClientOption.isOnceLocation = true
        //获取最近3s内精度最高的一次定位结果：
        mapLocationClientOption.isOnceLocationLatest = true
        //打开缓存机制
        mapLocationClientOption.isLocationCacheEnable = true
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mapLocationClientOption.isMockEnable = false
        //给定位客户端对象设置定位参数
        client.setLocationOption(mapLocationClientOption)
        return client
    }

    /**
     * 数据转换
     */
    private fun convertAMapLocationToBean(aMapLocation: AMapLocation): LocationBean {
        // TODO: 2021/4/27 0027 有待完善
        if (aMapLocation.provider == null) {
            return LocationBean("null")
        }
        val mapLocation = LocationBean(aMapLocation.provider!!)
        mapLocation.errorCode = aMapLocation.errorCode
        mapLocation.satellites = aMapLocation.satellites
        mapLocation.locationType = aMapLocation.locationType
        mapLocation.gpsAccuracyStatus = aMapLocation.gpsAccuracyStatus
        mapLocation.time = aMapLocation.time
        mapLocation.elapsedRealtimeNanos = aMapLocation.elapsedRealtimeNanos
        mapLocation.speed = aMapLocation.speed
        mapLocation.accuracy = aMapLocation.accuracy
        mapLocation.bearing = aMapLocation.bearing
        mapLocation.longitude = aMapLocation.longitude
        mapLocation.latitude = aMapLocation.latitude
        mapLocation.altitude = aMapLocation.altitude
        mapLocation.isMock = aMapLocation.isMock
        mapLocation.isOffset = aMapLocation.isOffset
        mapLocation.isFixLastLocation = aMapLocation.isFixLastLocation
        mapLocation.floor = aMapLocation.floor
        mapLocation.cityCode = aMapLocation.cityCode
        mapLocation.city = aMapLocation.city
        mapLocation.adCode = aMapLocation.adCode
        mapLocation.aoiName = aMapLocation.aoiName
        mapLocation.buildingId = aMapLocation.buildingId
        mapLocation.country = aMapLocation.country
        mapLocation.description = aMapLocation.description
        mapLocation.district = aMapLocation.district
        mapLocation.errorInfo = aMapLocation.errorInfo
        mapLocation.locationDetail = aMapLocation.locationDetail
        mapLocation.address = aMapLocation.address
        mapLocation.province = aMapLocation.province
        mapLocation.poiName = aMapLocation.poiName
        mapLocation.street = aMapLocation.street
        return mapLocation
    }


    /**
     * 打印定位信息
     */
    private fun printLogInfo(aMapLocation: AMapLocation) {

        val locationType = when (aMapLocation.locationType) {
            AMapLocation.LOCATION_TYPE_GPS -> "定位结果:GPS定位结果,通过设备GPS定位模块返回的定位结果"
            AMapLocation.LOCATION_TYPE_SAME_REQ -> "定位结果:前次定位结果,网络定位请求低于1秒、或两次定位之间设备位置变化非常小时返回，设备位移通过传感器感知"
            AMapLocation.LOCATION_TYPE_FAST -> "定位结果:已合并到AMapLocation.LOCATION_TYPE_SAME_REQ"
            AMapLocation.LOCATION_TYPE_FIX_CACHE -> "定位结果:缓存定位结果,返回一段时间前设备在相同的环境中缓存下来的网络定位结果，节省无必要的设备定位消耗"
            AMapLocation.LOCATION_TYPE_WIFI -> "定位结果:Wifi定位结果,属于网络定位，定位精度相对基站定位会更好"
            AMapLocation.LOCATION_TYPE_CELL -> "定位结果:基站定位结果,属于网络定位"
            AMapLocation.LOCATION_TYPE_OFFLINE -> "定位结果:离线定位结果,"
            AMapLocation.LOCATION_TYPE_LAST_LOCATION_CACHE -> "定位结果:最后位置缓存"
            else -> "定位结果:其他"
        }
        val errorInfo = when (aMapLocation.errorCode) {
            0 -> "定位成功。"
            1 -> "一些重要参数为空，如context"
            2 -> "定位失败，由于仅扫描到单个wifi，且没有基站信息。"
            3 -> "获取到的请求参数为空，可能获取过程中出现异常"
            4 -> "请求服务器过程中的异常，多为网络情况差，链路不通导致"
            5 -> "请求被恶意劫持，定位结果解析失败"
            6 -> "定位服务返回定位失败"
            7 -> "KEY鉴权失败"
            8 -> "Android exception常规错误"
            9 -> "定位初始化时出现异常"
            10 -> "定位客户端启动失败"
            11 -> "定位时的基站信息错误"
            12 -> "缺少定位权限"
            13 -> "定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用"
            14 -> "GPS定位失败，由于设备当前GPS状态差"
            15 -> "定位结果被模拟导致定位失败"
            16 -> "当前POI检索条件、行政区划检索条件下，无可用地理围栏"
            18 -> "定位失败，由于手机WIFI功能被关闭同时设置为飞行模式"
            19 -> "定位失败，由于手机没插sim卡且WIFI功能被关闭"
            else -> "未知错误"
        }
        Log.i("AimyLocation", "Type:$locationType content:$errorInfo")
    }


    companion object {

        @JvmStatic
        val instance by lazy {
            AMapLocationHelper()
        }
    }

}


/**
 * 定位状态
 */
sealed class LocationState {
    /**
     * 定位失败
     */
    object LocationFailed : LocationState()

    /**
     * 没有定位权限
     */
    object LocationNoPermission : LocationState()

    /**
     * 定位成功
     */
    data class LocationSuccess(val locationBean: LocationBean?) : LocationState()
}