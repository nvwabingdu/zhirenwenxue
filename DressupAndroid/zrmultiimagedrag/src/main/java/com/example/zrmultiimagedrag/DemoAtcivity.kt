package com.example.zrmultiimagedrag

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

//也可以看readme
class DemoAtcivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val list: MutableList<ImageModel> = ArrayList()
//        for (i in 0..7) {
//            val model =
//               ImageModel()
//            model.path = R.mipmap.android
//            list.add(model)
//        }
//
//        uploadMultiImageView
//            .setImageInfoList(list.toList())
//            // 所有属性都可以在代码中再设置
//            // 开启拖拽排序
//            .setDrag(true)
//            // 设置每行3列
//            .setColumns(3)
//            // 显示新增按钮
//            .setShowAdd(true)
//            // 设置图片缩放类型 (默认 CENTER_CROP)
//            .setScaleType(ImageView.ScaleType.CENTER_CROP)
//            // item点击回调
//            .setImageItemClickListener { position ->
//                // imageview点击
//                Toast.makeText(baseContext, "点击第了${position}个", Toast.LENGTH_SHORT).show()
//            }
//            // 设置删除点击监听（如果不设置，测试默认移除数据），自己处理数据删除过程
//            .setDeleteClickListener { multiImageView, position ->
//                AlertDialog.Builder(this)
//                    .setTitle("删除")
//                    .setMessage("确定要删除图片吗？")
//                    .setNegativeButton("确定") { dialog, which ->
//                        dialog.dismiss()
//                        multiImageView.deleteItem(position)
//                    }
//                    .show()
//            }
//            // 图片加载
//            .setImageViewLoader { context, path, imageView ->
//                // （这里自己选择图片加载框架，不做限制）
//                imageView.setImageResource(path as Int)
//            }
//            // 新增按钮点击回调
//            .setAddClickListener {
//                // 模拟新增一条数据
//                addNewData()
//            }
//            .show()
//    }
//
//    /**
//     * 新增一条或多条数据
//     */
//    private fun addNewData() {
//        val tempList: MutableList<ImageModel> = ArrayList()
//        val model = ImageModel()
//        model.path = R.mipmap.android
//        tempList.add(model)
//        // 新增数据
//        uploadMultiImageView.addNewData(tempList.toList())
//    }
}
