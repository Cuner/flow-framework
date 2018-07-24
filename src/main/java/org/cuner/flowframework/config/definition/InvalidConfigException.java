package org.cuner.flowframework.config.definition;

/**
 * Created by houan on 18/7/24.
 */
public class InvalidConfigException extends Exception {

    public InvalidConfigException() {

    }

    public InvalidConfigException(String msg) {
        super("invalid config: " + msg);
    }

}
