package cn.dreamfruits.yaoguo.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author qiwangi
 * @Date 2023/4/9
 * @TIME 16:12
 * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)、
 * Spannable.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)、
 * Spannable.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)、
 * Spannable.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)这几个参数。
 */
public class SpannableStringUtils {

    /**
     * 1.目前仅设置了 #  @
     * 使用方式 SpannableStringUtils.setRichTextAndOnclick(textView,text,this,new String[]{"#","@"});
     *
     * //可用下面文本测试
     * String temp = "!@富文@  本不换@@ @@ 行的原因*#@#*@@可能有以@下几 种2121#dadad 32131###dtdad d****ad ddad**ada ###这种*类型       32131###dadad dad ddadada#98  ##这： 样式$设 $置问题 $：   $$$富 文 本#中的 样式设置可能#包含了 禁#7878789 止换行的属##@ @性 ，例如 #white-space : nowrap;。这会使得文@454 本*内容无 *论到哪 @@dadda @@ 个位置都不会@ @@自动换行。 编辑器限制：某些富文本编辑#89809 #--- 器可能具有默认的不换行行为，或者没有提供@-9980- 更改文!@3788 323本换行方式的选项。 硬编码问题：在 HTML 或富#=-=-= 文本编辑器中硬编码的#空格或换 行符可能会导致文本不换行，特别是当这$些字符 出现$在长 单词$$$或 URL 上时。 文本过长：如果富文本#中的 文本##内容非常长，它可能会#在一行上超出 容器的边界，从而导致不换行的现象发生。 容器宽度问题：如果富$文本所在的 容器宽度设置不合适，也可能导致文本内容不换行，可以尝试增加容器宽度或调整布局以适应更多的文本内容。";
     *
     *
     * @param textView
     * @param str
     * @param context
     * @param tag      # @   规则为#dadada 空格结尾   @dadada 空格结尾
     */
    public static void setRichTextAndOnclick(TextView textView, String str, Context context, String[] tag) {
        textView.setText(SpannableStringUtils.getSpannableString(str, context, tag));
        textView.setMovementMethod(LinkMovementMethod.getInstance());// 必须设置该方法才能响应点击事件
        textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
    }

    /**
     * 用于输入文本view
     * @param editText
     * @param str
     * @param context
     * @param tag
     */
    public static void setRichTextAndOnclick(EditText editText, String str, Context context, String[] tag) {
        editText.setText(SpannableStringUtils.getSpannableString(str, context, tag));
        editText.setMovementMethod(LinkMovementMethod.getInstance());// 必须设置该方法才能响应点击事件
        editText.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
    }

    /**
     * 2.设置可点击文本，#dada @34222
     */
    public static SpannableString getSpannableString(String temp, Context mContext, String[] tag) {
        SpannableString spannableString = new SpannableString(temp);
        for (int index = 0; index < tag.length; index++) {

            if (temp.contains(" ") && temp.contains(tag[index])) {//包含符合条件的#088909 这种情况
                int patternIndex=0;//用于角标累加

                for (int i = 0; i < temp.split(" ").length; i++) {
                    String splitStr=temp.split(" ")[i];//拆分出来的单独的

                    Log.e("dyfg134987", "============================="+splitStr);
                    if(splitStr.equals("")){
                        patternIndex+=1;
                    }else {
                        patternIndex+=splitStr.length();
                    }

                    if (splitStr.contains(tag[index])) {//￥  %   #   ￥
                        if (splitStr.lastIndexOf(tag[index]) != splitStr.length() - 1) {

                            String pattern = splitStr.substring(splitStr.lastIndexOf(tag[index]));//符合条件的子字符串

                            boolean isOK = true;
                            for (int m = 0; m < tag.length; m++) {//排除#@*#dadda 这种情况

                                if (!tag[m].equals(tag[index]) && pattern.contains(tag[m])) {
                                    Log.e("dyfg655", tag[m]+"====="+tag[index]+"===="+pattern);
                                    isOK = false;
                                }
                            }

                            if (isOK) {
                                Log.e("dyfg65", pattern);
                                spannableString = getPattern(spannableString,temp, pattern, mContext, tag[index]);//找到符合条件的子字符串的对应角标
                            }
                        }
                    }
                }
            }
        }
        return spannableString;
    }

    /**
     * 3.通过正则匹配 查找上文符合条件的子字符串  并返回角标
     */
    public static SpannableString getPattern(SpannableString spannableString,String temp, String pattern, Context mContext, String tag) {

        Log.e("323232", ""+pattern+"   ");
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(temp);

        int[] indices = new int[100];
        int i = 0;

        while (m.find()) {
            indices[i++] = m.start();
        }

        if (i != 0) {
            System.out.print("Matches found at indices: ");
            for (int j = 0; j < i; j++) {

                /**
                 * 过来的都是
                 * $设 $置问题 $： $富 $些字符 $在长 $或 $文本所在的 #*@@可能有以@下几 #dadad #dtdad #这种*类型 #dadad #98 #这： #中 #包含了 #7878789 *@@可能有以@下几
                 */
                spannableString.setSpan(new ClickableSpan() {//设置颜色 点击事件
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        if (tag.equals("#")) {
                            ds.setColor(Color.parseColor("#FF00ff00"));//设置颜色
                        } else if (tag.equals("@")) {
                            ds.setColor(Color.parseColor("#FFff0000"));//设置颜色
                        } else {
                            ds.setColor(Color.parseColor("#FF0fff00"));//设置颜色
                        }
                        ds.setUnderlineText(false);//去掉下划线
                    }

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, pattern, Toast.LENGTH_SHORT).show();//点击事件
                    }
                }, indices[j], indices[j] + pattern.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                Log.e("9876", ""+pattern+"   "+indices[j]+"   "+indices[j] + pattern.length());
            }
        }

        return spannableString;
    }
}
