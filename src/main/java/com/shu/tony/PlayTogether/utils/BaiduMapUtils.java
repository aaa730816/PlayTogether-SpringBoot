package com.shu.tony.PlayTogether.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class BaiduMapUtils {
    private final static String PLACE_SEARCH_URL = "http://api.map.baidu.com/place/v2/search?query={query}&region={region}&output={output}&ak={ak}";
    private final static String GEO_LOCATION_SEARCH_URL="http://api.map.baidu.com/geocoder/v2/?output={output}&pois={pois}&ak={ak}&location={latitude},{longitude}";
    private final static String AK="8DXKvoQPtI5TjqG5dP4c0F2ogV4W92HD";
    private final static RestTemplate REST_TEMPLATE = new RestTemplate();

    public static String placeSearch(String query,String region){
        Map<String,String> params = new HashMap<String,String>(){{
            put("query",query);
            put("region",region);
            put("output","json");
            put("ak", AK);
        }};
        ResponseEntity<String> templateForEntity = REST_TEMPLATE.getForEntity(PLACE_SEARCH_URL, String.class, params);
        return templateForEntity.getBody();
    }

    public static String geoLocationSearch(String longitude,String latitude) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("output", "json");
            put("ak", AK);
            put("pois", "0");
            put("latitude", latitude);
            put("longitude", longitude);
        }};
        ResponseEntity<String> templateForEntity = REST_TEMPLATE.getForEntity(GEO_LOCATION_SEARCH_URL, String.class, params);
        return templateForEntity.getBody();
    }
}
