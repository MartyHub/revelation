package org.sweet.revelation.revelation.core.concurrent;

import org.sweet.revelation.revelation.core.log.Work;

public interface Task<P, R> {

    R run(Work work, P param) throws Exception;
}
