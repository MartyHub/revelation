package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringToLocalDateTimeConverterTest {

    private StringToLocalDateTimeConverter converter;

    @Before
    public void init() {
        converter = new StringToLocalDateTimeConverter("yyyy-MM-dd HH:mm:ss");
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
        Assert.assertEquals(LocalDateTime.parse("2015-01-27T16:35:00"), converter.convert("2015-01-27 16:35:00"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("abc");
    }
}
