package com.myapp.CsTrigger.dto;

import lombok.Data;

@Data
public class Event {
    private String type;
    private String user;
    private String reaction;
    private Item item;
    private String itemUser;
    private String eventTs;
}
