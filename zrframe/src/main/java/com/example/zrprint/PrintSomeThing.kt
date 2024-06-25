package com.example.zrprint

import android.content.Context
import android.util.Log
import java.io.File

/**
 * @Author qiwangi
 * @Date 2023/8/11
 * @TIME 14:42
 */

val xmlString =
    """
      <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/ciphertext_output"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="Zrb"
        android:textColor="@color/black"
        android:textStyle="bold" />

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:layout_marginTop="10dp"
        android:clickable="true"
        android:focusable="true"
        android:foreground="?android:attr/selectableItemBackground"
        app:cardCornerRadius="10dp"
        app:cardPreventCornerOverlap="false"
        app:cardUseCompatPadding="true">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="10dp">
            <TextView
                android:id="@+id/tv_zrb"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="5743cf9798ca74eb09e7e44493447f7f487bb994b3595a3154f0a2f88d30d38ccf84726b1ae83cc71b439fa91dbdf62683f21630b78b5770720a2d77ebac58ac" />
            <TextView
                android:id="@+id/tv_zrb_copy"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:paddingTop="3dp"
                android:gravity="center"
                android:layout_alignParentBottom="true"
                android:layout_alignParentEnd="true"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="复制"/>
        </RelativeLayout>


    </androidx.cardview.widget.CardView>

    <!--信息显示部分-->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Creat Time："
                android:textColor="@color/black"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tv_zrb_time"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="2024.05.04 13:25:12" />
        </LinearLayout>


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Holder："
                android:textColor="@color/black"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tv_zrb_holder"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="王琦" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Holder Password："
                android:textColor="@color/black"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tv_zrb_holder_password"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="*******" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Balance："
                android:textColor="@color/black"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tv_zrb_balance"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="1478.25" />
        </LinearLayout>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:gravity="center_vertical"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="解释说明"
                android:textColor="@color/black"
                android:textStyle="bold" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:text="1.智人币仅能在本APP使用\n\n2.智人币来源于各种任务和特殊渠道\n\n3.每天能获取的智人币上限为0.1个智人币\n\n4.智人交易单位最小为1个智人币\n\n5.智人币的作用可用于购买涂鸦画" />
        </LinearLayout>

    </LinearLayout>

</LinearLayout>
    """

fun main() {
    //1.打印id
//    FIndViewByIdToool().out(xmlString, "rootView.")
    FIndViewByIdToool().out(xmlString, "")

    //1.打印id  java版
//    FIndViewByIdTooolJave().out(xmlString, "rootView.")


    //2.打印重复数据 根据需求修改
//    print2()



    //3.打印文件夹下所有文件名
//    print3()

    //4.遍历assets文件
//    listFilesInAssets()

}

/**
 * 打印重复数据
 */
fun print2(){
    var stu = StringBuffer()
    (1..48).forEach {
        stu.append(
            """  
                idList!!.add(s${it}!!)
                    """.trimIndent() + "\r\n"
        )
    }
    println(stu.toString())
}

/**
 * 打印文件夹下所有文件名
 */
fun print3(){
//    val directory = File("/Users/qiwangi/Downloads/字体(100)/未命名文件夹") // 指定文件夹路径
    val directory = File("/Users/qiwangi/Downloads") // 指定文件夹路径
    printFileNames(directory)
}

fun printFileNames(directory: File) {
    val files = directory.listFiles()

    if (files != null) {
        for (file in files) {
            if (file.isFile) {
                println(file.name)
            }
//            else if (file.isDirectory) {
//                printFileNames(file)
//            }
        }
    }
}


/**
 * 遍历Assets
 */
fun listFilesInAssets(context: Context) {
    val assetManager = context.assets
    val fileList = assetManager.list("") // 获取 assets 文件夹下的所有文件

    if (fileList != null) {
        for (filename in fileList) {
            println(filename) // 输出文件名
        }
    }
}



