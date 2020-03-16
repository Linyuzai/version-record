package com.github.linyuzai.versionrecord;

import com.github.linyuzai.versionrecord.annotation.EnableVersionRecord;
import com.github.linyuzai.versionrecord.annotation.VersionPoint;
import com.github.linyuzai.versionrecord.core.VersionScanPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@VersionPoint(description = "测试1", branch = "master", date = "2020-03-03")
@VersionPoint(description = "测试2", branch = "master", date = "2020-02-28")
@VersionPoint(description = "测试3", branch = "master", date = "2020-03-02")
@EnableVersionRecord
@SpringBootApplication
public class VersionRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(VersionRecordApplication.class, args);
    }

    @Bean
    VersionScanPath getScanPath() {
        return new VersionScanPath("com.github.linyuzai");
    }
}
