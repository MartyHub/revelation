package org.sweet.revelation.revelation.core.convert.registry;

import org.sweet.revelation.revelation.core.convert.impl.*;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.TimeZone;

public class DefaultStringConverterRegistry extends SimpleStringConverterRegistry {

    public DefaultStringConverterRegistry() {
        register(BigDecimal.class, new StringToBigDecimalConverter());
        register(Boolean.class, new StringToBooleanConverter());
        register(Byte.class, new StringToByteConverter());
        register(Character.class, new StringToCharacterConverter());
        register(DateTimeZone.class, new StringToDateTimeZoneConverter());
        register(Double.class, new StringToDoubleConverter());
        register(File.class, new StringToFileConverter());
        register(Float.class, new StringToFloatConverter());
        register(Integer.class, new StringToIntegerConverter());
        register(Long.class, new StringToLongConverter());
        register(Properties.class, new StringToPropertiesConverter());
        register(Short.class, new StringToShortConverter());
        register(String.class, new StringToStringConverter());
        register(TimeZone.class, new StringToTimeZoneConverter());
    }
}
