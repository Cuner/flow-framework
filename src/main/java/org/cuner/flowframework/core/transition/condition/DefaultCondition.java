package org.cuner.flowframework.core.transition.condition;

import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.support.Comparator;

import java.lang.reflect.Field;

/**
 * Created by houan on 18/7/23.
 */
public class DefaultCondition implements Condition {

    private String key;

    private String value;

    private Comparator.CompareType compareType;

    public DefaultCondition(String key, String value, Comparator.CompareType compareType) {
        this.key = key;
        this.value = value;
        this.compareType = compareType;
    }

    @Override
    public boolean match(FlowContext context) {
        Object data = context.getData();
        if (null == data){
            return false;
        }
        Class<?> clazz = data.getClass();
        try{
            Field field = clazz.getDeclaredField(this.key);
            field.setAccessible(true);
            return Comparator.validate(field.get(data), this.value, this.compareType);
        } catch (NoSuchFieldException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }
}
