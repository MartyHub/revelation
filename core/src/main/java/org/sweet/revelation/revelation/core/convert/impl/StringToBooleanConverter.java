package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.util.ArrayList;
import java.util.Collection;

public class StringToBooleanConverter extends SafeStringConverter<Boolean> {

    public String getUsage() {
        return "BOOLEAN";
    }

    @Override
    public String[] getUsageValues() {
        return new String[]{"y", "yes", "true", "n", "no", "false"};
    }

    @Override
    public String[] complete(String prefix) {
        String[] values = getUsageValues();
        Collection<String> result = new ArrayList<String>(values.length);

        for (String value : values) {
            if (prefix.regionMatches(true, 0, value, 0, prefix.length())) {
                result.add(value);
            }
        }

        return result.toArray(new String[result.size()]);
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
