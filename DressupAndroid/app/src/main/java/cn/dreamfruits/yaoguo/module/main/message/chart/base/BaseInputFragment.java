package cn.dreamfruits.yaoguo.module.main.message.chart.base;


import cn.dreamfruits.yaoguo.module.main.message.chart.interfaces.IChatLayout;

public class BaseInputFragment extends ChartBaseFragment {
    private IChatLayout mChatLayout;

    public IChatLayout getChatLayout() {
        return mChatLayout;
    }

    public BaseInputFragment setChatLayout(IChatLayout layout) {
        mChatLayout = layout;
        return this;
    }
}
