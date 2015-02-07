package org.sweet.revelation.revelation.job.doc;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;
import org.sweet.revelation.revelation.core.command.MultiThreadCommand;
import org.sweet.revelation.revelation.core.command.OutputFileCommand;

public class DocumentCommand implements MultiThreadCommand.HasMultiThreadCommand, OutputFileCommand.HasOutputFileCommand {

    private MultiThreadCommand multiThreadCommand;

    private OutputFileCommand outputFileCommand;

    @Mandatory
    @Description("Documentation's title")
    private String title;

    @Mandatory
    @Description("Documentation's version")
    private String version;

    @Description("Documentation's build number")
    private String buildNumber;

    private String confluenceUrl;

    private String confluenceSpace;

    public MultiThreadCommand getMultiThreadCommand() {
        return multiThreadCommand;
    }

    public void setMultiThreadCommand(MultiThreadCommand multiThreadCommand) {
        this.multiThreadCommand = multiThreadCommand;
    }

    public OutputFileCommand getOutputFileCommand() {
        return outputFileCommand;
    }

    public void setOutputFileCommand(OutputFileCommand outputFileCommand) {
        this.outputFileCommand = outputFileCommand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getConfluenceUrl() {
        return confluenceUrl;
    }

    public void setConfluenceUrl(String confluenceUrl) {
        this.confluenceUrl = confluenceUrl;
    }

    public String getConfluenceSpace() {
        return confluenceSpace;
    }

    public void setConfluenceSpace(String confluenceSpace) {
        this.confluenceSpace = confluenceSpace;
    }
}
