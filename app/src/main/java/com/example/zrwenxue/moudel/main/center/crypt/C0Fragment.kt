package com.example.zrwenxue.moudel.main.center.crypt


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.center.crypt.dapter.WaterfallAdapter
import com.example.zrwenxue.moudel.main.center.crypt.dapter.WaterfallBean
import com.example.zrwenxue.moudel.main.word.MyStatic

class C0Fragment : BaseFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_c0
    }

    private var mRecyclerview: RecyclerView? = null
    private var mStaggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var mAdapter: WaterfallAdapter? = null
    private var mList: MutableList<WaterfallBean>? = ArrayList()//推荐页面的feed列表
    override fun initView() {


        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val str = sharedPreferences.getString("bitmap_key", null)

        if (str!=null){
            str.split("<ycrg>").forEach {
                if (it!=""){
                    mList!!.add(WaterfallBean(
                        MyStatic.getBase64Bitmap(it)
                    ))
                }
            }
        }
        mRecyclerview = fvbi(R.id.recyclerView)
        //适配器
        mStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mStaggeredGridLayoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        mRecyclerview!!.layoutManager = mStaggeredGridLayoutManager
        mAdapter = WaterfallAdapter(requireActivity(), 20.0f, mList!!)
        mRecyclerview!!.adapter = mAdapter

        mAdapter!!.setWaterfallAdapterListener(object : WaterfallAdapter.WaterfallAdapterInterface{
            override fun onCallBack(position: Int, txt: String) {

               Single.showConfirmationDialog(
                    context = requireActivity(),
                    title = "删除文件",
                    message = "您确定要删除这个画作吗?删除之后不可找回",
                    onConfirm = {
                        // 执行确认操作

                        Log.e("bitmap_key","bitmap_key:  "+str!!.length)

//                        editor.putString(
//                            "bitmap_key",
//                            str!!.replace("$txt<ycrg>", "")
//                        )


                        Log.e("bitmap_key","bitmap_key:  "+str!!.replace("$txt<ycrg>", "").length)
                        Log.e("bitmap_key","bitmap_key:  "+txt.length)

                        editor.apply()
                        MyStatic.showToast(requireActivity(), "已删除")


                    },
                    onCancel = {
                        // 执行取消操作
                        // 什么也不做
                    }
                )






            }
        })
    }




}