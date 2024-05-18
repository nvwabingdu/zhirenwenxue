package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.similarbanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.SingleSimilarWearAdapter
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.util.Singleton

/**
 * 相似推荐用于banner的 其中一个fragment  这个fragment里面有一个recyclerview 有六张图片
 */
class SimilarBannerFragment : Fragment() {
    private var mSimilarWearList:MutableList<GetSimilarRecommendBean.Item>? = ArrayList()
    private var dSimilarWearRecyclerView: RecyclerView? = null
    private var mSimilarWearAdapter: SingleSimilarWearAdapter? = null

    private var key:Int=0//用于判断是哪个fragment

    private var view: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.home_fragment_similar_banner, container, false)

        key=arguments!!.getInt("key",0)//取得viewpager的当前页数

        Log.e("mSimilarWearList","key=="+key.toString())

        when(key){
            0->{
                if (Singleton.mSimilarWearList!=null&&Singleton.mSimilarWearList!!.size>=6){
                    mSimilarWearList=Singleton.mSimilarWearList!!.subList(0,6)
                    Log.e("mSimilarWearList","mSimilarWearList.size=="+mSimilarWearList!!.size)
                }
            }
            1->{
                if (Singleton.mSimilarWearList!=null&&Singleton.mSimilarWearList!!.size>=12){
                    mSimilarWearList=Singleton.mSimilarWearList!!.subList(6,12)
                    Log.e("mSimilarWearList","mSimilarWearList2.size=="+mSimilarWearList!!.size)
                }
            }
            2->{
                if (Singleton.mSimilarWearList!=null&&Singleton.mSimilarWearList!!.size>=18){
                    mSimilarWearList=Singleton.mSimilarWearList!!.subList(12,18)
                }
            }
            3->{
                if (Singleton.mSimilarWearList!=null&&Singleton.mSimilarWearList!!.size>=24){
                    mSimilarWearList=Singleton.mSimilarWearList!!.subList(18,24)
                }
            }
            4->{
                if (Singleton.mSimilarWearList!=null&&Singleton.mSimilarWearList!!.size>=30){
                    mSimilarWearList=Singleton.mSimilarWearList!!.subList(24,30)
                }
            }
        }

        dSimilarWearRecyclerView =view!!.findViewById(R.id.d_similar_recyclerView)//图片
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        dSimilarWearRecyclerView!!.layoutManager = layoutManager
        mSimilarWearAdapter = SingleSimilarWearAdapter(requireActivity(),mSimilarWearList!!,111.0f,375.0f)
        dSimilarWearRecyclerView!!.adapter = mSimilarWearAdapter

        return view
    }

}