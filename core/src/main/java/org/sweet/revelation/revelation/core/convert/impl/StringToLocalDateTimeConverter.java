package org.sweet.revelation.revelation.core.convert.impl;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter extends StringToJodaConverter<LocalDateTime> {

    public StringToLocalDateTimeConverter(String pattern) {
        super(pattern);
    }

    public StringToLocalDateTimeConverter(String[] patterns) {
        super(patterns);
    }

    public String getUsage() {
        return "TIMESTAMP";
    }

    @Override
    protected LocalDateTime doConvert(String s, DateTimeFormatter formatter) {
        return formatter.parseLocalDateTime(s);
    }
}
