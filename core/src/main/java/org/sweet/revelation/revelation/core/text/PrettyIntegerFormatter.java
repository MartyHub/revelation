package org.sweet.revelation.revelation.core.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PrettyIntegerFormatter {

    private final DecimalFormat decimalFormat;

    public PrettyIntegerFormatter() {
        this.decimalFormat = new DecimalFormat("#,##0");

        fixGroupingSeparator();
    }

    public String format(final long l) {
        return decimalFormat.format(l);
    }

    public String format(final double d) {
        return decimalFormat.format(d);
    }

    private void fixGroupingSeparator() {
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');

        decimalFormat.setDecimalFormatSymbols(symbols);
    }
}
