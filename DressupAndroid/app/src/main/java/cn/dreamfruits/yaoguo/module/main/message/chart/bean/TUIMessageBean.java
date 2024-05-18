package cn.dreamfruits.yaoguo.module.main.message.chart.bean;

import android.text.TextUtils;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class TUIMessageBean implements Serializable {
    /**
     * 消息正常状态
     * <p>
     * message normal
     */
    public static final int MSG_STATUS_NORMAL = 0;
    /**
     * 消息发送中状态
     * <p>
     * message sending
     */
    public static final int MSG_STATUS_SENDING = 1;
    /**
     * 消息发送成功状态
     * <p>
     * message send success
     */
    public static final int MSG_STATUS_SEND_SUCCESS = 2;
    /**
     * 消息发送失败状态
     * <p>
     * message send failed
     */
    public static final int MSG_STATUS_SEND_FAIL = 3;

    /**
     * 消息未读状态
     * <p>
     * message unread
     */
    public static final int MSG_STATUS_READ = 0x111;
    /**
     * 消息删除状态
     * <p>
     * message deleted
     */
    public static final int MSG_STATUS_DELETE = 0x112;
    /**
     * 消息撤回状态
     * <p>
     * messaage revoked
     */
    public static final int MSG_STATUS_REVOKE = 0x113;

    /**
     * 消息内容下载中状态
     * <p>
     * message downloading
     */
    public static final int MSG_STATUS_DOWNLOADING = 4;
    /**
     * 消息内容未下载状态
     * <p>
     * message undownloaded
     */
    public static final int MSG_STATUS_UN_DOWNLOAD = 5;
    /**
     * 消息内容已下载状态
     * <p>
     * message downloaded
     */
    public static final int MSG_STATUS_DOWNLOADED = 6;
    /**
     * 消息翻译初始化状态
     * <p>
     * message translation unknown
     */
    public static final int MSG_TRANSLATE_STATUS_UNKNOWN = 0;
    /**
     * 消息翻译隐藏状态
     * <p>
     * message translation hidden
     */
    public static final int MSG_TRANSLATE_STATUS_HIDDEN = 1;
    /**
     * 消息翻译进行中状态
     * <p>
     * message translation loading
     */
    public static final int MSG_TRANSLATE_STATUS_LOADING = 2;
    /**
     * 消息翻译展示状态
     * <p>
     * message translation shown
     */
    public static final int MSG_TRANSLATE_STATUS_SHOWN = 3;

    public static final String TRANSLATION_KEY = "translation";
    public static final String TRANSLATION_VIEW_STATUS_KEY = "translation_view_status";

    private V2TIMMessage v2TIMMessage;
    private long msgTime;
    private String extra;
    private String id;
    private boolean isGroup;
    private int status;
    private int downloadStatus;
    private String selectText;
    private int translationStatus = MSG_TRANSLATE_STATUS_UNKNOWN;
    private boolean excludeFromHistory;
    private boolean isUseMsgReceiverAvatar = false;
    private boolean isEnableForward = true;

    public void setExcludeFromHistory(boolean excludeFromHistory) {
        this.excludeFromHistory = excludeFromHistory;
    }

    public boolean isExcludeFromHistory() {
        return excludeFromHistory;
    }

    public void setUseMsgReceiverAvatar(boolean useMsgReceiverAvatar) {
        isUseMsgReceiverAvatar = useMsgReceiverAvatar;
    }

    public boolean isUseMsgReceiverAvatar() {
        return isUseMsgReceiverAvatar;
    }

    public boolean isEnableForward() {
        return isEnableForward;
    }

    public void setEnableForward(boolean enableForward) {
        isEnableForward = enableForward;
    }


    public void setCommonAttribute(V2TIMMessage v2TIMMessage) {
        msgTime = System.currentTimeMillis() / 1000;
        this.v2TIMMessage = v2TIMMessage;

        if (v2TIMMessage == null) {
            return;
        }

        id = v2TIMMessage.getMsgID();
        isGroup = !TextUtils.isEmpty(v2TIMMessage.getGroupID());

        if (v2TIMMessage.getStatus() == V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED) {
            status = MSG_STATUS_REVOKE;
            if (isSelf()) {
                extra = "您撤回了一条消息";
            } else if (isGroup) {
                String message = covert2HTMLString(getSender());
                extra = message + "撤回了一条消息";
            } else {
                extra = "对方撤回了一条消息";
            }
        } else {
            if (isSelf()) {
                if (v2TIMMessage.getStatus() == V2TIMMessage.V2TIM_MSG_STATUS_SEND_FAIL) {
                    status = MSG_STATUS_SEND_FAIL;
                } else if (v2TIMMessage.getStatus() == V2TIMMessage.V2TIM_MSG_STATUS_SEND_SUCC) {
                    status = MSG_STATUS_SEND_SUCCESS;
                } else if (v2TIMMessage.getStatus() == V2TIMMessage.V2TIM_MSG_STATUS_SENDING) {
                    status = MSG_STATUS_SENDING;
                }
            }
        }


    }


    /**
     * 获取要显示在会话列表的消息摘要
     * <p>
     * Get a summary of messages to display in the conversation list
     *
     * @return
     */
    public abstract String onGetDisplayString();

    public abstract void onProcessMessage(V2TIMMessage v2TIMMessage);

    public final long getMessageTime() {
        if (v2TIMMessage != null) {
            long timestamp = v2TIMMessage.getTimestamp();
            if (timestamp != 0) {
                return timestamp;
            }
        }
        return msgTime;
    }

    public long getMsgSeq() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getSeq();
        }
        return 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getUserID();
        }
        return "";
    }

    public boolean isSelf() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.isSelf();
        }
        return true;
    }

    public String getSender() {
        String sender = null;
        if (v2TIMMessage != null) {
            sender = v2TIMMessage.getSender();
        }
        if (TextUtils.isEmpty(sender)) {
            sender = V2TIMManager.getInstance().getLoginUser();
        }
        return sender;
    }

    public V2TIMMessage getV2TIMMessage() {
        return v2TIMMessage;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getGroupId() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getGroupID();
        }
        return "";
    }

    public String getNameCard() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getNameCard();
        }
        return "";
    }

    public String getNickName() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getNickName();
        }
        return "";
    }

    public String getFriendRemark() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getFriendRemark();
        }
        return "";
    }

    public String getUserDisplayName() {
        String displayName;
        if (!TextUtils.isEmpty(getNameCard())) {
            displayName = getNameCard();
        } else if (!TextUtils.isEmpty(getFriendRemark())) {
            displayName = getFriendRemark();
        } else if (!TextUtils.isEmpty(getNickName())) {
            displayName = getNickName();
        } else {
            displayName = getSender();
        }
        return displayName;
    }

    public String getFaceUrl() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getFaceUrl();
        }
        return "";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public int getMsgType() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.getElemType();
        } else {
            return V2TIMMessage.V2TIM_ELEM_TYPE_NONE;
        }
    }

    public boolean isNeedReadReceipt() {
        if (v2TIMMessage != null) {
            return v2TIMMessage.isNeedReadReceipt();
        }
        return false;
    }

    public void setNeedReadReceipt(boolean isNeedReceipt) {
        if (v2TIMMessage != null) {
            v2TIMMessage.setNeedReadReceipt(isNeedReceipt);
        }
    }

    public void setTranslationStatus(int status) {
        if (status != MSG_TRANSLATE_STATUS_UNKNOWN && status != MSG_TRANSLATE_STATUS_HIDDEN && status != MSG_TRANSLATE_STATUS_SHOWN
                && status != MSG_TRANSLATE_STATUS_LOADING) {
            return;
        }

        if (status == translationStatus) {
            return;
        }

        if (status == MSG_TRANSLATE_STATUS_LOADING) {
            translationStatus = MSG_TRANSLATE_STATUS_LOADING;
            return;
        }

        translationStatus = status;

        if (v2TIMMessage != null) {
            String localCustomData = v2TIMMessage.getLocalCustomData();
            JSONObject customJson = new JSONObject();
            try {
                if (!TextUtils.isEmpty(localCustomData)) {
                    customJson = new JSONObject(localCustomData);
                }
                customJson.put(TRANSLATION_VIEW_STATUS_KEY, status);
                v2TIMMessage.setLocalCustomData(customJson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTranslationStatus() {
        if (translationStatus != MSG_TRANSLATE_STATUS_UNKNOWN) {
            return translationStatus;
        }

        if (v2TIMMessage != null) {
            String localCustomData = v2TIMMessage.getLocalCustomData();
            if (TextUtils.isEmpty(localCustomData)) {
                return translationStatus;
            }
            try {
                JSONObject customJson = new JSONObject(localCustomData);
                if (customJson.has(TRANSLATION_VIEW_STATUS_KEY)) {
                    translationStatus = customJson.getInt(TRANSLATION_VIEW_STATUS_KEY);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return translationStatus;
    }

    public void setTranslation(String translation) {
        if (v2TIMMessage != null) {
            String localCustomData = v2TIMMessage.getLocalCustomData();
            JSONObject customJson = new JSONObject();
            try {
                if (!TextUtils.isEmpty(localCustomData)) {
                    customJson = new JSONObject(localCustomData);
                }
                customJson.put(TRANSLATION_KEY, translation);
                customJson.put(TRANSLATION_VIEW_STATUS_KEY, MSG_TRANSLATE_STATUS_SHOWN);
                v2TIMMessage.setLocalCustomData(customJson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            translationStatus = MSG_TRANSLATE_STATUS_SHOWN;
        }
    }

    public String getTranslation() {
        String translation = "";
        if (v2TIMMessage != null) {
            String localCustomData = v2TIMMessage.getLocalCustomData();
            if (TextUtils.isEmpty(localCustomData)) {
                return translation;
            }
            try {
                JSONObject customJson = new JSONObject(localCustomData);
                if (customJson.has(TRANSLATION_KEY)) {
                    translation = customJson.getString(TRANSLATION_KEY);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return translation;
    }

    public void setV2TIMMessage(V2TIMMessage v2TIMMessage) {
        this.v2TIMMessage = v2TIMMessage;
        setCommonAttribute(v2TIMMessage);
        onProcessMessage(v2TIMMessage);
    }

    public void update(TUIMessageBean messageBean) {
        setV2TIMMessage(messageBean.getV2TIMMessage());
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String text) {
        this.selectText = text;
    }

    public static String covert2HTMLString(String original) {
        return "\"<font color=\"#5B6B92\">" + original + "</font>\"";
    }
}
