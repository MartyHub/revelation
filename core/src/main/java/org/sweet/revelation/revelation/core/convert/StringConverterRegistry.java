package org.sweet.revelation.revelation.core.convert;

public interface StringConverterRegistry extends Iterable<Class<?>> {

    <T> void register(Class<T> type, StringConverter<? extends T> converter);

    <T> StringConverter<? extends T> getConverter(Class<T> type);

    int size();
}
