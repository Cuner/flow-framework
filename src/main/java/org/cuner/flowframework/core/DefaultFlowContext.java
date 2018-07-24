package org.cuner.flowframework.core;

import java.util.Map;

/**
 * Created by houan on 18/7/19.
 */
public class DefaultFlowContext implements FlowContext {

    private Object data;

    private Object Result;

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public void setResult(Object result) {

    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public String getFlowName() {
        return null;
    }

    @Override
    public String getStepName() {
        return null;
    }

    @Override
    public void setParameter(String key, Object value) {

    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }
}
