package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;
import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;

public class DefaultPredictableWorkLoad extends AbstractWork implements PredictableWorkLoad {

    private final int totalAmount;

    private final boolean cancelOnFirstError;

    private int nbOk = 0;

    private int amountDone = 0;

    DefaultPredictableWorkLoad(AbstractActivity parent, final int totalAmount, final boolean cancelOnFirstError) {
        super(parent);

        this.totalAmount = totalAmount;
        this.cancelOnFirstError = cancelOnFirstError;

        checkFinished();
    }

    public void worked(String message) {
        ++nbOk;
        ++amountDone;

        info(getWorkMessage(message, false, null));

        checkFinished();
    }

    public void failed(String message, Exception cause) {
        ++amountDone;

        info(getWorkMessage(message, true, cause));

        if (cancelOnFirstError) {
            parent.cancel();
        }

        checkFinished();
    }

    public PredictableWorkLoad synchronize() {
        return new SynchronizedPredictableWorkLoad(this);
    }

    private void checkFinished() {
        if (isFinished()) {
            info(getDoneMessage());
        }
    }

    private boolean isFinished() {
        return amountDone == totalAmount;
    }

    private String getWorkMessage(String message, final boolean failure, Exception e) {
        Duration duration = durationBuilder.build();
        PrettyIntegerFormatter numberFormat = new PrettyIntegerFormatter();
        String pattern = "%" + String.valueOf(totalAmount)
                                     .length() + "s";
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append(String.format(pattern, numberFormat.format(amountDone)));
        sb.append(" / ");
        sb.append(String.format(pattern, numberFormat.format(totalAmount)));
        sb.append(", ");

        final int percentage = getPercentage();

        sb.append(String.format("%3s", numberFormat.format(percentage)));
        sb.append("% in ");
        sb.append(duration.format());
        sb.append(", ");

        final double multiplier = getRemainingMultiplier();
        Duration remaining = new Duration((long) (duration.getValue() * multiplier));

        sb.append(remaining.format());
        sb.append(" remaining");

        if (amountDone != nbOk) {
            sb.append(", ");
            sb.append(String.format(pattern, numberFormat.format(amountDone - nbOk)));
            sb.append(" failure(s)");
        }

        sb.append(") ");
        sb.append(message);

        if (failure) {
            sb.append(' ');
            sb.append(messageBuilder().state("failed")
                                      .build());

            if (e != null && e.getMessage() != null) {
                sb.append(" {");
                sb.append(e.getMessage());
                sb.append("}");
            }
        } else {
            sb.append(' ');
            sb.append(messageBuilder().state("done")
                                      .build());
        }

        return sb.toString();
    }

    private String getDoneMessage() {
        Duration duration = durationBuilder.build();
        StringBuilder sb = new StringBuilder();

        if (isCancelled()) {
            sb.append(messageBuilder().state("cancelled")
                                      .build());
        } else {
            sb.append(messageBuilder().state("done")
                                      .build());
        }

        if (amountDone == 0) {
            sb.append(" (last ");
            sb.append(duration.format());
        } else {
            PrettyIntegerFormatter numberFormat = new PrettyIntegerFormatter();

            sb.append(" (");
            sb.append(String.format("%3s", numberFormat.format(getSuccessPercentage())));
            sb.append("% success in ");
            sb.append(duration.format());
            sb.append(", average for ");
            sb.append(numberFormat.format(amountDone));
            sb.append(" task(s) is ");
            sb.append(duration.average(amountDone)
                              .format());
        }

        sb.append(")");

        return sb.toString();
    }

    private int getPercentage() {
        if (totalAmount != 0) {
            return amountDone * 100 / totalAmount;
        }

        return 0;
    }

    private int getSuccessPercentage() {
        if (amountDone != 0) {
            return nbOk * 100 / amountDone;
        }

        return 0;
    }

    private double getRemainingMultiplier() {
        final double remaining = totalAmount - amountDone;
        final double multiplier = remaining / amountDone;

        return multiplier;
    }
}
