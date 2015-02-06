package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;

public class SynchronizedPredictableWorkLoad implements PredictableWorkLoad {

    private final PredictableWorkLoad delegate;

    public SynchronizedPredictableWorkLoad(PredictableWorkLoad delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate is mandatory");
        }

        this.delegate = delegate;
    }

    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    public synchronized void info(String message) {
        delegate.info(appendThreadName(message));
    }

    public void error(String message) {
        delegate.error(appendThreadName(message));
    }

    public void error(String message, Exception e) {
        delegate.error(appendThreadName(message), e);
    }

    public synchronized void worked(String message) {
        delegate.worked(appendThreadName(message));
    }

    public synchronized void failed(String message, Exception cause) {
        delegate.failed(appendThreadName(message), cause);
    }

    public PredictableWorkLoad synchronize() {
        return this;
    }

    private String appendThreadName(String message) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(Thread.currentThread()
                        .getName());
        sb.append("] ");
        sb.append(message);

        return sb.toString();
    }
}
