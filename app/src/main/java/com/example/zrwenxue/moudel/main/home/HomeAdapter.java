package com.example.zrwenxue.moudel.main.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.newzr.R;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-15
 * Time: 16:37
 */
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private ArrayList<HomeBean> list;
    private Context context;


    /**
     * 构造方法
     * @param context
     * @param list
     */
    public HomeAdapter(Context context, ArrayList<HomeBean> list) {
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
        inflater = LayoutInflater.from(context).inflate(R.layout.fragment_home_item1,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;
    }


    /**
     * 将数据和控件绑定  监听之类的
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(list.get(position).content);
        holder.imageView.setBackground(list.get(position).img);
        //点击布局
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //接口方法
                homeAdapterInterface.onclick(position);
            }
        });
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
        TextView textView;
        ImageView imageView;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.home_layout);
            textView = itemView.findViewById(R.id.home_tv);
            imageView = itemView.findViewById(R.id.home_img);
        }
    }


    /**
     * 回调接口
     */
    public interface HomeAdapterInterface{
        void onclick(int position);
    }


    /**
     * 回调方法 (把接口放入)
     */
    private HomeAdapterInterface homeAdapterInterface;
    public void homeAdapterCallback(HomeAdapterInterface homeAdapterInterface){
        this.homeAdapterInterface=homeAdapterInterface;
    }








}
