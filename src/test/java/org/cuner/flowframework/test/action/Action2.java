package org.cuner.flowframework.test.action;

import org.cuner.flowframework.core.Action;
import org.cuner.flowframework.core.FlowContext;
import org.cuner.flowframework.test.domain.Data;
import org.cuner.flowframework.test.domain.Result;

/**
 * Created by houan on 18/7/26.
 */
public class Action2 implements Action<Data, Result> {
    @Override
    public void execute(FlowContext<Data, Result> context) {
        if (context.getResult() == null) {
            context.setResult(new Result());
        }
        context.getResult().setResult(context.getData().getData());
        System.out.println("----------flow: " +  context.getFlowName() + " step: " + context.getStepName() + " action: " + this.getClass().getSimpleName() + "----------");
    }
}
