package com.myapp.CsTrigger.service;

import com.myapp.CsTrigger.dto.SlackRequestDto;
import org.springframework.stereotype.Service;

@Service
public class EmojiTriggerService {
    public String emojiTriggerProcess(SlackRequestDto slackRequestDto) {
        //dto에서 전송된 이모지 값 꺼내기(event.item.type) => enum처리
        //이모지 값에 따른 처리
            //white_mark?는 cs데이터 처리
            //event.item.channel, event.item.ts를 받아서 다시 api호출해야 message를 받을 수 있음


        return null;
    }
}
