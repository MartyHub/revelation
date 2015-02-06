package org.sweet.revelation.revelation.core.convert.impl;

import org.joda.time.DateTimeZone;

public class StringToDateTimeZoneConverter extends SafeStringConverter<DateTimeZone> {

    public String getUsage() {
        return "TIMEZONE ID";
    }

    @Override
    protected DateTimeZone doConvert(String s) {
        return DateTimeZone.forID(s);
    }
}
