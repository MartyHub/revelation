package org.sweet.revelation.revelation.job.io;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.ProcessorComponent;
import org.sweet.revelation.revelation.core.concurrent.Task;
import org.sweet.revelation.revelation.core.concurrent.TaskResultProcessor;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.ProcessorException;
import org.sweet.revelation.revelation.core.processor.ProcessorMessageBuilder;
import org.sweet.revelation.revelation.core.processor.ProcessorStatus;
import org.sweet.revelation.revelation.core.processor.impl.SequentialProcessor;
import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;

import java.io.*;
import java.util.Collection;

@ProcessorComponent(ConcatCommand.class)
@Description("Concatenate files")
public class Concat extends SequentialProcessor<ConcatCommand, File, Void> {

    @Override
    protected Collection<File> preProcess(Activity activity, ConcatCommand command) throws Exception {
        File outputFile = command.getOutputFileCommand()
                                 .getOutputFile();

        if (outputFile.exists() && !outputFile.delete()) {
            throw new ProcessorException(String.format("Failed to delete file <%s>", outputFile.getAbsolutePath()));
        }

        return super.preProcess(activity, command);
    }

    @Override
    protected Collection<File> createTaskParams(Work work, ConcatCommand command) throws Exception {
        return command.getInputFilesCommand()
                      .getFiles(work);
    }

    @Override
    protected Task<File, Void> newTask(final ConcatCommand command, final int index) throws Exception {
        return new Task<File, Void>() {

            public Void run(Work work, File param) throws Exception {
                File outputFile = command.getOutputFileCommand()
                                         .getOutputFile();
                BufferedReader reader = null;
                BufferedWriter writer = null;

                try {
                    reader = new BufferedReader(new FileReader(param));
                    writer = new BufferedWriter(new FileWriter(outputFile, true));

                    String line = null;

                    // Skip first line
                    switch (command.getHeaderAction()) {
                        case KEEP_ALL:
                            break;
                        case KEEP_FIRST:
                            if (index > 0) {
                                line = reader.readLine();
                            }

                            break;
                        case REMOVE_ALL:
                            line = reader.readLine();

                            break;
                    }

                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }

                    writer.flush();
                } finally {
                    closeQuietly(reader);
                    closeQuietly(writer);
                }

                deleteFileIfNeeded(command, param, work);

                return null;
            }
        };
    }

    @Override
    protected TaskResultProcessor<ConcatCommand, Void> createTaskResultProcessor(ConcatCommand command, final int taskCount) throws Exception {
        return new TaskResultProcessor<ConcatCommand, Void>(command, taskCount) {

            @Override
            protected ProcessorMessageBuilder newProcessorMessageBuilder() {
                return new ProcessorMessageBuilder(taskCount) {

                    @Override
                    public String build(ProcessorStatus status) {
                        if (status == ProcessorStatus.SUCCESS) {
                            return new PrettyIntegerFormatter().format(taskCount) + " file(s) concatenated to " + command.getOutputFileCommand()
                                                                                                                         .getOutputFile()
                                                                                                                         .getAbsolutePath();
                        } else {
                            return super.build(status);
                        }
                    }
                };
            }
        };
    }

    private void deleteFileIfNeeded(ConcatCommand command, File file, Work work) {
        if (command.isDeleteInputFiles()) {
            try {
                file.delete();
            } catch (Exception e) {
                work.error(String.format("Failed to delete <%s>", file.getAbsolutePath()), e);
            }
        }
    }

    private void closeQuietly(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private void closeQuietly(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
}
