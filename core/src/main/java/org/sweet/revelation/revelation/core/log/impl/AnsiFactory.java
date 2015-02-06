package org.sweet.revelation.revelation.core.log.impl;

import org.fusesource.jansi.Ansi;

public class AnsiFactory {

    public static AnsiFactory enabled() {
        return new AnsiFactory(false);
    }

    public static AnsiFactory disabled() {
        return new AnsiFactory(true);
    }

    private final boolean consoleFormatDisabled;

    private AnsiFactory(final boolean consoleFormatDisabled) {
        this.consoleFormatDisabled = consoleFormatDisabled;
    }

    public Ansi ansi() {
        return create(!consoleFormatDisabled);
    }

    private Ansi create(final boolean enabled) {
        Ansi.setEnabled(enabled);

        return Ansi.ansi();
    }
}
