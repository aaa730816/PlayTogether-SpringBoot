package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.activity.JoinActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.common.EventResult;
import com.shu.tony.PlayTogether.nonentity.common.PageResult;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("activity")
@Scope("request")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping("save")
    public EventResult save(@RequestBody ActivityVo activity) {
        return activityService.updateActivity(activity);
    }

    @RequestMapping("search/{requestType}")
    public PageResult<ActivityVo> nearest(@PathVariable("requestType") String requestType, @RequestBody ActivityCriteria activityCriteria) {
        PageResult<ActivityVo> result = new PageResult<>();
        switch (requestType) {
            case "nearest":
                PageImpl<ActivityVo> nearest = activityService.findNearest(activityCriteria);
                result = new PageResult<>(nearest.getNumber(), nearest.getSize(), nearest.getTotalPages(), nearest.getContent());
                break;
            case "around":
                PageImpl<ActivityVo> around = activityService.findAround(activityCriteria);
                result = new PageResult<>(around.getNumber(), around.getSize(), around.getTotalPages(), around.getContent());
                break;
            case "cheapest":
                PageImpl<ActivityVo> cheapest = activityService.findCheapest(activityCriteria);
                result = new PageResult<>(cheapest.getNumber(), cheapest.getSize(), cheapest.getTotalPages(), cheapest.getContent());
                break;
            case "newest":
                PageImpl<ActivityVo> newest = activityService.findNewest(activityCriteria);
                result = new PageResult<>(newest.getNumber(), newest.getSize(), newest.getTotalPages(), newest.getContent());
                break;
            default:
                break;
        }
        return result;
    }

    @RequestMapping("join")
    public EventResult join(@RequestBody JoinActivityCriteria joinActivityCriteria) {
        return activityService.join(joinActivityCriteria);
    }

    @RequestMapping("getById")
    public ActivityVo getById(@RequestParam("oid")String oid) {
        return activityService.getById(oid);
    }

    @RequestMapping("quit")
    public ResultBase quit(@RequestParam("activityId")String activityId,@RequestParam("userId")String userId) {
        return activityService.quit(activityId,userId);
    }

    @RequestMapping("cancel")
    public ResultBase cancel(@RequestParam("activityId") String activityId, @RequestParam("userId") String userId) {
        return activityService.cancel(activityId, userId);
    }

}
