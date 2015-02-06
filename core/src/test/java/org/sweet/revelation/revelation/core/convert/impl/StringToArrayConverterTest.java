package org.sweet.revelation.revelation.core.convert.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringToArrayConverterTest {

    private StringToArrayConverter<Integer> converter;

    @Before
    public void init() {
        converter = new StringToArrayConverter<Integer>(new StringToIntegerConverter(), Integer.class);
    }

    @Test
    public void usage_shouldNotBeNull() {
        Assert.assertNotNull(converter.getUsage());
    }

    @Test
    public void null_whenConverted_shouldBeNull() {
        Assert.assertNull(converter.convert(null));
    }

    @Test
    public void emptyString_whenConverted_shouldBeNull() {
        Assert.assertNull(converter.convert(""));
    }

    @Test
    public void validElement_whenConverted_shouldBeAnArray() {
        Assert.assertArrayEquals(new Integer[]{1}, converter.convert("1"));
    }

    @Test
    public void validElements_whenConverted_shouldBeAnArray() {
        Assert.assertArrayEquals(new Integer[]{1, 2}, converter.convert("1,2"));
    }
}
