package org.cuner.flowframework.support.log;

import org.cuner.flowframework.support.ThreadLocalDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Execution {
    /**
     * 执行对象类型
     */
    private ExecutionType executionType;

    /**
     * 执行对象名称
     */
    private String name;

    /**
     * 执行开始时间
     */
    private long startTime;

    /**
     * 执行结束事件
     */
    private long endTime;

    /**
     * 是否异步执行
     */
    private boolean asyn;

    private List<Execution> children = new ArrayList<>();

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public String getNodeName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<Execution> getChildren() {
        return children;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setChildren(List<Execution> children) {
        this.children = children;
    }

    public boolean isAsyn() {
        return asyn;
    }

    public void setAsyn(boolean asyn) {
        this.asyn = asyn;
    }

    public String getName() {
        return name;
    }

    /*
    构造流程执行堆栈日志
     */
    public static String getExecutionString(Execution execution, int level) {
        try {
            StringBuilder sb = new StringBuilder();
            if (level == 0) {
                sb.append(ThreadLocalDateFormat.current().format(new Date()));
                sb.append("\n\r");
            }
            if (null != execution) {

                if (level > 0) {
                    for (int i = 0; i < level - 1; i++) {
                        sb.append("        ");
                    }
                    sb.append("|----");
                }
                String start = ThreadLocalDateFormat.current().format(new Date(execution.getStartTime()));
                String end = ThreadLocalDateFormat.current().format(new Date(execution.getEndTime()));

                if (execution.isAsyn()) {
                    sb.append("(").append(execution.getExecutionType().getType()).
                            append(":").append(execution.getNodeName()).append(" | ").
                            append("start:").append(start).append(" | ").
                            append("end:").append(end).append(" | ").
                            append("cost:").append(execution.getEndTime() - execution.getStartTime()).append(") (asynchronously)");
                } else {
                    sb.append("[").append(execution.getExecutionType().getType()).
                            append(":").append(execution.getNodeName()).append(" | ").
                            append("start:").append(start).append(" | ").
                            append("end:").append(end).append(" | ").
                            append("cost:").append(execution.getEndTime() - execution.getStartTime()).append("]");

                }

                if (execution.getChildren() != null) {
                    for (Execution execution1 : execution.getChildren()) {
                        sb.append("\n\r");
                        sb.append(getExecutionString(execution1, level + 1));
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "parser execution stack failed!";
        }
    }
}
