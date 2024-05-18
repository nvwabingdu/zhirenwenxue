package cn.dreamfruits.yaoguo.adapter.base.listener

import android.view.View
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter

/**
 * @author Lee
 * @createTime 2023-06-20 14 GMT+8
 * @desc :
 */
interface OnItemClickListener {
      /**
       * Callback method to be invoked when an item in this RecyclerView has
       * been clicked.
       *
       * @param adapter  the adapter
       * @param view     The itemView within the RecyclerView that was clicked (this
       * will be a view provided by the adapter)
       * @param position The position of the view in the adapter.
       */
      fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)
}