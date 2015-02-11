package org.sweet.revelation.revelation.core.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistryBuilder;

public class CommandMetadataTest {

    private StringConverterRegistry registry;

    @Before
    public void init() {
        this.registry = new StringConverterRegistryBuilder().build();
    }

    @Test
    public void validParameters_shouldCreateValidBean() throws InstantiationException, IllegalAccessException {
        Object bean = new CommandMetadata(registry, Bean.class).create(new CommandLineParametersBuilder(true).addArg("property", "value")
                                                                                                             .build());

        Assert.assertNotNull(bean);
        Assert.assertTrue(bean instanceof Bean);
        Assert.assertEquals("value", ((Bean) bean).getProperty());
    }

    @Test(expected = ValidationException.class)
    public void bean_shouldBeValidated() throws InstantiationException, IllegalAccessException {
        new CommandMetadata(registry, NestedBean.class).create(new CommandLineParametersBuilder(true).addArg("property", "value")
                                                                                                     .build());
    }

    @Test
    public void command_usage_shouldBeValid() throws IllegalAccessException, InstantiationException {
        Assert.assertNotNull(new CommandMetadata(registry, Bean.class).getUsage());
    }

    public static class Bean {

        @Mandatory
        @Description("documentation")
        private String property;

        public String getProperty() {
            return property;
        }
    }

    public static class NestedBean implements ValidatableCommand {

        private Bean bean;

        public Bean getBean() {
            return bean;
        }

        public void validate(ValidationResult result) {
            result.addError("nested error");
        }
    }
}
