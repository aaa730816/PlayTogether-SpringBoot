package com.shu.tony.PlayTogether.nonentity.message;

import com.shu.tony.PlayTogether.nonentity.common.PageCriteria;
import lombok.Data;

@Data
public class MessageCriteria extends PageCriteria {
    private String eventId;
    private String type;
}
