package org.cuner.flowframework.config.definition.transition.condition;

import org.apache.commons.collections.CollectionUtils;
import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.condition.AndCondition;
import org.cuner.flowframework.core.transition.condition.Condition;
import org.cuner.flowframework.core.transition.condition.OrCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houan on 18/7/24.
 */
public class OrConditionDefinition extends ConditionDefinition {

    private List<ConditionDefinition> conditionDefinitionList;

    private Condition instance;

    public OrConditionDefinition(List<ConditionDefinition> conditionDefinitionList) {
        this.conditionDefinitionList = conditionDefinitionList;
    }

    @Override
    public boolean isValid() {
        return CollectionUtils.isNotEmpty(conditionDefinitionList);
    }

    @Override
    public Condition getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }

        List<Condition> conditionList = new ArrayList<>();
        for (ConditionDefinition conditionDefinition : conditionDefinitionList) {
            conditionList.add(conditionDefinition.getInstance());
        }

        instance = new OrCondition(conditionList);
        return instance;
    }

    @Override
    public String toString() {
        return "OrConditionDefinition{" +
                "conditionDefinitionList=" + conditionDefinitionList +
                '}';
    }
}
