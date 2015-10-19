package com.soxfmr.railgun.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DatabaseField {
    String field();
    int type();
    boolean nullable() default true;
    boolean unique() default false;
    boolean primary() default false;
    boolean foreign() default false;
    String references() default "";
}
