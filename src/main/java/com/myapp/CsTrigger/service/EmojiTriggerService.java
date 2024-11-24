package com.myapp.CsTrigger.service;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import com.myapp.CsTrigger.util.PrivateDataInfo;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.User;
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
    public User getUserInfo(SlackRequestDto slackRequestDto) throws SlackApiException, IOException {
        String userId = slackRequestDto.getEvent().getUser();

        // Slack 인스턴스 생성
        Slack slack = Slack.getInstance();

        // 유저 정보 요청
        UsersInfoRequest request = UsersInfoRequest.builder()
                .token(PrivateDataInfo.SLACK_TOKEN)
                .user(userId)
                .build();

        // 유저 정보 가져오기(slack app에서 users_read OAuth권한을 추가해줘야 한다.)
        UsersInfoResponse response = slack.methods().usersInfo(request);

        //API호출 성공시 처리자 정보 가져온 후 저장
        if (response.isOk()) {
            User user = response.getUser();
            log.info("처리자 name: " + user.getName());
            log.info("처리자 realName: " + user.getRealName());

            //TODO: DB 저장

            return user;
        }

        return null;
    }


    /**
     * CS 메시지 가져오기
     * @param slackRequestDto
     * @return
     */
    public ConversationsHistoryResponse getCsMessage(SlackRequestDto slackRequestDto) {
        String reaction = slackRequestDto.getEvent().getReaction();

        if (reaction != null) {
            if ("white_check_mark".equals(reaction)) {
                String channelId = slackRequestDto.getEvent().getItem().getChannel();
                String ts = slackRequestDto.getEvent().getItem().getTs();

                return this.getContents(channelId, ts);
            } else {
                //TODO: 기타 이모지
            }
        }

        return null;
    }

    /**
     * CS 메시지 가져오기 > 감지된 이모지 추가된 메시지 가져오기
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
                response.setError("responseError");
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

    /**
     * user정보와 cs메시지 정보를 DB에 저장한다.(TODO wiki 작성은 api 관련 구조를 알아야 어떻게 처리할지 알듯)
     * @param userInfo
     * @param csMessageResponse
     */
    public void writeCsMessage(User userInfo, ConversationsHistoryResponse csMessageResponse) {
        //todo: DB저장
    }
}
