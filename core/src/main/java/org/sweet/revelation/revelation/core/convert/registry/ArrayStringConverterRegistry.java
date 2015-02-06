package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.impl.StringToArrayConverter;

public class ArrayStringConverterRegistry extends StringConverterRegistryDelegate {

    public ArrayStringConverterRegistry(StringConverterRegistry delegate) {
        super(delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> StringConverter<? extends T> getConverter(Class<T> type) {
        StringConverter<? extends T> result = super.getConverter(type);

        if (result == null && type.isArray()) {
            Class<?> componentType = type.getComponentType();
            StringConverter<?> componentConverter = getDelegate().getConverter(componentType);

            if (componentConverter != null) {
                result = new StringToArrayConverter(componentConverter, componentType);

                register(type, result);
            }
        }

        return result;
    }
}
