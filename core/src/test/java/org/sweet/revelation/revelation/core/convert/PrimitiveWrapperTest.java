package org.sweet.revelation.revelation.core.convert;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class PrimitiveWrapperTest {

    @Test(expected = IllegalArgumentException.class)
    public void null_whenWrap_shouldThrowIllegalArgumentException() {
        new PrimitiveWrapper(null);
    }

    @Test
    public void nonPrimitive_whenWrap_shouldNotBeWrapped() {
        Assert.assertEquals(Object.class, new PrimitiveWrapper(Object.class).wrap());
    }

    @Test
    public void primitive_whenWrap_shouldBeWrapped() {
        Assert.assertEquals(Boolean.class, new PrimitiveWrapper(boolean.class).wrap());
        Assert.assertEquals(Byte.class, new PrimitiveWrapper(byte.class).wrap());
        Assert.assertEquals(Character.class, new PrimitiveWrapper(char.class).wrap());
        Assert.assertEquals(Double.class, new PrimitiveWrapper(double.class).wrap());
        Assert.assertEquals(Float.class, new PrimitiveWrapper(float.class).wrap());
        Assert.assertEquals(Integer.class, new PrimitiveWrapper(int.class).wrap());
        Assert.assertEquals(Long.class, new PrimitiveWrapper(long.class).wrap());
        Assert.assertEquals(Short.class, new PrimitiveWrapper(short.class).wrap());
    }
}
