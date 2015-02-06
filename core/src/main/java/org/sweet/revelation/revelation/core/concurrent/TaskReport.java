package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.processor.ProcessorStatus;

public class TaskReport<R> {

    public static <R> TaskReport<R> success(R result) {
        return new TaskReport<R>(ProcessorStatus.SUCCESS, result, null);
    }

    public static <R> TaskReport<R> cancelled() {
        return new TaskReport<R>(ProcessorStatus.CANCELLED, null, null);
    }

    public static <R> TaskReport<R> failure(Exception e) {
        return new TaskReport<R>(ProcessorStatus.ERROR, null, e);
    }

    private final ProcessorStatus status;

    private final R result;

    private final Exception cause;

    private TaskReport(ProcessorStatus status, R result, Exception cause) {
        this.status = status;
        this.result = result;
        this.cause = cause;
    }

    public ProcessorStatus getStatus() {
        return status;
    }

    public R getResult() {
        return result;
    }

    public Exception getCause() {
        return cause;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(status);

        if (result != null) {
            sb.append(" : ");
            sb.append(result);
        }

        if (cause != null && cause.getMessage() != null) {
            sb.append(" (");
            sb.append(cause.getMessage());
            sb.append(")");
        }

        return sb.toString();
    }
}
