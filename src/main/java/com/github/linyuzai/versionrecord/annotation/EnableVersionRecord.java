package com.github.linyuzai.versionrecord.annotation;

import com.github.linyuzai.versionrecord.controller.VersionRecordController;
import com.github.linyuzai.versionrecord.core.VersionPointRegister;
import com.github.linyuzai.versionrecord.filter.DefaultVersionRecordFilter;
import com.github.linyuzai.versionrecord.filter.VersionRecordFilter;
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

    String dateFormatter() default "yyyy-MM-dd";

    Class<? extends VersionRecordFilter> filter() default DefaultVersionRecordFilter.class;
}
