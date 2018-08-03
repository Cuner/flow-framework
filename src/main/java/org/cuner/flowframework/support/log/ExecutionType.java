package org.cuner.flowframework.support.log;

public enum ExecutionType {
    /**
     * 流程
     */
    FLOW("flow"),

    /**
     * 节点
     */
    STEP("step"),

    /**
     * 子流程
     */
    SUBFLOW("sub_flow");

    private String type;

    ExecutionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
