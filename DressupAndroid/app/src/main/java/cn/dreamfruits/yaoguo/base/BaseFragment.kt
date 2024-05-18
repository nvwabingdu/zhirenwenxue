package cn.dreamfruits.yaoguo.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import cn.dreamfruits.yaoguo.constants.MMKVConstants.Companion.initData
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesViewModel

abstract class BaseFragment : Fragment() {
    private var isVis = false
    private var isInitView = false
    private var isFirstLoad = true

    var convertView: View? = null

    var mContext: Activity? = null

    /**
     * 视图是否加载完毕
     */
    private var isLoad: Boolean = false
    private var isLoadView: Boolean = false

    /**
     * 加载页面布局ID
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化控件
     */
    protected abstract fun initView(convertView: View?, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 获取网络数据
     */
    protected abstract fun getData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        if (convertView == null) {
            convertView = inflater.inflate(getLayoutId(), container, false)

            mContext = activity
        }

        if (convertView!!.parent != null) {
            val parent: ViewGroup = convertView!!.parent!! as ViewGroup
            parent.removeView(convertView)
        }

        return convertView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLoadView) {
            isLoadView = true
            mContext = activity
            initView(view, savedInstanceState)
            isInitView = true
            initData()
        }
    }


    @CallSuper
    override fun onResume() {
        super.onResume()
        tryLoad()
    }

    open fun tryLoad() {
        if (!isLoad) {
            getData()
            isLoad = true
        }
    }

}