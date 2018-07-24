package org.cuner.flowframework.core.transition.condition;

import org.cuner.flowframework.core.FlowContext;

/**
 * Created by houan on 18/7/23.
 */
public interface Condition {

    boolean match(FlowContext context);
}
