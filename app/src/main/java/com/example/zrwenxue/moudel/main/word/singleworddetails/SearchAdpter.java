package com.example.zrwenxue.moudel.main.word.singleworddetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.newzr.R;

import java.util.List;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-30
 * Time: 14:55
 */
public class SearchAdpter extends RecyclerView.Adapter<SearchAdpter.ViewHolder>{

    private Context context;
    private List<ExplainBean> list;

    /**
     * 构造方法
     * @param context
     * @param list
     */
    public SearchAdpter(Context context, List<ExplainBean> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();//刷新适配器
    }


    /**
     * 创建ViewHolder，返回每一项的布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflater;
        inflater = LayoutInflater.from(context).inflate(R.layout.activity_word_search_listview_items,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;
    }


    /**
     * 将数据和控件绑定  监听之类的
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv1.setText(list.get(position).speech);
        holder.tv2.setText(list.get(position).explain);
    }


    /**
     * 返回Item总条数
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 内部类，绑定控件
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.t1);
            tv2 = itemView.findViewById(R.id.t2);
        }
    }



}
