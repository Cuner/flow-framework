package org.cuner.flowframework.config;

import org.cuner.flowframework.config.definition.FlowDefinition;
import org.cuner.flowframework.config.definition.InvalidConfigException;
import org.cuner.flowframework.config.definition.StepDefinition;
import org.cuner.flowframework.config.definition.transition.ConditionTransitionDefinition;
import org.cuner.flowframework.config.definition.transition.OrderTransitionDefinition;
import org.cuner.flowframework.config.definition.transition.TransitionDefinition;
import org.cuner.flowframework.config.definition.transition.condition.*;
import org.cuner.flowframework.core.Action;
import org.cuner.flowframework.core.Flow;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houan on 18/7/16.
 */
public class XmlParser {

    public static Map<String, Flow> parse(List<String> flowFiles, ApplicationContext applicationContext) throws InvalidConfigException, DocumentException {
        Map<String, Flow> flowMap = new HashMap<>();
        List<FlowDefinition> flowDefinitionList = new ArrayList<>();
        for (String file : flowFiles) {
            flowDefinitionList.addAll(parseFile(file));
        }

        for (FlowDefinition flowDefinition : flowDefinitionList) {
            if (flowMap.containsKey(flowDefinition.getName())) {
                throw new InvalidConfigException("duplicate flow definition,name:" + flowDefinition.getName());
            }
            Flow flow = flowDefinition.getInstance();
            flowMap.put(flow.getName(), flow);
        }

        //填充step中的action和subflow
        for (FlowDefinition flowDefinition : flowDefinitionList) {
            for (StepDefinition stepDefinition : flowDefinition.getStepDefinitionList()) {
                if (!StringUtils.isEmpty(stepDefinition.getAction())) {
                    stepDefinition.getInstance().setAction((Action) applicationContext.getBean(stepDefinition.getAction()));
                }
                if (!StringUtils.isEmpty(stepDefinition.getSubflow())) {
                    stepDefinition.getInstance().setSubflow(flowMap.get(stepDefinition.getSubflow()));
                }
            }
        }

        return flowMap;
    }

    private static List<FlowDefinition> parseFile(String file) throws DocumentException {
        List<FlowDefinition> flowDefinitionList = new ArrayList<>();
        // get document
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(file);
        Document doc = saxReader.read(inputStream);
        List flowElements = doc.getRootElement().elements();

        for (Object o : flowElements) {
            Element element = (Element) o;
            if (element.getName().equalsIgnoreCase("flow")) {
                flowDefinitionList.add(parseFlow(element));
            }
        }

        return flowDefinitionList;
    }

    private static FlowDefinition parseFlow(Element element) {
        String flowName = getNodeAttribute(element, "name", true);
        List<StepDefinition> stepDefinitionList = new ArrayList<>();
        for (Object o : element.elements()) {
            Element e = (Element) o;
            if (e.getName().equalsIgnoreCase("step")) {
                stepDefinitionList.add(parseStep(e));
            }
        }

        return new FlowDefinition(flowName, stepDefinitionList);
    }

    private static StepDefinition parseStep(Element element) {
        String stepName = getNodeAttribute(element, "name", true);
        String asyn = getNodeAttribute(element, "asyn", false);
        String action = getNodeAttribute(element, "action", false);
        String subflow = getNodeAttribute(element, "subflow", false);

        ConditionDefinition conditionDefinition = null;
        List<TransitionDefinition> transitionDefinitionList = new ArrayList<>();

        for (Object o : element.elements()) {
            Element e = (Element) o;
            switch (e.getName().toLowerCase()) {
                case "condition":
                case "expCondition":
                case "andCondition":
                case "orCondition":
                    conditionDefinition = parseCondition(e);
                    break;
                case "conditionTransition":
                case "orderTransition":
                    transitionDefinitionList.add(parseTransition(e));
                    break;
            }
        }

        StepDefinition stepDefinition = new StepDefinition();
        stepDefinition.setName(stepName);
        stepDefinition.setAsyn(asyn);
        stepDefinition.setAction(action);
        stepDefinition.setSubflow(subflow);
        stepDefinition.setConditionDefinition(conditionDefinition);
        stepDefinition.setTransitionDefinitionList(transitionDefinitionList);

        return stepDefinition;
    }

    private static TransitionDefinition parseTransition(Element element) {
        switch (element.getName().toLowerCase()) {
            case "conditionTransition":
                String to = getNodeAttribute(element, "to", true);
                ConditionDefinition conditionDefinition = null;
                for (Object o : element.elements()) {
                    conditionDefinition = parseCondition((Element) o);
                    break;
                }
                return new ConditionTransitionDefinition(conditionDefinition, to);
            case "orderTransition":
                return new OrderTransitionDefinition(getNodeAttribute(element, "to", true));
            default:
                return null;
        }
    }

    private static ConditionDefinition parseCondition(Element element) {
        List<ConditionDefinition> conditionDefinitionList = new ArrayList<>();
        switch (element.getName().toLowerCase()) {
            case "condition":
                String key = getNodeAttribute(element, "key", true);
                String value = getNodeAttribute(element, "value", true);
                String compareType = getNodeAttribute(element, "comparator", true);
                return new DefaultConditionDefinition(key, value, compareType);
            case "expCondition":
                String express = getNodeAttribute(element, "express", true);
                return new GroovyExpressConditionDefinition(express);
            case "andCondition":
                for (Object o : element.elements()) {
                    conditionDefinitionList.add(parseCondition((Element) o));
                }
                return new AndConditionDefinition(conditionDefinitionList);
            case "orCondition":
                for (Object o : element.elements()) {
                    conditionDefinitionList.add(parseCondition((Element) o));
                }
                return new OrConditionDefinition(conditionDefinitionList);
            default:
                return null;
        }
    }

    private static String getNodeAttribute(Element element, String key, boolean required) {
        Attribute attribute = element.attribute(key);
        if (required && (attribute == null || attribute.getValue() == null)) {
            throw new IllegalStateException("节点属性:" + attribute + " 不能为空!" + element.toString());
        }
        return attribute == null || attribute.getValue() == null ? null : attribute.getValue();
    }

}
