package com.myapp.CsTrigger.dto;

import lombok.Data;

@Data
public class Authorization {
    private String enterpriseId;  // enterprise_id -> enterpriseId
    private String teamId;  // team_id -> teamId
    private String userId;  // user_id -> userId
    private Boolean isBot;  // is_bot -> isBot
    private Boolean isEnterpriseInstall;
}
