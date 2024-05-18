package cn.dreamfruits.yaoguo.module.main.home.unity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author qiwangi
 * @Date 2023/6/8
 * @TIME 14:29
 */
public class ImageUtils {
    /**
     * 在图片边缘添加透明像素边框
     *
     * @param filePath 图片文件路径
     * @param borderWidth 边框像素大小
     * @return 返回添加边框后的图片的二进制数组
     */
    public static byte[] addTransparentBorder(String filePath, int borderWidth) {

        // 判断当前系统版本是否大于或等于 Android 11
        final boolean isAndroid11 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        int width = bitmap.getWidth() + borderWidth * 2;
        int height = bitmap.getHeight() + borderWidth * 2;

        Bitmap borderBitmap;
        if (isAndroid11) {
            //设备不支持Bitmap.Config.HARDWARE配置。Bitmap.Config.HARDWARE是Android 8.0引入的一种新型的位图配置，它可以在某些情况下提高位图处理性能。但是，如果你的设备不支持它，就会发生错误。
            //你可以尝试使用其他的位图配置，如Bitmap.Config.RGB_565或者Bitmap.Config.ARGB_8888，这两个配置都是比较常用的，并且兼容大部分Android设备。你也可以根据你的实际需求选择其他的位图配置。
            //修改代码为：
            //borderBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            //Bitmap.Config.RGB_565和Bitmap.Config.ARGB_8888是两种常用的位图配置，它们有以下区别：
            //Bitmap.Config.RGB_565使用16位的颜色通道，其中5个位用于表示红色，6个位用于表示绿色，5个位用于表示蓝色。因此它只能表示256^3 = 65536种不同的颜色，这相对来说比较少。但是由于它只使用了16位，所以它的占用空间相对较小，适合处理一些内存敏感的情况。
            //Bitmap.Config.ARGB_8888使用32位的颜色通道，其中8个位用于表示透明度，8个位用于表示红色，8个位用于表示绿色，8个位用于表示蓝色。这样它可以表示256^4 = 4294967296种不同的颜色，相对来说比较丰富。但是由于它使用了32位，所以它的占用空间相对较大，适合处理一些对内存占用不敏感的情况。
            //综上所述，你可以根据你的实际需求选择不同的位图配置。如果你的应用需要处理大量的位图，而且内存比较紧张，那么可以选择Bitmap.Config.RGB_565；如果你的应用需要表示非常丰富的颜色，而且内存比较充足，那么可以选择Bitmap.Config.ARGB_8888。
            borderBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } else {
            borderBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(borderBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        canvas.drawBitmap(bitmap, borderWidth, borderWidth, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            borderBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
