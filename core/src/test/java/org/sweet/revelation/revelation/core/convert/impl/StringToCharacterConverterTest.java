package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringToCharacterConverterTest {

    private StringToCharacterConverter converter;

    @Before
    public void init() {
        converter = new StringToCharacterConverter();
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
        Assert.assertEquals(Character.valueOf('a'), converter.convert("a"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("abc");
    }
}
