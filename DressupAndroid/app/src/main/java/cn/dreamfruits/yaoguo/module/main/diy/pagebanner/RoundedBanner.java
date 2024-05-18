package cn.dreamfruits.yaoguo.module.main.diy.pagebanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.youth.banner.Banner;

/**
 * @Author qiwangi
 * @Date 2023/6/19
 * @TIME 17:23
 *
 *
 * //用于继承banner 实现圆角  无效   用于以后设置自定义控件
 */
public class RoundedBanner extends Banner {

    private Path clipPath;

    public RoundedBanner(Context context) {
        super(context);
        init();
    }

    public RoundedBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        clipPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取控件的宽、高
        int w = getWidth();
        int h = getHeight();

        //创建一个圆角矩形路径
        RectF rectF = new RectF(0, 0, w, h);
        float radius = 20f; //圆角半径
        clipPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);

        //将canvas剪裁成圆角矩形区域
        canvas.clipPath(clipPath);

        //执行默认的onDraw()方法，绘制Banner内容
        super.onDraw(canvas);
    }
}

