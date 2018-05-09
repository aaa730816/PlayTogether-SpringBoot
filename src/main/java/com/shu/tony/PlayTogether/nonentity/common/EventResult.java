package com.shu.tony.PlayTogether.nonentity.common;

import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.user.EventVo;
import lombok.Data;

@Data
public class EventResult extends ResultBase {
    private EventVo event;
}
