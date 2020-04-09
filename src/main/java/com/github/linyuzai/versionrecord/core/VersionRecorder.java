package com.github.linyuzai.versionrecord.core;

import com.github.linyuzai.versionrecord.filter.VersionRecordFilter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VersionRecorder {

    //private static final VersionRecorder sInstance = new VersionRecorder();

    private DateTimeFormatter formatter;
    private VersionRecordFilter filter;
    private List<VersionPointInformation> records;
    private Map<String, List<VersionPointInformation>> branchRecords;

    public VersionRecorder duplicate() {
        VersionRecorder recorder = new VersionRecorder();
        recorder.formatter = this.formatter;
        recorder.filter = this.filter;
        recorder.records = this.records;
        recorder.branchRecords = this.branchRecords;
        return recorder;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public VersionRecordFilter getFilter() {
        return filter;
    }

    public void setFilter(VersionRecordFilter filter) {
        this.filter = filter;
    }

    public List<VersionPointInformation> getRecords() {
        return records;
    }

    public synchronized void record(List<VersionPointInformation> vis) {
        final DateTimeFormatter dtf = this.formatter;
        if (dtf == null) {
            throw new NullPointerException("DateTimeFormatter is null");
        }
        final VersionRecordFilter filter = this.filter;
        if (filter == null) {
            throw new NullPointerException("VersionRecordFilter is null");
        }
        this.records = filter.filter(vis, dtf);
        this.branchRecords = records.stream().collect(Collectors.groupingBy(VersionPointInformation::getBranch));
    }

    public List<VersionPointInformation> getRecords(Date startDate, Date endDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        return getRecords(startDate == null ? null : startDate.toInstant().atZone(zoneId).toLocalDate(),
                endDate == null ? null : endDate.toInstant().atZone(zoneId).toLocalDate());
    }

    public List<VersionPointInformation> getRecords(LocalDate startDate, LocalDate endDate) {
        List<VersionPointInformation> vis = new ArrayList<>();
        for (VersionPointInformation vpi : this.records) {
            if (startDate != null && vpi.dateBefore(startDate)) {
                continue;
            }
            if (endDate != null && vpi.dateAfter(endDate)) {
                continue;
            }
            vis.add(vpi);
        }
        return vis;
    }

    public List<VersionPointInformation> getRecords(String branch) {
        return branchRecords.get(branch);
    }

    public VersionRecorder branch(String branch) {
        if (branch == null) {
            return this;
        }
        List<VersionPointInformation> vis = branchRecords.getOrDefault(branch, Collections.emptyList());
        VersionRecorder b = duplicate();
        b.records = vis;
        b.branchRecords = new HashMap<String, List<VersionPointInformation>>() {{
            put(branch, vis);
        }};
        return b;
    }
}
