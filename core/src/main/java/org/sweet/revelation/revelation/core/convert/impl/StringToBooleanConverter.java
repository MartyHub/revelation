package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

public class StringToBooleanConverter extends SafeStringConverter<Boolean> {

    public String getUsage() {
        return "BOOLEAN";
    }

    @Override
    public String[] getUsageValues() {
        return new String[]{"y", "yes", "true", "n", "no", "false"};
    }

    @Override
    protected Boolean doConvert(String s) {
        if ("y".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        } else if ("n".equalsIgnoreCase(s) || "no".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
            return Boolean.FALSE;
        } else {
            throw ConvertException.fromSource(s);
        }
    }
}
