package com.myapp.CsTrigger.service;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import com.myapp.CsTrigger.util.PrivateDataInfo;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmojiTriggerService {
    /**
     * 유저정보 가져오기(CS처리자)
     * @param slackRequestDto
     */
    public void getUserInfo(SlackRequestDto slackRequestDto) throws SlackApiException, IOException {
        String userId = slackRequestDto.getEvent().getUser();

        // Slack 인스턴스 생성
        Slack slack = Slack.getInstance();

        // 유저 정보 요청
        UsersInfoRequest request = UsersInfoRequest.builder()
                .token(PrivateDataInfo.SLACK_TOKEN)
                .user(userId)
                .build();

        // 유저 정보 가져오기
        UsersInfoResponse response = slack.methods().usersInfo(request);

        //TODO: error(mssing_scope / need:users_read)
    }


    /**
     * CS 메시지 가져오기
     * @param slackRequestDto
     * @return
     */
    public String getCsMessage(SlackRequestDto slackRequestDto) {
        String reaction = slackRequestDto.getEvent().getReaction();

        if ("white_check_mark".equals(reaction)) {
            String channelId = slackRequestDto.getEvent().getItem().getChannel();
            String ts = slackRequestDto.getEvent().getItem().getTs();

            this.getContents(channelId, ts);
        } else {
            //TODO: 기타 이모지
        }

        return null;
    }

    /**
     * 감지된 이모지 메시지 가져오기
     * @param channelId
     * @param ts
     * @return
     */
    public ConversationsHistoryResponse getContents(String channelId, String ts) {
        Slack slack = Slack.getInstance();

        try {
            //1.요청값 세팅
            ConversationsHistoryRequest request = this.setRequest(channelId, ts);

            //2.API 호출
            ConversationsHistoryResponse response = slack.methods().conversationsHistory(request);

            if (response.isOk()) {
                response.getMessages().forEach(message -> {
                    log.info("Message: {}", message.getText());
                    log.info("Timestamp: {}", message.getTs());
                    log.info("User: {}", message.getUser());
                    log.info("------------------------");
                });
            } else {
                log.info("Error: {}", response.getError());
            }

            return response;
        } catch (SlackApiException e) {
            //TODO:slackApiException 처리
        } catch (Exception e) {
            //TODO: Exception 처리
        }

        return null;
    }

    /**
     * 슬랙 컨텐츠 가져오기 요청값 세팅
     * @param channelId
     * @param ts
     * @return
     */
    private ConversationsHistoryRequest setRequest(String channelId, String ts) {
        // conversations.history API 요청 생성
        ConversationsHistoryRequest request = ConversationsHistoryRequest.builder()
                .token(PrivateDataInfo.SLACK_TOKEN) //TODO : 환경변수처리 필요. 우선 private클래스로 관리후 gitignore
                .channel(channelId)
                .latest(ts)  // 타임스탬프를 기준으로 메시지 가져오기
                .inclusive(true)  // 해당 타임스탬프의 메시지도 포함
                .limit(1)  // 하나의 메시지만 가져오기
                .build();
        return request;
    }


}
