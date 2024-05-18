package cn.dreamfruits.yaoguo.module.main.home.labeldetails


import android.os.Bundle
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity

class LabelDetailsActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_label_details

    private var labelType: String? = ""
    private var label: String? = ""
    private var mId: String? = ""
    override fun initView() {
        labelType = intent.getStringExtra("type")
        label = intent.getStringExtra("label")
        mId = intent.getStringExtra("id")

        //创建fragment
        val mWaterfallFragment = LabelDetailsFragment()
        val bundle = Bundle()
        bundle.putString("id", mId!!)
        mWaterfallFragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.label_fragment_container, mWaterfallFragment).commit()

        //activity调用fragment的方法
        val f=supportFragmentManager.findFragmentById(R.id.label_fragment_container) as LabelDetailsFragment
        f.testMethod()
    }

    override fun initData() {}
}