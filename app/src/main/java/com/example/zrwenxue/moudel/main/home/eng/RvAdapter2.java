package com.example.zrwenxue.moudel.main.home.eng;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newzr.R;
import com.example.zrwenxue.moudel.main.word.singleworddetails.WordPronunciation;

import java.util.List;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-15
 * Time: 22:58
 */
class RvAdapter2 extends RecyclerView.Adapter<RvAdapter2.ViewHolder> {

//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }

    private Context context;
    private List<String> list;

    /**
     * 构造方法
     * @param context
     * @param list
     */
    public RvAdapter2(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
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
        inflater = LayoutInflater.from(context).inflate(R.layout.fragment_word_recycler_item,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;
    }

    /**
     * 将数据和控件绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(view -> {
            String mp3url= "http://ssl.gstatic.com/dictionary/static/sounds/oxford/"+list.get(position).toLowerCase()+"--_us_1.mp3";
            WordPronunciation wordPronunciation =new WordPronunciation(new MediaPlayer(),mp3url);
            wordPronunciation.play();
            Log.e("TAG","7777777777777777777777");
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.eng_item_textView);
        }
    }


}
