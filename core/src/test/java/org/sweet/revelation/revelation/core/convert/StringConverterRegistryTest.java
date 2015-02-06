package org.sweet.revelation.revelation.core.convert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringConverterRegistryTest {

    private StringConverterRegistry registry;

    @Before
    public void init() {
        registry = new StringConverterRegistryBuilder().build();
    }

    @Test
    public void shouldHaveDefaultConverter() {
        Assert.assertTrue(registry.size() > 0);

        for (Class<?> type : registry) {
            Assert.assertNotNull(registry.getConverter(type));
        }
    }

    @Test
    public void getConverter_onUnknownType_isNull() {
        Assert.assertNull(registry.getConverter(Object.class));
    }

    @Test
    public void getConverter_onKnownType_shouldBeValid() {
        Assert.assertNotNull(registry.getConverter(Integer.class));
    }

    @Test
    public void getConverter_onPrimitiveType_shouldBeValid() {
        Assert.assertNotNull(registry.getConverter(int.class));
    }

    @Test
    public void getConverter_onEnumType_shouldBeValid() {
        Assert.assertNotNull(registry.getConverter(Sample.class));
    }

    @Test
    public void getConverter_onArrayType_shouldBeValid() {
        Assert.assertNotNull(registry.getConverter(Integer[].class));
    }

    @Test
    public void getConverter_onPrimitiveArrayType_shouldBeValid() {
        Assert.assertNotNull(registry.getConverter(int[].class));
    }

    private static enum Sample {

        ONE, TWO
    }
}
