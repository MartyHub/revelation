package org.sweet.revelation.revelation.core.main;

import org.fusesource.jansi.AnsiConsole;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.sweet.revelation.revelation.core.processor.ProcessorStatus;

public class ProcessorFinalizer {

    private final boolean systemExitDisabled;

    private final Work work;

    public ProcessorFinalizer(final boolean systemExitDisabled, Work work) {
        this.systemExitDisabled = systemExitDisabled;
        this.work = work;
    }

    void endAndExit(ProcessorReport report, String... messages) {
        exit(end(report, messages));
    }

    int end(ProcessorReport report, String... messages) {
        doEnd(report, messages == null ? new String[0] : messages);

        return report.getStatus()
                     .getExitCode();
    }

    void exit(final int exitCode) {
        if (!systemExitDisabled && !work.isCancelled()) {
            AnsiConsole.systemUninstall();

            System.exit(exitCode);
        }
    }

    private void doEnd(ProcessorReport report, String[] messages) {
        if (report.getCause() != null) {
            report.getCause()
                  .printStackTrace();
        }

        for (String message : messages) {
            work.info(message);
        }

        if (report.getStatus() == ProcessorStatus.ERROR) {
            work.error(report.toString());
        } else {
            work.info(report.toString());
        }
    }
}
