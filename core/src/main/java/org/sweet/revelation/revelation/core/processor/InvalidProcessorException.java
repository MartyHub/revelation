package org.sweet.revelation.revelation.core.processor;

public class InvalidProcessorException extends ProcessorException {

    public InvalidProcessorException(String message) {
        super(message);
    }

    public InvalidProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
