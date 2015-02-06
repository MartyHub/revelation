package org.sweet.revelation.revelation.core.processor;

public interface Processor<C> {

    ProcessorReport process(C command) throws Exception;
}
