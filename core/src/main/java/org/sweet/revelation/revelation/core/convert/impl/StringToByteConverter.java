package org.sweet.revelation.revelation.core.convert.impl;

public class StringToByteConverter extends SafeStringConverter<Byte> {

    public String getUsage() {
        return "INTEGER";
    }

    @Override
    protected Byte doConvert(String s) {
        return Byte.valueOf(s);
    }
}
