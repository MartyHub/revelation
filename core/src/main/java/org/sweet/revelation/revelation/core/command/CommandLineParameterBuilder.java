package org.sweet.revelation.revelation.core.command;

public class CommandLineParameterBuilder {

    public static CommandLineParameterBuilder builder(final boolean strict) {
        return new CommandLineParameterBuilder(strict);
    }

    private final boolean strict;

    private StringBuilder sb;

    public CommandLineParameterBuilder(final boolean strict) {
        this.strict = strict;
    }

    public CommandLineParameterBuilder arg(String s) {
        this.sb = new StringBuilder(s);

        return this;
    }

    public CommandLineParameterBuilder arg(String name, String value) {
        this.sb = new StringBuilder("-").append(name)
                                        .append("=")
                                        .append(value);

        return this;
    }

    public Parameter build() {
        if (strict) {
            validate();
        }

        String name = null;
        String value = null;

        if (sb != null) {
            final int index = sb.indexOf("=");

            if (index > 1) {
                name = sb.substring(1, index);

                if (index < sb.length()) {
                    value = sb.substring(index + 1);
                }
            }
        }

        return new Parameter(name, value);
    }

    @Override
    public String toString() {
        return sb == null ? "null" : sb.toString();
    }

    private void validate() {
        if (sb == null || sb.length() == 0 || sb.charAt(0) != '-' || sb.indexOf("=") < 2) {
            throw new InvalidParameterException(String.format("Invalid parameter <%s>, syntax is <-name=value>", sb.toString()));
        }
    }
}
