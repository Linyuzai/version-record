package com.github.linyuzai.versionrecord.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VersionInformation {

    private String version;
    private String description;
    private String date;
    private String location;
    private LocalDate _date;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void applyFormatter(DateTimeFormatter formatter, LocalDate now) {
        _date = LocalDate.parse(date, formatter);
        if (_date.isAfter(now)) {
            throw new RuntimeException("Date " + date + " id not arrive");
        }
    }

    public int sort(VersionInformation vi) {
        return vi._date.compareTo(_date);
    }
}
