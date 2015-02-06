package org.sweet.revelation.revelation.core.event;

import org.sweet.revelation.revelation.core.command.Parameter;

public class CommandStartEvent extends CommandEvent {

    private final Parameter[] parameters;

    public CommandStartEvent(String commandName, Parameter[] parameters) {
        super(commandName);

        this.parameters = parameters == null ? new Parameter[0] : parameters.clone();
    }

    public Parameter[] getParameters() {
        return parameters;
    }
}
