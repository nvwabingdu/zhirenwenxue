package cn.dreamfruits.yaoguo.module.main.message.chart.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.shuyu.waveview.FileUtils;
import com.tencent.imsdk.base.ThreadUtils;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.FaceElement;
import com.tencent.imsdk.message.FileElement;
import com.tencent.imsdk.message.GroupTipsElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.LocationElement;
import com.tencent.imsdk.message.MergerElement;
import com.tencent.imsdk.message.SoundElement;
import com.tencent.imsdk.message.TextElement;
import com.tencent.imsdk.message.VideoElement;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMTextElem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dreamfruits.yaoguo.R;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.QuoteBean;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.TIMCustomElem;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.TIMImageElem;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.TIMSoundElem;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.TIMTextElem;
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.emoji.Emoji;
import cn.dreamfruits.yaoguo.module.main.message.chart.fragment.FaceFragment;
import cn.dreamfruits.yaoguo.module.main.message.chart.interfaces.ChatInputMoreListener;
import cn.dreamfruits.yaoguo.module.main.message.chart.manage.CustomFace;
import cn.dreamfruits.yaoguo.module.main.message.chart.manage.FaceManager;
import cn.dreamfruits.yaoguo.module.main.message.chart.utils.Mp3RecordUtils;
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants;
import cn.dreamfruits.yaoguo.module.main.message.utils.ChatMessageBuilder;
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity;

/**
 * @author Lee
 * @createTime 2023-06-28 15 GMT+8
 * @desc :
 */
public class InputView extends LinearLayout implements View.OnClickListener, TextWatcher {

    protected AppCompatActivity mActivity;

    private ImageView iv_switch_speak;
    private TextView mSendTextButton;
    private ImageView iv_photo;
    private ImageView iv_pic;
    private LinearLayout llayout_content;
    private TIMMentionEditText mTextInput;
    private TextView mSendAudioButton;
    private ImageView mEmojiInputButton;
    private LinearLayout llayout_quote;
    private TextView tv_quote_content;
    private ImageView iv_delete_quote;
    protected View mInputMoreView;


    private int mCurrentState;
    private static final int STATE_NONE_INPUT = -1;
    private static final int STATE_SOFT_INPUT = 0;
    private static final int STATE_VOICE_INPUT = 1;
    private static final int STATE_FACE_INPUT = 2;
    private static final int STATE_ACTION_INPUT = 3;

    private ChatInputHandler mChatInputHandler;

    private OnInputViewListener mOnInputViewListener;
    private ChatInputMoreListener chatInputMoreListener;
    private MessageHandler mMessageHandler;


    private boolean isQuoteModel = false;

    private boolean mAudioCancel;

    private float mStartRecordY;

    private String displayInputString;

    public static boolean isShowRead = false;


    public InputView(Context context) {
        super(context);
        initViews();
    }

    public InputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }


    private void initViews() {


        mActivity = (AppCompatActivity) getContext();
        inflate(mActivity, R.layout.layout_chart_input_view, this);
        mInputMoreView = findViewById(R.id.more_groups);
        iv_switch_speak = findViewById(R.id.iv_switch_speak);
        mSendTextButton = findViewById(R.id.tv_send);
        iv_photo = findViewById(R.id.iv_photo);
        iv_pic = findViewById(R.id.iv_pic);
        llayout_content = findViewById(R.id.llayout_content);
        mTextInput = findViewById(R.id.et_content);
        mSendAudioButton = findViewById(R.id.tv_touch_speak);
        mEmojiInputButton = findViewById(R.id.iv_emoge);
        llayout_quote = findViewById(R.id.llayout_quote);
        tv_quote_content = findViewById(R.id.tv_quote_content);
        iv_delete_quote = findViewById(R.id.iv_delete_quote);


        // TODO: 2023/7/14 删除 
        iv_switch_speak.setEnabled(false);
        

        init();
    }

    private void init() {
        iv_switch_speak.setOnClickListener(this);
        mSendTextButton.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        mEmojiInputButton.setOnClickListener(this);

        mTextInput.addTextChangedListener(this);

        mTextInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //滑动到最新一条消息
//                    if (presenter != null) {
//                        presenter.scrollToNewestMessage();
//                    }
                    showSoftInput();
                }
                return false;
            }
        });


        mTextInput.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((isQuoteModel) && TextUtils.isEmpty(mTextInput.getText().toString())) {
                        exitQuote();
                    }
                }
                return false;
            }
        });
        mTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        mTextInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus && mChatInputHandler != null) {
                    mChatInputHandler.onUserTyping(false, V2TIMManager.getInstance().getServerTime());
                }
            }
        });

        Mp3RecordUtils.getInstence(mActivity)
                .setmOnRecordListener(new Mp3RecordUtils.OnRecordListener() {
                    @Override
                    public boolean onStart(String path) {
                        return true;
                    }

                    @Override
                    public boolean onFinish(String path, long time) {
                        LogUtils.e(">>>> onFinish = " + path + " ---  " + time);
                        recordComplete(true, path, time);
                        return true;
                    }

                    @Override
                    public void onCancel() {
                        LogUtils.e(">>>> onCancel");
                        recordComplete(false, "", 0);
                    }

                    @Override
                    public void onFailure() {
                        LogUtils.e(">>>> onFailure");
                        recordComplete(false, "", 0);
                    }
                });

        mSendAudioButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                PermissionX.init(mActivity)
                        .permissions(Manifest.permission.RECORD_AUDIO)
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                                 @NonNull List<String> deniedList) {
                                if (allGranted) {
                                    switch (motionEvent.getAction()) {

                                        case MotionEvent.ACTION_DOWN:

                                            mAudioCancel = true;
                                            mStartRecordY = motionEvent.getY();
                                            if (mChatInputHandler != null) {
                                                mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_START);
                                            }
                                            mSendAudioButton.setText("松开结束");
                                            Mp3RecordUtils.getInstence(mActivity).startRecording();


                                            break;
                                        case MotionEvent.ACTION_MOVE:

                                            if (motionEvent.getY() - mStartRecordY < -100) {
                                                mAudioCancel = true;
                                                if (mChatInputHandler != null) {
                                                    mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_CANCEL);
                                                }
                                            } else {
                                                if (mAudioCancel) {
                                                    if (mChatInputHandler != null) {
                                                        mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_START);
                                                    }
                                                }
                                                mAudioCancel = false;
                                            }
                                            mSendAudioButton.setText("松开结束");
                                            break;
                                        case MotionEvent.ACTION_CANCEL:
                                        case MotionEvent.ACTION_UP:

                                            mAudioCancel = motionEvent.getY() - mStartRecordY < -100;
                                            if (mChatInputHandler != null) {
                                                mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_STOP);
                                            }

                                            Mp3RecordUtils.getInstence(mActivity).finishRecording();

                                            mSendAudioButton.setText("按住说话");
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    ToastUtils.showShort("未开启语音录制权限，请前往设置打开");
                                }
                            }
                        });


                return true;
            }
        });

        mTextInput.setOnMentionInputListener(new TIMMentionEditText.OnMentionInputListener() {
            @Override
            public void onMentionCharacterInput(String tag) {
                //群聊使用
//                if ((tag.equals(TIMMentionEditText.TIM_MENTION_TAG) || tag.equals(TIMMentionEditText.TIM_MENTION_TAG_FULL))
//                        && TUIChatUtils.isGroupChat(mChatLayout.getChatInfo().getType())) {
//                    if (mOnInputViewListener != null) {
//                        mOnInputViewListener.onStartGroupMemberSelectActivity();
//                    }
//                }
            }
        });

        iv_delete_quote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exitQuote();
            }
        });

    }

    public void addInputText(String name, String id) {
        if (id == null || id.isEmpty()) {
            return;
        }

        ArrayList<String> nameList = new ArrayList<String>() {
            {
                add(name);
            }
        };
        ArrayList<String> idList = new ArrayList<String>() {
            {
                add(id);
            }
        };

        updateAtUserInfoMap(nameList, idList);
        if (mTextInput != null) {
            Map<String, String> displayAtNameMap = new HashMap<>();
            displayAtNameMap.put(TIMMentionEditText.TIM_MENTION_TAG + displayInputString, id);
            mTextInput.setMentionMap(displayAtNameMap);
            int selectedIndex = mTextInput.getSelectionEnd();
            if (selectedIndex != -1) {
                String insertStr = TIMMentionEditText.TIM_MENTION_TAG + displayInputString;
                String text = mTextInput.getText().insert(selectedIndex, insertStr).toString();
                FaceManager.handlerEmojiText(mTextInput, text, true);
                mTextInput.setSelection(selectedIndex + insertStr.length());
            }
            showSoftInput();
        }
    }

    public void updateInputText(ArrayList<String> names, ArrayList<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        updateAtUserInfoMap(names, ids);
        if (mTextInput != null) {
            Map<String, String> displayAtNameList = getDisplayAtNameMap(names, ids);
            mTextInput.setMentionMap(displayAtNameList);

            int selectedIndex = mTextInput.getSelectionEnd();
            if (selectedIndex != -1) {
                String text = mTextInput.getText().insert(selectedIndex, displayInputString).toString();
                FaceManager.handlerEmojiText(mTextInput, text, true);
                mTextInput.setSelection(selectedIndex + displayInputString.length());
            }
            // @ 之后要显示软键盘。Activity 没有 onResume 导致无法显示软键盘
            // Afterwards @, the soft keyboard is to be displayed. Activity does not have onResume, so the soft keyboard cannot be displayed
            ThreadUtils.postOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftInput();
                }
            }, 200);
        }
    }

    private Map<String, String> getDisplayAtNameMap(List<String> names, List<String> ids) {
        Map<String, String> displayNameList = new HashMap<>();
        String mentionTag = TIMMentionEditText.TIM_MENTION_TAG;
        if (mTextInput != null) {
            Editable editable = mTextInput.getText();
            int selectionIndex = mTextInput.getSelectionEnd();
            if (editable != null && selectionIndex > 0) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)) {
                    mentionTag = text.substring(selectionIndex - 1, selectionIndex);
                }
            }
        }

        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                if (TextUtils.isEmpty(names.get(0))) {
                    displayNameList.put(mentionTag + ids.get(0) + " ", ids.get(0));
                } else {
                    displayNameList.put(mentionTag + names.get(0) + " ", ids.get(0));
                }
                continue;
            }
            String str = TIMMentionEditText.TIM_MENTION_TAG;
            if (TextUtils.isEmpty(names.get(i))) {
                str += ids.get(i);
            } else {
                str += names.get(i);
            }
            str += " ";
            displayNameList.put(str, ids.get(i));
        }
        return displayNameList;
    }


    private Map<String, String> atUserInfoMap = new HashMap<>();

    private void updateAtUserInfoMap(ArrayList<String> names, ArrayList<String> ids) {
        displayInputString = "";

        for (int i = 0; i < ids.size(); i++) {
            atUserInfoMap.put(ids.get(i), names.get(i));

            // for display
            if (TextUtils.isEmpty(names.get(i))) {
                displayInputString += ids.get(i);
                displayInputString += " ";
                displayInputString += TIMMentionEditText.TIM_MENTION_TAG;
            } else {
                displayInputString += names.get(i);
                displayInputString += " ";
                displayInputString += TIMMentionEditText.TIM_MENTION_TAG;
            }
        }

        if (!displayInputString.isEmpty()) {
            displayInputString = displayInputString.substring(0, displayInputString.length() - 1);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTextInput.removeTextChangedListener(this);
        atUserInfoMap.clear();
        if (mChatInputHandler != null) {
            mChatInputHandler.onUserTyping(false, V2TIMManager.getInstance().getServerTime());
        }
    }


    protected void startSendPhoto() {
        LogUtils.i(">>> ", "startSendPhoto");

    }


    private void updateChatBackground() {
        if (mOnInputViewListener != null) {
            mOnInputViewListener.onUpdateChatBackground();
        }
    }

    private void recordComplete(boolean success, String path, long duration) {
        LogUtils.i(">>>> record", "recordComplete duration:" + duration);
        if (mChatInputHandler != null) {
            if (!success || duration == 0) {
                mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_FAILED);
                return;
            }
            if (mAudioCancel) {
                mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_CANCEL);
                FileUtils.deleteFile(path);
                return;
            }
            if (duration < 1000) {
                mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_TOO_SHORT);
                FileUtils.deleteFile(path);
                return;
            }
            mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_STOP);
        }

        if (mMessageHandler != null && success) {
            LogUtils.e(">>> " + path);

            V2TIMMessage soundMessage = V2TIMManager.getMessageManager().createSoundMessage(path, (int) (duration / 1000));
            mMessageHandler.sendMessage(soundMessage);
        }

    }

    /**
     * 退出引用消息
     */
    public void exitQuote() {
        isQuoteModel = false;
        llayout_quote.setVisibility(View.GONE);
        replyPreviewBean = null;
        tv_quote_content.setText("");
    }

    /**
     * 引用某一条消息
     *
     * @param mConversation
     */
    public ConversionEntity replyPreviewBean = null;

    public void quoteMsg(ConversionEntity mConversation) {

        showSoftInput();

        isQuoteModel = true;

        llayout_quote.setVisibility(View.VISIBLE);

        replyPreviewBean = mConversation;

        String nickName = mConversation.getMessage().getNickName() + "";

        String msgType = getMsgTypeStr(mConversation.getMessage());

        tv_quote_content.setText(nickName + " : " + msgType);

    }

    private void showSoftInput() {
        mCurrentState = STATE_SOFT_INPUT;
        KeyboardUtils.showSoftInput();
        ThreadUtils.postOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                hideInputMoreLayout();
                iv_switch_speak.setImageResource(R.drawable.action_audio_selector);
                mEmojiInputButton.setImageResource(R.drawable.mes_icon_emote);
                mSendAudioButton.setVisibility(GONE);
                mTextInput.setVisibility(VISIBLE);
                mTextInput.requestFocus();
                Context context = getContext();
                if (context instanceof Activity) {
                    Window window = ((Activity) context).getWindow();
                    if (window != null) {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                }
            }
        }, 200);

        if (mChatInputHandler != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChatInputHandler.onInputAreaClick();
                }
            }, 200);
        }
    }

    private void hideInputMoreLayout() {
        mInputMoreView.setVisibility(View.GONE);
    }

    private String mInputContent;

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
        mInputContent = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            mSendEnable = false;
            showSendTextButton(View.GONE);
            showMoreInputButton(View.VISIBLE);
            if (mChatInputHandler != null) {
                mChatInputHandler.onUserTyping(false, V2TIMManager.getInstance().getServerTime());
            }
        } else {
            mSendEnable = true;
            showSendTextButton(View.VISIBLE);
            showMoreInputButton(View.GONE);
            if (mTextInput.getLineCount() != mLastMsgLineCount) {
                mLastMsgLineCount = mTextInput.getLineCount();
                if (mChatInputHandler != null) {
                    mChatInputHandler.onInputAreaClick();
                }
            }
            if (!TextUtils.equals(mInputContent, mTextInput.getText().toString())) {
                FaceManager.handlerEmojiText(mTextInput, mTextInput.getText(), true);
            }
        }

        if (mChatInputHandler != null && !mIsSending) {
            mChatInputHandler.onUserTyping(true, V2TIMManager.getInstance().getServerTime());
        }

        if (mIsSending) {
            mIsSending = false;
        }
    }


    private int mLastMsgLineCount;

    private boolean mIsSending = false;

    protected boolean mMoreInputDisable;


    protected void showMoreInputButton(int visibility) {
        if (mMoreInputDisable) {
            return;
        }
        // TODO: 2023/7/14 恢复以下注释代码
//        iv_photo.setVisibility(visibility);
    }

    protected void showSendTextButton(int visibility) {
        // TODO: 2023/7/14 恢复以下注释代码
//        if (mMoreInputDisable) {
//            mSendTextButton.setVisibility(VISIBLE);
//        } else {
//            mSendTextButton.setVisibility(visibility);
//        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_switch_speak) {

            if (mCurrentState == STATE_FACE_INPUT || mCurrentState == STATE_ACTION_INPUT) {
                mCurrentState = STATE_VOICE_INPUT;
                mInputMoreView.setVisibility(View.GONE);
                mEmojiInputButton.setImageResource(R.drawable.mes_icon_emote);
            } else if (mCurrentState == STATE_SOFT_INPUT) {
                mCurrentState = STATE_VOICE_INPUT;
            } else {
                mCurrentState = STATE_SOFT_INPUT;
            }
            if (mCurrentState == STATE_VOICE_INPUT) {
                mSendAudioButton.setVisibility(VISIBLE);
                mTextInput.setVisibility(GONE);
                iv_switch_speak.setImageResource(R.drawable.chat_input_keyboard);
                hideInputMoreLayout();
                hideSoftInput();
            } else {
                iv_switch_speak.setImageResource(R.drawable.action_audio_selector);
                mSendAudioButton.setVisibility(GONE);
                mTextInput.setVisibility(VISIBLE);
                showSoftInput();
            }
        } else if (view.getId() == R.id.iv_emoge) {
            iv_switch_speak.setImageResource(R.drawable.action_audio_selector);
            if (mCurrentState == STATE_VOICE_INPUT) {
                mCurrentState = STATE_NONE_INPUT;
                mSendAudioButton.setVisibility(GONE);
                mTextInput.setVisibility(VISIBLE);
            }
            if (mCurrentState == STATE_FACE_INPUT) {
                mCurrentState = STATE_SOFT_INPUT;
                mEmojiInputButton.setImageResource(R.drawable.mes_icon_emote);
                mTextInput.setVisibility(VISIBLE);
                showSoftInput();
            } else {
                mCurrentState = STATE_FACE_INPUT;
                mEmojiInputButton.setImageResource(R.drawable.chat_input_keyboard);
                showFaceViewGroup();
            }
        } else if (view.getId() == R.id.tv_send) {
            if (mSendEnable) {
                if (mMessageHandler != null) {

                    if (isQuoteModel && replyPreviewBean != null) {
                        V2TIMMessage v2TIMMessage =
                                V2TIMManager.getMessageManager().createTextMessage(mTextInput.getText().toString());
                        mMessageHandler.sendMessage(buildReplyMessage(v2TIMMessage, replyPreviewBean.getMessage()));
                        exitQuote();
                    } else {
                        V2TIMMessage v2TIMMessage =
                                V2TIMManager.getMessageManager().createTextMessage(mTextInput.getText().toString());
                        mMessageHandler.sendMessage(v2TIMMessage);
                    }
                }
                mIsSending = true;
                mTextInput.setText("");
            }
        } else if (view.getId() == R.id.iv_pic) {
            if (mMessageHandler != null) {
                // TODO: 2023/7/5 发送图片
            }
        } else if (view.getId() == R.id.iv_photo) {

        }
    }

    private boolean mSendEnable;

    public void hideSoftInput() {
        LogUtils.i(">>> ", "hideSoftInput");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTextInput.getWindowToken(), 0);
        mTextInput.clearFocus();
        Context context = getContext();
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }
        }
    }

    public void setChatInputHandler(ChatInputHandler handler) {
        this.mChatInputHandler = handler;
    }

    public void setMessageHandler(MessageHandler handler) {
        this.mMessageHandler = handler;
    }

    public void setOnInputViewListener(OnInputViewListener listener) {
        this.mOnInputViewListener = listener;
    }

    public void setChatInputMoreListener(ChatInputMoreListener chatInputMoreListener) {
        this.chatInputMoreListener = chatInputMoreListener;
    }

    public interface MessageHandler {
        void sendMessage(V2TIMMessage msg);

        void scrollToEnd();
    }

    public interface ChatInputHandler {
        int RECORD_START = 1;
        int RECORD_STOP = 2;
        int RECORD_CANCEL = 3;
        int RECORD_TOO_SHORT = 4;
        int RECORD_FAILED = 5;

        void onInputAreaClick();

        void onRecordStatusChanged(int status);

        void onUserTyping(boolean status, long time);
    }

    public interface OnInputViewListener {
        void onStartGroupMemberSelectActivity();

        void onUpdateChatBackground();
    }


    private FragmentManager mFragmentManager;
    private FaceFragment mFaceFragment;
    private boolean isShowCustomFace = true;

    private void showFaceViewGroup() {
        LogUtils.i(">>> ", "showFaceViewGroup");
        if (mFragmentManager == null) {
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
        if (mFaceFragment == null) {
            mFaceFragment = new FaceFragment();
        }
        hideSoftInput();
        mInputMoreView.setVisibility(View.VISIBLE);
        mTextInput.requestFocus();
        mFaceFragment.setShowCustomFace(isShowCustomFace);
        mFaceFragment.setListener(new FaceFragment.OnEmojiClickListener() {
            @Override
            public void onEmojiDelete() {
                int index = mTextInput.getSelectionStart();
                Editable editable = mTextInput.getText();
                boolean isFace = false;
                if (index <= 0) {
                    return;
                }
                if (editable.charAt(index - 1) == ']') {
                    for (int i = index - 2; i >= 0; i--) {
                        if (editable.charAt(i) == '[') {
                            String faceChar = editable.subSequence(i, index).toString();
                            if (FaceManager.isFaceChar(faceChar)) {
                                editable.delete(i, index);
                                isFace = true;
                            }
                            break;
                        }
                    }
                }
                if (!isFace) {
                    editable.delete(index - 1, index);
                }
            }

            @Override
            public void onEmojiClick(Emoji emoji) {
                int index = mTextInput.getSelectionStart();
                Editable editable = mTextInput.getText();
                editable.insert(index, emoji.getFaceKey());
                FaceManager.handlerEmojiText(mTextInput, editable, true);
            }

            @Override
            public void onCustomFaceClick(int groupIndex, CustomFace customFace) {

//                mMessageHandler.sendMessage(ChatMessageBuilder.buildFaceMessage(groupIndex, customFace.getFaceKey()));


            }
        });

        FragmentUtils.replace(mFragmentManager, mFaceFragment, R.id.more_groups);

//        mFragmentManager.beginTransaction().replace(R.id.more_groups, mFaceFragment).commitAllowingStateLoss();
        if (mChatInputHandler != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChatInputHandler.onInputAreaClick();
                }
            }, 100);
        }
    }

    public static String getMsgTypeStr(V2TIMMessage msg) {
        String typeStr;
        int msgType = msg.getElemType();
        switch (msgType) {
            case V2TIMMessage.V2TIM_ELEM_TYPE_TEXT: {
                typeStr = "" + msg.getTextElem().getText();
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_SOUND: {
                typeStr = "[语音]" + msg.getSoundElem().getDuration() + "''";
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_FILE: {
                typeStr = "[文件]" + msg.getFileElem().getFileName();
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE: {
                typeStr = "[图片]";
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION: {
                typeStr = "[定位]";
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO: {
                typeStr = "[视频]";
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_FACE: {
                typeStr = "[Animated Sticker]";
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM: {
                typeStr = "[自定义]";
                break;
            }
            default: {
                typeStr = "";
            }
        }
        return typeStr;
    }

    public static QuoteBean getMsgElem(QuoteBean msg, V2TIMMessage v2TIMMessage) {
        int msgType = v2TIMMessage.getElemType();
        switch (msgType) {
            case V2TIMMessage.V2TIM_ELEM_TYPE_TEXT: {
                TIMTextElem timTextElem = new TIMTextElem();
                timTextElem.setText(v2TIMMessage.getTextElem().getText());
//                msg.setTextElement(timTextElem);
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_SOUND: {

                TIMSoundElem timSoundElem = new TIMSoundElem();
                timSoundElem.setDuration(v2TIMMessage.getSoundElem().getDuration());
                timSoundElem.setDataSize(v2TIMMessage.getSoundElem().getDataSize());
                timSoundElem.setPath(v2TIMMessage.getSoundElem().getPath());
                timSoundElem.setUUID(v2TIMMessage.getSoundElem().getUUID());
//                msg.setSoundElement(timSoundElem);

                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_FILE: {
//                msg.setFileElement(v2TIMMessage.getFileElem());
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE: {
                TIMImageElem timImageElem = new TIMImageElem();
                timImageElem.setPath(v2TIMMessage.getImageElem().getPath());
                timImageElem.setImageList(v2TIMMessage.getImageElem().getImageList());
//                msg.setImageElement(timImageElem);
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION: {
//                msg.setLocationElement(v2TIMMessage.getLocationElem());
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO: {


//                msg.setVideoElement(v2TIMMessage.getVideoElem());

                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_FACE: {
//                msg.setFaceElement(v2TIMMessage.getFaceElem());
                break;
            }
            case V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM: {
//                TIMCustomElem timCustomElem = new TIMCustomElem();
//                v2TIMMessage.getCustomElem()
//                msg.setCustomElement(timCustomElem);
                break;
            }
            default: {

            }
        }
        return msg;
    }


    private static V2TIMMessage buildReplyMessage(V2TIMMessage v2TIMMessage/*发送的消息*/, V2TIMMessage previewBean/*引用的消息*/) {
        Map<String, V2TIMMessage> cloudData = new HashMap<>();
        previewBean.setCloudCustomData("");
        Gson gson = new Gson();
        cloudData.put(TIMCommonConstants.MESSAGE_REPLY_KEY, previewBean);
        v2TIMMessage.setCloudCustomData(gson.toJson(cloudData));
        return v2TIMMessage;
    }
}

