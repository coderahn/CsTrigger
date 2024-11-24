package com.myapp.CsTrigger.util;

import com.myapp.CsTrigger.dto.SlackRequestDto;

/**
 * 슬랙 이모지 이벤트 발생 후 들어온 요청 DTO의 validation 처리 유틸
 */
public class RequestValidator {
    public static void validate(SlackRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SlackRequestDto is null");
        }

        if (dto.getEvent() == null) {
            throw new IllegalArgumentException("SlackRequestDto event is null");
        }

        if (dto.getEvent().getItem() == null) {
            throw new IllegalArgumentException("SlackRequestDto event item is null");
        }
    }
}
