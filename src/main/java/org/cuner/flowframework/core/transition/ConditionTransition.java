package org.cuner.flowframework.core.transition;

import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.core.Step;
import org.cuner.flowframework.core.transition.condition.Condition;

/**
 * 条件跳转
 * Created by houan on 18/7/24.
 */
public class ConditionTransition implements Transition {

    private Condition condition;

    private Step to;

    public ConditionTransition(Condition condition, Step to) {
        this.condition = condition;
        this.to = to;
    }

    @Override
    public boolean match(FlowContext context) {
        return condition.match(context);
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
