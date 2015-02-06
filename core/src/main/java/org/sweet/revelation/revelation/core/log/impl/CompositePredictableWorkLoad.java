package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;

public class CompositePredictableWorkLoad implements PredictableWorkLoad {

    private final PredictableWorkLoad[] workLoads;

    private final Activity parent;

    CompositePredictableWorkLoad(Activity parent, PredictableWorkLoad[] workLoads) {
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

    public void worked(String message) {
        for (PredictableWorkLoad pw : workLoads) {
            pw.worked(message);
        }
    }

    public void failed(String message, Exception cause) {
        for (PredictableWorkLoad pw : workLoads) {
            pw.failed(message, cause);
        }
    }

    public PredictableWorkLoad synchronize() {
        return new SynchronizedPredictableWorkLoad(this);
    }
}
