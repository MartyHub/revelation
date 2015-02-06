package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringToBooleanConverterTest {

    private StringToBooleanConverter converter;

    @Before
    public void init() {
        converter = new StringToBooleanConverter();
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
        Assert.assertTrue(converter.convert("y"));
        Assert.assertTrue(converter.convert("yes"));
        Assert.assertTrue(converter.convert("true"));

        Assert.assertFalse(converter.convert("n"));
        Assert.assertFalse(converter.convert("no"));
        Assert.assertFalse(converter.convert("false"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("1");
    }
}
