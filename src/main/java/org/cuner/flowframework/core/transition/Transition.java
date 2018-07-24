package org.cuner.flowframework.core.transition;

import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.core.Step;

/**
 * Created by houan on 18/7/19.
 */
public interface Transition {

    /**
     * 是否满足跳转的条件
     * @param context
     * @return
     */
    boolean match(FlowContext context);
    /**
     * 获取下一个要执行的节点
     * @param context
     * @return
     */
    Step getTarget(FlowContext context);

    void setTarget(Step to);

}
