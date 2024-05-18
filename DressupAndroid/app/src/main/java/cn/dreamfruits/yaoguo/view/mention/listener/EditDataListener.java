package cn.dreamfruits.yaoguo.view.mention.listener;

public interface EditDataListener {
    /**
     * at符号响应
     *
     * @param str  改变内容
     *
     */
    void onEditAddAt(String str);

    /**
     * 井号响应
     *
     * @param str 改变内容
     */
    void onEditAddHashtag(String str);

    /**
     * 关闭搜索框
     */
    void onCloseSearchView();
}
