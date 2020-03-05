package com.github.linyuzai.versionrecord.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VersionRecorder {

    private static DateTimeFormatter formatter;
    private static List<VersionInformation> versions;

    public static List<VersionInformation> getVersions() {
        return versions;
    }

    public static void setFormatter(DateTimeFormatter formatter) {
        VersionRecorder.formatter = formatter;
    }

    public static synchronized void record(List<VersionInformation> versions) {
        final DateTimeFormatter dtf = VersionRecorder.formatter;
        if (dtf == null) {
            throw new NullPointerException("DateTimeFormatter is null");
        }
        LocalDate now = LocalDate.now();
        VersionRecorder.versions = new ArrayList<>();
        for (VersionInformation version : versions) {
            boolean isSame = false;
            for (VersionInformation have : VersionRecorder.versions) {
                if (version.getVersion().equals(have.getVersion())
                        && version.getDescription().equals(have.getDescription())
                        && version.getDate().equals(have.getDate())
                        && version.getLocation().equals(have.getLocation())) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                version.applyFormatter(dtf, now);
                VersionRecorder.versions.add(version);
            }
        }
        VersionRecorder.versions.sort(VersionInformation::sort);
    }
}