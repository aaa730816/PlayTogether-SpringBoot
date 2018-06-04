package com.shu.tony.PlayTogether.nonentity.activity;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActivityVo {
    private Long id;
    private String title;
    private Date startTime;
    private GeoLocation location;
    private Double cost;
    private int numOfPeople;
    private Boolean needBringEquipment = false;
    private String description;
    private String type;
    private Double distance;
    private String address;
    private List<String> participant;
    private String creator;
    private String game;
}
