package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;

public class BatchCommand implements ValidatableCommand {

    public interface HasBatchCommand {

        BatchCommand getBatchCommand();
    }

    @Description("Batch size")
    private int batchSize;

    public BatchCommand(final int defaultValue) {
        this.batchSize = defaultValue;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
    }

    public void validate(ValidationResult result) {
        if (batchSize < 1) {
            result.addError("-batchSize must be > 0");
        }
    }
}
