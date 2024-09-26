package com.skypro.sprint1.controller;

import com.skypro.sprint1.pojo.ServiceInfo;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ServiceInfoController {

    private final BuildProperties buildInfo;

    public ServiceInfoController(BuildProperties buildInfo) {
        this.buildInfo = buildInfo;
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInfo> getServiceInfo() {
        ServiceInfo info = new ServiceInfo(buildInfo.getName(), buildInfo.getVersion());
        return ResponseEntity.ok(info);
    }
}
