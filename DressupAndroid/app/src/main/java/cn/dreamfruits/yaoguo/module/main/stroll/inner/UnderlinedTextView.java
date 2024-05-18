package cn.dreamfruits.yaoguo.module.main.stroll.inner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @Author qiwangi
 * @Date 2023/6/19
 * @TIME 23:27
 * 使用 setUnderlineDistance() 方法来设置下划线与文字之间的距离。例如，在 Activity 中创建一个 UnderlinedTextView 对象，并将下划线距离设置为 4dp：
 * UnderlinedTextView textView = findViewById(R.id.text_view);
 * textView.setUnderlineDistance(getResources().getDisplayMetrics().density * 4);
 */
public class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint mPaint;
    private float mUnderlineDistance = getResources().getDisplayMetrics().density * 2; // 默认值为 2dp

    public UnderlinedTextView(Context context) {
        super(context);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#B7EA5A"));
        Log.e("zqr","mUnderlineDistance:"+mUnderlineDistance);
        mPaint.setStrokeWidth(mUnderlineDistance);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mUnderlineDistance!=0){
            float startX = getPaddingLeft();
            float endX = getWidth() - getPaddingRight();
            float baseline = getBaseline() + mUnderlineDistance+5.5f; // 下划线的位置稍微低于基线
            Log.e("zqr","baseline:"+baseline);
            canvas.drawLine(startX, baseline, endX, baseline, mPaint);
        }else {
            canvas.drawLine(0, 0, 0, 0, mPaint);
        }
    }

    public void setUnderlineDistance(float distance) {
        mUnderlineDistance = distance;
        mPaint.setStrokeWidth(distance);
        invalidate(); // 通知视图重新绘制
    }
}

