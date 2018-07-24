package org.cuner.flowframework.core.transition.condition;

import org.apache.commons.collections.CollectionUtils;
import org.cuner.flowframework.core.FlowContext;

import java.util.List;

/**
 * Created by houan on 18/7/23.
 */
public class AndCondition implements Condition {

    private List<Condition> conditionList;

    public AndCondition(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    @Override
    public boolean match(FlowContext context) {
        if (CollectionUtils.isEmpty(conditionList)) {
            return false;
        }

        for (Condition condition : conditionList) {
            if (!condition.match(context)) {
                return false;
            }
        }
        return true;
    }
}
