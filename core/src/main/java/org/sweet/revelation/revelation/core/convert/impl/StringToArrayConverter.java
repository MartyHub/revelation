package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.PrimitiveWrapper;
import org.sweet.revelation.revelation.core.convert.StringConverter;

import java.lang.reflect.Array;
import java.util.StringTokenizer;

public class StringToArrayConverter<T> extends SafeStringConverter<T[]> {

    private static final String DEFAULT_SEPARATOR = ",";

    private final StringConverter<T> elementConverter;

    private final String separator;

    private final Class<T> type;

    public StringToArrayConverter(StringConverter<T> elementConverter, Class<T> type) {
        this(elementConverter, type, DEFAULT_SEPARATOR);
    }

    public StringToArrayConverter(StringConverter<T> elementConverter, Class<T> type, final String separator) {
        if (elementConverter == null) {
            throw new IllegalArgumentException("Array Element Converter is mandatory");
        }

        if (type == null) {
            throw new IllegalArgumentException("Array Type is mandatory");
        }

        if (separator == null) {
            throw new IllegalArgumentException("Array Element separator is mandatory");
        }

        this.elementConverter = elementConverter;
        this.type = type;
        this.separator = separator;
    }

    public String getUsage() {
        return "COMMA SEPARATED LIST";
    }

    public String[] getUsageValues() {
        return elementConverter.getUsageValues();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T[] doConvert(String s) {
        StringTokenizer st = new StringTokenizer(s, separator);
        final int length = st.countTokens();
        final Object result = Array.newInstance(new PrimitiveWrapper(type).wrap(), length);

        for (int i = 0; i < length; ++i) {
            final T element = elementConverter.convert(st.nextToken());

            Array.set(result, i, element);
        }

        return (T[]) result;
    }
}
