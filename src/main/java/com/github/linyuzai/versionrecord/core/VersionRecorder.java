package com.github.linyuzai.versionrecord.core;

import java.time.LocalDate;
import java.time.ZoneId;
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

    public static synchronized void record(List<VersionInformation> vis) {
        final DateTimeFormatter dtf = VersionRecorder.formatter;
        if (dtf == null) {
            throw new NullPointerException("DateTimeFormatter is null");
        }
        LocalDate now = LocalDate.now();
        List<VersionInformation> versions = new ArrayList<>();
        for (VersionInformation version : vis) {
            boolean isSame = false;
            for (VersionInformation have : versions) {
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
                versions.add(version);
            }
        }
        versions.sort(VersionInformation::sort);
        VersionRecorder.versions = versions;
    }

    public static List<VersionInformation> getVersions(Date start, Date end) {
        ZoneId zoneId = ZoneId.systemDefault();
        return getVersions(start == null ? null : start.toInstant().atZone(zoneId).toLocalDate(),
                end == null ? null : end.toInstant().atZone(zoneId).toLocalDate());
    }

    public static List<VersionInformation> getVersions(LocalDate start, LocalDate end) {
        List<VersionInformation> temp = new ArrayList<>();
        for (VersionInformation version : VersionRecorder.versions) {
            if (start != null && version.dateBefore(start)) {
                continue;
            }
            if (end != null && version.dateAfter(end)) {
                continue;
            }
            temp.add(version);
        }
        return temp;
    }
}
