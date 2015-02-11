package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.sweet.revelation.revelation.core.convert.StringConverter;

public abstract class SafeStringConverter<T> implements StringConverter<T> {

    public T convert(String s) {
        if (s == null || "".equals(s)) {
            return getDefault();
        }

        try {
            return doConvert(s);
        } catch (ConvertException e) {
            throw e;
        } catch (Exception e) {
            throw ConvertException.fromSource(s, e);
        }
    }

    public String[] getUsageValues() {
        return new String[0];
    }

    public String getValueSeparator() {
        return null;
    }

    public String[] complete(String prefix) {
        return new String[0];
    }

    protected T getDefault() {
        return null;
    }

    protected abstract T doConvert(String s);
}
