package com.example.zrwenxue.moudel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 * @Author qiwangi
 * @Date 2023/8/17
 * @TIME 08:53
 */
abstract class BaseFragment : Fragment(){

     var mContext: Context? = null
     var rootView: View? = null
    /**
     * 1.当fragment与activity发生关联时调用
     * @param context  与之相关联的activity
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    /**
     * 2.绑定布局
     * @return
     */
    protected abstract fun setLayout(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         rootView = inflater.inflate(setLayout(), container, false)

        return rootView
    }




    /**
     * 3.初始化组件
     */
    protected abstract fun initView()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 4.简化findViewById 一般不用这种方法 直接用代码批量生产
     * @param resId
     * @param <T>
     * @return
    </T> */
    open fun <T : View?> fvbi(resId: Int): T {
        return view!!.findViewById<View>(resId) as T
    }

}