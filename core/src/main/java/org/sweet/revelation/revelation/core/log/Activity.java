package org.sweet.revelation.revelation.core.log;

public interface Activity extends Work {

    String getName();

    void cancel();

    Activity createSubActivity(String name);

    UnknownWorkLoad start();

    PredictableWorkLoad start(final int totalAmount, final boolean cancelOnFirstFailure);
}
