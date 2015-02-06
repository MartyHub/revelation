package org.sweet.revelation.revelation.core.convert.impl;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

public class StringToLocalDateConverter extends StringToJodaConverter<LocalDate> {

    public StringToLocalDateConverter(String pattern) {
        super(pattern);
    }

    public StringToLocalDateConverter(String[] patterns) {
        super(patterns);
    }

    public String getUsage() {
        return "DATE";
    }

    @Override
    protected LocalDate doConvert(String s, DateTimeFormatter formatter) {
        return formatter.parseLocalDate(s);
    }
}
