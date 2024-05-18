package cn.dreamfruits.yaoguo.module.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R

class FeedFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.recycler_view)

        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        mRecyclerView.adapter = TestAdapter()

    }


    companion object {

        /**
         * @param type 类型: 帖子，收藏，赞过
         */
        @JvmStatic
        fun newInstance(type:Int): FeedFragment {
            val args = Bundle()
            args.putInt("type",type)

            val fragment = FeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    class TestAdapter : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {
        var list = arrayListOf<Int>()

        init {
//            for (i in 1..5) {
//                list.add(i)
//            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_test_view, parent, false)
            return TestViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
            val data = list[position]
            holder.text.text = "$data"
        }

        override fun getItemCount(): Int {
            return list.size
        }


        class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text:TextView = itemView.findViewById(R.id.text_view)
        }


    }


}