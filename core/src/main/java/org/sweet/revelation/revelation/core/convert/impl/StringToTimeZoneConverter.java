package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;

public class StringToTimeZoneConverter extends SafeStringConverter<TimeZone> {

    public String getUsage() {
        return "TIMEZONE ID";
    }

    @Override
    public String[] complete(String prefix) {
        Collection<String> result = new ArrayList<String>();

        for (String s : TimeZone.getAvailableIDs()) {
            if (prefix.regionMatches(true, 0, s, 0, prefix.length())) {
                result.add(s);
            }
        }

        return result.toArray(new String[result.size()]);
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
