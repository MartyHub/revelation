package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Folder;
import org.sweet.revelation.revelation.core.log.Work;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class InputFilesCommand implements ValidatableCommand {

    public interface HasInputFilesCommand {

        InputFilesCommand getInputFilesCommand();
    }

    @Description("List of input files")
    private File[] inputFiles;

    @Description("Input folder to scan")
    private Folder inputFolder;

    @Description("Regex to filter files by name")
    private String inputFilePattern = ".*";

    @Description("Flag to control behaviour with folders")
    private boolean recursive = true;

    public InputFilesCommand() {
    }

    public InputFilesCommand(File[] inputFiles) {
        this.inputFiles = inputFiles;
    }

    public InputFilesCommand(Folder inputFolder, String inputFilePattern, final boolean recursive) {
        this.inputFolder = inputFolder;
        this.inputFilePattern = inputFilePattern;
        this.recursive = recursive;
    }

    public String getInputFilePattern() {
        return inputFilePattern;
    }

    public void setInputFilePattern(String inputFilePattern) {
        this.inputFilePattern = inputFilePattern;
    }

    public File[] getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(File[] inputFiles) {
        this.inputFiles = inputFiles;
    }

    public Folder getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(Folder inputFolder) {
        this.inputFolder = inputFolder;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(final boolean recursive) {
        this.recursive = recursive;
    }

    public void validate(ValidationResult result) {
        if (inputFolder == null) {
            if (inputFiles == null) {
                result.addError("You must provide -inputFiles or -inputFolder");
            }
        } else if (inputFiles != null) {
            result.addError("Only one of -inputFiles or -inputFolder must be provided");
        } else if (inputFilePattern == null) {
            result.addError("-inputFilePattern is mandatory with -inputFolder");
        }
    }

    public Collection<File> getFiles(Work work) {
        if (inputFiles != null) {
            return Arrays.asList(inputFiles);
        } else {
            work.info(String.format("scan %s%s for %s files", recursive ? "recursively " : "", inputFolder, inputFilePattern));

            return scanFolder();
        }
    }

    private Collection<File> scanFolder() {
        return addFiles(new ArrayList<File>(), inputFolder.asFile(), new FileFilter() {

            private final Pattern filePattern = Pattern.compile(inputFilePattern, Pattern.CASE_INSENSITIVE);

            public boolean accept(File pathname) {
                if (recursive && pathname.isDirectory()) {
                    return true;
                } else {
                    return filePattern.matcher(pathname.getName())
                                      .matches();
                }
            }
        });
    }

    private Collection<File> addFiles(Collection<File> result, File directory, FileFilter filter) {
        File[] found = directory.listFiles(filter);

        if (found != null) {
            for (File file : found) {
                if (file.isDirectory()) {
                    addFiles(result, file, filter);
                } else {
                    result.add(file);
                }
            }
        }

        return result;
    }
}
