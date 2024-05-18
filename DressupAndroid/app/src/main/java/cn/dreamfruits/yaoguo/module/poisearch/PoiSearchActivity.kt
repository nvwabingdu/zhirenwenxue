package cn.dreamfruits.yaoguo.module.poisearch

import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.poisearch.adapter.LocationListAdapter
import cn.dreamfruits.yaoguo.repository.bean.location.PoiItemBean
import cn.dreamfruits.yaoguo.util.AMapLocationHelper
import cn.dreamfruits.yaoguo.util.LocationState
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import com.blankj.utilcode.util.ToastUtils


/**
 * 周边位置搜索页面
 */
class PoiSearchActivity : BaseActivity() {

    private lateinit var mAddressEt: EditText
    private lateinit var mCancelTv: TextView
    private lateinit var mLocationRv: RecyclerView


    private var mAdapter: LocationListAdapter? = null

    private var mCachePoiItemList: List<PoiItemBean>? =null

    override fun layoutResId(): Int = R.layout.activity_poi_search


    override fun initView() {
        mAddressEt = findViewById(R.id.et_address)
        mCancelTv = findViewById(R.id.tv_cancel)
        mLocationRv = findViewById(R.id.rv_location)

        mCancelTv.setOnClickListener{
            finish()
        }

        mAdapter = LocationListAdapter()
        mAdapter?.setOnItemClickListener(object : LocationListAdapter.OnItemClickListener {
            override fun onLocationClick(poiItemBean: PoiItemBean) {
                val data = Intent()
                data.putExtra("PoiItemBean",poiItemBean)

                setResult(RouterConstants.FeedPublish.RESULT_CODE_POI_SEARCH,data)
                finish()

            }

            override fun onNoSelect() {

                setResult(RouterConstants.FeedPublish.RESULT_CODE_POI_SEARCH,null)
                finish()
            }
        })

        mLocationRv.layoutManager = LinearLayoutManager(this)
        mLocationRv.adapter = mAdapter


        mAddressEt.addTextChangedListener { text ->
            if (text.isNullOrBlank()) {
                mCachePoiItemList?.let { data ->
                    mAdapter?.setData(data)
                }
            }else{
                searchPoi(keyword = text.toString())
            }
        }

    }

    override fun initData() {
        AMapLocationHelper.instance.location()
        AMapLocationHelper.instance.locationState.observe(this) { locationState ->
            when (locationState) {
                is LocationState.LocationSuccess -> {
                    locationState.locationBean?.let {
                        searchPoi(
                            mLongitude = it.longitude,
                            mLatitude = it.latitude,
                            cityCode = it.cityCode
                        )
                    }
                }

                is LocationState.LocationFailed -> {
                    ToastUtils.showShort("定位失败")
                }
                else -> {}
            }
        }
    }


    /**
     * 搜索周边位置
     */
    private fun searchPoi(
        keyword: String = "",
        mLongitude: Double? = null,
        mLatitude: Double? = null,
        cityCode: String = ""
    ) {

        val query = PoiSearchV2.Query(keyword, "", cityCode)
        query.pageSize = 15
        query.pageNum = 1
        val poiSearch = PoiSearchV2(applicationContext, query)

        mLongitude?.let {
            poiSearch.bound = PoiSearchV2.SearchBound(LatLonPoint(mLongitude!!, mLatitude!!), 500)
        }

        poiSearch.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResultV2?, rCode: Int) {
                if (rCode == 1000) {
                    val list = result?.pois?.map {
                        PoiItemBean(
                            it.provinceCode,
                            it.provinceName,
                            it.cityCode,
                            it.cityName,
                            it.latLonPoint.longitude,
                            it.latLonPoint.latitude,
                            it.title,
                            it.snippet
                        )
                    }
                    list?.let { list ->
                        //将第一次周边位置缓存起来
                        if (mCachePoiItemList == null){
                            mCachePoiItemList = list
                        }
                        mAdapter?.setData(list)
                    }

                } else {
                    ToastUtils.showShort("搜索位置错误")
                }
            }

            override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {

            }
        })
        poiSearch.searchPOIAsyn()
    }


}