package org.sweet.revelation.revelation.core.convert.impl;

public class StringToIntegerConverter extends SafeStringConverter<Integer> {

    public String getUsage() {
        return "INTEGER";
    }

    @Override
    protected Integer doConvert(String s) {
        return Integer.valueOf(s);
    }
}
