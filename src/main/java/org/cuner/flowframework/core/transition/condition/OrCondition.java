package org.cuner.flowframework.core.transition.condition;

import org.cuner.flowframework.core.FlowContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by houan on 18/7/23.
 */
public class OrCondition implements Condition {

    private List<Condition> conditionList;

    public OrCondition(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    @Override
    public boolean match(FlowContext context) {
        if (CollectionUtils.isEmpty(conditionList)) {
            return false;
        }

        for (Condition condition : conditionList) {
            if (condition.match(context)) {
                return true;
            }
        }
        return false;
    }
}
