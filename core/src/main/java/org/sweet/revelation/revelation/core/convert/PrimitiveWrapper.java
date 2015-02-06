package org.sweet.revelation.revelation.core.convert;

public class PrimitiveWrapper<T> {

    private final Class<T> type;

    public PrimitiveWrapper(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type is mandatory");
        }

        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public Class<T> wrap() {
        if (type.isPrimitive()) {
            if (type.equals(boolean.class)) {
                return (Class<T>) Boolean.class;
            } else if (type.equals(byte.class)) {
                return (Class<T>) Byte.class;
            } else if (type.equals(char.class)) {
                return (Class<T>) Character.class;
            } else if (type.equals(double.class)) {
                return (Class<T>) Double.class;
            } else if (type.equals(float.class)) {
                return (Class<T>) Float.class;
            } else if (type.equals(int.class)) {
                return (Class<T>) Integer.class;
            } else if (type.equals(long.class)) {
                return (Class<T>) Long.class;
            } else if (type.equals(short.class)) {
                return (Class<T>) Short.class;
            }
        }

        return type;
    }
}
