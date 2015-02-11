package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Folder;
import org.sweet.revelation.revelation.core.Mandatory;

import java.io.File;

public class OutputFolderCommand {

    public static interface HasOutputFolderCommand {

        OutputFolderCommand getOutputFolderCommand();
    }

    @Mandatory
    @Description("Output folder")
    private Folder outputFolder;

    public OutputFolderCommand() {
    }

    public OutputFolderCommand(File defaultValue) {
        if (defaultValue != null) {
            this.outputFolder = new Folder(defaultValue);
        }
    }

    public OutputFolderCommand(Folder defaultValue) {
        this.outputFolder = defaultValue;
    }

    public Folder getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(Folder outputFolder) {
        this.outputFolder = outputFolder;
    }
}
