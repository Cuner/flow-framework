package org.cuner.flowframework.config.definition.transition.condition;

import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.condition.Condition;
import org.cuner.flowframework.core.transition.condition.GroovyExpressCondition;
import org.springframework.util.StringUtils;

/**
 * Created by houan on 18/7/24.
 */
public class GroovyExpressConditionDefinition extends ConditionDefinition {

    private String express;

    private Condition instance;

    public GroovyExpressConditionDefinition(String express) {
        this.express = express;
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(express);
    }

    @Override
    public Condition getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }

        instance = new GroovyExpressCondition(express);
        return instance;
    }

    @Override
    public String toString() {
        return "GroovyExpressConditionDefinition{" +
                "express='" + express + '\'' +
                '}';
    }
}
