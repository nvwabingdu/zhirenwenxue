package cn.dreamfruits.yaoguo.module.main.message.chart.interfaces;

import java.util.Map;

public interface ITUIObjectFactory {
    Object onCreateObject(String objectName, Map<String, Object> param);
}
