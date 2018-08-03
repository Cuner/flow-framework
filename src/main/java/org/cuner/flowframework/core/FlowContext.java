package org.cuner.flowframework.core;

import org.cuner.flowframework.support.log.Execution;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by houan on 18/7/19.
 */
public class FlowContext<T, R> {

    /**
     * 业务数据对象
     */
    private T data;

    /**
     * 流程执行结果
     */
    private R result;

    /**
     * 正在执行的流程
     */
    private Flow flow;

    /**
     * 正在执行的步骤
     */
    private Step step;

    /**
     * 自定义参数
     */
    private ConcurrentHashMap<String, Object> parameters = new ConcurrentHashMap<>();

    /**
     * 执行路径
     */
    private Execution execution;

    /**
     * 闭锁 用于触发异步任务全部完成
     */
    private CountDownLatch countDownLatch;

    /*
     * 构造一个业务方不能修改的流程上下文，防止业务方干预流程执行过程
     */
    public FlowContext<T, R> createFlowContext() {
        return new FlowContext<T, R>() {
            public T getData() {
                return data;
            }

            public void setResult(R object) {
                result = object;
            }

            public R getResult() {
                return result;
            }

            public String getFlowName() {
                return flow.getName();
            }

            public String getStepName() {
                return step.getName();
            }

            public void setParameter(String key, Object value) {
                if (null == key){
                    throw new IllegalArgumentException("key is null!");
                }
                parameters.put(key, value);
            }

            public Object getParameter(String key) {
                return parameters.get(key);
            }

            public ConcurrentHashMap<String, Object> getParameters() {
                return parameters;
            }
        };
    }

    public FlowContext copy() {
        FlowContext<T, R> flowContext = new FlowContext<>();
        flowContext.setFlow(this.flow);
        flowContext.setStep(this.step);
        flowContext.setData(this.data);
        flowContext.setResult(null);
        flowContext.setParameters(this.parameters);
        flowContext.setCountDownLatch(countDownLatch);
        return flowContext;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public ConcurrentHashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(ConcurrentHashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getFlowName() {
        return flow.getName();
    }

    public String getStepName() {
        return step.getName();
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String toString() {
        return "FlowContext{" +
                "data=" + data +
                ", result=" + result +
                ", flow=" + flow +
                ", step=" + step +
                ", parameters=" + parameters +
                '}';
    }
}
