package org.sweet.revelation.revelation.core.log;

public interface PredictableWorkLoad extends Work {

    void worked(String message);

    void failed(String message, Exception cause);

    PredictableWorkLoad synchronize();
}
