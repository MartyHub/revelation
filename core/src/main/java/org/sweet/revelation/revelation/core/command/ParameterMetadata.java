package org.sweet.revelation.revelation.core.command;

import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.Mandatory;

import java.lang.reflect.Field;

public class ParameterMetadata implements Comparable<ParameterMetadata> {

    private final String name;

    private final String[] prefixes;

    private final Class<?> type;

    private final boolean mandatory;

    private final String description;

    ParameterMetadata(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("Field is mandatory");
        }

        this.name = field.getName();
        this.prefixes = new String[0];
        this.type = field.getType();
        this.mandatory = field.getAnnotation(Mandatory.class) != null;

        Description description = field.getAnnotation(Description.class);

        if (description != null) {
            this.description = description.value();
        } else {
            this.description = null;
        }
    }

    private ParameterMetadata(ParameterMetadata source, String prefix) {
        this.name = source.getName();
        this.prefixes = new String[source.prefixes.length + 1];
        this.type = source.getType();
        this.mandatory = source.isMandatory();
        this.description = source.getDescription();

        System.arraycopy(source.prefixes, 0, this.prefixes, 0, source.prefixes.length);

        this.prefixes[source.prefixes.length] = prefix;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public Class<?> getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Object getValue(Object bean) {
        Object lastBean = processNestedFields(bean, new NestedFieldHandler());
        Field field = getField(lastBean, name);

        try {
            return field.get(lastBean);
        } catch (IllegalAccessException e) {
            throw new InvalidCommandException(e);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof ParameterMetadata)) {
            return false;
        }

        ParameterMetadata other = (ParameterMetadata) o;

        return name.equals(other.name);
    }

    public int compareTo(ParameterMetadata other) {
        int result = 0;

        if (mandatory) {
            if (!other.mandatory) {
                result = -1;
            }
        } else if (other.mandatory) {
            result = 1;
        }

        if (result == 0) {
            result = name.compareTo(other.name);
        }

        return result;
    }

    ParameterMetadata withPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix is mandatory");
        }

        return new ParameterMetadata(this, prefix);
    }

    ParameterMetadata init(Object bean) {
        processNestedFields(bean, new NestedFieldHandler() {

            public Object handle(Object bean, Field field, Object value) throws IllegalAccessException, InstantiationException {
                if (value == null) {
                    Object newValue = field.getType()
                                           .newInstance();

                    field.set(bean, newValue);

                    return newValue;
                }

                return value;
            }
        });

        return this;
    }

    void validate(Object bean, final ValidationResult result) {
        Object lastBean = processNestedFields(bean, new NestedFieldHandler() {

            @Override
            Object handle(Object bean, Field field, Object value) throws IllegalAccessException, InstantiationException {
                if (value instanceof ValidatableCommand) {
                    ((ValidatableCommand) value).validate(result);
                }

                return value;
            }
        });

        if (mandatory) {
            Field field = getField(lastBean, name);

            try {
                Object value = field.get(lastBean);

                if (value == null) {
                    result.addError(String.format("Parameter <%s> is mandatory", name));
                }
            } catch (IllegalAccessException e) {
                throw new InvalidCommandException(e);
            }
        }
    }

    void setValue(Object bean, Object value) {
        Object lastBean = getLastBean(bean);
        Field field = getField(lastBean, name);

        try {
            field.set(lastBean, value);
        } catch (IllegalAccessException e) {
            throw new InvalidCommandException(e);
        }
    }

    String getUsage(String converterUsage) {
        StringBuilder sb = new StringBuilder(64);

        if (mandatory) {
            sb.append(" * ");
        } else {
            sb.append("   ");
        }

        sb.append('-');
        sb.append(name);
        sb.append('=');
        sb.append(converterUsage);

        return sb.toString();
    }

    private Field getField(Object bean, String name) {
        Class<?> type = bean.getClass();

        while (type != null) {
            try {
                final Field result = type.getDeclaredField(name);

                result.setAccessible(true);

                return result;
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }

        throw new InvalidCommandException(String.format("Failed to find <%s> in <%s>", name, bean));
    }

    private Object processNestedFields(Object bean, NestedFieldHandler handler) {
        Object currentBean = bean;

        try {
            for (String currentName : prefixes) {
                final Field currentField = getField(currentBean, currentName);

                currentBean = handler.handle(currentBean, currentField, currentField.get(currentBean));
            }
        } catch (Exception e) {
            throw new InvalidCommandException(e);
        }

        return currentBean;
    }

    private Object getLastBean(Object bean) {
        return processNestedFields(bean, new NestedFieldHandler());
    }

    private class NestedFieldHandler {

        Object handle(Object bean, Field field, Object value) throws IllegalAccessException, InstantiationException {
            return value;
        }
    }
}
