package org.sweet.revelation.revelation.core.command;

import java.util.ArrayList;
import java.util.Collection;

public class CommandLineParameterBuilder {

    private final Collection<String> args;

    public CommandLineParameterBuilder() {
        this.args = new ArrayList<String>();
    }

    public CommandLineParameterBuilder addArg(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name is mandatory");
        }

        args.add(new StringBuilder("-").append(name)
                                       .append("=")
                                       .append(value == null ? "" : value)
                                       .toString());

        return this;
    }

    public CommandLineParameterBuilder addArg(String s) {
        if (s != null) {
            args.add(s);
        }

        return this;
    }

    public Parameter[] build() {
        Collection<Parameter> parameters = new ArrayList<Parameter>(args.size());

        for (String arg : args) {
            parameters.add(parseParameter(arg));
        }

        return parameters.toArray(new Parameter[parameters.size()]);
    }

    private Parameter parseParameter(String s) {
        validateParameter(s);

        final int index = s.indexOf('=');
        final String name = s.substring(1, index);
        final String value = s.substring(index + 1);

        return new Parameter(name, value);
    }

    private void validateParameter(String s) {
        if (s == null || s.charAt(0) != '-' || s.indexOf('=') < 2) {
            throw new InvalidParameterException(String.format("Invalid parameter <%s>, syntax is <-name=value>", s));
        }
    }
}
