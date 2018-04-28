package com.shu.tony.PlayTogether.service.activity;

import ch.hsr.geohash.GeoHash;
import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.entity.GeoLocation;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.activity.JoinActivityCriteria;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.repository.ActivityRepository;
import com.shu.tony.PlayTogether.repository.ActivityRepositoryImpl;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.spatial4j.core.io.GeohashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
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

    @Transactional
    public ResultBase updateActivity(ActivityVo activityVo) {
        ResultBase result = new ResultBase();
        try {
            Activity activity;
            if (StringUtils.isEmpty(activityVo.getId())) {
                activity = new Activity();
                translateVoToEntity(activity, activityVo);
                User user = userRepository.findById(Long.valueOf(activityVo.getCreator())).orElse(null);
                if (user != null) {
                    activity.getParticipant().add(user);
                }
            } else {
                activity = activityRepository.findById(Long.valueOf(activityVo.getId())).orElse(null);
                if (activityVo.getNumOfPeople() < (activity.getParticipant().size())) {
                    result.setMessage("已经有"+(activity.getParticipant().size())+"人参与此活动");
                    result.setSuccess(false);
                } else {
                    translateVoToEntity(activity, activityVo);
                }
            }
            if (activity != null) {
                activityRepository.save(activity);
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

    //5km以内
    @Transactional
    public List<Activity> findAround(GeoLocation geoLocation) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(Double.valueOf(geoLocation.getLatitude()), Double.valueOf(geoLocation.getLongitude()), 5);
        List<String> geoCodes = Arrays.stream(geoHash.getAdjacent()).map(x -> x.toBase32()).collect(Collectors.toList());
        geoCodes.add(geoHash.toBase32());
        return activityRepository.findByLocationGeoCodeIn(geoCodes);
    }

    @Transactional(readOnly = true)
    public PageImpl<ActivityVo> findNearest(ActivityCriteria activityCriteria) {
        return activityRepositoryImpl.getNearest(activityCriteria);
    }

    @Transactional(readOnly = true)
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
    public ResultBase join(JoinActivityCriteria criteria) {
        Activity activity = activityRepository.findById(Long.valueOf(criteria.getActivityId())).orElse(null);
        User user = userRepository.findById(Long.valueOf(criteria.getUserId())).orElse(null);
        ResultBase result = new ResultBase();
        if (activity != null && user != null) {
            if (activity.getParticipant().size() >= activity.getNumOfPeople()) {
                result.setSuccess(false);
                result.setMessage("人数已满");
            } else {
                activity.getParticipant().add(user);
                result.setSuccess(true);
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
        activityVo.setNumOfPeople(activity.getNumOfPeople()-activity.getParticipant().size());
        activityVo.setNeedBringEquipment(activity.getNeedBringEquipment());
        activityVo.setId(activity.getId());
        activityVo.setParticipant(activity.getParticipant().stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList()));
        activityVo.setCreator(activity.getCreator());
        activityVo.setGame(activity.getGame());
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
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMessage("该活动已不存在");
        }
        return result;
    }
}
