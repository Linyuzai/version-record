package com.github.linyuzai.versionrecord.configuration;

import com.github.linyuzai.versionrecord.core.VersionRecorder;
import com.github.linyuzai.versionrecord.filter.DefaultVersionRecordFilter;
import com.github.linyuzai.versionrecord.filter.VersionRecordFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class VersionRecordAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(VersionRecorder.class)
    public VersionRecorder getVersionRecorder() {
        return new VersionRecorder();
    }

    @Bean
    @ConditionalOnMissingBean(VersionRecordFilter.class)
    public VersionRecordFilter getVersionRecordFilter() {
        return new DefaultVersionRecordFilter();
    }
}
