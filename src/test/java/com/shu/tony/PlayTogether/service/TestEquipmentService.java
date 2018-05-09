package com.shu.tony.PlayTogether.service;

import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentCriteria;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.service.equipment.EquipmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEquipmentService {
    @Autowired
    private EquipmentService equipmentService;
    @Test
    public void testFindNearest() {
        EquipmentCriteria equipmentCriteria = new EquipmentCriteria();
        equipmentCriteria.setType("basketball");
        GeoLocation location = new GeoLocation();
        location.setLatitude("31.3214");
        location.setLongitude("121.39903");
        equipmentCriteria.setLocation(location);
        equipmentCriteria.setPage(0);
        equipmentCriteria.setSize(10);
        PageImpl<EquipmentVo> nearest = equipmentService.findNearest(equipmentCriteria);
        System.out.println(nearest.getSize());
    }
    @Test
    public void testFindAround(){
        EquipmentCriteria equipmentCriteria = new EquipmentCriteria();
        equipmentCriteria.setType("basketball");
        GeoLocation location = new GeoLocation();
        location.setLatitude("31.3214");
        location.setLongitude("121.39903");
        equipmentCriteria.setLocation(location);
        equipmentCriteria.setPage(0);
        equipmentCriteria.setSize(10);
        PageImpl<EquipmentVo> nearest = equipmentService.findAround(equipmentCriteria);
        System.out.println(nearest.getSize());
    }
}
