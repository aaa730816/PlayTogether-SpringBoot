package com.shu.tony.PlayTogether.service.chat;

import com.shu.tony.PlayTogether.entity.Equipment;
import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.entity.User;
import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.common.EventType;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.message.EventSummary;
import com.shu.tony.PlayTogether.nonentity.message.MessageCriteria;
import com.shu.tony.PlayTogether.nonentity.message.MessageVo;
import com.shu.tony.PlayTogether.repository.MessageRepository;
import com.shu.tony.PlayTogether.repository.UserRepository;
import com.shu.tony.PlayTogether.service.activity.ActivityService;
import com.shu.tony.PlayTogether.service.equipment.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;

@Slf4j
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EquipmentService equipmentService;

    @Transactional
    public MessageVo saveMessage(Message message) {
        messageRepository.save(message);
        String title = "";
        if (message.getType().equals(EventType.ACTIVITY.getName())) {
            title = activityService.getById(message.getEventId()).getTitle();
        } else if (message.getType().equals(EventType.EQUIPMENT.getName())) {
            title = equipmentService.get(message.getEventId()).getTitle();
        }
        MessageVo messageVo = new MessageVo(message.getCreateTime(), message.getSender(), message.getEventId(), message.getMessage(), String.valueOf(message.getId().getCounter()), message.getType(),title);
        User user = userRepository.findById(Long.valueOf(message.getSender())).get();
        messageVo.setSenderNickName(user == null ? "匿名用户" : user.getNickName());
        return messageVo;
    }


    @Transactional
    public PageImpl<MessageVo> getMessages(MessageCriteria criteria) {
        Page<Message> messagePage = messageRepository.findByEventIdAndTypeEquals(criteria.getEventId(), criteria.getType(),
                new PageRequest(criteria.getPage(), criteria.getSize(),
                        new Sort(Sort.Direction.DESC, "createTime")));
        List<MessageVo> messages = messagePage.getContent().stream().map(message -> {
            MessageVo messageVo = new MessageVo(message.getCreateTime(), message.getSender(), message.getEventId(), message.getMessage(), String.valueOf(message.getId().getCounter()), message.getType());
            User user = userRepository.findById(Long.valueOf(message.getSender())).get();
            messageVo.setSenderNickName(user == null ? "匿名用户" : user.getNickName());
            return messageVo;
        }).sorted(Comparator.comparing(MessageVo::getCreateTime)).collect(Collectors.toList());
        return new PageImpl<MessageVo>(messages, new PageRequest(criteria.getPage(), criteria.getSize()), messagePage.getTotalElements());
    }

    @Transactional
    public ResultBase deleteMessages(String eventId, String type) {
        ResultBase result = new ResultBase();
        try {
            messageRepository.deleteAllByEventIdAndTypeEquals(eventId, type);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @Transactional
    public EventSummary getEventSummary(String eventId, String type) {
        EventSummary eventSummary = new EventSummary();
        switch (type) {
            case "activity":
                return activityService.getActivityEventSummary(eventId);
            case "equipment":
                return equipmentService.getEquipmentEventSummart(eventId);
            default:
                return null;
        }
    }
}
