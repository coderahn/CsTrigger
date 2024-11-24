package com.myapp.CsTrigger.controller;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import com.myapp.CsTrigger.service.EmojiTriggerService;
import com.myapp.CsTrigger.util.RequestValidator;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.User;
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
     * TODO : response시 status별 처리
     * @param slackRequestDto
     * @return
     */
    @PostMapping("event/trigger")
    public ResponseEntity<Map> trigger(@RequestBody SlackRequestDto slackRequestDto) throws SlackApiException, IOException {
        Map<String, String> responseBody = new HashMap<>();

        //1.슬랙 앱 Request URL 변경시 retry 체크용
        if (StringUtils.isNotEmpty(slackRequestDto.getChallenge())) {
            responseBody.put("challenge", slackRequestDto.getChallenge());

            return ResponseEntity.ok(responseBody);
        }

        //2.기본 valid 처리(TDOO : 규모커지면 @ControllerDevice 변경)
        RequestValidator.validate(slackRequestDto);

        //3.유저정보 API 호출
        User userInfo = emojiTriggerService.getUserInfo(slackRequestDto);

        //4.CS 메시지 가져오기
        ConversationsHistoryResponse csMessageResponse = emojiTriggerService.getCsMessage(slackRequestDto);

        if (csMessageResponse.getError() != null) {
            responseBody.put("error", csMessageResponse.getError());

            return ResponseEntity.ok(responseBody);
        }

        //5.CS 메시지 wiki 등록
        emojiTriggerService.writeCsMessage(userInfo, csMessageResponse);

        return ResponseEntity.ok(responseBody);
    }
}