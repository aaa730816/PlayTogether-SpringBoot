package com.shu.tony.PlayTogether.nonentity.activity;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.nonentity.common.PageCriteria;
import lombok.Data;

@Data
public class ActivityCriteria extends PageCriteria {
    private GeoLocation geoLocation;
    private String activityType;
}
