package org.sweet.revelation.revelation.core.event;

import java.util.EventObject;

public class CommandEvent extends EventObject {

    private final long timestamp;

    public CommandEvent(String commandName) {
        super(commandName);

        this.timestamp = System.currentTimeMillis();
    }

    public final String getCommandName() {
        return (String) source;
    }

    public final long getTimestamp() {
        return timestamp;
    }
}
