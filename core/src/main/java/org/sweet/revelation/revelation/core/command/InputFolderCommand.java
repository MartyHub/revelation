package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Folder;
import org.sweet.revelation.revelation.core.Mandatory;

import java.io.File;

public class InputFolderCommand {

    public static interface HasOutputFolderCommand {

        InputFolderCommand getOutputFolderCommand();
    }

    @Mandatory
    @Description("Input folder")
    private Folder inputFolder;

    public InputFolderCommand() {
    }

    public InputFolderCommand(File defaultValue) {
        if (defaultValue != null) {
            this.inputFolder = new Folder(defaultValue);
        }
    }

    public InputFolderCommand(Folder defaultValue) {
        this.inputFolder = defaultValue;
    }

    public Folder getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(Folder inputFolder) {
        this.inputFolder = inputFolder;
    }
}
