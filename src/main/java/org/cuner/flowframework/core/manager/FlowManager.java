package org.cuner.flowframework.core.manager;

import org.apache.commons.collections.CollectionUtils;
import org.cuner.flowframework.config.XmlParser;
import org.cuner.flowframework.core.Action;
import org.cuner.flowframework.core.Flow;
import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.core.Step;
import org.cuner.flowframework.support.log.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by houan on 18/7/26.
 */
public class FlowManager implements ApplicationListener<ApplicationContextEvent>, ApplicationContextAware {

    private boolean init = false;

    private List<String> flowFiles;

    private Map<String, Flow> flowMap;

    private ExecutorService executorService;

    private ApplicationContext applicationContext;

    private Map<Flow, Type> flowDataTypeMap = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger("flow-record");

    public FlowManager() {
    }

    public FlowManager(List<String> flowFiles) {
        this.flowFiles = flowFiles;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.init = true;
            try {
                this.flowMap = XmlParser.parse(flowFiles, applicationContext);

                //校验子流程, 为了防止出现死循环
                this.flowMap.values().forEach(flow -> checkSubFlow(flow.getSubflows(), flow.getName()));
                this.flowMap.values().forEach(flow -> flowDataTypeMap.put(flow, validateFlow(flow)));

                executorService = new ThreadPoolExecutor(5,
                        10,
                        60,
                        TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(1000),
                        new ThreadPoolExecutor.CallerRunsPolicy());
            } catch (Exception e) {
                throw new RuntimeException("fail to load flows:", e);
            }
        }

    }

    public <T, R> R execute(String flowName, T data) {
        if (!init) {
            throw new IllegalStateException("The flow engine has not been initialized!");
        }
        if (null == flowName) {
            throw new IllegalArgumentException("flowName is null!");
        }
        Flow flow = flowMap.get(flowName);
        if (null == flow) {
            throw new IllegalArgumentException("flow: " + flowName + "not exists!");
        }

        flow.setExecutor(executorService);
        for (Flow subflow : flow.getSubflows()) {
            subflow.setExecutor(executorService);
        }

        FlowContext<T, R> flowContext = new FlowContext<>();

        //get all aync step count
        int ayncStepCount = getFlowAyncStepCount(flow);
        flowContext.setCountDownLatch(new CountDownLatch(ayncStepCount));

        flowContext.setData(data);
        flowContext.setFlow(flow);

        Type genericDataType = flowDataTypeMap.get(flow);
        if (data.getClass() != genericDataType) {
            throw new IllegalArgumentException("input data type or result data is not compatible with action generic type! ");
        }

        try {
            flow.execute(flowContext, null);
            flowContext.getCountDownLatch().await();
        } catch (InterruptedException e) {
            return flowContext.getResult();
        } finally {
            logger.info(Execution.getExecutionString(flowContext.getExecution(), 0));
        }

        return flowContext.getResult();
    }

    private int getFlowAyncStepCount(Flow flow) {
        int count = 0;
        for (Step step : flow.getSteps()) {
            if (step.isAsyn()) {
                count++;
            }
            if (!StringUtils.isEmpty(step.getSubflow())) {
                count += getFlowAyncStepCount(step.getSubflow());
            }
        }

        return count;
    }

    //流程包含子流程 校验子流程, 为了防止出现死循环：子流程不能为父流程
    private void checkSubFlow(List<Flow> subflowList, String parentFlow) {
        if (CollectionUtils.isNotEmpty(subflowList)) {
            if (subflowList.stream().anyMatch(flow -> flow.getName().equals(parentFlow))) {
                throw new IllegalArgumentException("flow is in endless loop");
            } else {
                subflowList.forEach(flow -> checkSubFlow(flow.getSubflows(), parentFlow));
            }
        }
    }

    /*
     * 流程Action校验, 同一个流程中的所有step的Action必须继承相同的FlowAction接口
     */
    private Type validateFlow(Flow flow) {
        Type type = null;

        List<Step> steps = flow.getSteps();
        if (null == steps) {
            throw new IllegalStateException("flow: " + flow.getName() + " has no steps!");
        }
        Type genericDataType = null;
        Type genericResultType = null;
        for (Step step : steps) {
            Action action = step.getAction();
            if (null == action) {
                continue;
            }

            if (!Action.class.isAssignableFrom(action.getClass())) {
                throw new IllegalStateException("Action class:" + action.getClass().getName() + " must implement the FlowAction interface!");
            }

            try {
                Method method = action.getClass().getMethod("execute", FlowContext.class);
                Type[] types = method.getGenericParameterTypes();
                Type[] params = ((ParameterizedType) types[0]).getActualTypeArguments();

                if (null == genericDataType) {
                    genericDataType = params[0];
                } else if (!params[0].equals(genericDataType)) {
                    throw new IllegalStateException("flow:" + flow.getName() + " has different Action generic parameter types!");
                }

                if (null == genericResultType) {
                    genericResultType = params[1];
                } else if (!params[1].equals(genericResultType)) {
                    throw new IllegalStateException("flow:" + flow.getName() + " has different Action generic result types!");
                }

            } catch (NoSuchMethodException e) {
                //不太可能出现这种异常
            }
        }
        if (null != genericDataType) {
            if (genericDataType instanceof ParameterizedType) {
                type = ((ParameterizedType) genericDataType).getRawType();
            } else {
                type = genericDataType;
            }
        }

        return type;
    }

    public List<String> getFlowFiles() {
        return flowFiles;
    }

    public void setFlowFiles(List<String> flowFiles) {
        this.flowFiles = flowFiles;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
