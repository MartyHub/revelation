package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;

import java.text.ChoiceFormat;

public class ValidationException extends RuntimeException {

    private final ValidationResult result;

    public ValidationException(ValidationResult result) {
        super(new PrettyIntegerFormatter().format(result.countErrors()) + new ChoiceFormat("1# validation error|1< validation errors").format(result
                .countErrors()));

        this.result = result;
    }

    public ValidationResult getResult() {
        return result;
    }
}
