package com.github.linyuzai.versionrecord.filter;

import com.github.linyuzai.versionrecord.core.VersionPointInformation;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface VersionRecordFilter {

    List<VersionPointInformation> filter(List<VersionPointInformation> sources, DateTimeFormatter dtf);
}
