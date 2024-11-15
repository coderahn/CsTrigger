package com.myapp.CsTrigger.controller;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import com.myapp.CsTrigger.service.EmojiTriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TriggerController {
    private final EmojiTriggerService emojiTriggerService;

    /**
     * slack event 발생시 호출 API
     * @param slackRequestDto
     * @return
     */
    @PostMapping("event/trigger")
    public ResponseEntity<Map> test(@RequestBody SlackRequestDto slackRequestDto) {
        //1.이모지 프로세스 처리
        emojiTriggerService.emojiTriggerProcess(slackRequestDto);

        //2.응답 처리
        Map<String, String> responseBody = new HashMap<>();

        return ResponseEntity.ok(responseBody);
    }
}