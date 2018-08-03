package org.cuner.flowframework.core;

import org.cuner.flowframework.support.log.Execution;
import org.cuner.flowframework.support.log.ExecutionType;

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
     * @param context 上下问
     * @param execution 上游执行流程节点
     */
    public void execute(FlowContext context, Execution execution) {
        if (null == steps) {
            return;
        }

        Execution flowExecution = new Execution();
        flowExecution.setName(this.name);
        flowExecution.setStartTime(System.currentTimeMillis());

        if (execution == null) {
            flowExecution.setExecutionType(ExecutionType.FLOW);
            context.setExecution(flowExecution);
        } else {
            flowExecution.setExecutionType(ExecutionType.SUBFLOW);
            execution.getChildren().add(flowExecution);
        }

        try {
            Step step = steps.get(0);
            do {
                if (!step.isAsyn()) {
                    step.execute(context, flowExecution);
                } else {
                    final Step nextStep = step;
                    executor.execute(() -> nextStep.execute(context, flowExecution));
                }

                step = step.next(context);
            } while (step != null);
        } finally {
            flowExecution.setEndTime(System.currentTimeMillis());
        }

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
