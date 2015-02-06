package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;

public class DefaultUnknownWorkLoad extends AbstractWork implements UnknownWorkLoad {

    DefaultUnknownWorkLoad(AbstractActivity parent) {
        super(parent);
    }

    public void done() {
        Duration duration = durationBuilder.build();
        StringBuilder sb = new StringBuilder();

        if (isCancelled()) {
            sb.append(messageBuilder().state("cancelled")
                                      .build());
        } else {
            sb.append(messageBuilder().state("done")
                                      .build());
        }

        sb.append(" (last ");
        sb.append(duration.format());
        sb.append(")");

        info(sb.toString());
    }
}
