package com.example.zrtool.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @Author qiwangi
 * @Date 2023/4/19
 * @TIME 14:08
 * 用于监听光标的edit (作用使光标不能插入到某些位置或者其他操作)
 */
public class NoteContentEditText extends EditText {

    public NoteContentEditText(Context context) {
        super(context);
    }

    public NoteContentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteContentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NoteContentEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        /*Log.e("zqr","onSelectionChanged selStart "+selStart+" selEnd "+selEnd);*/
        if (mSelectionChanged!=null){
            mSelectionChanged.onclick(selStart);
        }
    }

    private SelectionChanged mSelectionChanged=null;
    public interface  SelectionChanged{
        void onclick(int selStart);
    }
    public void setOnSelectionChanged(SelectionChanged mSelectionChanged){
        this.mSelectionChanged=mSelectionChanged;
    }
}

/**
 <cn.dreamfruits.yaoguo.module.main.home.commentreplay.NoteContentEditText
 android:paddingTop="10dp"
 android:paddingBottom="10dp"
 android:hint=" 欢迎您的友善评论……"
 android:id="@+id/comment_edittext"
 android:background="@null"
 android:textColorHint="#E6E6E6"
 android:maxLines="4"
 android:textSize="14sp"
 android:layout_marginRight="64dp"
 android:layout_marginLeft="20dp"
 android:layout_width="match_parent"
 android:layout_height="wrap_content" />


 NoteContentEditText editText=inflate.findViewById(R.id.comment_edittext);//输入框


 editText.setOnSelectionChanged(new NoteContentEditText.SelectionChanged() {
@Override
public void onclick(int selStart) {
try {
int tempNum=selStart;
if (selStart<atUserList.size()){
if (atUserList.get(selStart).getId()==0){
return;
}
for(int i=selStart;i<atUserList.size();i++){
if (atUserList.get(i).getId()!=0){
tempNum++;
}else{
break;
}
}
}
editText.setSelection(tempNum+1);
}catch (Exception e){
//异常不管
}
}
});


 * */