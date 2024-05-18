package cn.dreamfruits.yaoguo.module.locationdetail

import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.repository.bean.editprofile.ProfileCurrentLocalBean
import cn.dreamfruits.yaoguo.repository.bean.editprofile.ProfileLocationBean
import cn.dreamfruits.yaoguo.util.AMapLocationHelper
import cn.dreamfruits.yaoguo.util.LocationState
import com.blankj.utilcode.util.GsonUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 编辑地区详情页面
 */
class EditLocationDetailActivity : BaseActivity() {

    private lateinit var mCancel: TextView
    private lateinit var mLocationDetailRecycler: RecyclerView
    private var mAdapter: LocationDetailAdapter? = null


    private var mLocationList: MutableList<Any>? = null

    private var currentLocalBean: ProfileCurrentLocalBean? = null
    private var mCountry: ProfileLocationBean.Country? = null
    private var mProvince: ProfileLocationBean.Province? = null
   // private var mCity: ProfileLocationBean.City? = null


    private var mCityStr: String? = null
    private var mProvinceStr: String? = null
    private var mCountryStr: String? = null


    override fun onCreateBefore() {

    }

    override fun initStatus() {
        immersionBar {
            statusBarDarkFont(true)
            fitsSystemWindows(true)
        }
    }


    override fun layoutResId(): Int {
        return R.layout.activity_edit_location_detail
    }

    override fun initView() {
        findViewById<ImageView>(R.id.iv_back).visibility = View.GONE
        mLocationDetailRecycler = findViewById(R.id.location_detail_recycler_view)
        mCancel = findViewById(R.id.tv_title_left)
        findViewById<TextView>(R.id.tv_title).text = "地区"

        mCancel.visibility = View.VISIBLE
        mCancel.setOnClickListener {
            finish()
        }

        mAdapter = LocationDetailAdapter()
        mAdapter?.setOnItemClickListener(object : LocationDetailAdapter.OnItemClickListener {

            override fun next(data: Any) {
                //获取对应省份数据
                if (data is ProfileLocationBean.Country) {

                    val provinceList = data.administrative_area
                    //设置已选地区
                    val element = provinceList.findLast { it.name == mProvinceStr }
                    element?.let {
                        val index = provinceList.lastIndexOf(element)
                        provinceList.add(0,provinceList.removeAt(index))
                        provinceList.first().isChose = true
                    }

                    provinceList.first().firstItem = true
                    provinceList.last().lastItem = true
                    mCountry = data

                    mAdapter?.setData(provinceList.toMutableList())


                 //获取对应城市数据
                } else if (data is ProfileLocationBean.Province) {

                    val cityList = data.sub_administrative_area
                    //设置已选地区
                    val element = cityList.findLast { it.name == mCityStr }
                    element?.let {
                        val index = cityList.lastIndexOf(element)
                        cityList.add(0,cityList.removeAt(index))
                        cityList.first().isChose = true
                    }

                    cityList.first().firstItem = true
                    cityList.last().lastItem = true

                    mProvince = data
                    mAdapter?.setData(cityList.toMutableList())
                }
            }

            override fun chose(country: String?, province: String?, city: String?) {

                val data = Intent()
                data.putExtra("key_city",city)
                data.putExtra("key_province",province)
                data.putExtra("key_country",country)
                this@EditLocationDetailActivity.setResult(200,data)
                this@EditLocationDetailActivity.finish()
            }
        })
        mLocationDetailRecycler.layoutManager = LinearLayoutManager(this)
        mLocationDetailRecycler.adapter = mAdapter


        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    AMapLocationHelper.instance.locationState.observe(this){
                        when(it){
                            //定位成功
                            is LocationState.LocationSuccess ->{

                                it.locationBean?.let { locationBean ->

                                   currentLocalBean?.apply {
                                       country = locationBean.country
                                       //去除位置后面的 省
                                       province =  locationBean.province.filterIndexed { index, c ->
                                           index < locationBean.province.length - 1 && c != '省'
                                       }
                                       //去除位置后面的 市
                                       city = locationBean.city.filterIndexed { index, c ->
                                           index < locationBean.city.length - 1 && c != '市'
                                       }
                                       isError = false
                                   }
                                    //更新位置信息
                                    mAdapter?.notifyItemChanged(0)
                                }
                            }
                            else -> {}
                        }
                    }
                    AMapLocationHelper.instance.location()
                } else {

                }
            }

    }

    override fun initData() {

        lifecycleScope.launch {
            loadLocationData()
        }

    }

    override fun onBackPressed() {
        if (mCountry != null) {

            if (mProvince != null) {
                mAdapter?.setData(mCountry!!.administrative_area.toMutableList())
                mProvince = null
                return
            }
            mAdapter?.setData(mLocationList!!)
            mCountry = null

        } else {
            super.onBackPressed()
        }
    }

    /**
     * 加载所有国家数据
     */
    private suspend fun loadLocationData() {
        withContext(Dispatchers.IO) {
            val open = assets.open("city_info_zh_cn.txt")
            var str = ""
            open.bufferedReader().use {
                str = it.readText()
            }
            val result = GsonUtils.fromJson(str, ProfileLocationBean.Result::class.java)

            mLocationList = mutableListOf()
            currentLocalBean = ProfileCurrentLocalBean()
            mLocationList?.add(currentLocalBean!!)

            //找到已选国家名称
            val country = result.data.country.findLast { it.name == mCountryStr }
            country?.let {
                val index = result.data.country.lastIndexOf(country)
                //将该地址移动到首位
                mLocationList?.add(result.data.country.removeAt(index))
                (mLocationList as? MutableList<ProfileLocationBean.Country>)?.get(1)?.isChose = true
            }

            mLocationList?.addAll(result.data.country)
            val list = mLocationList as? MutableList<ProfileLocationBean.Country>
            list?.let {
                it[1].firstItem = true
                it.last().lastItem = true
            }

            withContext(Dispatchers.Main) {

                mAdapter?.setData(mLocationList!!)
            }
        }
    }


}