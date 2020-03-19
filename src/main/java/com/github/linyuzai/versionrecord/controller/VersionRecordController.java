package com.github.linyuzai.versionrecord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.versionrecord.core.VersionPointInformation;
import com.github.linyuzai.versionrecord.core.VersionRecorder;
import com.github.linyuzai.versionrecord.util.JsonUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/version-record")
public class VersionRecordController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/list")
    public List<VersionPointInformation> getVersionRecords(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                           String branch) throws JsonProcessingException {
        return VersionRecorder.getInstance().branch(branch).getRecords(startDate, endDate);
    }

    @GetMapping("/json")
    public String getVersionRecordJson(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                       String branch) throws JsonProcessingException {
        return objectMapper.writeValueAsString(VersionRecorder.getInstance().branch(branch).getRecords(startDate, endDate));
    }

    @GetMapping("/json-format")
    public String getVersionRecordFormatJson(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                             String branch) throws JsonProcessingException {
        return JsonUtils.format(getVersionRecordJson(startDate, endDate, branch));
    }
}
