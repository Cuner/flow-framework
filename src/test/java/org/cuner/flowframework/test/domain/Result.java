package org.cuner.flowframework.test.domain;

/**
 * Created by houan on 18/7/26.
 */
public class Result {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result='" + result + '\'' +
                '}';
    }
}
