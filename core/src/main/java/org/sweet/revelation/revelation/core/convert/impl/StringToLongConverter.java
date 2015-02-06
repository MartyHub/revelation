package org.sweet.revelation.revelation.core.convert.impl;

public class StringToLongConverter extends SafeStringConverter<Long> {

    public String getUsage() {
        return "INTEGER";
    }

    @Override
    protected Long doConvert(String s) {
        return Long.valueOf(s);
    }
}
