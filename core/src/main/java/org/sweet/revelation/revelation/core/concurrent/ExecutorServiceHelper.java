package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class ExecutorServiceHelper<R> {

    private final int taskCount;

    private final MutableThreadPoolExecutor executor;

    private final ExecutorCompletionService<TaskReport<R>> executorCompletionService;

    private final PredictableWorkLoad pw;

    private int submittedCount;

    public ExecutorServiceHelper(Activity activity, final int taskCount, final boolean cancelOnFirstFailure, final int threadCount) {
        this(activity, taskCount, cancelOnFirstFailure, new MutableThreadPoolExecutor(threadCount, taskCount, activity));
    }

    public ExecutorServiceHelper(Activity activity, final int taskCount, final boolean cancelOnFirstFailure, MutableThreadPoolExecutor executor) {
        this.taskCount = taskCount;
        this.executor = executor;
        this.executorCompletionService = new ExecutorCompletionService<TaskReport<R>>(executor);
        this.pw = activity.start(taskCount, cancelOnFirstFailure)
                          .synchronize();
    }

    public int getTaskCount() {
        return taskCount;
    }

    public <P> void submit(Task<P, R> task, P param) {
        executorCompletionService.submit(new TaskProcessor<P, R>(pw, task, param));

        ++submittedCount;
    }

    public ProcessorReport process(Work work, TaskResultProcessor<?, R> taskResultProcessor) throws Exception {
        executor.shutdown();

        taskResultProcessor.start(work);

        try {
            while (submittedCount > 0) {
                final Future<TaskReport<R>> future = executorCompletionService.take();

                --submittedCount;

                taskResultProcessor.addTaskReport(future.get());
            }

            return taskResultProcessor.toProcessorReport();
        } finally {
            taskResultProcessor.done(work);
        }
    }
}
