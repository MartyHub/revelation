package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;

import java.util.Iterator;

public abstract class StringConverterRegistryDelegate implements StringConverterRegistry {

    private final StringConverterRegistry delegate;

    protected StringConverterRegistryDelegate(StringConverterRegistry delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate is mandatory");
        }

        this.delegate = delegate;
    }

    public <T> void register(Class<T> type, StringConverter<? extends T> converter) {
        delegate.register(type, converter);
    }

    public <T> StringConverter<? extends T> getConverter(Class<T> type) {
        return delegate.getConverter(type);
    }

    public Iterator<Class<?>> iterator() {
        return delegate.iterator();
    }

    public int size() {
        return delegate.size();
    }

    protected final StringConverterRegistry getDelegate() {
        return delegate;
    }
}
