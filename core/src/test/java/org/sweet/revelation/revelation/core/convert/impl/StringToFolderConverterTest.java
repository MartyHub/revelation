package org.sweet.revelation.revelation.core.convert.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sweet.revelation.revelation.core.Folder;

public class StringToFolderConverterTest {

    private StringToFolderConverter converter;

    @Before
    public void init() {
        converter = new StringToFolderConverter();
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
        Assert.assertEquals(new Folder("MyFolder"), converter.convert("MyFolder"));
    }
}
