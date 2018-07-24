package org.cuner.flowframework.config.definition;

import org.apache.commons.collections.CollectionUtils;
import org.cuner.flowframework.config.definition.transition.TransitionDefinition;
import org.cuner.flowframework.core.Flow;
import org.cuner.flowframework.core.Step;
import org.cuner.flowframework.core.transition.OrderTransition;
import org.cuner.flowframework.core.transition.Transition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houan on 18/7/24.
 */
public class FlowDefinition implements Definition {

    private String name;

    private List<StepDefinition> stepDefinitionList;

    private Flow instance;

    public FlowDefinition(String name, List<StepDefinition> stepDefinitionList) {
        this.name = name;
        this.stepDefinitionList = stepDefinitionList;
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(name) && CollectionUtils.isNotEmpty(stepDefinitionList);
    }

    @Override
    public Flow getInstance() throws InvalidConfigException {
        if (instance != null) {
            return instance;
        }

        if (!isValid()) {
            throw new InvalidConfigException(this.toString());
        }

        instance = new Flow();
        instance.setName(name);

        List<Step> stepList = new ArrayList<>();
        Map<String, Step> stepMap = new HashMap<>();
        for (StepDefinition stepDefinition : stepDefinitionList) {
            Step step = stepDefinition.getInstance();
            stepList.add(step);
            stepMap.put(step.getName(), step);
        }
        instance.setSteps(stepList);

        //解析transition to
        for (StepDefinition sd : stepDefinitionList) {
            List<TransitionDefinition> tdl = sd.getTransitionDefinitionList();
            for (TransitionDefinition td : tdl) {
                Step targetStep = stepMap.get(td.getTo());
                if (targetStep == null) {
                    throw new InvalidConfigException("transition to not found:" + td.getTo());
                }
                td.getInstance().setTarget(targetStep);
            }
        }

        // 为没有定义的设置默认transition
        for (int i = 0; i < stepList.size() - 1; i++) {
            Step s = stepList.get(i);
            if (CollectionUtils.isEmpty(s.getTransitionList())) {
                OrderTransition st = new OrderTransition(stepList.get(i + 1));
                List<Transition> transitions = new ArrayList<>();
                transitions.add(st);
                s.setTransitionList(transitions);
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return "FlowDefinition{" +
                "name='" + name + '\'' +
                ", stepDefinitionList=" + stepDefinitionList +
                '}';
    }
}
