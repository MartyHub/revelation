package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.processor.ProcessorStatus;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleActivity extends AbstractActivity {

    static {
        AnsiConsole.systemInstall();
    }

    ConsoleActivity() {
        this(null);
    }

    public ConsoleActivity(String name) {
        this(name, null);
    }

    protected ConsoleActivity(String name, Activity parent) {
        super(AnsiFactory.enabled(), name, parent);
    }

    public void info(String message) {
        if (message.startsWith(ProcessorStatus.SUCCESS.name())) {
            message = message.replace(ProcessorStatus.SUCCESS.name(), messageBuilder().success()
                                                                                      .bold()
                                                                                      .a(ProcessorStatus.SUCCESS.name())
                                                                                      .build());
        } else if (message.startsWith(ProcessorStatus.CANCELLED.name())) {
            message = message.replace(ProcessorStatus.CANCELLED.name(), messageBuilder().warning()
                                                                                        .bold()
                                                                                        .a(ProcessorStatus.CANCELLED.name())
                                                                                        .build());
        }

        if (name == null) {
            System.out.println(message);
        } else {
            System.out.println(String.format("%s - %s", name, message));
        }

        System.out.flush();
    }

    public void error(String message) {
        System.err.println(messageBuilder().error()
                                           .aIf(name, " - ")
                                           .a(message)
                                           .build());
        System.err.flush();
    }

    public void error(String message, Exception e) {
        error(message);
    }

    public Activity createSubActivity(String name) {
        return new ConsoleActivity(subActivityName(name), this);
    }
}
