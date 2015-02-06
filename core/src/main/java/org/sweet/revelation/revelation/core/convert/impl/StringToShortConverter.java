package org.sweet.revelation.revelation.core.convert.impl;

public class StringToShortConverter extends SafeStringConverter<Short> {

    public String getUsage() {
        return "INTEGER";
    }

    @Override
    protected Short doConvert(String s) {
        return Short.valueOf(s);
    }
}
