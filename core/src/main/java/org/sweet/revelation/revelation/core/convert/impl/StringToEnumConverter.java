package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

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
