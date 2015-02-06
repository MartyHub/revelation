package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.PrimitiveWrapper;
import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;

public class PrimitiveStringConverterRegistry extends StringConverterRegistryDelegate {

    public PrimitiveStringConverterRegistry(StringConverterRegistry delegate) {
        super(delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> StringConverter<? extends T> getConverter(Class<T> type) {
        return super.getConverter(new PrimitiveWrapper(type).wrap());
    }
}
