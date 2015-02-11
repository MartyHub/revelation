package org.sweet.revelation.revelation.core.command;

import java.util.ArrayList;
import java.util.Collection;

public class CommandLineParametersBuilder {

    private final boolean strict;

    private final Collection<String> args;

    public CommandLineParametersBuilder(final boolean strict) {
        this.strict = strict;
        this.args = new ArrayList<String>();
    }

    public CommandLineParametersBuilder addArg(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name is mandatory");
        }

        args.add(CommandLineParameterBuilder.builder(strict)
                                            .arg(name, value)
                                            .toString());

        return this;
    }

    public CommandLineParametersBuilder addArg(String s) {
        if (s != null) {
            args.add(s);
        }

        return this;
    }

    public Parameter[] build() {
        Collection<Parameter> parameters = new ArrayList<Parameter>(args.size());

        for (String arg : args) {
            parameters.add(CommandLineParameterBuilder.builder(strict)
                                                      .arg(arg)
                                                      .build());
        }

        return parameters.toArray(new Parameter[parameters.size()]);
    }
}
