package org.cuner.flowframework.test.case1;

import org.cuner.flowframework.core.manager.FlowManager;
import org.cuner.flowframework.test.domain.Data;
import org.cuner.flowframework.test.domain.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by houan on 18/7/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:case1/spring.xml"})
public class OrderSyncNoSubFlowTest {

    @Autowired
    private FlowManager flowManager;

    @Test
    public void excute() {
        Data data = new Data();
        data.setData("begin");
        Result result = flowManager.execute("orderSyncNoSubFlow", data);
        System.out.println("data:" + data);
        System.out.println("result:" + result);
    }
}
