package org.sweet.revelation.revelation.core.processor;

import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;

public class ProcessorMessageBuilder {

    private final int taskCount;

    private int successfulTaskCount;

    private int failedTaskCount;

    private int canceledTaskCount;

    public ProcessorMessageBuilder(final int taskCount) {
        this.taskCount = taskCount;
    }

    public ProcessorMessageBuilder successfulTaskCount(final int count) {
        this.successfulTaskCount = count;

        return this;
    }

    public ProcessorMessageBuilder failedTaskCount(final int count) {
        this.failedTaskCount = count;

        return this;
    }

    public ProcessorMessageBuilder canceledTaskCount(final int count) {
        this.canceledTaskCount = count;

        return this;
    }

    public String build(ProcessorStatus status) {
        return toString();
    }

    @Override
    public String toString() {
        PrettyIntegerFormatter formatter = new PrettyIntegerFormatter();
        StringBuilder sb = new StringBuilder();

        if (failedTaskCount != 0) {
            sb.append(formatter.format(failedTaskCount));
            sb.append(" failure(s) / ");
        }

        if (canceledTaskCount != 0) {
            sb.append(formatter.format(canceledTaskCount));
            sb.append(" cancel(s) / ");
        }

        if (successfulTaskCount != 0) {
            sb.append(formatter.format(successfulTaskCount));
            sb.append(" success / ");
        }

        sb.append(formatter.format(taskCount));
        sb.append(" task(s)");

        return sb.toString();
    }
}
