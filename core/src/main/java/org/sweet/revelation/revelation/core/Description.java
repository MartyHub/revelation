package org.sweet.revelation.revelation.core;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    String value();
}
