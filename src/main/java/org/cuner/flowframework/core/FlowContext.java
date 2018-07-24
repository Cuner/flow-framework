package org.cuner.flowframework.core;

import java.util.Map;

/**
 * Created by houan on 18/7/18.
 */
public interface FlowContext<T, R> {

    /**
     * 获取业务数据对象
     * @return
     */
    T getData();

    /**
     * 设置流程执行结果
     * @param result
     */
    void setResult(R result);

    /**
     * 获取流程执行结果
     * @return
     */
    R getResult();

    /**
     * 获取当前执行流程名称
     * @return
     */
    String getFlowName();

    /**
     * 获取当前执行步骤名称
     * @return
     */
    String getStepName();

    /**
     * 设置自定义参数
     * @param key
     * @param value
     */
    void setParameter(String key, Object value);

    /**
     * 获取自定义参数
     * @param key
     * @return
     */
    Object getParameter(String key);

    /**
     * 获取所有自定义参数
     * @return
     */
    Map<String, Object> getParameters();
}
