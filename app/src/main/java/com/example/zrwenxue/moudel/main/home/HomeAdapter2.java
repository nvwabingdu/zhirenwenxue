package com.example.zrwenxue.moudel.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.newzr.R;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-15
 * Time: 17:50
 */
class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.ViewHolder>{

    private ArrayList<HomeBean> list;
    private Context context;


    /**
     * 构造方法
     * @param context
     * @param list
     */
    public HomeAdapter2(Context context, ArrayList<HomeBean> list) {
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
        inflater = LayoutInflater.from(context).inflate(R.layout.fragment_home_item2,parent,false);
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
        holder.textView.setText(list.get(position).content);
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(80));//图片圆角为30
        Glide.with(context)
                .load(list.get(position).img) //图片地址
                .apply(options)
                .into(holder.imageView);

        //item点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注入接口
                homeAdapterInterfaceTWO.onclick(position);
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
            textView = itemView.findViewById(R.id.home_tv2);
            imageView = itemView.findViewById(R.id.home_img2);
            view = itemView.findViewById(R.id.home_layout2);
        }
    }


    /**
     * 用于回调的接口
     */
    interface HomeAdapterInterfaceTWO{
        void onclick(int position);
    }

    /**
     * 用于回调的方法
     */
    HomeAdapterInterfaceTWO homeAdapterInterfaceTWO;
    void HomeAdapter2CallBack(HomeAdapterInterfaceTWO homeAdapterInterfaceTWO){
        this.homeAdapterInterfaceTWO=homeAdapterInterfaceTWO;
    }


}
