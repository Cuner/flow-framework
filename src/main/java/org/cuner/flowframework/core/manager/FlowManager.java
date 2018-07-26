package org.cuner.flowframework.core.manager;

import org.cuner.flowframework.config.XmlParser;
import org.cuner.flowframework.core.Flow;
import org.cuner.flowframework.core.FlowContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;

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

    public <T,R> R execute(String flowName, T data) {
        if (!init){
            throw new IllegalStateException("The flow engine has not been initialized!");
        }
        if (null == flowName){
            throw new IllegalArgumentException("flowName is null!");
        }
        Flow flow = flowMap.get(flowName);
        if (null == flow){
            throw new IllegalArgumentException("flow: " + flowName + "not exists!");
        }

        flow.setExecutor(executorService);

        FlowContext<T, R> flowContext = new FlowContext<>();
        flowContext.setData(data);
        flowContext.setFlow(flow);

        flow.execute(flowContext);
        return flowContext.getResult();
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
