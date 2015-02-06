package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;

import java.io.File;

public class OutputFileCommand {

    public static interface HasOutputFileCommand {

        OutputFileCommand getOutputFileCommand();
    }

    @Mandatory
    @Description("Output file")
    private File outputFile;

    public OutputFileCommand() {
    }

    public OutputFileCommand(File defaultValue) {
        this.outputFile = defaultValue;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }
}
