package com.shu.tony.PlayTogether.nonentity.message;

import lombok.Data;

@Data
public class MessageVo {
    private Long createTime;
    private String sender;
    private String senderNickName;
    private String eventId;
    private String message;
    private String id;
    private String type;
    private String title;

    public MessageVo(Long createTime, String sender, String eventId, String message, String id, String type, String title) {
        this.createTime = createTime;
        this.sender = sender;
        this.eventId = eventId;
        this.message = message;
        this.id = id;
        this.type = type;
        this.title = title;
    }

    public MessageVo(Long createTime, String sender, String eventId, String message, String id, String type) {
        this.createTime = createTime;
        this.sender = sender;
        this.eventId = eventId;
        this.message = message;
        this.id = id;
        this.type = type;
    }

    public MessageVo() {
    }
}
