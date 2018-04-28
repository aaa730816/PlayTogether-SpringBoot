package com.shu.tony.PlayTogether.nonentity.equipment;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import lombok.Data;

@Data
public class EquipmentVo {
    private String id;
    private String creator;
    private int num;
    private String type;
    private String otherType;
    private GeoLocation location;
    private String title;
    private String description;
    private String tenant;
    private Double cost;
    private String unit;
    private Double guarantee;
}
