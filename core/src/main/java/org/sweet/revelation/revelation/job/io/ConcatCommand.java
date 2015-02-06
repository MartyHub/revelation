package org.sweet.revelation.revelation.job.io;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.command.InputFilesCommand;
import org.sweet.revelation.revelation.core.command.OutputFileCommand;

public class ConcatCommand {

    public static enum HeaderAction {

        KEEP_ALL, KEEP_FIRST, REMOVE_ALL;
    }

    private InputFilesCommand inputFilesCommand;

    private OutputFileCommand outputFileCommand;

    @Description("Action on file's header")
    private HeaderAction headerAction = HeaderAction.KEEP_FIRST;

    @Description("Flag to control behaviour with processed input files")
    private boolean deleteInputFiles = false;

    public InputFilesCommand getInputFilesCommand() {
        return inputFilesCommand;
    }

    public void setInputFilesCommand(InputFilesCommand inputFilesCommand) {
        this.inputFilesCommand = inputFilesCommand;
    }

    public OutputFileCommand getOutputFileCommand() {
        return outputFileCommand;
    }

    public void setOutputFileCommand(OutputFileCommand outputFileCommand) {
        this.outputFileCommand = outputFileCommand;
    }

    public HeaderAction getHeaderAction() {
        return headerAction;
    }

    public void setHeaderAction(HeaderAction headerAction) {
        this.headerAction = headerAction;
    }

    public boolean isDeleteInputFiles() {
        return deleteInputFiles;
    }

    public void setDeleteInputFiles(boolean deleteInputFiles) {
        this.deleteInputFiles = deleteInputFiles;
    }
}
