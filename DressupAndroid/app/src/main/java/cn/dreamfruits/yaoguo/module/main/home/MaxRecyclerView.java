package cn.dreamfruits.yaoguo.module.main.home;
import androidx.recyclerview.widget.RecyclerView;

/**
 * sgf
 * 最大化的RecyclerView，嵌套于ScrollView之中使用,处理多个RecyclerView显示不全的问题
 */
public class MaxRecyclerView extends RecyclerView {

    public MaxRecyclerView(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }
    public MaxRecyclerView(android.content.Context context){
        super(context);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //size参数表示View所需的大小，而mode参数指定如何解释大小。有三种可能的模式：
        //MeasureSpec.EXACTLY：尺寸以像素或dp为单位精确指定，View应使用此精确尺寸进行布局。
        //MeasureSpec.AT_MOST：尺寸可以最多为指定大小，并且View不得超过此大小。
        //MeasureSpec.UNSPECIFIED：大小未指定，View可以选择自己的大小。
        //例如，要创建一个具有200像素精确尺寸的MeasureSpec，您可以调用以下代码：
        //
        //int measureSpec = MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY);

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.EXACTLY);
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
