package cn.dreamfruits.yaoguo.module.main.home.commentreplay;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * @Author qiwangi
 * @Date 2023/4/19
 * @TIME 14:08
 * 用于监听光标的edit
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
