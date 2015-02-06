package org.sweet.revelation.revelation.core.convert.impl;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter extends StringToJodaConverter<LocalTime> {

    public StringToLocalTimeConverter(String pattern) {
        super(pattern);
    }

    public StringToLocalTimeConverter(String[] patterns) {
        super(patterns);
    }

    public String getUsage() {
        return "TIME";
    }

    @Override
    protected LocalTime doConvert(String s, DateTimeFormatter formatter) {
        return formatter.parseLocalTime(s);
    }
}
