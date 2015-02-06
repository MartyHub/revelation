package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleStringConverterRegistry implements StringConverterRegistry {

    private final ConcurrentMap<Class<?>, StringConverter<?>> converters = new ConcurrentHashMap<Class<?>, StringConverter<?>>();

    private final boolean allowOverriding;

    public SimpleStringConverterRegistry() {
        this(false);
    }

    public SimpleStringConverterRegistry(final boolean allowOverriding) {
        this.allowOverriding = allowOverriding;
    }

    public <T> void register(Class<T> type, StringConverter<? extends T> converter) {
        StringConverter<?> currentConverter = converters.putIfAbsent(type, converter);

        if (!allowOverriding && currentConverter != null) {
            throw new ConvertException(String.format("Type <%s> is already handled by <%s>", type, currentConverter));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> StringConverter<? extends T> getConverter(Class<T> type) {
        return (StringConverter<? extends T>) converters.get(type);
    }

    public Iterator<Class<?>> iterator() {
        return converters.keySet()
                         .iterator();
    }

    public int size() {
        return converters.size();
    }
}
