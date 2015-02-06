package org.sweet.revelation.revelation.core.command;

import java.util.SortedSet;
import java.util.TreeSet;

public class ValidationResult {

    private final SortedSet<String> errors = new TreeSet<String>();

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isOk() {
        return errors.isEmpty();
    }

    public String[] getErrors() {
        return errors.toArray(new String[errors.size()]);
    }

    public int countErrors() {
        return errors.size();
    }

    @Override
    public String toString() {
        if (isOk()) {
            return "OK";
        } else {
            return errors.toString();
        }
    }
}
