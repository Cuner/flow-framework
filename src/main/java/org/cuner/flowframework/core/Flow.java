package org.cuner.flowframework.core;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by houan on 18/7/18.
 */
public interface Flow {

    /**
     * 获取流程名称
     * @return
     */
    String getName();

    /**
     * 获取流程下所有的步骤
     * @return
     */
    List<Step> getAllSteps();

    /**
     * 获取流程下所有的子流程
     * @return
     */
    List<Flow> getSubFlows();

    /**
     * 设置执行器
     * @param executor
     */
    void setExecutor(Executor executor);
}
