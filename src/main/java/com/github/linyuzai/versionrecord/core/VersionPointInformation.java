package com.github.linyuzai.versionrecord.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VersionPointInformation {

    private String version;
    private String description;
    private String branch;
    private String[] dependServices;
    private String[] dependTables;
    private String date;
    private String[] locations;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String[] getDependServices() {
        return dependServices;
    }

    public void setDependServices(String[] dependServices) {
        this.dependServices = dependServices;
    }

    public String[] getDependTables() {
        return dependTables;
    }

    public void setDependTables(String[] dependTables) {
        this.dependTables = dependTables;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public void applyFormatter(DateTimeFormatter formatter, LocalDate now) {
        _date = LocalDate.parse(date, formatter);
        if (_date.isAfter(now)) {
            throw new RuntimeException("Date " + date + " id not arrive");
        }
    }

    public int sort(VersionPointInformation vi) {
        return vi._date.compareTo(_date);
    }

    public boolean dateBefore(LocalDate date) {
        return _date.isBefore(date);
    }

    public boolean dateAfter(LocalDate date) {
        return _date.isAfter(date);
    }
}
