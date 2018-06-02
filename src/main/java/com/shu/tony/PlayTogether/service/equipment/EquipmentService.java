package com.shu.tony.PlayTogether.service.equipment;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSONObject;
import com.shu.tony.PlayTogether.entity.Equipment;
import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.common.CostUnit;
import com.shu.tony.PlayTogether.nonentity.common.EventResult;
import com.shu.tony.PlayTogether.nonentity.common.EventType;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentCriteria;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.nonentity.message.EventSummary;
import com.shu.tony.PlayTogether.nonentity.user.EventVo;
import com.shu.tony.PlayTogether.nonentity.user.NickNameVo;
import com.shu.tony.PlayTogether.repository.EquipmentRespository;
import com.shu.tony.PlayTogether.repository.EquipmentRespositoryImpl;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.chat.MessageService;
import com.shu.tony.PlayTogether.utils.LocationUtil;
import com.shu.tony.PlayTogether.utils.NotificationUtil;
import com.spatial4j.core.io.GeohashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRespository equipmentRespository;
    @Autowired
    private EquipmentRespositoryImpl equipmentRespositoryImpl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;

    private final Map<String,CostUnit> costUnitMap = new HashMap<String,CostUnit>(){{
        put("day",CostUnit.DAY);
        put("minute",CostUnit.MINUTE);
        put("hour", CostUnit.HOUR);
    }};


    @Transactional
    public EventSummary getEquipmentEventSummart(String equipmentId) {
        Equipment equipment = equipmentRespository.findById(Long.valueOf(equipmentId)).get();
        EventSummary eventSummary = new EventSummary();
        if (equipment != null) {
            EquipmentVo equipmentVo = new EquipmentVo();
            translateEntityToVo(equipmentVo, equipment);
            eventSummary.setEquipment(equipmentVo);
            eventSummary.getMember().add(new NickNameVo(equipment.getCreator(),userRepository.findById(Long.valueOf(equipment.getCreator())).get().getNickName()));
            if (equipment.getTenant() != null) {
                eventSummary.getMember().add(new NickNameVo(String.valueOf(equipment.getTenant().getId()),equipment.getTenant().getNickName()));
            }
        }
        return eventSummary;
    }
    @Transactional
    public EventResult save(EquipmentVo equipmentVo) {
        Equipment equipment;
        EventResult result = new EventResult();
        if (StringUtils.isEmpty(equipmentVo.getId())) {
            equipment = new Equipment();
            equipmentRespository.save(equipment);
            EventVo eventVo = new EventVo();
            eventVo.setId(String.valueOf(equipment.getId()));
            eventVo.setTitle(equipmentVo.getTitle());
            eventVo.setType(EventType.EQUIPMENT.getName());
            result.setEvent(eventVo);
        } else {
            equipment = equipmentRespository.findById(Long.valueOf(equipmentVo.getId())).get();
        }
        if (equipment != null) {
            translateVoToEntity(equipmentVo, equipment);
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
        equipment.setCostForSort(equipmentVo.getCost() / costUnitMap.get(equipmentVo.getUnit()).getMul());
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
        equipmentVo.setTenant(StringUtils.isEmpty(equipment.getTenant()) ? null : String.valueOf(equipment.getTenant().getId()));
        equipmentVo.setId(String.valueOf(equipment.getId()));
    }

    @Transactional
    public PageImpl<EquipmentVo> findNearest(EquipmentCriteria equipmentCriteria) {
        return equipmentRespositoryImpl.findNearest(equipmentCriteria);
    }

    @Transactional
    public PageImpl<EquipmentVo> findAround(EquipmentCriteria equipmentCriteria) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(Double.valueOf(equipmentCriteria.getLocation().getLatitude()), Double.valueOf(equipmentCriteria.getLocation().getLongitude()), 5);
        List<String> geoCodes = Arrays.stream(geoHash.getAdjacent()).parallel().map(x -> x.toBase32()).collect(Collectors.toList());
        geoCodes.add(geoHash.toBase32());
        Page<Equipment> equipmentPage = equipmentRespository.findByLocationGeoCodeInAndTypeEqualsAndTenantIsNull(geoCodes,
                equipmentCriteria.getType(),
                new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize(),new Sort(Sort.Direction.ASC, "title"))
                );
        List<EquipmentVo> equipmentVos = equipmentPage.getContent().stream().parallel().map(equipment -> {
            EquipmentVo equipmentVo = new EquipmentVo();
            translateEntityToVo(equipmentVo, equipment);
            equipmentVo.setDistance(LocationUtil.computeDistance(equipmentCriteria.getLocation(), equipment.getLocation()));
            return equipmentVo;
        }).collect(Collectors.toList());
        return new PageImpl<EquipmentVo>(equipmentVos, new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize()), equipmentPage.getTotalElements());
    }

    @Transactional
    public PageImpl<EquipmentVo> findCheapest(EquipmentCriteria equipmentCriteria) {
        Page<Equipment> equipmentPage = equipmentRespository.findAllByTenantIsNull(
                new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize(),new Sort(Sort.Direction.ASC, "costForSort")));
        List<EquipmentVo> equipmentVos = equipmentPage.getContent().stream().map(equipment -> {
            EquipmentVo equipmentVo = new EquipmentVo();
            translateEntityToVo(equipmentVo, equipment);
            equipmentVo.setDistance(LocationUtil.computeDistance(equipmentCriteria.getLocation(), equipment.getLocation()));
            return equipmentVo;
        }).collect(Collectors.toList());
        return new PageImpl<>(equipmentVos, new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize()), equipmentPage.getTotalElements());
    }

    @Transactional
    public PageImpl<EquipmentVo> newest(EquipmentCriteria equipmentCriteria) {
        Page<Equipment> equipmentPage = equipmentRespository.findAllByTenantIsNull(
                new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize(),new Sort(Sort.Direction.DESC, "createTime")));
        List<EquipmentVo> equipmentVos = equipmentPage.getContent().stream().map(equipment -> {
            EquipmentVo equipmentVo = new EquipmentVo();
            translateEntityToVo(equipmentVo, equipment);
            equipmentVo.setDistance(LocationUtil.computeDistance(equipmentCriteria.getLocation(), equipment.getLocation()));
            return equipmentVo;
        }).collect(Collectors.toList());
        return new PageImpl<>(equipmentVos, new PageRequest(equipmentCriteria.getPage(), equipmentCriteria.getSize()), equipmentPage.getTotalElements());
    }
    @Transactional
    public EventResult rent(String userId, String equipmentId) {
        EventResult result = new EventResult();
        User user = userRepository.findById(Long.valueOf(userId)).get();
        if (user != null) {
            Equipment equipment = equipmentRespository.findById(Long.valueOf(equipmentId)).get();
            if (equipment != null) {
                equipment.setTenant(user);
                EventVo eventVo = new EventVo();
                eventVo.setId(String.valueOf(equipment.getId()));
                eventVo.setTitle(equipment.getTitle());
                eventVo.setType(EventType.EQUIPMENT.getName());
                result.setEvent(eventVo);
                result.setSuccess(true);
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(user.getNickName()).append("租借了你的器材[").append(equipment.getTitle()).append("]");
                Message message = new Message(messageBuilder.toString(), "system", String.valueOf(equipment.getId()), new Date().getTime(), EventType.EQUIPMENT.getName());
                NotificationUtil.SendNotification(JSONObject.toJSONString(message), Arrays.asList(equipment.getCreator()));
            } else {
                result.setMessage("找不到器材");
                result.setSuccess(false);
            }
        } else {
            result.setMessage("找不到用户");
            result.setSuccess(false);
        }
        return result;
    }

    @Transactional
    public EquipmentVo get(String equipmentId) {
        Equipment equipment = equipmentRespository.findById(Long.valueOf(equipmentId)).get();
        EquipmentVo result = new EquipmentVo();
        if (equipment != null) {
            translateEntityToVo(result, equipment);
        }
        return result;
    }

    @Transactional
    public ResultBase cancel(String equipmentId) {
        Equipment equipment = equipmentRespository.findById(Long.valueOf(equipmentId)).get();
        ResultBase result = new ResultBase();
        if (equipment != null) {
            equipmentRespository.delete(equipment);
            messageService.deleteMessages(equipmentId, EventType.EQUIPMENT.getName());
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("找不到器材");
        }
        return result;
    }

    @Transactional
    public ResultBase quit(String equipmentId) {
        Equipment equipment = equipmentRespository.findById(Long.valueOf(equipmentId)).get();
        ResultBase result = new ResultBase();
        if (equipment != null) {
            equipment.setTenant(null);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("找不到器材");
        }
        return result;
    }
}
