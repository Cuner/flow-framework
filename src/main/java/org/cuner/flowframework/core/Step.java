package org.cuner.flowframework.core;

import org.cuner.flowframework.core.transition.Transition;

import java.util.List;

/**
 * Created by houan on 18/7/18.
 */
public interface Step {

    /**
     * 获取步骤名称
     * @return
     */
    String getName();

    /**
     * 是否异步执行
     * @return
     */
    boolean isAsyn();

    /**
     * 获取步骤需要执行的操作
     * @return
     */
    Action getAction();

    /**
     * 设置步骤执行的操作
     * @param action
     */
    void setAction(Action action);

    /**
     * 获取该步骤代表的子流程
     * @return
     */
    Flow getSubFlow();

    /**
     * 设置改步骤代表的子流程
     */
    void setSubFlow();

    /**
     * 获取该步骤执行结束后的符合所有条件的流转信息(因为什么条件流程到什么步骤)
     * @return
     */
    List<Transition> getTransitions();

    /**
     * 设置改步骤执行结束后的符合所有条件的流转信息
     * @param transitions
     */
    void setTransitions(List<Transition> transitions);

    /**
     * 通过上下文获取符合条件的流转信息
     * @param context
     * @return
     */
    Transition getMatchedTransition(FlowContext context);

    /**
     * 获取下一个步骤
     * @param flowContext
     * @return
     */
    Step next(FlowContext flowContext);

}
