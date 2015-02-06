package org.sweet.revelation.revelation.core.processor.impl;

import org.sweet.revelation.revelation.core.concurrent.ExecutorServiceHelper;
import org.sweet.revelation.revelation.core.concurrent.Task;
import org.sweet.revelation.revelation.core.concurrent.TaskResultProcessor;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.Processor;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public abstract class StepProcessor<C, P, R> implements Processor<C> {

    @Autowired
    private Activity activity;

    public ProcessorReport process(C command) throws Exception {
        Collection<P> taskParams = preProcess(activity, command);

        if (taskParams == null || taskParams.isEmpty()) {
            return ProcessorReport.done(!failOnEmptyTask(), "No task found", null);
        }

        ExecutorServiceHelper<R> executorServiceHelper = process(activity, command, taskParams);

        return postProcess(activity, command, executorServiceHelper);
    }

    protected Collection<P> preProcess(Activity activity, C command) throws Exception {
        UnknownWorkLoad taskLooking = activity.createSubActivity("Looking for task(s)")
                                              .start();
        Collection<P> result = createTaskParams(taskLooking, command);

        taskLooking.done();

        return result;
    }

    protected ExecutorServiceHelper<R> process(Activity activity, C command, Collection<P> taskParams) throws Exception {
        ExecutorServiceHelper<R> result = createExecutorServiceHelper(activity, command, taskParams);
        int index = 0;

        for (final P taskParam : taskParams) {
            if (activity.isCancelled()) {
                break;
            }

            result.submit(newTask(command, index), taskParam);

            ++index;
        }

        return result;
    }

    protected ProcessorReport postProcess(Activity activity, C command, ExecutorServiceHelper<R> executorServiceHelper) throws Exception {
        UnknownWorkLoad taskChecking = activity.createSubActivity("Checking task result(s)")
                                               .start();
        ProcessorReport processorReport = executorServiceHelper.process(taskChecking, createTaskResultProcessor(command,
                executorServiceHelper.getTaskCount()));

        taskChecking.done();

        return processorReport;
    }

    protected abstract ExecutorServiceHelper<R> createExecutorServiceHelper(Activity activity, C command, Collection<P> taskParams) throws Exception;

    protected boolean cancelOnFirstFailure() throws Exception {
        return true;
    }

    protected boolean failOnEmptyTask() throws Exception {
        return true;
    }

    protected abstract Collection<P> createTaskParams(Work work, C command) throws Exception;

    protected abstract Task<P, R> newTask(C command, final int index) throws Exception;

    protected abstract TaskResultProcessor<C, R> createTaskResultProcessor(C command, final int taskCount) throws Exception;
}
