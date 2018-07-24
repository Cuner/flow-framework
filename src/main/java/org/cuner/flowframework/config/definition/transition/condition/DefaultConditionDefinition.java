package org.cuner.flowframework.config.definition.transition.condition;

import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.condition.Condition;
import org.cuner.flowframework.core.transition.condition.DefaultCondition;
import org.cuner.flowframework.support.Comparator;
import org.springframework.util.StringUtils;

/**
 * Created by houan on 18/7/24.
 */
public class DefaultConditionDefinition extends ConditionDefinition {

    private String key;

    private String value;

    private String compareType;

    private Condition instance;

    public DefaultConditionDefinition(String key, String value, String compareType) {
        this.key = key;
        this.value = value;
        this.compareType = compareType;
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(key) && !StringUtils.isEmpty(value) && !StringUtils.isEmpty(compareType)
                && Comparator.CompareType.getCompareType(this.compareType) != null;
    }

    @Override
    public Condition getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }
        instance = new DefaultCondition(key, value, Comparator.CompareType.getCompareType(this.compareType));
        return instance;
    }

    @Override
    public String toString() {
        return "DefaultConditionDefinition{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", compareType='" + compareType + '\'' +
                '}';
    }
}
