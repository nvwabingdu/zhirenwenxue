package com.example.zrwenxue.moudel.main.memory

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.word.MyStatic
import com.example.zrwenxue.moudel.main.word.Singleton
import com.example.zrwenxue.moudel.BaseFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.random.Random


class MemoryFragment : BaseFragment() {

    private var fileLines: MutableList<String>? = ArrayList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(MemoryViewModel::class.java)
    }

    companion object {
        fun newInstance() = MemoryFragment()
    }

    private lateinit var viewModel: MemoryViewModel

    override fun setLayout(): Int = R.layout.fragment_memory

    override fun initView() {
        getWordBeanList(activity!!)
        setView()
    }

    private var mark: TextView? = null
    private var markImgSound: ImageView? = null
    private var markExample: TextView? = null
    private var markRecyclerView01: RecyclerView? = null
    private var markDetails: TextView? = null
    private var s1: TextView? = null
    private var s2: TextView? = null
    private var s3: TextView? = null
    private var s4: TextView? = null
    private var s5: TextView? = null
    private var s6: TextView? = null
    private var s7: TextView? = null
    private var s8: TextView? = null
    private var s9: TextView? = null
    private var s10: TextView? = null
    private var s11: TextView? = null
    private var s12: TextView? = null
    private var s13: TextView? = null
    private var s14: TextView? = null
    private var s15: TextView? = null
    private var s16: TextView? = null
    private var s17: TextView? = null
    private var s18: TextView? = null
    private var s19: TextView? = null
    private var s20: TextView? = null
    private var s21: TextView? = null
    private var s22: TextView? = null
    private var s23: TextView? = null
    private var s24: TextView? = null
    private var s25: TextView? = null
    private var s26: TextView? = null
    private var s27: TextView? = null
    private var s28: TextView? = null
    private var s29: TextView? = null
    private var s30: TextView? = null
    private var s31: TextView? = null
    private var s32: TextView? = null
    private var s33: TextView? = null
    private var s34: TextView? = null
    private var s35: TextView? = null
    private var s36: TextView? = null
    private var s37: TextView? = null
    private var s38: TextView? = null
    private var s39: TextView? = null
    private var s40: TextView? = null
    private var s41: TextView? = null
    private var s42: TextView? = null
    private var s43: TextView? = null
    private var s44: TextView? = null
    private var s45: TextView? = null
    private var s46: TextView? = null
    private var s47: TextView? = null
    private var s48: TextView? = null
    private var idList:ArrayList<TextView>?=null

    private fun setView() {
        mark = rootView!!.findViewById( R.id.mark_)
        markImgSound = rootView!!.findViewById( R.id.mark_img_sound)
        markExample = rootView!!.findViewById( R.id.mark_example)
        markRecyclerView01 = rootView!!.findViewById( R.id.mark_RecyclerView01)
        markDetails = rootView!!.findViewById( R.id.mark_details)
        s1 = rootView!!.findViewById( R.id.s1)
        s2 = rootView!!.findViewById( R.id.s2)
        s3 = rootView!!.findViewById( R.id.s3)
        s4 = rootView!!.findViewById( R.id.s4)
        s5 = rootView!!.findViewById( R.id.s5)
        s6 = rootView!!.findViewById( R.id.s6)
        s7 = rootView!!.findViewById( R.id.s7)
        s8 = rootView!!.findViewById( R.id.s8)
        s9 = rootView!!.findViewById( R.id.s9)
        s10 = rootView!!.findViewById( R.id.s10)
        s11 = rootView!!.findViewById( R.id.s11)
        s12 = rootView!!.findViewById( R.id.s12)
        s13 = rootView!!.findViewById( R.id.s13)
        s14 = rootView!!.findViewById( R.id.s14)
        s15 = rootView!!.findViewById( R.id.s15)
        s16 = rootView!!.findViewById( R.id.s16)
        s17 = rootView!!.findViewById( R.id.s17)
        s18 = rootView!!.findViewById( R.id.s18)
        s19 = rootView!!.findViewById( R.id.s19)
        s20 = rootView!!.findViewById( R.id.s20)
        s21 = rootView!!.findViewById( R.id.s21)
        s22 = rootView!!.findViewById( R.id.s22)
        s23 = rootView!!.findViewById( R.id.s23)
        s24 = rootView!!.findViewById( R.id.s24)
        s25 = rootView!!.findViewById( R.id.s25)
        s26 = rootView!!.findViewById( R.id.s26)
        s27 = rootView!!.findViewById( R.id.s27)
        s28 = rootView!!.findViewById( R.id.s28)
        s29 = rootView!!.findViewById( R.id.s29)
        s30 = rootView!!.findViewById( R.id.s30)
        s31 = rootView!!.findViewById( R.id.s31)
        s32 = rootView!!.findViewById( R.id.s32)
        s33 = rootView!!.findViewById( R.id.s33)
        s34 = rootView!!.findViewById( R.id.s34)
        s35 = rootView!!.findViewById( R.id.s35)
        s36 = rootView!!.findViewById( R.id.s36)
        s37 = rootView!!.findViewById( R.id.s37)
        s38 = rootView!!.findViewById( R.id.s38)
        s39 = rootView!!.findViewById( R.id.s39)
        s40 = rootView!!.findViewById( R.id.s40)
        s41 = rootView!!.findViewById( R.id.s41)
        s42 = rootView!!.findViewById( R.id.s42)
        s43 = rootView!!.findViewById( R.id.s43)
        s44 = rootView!!.findViewById( R.id.s44)
        s45 = rootView!!.findViewById( R.id.s45)
        s46 = rootView!!.findViewById( R.id.s46)
        s47 = rootView!!.findViewById( R.id.s47)
        s48 = rootView!!.findViewById( R.id.s48)

        idList= ArrayList()
        idList!!.add(s1!!)
        idList!!.add(s2!!)
        idList!!.add(s3!!)
        idList!!.add(s4!!)
        idList!!.add(s5!!)
        idList!!.add(s6!!)
        idList!!.add(s7!!)
        idList!!.add(s8!!)
        idList!!.add(s9!!)
        idList!!.add(s10!!)
        idList!!.add(s11!!)
        idList!!.add(s12!!)
        idList!!.add(s13!!)
        idList!!.add(s14!!)
        idList!!.add(s15!!)
        idList!!.add(s16!!)
        idList!!.add(s17!!)
        idList!!.add(s18!!)
        idList!!.add(s19!!)
        idList!!.add(s20!!)
        idList!!.add(s21!!)
        idList!!.add(s22!!)
        idList!!.add(s23!!)
        idList!!.add(s24!!)
        idList!!.add(s25!!)
        idList!!.add(s26!!)
        idList!!.add(s27!!)
        idList!!.add(s28!!)
        idList!!.add(s29!!)
        idList!!.add(s30!!)
        idList!!.add(s31!!)
        idList!!.add(s32!!)
        idList!!.add(s33!!)
        idList!!.add(s34!!)
        idList!!.add(s35!!)
        idList!!.add(s36!!)
        idList!!.add(s37!!)
        idList!!.add(s38!!)
        idList!!.add(s39!!)
        idList!!.add(s40!!)
        idList!!.add(s41!!)
        idList!!.add(s42!!)
        idList!!.add(s43!!)
        idList!!.add(s44!!)
        idList!!.add(s45!!)
        idList!!.add(s46!!)
        idList!!.add(s47!!)
        idList!!.add(s48!!)

        //设置控件
        var tempTag=0
        idList!!.forEach {
            setClick(it, Singleton.getInstance().SoundMarkList[tempTag])
            tempTag++
        }

        //设置随机
        var random= Random.nextInt(48)
        setOne(idList!![random], Singleton.getInstance().SoundMarkList[random])
    }

    /**
     * 设置初始化
     */
    private fun setOne(mTv: TextView, bean: SoundMarkBean){
        mTv.text = bean.mark
        mark!!.text = bean.mark
        mark!!.setTextColor(Color.parseColor(com.example.zrtool.utils.ColorUtils.getRandomHexColor()))
        markExample!!.text = bean.markExample
        markDetails!!.text = bean.markPronunciationSkill
        //单词示例
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        markRecyclerView01!!.layoutManager = layoutManager
        val mAdapter = SoundMarkAdapter(requireActivity(), bean.markWorkList!!)
        markRecyclerView01!!.adapter = mAdapter
        //音频
        markImgSound!!.setOnClickListener {
            set(bean.markSoundPath)
        }
    }

    //设置音标item
    private fun setClick(mTv: TextView, bean: SoundMarkBean) {
        mTv.text = bean.mark
        mTv.setOnClickListener {
            mark!!.text = bean.mark
            mark!!.setTextColor(Color.parseColor(com.example.zrtool.utils.ColorUtils.getRandomHexColor()))
            markExample!!.text = bean.markExample
            markDetails!!.text = bean.markPronunciationSkill
            //单词示例
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            markRecyclerView01!!.layoutManager = layoutManager
            val mAdapter = SoundMarkAdapter(requireActivity(), bean.markWorkList!!)
            markRecyclerView01!!.adapter = mAdapter
            //音频
            markImgSound!!.setOnClickListener {
                set(bean.markSoundPath)
            }
        }
    }




    /**
     *播放逻辑
     */
    private var mediaPlayer: MediaPlayer? = null
    fun set(path: String) {
        // 初始化MediaPlayer
        mediaPlayer = MediaPlayer()
        try {
            // 获取AssetManager
            val assetManager: AssetManager = activity!!.getAssets()

            // 打开音频文件
            val descriptor = assetManager.openFd("soundmark/$path")

            // 设置MediaPlayer的数据源
            mediaPlayer!!.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )

            // 准备播放
            mediaPlayer!!.prepare()

            // 开始播放
            mediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // 释放MediaPlayer资源
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }



    /**
     * 把raw中的数据装入单例集合
     */
    @Throws(Exception::class)
    fun getWordBeanList(context: Context): java.util.ArrayList<SoundMarkBean>? {
        if (Singleton.getInstance().SoundMarkList.size != 0) {
            return Singleton.getInstance().SoundMarkList
        }
        val arrayList = java.util.ArrayList<SoundMarkBean>()
        //创建流 此处用本地文件的获取方式：mcontext.getResources().openRawResource(file)
        val bufferedReader = BufferedReader(
            InputStreamReader(
                context.resources.openRawResource( R.raw.soundmark),
                "UTF-8"
            )
        )
        //读取流
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            arrayList.add(
                SoundMarkBean(
                    MyStatic.getSubStr(line, "<1>", "<2>") + "",  //音标
                    MyStatic.getSubStr(line, "<2>", "<3>") + "",
                    MyStatic.getSubStr(line, "<3>", "<4>") + "",
                    changeToList(MyStatic.getSubStr(line, "<4>", "<5>") + ""),
                    MyStatic.getSubStr(line, "<5>", "<6>") + "",
                )
            )
        }
        bufferedReader.close() //关闭流
        Singleton.getInstance().SoundMarkList.clear()
        Singleton.getInstance().SoundMarkList = arrayList
        return arrayList
    }

    private fun changeToList(str: String): ArrayList<String> {
        var mList: ArrayList<String> = ArrayList()
        str.split("#").forEach {
            mList.add(it)
        }
        return mList
    }
}