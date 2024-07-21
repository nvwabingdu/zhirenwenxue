package com.example.zrwenxue.moudel.main.center.crypt


import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.center.crypt.dapter.WaterfallAdapter
import com.example.zrwenxue.moudel.main.center.crypt.dapter.WaterfallBean
import com.example.zrwenxue.moudel.main.center.crypt.database.MyDatabaseHelper
import com.example.zrwenxue.moudel.main.word.MyStatic

class C0Fragment : BaseFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_c0
    }

    private var mRecyclerview: RecyclerView? = null
    private var mStaggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var mAdapter: WaterfallAdapter? = null
    private var dbHelper: MyDatabaseHelper? = null
    private var mList: MutableList<WaterfallBean>? = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        //清空集合
        mList!!.clear()
        //数据库
        dbHelper = MyDatabaseHelper(requireActivity())
        //从0开始装20条
        mList=getSplitDate(0)

        //适配器
        mRecyclerview = fvbi(R.id.recyclerView)
        mStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mStaggeredGridLayoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        mRecyclerview!!.layoutManager = mStaggeredGridLayoutManager
        //2.防止第一页出现空白 3.使用notifyItemRangeChanged
        mRecyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mStaggeredGridLayoutManager!!.invalidateSpanAssignments()
            }
        })
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
                        MyStatic.deleteData(dbHelper, mList!![position].id);// 删除数据
                        mAdapter!!.delItem(position)//删除数据
                        MyStatic.showToast(requireActivity(), "已删除")
                    },
                    onCancel = {

                    }
                )
            }
            override fun onPreload() {
                //预加载
                mRecyclerview!!.postDelayed({
                    //预加载
                    mAdapter!!.setData(getSplitDate(mList!!.size), false)//设置数据 更新适配器
                }, 100) // 延迟 100 毫秒
            }
        })
    }

    /**
     * 给定
     */
    fun  getSplitDate(index:Int): MutableList<WaterfallBean> {
        //一共多少数据
        var endNum=MyStatic.getDataCount(dbHelper)
        if (index+20<endNum){
            endNum=index+20
        }
        // 查询数据 某两个角标之间的数据
        val splitData = MyStatic.getAllData(dbHelper,index,endNum)

        //将此数据装入集合
       val tempList: MutableList<WaterfallBean> = ArrayList()
        splitData.forEach {
//            Log.e("bitmap_key212","bitmap_key:=====  "+it[0])
            tempList.add(WaterfallBean(
                it[0],
                "",
                it[1],
                it[2],
                true,
                464L,
                "",
                0.0,
                MyStatic.getBase64Bitmap(it[3])))
        }
        return tempList
    }
}