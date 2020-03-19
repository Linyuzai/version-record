package com.github.linyuzai.versionrecord.filter;

import com.github.linyuzai.versionrecord.core.VersionPointInformation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DefaultVersionRecordFilter implements VersionRecordFilter {

    private static final DefaultVersionRecordFilter sInstance = new DefaultVersionRecordFilter();

    private DefaultVersionRecordFilter() {

    }

    public static DefaultVersionRecordFilter getInstance() {
        return sInstance;
    }

    @Override
    public List<VersionPointInformation> filter(List<VersionPointInformation> sources, DateTimeFormatter dtf) {
        LocalDate now = LocalDate.now();
        List<VersionPointInformation> records = new ArrayList<>();
        for (VersionPointInformation vi : sources) {
            boolean isSame = false;
            for (VersionPointInformation have : records) {
                if (vi.getVersion().equals(have.getVersion())
                        && vi.getDescription().equals(have.getDescription())
                        && vi.getBranch().equals(have.getBranch())
                        && vi.getDate().equals(have.getDate())) {
                    boolean isLocationsSame = Arrays.equals(vi.getLocations(), have.getLocations());
                    boolean isDependServicesSame = Arrays.equals(vi.getDependServices(), have.getDependServices());
                    boolean isDependTablesSame = Arrays.equals(vi.getDependTables(), have.getDependTables());
                    if (isLocationsSame) {
                        if (!isDependServicesSame) {
                            Set<String> newDependServices = new HashSet<>();
                            newDependServices.addAll(Arrays.asList(have.getDependServices()));
                            newDependServices.addAll(Arrays.asList(vi.getDependServices()));
                            have.setDependServices(newDependServices.toArray(new String[]{}));
                        }
                        if (!isDependTablesSame) {
                            Set<String> newDependTables = new HashSet<>();
                            newDependTables.addAll(Arrays.asList(have.getDependTables()));
                            newDependTables.addAll(Arrays.asList(vi.getDependTables()));
                            have.setDependTables(newDependTables.toArray(new String[]{}));
                        }
                        isSame = true;
                        break;
                    } else {
                        if (isDependServicesSame && isDependTablesSame) {
                            Set<String> newLocations = new HashSet<>();
                            newLocations.addAll(Arrays.asList(have.getLocations()));
                            newLocations.addAll(Arrays.asList(vi.getLocations()));
                            have.setLocations(newLocations.toArray(new String[]{}));
                            isSame = true;
                            break;
                        }
                    }
                }
            }
            if (!isSame) {
                vi.applyFormatter(dtf, now);
                records.add(vi);
            }
        }
        records.sort(VersionPointInformation::sort);
        return records;
    }
}
