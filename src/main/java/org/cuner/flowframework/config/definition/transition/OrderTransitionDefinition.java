package org.cuner.flowframework.config.definition.transition;

import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.OrderTransition;
import org.cuner.flowframework.core.transition.Transition;
import org.springframework.util.StringUtils;

/**
 * Created by houan on 18/7/24.
 */
public class OrderTransitionDefinition extends TransitionDefinition {

    private Transition instance;

    public OrderTransitionDefinition(String to) {
        this.to = to;
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(to);
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
        instance = new OrderTransition(null);
        return instance;
    }

    @Override
    public String toString() {
        return "OrderTransitionDefinition{" +
                "to='" + to + '\'' +
                '}';
    }
}
