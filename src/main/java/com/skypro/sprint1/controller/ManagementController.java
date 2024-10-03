package com.skypro.sprint1.controller;

import com.skypro.sprint1.pojo.ServiceInfo;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final UserRecommendationService userRecommendationService;
    private final BuildProperties buildInfo;

    public ManagementController(UserRecommendationService userRecommendationService, BuildProperties buildInfo) {
        this.userRecommendationService = userRecommendationService;
        this.buildInfo = buildInfo;
    }

    @GetMapping("/clear-caches")
    public ResponseEntity<String> clearRecommendationCache() {
        userRecommendationService.clearRecommendationCache();
        return ResponseEntity.ok("Cache cleared");
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInfo> getServiceInfo() {
        ServiceInfo info = new ServiceInfo(buildInfo.getName(), buildInfo.getVersion());
        return ResponseEntity.ok(info);
    }
}
