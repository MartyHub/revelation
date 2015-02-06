package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;
import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;
import org.sweet.revelation.revelation.core.text.MessageBuilder;

public abstract class AbstractActivity implements Activity {

    private final AnsiFactory ansiFactory;

    protected final String name;

    protected final Activity parent;

    private boolean cancelled = false;

    protected AbstractActivity(AnsiFactory ansiFactory, String name, Activity parent) {
        this.ansiFactory = ansiFactory;
        this.name = name;
        this.parent = parent;
    }

    public final String getName() {
        if (cancelled) {
            return name + " (Cancelled)";
        } else {
            return name;
        }
    }

    public final boolean isCancelled() {
        if (parent != null) {
            return parent.isCancelled();
        }

        return cancelled;
    }

    public final void cancel() {
        if (parent != null) {
            parent.cancel();
        } else {
            if (log()) {
                info(messageBuilder().state("cancel")
                                     .build());
            }

            this.cancelled = true;

            doCancel();
        }
    }

    public final UnknownWorkLoad start() {
        if (log()) {
            info(messageBuilder().state("start")
                                 .build());
        }

        return doStart();
    }

    public final PredictableWorkLoad start(final int totalAmount, final boolean cancelOnFirstFailure) {
        if (log()) {
            info(messageBuilder().state("start")
                                 .a(" with ")
                                 .a(totalAmount)
                                 .a(" task(s)")
                                 .build());
        }

        return doStart(totalAmount, cancelOnFirstFailure);
    }

    @Override
    public String toString() {
        return name;
    }

    MessageBuilder messageBuilder() {
        return new MessageBuilder(ansiFactory);
    }

    protected boolean log() {
        return true;
    }

    protected void doCancel() {
    }

    protected UnknownWorkLoad doStart() {
        return new DefaultUnknownWorkLoad(this);
    }

    protected PredictableWorkLoad doStart(final int totalAmount, final boolean cancelOnFirstError) {
        return new DefaultPredictableWorkLoad(this, totalAmount, cancelOnFirstError);
    }

    protected final String subActivityName(String s) {
        if (name == null) {
            return s;
        }

        return String.format("%s / %s", name, s);
    }
}
