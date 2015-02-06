package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.text.MessageBuilder;

public abstract class AbstractWork implements Work {

    protected final Duration.Builder durationBuilder;

    protected final AbstractActivity parent;

    protected AbstractWork(AbstractActivity parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is mandatory");
        }

        this.parent = parent;
        this.durationBuilder = Duration.builder();
    }

    public final boolean isCancelled() {
        return parent.isCancelled();
    }

    public final void info(String message) {
        parent.info(message);
    }

    public final void error(String message) {
        parent.error(message);
    }

    public final void error(String message, Exception e) {
        parent.error(message, e);
    }

    protected MessageBuilder messageBuilder() {
        return parent.messageBuilder();
    }
}
