package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.utils.BaiduMapUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("location")
@Scope("request")
public class LocationController {
    @RequestMapping("placeSearch")
    public String placeSearch(@RequestParam("query") String query, @RequestParam("region") String region) {
        return BaiduMapUtils.placeSearch(query, region);
    }

    @RequestMapping("geoLocationSearch")
    public String geoLocationSearch(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) {
        return BaiduMapUtils.geoLocationSearch(longitude, latitude);
    }
}
