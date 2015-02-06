package org.sweet.revelation.revelation.core.convert.impl;

public class StringToDoubleConverter extends SafeStringConverter<Double> {

    public String getUsage() {
        return "DECIMAL";
    }

    @Override
    protected Double doConvert(String s) {
        return Double.valueOf(s);
    }
}
