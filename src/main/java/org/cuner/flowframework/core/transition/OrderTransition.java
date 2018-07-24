package org.cuner.flowframework.core.transition;

import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.core.Step;

/**
 * 顺序跳转
 * Created by houan on 18/7/24.
 */
public class OrderTransition implements Transition {

    private Step to;

    public OrderTransition(Step to) {
        this.to = to;
    }

    @Override
    public boolean match(FlowContext context) {
        return true;
    }

    @Override
    public Step getTarget(FlowContext context) {
        return to;
    }

    @Override
    public void setTarget(Step to) {
        this.to = to;
    }
}
