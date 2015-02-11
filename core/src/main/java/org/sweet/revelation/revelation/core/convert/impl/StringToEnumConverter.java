package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.util.ArrayList;
import java.util.Collection;

public class StringToEnumConverter<E extends Enum<E>> extends SafeStringConverter<E> {

    private final Class<E> enumType;

    public StringToEnumConverter(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("Enum is mandatory");
        }

        this.enumType = enumType;
    }

    public String getUsage() {
        return "TYPE";
    }

    @Override
    public String[] getUsageValues() {
        E[] enumConstants = enumType.getEnumConstants();
        String[] result = new String[enumConstants.length];
        int index = 0;

        for (E enumConstant : enumConstants) {
            result[index] = enumConstant.name();

            ++index;
        }

        return result;
    }

    @Override
    public String[] complete(String prefix) {
        E[] enumConstants = enumType.getEnumConstants();
        Collection<String> result = new ArrayList<String>(enumConstants.length);

        for (E enumConstant : enumConstants) {
            if (prefix.regionMatches(true, 0, enumConstant.name(), 0, prefix.length())) {
                result.add(enumConstant.name());
            }
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    protected E doConvert(String s) {
        E[] enumConstants = enumType.getEnumConstants();

        for (E enumConstant : enumConstants) {
            if (enumConstant.name()
                            .equalsIgnoreCase(s)) {
                return enumConstant;
            }
        }

        throw ConvertException.fromSource(s);
    }
}
