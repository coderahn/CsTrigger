package com.myapp.CsTrigger.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SlackRequestDto {
    //event description 정보
    private String token;
    private String teamId;  // team_id -> teamId
    private String contextTeamId;  // context_team_id -> contextTeamId
    private String contextEnterpriseId;  // context_enterprise_id -> contextEnterpriseId
    private String apiAppId;  // api_app_id -> apiAppId
    private Event event; // 중첩된 이벤트 객체
    private String type;
    private String eventId;  // event_id -> eventId
    private Long eventTime;  // event_time -> eventTime
    private List<Authorization> authorizations; // 배열
    private boolean isExtSharedChannel;  // is_ext_shared_channel -> isExtSharedChannel
    private String eventContext;  // event_context -> eventContext
    private String challenge;

    //user api 호출 정보
}