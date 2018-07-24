package org.cuner.flowframework.config.definition.transition;

import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.config.definition.transition.condition.ConditionDefinition;
import org.cuner.flowframework.core.transition.ConditionTransition;
import org.cuner.flowframework.core.transition.Transition;
import org.springframework.util.StringUtils;

/**
 * Created by houan on 18/7/24.
 */
public class ConditionTransitionDefinition extends TransitionDefinition {

    private ConditionDefinition conditionDefinition;

    private Transition instance;

    public ConditionTransitionDefinition(ConditionDefinition conditionDefinition, String to) {
        this.conditionDefinition = conditionDefinition;
        this.to = to;
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(to) && conditionDefinition != null;
    }

    @Override
    public Transition getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }

        //后面通过FlowDefinition注入to所代表的step
        instance = new ConditionTransition(conditionDefinition.getInstance(), null);
        return instance;
    }

    @Override
    public String toString() {
        return "ConditionTransitionDefinition{" +
                "conditionDefinition=" + conditionDefinition +
                ", to='" + to + '\'' +
                '}';
    }
}
