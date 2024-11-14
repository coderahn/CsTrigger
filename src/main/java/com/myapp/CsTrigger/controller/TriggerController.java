package com.myapp.CsTrigger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TriggerController {
    /**
     * slack event 발생시 호출 API
     * @param map
     * @return
     */
    @PostMapping("event/trigger")
    public ResponseEntity<Map> test(@RequestBody Map<String, String> map) {
        //key&value printing
        for (String key : map.keySet()) {
            log.info("key:{}, value:{}", key, map.get(key));
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("challenge", String.valueOf(map.get("challenge")));

        return ResponseEntity.ok(responseBody);
    }
}