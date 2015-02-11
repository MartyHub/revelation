package org.sweet.revelation.revelation.job.admin;

import jline.console.completer.Completer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweet.revelation.revelation.core.command.CommandLineParameterBuilder;
import org.sweet.revelation.revelation.core.command.CommandLineParametersBuilder;
import org.sweet.revelation.revelation.core.command.Parameter;
import org.sweet.revelation.revelation.core.command.ParameterMetadata;
import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.main.RevelationMetadata;
import org.sweet.revelation.revelation.core.processor.ProcessorMetadata;

import java.util.*;

public class ShellCompleter implements Completer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellCompleter.class);

    private final RevelationMetadata revelationMetadata;

    private final StringConverterRegistry stringConverterRegistry;

    public ShellCompleter(RevelationMetadata revelationMetadata, StringConverterRegistry stringConverterRegistry) {
        this.revelationMetadata = revelationMetadata;
        this.stringConverterRegistry = stringConverterRegistry;
    }

    public int complete(String buffer, final int cursor, List<CharSequence> candidates) {
        SortedSet<String> completions = new TreeSet<String>();
        int result = cursor;

        try {
            List<String> tokens = tokenize(buffer, cursor);

            switch (tokens.size()) {
                case 0:
                    result = completeCommands(cursor, completions, "");

                    break;
                case 1:
                    if (lastCharIsSpace(buffer, cursor)) {
                        result = completeParameters(cursor, completions, tokens, "");
                    } else {
                        result = completeCommands(cursor, completions, tokens.get(0));
                    }

                    break;
                default:
                    if (lastCharIsSpace(buffer, cursor)) {
                        result = completeParameters(cursor, completions, tokens, "");
                    } else {
                        result = completeParameter(cursor, completions, tokens);
                    }

                    break;
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("Failed to complete <%s>", buffer), e);
        }

        candidates.addAll(completions);

        return result;
    }

    private List<String> tokenize(String buffer, final int cursor) {
        List<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cursor; ++i) {
            final char c = buffer.charAt(i);

            if (c == ' ') {
                if (sb.length() > 0) {
                    result.add(sb.toString());

                    sb = new StringBuilder();
                }
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            result.add(sb.toString());
        }

        return result;
    }

    private int completeCommands(final int cursor, SortedSet<String> completions, String prefix) {
        addCommands(completions, prefix);
        addSpaceIfSingleton(completions);

        return completions.isEmpty() ? cursor : cursor - prefix.length();
    }

    private int completeParameters(final int cursor, SortedSet<String> completions, List<String> tokens, String prefix) {
        ProcessorMetadata processorMetadata = revelationMetadata.getProcessorMetadata(tokens.get(0));
        Set<String> parameters = extractParameters(tokens);

        for (ParameterMetadata parameterMetadata : processorMetadata.createCommandMetadata(stringConverterRegistry)) {
            if (!parameters.contains(parameterMetadata.getName()) && prefix.regionMatches(true, 0, parameterMetadata.getName(), 0, prefix.length())) {
                completions.add(CommandLineParameterBuilder.builder(true)
                                                           .arg(parameterMetadata.getName(), "")
                                                           .toString());
            }
        }

        return completions.isEmpty() ? cursor : cursor - prefix.length();
    }

    private int completeParameter(final int cursor, SortedSet<String> completions, List<String> tokens) {
        String arg = tokens.get(tokens.size() - 1);

        if (arg == null || arg.length() == 0) {
            return completeParameters(cursor, completions, tokens, "");
        }

        if (!arg.startsWith("-")) {
            return cursor;
        }

        final int index = arg.indexOf("=");

        switch (index) {
            case -1:
                return completeParameters(cursor, completions, tokens, arg.substring(1)) - 1;
            case 0:
            case 1:
                return cursor;
            default:
                String name = arg.substring(1, index);
                String value = index < arg.length() ? arg.substring(index + 1) : "";

                return completeParameterValue(cursor, completions, tokens.get(0), name, value);
        }
    }

    private int completeParameterValue(final int cursor, SortedSet<String> completions, String commandName, String parameterName, String parameterValue) {
        Class<?> type = revelationMetadata.getProcessorMetadata(commandName)
                                          .createCommandMetadata(stringConverterRegistry)
                                          .getParameterMetadata(parameterName)
                                          .getType();
        SortedSet<String> excludedParameters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        String prefix = excludeParameter(excludedParameters, type, parameterValue);

        for (String s : stringConverterRegistry.getConverter(type)
                                               .complete(prefix)) {
            if (!excludedParameters.contains(s)) {
                completions.add(s);
            }
        }

        return completions.isEmpty() ? cursor : cursor - prefix.length();
    }

    private String excludeParameter(SortedSet<String> excludedParameters, Class<?> parameterType, String parameterValue) {
        if (parameterType.isArray()) {
            StringConverter<?> converter = stringConverterRegistry.getConverter(parameterType);

            if (converter.getValueSeparator() != null) {
                for (StringTokenizer st = new StringTokenizer(parameterValue, converter.getValueSeparator()); st.hasMoreTokens(); ) {
                    final String token = st.nextToken();

                    if (st.hasMoreTokens()) {
                        excludedParameters.add(token);
                    } else {
                        return token;
                    }
                }
            }
        }

        return parameterValue;
    }

    private void addCommands(SortedSet<String> completions, String prefix) {
        for (ProcessorMetadata processorMetadata : revelationMetadata.iterate()) {
            addCompletion(completions, processorMetadata.getName(), prefix);
        }
    }

    private void addCompletion(SortedSet<String> completions, String completion, String prefix) {
        if (prefix.regionMatches(true, 0, completion, 0, prefix.length())) {
            completions.add(completion);
        }
    }

    private void addSpaceIfSingleton(SortedSet<String> completions) {
        if (completions.size() == 1) {
            Iterator<String> iterator = completions.iterator();
            String newValue = iterator.next() + " ";

            iterator.remove();

            completions.add(newValue);
        }
    }

    private boolean lastCharIsSpace(String buffer, final int cursor) {
        return buffer.charAt(cursor - 1) == ' ';
    }

    private Set<String> extractParameters(List<String> tokens) {
        CommandLineParametersBuilder commandLineParametersBuilder = new CommandLineParametersBuilder(false);

        // index 0 is command name
        for (int i = 1, size = tokens.size(); i < size; ++i) {
            commandLineParametersBuilder.addArg(tokens.get(i));
        }

        Set<String> result = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

        for (Parameter parameter : commandLineParametersBuilder.build()) {
            if (parameter.getName() != null) {
                result.add(parameter.getName());
            }
        }

        return result;
    }
}
