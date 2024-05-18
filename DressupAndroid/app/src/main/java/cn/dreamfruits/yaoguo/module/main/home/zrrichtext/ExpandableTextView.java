package cn.dreamfruits.yaoguo.module.main.home.zrrichtext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.dreamfruits.yaoguo.module.main.home.labeldetails.LabelDetailsActivity;
import cn.dreamfruits.yaoguo.module.main.home.vlistvideo.ListVideoActivity;
import cn.dreamfruits.yaoguo.util.Singleton;

/**
 * 使用方式如下
 */
//activity

// public class MainActivity extends AppCompatActivity {
 //
 //    @Override
 //    protected void onCreate(Bundle savedInstanceState) {
 //        super.onCreate(savedInstanceState);
 //        setContentView(R.layout.activity_main);
 //
 //        ExpandableTextView expandableTextView = findViewById(R.id.expanded_text);
 //        int viewWidth = getWindowManager().getDefaultDisplay().getWidth() - dp2px(this, 20f);
 //        expandableTextView.initWidth(viewWidth);
 //        expandableTextView.setMaxLines(3);
 //        expandableTextView.setHasAnimation(true);
 //        expandableTextView.setCloseInNewLine(true);
 //        expandableTextView.setOpenSuffixColor(getResources().getColor(R.color.colorAccent));
 //        expandableTextView.setCloseSuffixColor(getResources().getColor(R.color.colorAccent));
 //        expandableTextView.setOriginalText("在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
 //                "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
 //                "Preview版本后，国内刮起来一股学习Flutter的热潮。\n\n为了更好的方便帮助中国开发者了解这门新技术" +
 //                "，我们，Flutter中文网，前后发起了Flutter翻译计划、Flutter开源计划，前者主要的任务是翻译" +
 //                "Flutter官方文档，后者则主要是开发一些常用的包来丰富Flutter生态，帮助开发者提高开发效率。而时" +
 //                "至今日，这两件事取得的效果还都不错！"
 //        );
 //    }

/**

 ExpandableTextView expandableTextView = findViewById(R.id.expanded_text);
 int viewWidth = getWindowManager().getDefaultDisplay().getWidth() - dp2px(this, 50f);//
 expandableTextView.initWidth(viewWidth);
 expandableTextView.setHasAnimation(false);

 ArrayList<ExpandableTextView.RichTextBean> strList=new ArrayList<>();

 String temp="在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
 "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
 "Preview版本后，国内刮起来一股学习Flutter的热潮。\n\n为了更好的方便帮助中国开发者了解这门新技术" +
 "，我们，Flutter中文网，前后发起了Flutter翻译计划、Flutter开源计划，前者主要的任务是翻译" +
 "Flutter官方文档，后者则主要是开发一些常用的包来丰富Flutter生态，帮助开发者提高开发效率。而时" +
 "至今日，这两件事取得的效果还都不错！";

 //        String temp="在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
 //                "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
 //                "Preview版本后，";

 for(int i=0;i<temp.length();i++){
 if (i>10&&i<20){
 strList.add(new ExpandableTextView.RichTextBean(
 temp.split("")[i],
 i,
 0,
 "yg121212",
 "张三",
 "@",
 "#32CD32"
 ));
 }

 else if(i>30&&i<40){
 strList.add(new ExpandableTextView.RichTextBean(
 temp.split("")[i],
 i,
 0,
 "yg121212",
 "张三",
 "#",
 "#A020F0"
 ));

 }

 else {
 strList.add(new ExpandableTextView.RichTextBean(
 temp.split("")[i],
 i,
 0,
 "yg121212",
 "张三",
 "",
 ""
 ));
 }

 }

 //富文本点击事件回调
 expandableTextView.setOnRichTextClickListener(new ExpandableTextView.RichTextInterface() {
@Override
public void onclick(String contentStr, String type) {
if (type.equals("@")){
Toast.makeText(TempActivity.this,"点击了：  "+type+"   "+contentStr,Toast.LENGTH_SHORT).show();
Log.e("34543","点击了：  "+type+"   "+contentStr);
}

if (type.equals("#")){
Toast.makeText(TempActivity.this,"点击了：  "+type+"   "+contentStr,Toast.LENGTH_SHORT).show();
Log.e("34543","点击了：  "+type+"   "+contentStr);
}
}
});
 expandableTextView.setRichTextList(strList);

 expandableTextView.setOriginalText(temp);

 */



/**
 *
//根据手机的分辨率从 dp 的单位 转成为 px(像素)
public static int dp2px(Context context, float dpValue) {
        int res = 0;
final float scale = context.getResources().getDisplayMetrics().density;
        if (dpValue < 0)
        res = -(int) (-dpValue * scale + 0.5f);
        else
        res = (int) (dpValue * scale + 0.5f);
        return res;
        }
        }

 */

//xml

/**

// <?xml version="1.0" encoding="utf-8"?>
 <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:tools="http://schemas.android.com/tools"
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 xmlns:app="http://schemas.android.com/apk/res-auto"
 tools:ignore="MissingDefaultResource">

 <com.example.myapplication.zrrichtext.ExpandableTextView
 android:id="@+id/expanded_text"
 android:layout_width="0dp"
 android:layout_height="wrap_content"
 app:layout_constraintLeft_toLeftOf="parent"
 app:layout_constraintRight_toRightOf="parent"
 app:layout_constraintTop_toTopOf="parent"/>

 </androidx.constraintlayout.widget.ConstraintLayout>

 */



public class ExpandableTextView extends AppCompatTextView {
    public static final String ELLIPSIS_STRING = new String(new char[]{'\u2026'});//表示省略号
    volatile boolean animating = false;
    boolean isClosed = false;//是否关闭
    private int mMaxLines = 2;//最大行数
    private String mOpenStr = " 展开";
    private String mCloseStr = " 收起";
    private SpannableStringBuilder mOpenSpannableStr, mCloseSpannableStr;//展开 收起的富文本
    @Nullable
    private SpannableString mOpenSuffixSpan, mCloseSuffixSpan;
    private int mOpenHeight, mCLoseHeight;//展开的高度   关闭的高度
    private boolean mCloseInNewLine=true;//收起后缀 是否另起一行
    private boolean mExpandable;//是否可展开
    private int initWidth = 0;/** TextView可展示宽度，包含paddingLeft和paddingRight */

    private CharSequence originalText; /** 原始文本 */

    private boolean hasAnimation = false;
    private Animation mOpenAnim, mCloseAnim;
    private int mOpenSuffixColor=Color.parseColor("#B3B3B3"), mCloseSuffixColor=Color.parseColor("#B3B3B3");//展开和收起的颜色


    private ArrayList<RichTextBean> mRichTextBeanList=new ArrayList<>();

    private CharSequenceToSpannableHandler mCharSequenceToSpannableHandler;

    private Context mContext;

    public ExpandableTextView(Context context) {
        super(context);
        mContext=context;
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initialize();
    }

    /** 初始化 */
    private void initialize() {
        //LinkMovementMethod 是一个 Android 中的类，通常用于在 TextView 或其子类中处理链接（links）的点击事件。
        // 具体来说，当 TextView 中包含可点击的文本链接时，用户点击该链接后，如果没有设置 LinkMovementMethod，则无法响应点击事件。
        setMovementMethod(OverLinkMovementMethod.getInstance());

        //setIncludeFontPadding 是一个 Android 中的 View 类的方法，用于控制视图是否应该在其内容周围包含字体填充（font padding）。默认情况下，视图会将字体填充包含在内，以保持与文本相关的测量尺寸的一致性。但是，这可能会导致在一些场景下出现额外的空白区域。
        //通过调用 setIncludeFontPadding(false) 可以关闭字体填充。当字体填充被禁用后，视图的实际高度将等于视图内容的高度，不再包含额外的空白区域。通常情况下，如果您已经知道视图内容中不会包含任何换行符或文本对齐方面的需求，那么禁用字体填充可以帮助您更好地控制视图的布局。
        //需要注意的是，关闭字体填充可能会导致在某些情况下视图文字之间出现一些视觉上的挤压感，因此建议仅在必要时使用该方法，并在使用前进行一定的测试和评估。
        setIncludeFontPadding(false);

        //更新展开后缀Spannable
        updateOpenSuffixSpan();

        //更新收起后缀Spannable
        updateCloseSuffixSpan();
    }

    /**
     * hasOverlappingRendering() 是一个 Android View 类中的方法，用于检查此视图是否与其元素重叠渲染。在 Android 中，如果两个视图完全重叠，则只有前面的视图会接收到触摸事件。这可能会导致用户无法与期望的视图进行交互。
     * 因此，在布局视图时，可以使用 hasOverlappingRendering() 方法来避免视图之间的渲染重叠。如果返回值为 false，则表示此视图与其元素没有重叠渲染，否则返回 true。
     * 如果需要进一步优化应用程序的性能和内存占用，可以通过将此方法返回值设置为 false 来启用视图层次结构优化（View Hierarchy Optimization）。但是，在使用此功能之前，请确保您已经测试了您的应用程序，并且没有出现任何不期望的行为。
     * @return
     */
    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }



    public void setOriginalText(CharSequence originalText) {
        this.originalText = originalText;//通过方法设置原始文本

        mExpandable = false;//可展开 设置false

        mCloseSpannableStr = new SpannableStringBuilder();//关闭按钮富文本

        final int maxLines = mMaxLines;//设置最大行数

        SpannableStringBuilder tempText = charSequenceToSpannable(originalText);//temp富文本  设置的是原始文本


        /**
         * charSequenceToSpannable() 是一个 Android 中的方法，用于将 CharSequence 对象转换为 Spannable 对象。CharSequence 是一个接口，表示一系列字符序列，例如字符串、字符数组等。
         * Spannable 是另一个接口，它继承自 CharSequence，并且允许在文本中添加样式和其他属性，如颜色、字体大小、下划线等。
         * 使用 charSequenceToSpannable() 方法可以将原始的 CharSequence 对象包装成一个具有样式和属性的 Spannable 对象，以便于在 UI 组件中显示。
         */


        mOpenSpannableStr = charSequenceToSpannable(originalText);//open富文本
        /**
         * 插入给好的格式
         */
        SpannableStringBuilder mTempSpannableStr = new SpannableStringBuilder();
        mTempSpannableStr=insert(mOpenSpannableStr);
        mOpenSpannableStr=mTempSpannableStr;


        if (maxLines != -1) {//最大行数不等于-1
            /**
             * createStaticLayout() 是一个 Android 中的方法，用于创建一个静态的文字布局（layout）。它用于将文本字符串渲染到屏幕上，并且可以控制每行的长度、对齐方式等。
             * 该方法位于 android.text.StaticLayout 类中，其构造函数需要传递一些参数，如待渲染的字符串、字体、字体大小、每行最大宽度等。创建完静态布局后，可以使用 draw() 方法将其绘制在 Canvas 或其他画布上。
             * 相比于动态布局（Dynamic Layout），静态布局的优势是渲染速度更快、内存占用更小。因此，在不需要更新文本内容的场景下，使用静态布局可以提高应用程序的性能和响应速度。
             * 需要注意的是，由于静态布局无法自动调整文本大小以适应不同屏幕密度和设备方向，因此在使用时需要根据具体需求进行调整。
             */
            Layout layout = createStaticLayout(tempText);

            mExpandable = layout.getLineCount() > maxLines;//获取实际的行数 确定是否可以展开

            if (mExpandable) {
                //拼接展开内容
                if (mCloseInNewLine) {
                    mOpenSpannableStr.append("\n");//如果关闭按钮  另起一行   则添加一个换行符
                }
                if (mCloseSuffixSpan != null) {
                    mOpenSpannableStr.append(mCloseSuffixSpan);// 关闭按钮不等于空  添加后缀富文本
                }

                /**
                 * 关键代码 计算原文截取的位置
                 */
                int endPos = layout.getLineEnd(maxLines -1);//获取第几行 角标0开始的  的末尾这一段的字符个数
//                Log.e("7544342","endPos===layout.getLineEnd(maxLines -1)==maxLines=="+maxLines);
//                Log.e("7544342","endPos===layout.getLineEnd(maxLines -1)=="+endPos);


                if (originalText.length() <= endPos) {//原始文本的长度小于 上面3行的个数
                    mCloseSpannableStr = charSequenceToSpannable(originalText);
                } else {
                    mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, endPos));//截取3行的位置的字符
                }


                SpannableStringBuilder tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);//添加省略号

                if (mOpenSuffixSpan != null) {
                    tempText2.append(mOpenSuffixSpan); //在省略号后面添加  展开两字
                }


                //循环判断，收起内容添加展开后缀后的内容
                Layout tempLayout = createStaticLayout(tempText2);

                while (tempLayout.getLineCount() > maxLines) {//实际行数大于设定的行数  小于或等于就跳出
                    int lastSpace = mCloseSpannableStr.length() - 1;
                   /* Log.e("7544342","lastSpace=="+lastSpace);*/
                    if (lastSpace == -1) {
                        break;
                    }
                    if (originalText.length() <= lastSpace) {//原始文本的长度  小于等于 设置的富文本的长度
                        mCloseSpannableStr = charSequenceToSpannable(originalText);
                    } else {
                        mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace));
                    }

                    tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);

                    if (mOpenSuffixSpan != null) {
                        tempText2.append(mOpenSuffixSpan);
                    }
                    tempLayout = createStaticLayout(tempText2);

                }


                /*Log.e("7544342","tempText2=="+tempText2);//xxx国内刮起来… 展开*/
                int lastSpace = mCloseSpannableStr.length() - mOpenSuffixSpan.length();
                /*Log.e("7544342","mCloseSpannableStr=="+mCloseSpannableStr);//xxx国内刮起来*/

               /* Log.e("7544342","mOpenSuffixSpan=="+mOpenSuffixSpan);// 展开*/

                if(lastSpace >= 0 && originalText.length() > lastSpace){

                    CharSequence redundantChar = originalText.subSequence(lastSpace, lastSpace + mOpenSuffixSpan.length());//截取后面的文本

                    //统计 可见字符
                    int offset = hasEnCharCount(redundantChar) - hasEnCharCount(mOpenSuffixSpan) + 1;
                    lastSpace = offset <= 0 ? lastSpace : lastSpace - offset;


                    // TODO: 2023/6/28 临时处理   后续处理
                    try {
                        mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace));//xxx本后，国内
                    }catch (Exception  e){
                        Log.e("zqr","e=="+e.toString());
                        Log.e("zqr","originalText=="+originalText);
                    }
                    
                    /*Log.e("7544342","mCloseSpannableStr=="+mCloseSpannableStr);//至此计算工作完毕 */
                }


                //计算收起的文本高度
                mCLoseHeight = tempLayout.getHeight() + getPaddingTop() + getPaddingBottom();


                /**
                 * 插入给好的格式
                 */
                mTempSpannableStr=insert(mCloseSpannableStr);
                Log.e("dadasda",mTempSpannableStr.toString());
                mCloseSpannableStr=mTempSpannableStr;


                //收起状态
                //插入设置富文本


                mCloseSpannableStr.append(ELLIPSIS_STRING);

                if (mOpenSuffixSpan != null) {
                    mCloseSpannableStr.append(mOpenSuffixSpan);
                }

                //xxx本后，国内… 展开



            }
        }

        isClosed = mExpandable;
        if (mExpandable) {
            setText(mCloseSpannableStr);
            //设置监听
            super.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    switchOpenClose();
//                    if (mOnClickListener != null) {
//                        mOnClickListener.onClick(v);
//                    }
                }
            });
        } else {
            /**
             * 插入给好的格式
             */
            mTempSpannableStr=insert(mOpenSpannableStr);
            mOpenSpannableStr=mTempSpannableStr;
            setText(mOpenSpannableStr);
        }
        setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
    }


    /**
     * 插入富文本 并添加点击事件
     */
    private SpannableStringBuilder insert(SpannableStringBuilder mSpannableStr) {

        try {
            int length = mSpannableStr.toString().length();//获取截取的长度 用于遍历
            SpannableStringBuilder mTempSpannableStr = new SpannableStringBuilder(mSpannableStr.toString());

            if (mSpannableStr != null && mRichTextBeanList.size() != 0) {
                Log.e("dsda",mRichTextBeanList.toString());
                for (int i = 0; i < length; i++) {
                    if (!mRichTextBeanList.get(i).getType().equals("")) {//包含
                        String tempType=mRichTextBeanList.get(i).getType();
                        String tempmId=mRichTextBeanList.get(i).getmId();
                        String tempName=mRichTextBeanList.get(i).getName();
                        String tempStrColor=mRichTextBeanList.get(i).getColorStr();

                        mTempSpannableStr.setSpan(new ForegroundColorSpan(Color.parseColor(tempStrColor)), i, i+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        mTempSpannableStr.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                if (tempType.equals("@")){
                                    mRichTextInterface.onclick(tempmId,tempType);

                                    //跳转到个人主页

                                }else if (tempType.equals("#")){
//                                    mRichTextInterface.onclick(tempName,tempType);


                                    Intent intent = new Intent(mContext, LabelDetailsActivity.class);//视频列表页面
                                    intent.putExtra("type", tempType);
                                    intent.putExtra("label", tempName);
                                    intent.putExtra("id", tempmId);//string的id
                                    mContext.startActivity(intent);

                                }
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.parseColor(tempStrColor));
                                ds.setUnderlineText(false);
                            }
                        },i, i+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                }
            }

            return mTempSpannableStr;
        }catch (Exception e){
            Log.e("zqr",e.toString());
            return mSpannableStr;
        }
    }


    /**
     * 富文本点击回调
     */
    public interface RichTextInterface{
       void  onclick(String contentStr,String type);
    }
     RichTextInterface mRichTextInterface;

    public void setOnRichTextClickListener(RichTextInterface mRichTextInterface){
        this.mRichTextInterface=mRichTextInterface;
    }

    public void setRichTextList(ArrayList<RichTextBean> mList) {
        this.mRichTextBeanList = mList;
    }
    //这是啥
    public void setRichTextBeanList(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    /**
     * 统计可见字符
     * 这段代码的作用是计算字符串 "str" 中可见字符的数量（即 ASCII 码表中的 0x20 到 0x7E 范围内的字符）。代码首先使用 Android SDK 中的 TextUtils.isEmpty() 方法检查字符串是否为空或 null。如果字符串不为空，则遍历字符串中的每个字符，并将每个可见字符计入变量 "count" 中。
     * 在循环体中，代码通过使用 String 类的 charAt() 方法获取字符串中位置为 i 的字符，并将其赋值给变量 "c"。然后，使用条件语句来检查该字符是否在可见字符范围内（即ASCII码表中的 0x20 到 0x7E 范围内）。如果该字符在此范围内，则将变量 "count" 加 1。
     * 最终，当循环结束时，变量 "count" 包含了字符串中可见字符的数量。
     */
    private int hasEnCharCount(CharSequence str){
        int count = 0;
        if(!TextUtils.isEmpty(str)){
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if(c >= ' ' && c <= '~'){
                    count++;
                }
            }
        }
        return count;
    }

    private void switchOpenClose() {
        if (mExpandable) {
            isClosed = !isClosed;
            if (isClosed) {
                close();
            } else {
                open();
            }
        }
    }

    /**
     * 设置是否有动画
     *
     * @param hasAnimation
     */
    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    /** 展开 */
    private void open() {
        if (hasAnimation) {
            Layout layout = createStaticLayout(mOpenSpannableStr);
            mOpenHeight = layout.getHeight() + getPaddingTop() + getPaddingBottom();
            executeOpenAnim();
        } else {
            ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
            setText(mOpenSpannableStr);
            if (mOpenCloseCallback != null){
                mOpenCloseCallback.onOpen();
            }
        }
    }

    /** 收起 */
    private void close() {
        if (hasAnimation) {
            executeCloseAnim();
        } else {
            ExpandableTextView.super.setMaxLines(mMaxLines);
            setText(mCloseSpannableStr);
            if (mOpenCloseCallback != null){
                mOpenCloseCallback.onClose();
            }
        }
    }

    /** 执行展开动画 */
    private void executeOpenAnim() {
        //创建展开动画
        if (mOpenAnim == null) {
            mOpenAnim = new ExpandCollapseAnimation(this, mCLoseHeight, mOpenHeight);
            mOpenAnim.setFillAfter(true);
            mOpenAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
                    setText(mOpenSpannableStr);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //  动画结束后textview设置展开的状态
                    getLayoutParams().height = mOpenHeight;
                    requestLayout();
                    animating = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if (animating) {
            return;
        }
        animating = true;
        clearAnimation();
        //  执行动画
        startAnimation(mOpenAnim);
    }

    /** 执行收起动画 */
    private void executeCloseAnim() {
        //创建收起动画
        if (mCloseAnim == null) {
            mCloseAnim = new ExpandCollapseAnimation(this, mOpenHeight, mCLoseHeight);
            mCloseAnim.setFillAfter(true);
            mCloseAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                    ExpandableTextView.super.setMaxLines(mMaxLines);
                    setText(mCloseSpannableStr);
                    getLayoutParams().height = mCLoseHeight;
                    requestLayout();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if (animating) {
            return;
        }
        animating = true;
        clearAnimation();
        //  执行动画
        startAnimation(mCloseAnim);
    }

    /**
     * @param spannable
     *
     * @return
     */
private Layout createStaticLayout(SpannableStringBuilder spannable) {
    int contentWidth = initWidth - getPaddingLeft() - getPaddingRight();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        StaticLayout.Builder builder = StaticLayout.Builder.obtain(spannable, 0, spannable.length(), getPaint(), contentWidth);
        builder.setAlignment(Layout.Alignment.ALIGN_NORMAL);
        builder.setIncludePad(getIncludeFontPadding());
        builder.setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier());
        return builder.build();
    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        return new StaticLayout(spannable, getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL,
                getLineSpacingMultiplier(), getLineSpacingExtra(), getIncludeFontPadding());
    }else{
        return new StaticLayout(spannable, getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL,
                getFloatField("mSpacingMult",1f), getFloatField("mSpacingAdd",0f), getIncludeFontPadding());
    }
}

    private float getFloatField(String fieldName,float defaultValue){
        float value = defaultValue;
        if(TextUtils.isEmpty(fieldName)){
            return value;
        }
        try {
            // 获取该类的所有属性值域
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field:fields) {
                if(TextUtils.equals(fieldName,field.getName())){
                    value = field.getFloat(this);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private SpannableStringBuilder charSequenceToSpannable(@NonNull CharSequence charSequence) {
        SpannableStringBuilder spannableStringBuilder = null;
        if (mCharSequenceToSpannableHandler != null) {
            spannableStringBuilder = mCharSequenceToSpannableHandler.charSequenceToSpannable(charSequence);
        }
        if (spannableStringBuilder == null) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        }
        return spannableStringBuilder;
    }

    /**初始化TextView的可展示宽度*/
    public void initWidth(int width) {
        initWidth = width;
    }

    /** 更新展开后缀Spannable */
    private void updateOpenSuffixSpan() {
        if (TextUtils.isEmpty(mOpenStr)) {
            mOpenSuffixSpan = null;
            return;
        }
        mOpenSuffixSpan = new SpannableString(mOpenStr);
        mOpenSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mOpenStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mOpenSuffixSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                switchOpenClose();//点击事件
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mOpenSuffixColor);
                ds.setUnderlineText(false);
            }
        },0, mOpenStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /** 更新收起后缀Spannable */
    private void updateCloseSuffixSpan() {
        if (TextUtils.isEmpty(mCloseStr)) {
            mCloseSuffixSpan = null;
            return;
        }
        mCloseSuffixSpan = new SpannableString(mCloseStr);
        mCloseSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mCloseStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mCloseInNewLine) {
            // AlignmentSpan 类有几个子类，允许不同类型的对齐方式，包括
            // Layout.Alignment.ALIGN_NORMAL、
            // Layout.Alignment.ALIGN_OPPOSITE 和
            // Layout.Alignment.ALIGN_CENTER。这些子类可用于指定给定文本范围的所需对齐行为。
            AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);//另起一行之后 在末尾 靠右边

            mCloseSuffixSpan.setSpan(alignmentSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mCloseSuffixSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                switchOpenClose();//点击事件
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mCloseSuffixColor);
                ds.setUnderlineText(false);
            }
        },1, mCloseStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }
    /**
     * 设置展开后缀text
     *
     * @param openSuffix
     */
    public void setOpenSuffix(String openSuffix) {
        mOpenStr = openSuffix;
        updateOpenSuffixSpan();
    }

    /**
     * 设置展开后缀文本颜色
     *
     * @param openSuffixColor
     */
    public void setOpenSuffixColor(@ColorInt int openSuffixColor) {
        mOpenSuffixColor = openSuffixColor;
        updateOpenSuffixSpan();
    }

    /**
     * 设置收起后缀text
     *
     * @param closeSuffix
     */
    public void setCloseSuffix(String closeSuffix) {
        mCloseStr = closeSuffix;
        updateCloseSuffixSpan();
    }

    /**
     * 设置收起后缀文本颜色
     *
     * @param closeSuffixColor
     */
    public void setCloseSuffixColor(@ColorInt int closeSuffixColor) {
        mCloseSuffixColor = closeSuffixColor;
        updateCloseSuffixSpan();
    }


    /**展开收起的回调*/
    public OpenAndCloseCallback mOpenCloseCallback;
    public void setOpenAndCloseCallback(OpenAndCloseCallback callback){
        this.mOpenCloseCallback = callback;
    }
    public interface OpenAndCloseCallback{
        void onOpen();
        void onClose();
    }

    /**设置文本内容处理*/
    public void setCharSequenceToSpannableHandler(CharSequenceToSpannableHandler handler) {
        mCharSequenceToSpannableHandler = handler;
    }
    public interface CharSequenceToSpannableHandler {
        @NonNull
        SpannableStringBuilder charSequenceToSpannable(CharSequence charSequence);
    }

    /**动画*/
    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;//动画执行view
        private final int mStartHeight;//动画执行的开始高度
        private final int mEndHeight;//动画结束后的高度

        ExpandCollapseAnimation(View target, int startHeight, int endHeight) {
            mTargetView = target;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(400);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mTargetView.setScrollY(0);
            //计算出每次应该显示的高度,改变执行view的高度，实现动画
            mTargetView.getLayoutParams().height = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.requestLayout();
        }
    }


    /**
     * 内部类
     */
    public static class RichTextBean {
        private String singleStr;
        private int index;
        private int len;
        private String mId;//@是用户id  #是话题id
        private String name;


        private String type;//用于标记 点击事件使用 id  还是  name

        private String colorStr;//文本颜色#099089


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "AtTextBean=>|" + singleStr +"|"+ index +"|"+ len +"|"+ mId +"|"+ name+"|"+type+"|"+colorStr;
        }

        public RichTextBean(String singleStr, int index, int len, String mId, String name) {
            this.singleStr = singleStr;
            this.index = index;
            this.len = len;
            this.mId = mId;
            this.name = name;
        }

        public RichTextBean(String singleStr, int index, int len, String mId, String name, String type, String colorStr) {
            this.singleStr = singleStr;
            this.index = index;
            this.len = len;
            this.mId = mId;
            this.name = name;
            this.type = type;
            this.colorStr = colorStr;
        }

        public String getColorStr() {
            return colorStr;
        }

        public void setColorStr(String colorStr) {
            this.colorStr = colorStr;
        }

        public String getSingleStr() {
            return singleStr;
        }

        public void setSingleStr(String singleStr) {
            this.singleStr = singleStr;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String getmId() {
            return mId;
        }

        public void setmId(String mId) {
            this.mId = mId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
    public static int dp2px(Context context, float dpValue) {
        int res = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        if (dpValue < 0)
            res = -(int) (-dpValue * scale + 0.5f);
        else
            res = (int) (dpValue * scale + 0.5f);
        return res;
    }

}
