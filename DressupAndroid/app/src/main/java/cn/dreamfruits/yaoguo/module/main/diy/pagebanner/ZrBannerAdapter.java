package cn.dreamfruits.yaoguo.module.main.diy.pagebanner;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean;

/**
 * @Author qiwangi
 * @Date 2023/6/15
 * @TIME 15:16
 */
public class ZrBannerAdapter extends BannerAdapter<GetStyleVersionListByTypeBean.Item, ZrBannerAdapter.ZrViewHolder> {
    List<GetStyleVersionListByTypeBean.Item> dataList;

    public ZrBannerAdapter(List<GetStyleVersionListByTypeBean.Item> datas) {
        super(datas);
        dataList = datas;
    }


    @Override
    public ZrViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ZrView zrView = new ZrView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        zrView.setLayoutParams(params);

        return new ZrViewHolder(zrView);
    }

    @Override
    public void onBindView(ZrViewHolder holder, GetStyleVersionListByTypeBean.Item data, int position, int size) {
        holder.zrView.setData(data);
    }


    class ZrViewHolder extends RecyclerView.ViewHolder {
        public ZrView zrView;

        public ZrViewHolder(@NonNull View itemView) {
            super(itemView);
            zrView = (ZrView) itemView;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBannerData(List<GetStyleVersionListByTypeBean.Item> tempList, Boolean isClear) {
        if (isClear) {
            //很恶心的  mDatas为当前的数据的泛型实例  不能使用datalist
            mDatas.clear();
            mDatas = tempList;
            notifyDataSetChanged();
        } else {
            mDatas.addAll(tempList);
            notifyItemRangeChanged(mDatas.size(), mDatas.size() + tempList.size());
        }
    }
}
