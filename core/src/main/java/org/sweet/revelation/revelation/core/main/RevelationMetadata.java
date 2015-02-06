package org.sweet.revelation.revelation.core.main;

import org.sweet.revelation.revelation.core.ProcessorComponent;
import org.sweet.revelation.revelation.core.processor.InvalidProcessorException;
import org.sweet.revelation.revelation.core.processor.Processor;
import org.sweet.revelation.revelation.core.processor.ProcessorMetadata;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Lazy
public class RevelationMetadata {

    @Autowired
    private ListableBeanFactory beanFactory;

    @SuppressWarnings("unchecked")
    public Iterable<ProcessorMetadata> iterate() {
        Map<String, Object> processors = beanFactory.getBeansWithAnnotation(ProcessorComponent.class);
        Collection<ProcessorMetadata> result = new ArrayList<ProcessorMetadata>(processors.size());

        for (Map.Entry<String, Object> entry : processors.entrySet()) {
            final Object bean = entry.getValue();

            if (Processor.class.isInstance(bean)) {
                result.add(new ProcessorMetadata(entry.getKey(), Processor.class.cast(bean)));
            }
        }

        return result;
    }

    public ProcessorMetadata getProcessorMetadata(String name) {
        ProcessorMetadata result = doGetProcessorMetadata(name);

        if (result == null) {
            result = findProcessorMetadata(name);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private ProcessorMetadata doGetProcessorMetadata(String name) {
        try {
            Object bean = beanFactory.getBean(name);

            if (bean != null && Processor.class.isInstance(bean)) {
                return new ProcessorMetadata(name, Processor.class.cast(bean));
            }
        } catch (BeansException be) {
        }

        return null;
    }

    private ProcessorMetadata findProcessorMetadata(String name) {
        for (ProcessorMetadata processorMetadata : iterate()) {
            if (processorMetadata.getName()
                                 .equalsIgnoreCase(name)) {
                return processorMetadata;
            }
        }

        throw new InvalidProcessorException(String.format("Unknown command <%s>", name));
    }
}
