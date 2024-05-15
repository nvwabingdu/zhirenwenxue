package com.example.zrtool.utilsjava;

/**
 * @Author qiwangi
 * @Date 2023/8/24
 * @TIME 20:50
 */
public class ColorUtils {

    /**
     * @param colorText
     * @return
     */
    public static int[] convertColorTextToRGB(String colorText) {
        int[] rgbValues = new int[3];

        // 移除颜色文本中的非十六进制字符
        colorText = colorText.replaceAll("[^A-Fa-f0-9]", "");

        // 检查颜色文本的长度
        int colorLength = colorText.length();
        if (colorLength == 3 || colorLength == 6) {
            // 处理三个十六进制字符的情况
            if (colorLength == 3) {
                colorText = colorText.charAt(0) + "" + colorText.charAt(0) +
                        colorText.charAt(1) + "" + colorText.charAt(1) +
                        colorText.charAt(2) + "" + colorText.charAt(2);
            }

            // 解析颜色文本为 RGB 值
            try {
                rgbValues[0] = Integer.parseInt(colorText.substring(0, 2), 16);
                rgbValues[1] = Integer.parseInt(colorText.substring(2, 4), 16);
                rgbValues[2] = Integer.parseInt(colorText.substring(4, 6), 16);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return rgbValues;
    }
}
