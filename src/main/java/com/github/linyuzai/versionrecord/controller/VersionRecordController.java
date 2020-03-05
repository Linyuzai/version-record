package com.github.linyuzai.versionrecord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.versionrecord.core.VersionRecorder;
import com.github.linyuzai.versionrecord.util.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version-record")
public class VersionRecordController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/json")
    public String getVersionRecordJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(VersionRecorder.getVersions());
    }

    @GetMapping("/json-format")
    public String getVersionRecordFormatJson() throws JsonProcessingException {
        return JsonUtils.format(getVersionRecordJson());
    }

}
