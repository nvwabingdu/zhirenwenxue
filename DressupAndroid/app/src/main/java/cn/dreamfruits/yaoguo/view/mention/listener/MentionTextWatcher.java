package cn.dreamfruits.yaoguo.view.mention.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;


import java.util.Iterator;

import cn.dreamfruits.yaoguo.view.mention.MentionEditText;
import cn.dreamfruits.yaoguo.view.mention.bean.MentionTopic;
import cn.dreamfruits.yaoguo.view.mention.bean.Range;
import cn.dreamfruits.yaoguo.view.mention.util.RangeManager;


public class MentionTextWatcher implements TextWatcher {
    private final MentionEditText mEditText;
    private final RangeManager mRangeManager;

    /**
     * 1 = @
     * 2 = #
     */
    private int mCurrentType = -1;

//    private static final String USER_REGEX = "@[^@#\\s]{0,20}?$";     //匹配最后一个@

    public MentionTextWatcher(MentionEditText editText) {
        this.mEditText = editText;
        this.mRangeManager = mEditText.getRangeManager();
    }

    //若从整串string中间插入字符，需要将插入位置后面的range相应地挪位
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Editable editable = mEditText.getText();
//        LogUtils.e("-->>beforeTextChanged  start=" + start + " , count=" + count + " , after=" + after);
        //相同位置增加
        if (count == 0 && (mEditText.getMentionInputConnection() != null && start + 1 == mEditText.getMentionInputConnection().getLastIndex())) {
            mEditText.getMentionInputConnection().resetLastIndex();
        }
        //在末尾增加就不需要处理了
        if (start < editable.length()) {
            int end = start + count;
            int offset = after - count;

            //清理start 到 start + count之间的span
            //如果range.from = 0，也会被getSpans(0,0,ForegroundColorSpan.class)获取到
            if (start != end && !mRangeManager.isEmpty()) {
                ForegroundColorSpan[] spans = editable.getSpans(start, end, ForegroundColorSpan.class);
                for (ForegroundColorSpan span : spans) {
                    editable.removeSpan(span);
                }
            }

            //清理arraylist中上面已经清理掉的range
            //将end之后的span往后挪offset个位置
            Iterator iterator = mRangeManager.iterator();
            while (iterator.hasNext()) {
                Range range = (Range) iterator.next();
                if (range.isWrapped(start, end)) {
                    iterator.remove();
                    continue;
                }

                if (range.getFrom() >= end) {
                    range.setOffset(offset);
                }
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        int selectionStart = mEditText.getSelectionStart();
        if (selectionStart > 0) {
            char tag = charSequence.charAt(selectionStart - 1);

            //如果是@ 和 空格则不做处理
            if (tag == ' ' || tag == '@' || tag == '\n') {
                Log.i("TAG11", "onTextChanged: close");
                mEditText.getEditDataListener().onCloseSearchView();
                return;
            }

            int index = -1;
            //光标内容第左边第1个开始
            for (int i = selectionStart - 1; i >= 0; i--) {
                if (charSequence.charAt(i) == '#' || charSequence.charAt(i) == '@' || charSequence.charAt(i) == ' ') {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                if (charSequence.charAt(index) == '#') {
                    String topic = charSequence.toString().substring(index + 1, selectionStart);

                    mEditText.insertRecover(new MentionTopic("undefined", topic), index, topic.length() + 1);

                    Log.i("TAG11", "topic " + topic + "-- length" + topic.length() + "-----index" + index);
                }
            }

        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int selectionStart = mEditText.getSelectionStart();
        if (selectionStart > 0) {
            for (int i = selectionStart - 1; i >= 0; i--) {
                char c = editable.charAt(i);

                if (c == ' ' || c == '\n'){
                    return;
                }

                String content = editable.toString().substring(i + 1, selectionStart);
                if (c == '#') {
                    if (mEditText.getEditDataListener() != null) {
                        mEditText.getEditDataListener().onEditAddHashtag(content);
                    }
                    Log.i("TAG22", "onTopicClick: " + content);
                    break;
                } else if (c == '@' && !content.endsWith(" ")) {
                    if (mEditText.getEditDataListener() != null) {
                        mEditText.getEditDataListener().onEditAddAt(content);
                    }
                    Log.i("TAG22", "onAtUserClick: " + content);
                    break;
                }
            }
        }

    }

    /**
     * 全角转换为半角
     *
     * @param input
     * @return
     */
    public String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * @param input 任意字符串
     * @return 全角字符串
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

}
