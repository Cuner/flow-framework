package org.cuner.flowframework.config.definition.transition.condition;

import org.cuner.flowframework.config.definition.Definition;
import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.condition.Condition;

/**
 * Created by houan on 18/7/24.
 */
public abstract class ConditionDefinition implements Definition {
    public abstract Condition getInstance() throws InvalidConfigException;
}
