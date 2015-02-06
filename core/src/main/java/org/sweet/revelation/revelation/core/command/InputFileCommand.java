package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;

import java.io.File;

public class InputFileCommand {

    public interface HasInputFileCommand {

        InputFileCommand getInputFileCommand();
    }

    @Mandatory
    @Description("Input file")
    private File inputFile;

    public InputFileCommand() {
    }

    public InputFileCommand(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }
}
