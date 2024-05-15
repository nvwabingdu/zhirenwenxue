package com.example.zrwenxue.moudel.main.mine

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newzr.R
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.login.LoginActivity
import com.example.zrwenxue.moudel.main.word.SingleUrl

class MineFragment : BaseFragment() {

    companion object {
        fun newInstance() = MineFragment()
    }

    private lateinit var viewModel: MineViewModel
    override fun setLayout(): Int = R.layout.fragment_mine2


    private var img:ImageView?=null
    override fun initView() {
        img=rootView!!.findViewById(R.id.imageView)
        /**头像*/
        Glide.with(requireActivity())
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(com.example.zrframe.R.drawable.icon_user)
            .load(SingleUrl.getRandomImageUrl())
            .into(img!!)
        rootView!!.findViewById<TextView>(R.id.me_name).text= activity!!.getSharedPreferences("MyUserInfo", AppCompatActivity.MODE_PRIVATE)!!.getString("username","")?:"暂无"
        rootView!!.findViewById<TextView>(R.id.me_info).text= activity!!.getSharedPreferences("MyUserInfo", AppCompatActivity.MODE_PRIVATE)!!.getString("usermotto","")?:"暂无"

        setOnClick()
    }

   private fun setOnClick() {
       rootView!!.findViewById<LinearLayout>(R.id.l1).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l2).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l3).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l4).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l5).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l6).setOnClickListener {

       }
       rootView!!.findViewById<LinearLayout>(R.id.l7).setOnClickListener {
           LoginActivity.startAc(activity!!,"","")
       }
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}