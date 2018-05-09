package com.shu.tony.PlayTogether.utils;

import com.shu.tony.PlayTogether.entity.GeoLocation;

import static java.lang.Math.*;

public class LocationUtil {
    public static Double computeDistance(GeoLocation fromGeoLocation, GeoLocation toLocation) {
        return Double.valueOf(round(6378.138 * 2 * asin(sqrt(pow(sin((Double.valueOf(fromGeoLocation.getLatitude()) * PI / 180 - Double.valueOf(toLocation.getLatitude()) * PI / 180) / 2), 2)
                + cos(Double.valueOf(fromGeoLocation.getLatitude()) * PI / 180) * cos(Double.valueOf(toLocation.getLatitude()) * PI / 180)
                * pow(sin((Double.valueOf(fromGeoLocation.getLongitude()) * PI / 180 - Double.valueOf(toLocation.getLongitude()) * PI / 180) / 2), 2))) * 1000));
    }
}
