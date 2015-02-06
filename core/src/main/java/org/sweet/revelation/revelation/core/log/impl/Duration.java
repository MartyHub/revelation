package org.sweet.revelation.revelation.core.log.impl;

public class Duration implements Comparable<Duration> {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final long start;

        public Builder() {
            start = now();
        }

        public Duration build() {
            return new Duration(now() - start);
        }

        private long now() {
            return System.currentTimeMillis();
        }
    }

    public static String format(final long duration) {
        final long hours = duration / ONE_HOUR_IN_MS;
        long remaining = duration - hours * ONE_HOUR_IN_MS;

        final long minutes = remaining / ONE_MINUTE_IN_MS;
        remaining -= minutes * ONE_MINUTE_IN_MS;

        final long seconds = remaining / ONE_SECOND_IN_MS;
        remaining -= seconds * ONE_SECOND_IN_MS;

        StringBuilder sb = new StringBuilder(12);

        append(hours, sb, 2);
        sb.append(":");
        append(minutes, sb, 2);
        sb.append(":");
        append(seconds, sb, 2);
        sb.append(".");
        append(remaining, sb, 3);

        return sb.toString();
    }

    private static final int ONE_SECOND_IN_MS = 1000;

    private static final int ONE_MINUTE_IN_MS = ONE_SECOND_IN_MS * 60;

    private static final int ONE_HOUR_IN_MS = ONE_MINUTE_IN_MS * 60;

    private static void append(final long number, StringBuilder sb, int pad) {
        if (number < 100 && pad == 3) {
            sb.append('0');

            --pad;
        }

        if (number < 10 && pad == 2) {
            sb.append('0');

            --pad;
        }

        sb.append(number);
    }

    private final long value;

    public Duration(final long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public Duration average(final int nb) {
        if (nb != 0) {
            return new Duration(value / nb);
        } else {
            return new Duration(0);
        }
    }

    public String format() {
        return format(value);
    }

    public int compareTo(Duration other) {
        if (value < other.value) {
            return -1;
        } else if (value > other.value) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
