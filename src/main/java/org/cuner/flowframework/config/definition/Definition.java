package org.cuner.flowframework.config.definition;

/**
 * Created by houan on 18/7/24.
 */
public interface Definition {

    boolean isValid();

    Object getInstance() throws InvalidConfigException;

}
