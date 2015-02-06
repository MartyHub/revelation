package org.sweet.revelation.revelation.core.convert.impl;

public class StringToFloatConverter extends SafeStringConverter<Float> {

    public String getUsage() {
        return "DECIMAL";
    }

    @Override
    protected Float doConvert(String s) {
        return Float.valueOf(s);
    }
}
