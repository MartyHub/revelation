package org.sweet.revelation.revelation.core.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PrettyThreadNameFactory implements ThreadFactory {

    private final int threadIndexLength;

    private final String prefix;

    private final AtomicInteger nextThreadIndex;

    public PrettyThreadNameFactory(final int nbThreads, String prefix) {
        this.threadIndexLength = String.valueOf(nbThreads)
                                       .length();
        this.prefix = prefix == null ? "T_" : prefix + "_";
        this.nextThreadIndex = new AtomicInteger(1);
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, nextThreadName());
    }

    private String nextThreadName() {
        String threadIndex = String.valueOf(nextThreadIndex.getAndIncrement());
        StringBuilder sb = new StringBuilder(prefix);

        for (int i = threadIndex.length(); i < threadIndexLength; ++i) {
            sb.append('0');
        }

        sb.append(threadIndex);

        return sb.toString();
    }
}
