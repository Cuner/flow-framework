# 介绍
基于xml配置的流程编排框架，将整个流程以xml配置文件的形式管理起来：定义流程、管理上下文、控制流程的流转（按条件或者顺序），各个流程节点的执行（同步或者异步）。同时对每个流程节点进行监控以及统计，完成对流程的整体把控。

# 基本概念

- flow:数据流程对象
- step:流程的子步骤，也叫流程节点
- action:流程中各个步骤执行的操作
- subflow:子流程，一个流程节点可以执行操作；也可以执行某个子流程

# 用法

## 1.定义流程的输入与输出

- 定义输入为Data
- 定义输出为Result

## 2.定义步骤（子节点）

所有的子节点需要实现接口org.cuner.flowframework.core.Action<Data, Result>

```
public class Action1 implements Action<Data, Result> {
    @Override
    public void execute(FlowContext<Data, Result> context) {
        if (context.getResult() == null) {
            context.setResult(new Result());
        }
        context.setParameter("param", 1);
        context.getResult().setResult(context.getData().getData());
        System.out.println("----------flow: " +  context.getFlowName() + " step: " + context.getStepName() + " action: " + this.getClass().getSimpleName() + "----------");
    }
}
```

同时保证启动后，将所有action注入到spring容器中

```
<bean id="action1" class="org.cuner.flowframework.test.action.Action1"/>
<bean id="action2" class="org.cuner.flowframework.test.action.Action2"/>
<bean id="action3" class="org.cuner.flowframework.test.action.Action3"/>
```

## 3.通过xml配置定义流程

详细的配置结构请查看xsd文件
- `flows`标签为root标签，可包含多个`flow`标签
- `flow`标签标识一个流程，可以是一个主流程也可以是一个子流程，拥有name属性代表流程名称（流程名字要求全局唯一），可包含多个`step`标签
- `step`标签标识一个步骤（流程节点）
  - 包含零或一个`condition类`标签，标识该步骤的执行条件，若没有则默认执行
  - 包含零或多个`conditionTransition`标签，标识状态的流转，若没有则默认流转到配置文件中下一个步骤
  - name属性：标识步骤名称
  - asyn属性：标识是否异步执行
  - action属性：标识步骤执行的操作，关联Action接口某个实现类的bean id
  - subflow属性：标识步骤所代表的子流程（action属性和subflow属性只能存在一者）
- `condition类标签`
  - `condition`标签：
    - 属性key：为输入对象Data的某个属性
    - value：与输入对象Data的某个属性值相比较的值
    - comparator：关系运算符（eq、ne、gt、lt、ge、le、ln）
  - `expCondition`标签，拥有express属性，值为判断条件的表达式，如 data == "test"，其中data为输入对象Data的一个属性
  - `andCondtion`标签，包含多个condition类标签，对多个condition的结果做与操作
  - `orCondtion`标签，包含多个condition类标签，对多个condition的结果做或操作
- `conditionTransition`标签
  - 包含一个`condition`标签，判断是否符合状态流程的条件
  - 包含一个to属性：标识状态流转的下一个节点

```
<?xml version="1.0" encoding="UTF-8"?>
<flows xmlns="http://repo.cuner.com/schema/flow">
    <flow name="mainFlow">
        <step name="step1" action="action1"/>
        <step name="step2" action="action2" asyn="true"/>
        <step name="step3" subflow="subflow"/>
        <step name="step4" subflow="subflow" asyn="true"/>
        <step name="step5" action="action3">
            <condition key="data" value="test" comparator="eq"/>
        </step>
        <step name="step6" action="action3">
            <expCondition expression='data.getData() == "test" '/>
        </step>
        <step name="step7" action="action3">
            <andCondition>
                <condition key="data" value="test" comparator="eq"/>
            </andCondition>
        </step>
        <step name="step8" action="action3">
            <orderTransition to="step9"/>
        </step>
        <step name="step9" action="action3">
            <conditionTransition to="step10">
                <condition key="data" value="test" comparator="eq"/>
            </conditionTransition>
        </step>
        <step name="step10" action="action3">
            <expCondition expression='parameters.get("params") == 1'/>
        </step>
    </flow>

    <flow name="subflow">
        <step name="sub_step1" action="action1" asyn="true"/>
        <step name="sub_step2" action="action2"/>
    </flow>
</flows>
```

## 4.流程注入

配置文件需要放在classpath下，案例中的配置文件就是在classpath下的flow.xml文件

```
<bean id="flowManager" class="org.cuner.flowframework.core.manager.FlowManager">
    <property name="flowFiles">
        <list>
            <value>flow.xml</value>
        </list>
    </property>
</bean>
```

## 5.执行

```
Result result = flowManager.execute("mainFlow", data);
```
由于各个步骤可异步执行，在主流程的执行中加入了闭锁，只有当所有步骤（包括异步的）执行完成，execute方法才能正确返回。

## 6.查看日志
可查看流程执行的完整堆栈，可以在logback.xml中如下配置
```
<logger name="flow-record" level="info" additivity="false">
    <appender-ref ref="FLOW_RECORD"/>
</logger>
```

上述案例执行后日志如下
```
21:03:23.431 [main] INFO  flow-record - 2018-08-04 21:03:23,415
[flow:mainFlow | start:2018-08-04 21:03:23,353 | end:2018-08-04 21:03:23,380 | cost:27]
|----[step:step1 | start:2018-08-04 21:03:23,354 | end:2018-08-04 21:03:23,355 | cost:1]
|----(step:step2 | start:2018-08-04 21:03:23,358 | end:2018-08-04 21:03:23,359 | cost:1) (asynchronously)
|----[step:step3 | start:2018-08-04 21:03:23,359 | end:2018-08-04 21:03:23,360 | cost:1]
        |----[sub_flow:subflow | start:2018-08-04 21:03:23,359 | end:2018-08-04 21:03:23,360 | cost:1]
                |----(step:sub_step1 | start:2018-08-04 21:03:23,360 | end:2018-08-04 21:03:23,360 | cost:0) (asynchronously)
                |----[step:sub_step2 | start:2018-08-04 21:03:23,360 | end:2018-08-04 21:03:23,360 | cost:0]
|----(step:step4 | start:2018-08-04 21:03:23,361 | end:2018-08-04 21:03:23,361 | cost:0) (asynchronously)
        |----[sub_flow:subflow | start:2018-08-04 21:03:23,361 | end:2018-08-04 21:03:23,361 | cost:0]
                |----[step:sub_step2 | start:2018-08-04 21:03:23,361 | end:2018-08-04 21:03:23,361 | cost:0]
                |----(step:sub_step1 | start:2018-08-04 21:03:23,361 | end:2018-08-04 21:03:23,361 | cost:0) (asynchronously)
|----[step:step5 | start:2018-08-04 21:03:23,378 | end:2018-08-04 21:03:23,380 | cost:2]
|----[step:step6 | start:2018-08-04 21:03:23,380 | end:2018-08-04 21:03:23,380 | cost:0]
|----[step:step7 | start:2018-08-04 21:03:23,380 | end:2018-08-04 21:03:23,380 | cost:0]
|----[step:step8 | start:2018-08-04 21:03:23,380 | end:2018-08-04 21:03:23,380 | cost:0]
|----[step:step9 | start:2018-08-04 21:03:23,380 | end:2018-08-04 21:03:23,380 | cost:0]
|----[step:step10 | start:2018-08-04 21:03:23,380 | end:2018-08-04 21:03:23,380 | cost:0]
```