package org.sweet.revelation.revelation.core.processor;

public enum ProcessorStatus {

    SUCCESS, ERROR, CANCELLED;

    public int getExitCode() {
        return ordinal();
    }
}
