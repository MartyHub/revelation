package org.sweet.revelation.revelation.core;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Component
@Lazy
public @interface ProcessorComponent {

    Class<?> value();
}
