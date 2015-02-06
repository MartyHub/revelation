package org.sweet.revelation.revelation.core.command;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(Throwable cause) {
        super(cause);
    }

    public InvalidCommandException(String message) {
        super(message);
    }
}
