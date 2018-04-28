package com.shu.tony.PlayTogether.service.equipment;

import com.shu.tony.PlayTogether.entity.Equipment;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.repository.EquipmentRespository;
import com.spatial4j.core.io.GeohashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRespository equipmentRespository;

    public ResultBase save(EquipmentVo equipmentVo) {
        Equipment equipment;
        ResultBase result = new ResultBase();
        if (StringUtils.isEmpty(equipmentVo.getId())) {
            equipment = new Equipment();
        } else {
            equipment = equipmentRespository.findById(Long.valueOf(equipmentVo.getId())).get();
        }
        if (equipment != null) {
            translateVoToEntity(equipmentVo,equipment);
            equipmentRespository.save(equipment);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("器材不存在");
        }
        return result;
    }

    public void translateVoToEntity(EquipmentVo equipmentVo, Equipment equipment) {
        equipment.setCost(equipmentVo.getCost());
        equipment.setDescription(equipmentVo.getDescription());
        equipment.setGuarantee(equipmentVo.getGuarantee());
        equipment.setLocation(equipmentVo.getLocation());
        equipment.getLocation().setGeoCode(GeohashUtils.encodeLatLon(
                Double.valueOf(equipment.getLocation().getLatitude()),
                Double.valueOf(equipment.getLocation().getLongitude()), 5));
        equipment.setNum(equipmentVo.getNum());
        equipment.setOtherType(equipmentVo.getOtherType());
        equipment.setType(equipmentVo.getType());
        equipment.setTitle(equipmentVo.getTitle());
        equipment.setUnit(equipmentVo.getUnit());
        equipment.setCreator(equipmentVo.getCreator());
    }

    public void translateEntityToVo(EquipmentVo equipmentVo, Equipment equipment) {
        equipmentVo.setCost(equipment.getCost());
        equipmentVo.setDescription(equipment.getDescription());
        equipmentVo.setGuarantee(equipment.getGuarantee());
        equipmentVo.setLocation(equipment.getLocation());
        equipmentVo.setNum(equipment.getNum());
        equipmentVo.setOtherType(equipment.getOtherType());
        equipmentVo.setType(equipment.getType());
        equipmentVo.setTitle(equipment.getTitle());
        equipmentVo.setUnit(equipment.getUnit());
        equipmentVo.setCreator(equipment.getCreator());
        equipmentVo.setTenant(String.valueOf(equipment.getTenant().getId()));
        equipmentVo.setId(String.valueOf(equipment.getId()));
    }
}
