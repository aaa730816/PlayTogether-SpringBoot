package com.shu.tony.PlayTogether.nonentity.equipment;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.nonentity.common.PageCriteria;
import lombok.Data;

@Data
public class EquipmentCriteria extends PageCriteria {
    private GeoLocation location;
    private String type;
}
