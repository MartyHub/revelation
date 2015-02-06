package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringToDateTimeZoneConverterTest {

    private StringToDateTimeZoneConverter converter;

    @Before
    public void init() {
        converter = new StringToDateTimeZoneConverter();
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
        Assert.assertEquals(DateTimeZone.forID("Europe/Paris"), converter.convert("Europe/Paris"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("abc");
    }
}
