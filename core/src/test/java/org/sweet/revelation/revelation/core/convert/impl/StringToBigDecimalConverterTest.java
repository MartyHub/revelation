package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class StringToBigDecimalConverterTest {

    private StringToBigDecimalConverter converter;

    @Before
    public void init() {
        converter = new StringToBigDecimalConverter();
    }

    @Test
    public void usage_shouldNotBeNull() {
        Assert.assertNotNull(converter.getUsage());
    }

    @Test
    public void nullString_whenConverted_shouldBeNull() {
        Assert.assertNull(converter.convert(null));
    }

    @Test
    public void emptyString_whenConverted_shouldBeNull() {
        Assert.assertNull(converter.convert(""));
    }

    @Test
    public void validString_whenConverted_shouldBeValid() {
        Assert.assertEquals(new BigDecimal("1.2"), converter.convert("1.2"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("1.2.3");
    }
}
