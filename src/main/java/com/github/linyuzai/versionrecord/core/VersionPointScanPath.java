package com.github.linyuzai.versionrecord.core;

import java.util.Arrays;
import java.util.List;

public class VersionPointScanPath {

    private List<String> basePackages;

    public VersionPointScanPath(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public VersionPointScanPath(String... basePackages) {
        this.basePackages = Arrays.asList(basePackages);
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public void setBasePackages(String... basePackages) {
        this.basePackages = Arrays.asList(basePackages);
    }
}
