package org.sweet.revelation.revelation.core.processor.impl;

import org.sweet.revelation.revelation.core.command.MultiThreadCommand;
import org.sweet.revelation.revelation.core.concurrent.ExecutorServiceHelper;
import org.sweet.revelation.revelation.core.log.Activity;

import java.util.Collection;

public abstract class ParallelProcessor<C extends MultiThreadCommand.HasMultiThreadCommand, P, R> extends StepProcessor<C, P, R> {

    @Override
    protected ExecutorServiceHelper<R> createExecutorServiceHelper(Activity activity, C command, Collection<P> taskParams) throws Exception {
        return new ExecutorServiceHelper<R>(activity, taskParams.size(), cancelOnFirstFailure(), command.getMultiThreadCommand()
                                                                                                        .getNbThreads());
    }
}
