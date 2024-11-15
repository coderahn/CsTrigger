package com.myapp.CsTrigger.dto;

import lombok.Data;

@Data
public class Item {
    private String type;
    private String channel;
    private String ts; // timestamp
}
