package org.cuner.flowframework.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by houan on 18/7/18.
 */
public class Flow {

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程下所有的步骤
     */
    private List<Step> steps;

    /**
     * 执行器
     */
    private Executor executor;

    /**
     * 流程执行
     * @param context
     */
    public void execute(FlowContext context) {
        if (null == steps) {
            return;
        }

        Step step = steps.get(0);
        do {
            if (!step.isAsyn()) {
                step.execute(context);
            } else {
                final Step nextStep = step;
                executor.execute(() -> nextStep.execute(context));
            }

            step = step.next(context);
        } while (step != null);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public List<Flow> getSubflows() {
        if (null == steps){
            return new ArrayList<>();
        }
        List<Flow> subflows = new ArrayList<>();
        for (Step step : steps){
            Flow subflow = step.getSubflow();
            if (null != subflow){
                subflows.add(subflow);
            }
        }
        return subflows;
    }

}
