package org.sweet.revelation.revelation.core.main;

import org.sweet.revelation.revelation.core.command.CommandLineParameterBuilder;
import org.sweet.revelation.revelation.core.command.InvalidParameterException;
import org.sweet.revelation.revelation.core.command.Parameter;
import org.sweet.revelation.revelation.core.command.ValidationException;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.event.CommandNotifier;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.processor.InvalidProcessorException;
import org.sweet.revelation.revelation.core.processor.ProcessorMetadata;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.sweet.revelation.revelation.core.text.MaxLengthBuilder;
import org.sweet.revelation.revelation.core.text.UncapitalizeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Lazy
public class ProcessorRunner {

    @Autowired
    private RevelationMetadata revelationMetadata;

    @Autowired
    private CommandNotifier commandNotifier;

    @Autowired
    private StringConverterRegistry registry;

    @Autowired
    private Activity activity;

    public void run(ProcessorFinalizer processorFinalizer, Iterator<String> args) {
        if (!args.hasNext()) {
            processorFinalizer.endAndExit(ProcessorReport.failure("command name is mandatory"), getUsage(), listAvailableCommands());
        } else {
            try {
                ProcessorMetadata processorMetadata = revelationMetadata.getProcessorMetadata(args.next());

                doRun(processorFinalizer, args, processorMetadata);
            } catch (InvalidProcessorException e) {
                processorFinalizer.endAndExit(ProcessorReport.failure(e.getMessage()), getUsage(), listAvailableCommands());
            } catch (Exception e) {
                processorFinalizer.endAndExit(ProcessorReport.failure(e.getMessage(), e));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void doRun(ProcessorFinalizer processorFinalizer, Iterator<String> args, ProcessorMetadata processorMetadata) {
        try {
            Parameter[] parameters = parseParameters(args);
            Object command = processorMetadata.createCommandMetadata(registry)
                                              .create(parameters);

            commandNotifier.commandStart(processorMetadata.getName(), parameters);

            ProcessorReport report = null;

            try {
                report = processorMetadata.getProcessor()
                                          .process(command);
            } catch (Exception e) {
                report = ProcessorReport.failure(e.getMessage(), e);
            } finally {
                if (report == null) {
                    report = ProcessorReport.failure("no report");
                }

                synchronized (commandNotifier) {
                    commandNotifier.commandEnd(processorMetadata.getName(), report);

                    processorFinalizer.end(report);

                    commandNotifier.notify();
                }

                processorFinalizer.exit(report.getStatus()
                                              .getExitCode());
            }
        } catch (InvalidParameterException e) {
            processorFinalizer.endAndExit(ProcessorReport.failure(e.getMessage()), processorMetadata.getUsage(registry));
        } catch (ValidationException e) {
            StringBuilder sb = new StringBuilder();
            String lineSeparator = System.getProperty("line.separator");

            sb.append(e.getMessage());

            for (String s : e.getResult()
                             .getErrors()) {
                sb.append(lineSeparator);
                sb.append(" - ");
                sb.append(s);
            }

            processorFinalizer.endAndExit(ProcessorReport.failure(sb.toString()), processorMetadata.getUsage(registry));
        } catch (Exception e) {
            processorFinalizer.endAndExit(ProcessorReport.failure(e.getMessage(), e));
        }
    }

    private Parameter[] parseParameters(Iterator<String> args) {
        CommandLineParameterBuilder builder = new CommandLineParameterBuilder();

        while (args.hasNext()) {
            builder.addArg(args.next());
        }

        return builder.build();
    }

    private String getUsage() {
        return "Usage : run FILE COMMAND [PARAMETER]";
    }

    private String listAvailableCommands() {
        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        sb.append("List of available commands :");

        MaxLengthBuilder maxLengthBuilder = MaxLengthBuilder.builder();
        Map<String, String> commands = new LinkedHashMap<String, String>();

        for (ProcessorMetadata processorMetadata : revelationMetadata.iterate()) {
            commands.put(processorMetadata.getName(), processorMetadata.getDescription());
            maxLengthBuilder.add(processorMetadata.getName());
        }

        if (maxLengthBuilder.getMaxLength() == 0) {
            sb.append(" no command found");
        } else {
            for (Map.Entry<String, String> command : commands.entrySet()) {
                sb.append(lineSeparator);
                sb.append(" - ");
                sb.append(maxLengthBuilder.build(command.getKey()));

                if (command.getValue() != null) {
                    sb.append(' ');
                    sb.append(UncapitalizeBuilder.build(command.getValue()));
                }
            }
        }

        return sb.toString();
    }
}
