package com.example.zrwenxue.moudel.main.center.tetris

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newzr.R
import com.example.zrtool.ui.noslidingconflictview.MaxRecyclerView
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.center.crypt.CryptActivity
import com.example.zrwenxue.moudel.main.home.dict.OtherDictActivity
import com.example.zrwenxue.moudel.main.home.led.LEDActivity
import com.example.zrwenxue.moudel.main.home.lottery.LotteryActivity
import com.example.zrwenxue.moudel.main.home.phrase.PhraseActivity
import com.example.zrwenxue.moudel.main.memory.MemoryActivity
import com.example.zrwenxue.moudel.main.word.MyStatic
import com.example.zrwenxue.others.zrdrawingboard.DoodleViewActivity
import com.example.zrwenxue.others.zrdrawingboard.SetDoodleInfoActivity
import java.util.Arrays
import java.util.Random

class TetrisListFragment : BaseFragment() {


    override fun setLayout(): Int = R.layout.fragment_tetris_list

    //拼块布局
    var m1: MaxRecyclerView? = null
    var m2: MaxRecyclerView? = null
    var m3: MaxRecyclerView? = null
    var m4: MaxRecyclerView? = null
    var m5: MaxRecyclerView? = null
    var m6: MaxRecyclerView? = null
    private var m1List: MutableList<TetrisBean>? = null
    private var m2List: MutableList<TetrisBean>? = null
    private var m3List: MutableList<TetrisBean>? = null
    private var m4List: MutableList<TetrisBean>? = null
    private var m5List: MutableList<TetrisBean>? = null
    private var m6List: MutableList<TetrisBean>? = null

    private val transparentColor: String = "#00ffffff"//透明色 用于占位item

    val h1 = 200
    val h2 = 400
    val h3 = 800

    /**
     * 初始化view
     */
    override fun initView() {


//        if ( Singleton.getInstance().fileLines.size==0){
//            //这里加载记忆一百法
//            Singleton.getInstance().fileLines= AssetsUtils.readTextFileFromAssets(activity!!, "记忆100.txt") as ArrayList<String>
//        }

        m1List = mutableListOf()
        m2List = mutableListOf()
        m3List = mutableListOf()
        m4List = mutableListOf()
        m5List = mutableListOf()
        m6List = mutableListOf()

//        for (i in 0..10){
//            temp4(i, Singleton.getInstance().fileLines!![i].split("：")[0])
//        }

        //设置集合
        setList()

        m1 = rootView!!.findViewById(R.id.tetris_1)
        m1!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager1 = LinearLayoutManager(activity!!)
        m1!!.layoutManager = layoutManager1
        val adapter1 = TetrisAdapter1(activity!!, m1List!!)
        m1!!.adapter = adapter1

        adapter1.setOnItemClickListener(object : TetrisAdapter1.TetrisAdapterInterface {
            override fun onRecyclerViewItemClick(view: View?, position: Int, contentStr: String) {
                //这个position不是角标  是传入的第几次的position

                if (setOnclick(m1List!!, position, 1)) {
                    return
                }
                if (setOnclick(m4List!!, position, 4)) {
                    return
                }
                if (setOnclick(m6List!!, position, 6)) {
                    return
                }

            }
        })

        m2 = rootView!!.findViewById(R.id.tetris_2)
        m2!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager2 = LinearLayoutManager(activity!!)
        m2!!.layoutManager = layoutManager2
        val adapter2 = TetrisAdapter1(activity!!, m2List!!)
        m2!!.adapter = adapter2
        adapter2.setOnItemClickListener(object : TetrisAdapter1.TetrisAdapterInterface {
            override fun onRecyclerViewItemClick(view: View?, position: Int, contentStr: String) {
                if (setOnclick(m2List!!, position, 2)) {
                    return
                }
                if (setOnclick(m4List!!, position, 4)) {
                    return
                }
                if (setOnclick(m5List!!, position, 5)) {
                    return
                }
                if (setOnclick(m6List!!, position, 6)) {
                    return
                }

            }
        })

        m3 = rootView!!.findViewById(R.id.tetris_3)
        m3!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager3 = LinearLayoutManager(activity!!)
        m3!!.layoutManager = layoutManager3
        val adapter3 = TetrisAdapter1(activity!!, m3List!!)
        m3!!.adapter = adapter3
        adapter3.setOnItemClickListener(object : TetrisAdapter1.TetrisAdapterInterface {
            override fun onRecyclerViewItemClick(view: View?, position: Int, contentStr: String) {
                if (setOnclick(m3List!!, position, 3)) {
                    return
                }

                if (setOnclick(m5List!!, position, 5)) {
                    return
                }
                if (setOnclick(m6List!!, position, 6)) {
                    return
                }

            }
        })

        m4 = rootView!!.findViewById(R.id.tetris_4)
        m4!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager4 = LinearLayoutManager(activity!!)
        m4!!.layoutManager = layoutManager4
        val adapter4 = TetrisAdapter2(activity!!, m4List!!)
        m4!!.adapter = adapter4

        m5 = rootView!!.findViewById(R.id.tetris_5)
        m5!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager5 = LinearLayoutManager(activity!!)
        m5!!.layoutManager = layoutManager5
        val adapter5 = TetrisAdapter2(activity!!, m5List!!)
        m5!!.adapter = adapter5

        m6 = rootView!!.findViewById(R.id.tetris_6)
        m6!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        var layoutManager6 = LinearLayoutManager(activity!!)
        m6!!.layoutManager = layoutManager6
        val adapter6 = TetrisAdapter2(activity!!, m6List!!)
        m6!!.adapter = adapter6
    }


    /**
     * 用于简化下面操作的类
     */
    data class SetBean(
        val str: String,
        val mClass: Class<*>,
        val tag: String,
        val content: String

    )

    var setClassList: MutableList<SetBean>? = null

    private fun setList() {
        /**
         * 以后仅修改这里
         */
        setClassList = mutableListOf(
            SetBean("智人社区", CryptActivity::class.java, "", ""),
            SetBean("智人涂鸦", DoodleViewActivity::class.java, "", ""),
            SetBean("短语学习", PhraseActivity::class.java, "", ""),
            SetBean("认识音标", MemoryActivity::class.java, "", ""),
            SetBean("幸运彩票", LotteryActivity::class.java, "", ""),
            SetBean("LED滚动", LEDActivity::class.java, "", ""),
            SetBean("中医方剂大全", OtherDictActivity::class.java, "dict", "0"),
            SetBean("佛学大辞典", OtherDictActivity::class.java, "dict", "1"),
            SetBean("全唐诗", OtherDictActivity::class.java, "dict", "2"),
            SetBean("古汉语常用词典", OtherDictActivity::class.java, "dict", "3"),
            SetBean("唐诗三百首", OtherDictActivity::class.java, "dict", "4"),
            SetBean("姓氏起源", OtherDictActivity::class.java, "dict", "5"),
            SetBean("宋词鉴赏大辞典", OtherDictActivity::class.java, "dict", "6"),
            SetBean("家常菜", OtherDictActivity::class.java, "dict", "7"),
            SetBean("成语词典", OtherDictActivity::class.java, "dict", "8"),
            SetBean("掌上法律库", OtherDictActivity::class.java, "dict", "9"),
            SetBean("本草纲目", OtherDictActivity::class.java, "dict", "10"),
            SetBean("唐代诗人简介", OtherDictActivity::class.java, "dict", "11"),
            SetBean("小众佛学", OtherDictActivity::class.java, "dict", "12"),
            SetBean("中日词典", OtherDictActivity::class.java, "dict", "13"),
            SetBean("日汉大辞典", OtherDictActivity::class.java, "dict", "14"),
            SetBean("设置", SetDoodleInfoActivity::class.java, "", ""),
        )

        for (i in 0..<setClassList!!.size) {
            temp4(i, setClassList!![i].str)
        }

    }

    /**
     * 点击事件
     */
    fun setOnclick(tempList: MutableList<TetrisBean>, tempPosition: Int, listType: Int): Boolean {


        tempList.forEach {
            if (it.mPosition == tempPosition && it.content != "") {

                MyStatic.setActivityString(activity, setClassList!![tempPosition].mClass, setClassList!![tempPosition].tag, setClassList!![tempPosition].content)

                return true
            }
        }

        return false
    }

    /**
     * 六个集合高度一致的情况  齐平
     */
    private fun addOne(type: Int, pos_: Int, str: String) {//可以添加9种布局
        when (type) {
            11 -> {
                m1List!!.add(TetrisBean(getRandomHexColor(), str, h1, pos_, true))
                m4List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
            }

            12 -> {
                m1List!!.add(TetrisBean(getRandomHexColor(), str, h2, pos_, true))
                m4List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
            }

            13 -> {
                m1List!!.add(TetrisBean(getRandomHexColor(), str, h3, pos_, true))
                m4List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
            }

            21 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m4List!!.add(TetrisBean(getRandomHexColor(), str, h1, pos_, true))
                m5List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
            }

            22 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m4List!!.add(TetrisBean(getRandomHexColor(), str, h2, pos_, true))
                m5List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
            }

            23 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m4List!!.add(TetrisBean(getRandomHexColor(), str, h3, pos_, true))
                m5List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m6List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
            }

            31 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m3List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m4List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m5List!!.add(TetrisBean(transparentColor, "", h1, pos_))//添加占位
                m6List!!.add(TetrisBean(getRandomHexColor(), str, h1, pos_, true))
            }

            32 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m3List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m4List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m5List!!.add(TetrisBean(transparentColor, "", h2, pos_))//添加占位
                m6List!!.add(TetrisBean(getRandomHexColor(), str, h2, pos_, true))
            }

            33 -> {
                m1List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m2List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m3List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m4List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m5List!!.add(TetrisBean(transparentColor, "", h3, pos_))//添加占位
                m6List!!.add(TetrisBean(getRandomHexColor(), str, h3, pos_, true))
            }
        }
    }

    private fun temp4(pos_: Int, str: String) {
        //获取各集合高度
        var a1 = getListHeight(m1List!!)
        var a2 = getListHeight(m2List!!)
        var a3 = getListHeight(m3List!!)
        var a4 = getListHeight(m4List!!)
        var a5 = getListHeight(m5List!!)
        var a6 = getListHeight(m6List!!)
        if (a1 == a2 && a2 == a3 && a3 == a4 && a4 == a5 && a5 == a6) {//齐平的情况下 随机选择一种
            addOne(getType(), pos_, str)//第一步 齐平的情况下  随机9中格式的添加一个
        } else {//不平的情况下
            /**从m1--m6这个顺序 找高度低于前面的集合  然后添加其 或可以其 的一个*/
            if (a1 > a2) {//说明m2可以添加  m5  可以添加
                if (a3 > a2) {
                    val tempH = getRandomHeight()//可以随机高度
                    m2List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                    //m4 m5 m6 重叠
                    setListAddHeightDiff((a2 + tempH) - a4, m4List!!, pos_)
                    setListAddHeightDiff((a2 + tempH) - a5, m5List!!, pos_)
                    setListAddHeightDiff((a2 + tempH) - a6, m6List!!, pos_)
                    return
                }
                if (a2 > a3) {//说明m3可以添加
                    val tempH = getRandomHeight()//可以随机高度
                    m3List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                    //m5 m6 重叠
                    setListAddHeightDiff((a3 + tempH) - a5, m5List!!, pos_)
                    setListAddHeightDiff((a3 + tempH) - a6, m6List!!, pos_)
                    return
                }
                when (Random().nextInt(2)) {
                    0 -> {//m2添加
                        val tempH = getRandomHeight()//可以随机高度
                        m2List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                        //m4 m5 m6 重叠
                        setListAddHeightDiff((a2 + tempH) - a4, m4List!!, pos_)
                        setListAddHeightDiff((a2 + tempH) - a5, m5List!!, pos_)
                        setListAddHeightDiff((a2 + tempH) - a6, m6List!!, pos_)
                    }


                    1 -> {//m5添加
                        val tempH = getRandomHeight()//可以随机高度
                        m5List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                        //m2 m3 m6 重叠
                        setListAddHeightDiff((a5 + tempH) - a2, m2List!!, pos_)
                        setListAddHeightDiff((a5 + tempH) - a3, m3List!!, pos_)
                        setListAddHeightDiff((a5 + tempH) - a6, m6List!!, pos_)
                    }
                }

                return
            }

            if (a2 > a1) {
                val tempH = getRandomHeight()//可以随机高度
                m1List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                //m4  m6 重叠
                setListAddHeightDiff((a1 + tempH) - a4, m4List!!, pos_)
                setListAddHeightDiff((a1 + tempH) - a6, m6List!!, pos_)
                return
            }

            if (a1 == a2) {
                if (a2 > a3) {//说明m3可以添加
                    val tempH = getRandomHeight()//可以随机高度
                    m3List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                    //m5 m6 重叠
                    setListAddHeightDiff((a3 + tempH) - a5, m5List!!, pos_)
                    setListAddHeightDiff((a3 + tempH) - a6, m6List!!, pos_)
                    return
                }

                when (Random().nextInt(2)) {
                    0 -> {//m1添加
                        val tempH = getRandomHeight()//可以随机高度
                        m1List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                        //m4  m6 重叠
                        setListAddHeightDiff((a1 + tempH) - a4, m4List!!, pos_)
                        setListAddHeightDiff((a1 + tempH) - a6, m6List!!, pos_)
                        return
                    }

                    1 -> {//m4添加
                        val tempH = getRandomHeight()//可以随机高度
                        m4List!!.add(TetrisBean(getRandomHexColor(), str, tempH, pos_, true))
                        //m1  m6 重叠
                        setListAddHeightDiff((a4 + tempH) - a1, m1List!!, pos_)
                        setListAddHeightDiff((a4 + tempH) - a2, m2List!!, pos_)
                        setListAddHeightDiff((a4 + tempH) - a6, m6List!!, pos_)
                        return
                    }

                }
            }
        }
    }

    /**
     * 设置重叠集合添加高度差
     */
    private fun setListAddHeightDiff(heightDiff: Int, ttlist: MutableList<TetrisBean>, pos_: Int) {
        if (heightDiff > 0) {
            ttlist.add(TetrisBean(transparentColor, "", heightDiff, pos_))//添加占位
        }
    }

    /**
     * 获取集合所占高度
     */
    private fun getListHeight(list: MutableList<TetrisBean>): Int {
        var height = 0
        for (i in 0 until list.size) {
            height += list[i].height
        }
        return height
    }

    /**
     * 随机高度
     */
    private fun getRandomHeight(): Int {
        // TODO:  后续用图片实际宽高比例来做
        val values = listOf(h1, h2, h3)
        return values.random()
    }

    /**
     * 随机9种模式
     */
    private fun getType(): Int {
        val values = listOf(11, 12, 13, 21, 22, 23, 31, 32, 33)
        return values.random()
    }

    /**
     * 不带透明度的随机颜色
     */
    private fun getRandomHexColor(): String {
        val color = (0..10000000).random() // 生成一个 0 到 16777215 之间的随机整数
        return String.format("#%06X", color) // 将整数转换为 6 位十六进制字符串
    }

    /**
     * 带透明度的随机颜色
     */
    fun getRandomHexColorWithAlpha(): String {
        val color = (0..16777215).random()
        val alpha = (0..255).random()
        return String.format("#%02X%06X", alpha, color)
    }

    /**
     * 随机图片链接
     */
    private fun getRandomImageUr2l(): String {
        // 将多个图片链接存储在列表中
        val imageUrls: List<String> = Arrays.asList(
//            "https://images.alphacoders.com/129/thumbbig-1298253.webp",
//            "https://images4.alphacoders.com/114/thumbbig-1140982.webp",
//            "https://images3.alphacoders.com/114/thumbbig-1144293.webp",
//            "https://images5.alphacoders.com/114/thumbbig-1141137.webp",
//            "https://images.alphacoders.com/120/thumbbig-1207008.webp",
//            "https://images7.alphacoders.com/130/thumbbig-1305785.webp",
//            "https://images6.alphacoders.com/112/thumbbig-1120777.webp",
//            "https://images6.alphacoders.com/114/thumbbig-1141601.webp"
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N"
        )
        // 随机选择一个链接并返回
        val random = Random()
        return imageUrls[random.nextInt(imageUrls.size)]
    }

    /**
     * 随机图片
     */
    private fun getRandomImageUrl2(): String {
        // 将多个图片链接存储在列表中
        val imageUrls: List<String> = Arrays.asList(
            "https://img2.woyaogexing.com/2023/04/09/55f771c75e6a4883c167aa6a6766cb22.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/7adb5986b2cd7ab25c9d2cba0e8af8e1.jpeg",
            "https://img2.woyaogexing.com/2023/04/08/16a5092608019b35f7172476bf48968e.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/08/c626834b97f48a3f00bda3cd1580db44.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/c0df9e9f51bf18ac6882fb1b9dffe3e2.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/09/2599eb37c7f6cbe0c52006270ec6cf4f.png",
            "https://img2.woyaogexing.com/2023/04/08/ab2c6520fb3d05a998ed0f2ce6d2d007.jpeg"
        )
        // 随机选择一个链接并返回
        val random = Random()
        return imageUrls[random.nextInt(imageUrls.size)]
    }


}