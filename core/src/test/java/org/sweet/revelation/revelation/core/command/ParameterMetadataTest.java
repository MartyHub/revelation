package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;
import org.junit.Assert;
import org.junit.Test;

public class ParameterMetadataTest {

    @Test
    public void bean_setProperty_shouldBeValid() throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        Bean bean = new Bean();

        new ParameterMetadata(Bean.class.getDeclaredField("property")).setValue(bean, "value");

        Assert.assertEquals("value", bean.getProperty());
    }

    @Test
    public void bean_setNestedProperty_shouldBeValid() throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        NestedBean bean = new NestedBean();

        new ParameterMetadata(Bean.class.getDeclaredField("property")).withPrefix("bean")
                                                                      .init(bean)
                                                                      .setValue(bean, "value");

        Assert.assertNotNull(bean.getBean());
        Assert.assertEquals("value", bean.getBean()
                                         .getProperty());
    }

    @Test
    public void bean_withNestedValidatableProperties_shouldBeValidated() throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        NestedBean bean = new NestedBean();
        ValidationResult result = new ValidationResult();

        new ParameterMetadata(Bean.class.getDeclaredField("property")).withPrefix("bean")
                                                                      .init(bean)
                                                                      .validate(bean, result);

        Assert.assertFalse(result.isOk());
    }

    @Test
    public void property_withMandatoryAnnotation_shouldBeMandatory() throws NoSuchFieldException {
        Assert.assertTrue(new ParameterMetadata(Bean.class.getDeclaredField("property")).isMandatory());
    }

    @Test
    public void nullProperty_withMandatoryAnnotation_shouldNotBeValid() throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        ValidationResult result = new ValidationResult();

        new ParameterMetadata(Bean.class.getDeclaredField("property")).validate(new Bean(), result);

        Assert.assertFalse(result.isOk());
    }

    @Test
    public void property_withDocAnnotation_shouldBeMandatory() throws NoSuchFieldException {
        Assert.assertEquals("documentation", new ParameterMetadata(Bean.class.getDeclaredField("property")).getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bean_withNullPrefix_shouldThrowException() throws NoSuchFieldException {
        new ParameterMetadata(Bean.class.getDeclaredField("property")).withPrefix(null);
    }

    public static class Bean implements ValidatableCommand {

        @Mandatory
        @Description("documentation")
        private String property;

        public String getProperty() {
            return property;
        }

        public void validate(ValidationResult result) {
            result.addError("error");
        }
    }

    public static class NestedBean {

        private Bean bean;

        public Bean getBean() {
            return bean;
        }
    }
}
