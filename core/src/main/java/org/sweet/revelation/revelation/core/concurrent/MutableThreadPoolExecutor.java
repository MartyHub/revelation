package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MutableThreadPoolExecutor extends ThreadPoolExecutor {

    private final String name;

    private final MutableThreadPoolExecutor delegate;

    private final Activity activity;

    private final ReentrantLock lock;

    public MutableThreadPoolExecutor(final int threadCount, final int taskCount, Activity activity) {
        this(threadCount, taskCount, activity, activity.getName());
    }

    public MutableThreadPoolExecutor(final int threadCount, final int taskCount, Activity activity, String name) {
        this(threadCount, taskCount, activity, name, null);
    }

    public MutableThreadPoolExecutor(final int threadCount, final int taskCount, Activity activity, String name, MutableThreadPoolExecutor delegate) {
        super(1, threadCount, 5L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(taskCount), new PrettyThreadNameFactory(threadCount, name));

        this.name = name;
        this.delegate = delegate;
        this.activity = activity;
        this.lock = new ReentrantLock();

        PrettyIntegerFormatter formatter = new PrettyIntegerFormatter();

        activity.info(String.format("using maximum of <%s> thread(s) for <%s> task(s)", formatter.format(threadCount), formatter.format(taskCount)));

        if (delegate != null) {
            activity.info(String.format("delegate to <%s>", delegate));
        }
    }

    void addThreads(final int nbThreads) {
        final ReentrantLock lock = this.lock;

        lock.lock();

        try {
            final int oldValue = getMaximumPoolSize();
            final int newValue = oldValue + nbThreads;

            if (!getQueue().isEmpty()) {
                PrettyIntegerFormatter formatter = new PrettyIntegerFormatter();

                activity.info(String.format("Setting maximum pool size from <%s> to <%s> for <%s>", formatter.format(oldValue),
                        formatter.format(newValue), this));

                setMaximumPoolSize(newValue);
            } else if (delegate != null) {
                delegate.addThreads(newValue);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (delegate != null && getActiveCount() == 0 && getQueue().isEmpty()) {
            delegate.addThreads(getMaximumPoolSize());
        }
    }
}
