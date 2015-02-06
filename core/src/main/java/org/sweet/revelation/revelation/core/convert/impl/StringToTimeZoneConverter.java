package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.util.TimeZone;

public class StringToTimeZoneConverter extends SafeStringConverter<TimeZone> {

    public String getUsage() {
        return "TIMEZONE ID";
    }

    @Override
    protected TimeZone doConvert(String s) {
        TimeZone timeZone = TimeZone.getTimeZone(s);

        if (s.equals(timeZone.getID())) {
            return timeZone;
        }

        throw ConvertException.fromSource(s);
    }
}
