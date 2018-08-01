package org.cuner.flowframework.config.definition;

import org.apache.commons.collections.CollectionUtils;
import org.cuner.flowframework.config.definition.transition.TransitionDefinition;
import org.cuner.flowframework.config.definition.transition.condition.ConditionDefinition;
import org.cuner.flowframework.core.Step;
import org.cuner.flowframework.core.transition.Transition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houan on 18/7/24.
 */
public class StepDefinition implements Definition {

    private String name;

    private String asyn = "false";

    private ConditionDefinition conditionDefinition;

    private String action;

    private String subflow;

    private List<TransitionDefinition> transitionDefinitionList;

    private Step instance;

    @Override
    public boolean isValid() {
        if (StringUtils.isEmpty(name)) {
            return false;
        }

        if (StringUtils.isEmpty(action) && StringUtils.isEmpty(subflow)) {
            return false;
        }

        if (!StringUtils.isEmpty(action) && !StringUtils.isEmpty(subflow)) {
            return false;
        }

        return true;
    }

    @Override
    public Step getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }

        instance = new Step();
        instance.setName(this.name);
        instance.setAsyn(Boolean.parseBoolean(this.asyn));

        if (conditionDefinition != null) {
            instance.setCondition(conditionDefinition.getInstance());
        }

        if (CollectionUtils.isNotEmpty(transitionDefinitionList)) {
            List<Transition> transitionList = new ArrayList<>();
            for (TransitionDefinition td : transitionDefinitionList) {
                transitionList.add(td.getInstance());
            }
            instance.setTransitionList(transitionList);
        }

        //step的action和subflow后面注入
        return instance;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsyn() {
        return asyn;
    }

    public void setAsyn(String asyn) {
        this.asyn = asyn;
    }

    public ConditionDefinition getConditionDefinition() {
        return conditionDefinition;
    }

    public void setConditionDefinition(ConditionDefinition conditionDefinition) {
        this.conditionDefinition = conditionDefinition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubflow() {
        return subflow;
    }

    public void setSubflow(String subflow) {
        this.subflow = subflow;
    }

    public List<TransitionDefinition> getTransitionDefinitionList() {
        return transitionDefinitionList;
    }

    public void setTransitionDefinitionList(List<TransitionDefinition> transitionDefinitionList) {
        this.transitionDefinitionList = transitionDefinitionList;
    }

    public void setInstance(Step instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "StepDefinition{" +
                "name='" + name + '\'' +
                ", asyn='" + asyn + '\'' +
                ", conditionDefinition=" + conditionDefinition +
                ", action='" + action + '\'' +
                ", subflow='" + subflow + '\'' +
                ", transitionDefinitionList=" + transitionDefinitionList +
                '}';
    }
}
