package org.sweet.revelation.revelation.core.convert.impl;

import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Collection;

public class StringToDateTimeZoneConverter extends SafeStringConverter<DateTimeZone> {

    public String getUsage() {
        return "TIMEZONE ID";
    }

    @Override
    public String[] complete(String prefix) {
        Collection<String> result = new ArrayList<String>();

        for (String s : DateTimeZone.getAvailableIDs()) {
            if (prefix.regionMatches(true, 0, s, 0, prefix.length())) {
                result.add(s);
            }
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    protected DateTimeZone doConvert(String s) {
        return DateTimeZone.forID(s);
    }
}
