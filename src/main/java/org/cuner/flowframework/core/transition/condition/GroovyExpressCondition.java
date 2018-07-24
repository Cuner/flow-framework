package org.cuner.flowframework.core.transition.condition;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.cuner.flowframework.core.FlowContext;

/**
 * Created by houan on 18/7/23.
 */
public class GroovyExpressCondition implements Condition {

    private String express;

    public GroovyExpressCondition(String express) {
        this.express = express;
    }

    @Override
    public boolean match(FlowContext context) {
        Binding binding = new Binding();
        binding.setProperty("data", context.getData());
        binding.setProperty("parameters", context.getParameters());

        GroovyShell groovyShell = new GroovyShell(binding);
        return (boolean) groovyShell.evaluate("if (" + this.express + ") {\n\nreturn true; } \n\nreturn false;");
    }
}
