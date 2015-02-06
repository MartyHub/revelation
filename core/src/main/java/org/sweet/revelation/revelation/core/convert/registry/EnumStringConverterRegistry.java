package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.impl.StringToEnumConverter;

public class EnumStringConverterRegistry extends StringConverterRegistryDelegate {

    public EnumStringConverterRegistry(StringConverterRegistry delegate) {
        super(delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> StringConverter<? extends T> getConverter(Class<T> type) {
        StringConverter<? extends T> result = super.getConverter(type);

        if (result == null && type.isEnum()) {
            result = new StringToEnumConverter(type);

            register(type, result);
        }

        return result;
    }
}
