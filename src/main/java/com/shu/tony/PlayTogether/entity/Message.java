package com.shu.tony.PlayTogether.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import javax.persistence.Id;

@Data
public class Message {
    @Id
    private ObjectId id;
    private String message;
    private String sender;
    private String eventId;
    private Long createTime;
    private String type;

    public Message(String message, String sender, String eventId, Long createTime,String type) {
        this.message = message;
        this.sender = sender;
        this.eventId = eventId;
        this.createTime = createTime;
        this.type = type;
    }

    public Message() {
    }
}
