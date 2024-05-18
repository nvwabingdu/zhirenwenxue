//package cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.dreamfruits.yaoguo.R;
//import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.library.CardAdapterHelper;
//
//
///**
// * Created by jameson on 8/30/16.
// */
//class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
//    private List<Integer> mList = new ArrayList<>();
//    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
//
//    public CardAdapter(List<Integer> mList) {
//        this.mList = mList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
//        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
//        holder.mImageView.setImageResource(mList.get(position));
//        holder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ToastUtils.show(holder.mImageView.getContext(), "" + position);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final ImageView mImageView;
//
//        public ViewHolder(final View itemView) {
//            super(itemView);
//            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
//        }
//
//    }
//
//}
