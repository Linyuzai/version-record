package com.github.linyuzai.versionrecord.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(VersionPoints.class)
public @interface VersionPoint {

    String version() default "";

    String description();

    String date();
}
