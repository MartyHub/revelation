package org.sweet.revelation.revelation.core.convert;

public class ConvertException extends RuntimeException {

    public static ConvertException fromSource(String source) {
        return fromSource(source, null);
    }

    public static ConvertException fromSource(String source, Throwable cause) {
        return new ConvertException(String.format("Failed to convert <%s>", source), cause);
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
