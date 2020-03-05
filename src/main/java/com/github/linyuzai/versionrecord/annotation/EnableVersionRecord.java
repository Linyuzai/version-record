package com.github.linyuzai.versionrecord.annotation;

import com.github.linyuzai.versionrecord.controller.VersionRecordController;
import com.github.linyuzai.versionrecord.core.VersionPointRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({VersionPointRegister.class, VersionRecordController.class})
public @interface EnableVersionRecord {

    String[] basePackages() default {};

    String dateTimeFormatter() default "yyyy-MM-dd";
}
