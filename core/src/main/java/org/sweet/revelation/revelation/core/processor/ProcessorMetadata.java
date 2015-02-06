package org.sweet.revelation.revelation.core.processor;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.ProcessorComponent;
import org.sweet.revelation.revelation.core.command.CommandMetadata;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.text.UncapitalizeBuilder;

public class ProcessorMetadata<P extends Processor> {

    private final P processor;

    private final Class<?> commandType;

    private final String name;

    private final String description;

    public ProcessorMetadata(String name, P processor) {
        this.name = name;
        this.processor = check(processor);

        Class<? extends Processor> processorType = processor.getClass();

        this.commandType = getCommandType(processorType);
        this.description = getDescription(processorType);
    }

    public String getName() {
        return name;
    }

    public P getProcessor() {
        return processor;
    }

    public Class<? extends Processor> getProcessorType() {
        return processor.getClass();
    }

    public String getDescription() {
        return description;
    }

    public CommandMetadata createCommandMetadata(StringConverterRegistry registry) {
        return new CommandMetadata(registry, commandType);
    }

    public String getUsage(StringConverterRegistry registry) {
        StringBuilder sb = new StringBuilder();

        sb.append(name);

        if (description != null) {
            sb.append(" : ");
            sb.append(UncapitalizeBuilder.build(description));
        }

        String usage = createCommandMetadata(registry).getUsage();

        if (!"".equals(usage)) {
            sb.append(System.getProperty("line.separator"));
            sb.append(usage);
        }

        return sb.toString();
    }

    public String toString() {
        return String.format("%s (%s)", name, processor.getClass()
                                                       .getName());
    }

    private P check(P processor) {
        if (processor == null) {
            throw new IllegalArgumentException("Processor is mandatory");
        }

        if (processor.getClass()
                     .getAnnotation(ProcessorComponent.class) == null) {
            throw new IllegalArgumentException(String.format("Failed to find Processor annotation on <%s>", processor));
        }

        return processor;
    }

    private Class<?> getCommandType(Class<? extends Processor> processorType) {
        return processorType.getAnnotation(ProcessorComponent.class)
                            .value();
    }

    private String getDescription(Class<?> processorType) {
        Description description = processorType.getAnnotation(Description.class);

        if (description == null) {
            return null;
        } else {
            return description.value();
        }
    }
}
