package com.shu.tony.PlayTogether.nonentity.user;

import lombok.Data;

@Data
public class EventVo {
    private String id;
    private String title;
    private String type;
    private int unread=0;

    public EventVo() {
    }

    public EventVo(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
