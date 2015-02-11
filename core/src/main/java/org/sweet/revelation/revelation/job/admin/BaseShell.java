package org.sweet.revelation.revelation.job.admin;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import jline.console.history.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.event.CommandNotifier;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.main.ProcessorFinalizer;
import org.sweet.revelation.revelation.core.main.ProcessorRunner;
import org.sweet.revelation.revelation.core.main.RevelationMetadata;
import org.sweet.revelation.revelation.core.processor.Processor;
import org.sweet.revelation.revelation.core.processor.ProcessorException;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;
import org.sweet.revelation.revelation.core.text.UncapitalizeBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class BaseShell<C> implements Processor<C> {

    @Autowired
    private RevelationMetadata revelationMetadata;

    @Autowired
    private CommandNotifier commandNotifier;

    @Autowired
    private StringConverterRegistry stringConverterRegistry;

    @Autowired
    private Activity activity;

    private ConsoleReader consoleReader;

    private ProcessorRunner processorRunner;

    private ProcessorFinalizer processorFinalizer;

    public ProcessorReport process(C command) throws Exception {
        init();
        final int countCommand = repl();

        consoleReader.flush();

        return ProcessorReport.success(new PrettyIntegerFormatter().format(countCommand) + new ChoiceFormat("0# command|1# command|1< commands").format
                (countCommand));
    }

    protected void init() throws IOException {
        this.consoleReader = createConsoleReader();

        init(consoleReader);

        this.processorRunner = createProcessorRunner(revelationMetadata, commandNotifier, stringConverterRegistry);
        this.processorFinalizer = new ProcessorFinalizer(true, activity);
    }

    private int repl() throws IOException {
        int countCommand = 0;

        while (true) {
            final String line = consoleReader.readLine();

            if (line == null) {
                consoleReader.println();

                break; // ctrl + d
            }

            final List<String> tokens = splitLine(line);

            if (tokens.size() == 1 && ("quit".equalsIgnoreCase(tokens.get(0)) || "exit".equalsIgnoreCase(tokens.get(0)) || "bye".equalsIgnoreCase(tokens
                    .get(0)))) {
                break;
            }

            processorRunner.run(processorFinalizer, tokens.iterator());

            ++countCommand;
        }

        return countCommand;
    }

    protected ConsoleReader createConsoleReader() throws IOException {
        return new ConsoleReader(System.in, System.out);
    }

    protected void init(ConsoleReader consoleReader) throws IOException {
        consoleReader.addCompleter(new ShellCompleter(revelationMetadata, stringConverterRegistry));
        consoleReader.setHistory(createHistory());
        consoleReader.setPrompt("> ");
    }

    protected History createHistory() throws IOException {
        File historyDirectory = getHistoryDirectory();

        if (!historyDirectory.exists()) {
            if (!historyDirectory.mkdirs()) {
                throw new ProcessorException(String.format("Failed to create history directory <%s>", historyDirectory.getAbsolutePath()));
            }
        }

        FileHistory result = new FileHistory(new File(historyDirectory, getHistoryFilename()));

        Runtime.getRuntime()
               .addShutdownHook(new Thread(new FileHistoryFlusher(result), "revelation_history_flusher"));

        return result;
    }

    protected File getHistoryDirectory() throws IOException {
        return new File(System.getProperty("user.home"), ".revelation");
    }

    protected String getHistoryFilename() throws IOException {
        return UncapitalizeBuilder.build(getClass().getSimpleName());
    }

    protected ProcessorRunner createProcessorRunner(RevelationMetadata revelationMetadata, CommandNotifier commandNotifier,
                                                    StringConverterRegistry stringConverterRegistry) {
        return new ProcessorRunner(revelationMetadata, commandNotifier, stringConverterRegistry, "Usage : COMMAND [PARAMETER]");
    }

    protected List<String> splitLine(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        List<String> result = new ArrayList<String>(st.countTokens());

        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }

        return result;
    }
}
