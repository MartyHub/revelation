package org.sweet.revelation.revelation.core.processor.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.processor.Processor;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UnknownWorkLoadProcessor<C> implements Processor<C> {

    @Autowired
    private Activity activity;

    public ProcessorReport process(C command) {
        UnknownWorkLoad uw = activity.start();

        try {
            return process(uw, command);
        } finally {
            uw.done();
        }
    }

    protected abstract ProcessorReport process(Work work, C command);
}
