package com.shu.tony.PlayTogether.service;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        geoCriteria.setGeoLocation(geoLocation);
        geoCriteria.setPage(0);
        geoCriteria.setSize(10);
        geoCriteria.setActivityType("basketball");
        PageImpl<ActivityVo> nearest = activityService.findNearest(geoCriteria);
        System.out.println(nearest.getTotalPages());
    }
}
