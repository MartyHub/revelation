package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;

public class MultiThreadCommand implements ValidatableCommand {

    public interface HasMultiThreadCommand {

        MultiThreadCommand getMultiThreadCommand();
    }

    @Description("Number of threads")
    private int nbThreads;

    public MultiThreadCommand() {
        this(Runtime.getRuntime()
                    .availableProcessors());
    }

    public MultiThreadCommand(final int defaultValue) {
        this.nbThreads = defaultValue;
    }

    public int getNbThreads() {
        return nbThreads;
    }

    public void setNbThreads(final int nbThreads) {
        this.nbThreads = nbThreads;
    }

    public void validate(ValidationResult result) {
        if (nbThreads < 1) {
            result.addError("-nbThreads must be > 0");
        }
    }
}
