package org.cuner.flowframework.test;

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
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class FlowManagerTest {

    @Autowired
    private FlowManager flowManager;

    @Test
    public void excute() {
        Data data = new Data();
        data.setData("test");
        Result result = flowManager.execute("mainFlow", data);
        System.out.println("data:" + data);
        System.out.println("result:" + result);
    }
}
