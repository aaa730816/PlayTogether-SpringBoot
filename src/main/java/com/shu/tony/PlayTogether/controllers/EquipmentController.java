package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.nonentity.common.EventResult;
import com.shu.tony.PlayTogether.nonentity.common.PageResult;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentCriteria;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("request")
@RequestMapping("equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @RequestMapping("save")
    public EventResult save(@RequestBody EquipmentVo equipmentVo) {
        return equipmentService.save(equipmentVo);
    }

    @RequestMapping("get")
    public EquipmentVo get(@RequestParam("equipmentId") String equipmentId) {
        return equipmentService.get(equipmentId);
    }
    @RequestMapping("rent")
    public EventResult rent(@RequestParam("userId") String userId, @RequestParam("equipmentId")String equipmentId) {
        return equipmentService.rent(userId, equipmentId);
    }

    @RequestMapping("quit")
    public ResultBase quit(@RequestParam("equipmentId") String equipmentId) {
        return equipmentService.quit(equipmentId);
    }

    @RequestMapping("cancel")
    public ResultBase cancel(@RequestParam("equipmentId") String equipmentId) {
        return equipmentService.cancel(equipmentId);
    }
    @RequestMapping("search/{requestType}")
    public PageResult<EquipmentVo> findNearest(@PathVariable("requestType") String requestType, @RequestBody EquipmentCriteria equipmentCriteria) {
        PageResult<EquipmentVo> result = new PageResult<>();
        switch (requestType) {
            case "nearest":
                PageImpl<EquipmentVo> nearest = equipmentService.findNearest(equipmentCriteria);
                result = new PageResult<>(nearest.getNumber(), nearest.getSize(), nearest.getTotalPages(), nearest.getContent());
                break;
            case "around":
                PageImpl<EquipmentVo> around = equipmentService.findAround(equipmentCriteria);
                result = new PageResult<>(around.getNumber(), around.getSize(), around.getTotalPages(), around.getContent());
                break;
            case "cheapest":
                PageImpl<EquipmentVo> cheapest = equipmentService.findCheapest(equipmentCriteria);
                result = new PageResult<>(cheapest.getNumber(), cheapest.getSize(), cheapest.getTotalPages(), cheapest.getContent());
                break;
            case "newest":
                PageImpl<EquipmentVo> newest = equipmentService.newest(equipmentCriteria);
                result = new PageResult<>(newest.getNumber(), newest.getSize(), newest.getTotalPages(), newest.getContent());
                break;
            default:
                break;
        }
        return result;
    }
}
