package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.ProcessorMessageBuilder;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.sweet.revelation.revelation.core.processor.ProcessorStatus;

public class TaskResultProcessor<C, R> {

    protected final C command;

    protected final int taskCount;

    private int successfulTaskCount;

    private int failedTaskCount;

    private int canceledTaskCount;

    private Exception firstError;

    public TaskResultProcessor(C command, final int taskCount) {
        this.command = command;
        this.taskCount = taskCount;
    }

    @Override
    public String toString() {
        return newProcessorMessageBuilder().successfulTaskCount(successfulTaskCount)
                                           .failedTaskCount(failedTaskCount)
                                           .canceledTaskCount(canceledTaskCount)
                                           .toString();
    }

    protected void start(Work work) throws Exception {
    }

    protected void process(R result) throws Exception {
    }

    protected void done(Work work) throws Exception {
    }

    protected ProcessorMessageBuilder newProcessorMessageBuilder() {
        return new ProcessorMessageBuilder(taskCount);
    }

    ProcessorReport toProcessorReport() {
        ProcessorMessageBuilder processorMessageBuilder = newProcessorMessageBuilder().successfulTaskCount(successfulTaskCount)
                                                                                      .failedTaskCount(failedTaskCount)
                                                                                      .canceledTaskCount(canceledTaskCount);

        if (failedTaskCount == 0) {
            if (canceledTaskCount == 0) {
                return ProcessorReport.success(processorMessageBuilder.build(ProcessorStatus.SUCCESS));
            }

            return ProcessorReport.cancelled(processorMessageBuilder.build(ProcessorStatus.CANCELLED));
        }

        return ProcessorReport.failure(processorMessageBuilder.build(ProcessorStatus.ERROR), firstError);
    }

    void addTaskReport(TaskReport<R> taskReport) {
        switch (taskReport.getStatus()) {
            case ERROR:
                addError(taskReport.getCause());

                break;
            case SUCCESS:
                try {
                    process(taskReport.getResult());

                    ++successfulTaskCount;
                } catch (Exception e) {
                    addError(e);
                }

                break;
            case CANCELLED:
                ++canceledTaskCount;

                break;
            default:
                throw new IllegalArgumentException(String.format("Don't know how to handle <%s>", taskReport.getStatus()));
        }
    }

    private void addError(Exception cause) {
        ++failedTaskCount;

        if (firstError == null) {
            firstError = cause;
        }
    }
}
