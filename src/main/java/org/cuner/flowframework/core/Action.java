package org.cuner.flowframework.core;

/**
 * Created by houan on 18/7/18.
 */
public interface Action<T, R> {

    void execute(FlowContext<T, R> context);
}
