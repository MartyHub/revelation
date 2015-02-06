package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;

import java.util.concurrent.Callable;

public class TaskProcessor<P, R> implements Callable<TaskReport<R>> {

    private final PredictableWorkLoad pw;

    private final Task<P, R> task;

    private final P param;

    public TaskProcessor(PredictableWorkLoad pw, Task<P, R> task, P param) {
        this.pw = pw;
        this.task = task;
        this.param = param;
    }

    public TaskReport<R> call() throws Exception {
        if (pw.isCancelled()) {
            return TaskReport.cancelled();
        }

        try {
            R result = task.run(pw, param);

            pw.worked(param.toString());

            return TaskReport.success(result);
        } catch (Exception e) {
            pw.failed(param.toString(), e);

            return TaskReport.failure(e);
        }
    }
}
