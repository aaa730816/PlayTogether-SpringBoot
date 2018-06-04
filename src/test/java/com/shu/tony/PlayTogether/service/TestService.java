package com.shu.tony.PlayTogether.service;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import com.shu.tony.PlayTogether.utils.BaiduMapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {
    @Autowired
    private ActivityService activityService;
    @Test
    @Transactional
    @Rollback(value = false)
    public void testActivityService(){
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLongitude("121.39903");
        geoLocation.setLatitude("31.32144");
        ActivityCriteria geoCriteria=new ActivityCriteria();
        geoCriteria.setLocation(geoLocation);
        geoCriteria.setPage(0);
        geoCriteria.setSize(10);
        geoCriteria.setType("basketball");
        PageImpl<ActivityVo> nearest = activityService.findNearest(geoCriteria);
        System.out.println(nearest.getTotalPages());
    }

    @Test
    public void testGeoSearch(){
        System.out.println(BaiduMapUtils.placeSearch("上海大学","289"));
        System.out.println(BaiduMapUtils.geoLocationSearch("121.399","31.3214"));
    }
}
