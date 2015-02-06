package org.sweet.revelation.revelation.core.main;

import org.sweet.revelation.revelation.core.event.CommandNotifier;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;

public class ShutdownHook extends Thread {

    private final CommandNotifier commandNotifier;

    public ShutdownHook(CommandNotifier commandNotifier) {
        this.commandNotifier = commandNotifier;
    }

    @Override
    public void run() {
        synchronized (commandNotifier) {
            if (commandNotifier.hasRunningCommand()) {
                final long timeoutInSeconds = 10;
                Activity activity = commandNotifier.getActivity();

                activity.cancel();
                activity.info(String.format("A command is currently being processed, waiting for its end for %s seconds", timeoutInSeconds));

                try {
                    commandNotifier.wait(timeoutInSeconds * 1000);
                } catch (final InterruptedException e) {
                    activity.info("Done through interruption");
                }

                for (String commandName : commandNotifier.runningCommands()) {
                    commandNotifier.commandEnd(commandName, ProcessorReport.failure("Timeout after cancel"));
                }
            }
        }
    }
}
