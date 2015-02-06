package org.sweet.revelation.revelation.core.event;

import org.sweet.revelation.revelation.core.processor.ProcessorReport;

public class CommandEndEvent extends CommandEvent {

    private final ProcessorReport report;

    public CommandEndEvent(String commandName, ProcessorReport report) {
        super(commandName);

        this.report = report;
    }

    public ProcessorReport getReport() {
        return report;
    }
}
