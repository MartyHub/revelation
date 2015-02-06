package org.sweet.revelation.revelation.core.convert;

public interface StringConverter<T> {

    T convert(String s) throws ConvertException;

    String getUsage();

    String[] getUsageValues();
}
