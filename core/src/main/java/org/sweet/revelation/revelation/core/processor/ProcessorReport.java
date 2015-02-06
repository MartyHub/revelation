package org.sweet.revelation.revelation.core.processor;

import org.sweet.revelation.revelation.core.text.UncapitalizeBuilder;

public class ProcessorReport {

    public static final ProcessorReport success(String format, Object... args) {
        if (format == null) {
            return new ProcessorReport(ProcessorStatus.SUCCESS, format, null);
        } else {
            return new ProcessorReport(ProcessorStatus.SUCCESS, String.format(format, args), null);
        }
    }

    public static final ProcessorReport cancelled(String format, Object... args) {
        if (format == null) {
            return new ProcessorReport(ProcessorStatus.CANCELLED, format, null);
        } else {
            return new ProcessorReport(ProcessorStatus.CANCELLED, String.format(format, args), null);
        }
    }

    public static final ProcessorReport failure(String format, Object... args) {
        if (format == null) {
            return new ProcessorReport(ProcessorStatus.ERROR, format, null);
        } else {
            return new ProcessorReport(ProcessorStatus.ERROR, String.format(format, args), null);
        }
    }

    public static final ProcessorReport failure(String format, Exception cause, Object... args) {
        if (format == null) {
            return new ProcessorReport(ProcessorStatus.ERROR, format, cause);
        } else {
            return new ProcessorReport(ProcessorStatus.ERROR, String.format(format, args), cause);
        }
    }

    public static final ProcessorReport done(final boolean success, String format, Exception cause, Object... args) {
        if (format == null) {
            return new ProcessorReport(success ? ProcessorStatus.SUCCESS : ProcessorStatus.ERROR, format, cause);
        } else {
            return new ProcessorReport(success ? ProcessorStatus.SUCCESS : ProcessorStatus.ERROR, String.format(format, args), cause);
        }
    }

    private final ProcessorStatus status;

    private final String message;

    private final Exception cause;

    private ProcessorReport(ProcessorStatus status, String message, Exception cause) {
        this.status = status;
        this.message = message;
        this.cause = cause;
    }

    public ProcessorStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Exception getCause() {
        return cause;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(status);

        if (message != null) {
            sb.append(" : ");
            sb.append(UncapitalizeBuilder.build(message));
        }

        if (cause != null && cause.getMessage() != null && !cause.getMessage()
                                                                 .equals(message)) {
            sb.append(" (");
            sb.append(cause.getMessage());
            sb.append(")");
        }

        return sb.toString();
    }
}
