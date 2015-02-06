package org.sweet.revelation.revelation.core.event;

import org.sweet.revelation.revelation.core.command.Parameter;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Lazy
public class CommandNotifier {

    @Autowired(required = false)
    private CommandListener[] listeners;

    @Autowired
    private Activity activity;

    private final Set<String> runningCommands;

    public CommandNotifier() {
        this.runningCommands = new HashSet<String>(1);
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean hasRunningCommand() {
        return !runningCommands.isEmpty();
    }

    public Iterable<String> runningCommands() {
        return runningCommands;
    }

    public void commandStart(String commandName, Parameter[] parameters) {
        runningCommands.add(commandName);

        if (listeners != null) {
            CommandStartEvent event = new CommandStartEvent(commandName, parameters);

            for (CommandListener listener : listeners) {
                try {
                    listener.onCommandStart(event);
                } catch (Exception e) {
                    activity.error(e.getMessage(), e);
                }
            }
        }
    }

    public void commandEnd(String commandName, ProcessorReport report) {
        if (runningCommands.remove(commandName) && listeners != null) {
            CommandEndEvent event = new CommandEndEvent(commandName, report);

            for (CommandListener listener : listeners) {
                try {
                    listener.onCommandEnd(event);
                } catch (Exception e) {
                    activity.error(e.getMessage(), e);
                }
            }
        }
    }
}
