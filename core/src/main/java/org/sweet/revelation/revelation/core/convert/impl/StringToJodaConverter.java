package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.joda.time.base.BaseLocal;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class StringToJodaConverter<T extends BaseLocal> extends SafeStringConverter<T> {

    private final PatternFormatter[] formatters;

    public StringToJodaConverter(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is mandatory");
        }

        this.formatters = new PatternFormatter[]{new PatternFormatter(pattern)};
    }

    public StringToJodaConverter(String[] patterns) {
        if (patterns == null || patterns.length == 0) {
            throw new IllegalArgumentException("At least one pattern is mandatory");
        }

        this.formatters = new PatternFormatter[patterns.length];

        for (int i = 0; i < patterns.length; ++i) {
            formatters[i] = new PatternFormatter(patterns[i]);
        }
    }

    @Override
    public String[] getUsageValues() {
        String[] result = new String[formatters.length];
        int index = 0;

        for (PatternFormatter patternFormatter : formatters) {
            result[index] = patternFormatter.pattern;

            ++index;
        }

        return result;
    }

    @Override
    protected T doConvert(String s) {
        for (PatternFormatter patternFormatter : formatters) {
            try {
                return doConvert(s, patternFormatter.formatter);
            } catch (IllegalArgumentException e) {
            }
        }

        throw ConvertException.fromSource(s);
    }

    protected abstract T doConvert(String s, DateTimeFormatter formatter);

    private static final class PatternFormatter {

        private final String pattern;

        private final DateTimeFormatter formatter;

        private PatternFormatter(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern is mandatory");
            }

            this.pattern = pattern;
            this.formatter = DateTimeFormat.forPattern(pattern);
        }
    }
}
