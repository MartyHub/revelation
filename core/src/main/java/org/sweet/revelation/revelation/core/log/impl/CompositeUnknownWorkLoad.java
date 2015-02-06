package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;

public class CompositeUnknownWorkLoad implements UnknownWorkLoad {

    private final Activity parent;

    private final UnknownWorkLoad[] workLoads;

    CompositeUnknownWorkLoad(Activity parent, UnknownWorkLoad[] workLoads) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is mandatory");
        }

        if (workLoads == null) {
            throw new IllegalArgumentException("WorkLoads is mandatory");
        }

        this.parent = parent;
        this.workLoads = workLoads;
    }

    public boolean isCancelled() {
        return parent.isCancelled();
    }

    public void info(String message) {
        parent.info(message);
    }

    public void error(String message) {
        parent.error(message);
    }

    public void error(String message, Exception e) {
        parent.error(message, e);
    }

    public void done() {
        for (UnknownWorkLoad uw : workLoads) {
            uw.done();
        }
    }
}
