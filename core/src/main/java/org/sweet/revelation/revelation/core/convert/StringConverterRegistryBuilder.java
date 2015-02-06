package org.sweet.revelation.revelation.core.convert;

import org.sweet.revelation.revelation.core.convert.impl.StringToLocalDateConverter;
import org.sweet.revelation.revelation.core.convert.impl.StringToLocalDateTimeConverter;
import org.sweet.revelation.revelation.core.convert.impl.StringToLocalTimeConverter;
import org.sweet.revelation.revelation.core.convert.registry.ArrayStringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.registry.DefaultStringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.registry.EnumStringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.registry.PrimitiveStringConverterRegistry;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class StringConverterRegistryBuilder {

    private boolean enumEnabled = true;

    private boolean primitiveEnabled = true;

    private boolean arrayEnabled = true;

    private String[] datePatterns = {"yyyy-MM-dd", "yyyyMMdd"};

    private String[] dateTimePatterns = {"yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyyMMddHHmmssSSS"};

    private String[] timePatterns = {"HH:mm:ss.SSS", "HH:mm:ss", "HH:mm", "HHmmssSSS", "HHmmss", "HHmm"};

    public StringConverterRegistry build() {
        StringConverterRegistry result = new DefaultStringConverterRegistry();

        if (datePatterns != null && datePatterns.length > 0) {
            result.register(LocalDate.class, new StringToLocalDateConverter(datePatterns));
        }

        if (dateTimePatterns != null && dateTimePatterns.length > 0) {
            result.register(LocalDateTime.class, new StringToLocalDateTimeConverter(dateTimePatterns));
        }

        if (timePatterns != null && timePatterns.length > 0) {
            result.register(LocalTime.class, new StringToLocalTimeConverter(timePatterns));
        }

        if (enumEnabled) {
            result = new EnumStringConverterRegistry(result);
        }

        if (primitiveEnabled) {
            result = new PrimitiveStringConverterRegistry(result);
        }

        if (arrayEnabled) {
            result = new ArrayStringConverterRegistry(result);
        }

        return result;
    }

    public StringConverterRegistryBuilder withDatePatterns(String... datePatterns) {
        this.datePatterns = datePatterns == null ? null : datePatterns.clone();

        return this;
    }

    public StringConverterRegistryBuilder withDateTimePatterns(String... dateTimePatterns) {
        this.dateTimePatterns = dateTimePatterns == null ? null : dateTimePatterns.clone();

        return this;
    }

    public StringConverterRegistryBuilder withTimePatterns(String... timePatterns) {
        this.timePatterns = timePatterns == null ? null : timePatterns.clone();

        return this;
    }

    public StringConverterRegistryBuilder withAll() {
        return withArray().withEnum()
                          .withPrimitive();
    }

    public StringConverterRegistryBuilder withoutAnything() {
        return withArray(false).withEnum(false)
                               .withPrimitive(false);
    }

    public StringConverterRegistryBuilder withEnum() {
        return withEnum(true);
    }

    public StringConverterRegistryBuilder withEnum(final boolean enabled) {
        enumEnabled = enabled;

        return this;
    }

    public StringConverterRegistryBuilder withPrimitive() {
        return withPrimitive(true);
    }

    public StringConverterRegistryBuilder withPrimitive(final boolean enabled) {
        primitiveEnabled = enabled;

        return this;
    }

    public StringConverterRegistryBuilder withArray() {
        return withArray(true);
    }

    public StringConverterRegistryBuilder withArray(final boolean enabled) {
        arrayEnabled = enabled;

        return this;
    }
}
