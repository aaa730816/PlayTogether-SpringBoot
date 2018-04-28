package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.activity.JoinActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.common.PageResult;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activity")
@Scope("request")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping("save")
    public ResultBase save(@RequestBody ActivityVo activity) {
        return activityService.updateActivity(activity);
    }

    @RequestMapping("nearest")
    public PageResult<ActivityVo> nearest(@RequestBody ActivityCriteria activityCriteria) {
        PageImpl<ActivityVo> nearest = activityService.findNearest(activityCriteria);
        return new PageResult<>(nearest.getNumber(), nearest.getSize(), nearest.getTotalPages(), nearest.getContent());
    }

    @RequestMapping("join")
    public ResultBase join(@RequestBody JoinActivityCriteria joinActivityCriteria) {
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
