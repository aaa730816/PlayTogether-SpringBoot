package com.shu.tony.PlayTogether.service.user;

import com.shu.tony.PlayTogether.entity.Activity;
import com.shu.tony.PlayTogether.entity.Equipment;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.common.EventType;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.nonentity.user.*;
import com.shu.tony.PlayTogether.repository.ActivityRepository;
import com.shu.tony.PlayTogether.repository.EquipmentRespository;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import com.shu.tony.PlayTogether.service.equipment.EquipmentService;
import com.shu.tony.PlayTogether.utils.SHAEncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EquipmentRespository equipmentRespository;

    @Transactional
    public UserResult updateUser(UserVo vo) {
        UserResult result = new UserResult();
        try {
            User user = new User();
            user.setUsername(vo.getUserName());
            user.setPassword(SHAEncodeUtil.shaEncode(vo.getPassWord()));
            user.setNickName(vo.getUserName());
            user.setLoginType(vo.getLoginType());
            userRepository.save(user);
            result.setUserOid(user.getId());
            result.setUsername(user.getUsername());
            result.setNickName(user.getNickName());
            result.setSuccess(true);
        } catch (Exception e) {
            log.info(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @Transactional
    public UserResult login(UserVo vo) {
        UserResult result = new UserResult();
        try {
            if (vo.getLoginType().equals(LoginType.QQ.getType())) {
                User user = userRepository.findByUsername(vo.getUserName());
                if (user == null) {
                    user = new User();
                    user.setUsername(vo.getUserName());
                    user.setLoginType(vo.getLoginType());
                    userRepository.save(user);
                    user.setNickName("QQ用户_" + user.getId());
                }
                result.setUsername(user.getUsername());
                result.setNickName(user.getNickName());
                result.setUserOid(user.getId());
                result.setSuccess(true);
            } else {
                vo.setPassWord(SHAEncodeUtil.shaEncode(vo.getPassWord()));
                User user = userRepository.findByUsername(vo.getUserName());
                if (user != null && user.getPassword().equals(vo.getPassWord())) {
                    result.setUsername(user.getUsername());
                    result.setUserOid(user.getId());
                    result.setNickName(user.getNickName());
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                    result.setMessage("用户名或密码错误");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage());
            result.setSuccess(false);
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @Transactional
    public ResultBase changeNickName(NickNameVo vo) {
        ResultBase result = new ResultBase();
        if (userRepository.findByNickName(vo.getNickName()) == null) {
            Optional<User> user = userRepository.findById(Long.valueOf(vo.getUserOid()));
            user.get().setNickName(vo.getNickName());
            result.setSuccess(true);
            result.setMessage("更改成功");
        } else {
            result.setSuccess(false);
            result.setMessage("昵称已存在");
        }
        return result;
    }

    @Transactional
    public List<ActivityVo> getJoinActivities(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        if (user != null) {
            Set<Activity> participateActivities = user.getParticipateActivities();
            return participateActivities.stream().map(activity -> {
                ActivityVo activityVo = new ActivityVo();
                activityService.translateEntityToVo(activity, activityVo);
                return activityVo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<ActivityVo> getCreateActivities(String userId) {
        return activityRepository.findByCreator(userId).stream().map(activity -> {
            ActivityVo activityVo = new ActivityVo();
            activityService.translateEntityToVo(activity, activityVo);
            return activityVo;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<EquipmentVo> getRentEquipments(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        if (user != null) {
            Set<Equipment> equipments = user.getEquipmentSet();
            return equipments.stream().map(equipment -> {
                EquipmentVo equipmentVo = new EquipmentVo();
                equipmentService.translateEntityToVo(equipmentVo, equipment);
                return equipmentVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public List<EquipmentVo> getCreateEquipments(String userId) {
        return equipmentRespository.findByCreatorEquals(userId).stream().map(equipment -> {
            EquipmentVo equipmentVo = new EquipmentVo();
            equipmentService.translateEntityToVo(equipmentVo, equipment);
            return equipmentVo;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<EventVo> getEvents(String userId) {
        List<EventVo> eventVos = new ArrayList<>();
        eventVos.addAll(getJoinActivities(userId).stream().parallel()
                .map(activityVo -> new EventVo(String.valueOf(activityVo.getId()), activityVo.getTitle(), EventType.ACTIVITY.getName()))
                .collect(Collectors.toList()));
        eventVos.addAll(getRentEquipments(userId).stream().parallel()
                .map(equipmentVo -> new EventVo(equipmentVo.getId(), equipmentVo.getTitle(), EventType.EQUIPMENT.getName()))
                .collect(Collectors.toList()));
        eventVos.addAll(getCreateEquipments(userId).stream().parallel()
                .map(equipmentVo -> new EventVo(equipmentVo.getId(), equipmentVo.getTitle(), EventType.EQUIPMENT.getName()))
                .collect(Collectors.toList()));
        return eventVos;
    }
}
