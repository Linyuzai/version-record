package com.github.linyuzai.versionrecord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.versionrecord.core.VersionInformation;
import com.github.linyuzai.versionrecord.core.VersionRecorder;
import com.github.linyuzai.versionrecord.util.JsonUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/version-record")
public class VersionRecordController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/list")
    public List<VersionInformation> getVersionRecords(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws JsonProcessingException {
        return VersionRecorder.getVersions(start, end);
    }

    @GetMapping("/json")
    public String getVersionRecordJson(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws JsonProcessingException {
        return objectMapper.writeValueAsString(VersionRecorder.getVersions(start, end));
    }

    @GetMapping("/json-format")
    public String getVersionRecordFormatJson(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws JsonProcessingException {
        return JsonUtils.format(getVersionRecordJson(start, end));
    }
}
