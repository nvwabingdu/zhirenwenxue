package com.example.zrwenxue.moudel.main.word.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.word.MyStatic;
import com.example.zrwenxue.moudel.main.word.Singleton;
import com.example.zrwenxue.moudel.main.word.WordFromShardToSingleton;
import com.example.zrwenxue.moudel.main.word.Wordbean;
import com.example.zrwenxue.moudel.main.word.singleworddetails.SearchActivity;

import java.util.List;


/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-08
 * Time: 21:27
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private Context context;
    private List<Wordbean> list;

    /**
     * 构造方法
     *
     * @param context
     * @param list
     */
    public WordAdapter(Context context, List<Wordbean> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();//刷新适配器
    }

    /**
     * 更新适配器
     */
    public void update(List<Wordbean> list) {
        this.list = list;
        notifyDataSetChanged();//刷新适配器
    }


    /**
     * 创建ViewHolder，返回每一项的布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflater;
        inflater = LayoutInflater.from(context).inflate(R.layout.fragment_remember_item, parent, false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;
    }

    /**
     * 将数据和控件绑定  监听之类的
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.word.setText(list.get(position).word);
        if (Singleton.getInstance().showSoundMark){
            holder.mark.setText(list.get(position).getWord_sound_mark());
            holder.mark.setVisibility(View.VISIBLE);
        }else {
            holder.mark.setVisibility(View.GONE);
        }


        if (String.valueOf(list.get(position).getWord_times()).equals("0")){
            holder.times.setVisibility(View.INVISIBLE);
        }else {
            holder.times.setVisibility(View.VISIBLE);
            holder.times.setText(list.get(position).getWord_times() + "");
        }

        holder.view.setBackgroundColor(ContextCompat.getColor(context, MyStatic.setTextColor(list.get(position).getWord_times())));
        if (list.get(position).getWord_times() > 0) {
            holder.word.setTextColor(0xff000000);
            holder.times.setTextColor(0xff000000);
            holder.times.setText(list.get(position).getWord_times() + "");
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                WordFromShardToSingleton.setShardWordTimes(context,"");
                //共享参数设置，并更新到单例集合
                WordFromShardToSingleton.fixSingletonList(context,
                        WordFromShardToSingleton.getShardWordTimes(context),
                        list.get(position).word);


                holder.word.setTextColor(0xff000000);
                holder.times.setTextColor(0xff000000);
                holder.times.setText(list.get(position).getWord_times() + 1 + "");
                holder.view.setBackgroundColor(ContextCompat.getColor(context,MyStatic.setTextColor(list.get(position).getWord_times()+1)));


                Singleton.getInstance().mWordCurrentWordBean=list.get(position);//wq 赋值给共享参数
                //点击跳转到单词页面
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("soundmark", list.get(position).getWord());

                intent.putExtra("times", holder.times.getText());
                context.startActivity(intent);

//            MyStatic.showPopupWindow((Activity) context,context,R.layout.common_dialog_layout,R.layout.fragment_remember);
            }
        });
    }


    /**
     * 返回Item总条数
     *
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
        View view;
//        LengthAndSizeText word;
        TextView word;
        TextView times;
        TextView mark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.fragment_remember_item_layout);
            word = itemView.findViewById(R.id.tv_word);
            times = itemView.findViewById(R.id.tv_times);
            mark = itemView.findViewById(R.id.tv_mark);
        }
    }


}
