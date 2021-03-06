package com.shu.tony.PlayTogether.service.activity;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.JoinActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.common.EventResult;
import com.shu.tony.PlayTogether.nonentity.common.EventType;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.message.EventSummary;
import com.shu.tony.PlayTogether.nonentity.user.EventVo;
import com.shu.tony.PlayTogether.nonentity.user.NickNameVo;
import com.shu.tony.PlayTogether.repository.ActivityRepository;
import com.shu.tony.PlayTogether.repository.ActivityRepositoryImpl;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.chat.MessageService;
import com.shu.tony.PlayTogether.utils.LocationUtil;
import com.shu.tony.PlayTogether.utils.NotificationUtil;
import com.spatial4j.core.io.GeohashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityRepositoryImpl activityRepositoryImpl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;

    @Transactional
    public EventResult updateActivity(ActivityVo activityVo) {
        EventResult result = new EventResult();
        try {
            Activity activity;
            if (StringUtils.isEmpty(activityVo.getId())) {
                activity = new Activity();
                activityRepository.save(activity);
                translateVoToEntity(activity, activityVo);
                EventVo eventVo = new EventVo();
                eventVo.setId(String.valueOf(activity.getId()));
                eventVo.setTitle(activity.getTitle());
                eventVo.setType(EventType.ACTIVITY.getName());
                result.setEvent(eventVo);
                User user = userRepository.findById(Long.valueOf(activityVo.getCreator())).orElse(null);
                if (user != null) {
                    activity.getParticipant().add(user);
                }
            } else {
                activity = activityRepository.findById(Long.valueOf(activityVo.getId())).orElse(null);
                if (activityVo.getNumOfPeople() < (activity.getParticipant().size())) {
                    result.setMessage("已经有" + (activity.getParticipant().size()) + "人参与此活动");
                    result.setSuccess(false);
                } else {
                    translateVoToEntity(activity, activityVo);
                }
            }
            if (activity != null) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setMessage("此活动不存在");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @Transactional
    public EventSummary getActivityEventSummary(String activityId) {
        Activity activity = activityRepository.findById(Long.valueOf(activityId)).get();
        EventSummary eventSummary = new EventSummary();
        if (activity != null) {
            ActivityVo activityVo = new ActivityVo();
            translateEntityToVo(activity, activityVo);
            eventSummary.setActivity(activityVo);
            eventSummary.getMember().addAll(activity.getParticipant().stream().map(user -> new NickNameVo(String.valueOf(user.getId()), user.getNickName())).collect(Collectors.toList()));
        }
        return eventSummary;
    }

    //5km以内
    @Transactional
    public PageImpl<ActivityVo> findAround(ActivityCriteria activityCriteria) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(Double.valueOf(activityCriteria.getLocation().getLatitude()), Double.valueOf(activityCriteria.getLocation().getLongitude()), 5);
        List<String> geoCodes = Arrays.stream(geoHash.getAdjacent()).parallel().map(x -> x.toBase32()).collect(Collectors.toList());
        geoCodes.add(geoHash.toBase32());
        Page<Activity> activityPage = activityRepository.findByTypeEqualsAndLocationGeoCodeIn(activityCriteria.getType(),
                geoCodes,
                new PageRequest(activityCriteria.getPage(), activityCriteria.getSize(), new Sort(Sort.Direction.ASC, "title"))
        );
        List<ActivityVo> activityVos = translateEntitysToVos(activityCriteria, activityPage);
        return new PageImpl<>(activityVos, new PageRequest(activityCriteria.getPage(), activityCriteria.getSize()), activityPage.getTotalElements());
    }

    @Transactional
    public PageImpl<ActivityVo> findNearest(ActivityCriteria activityCriteria) {
        return activityRepositoryImpl.getNearest(activityCriteria);
    }

    @Transactional
    public PageImpl<ActivityVo> findNewest(ActivityCriteria activityCriteria) {
        Page<Activity> activityPage = activityRepository.findAllByType(activityCriteria.getType(), new PageRequest(activityCriteria.getPage(), activityCriteria.getSize(), new Sort(Sort.Direction.DESC, "createTime")));
        List<ActivityVo> activityVos = translateEntitysToVos(activityCriteria, activityPage);
        return new PageImpl<>(activityVos, new PageRequest(activityCriteria.getPage(), activityCriteria.getSize()), activityPage.getTotalElements());
    }

    @Transactional
    public PageImpl<ActivityVo> findCheapest(ActivityCriteria activityCriteria) {
        Page<Activity> activityPage = activityRepository.findAllByType(activityCriteria.getType(), new PageRequest(activityCriteria.getPage(), activityCriteria.getSize(), new Sort(Sort.Direction.ASC, "cost")));
        List<ActivityVo> activityVos = translateEntitysToVos(activityCriteria, activityPage);
        return new PageImpl<>(activityVos, new PageRequest(activityCriteria.getPage(), activityCriteria.getSize()), activityPage.getTotalElements());
    }

    public List<ActivityVo> translateEntitysToVos(ActivityCriteria activityCriteria, Page<Activity> activityPage) {
        return activityPage.getContent().stream().map(activity -> {
            ActivityVo activityVo = new ActivityVo();
            translateEntityToVo(activity, activityVo);
            activityVo.setDistance(LocationUtil.computeDistance(activityCriteria.getLocation(), activity.getLocation()));
            return activityVo;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ActivityVo getById(String oid) {
        Optional<Activity> activityOptional = activityRepository.findById(Long.valueOf(oid));
        Activity activity = activityOptional.orElse(null);
        ActivityVo result = new ActivityVo();
        if (activity != null) {
            translateEntityToVo(activity, result);
        }
        return result;
    }

    @Transactional
    public EventResult join(JoinActivityCriteria criteria) {
        Activity activity = activityRepository.findById(Long.valueOf(criteria.getActivityId())).orElse(null);
        User user = userRepository.findById(Long.valueOf(criteria.getUserId())).orElse(null);
        EventResult result = new EventResult();
        if (activity != null && user != null) {
            if (activity.getParticipant().size() >= activity.getNumOfPeople()) {
                result.setSuccess(false);
                result.setMessage("人数已满");
            } else {
                activity.getParticipant().add(user);
                EventVo eventVo = new EventVo();
                eventVo.setId(String.valueOf(activity.getId()));
                eventVo.setTitle(activity.getTitle());
                eventVo.setType(EventType.ACTIVITY.getName());
                result.setEvent(eventVo);
                result.setSuccess(true);
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(user.getNickName()).append("加入了活动[").append(activity.getTitle()).append("]");
                Message message = new Message(messageBuilder.toString(), "system", String.valueOf(activity.getId()), new Date().getTime(), EventType.ACTIVITY.getName());
                NotificationUtil.SendNotification(JSONObject.toJSONString(message),Arrays.asList(activity.getCreator()));
            }
        } else {
            result.setSuccess(false);
            result.setMessage(activity == null ? (user == null ? "找不到活动和用户" : "找不到活动") : "找不到用户");
        }
        return result;
    }

    public void translateVoToEntity(Activity activity, ActivityVo activityVo) {
        activity.setLocation(activityVo.getLocation());
        activity.setType(activityVo.getType());
        activity.setTitle(activityVo.getTitle());
        activity.setStartTime(activityVo.getStartTime());
        activity.setDescription(activityVo.getDescription());
        activity.setCost(activityVo.getCost());
        activity.setNumOfPeople(activityVo.getNumOfPeople());
        activity.setNeedBringEquipment(activityVo.getNeedBringEquipment());
        String geoCode = GeohashUtils.encodeLatLon(Double.valueOf(activity.getLocation().getLatitude()), Double.valueOf(activity.getLocation().getLongitude()), 5);
        activity.getLocation().setGeoCode(geoCode);
        activity.setCreator(activityVo.getCreator());
        activity.setGame(activityVo.getGame());
    }

    public void translateEntityToVo(Activity activity, ActivityVo activityVo) {
        activityVo.setLocation(activity.getLocation());
        activityVo.setType(activity.getType());
        activityVo.setTitle(activity.getTitle());
        activityVo.setStartTime(activity.getStartTime());
        activityVo.setDescription(activity.getDescription());
        activityVo.setCost(activity.getCost());
        activityVo.setNumOfPeople(activity.getNumOfPeople() - activity.getParticipant().size());
        activityVo.setNeedBringEquipment(activity.getNeedBringEquipment());
        activityVo.setId(activity.getId());
        activityVo.setParticipant(activity.getParticipant().stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList()));
        activityVo.setCreator(activity.getCreator());
        activityVo.setGame(activity.getGame());
        activityVo.setAddress(activity.getLocation().getAddress());
    }

    @Transactional
    public ResultBase quit(String activityId, String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        Activity activity = activityRepository.findById(Long.valueOf(activityId)).get();
        ResultBase result = new ResultBase();
        if (activity != null) {
            user.getParticipateActivities().remove(activity);
            activity.getParticipant().remove(user);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("该活动已不存在");
        }
        return result;
    }

    @Transactional
    public ResultBase cancel(String activityId, String userId) {
        Activity activity = activityRepository.findById(Long.valueOf(activityId)).get();
        User user = userRepository.findById(Long.valueOf(userId)).get();
        ResultBase result = new ResultBase();
        if (activity != null) {
            user.getParticipateActivities().remove(activity);
            activityRepository.delete(activity);
            messageService.deleteMessages(activityId, EventType.ACTIVITY.getName());
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("该活动已不存在");
        }
        return result;
    }

}
