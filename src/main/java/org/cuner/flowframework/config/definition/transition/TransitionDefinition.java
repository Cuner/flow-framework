package org.cuner.flowframework.config.definition.transition;

import org.cuner.flowframework.config.definition.Definition;
import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.core.transition.Transition;

/**
 * Created by houan on 18/7/24.
 */
public abstract class TransitionDefinition implements Definition {

    protected String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public abstract Transition getInstance() throws InvalidConfigException;
}
