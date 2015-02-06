package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;

import java.io.File;

public class OutputFolderCommand {

    public static interface HasOutputFolderCommand {

        OutputFolderCommand getOutputFolderCommand();
    }

    @Mandatory
    @Description("Output folder")
    private File outputFolder;

    public OutputFolderCommand() {
    }

    public OutputFolderCommand(File defaultValue) {
        this.outputFolder = defaultValue;
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }
}
