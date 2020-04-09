package com.github.linyuzai.versionrecord;

import com.github.linyuzai.versionrecord.annotation.EnableVersionRecord;
import com.github.linyuzai.versionrecord.annotation.VersionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@VersionPoint(description = "测试1", branch = "master", author = "linyuzai", date = "2020-03-03")
@VersionPoint(description = "测试2", branch = "master", author = "linyuzai", date = "2020-02-28")
@VersionPoint(description = "测试3", branch = "dev-01", author = "linyuzai", date = "2020-03-02")
@EnableVersionRecord
@SpringBootApplication
public class VersionRecordApplication {

    @VersionPoint(description = "测试4", branch = "dev-02", author = "linyuzai", date = "2020-04-02")
    public static void main(String[] args) {
        SpringApplication.run(VersionRecordApplication.class, args);
    }

}
