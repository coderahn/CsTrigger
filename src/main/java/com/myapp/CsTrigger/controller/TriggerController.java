package com.myapp.CsTrigger.controller;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import com.myapp.CsTrigger.service.EmojiTriggerService;
import com.slack.api.methods.SlackApiException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public ResponseEntity<Map> test(@RequestBody SlackRequestDto slackRequestDto) throws SlackApiException, IOException {
        Map<String, String> responseBody = new HashMap<>();

        //1.슬랙 앱 Request URL 변경시 retry 체크용
        if (StringUtils.isNotEmpty(slackRequestDto.getChallenge())) {
            responseBody.put("challenge", slackRequestDto.getChallenge());
            return ResponseEntity.ok(responseBody);
        }

        //TODO: 2.유저정보 API 호출
        emojiTriggerService.getUserInfo(slackRequestDto);

        //3.CS 메시지 가져오기
        emojiTriggerService.getCsMessage(slackRequestDto);

        return ResponseEntity.ok(responseBody);
    }
}