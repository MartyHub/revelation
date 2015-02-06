package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

public class StringToCharacterConverter extends SafeStringConverter<Character> {

    public String getUsage() {
        return "CHARACTER";
    }

    @Override
    protected Character doConvert(String s) {
        if (s.length() != 1) {
            throw new ConvertException(String.format("Invalid character <%s>", s));
        }

        return s.charAt(0);
    }
}
