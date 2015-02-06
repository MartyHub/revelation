package org.sweet.revelation.revelation.core.convert;

public class ConverterNotFoundException extends RuntimeException {

    public ConverterNotFoundException(Class<?> type) {
        super(String.format("Don't know how to handle <%s>", type));
    }
}
