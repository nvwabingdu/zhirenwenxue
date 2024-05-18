package cn.dreamfruits.yaoguo.view.swipe.interfaces;


import java.util.List;

import cn.dreamfruits.yaoguo.view.swipe.SwipeLayout;
import cn.dreamfruits.yaoguo.view.swipe.util.Attributes;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
