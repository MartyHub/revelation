package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.text.MaxLengthBuilder;
import org.sweet.revelation.revelation.core.text.UncapitalizeBuilder;

import java.lang.reflect.Field;
import java.util.*;

public class CommandMetadata implements Iterable<ParameterMetadata> {

    private final Map<String, ParameterMetadata> properties;

    private final StringConverterRegistry registry;

    private final Class<?> commandType;

    public CommandMetadata(StringConverterRegistry registry, Class<?> commandType) {
        if (registry == null) {
            throw new IllegalArgumentException("String Converter Registry is mandatory");
        }

        this.registry = registry;

        if (commandType == null) {
            throw new IllegalArgumentException("Command Type is mandatory");
        }

        this.commandType = commandType;

        Collection<Field> fields = getFields(commandType);

        this.properties = new TreeMap<String, ParameterMetadata>(String.CASE_INSENSITIVE_ORDER);

        for (Field field : fields) {
            final ParameterMetadata parameterMetadata = new ParameterMetadata(field);
            final Class<?> type = parameterMetadata.getType();

            if (hasConverter(type)) {
                if (!addParameterMetadata(parameterMetadata)) {
                    throw new IllegalArgumentException(String.format("Parameter mismatch <%s> in <%s>", parameterMetadata.getName(), commandType));
                }
            } else {
                for (ParameterMetadata nestedParameterMetadata : new CommandMetadata(registry, type)) {
                    final ParameterMetadata fullParameterMetadata = nestedParameterMetadata.withPrefix(parameterMetadata.getName());

                    if (!addParameterMetadata(fullParameterMetadata)) {
                        throw new IllegalArgumentException(String.format("Parameter mismatch <%s> between <%s> and <%s>",
                                fullParameterMetadata.getName(), commandType, type));
                    }
                }
            }
        }
    }

    public Iterator<ParameterMetadata> iterator() {
        return new TreeSet<ParameterMetadata>(properties.values()).iterator();
    }

    public Object create() {
        Object result = newCommandInstance();

        init(result);

        return result;
    }

    public Object create(Parameter[] parameters) {
        Object result = create();

        fill(result, parameters);
        validate(result);

        return result;
    }

    public String getUsage() {
        StringBuilder sb = new StringBuilder();
        Map<String, ParameterMetadata> parameterUsages = new LinkedHashMap<String, ParameterMetadata>(properties.size());

        for (ParameterMetadata parameterMetadata : this) {
            final StringConverter<?> converter = registry.getConverter(parameterMetadata.getType());

            parameterUsages.put(parameterMetadata.getUsage(converter.getUsage()), parameterMetadata);
        }

        MaxLengthBuilder maxLengthBuilder = MaxLengthBuilder.builder()
                                                            .add(parameterUsages.keySet());
        String lineSeparator = System.getProperty("line.separator");
        Object command = create();

        for (Map.Entry<String, ParameterMetadata> entry : parameterUsages.entrySet()) {
            if (sb.length() > 0) {
                sb.append(lineSeparator);
            }

            sb.append(maxLengthBuilder.build(entry.getKey()));

            final ParameterMetadata parameterMetadata = entry.getValue();
            boolean goToLine = false;

            if (parameterMetadata.getDescription() != null) {
                sb.append(' ');
                sb.append(UncapitalizeBuilder.build(parameterMetadata.getDescription()));

                goToLine = true;
            }

            final String[] values = registry.getConverter(parameterMetadata.getType())
                                            .getUsageValues();
            int length;

            if (values != null && (length = values.length) > 0) {
                if (goToLine) {
                    sb.append(lineSeparator);
                    sb.append(maxLengthBuilder.build());
                }

                sb.append(" one of : ");

                for (int i = length; i > 0; --i) {
                    if (i < length) {
                        if (i == 1) {
                            sb.append(" or ");
                        } else {
                            sb.append(", ");
                        }
                    }

                    sb.append(values[i - 1]);
                }

                goToLine = true;
            }

            final Object defaultValue = parameterMetadata.getValue(command);

            if (defaultValue != null) {
                if (goToLine) {
                    sb.append(lineSeparator);
                    sb.append(maxLengthBuilder.build());
                }

                sb.append(" default to : ");
                sb.append(defaultValue.toString());
            }
        }

        return sb.toString();
    }

    private ParameterMetadata getParameterMetadata(String name) {
        ParameterMetadata result = properties.get(name);

        if (result == null) {
            throw new InvalidParameterException(String.format("Unknown parameter <%s>", name));
        }

        return result;
    }

    private boolean addParameterMetadata(ParameterMetadata parameterMetadata) {
        return properties.put(parameterMetadata.getName(), parameterMetadata) == null;
    }

    private Collection<Field> getFields(Class<?> type) {
        Collection<Field> result = new ArrayList<Field>();

        for (Class<?> currentType = type; currentType != null; currentType = currentType.getSuperclass()) {
            Collections.addAll(result, currentType.getDeclaredFields());
        }

        return result;
    }

    private boolean hasConverter(Class<?> type) {
        return registry.getConverter(type) != null;
    }

    private Object newCommandInstance() {
        try {
            return commandType.newInstance();
        } catch (Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    private void init(Object bean) {
        for (ParameterMetadata parameterMetadata : this) {
            parameterMetadata.init(bean);
        }
    }

    private void fill(Object bean, Parameter[] parameters) {
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                final Object value = convert(parameter);

                getParameterMetadata(parameter.getName()).setValue(bean, value);
            }
        }
    }

    private Object convert(Parameter parameter) {
        ParameterMetadata parameterMetadata = getParameterMetadata(parameter.getName());
        StringConverter<?> converter = registry.getConverter(parameterMetadata.getType());

        try {
            return converter.convert(parameter.getValue());
        } catch (ConvertException e) {
            throw new InvalidParameterException(String.format("Invalid parameter <%s> : %s", parameter, converter.getUsage()));
        }
    }

    private void validate(Object bean) {
        ValidationResult result = new ValidationResult();

        for (ParameterMetadata parameterMetadata : this) {
            parameterMetadata.validate(bean, result);
        }

        if (bean instanceof ValidatableCommand) {
            ((ValidatableCommand) bean).validate(result);
        }

        if (!result.isOk()) {
            throw new ValidationException(result);
        }
    }
}
