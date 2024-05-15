package com.example.zrwenxue.moudel.main.home.news.newsitem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newzr.R;
import com.example.zrtool.oldnet.beans.MainNewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */
public class RecyClerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MainNewsBean.ResultBean.DataBean> list;
    private Context context;

    /**
     * @param context 单参数构造
     */
    public RecyClerViewAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    /**
     * @param list    构造方法传参
     * @param context
     */
    public RecyClerViewAdapter(List<MainNewsBean.ResultBean.DataBean> list, Context context) {
        list = new ArrayList<>();
        this.list = list;
        this.context = context;
    }

    /**
     * @param parent   创建子布局  返回内部类的对象holder
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_one, null);
                holder = new MyViewHolder2(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_two, null);
                holder = new MyViewHolder2(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_three, null);
                holder = new MyViewHolder3(view);
                break;
        }
        return holder;
    }

    /**
     * @param holder   在holder上绑定控件
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //点击item的回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClick(v, position);
                }
            }
        });
        //判断是哪个类的实例
        if (holder instanceof MyViewHolder2) {
            MyViewHolder2 holder2 = (MyViewHolder2) holder;
            holder2.tv_title.setText(list.get(position).getTitle());
            holder2.tv_autorName.setText(list.get(position).getAuthor_name());
            holder2.tv_date.setText(list.get(position).getDate());
            Glide.with(context)
                    .load(list.get(position).getThumbnail_pic_s()+"")
                    //.placeholder(R.drawable.errloading)
                    .error(R.drawable.p_error)
                    .into(((MyViewHolder2) holder2).image_s);
            Glide.with(context)
                    .load(list.get(position).getThumbnail_pic_s02()+"")
                    .error(R.drawable.p_error)
                    .into(((MyViewHolder2) holder2).image_s2);
            Glide.with(context)
                    .load(list.get(position).getThumbnail_pic_s03()+"")
                    .error(R.drawable.p_error)
                    .into(((MyViewHolder2) holder2).image_s3);
        } else {
            MyViewHolder3 holder3 = (MyViewHolder3) holder;
            holder3.tv_title.setText(list.get(position).getTitle());
            holder3.tv_autorName.setText(list.get(position).getAuthor_name());
            holder3.tv_date.setText(list.get(position).getDate());
            Glide.with(context)
                    .load(list.get(position).getThumbnail_pic_s()+"")
                    .error(R.drawable.p_error)
                    .into(((MyViewHolder3) holder3).image_s3);
        }
    }


    /**
     * @return 返回数据的条数 这里和list一样
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * @param position 复写设置类型方法  也就是上面onCreateViewHolder的viewType的取值
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 内部类布局二
     */
    class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView tv_title, tv_autorName, tv_date;
        ImageView image_s, image_s2, image_s3;

        public MyViewHolder2(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.news_item_two_title);
            tv_autorName = (TextView) itemView.findViewById(R.id.news_item_two_author_name);
            tv_date = (TextView) itemView.findViewById(R.id.news_item_two_date);
            image_s = (ImageView) itemView.findViewById(R.id.news_item_two_img1);
            image_s2 = (ImageView) itemView.findViewById(R.id.news_item_two_img2);
            image_s3 = (ImageView) itemView.findViewById(R.id.news_item_two_img3);
        }
    }

    /**
     * 内部类布局三
     */
    class MyViewHolder3 extends RecyclerView.ViewHolder {
        TextView tv_title, tv_autorName, tv_date;
        ImageView image_s3;

        public MyViewHolder3(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.news_item_three_title);
            tv_autorName = (TextView) itemView.findViewById(R.id.news_item_three_author_name);
            tv_date = (TextView) itemView.findViewById(R.id.news_item_three_date);
            image_s3 = (ImageView) itemView.findViewById(R.id.news_item_three_img);
        }
    }

    //-------------------------------------------------------------------//

    /**
     * 回调    先创建接口
     */
    public interface Callback {
        void onItemClick(View view, int position);
    }


    /**
     * @param jie 注册接口
     */
    private Callback callback;
    public void getCallback(Callback callback) {
        this.callback = callback;
    }


    /**
     * @param list 自定义方法 添加数据
     */
    public void addList(List<MainNewsBean.ResultBean.DataBean> list) {
        if (this.list.containsAll(list)) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * @param list 自定义方法  更新数据
     */
    public void updateList(List<MainNewsBean.ResultBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * @return 自定义方法 获取集合
     */
    public List<MainNewsBean.ResultBean.DataBean> getList() { return this.list; }
}

