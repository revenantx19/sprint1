package com.skypro.sprint1.controller;

import com.skypro.sprint1.pojo.ServiceInfo;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для управления сервисом.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    private final UserRecommendationService userRecommendationService;
    private final BuildProperties buildInfo;

    public ManagementController(UserRecommendationService userRecommendationService, BuildProperties buildInfo) {
        this.userRecommendationService = userRecommendationService;
        this.buildInfo = buildInfo;
    }

    /**
     * Очистка кэша рекомендаций пользователей.
     *
     * @return сообщение об успешном очищении кэша.
     */
    @GetMapping("/clear-caches")
    public ResponseEntity<String> clearRecommendationCache() {
        userRecommendationService.clearRecommendationCache();
        return ResponseEntity.ok("Cache cleared");
    }

    /**
     * Получение информации о сервисе.
     *
     * @return объект {@link ServiceInfo} с информацией о сервисе.
     */
    @GetMapping("/info")
    public ResponseEntity<ServiceInfo> getServiceInfo() {
        ServiceInfo info = new ServiceInfo(buildInfo.getName(), buildInfo.getVersion());
        return ResponseEntity.ok(info);
    }
}
