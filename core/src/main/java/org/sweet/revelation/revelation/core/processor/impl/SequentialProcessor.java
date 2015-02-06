package org.sweet.revelation.revelation.core.processor.impl;

import org.sweet.revelation.revelation.core.concurrent.ExecutorServiceHelper;
import org.sweet.revelation.revelation.core.log.Activity;

import java.util.Collection;

public abstract class SequentialProcessor<C, P, R> extends StepProcessor<C, P, R> {

    @Override
    protected final ExecutorServiceHelper<R> createExecutorServiceHelper(Activity activity, C command, Collection<P> taskParams) throws Exception {
        return new ExecutorServiceHelper<R>(activity, taskParams.size(), cancelOnFirstFailure(), 1);
    }
}
