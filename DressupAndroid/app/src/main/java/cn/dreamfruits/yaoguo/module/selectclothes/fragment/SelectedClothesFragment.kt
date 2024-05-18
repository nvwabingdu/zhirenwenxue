package cn.dreamfruits.yaoguo.module.selectclothes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesActivity
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesViewModel
import cn.dreamfruits.yaoguo.module.selectclothes.adapter.SelectedClothesAdapter
import cn.dreamfruits.yaoguo.view.decoration.HItemDecoration

/**
 * 已经选择的服装列表
 */
@Deprecated("修改为最新的UI")
class SelectedClothesFragment : Fragment() {

    private lateinit var mSelectedClothesRv: RecyclerView

    private val clothesViewModel by activityViewModels<SelectClothesViewModel>()

    private var mAdapter: SelectedClothesAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_clothes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view.setOnClickListener {
//            (activity as SelectClothesActivity).closeFeature()
//        }

        mSelectedClothesRv = view.findViewById(R.id.recy_selected_clothes)

        initSelectedClothesList()
//        mAdapter?.setData(clothesViewModel.selectedClothesList)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
//            mAdapter?.setData(clothesViewModel.selectedClothesList)
        }
    }

    private fun initSelectedClothesList() {
        mAdapter = SelectedClothesAdapter(onDeleteClick = { clothesBean ->
//            clothesViewModel.cancel(clothesBean)
        })

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        mSelectedClothesRv.layoutManager = layoutManager

        mSelectedClothesRv.addItemDecoration(HItemDecoration(17))

        mSelectedClothesRv.adapter = mAdapter
    }


    companion object {
        fun newInstance(): SelectedClothesFragment {
            return SelectedClothesFragment()
        }
    }


}