package org.sweet.revelation.revelation.core.log;

public interface Work {

    boolean isCancelled();

    void info(String message);

    void error(String message);

    void error(String message, Exception e);
}
