package org.sweet.revelation.revelation.core.command;

public class Parameter {

    private final String name;

    private final String value;

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('-');
        sb.append(name);
        sb.append('=');
        sb.append(value);

        return sb.toString();
    }
}
