package com.shu.tony.PlayTogether.websocket;

import com.alibaba.fastjson.JSONObject;
import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.service.chat.MessageService;
import com.shu.tony.PlayTogether.utils.ApplicationContextRegister;
import com.shu.tony.PlayTogether.utils.JpushConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.shu.tony.PlayTogether.utils.NotificationUtil.SendNotification;

@ServerEndpoint("/activityServer/{activityId}/{deviceId}")
@Component
@Slf4j
public class ActivityServer {
    private static ConcurrentHashMap<String, Integer> onlineCountMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Map<String, ActivityServer>> activityServers = new ConcurrentHashMap<>();
    private Session session;

    @OnOpen
    public void onOpen(@PathParam("activityId") String activityId, @PathParam("deviceId") String deviceId, Session session) {
        this.session = session;
        if (activityServers.containsKey(activityId)) {
            activityServers.get(activityId).put(deviceId, this);
        } else {
            ConcurrentHashMap<String, ActivityServer> activityServerList = new ConcurrentHashMap<>();
            activityServerList.put(deviceId, this);
            activityServers.put(activityId, activityServerList);
        }
        if (onlineCountMap.containsKey(activityId)) {
            onlineCountMap.put(activityId, onlineCountMap.get(activityId) + 1);
        } else {
            onlineCountMap.put(activityId, 1);
        }
        System.out.println(activityId + "活动聊天室有新链接"+deviceId+"!共" + onlineCountMap.get(activityId) + "个用户");
    }

    @OnClose
    public void onClose(@PathParam("activityId") String activityId, @PathParam("deviceId") String deviceId) {
        activityServers.get(activityId).remove(deviceId);
        onlineCountMap.put(activityId, onlineCountMap.get(activityId) - 1);
        System.out.println(activityId + "活动聊天室关闭");
    }

    @OnError
    public void onError(@PathParam("activityId") String activityId,
                        Throwable throwable,
                        Session session) {
//        onClose(activityId);
    }

    @OnMessage
    public void onMessage(@PathParam("activityId") String activityId, @PathParam("deviceId") String deviceId, String message, Session session) throws IOException {
        message = saveMessage(message);
        sendMessageToAll(activityId, message, deviceId);
        System.out.println(message);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageToAll(String activityId, String message, String deviceId) throws IOException {
        List<String> alias = new ArrayList<>();
        activityServers.get(activityId).entrySet().stream().forEach(e -> {
            if (!e.getKey().equals(deviceId)) {
                alias.add(e.getKey());
            }
            try {
                e.getValue().sendMessage(message);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        SendNotification(message, alias, JpushConfig.URL.getConfig());
    }


    private String saveMessage(String messageStr) {
        Message message = JSONObject.parseObject(messageStr, Message.class);
        message.setCreateTime(new Date().getTime());
        return JSONObject.toJSONString(ApplicationContextRegister.getApplicationContext().getBean(MessageService.class).saveMessage(message));
    }
}
