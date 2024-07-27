package com.example.zrwenxue.moudel.main.center.zr


import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.center.zr.dapter.WaterfallAdapter
import com.example.zrwenxue.moudel.main.center.zr.dapter.WaterfallBean
import com.example.zrwenxue.moudel.main.center.zr.database.MyDatabaseHelper
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
                //长按监听
                replayPopup(position,MyStatic.getBase64Bitmap(txt))
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
//            Log.e("bitmap_key212","bitmap_key:=====  "+it[0])   介绍|画作名称|画作作者|持有人|持有人密码|售卖次数|历史最高价|创建时间|价值
            tempList.add(WaterfallBean(
                ""+it[0],
                "",
                ""+it[1],
                ""+it[2].split("|")[0],
                it[2].split("|")[7].toLong(),
                ""+it[2].split("|")[3],
                it[2].split("|")[6].toDouble(),
                it[2].split("|")[5].toInt(),
                it[2].split("|")[8].toDouble(),
                MyStatic.getBase64Bitmap(it[3])))
        }
        return tempList
    }



    /**
     * 2.举报子级弹窗
     */
    private var replayPopupWindow: PopupWindow? = null
    private fun replayPopup(position:Int,bitmap: Bitmap) {


        val inflateView: View = layoutInflater.inflate(R.layout.dialog_comment_all, null)


        val wallpaperManager: WallpaperManager? = WallpaperManager.getInstance(requireActivity())



        val breakTheLaw = inflateView.findViewById<TextView>(R.id.dialog_comment_break_the_law)//违法
        breakTheLaw.setOnClickListener {
            if (wallpaperManager != null) {
                Single.setWallpaper(requireActivity(),wallpaperManager,bitmap)
                MyStatic.showToast(requireActivity(),"设置成功")
            }else{
                MyStatic.showToast(requireActivity(),"未知错误")
            }

            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }

        }
        val withPornographicContent =
            inflateView.findViewById<TextView>(R.id.dialog_comment_with_pornographic_content)//涉黄
        withPornographicContent.setOnClickListener {
            if (wallpaperManager != null) {
                Single.setScreensaver(requireActivity(),wallpaperManager,bitmap)
                MyStatic.showToast(requireActivity(),"设置成功")
            }else{
                MyStatic.showToast(requireActivity(),"未知错误")
            }

            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }

        }
        val personalAttack =
            inflateView.findViewById<TextView>(R.id.dialog_comment_personal_attack)//人身攻击
        personalAttack.setOnClickListener {
            if (wallpaperManager != null) {
                Single.setWallpaper(requireActivity(),wallpaperManager,bitmap)
                Single.setScreensaver(requireActivity(),wallpaperManager,bitmap)
                MyStatic.showToast(requireActivity(),"设置成功")
            }else{
                MyStatic.showToast(requireActivity(),"未知错误")
            }

            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }

        }
        val rumourFraud =
            inflateView.findViewById<TextView>(R.id.dialog_comment_rumour_fraud)//谣言 欺诈
        rumourFraud.setOnClickListener {

            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }

            //确认删除
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

        val cancel = inflateView.findViewById<TextView>(R.id.dialog_comment_cancel)//取消
        cancel.setOnClickListener {
            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }
        }

        /**
         * pop实例
         */
        replayPopupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //动画
        replayPopupWindow!!.animationStyle = R.style.BottomDialogAnimation
        replayPopupWindow?.setOnDismissListener {
            Single.bgAlpha(requireActivity(), 1f) //恢复透明度
        }
        if (replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            replayPopupWindow!!.dismiss()
        } else {
            replayPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            replayPopupWindow!!.isOutsideTouchable = true
            replayPopupWindow!!.isTouchable = true
            replayPopupWindow!!.isFocusable = true
            replayPopupWindow!!.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
            Single.bgAlpha(requireActivity(), 0.5f) //设置透明度0.5
        }
    }

    /**
     * 3.消灭弹窗
     */
    override fun onPause() {
        super.onPause()
        if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            replayPopupWindow!!.dismiss()
        }
    }

}