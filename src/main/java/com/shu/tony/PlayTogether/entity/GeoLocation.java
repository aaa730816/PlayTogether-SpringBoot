package com.shu.tony.PlayTogether.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GeoLocation {
    private String longitude;
    private String latitude;
    private String address;
    private String name;
    private String geoCode;

    public GeoLocation(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public GeoLocation() {
    }
}
