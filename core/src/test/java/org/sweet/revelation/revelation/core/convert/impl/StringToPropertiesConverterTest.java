package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class StringToPropertiesConverterTest {

    private StringToPropertiesConverter converter;

    @Before
    public void init() {
        converter = new StringToPropertiesConverter();
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
    public void classpathResource_whenConverted_shouldBeValid() {
        Properties properties = converter.convert("sample.properties");

        Assert.assertNotNull(properties);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.getProperty("key"));
    }

    @Test
    public void file_whenConverted_shouldBeValid() throws URISyntaxException {
        URL url = getClass().getClassLoader()
                            .getResource("sample.properties");

        Assert.assertNotNull(url);

        Properties properties = converter.convert(new File(url.toURI()).getAbsolutePath());

        Assert.assertNotNull(properties);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.getProperty("key"));
    }

    @Test(expected = ConvertException.class)
    public void invalidString_whenConverted_shouldThrowConvertException() {
        converter.convert("abc");
    }
}
