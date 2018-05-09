package com.shu.tony.PlayTogether.controllers;

import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.nonentity.common.EventType;
import com.shu.tony.PlayTogether.nonentity.common.PageResult;
import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import com.shu.tony.PlayTogether.nonentity.message.EventSummary;
import com.shu.tony.PlayTogether.nonentity.message.MessageCriteria;
import com.shu.tony.PlayTogether.nonentity.message.MessageVo;
import com.shu.tony.PlayTogether.service.chat.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping("get")
    public PageResult<MessageVo> getMessages(@RequestBody MessageCriteria criteria) {
        PageImpl<MessageVo> messagePage = messageService.getMessages(criteria);
        return new PageResult<>(messagePage.getNumber(), messagePage.getSize(), messagePage.getTotalPages(), messagePage.getContent());
    }

    @RequestMapping("delete")
    public ResultBase deleteMessages(@RequestParam("eventId") String eventId,@RequestParam("type") String type) {
        return messageService.deleteMessages(eventId, type);
    }

    @RequestMapping("eventSummary")
    public EventSummary getEventSummary(@RequestParam("eventId") String eventId, @RequestParam("type") String type) {
        return messageService.getEventSummary(eventId, type);
    }
}
