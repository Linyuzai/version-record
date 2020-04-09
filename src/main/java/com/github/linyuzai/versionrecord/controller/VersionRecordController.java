package com.github.linyuzai.versionrecord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.linyuzai.versionrecord.core.VersionPointInformation;
import com.github.linyuzai.versionrecord.core.VersionRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/version-record")
public class VersionRecordController {

    @Autowired
    private VersionRecorder versionRecorder;

    @GetMapping("/list")
    public List<VersionPointInformation> getVersionRecords(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                           String branch) throws JsonProcessingException {
        return versionRecorder.branch(branch).getRecords(startDate, endDate);
    }
}
