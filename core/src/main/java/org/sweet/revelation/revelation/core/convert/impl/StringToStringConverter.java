package org.sweet.revelation.revelation.core.convert.impl;

public class StringToStringConverter extends SafeStringConverter<String> {

    @Override
    protected String doConvert(String s) {
        return s;
    }

    public String getUsage() {
        return "STRING";
    }
}
