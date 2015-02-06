package org.sweet.revelation.revelation.core.text;

import org.sweet.revelation.revelation.core.log.impl.AnsiFactory;
import org.fusesource.jansi.Ansi;

public class MessageBuilder {

    private final Ansi ansi;

    private PrettyIntegerFormatter prettyIntegerFormatter;

    public MessageBuilder(AnsiFactory ansiFactory) {
        ansi = ansiFactory.ansi();
    }

    public MessageBuilder state(String state) {
        ansi.fg(Ansi.Color.CYAN)
            .a('#')
            .a(state)
            .reset();

        return this;
    }

    public MessageBuilder a(String s) {
        if (s != null) {
            ansi.a(s);
        }

        return this;
    }

    public MessageBuilder aIf(String condition, String s) {
        if (condition != null && s != null) {
            ansi.a(condition)
                .a(s);
        }

        return this;
    }

    public MessageBuilder a(final long l) {
        if (prettyIntegerFormatter == null) {
            prettyIntegerFormatter = new PrettyIntegerFormatter();
        }

        ansi.a(prettyIntegerFormatter.format(l));

        return this;
    }

    public MessageBuilder bold() {
        ansi.bold();

        return this;
    }

    public MessageBuilder success() {
        ansi.fg(Ansi.Color.GREEN);

        return this;
    }

    public MessageBuilder warning() {
        ansi.fg(Ansi.Color.YELLOW);

        return this;
    }

    public MessageBuilder error() {
        ansi.fg(Ansi.Color.RED);

        return this;
    }

    public String build() {
        return ansi.reset()
                   .toString();
    }
}
