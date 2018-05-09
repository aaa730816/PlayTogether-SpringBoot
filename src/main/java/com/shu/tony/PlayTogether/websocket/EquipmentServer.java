package com.shu.tony.PlayTogether.websocket;

import com.alibaba.fastjson.JSONObject;
import com.shu.tony.PlayTogether.entity.Message;
import com.shu.tony.PlayTogether.nonentity.message.MessageVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.shu.tony.PlayTogether.utils.NotificationUtil.SendNotification;

@ServerEndpoint("/equipmentServer/{equipmentId}/{deviceId}")
@Component
@Slf4j
public class EquipmentServer {
    private static ConcurrentHashMap<String, Integer> onlineCountMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Map<String, EquipmentServer>> equipmentServers = new ConcurrentHashMap<>();
    private Session session;
    @OnOpen
    public void onOpen(@PathParam("equipmentId") String equipmentId,@PathParam("deviceId") String deviceId, Session session) {
        this.session = session;
        if (equipmentServers.containsKey(equipmentId)) {
            equipmentServers.get(equipmentId).put(deviceId,this);
        } else {
            Map<String,EquipmentServer> equipmentServerList = new ConcurrentHashMap();
            equipmentServerList.put(deviceId,this);
            equipmentServers.put(equipmentId, equipmentServerList);
        }
        if (onlineCountMap.containsKey(equipmentId)) {
            onlineCountMap.put(equipmentId, onlineCountMap.get(equipmentId) + 1);
        } else {
            onlineCountMap.put(equipmentId, 1);
        }
        System.out.println(equipmentId + "器材聊天室有新链接"+deviceId+"!共" + onlineCountMap.get(equipmentId) + "个用户");
    }

    @OnClose
    public void onClose(@PathParam("equipmentId") String equipmentId,@PathParam("deviceId") String deviceId) {
        equipmentServers.get(equipmentId).remove(deviceId);
        onlineCountMap.put(equipmentId, onlineCountMap.get(equipmentId) - 1);
        System.out.println(equipmentId+"器材聊天室关闭");
    }

    @OnMessage
    public void onMessage(@PathParam("equipmentId") String equipmentId,@PathParam("deviceId") String deviceId, String message, Session session) throws IOException {
        message = saveMessage(message);
        sendMessageToAll(equipmentId, message,deviceId);
        System.out.println(message);
    }

    @OnError
    public void onError(@PathParam("equipmentId") String equipmentId,
                        Throwable throwable,
                        Session session) {
//        onClose(equipmentId);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageToAll(String equipmentId, String message,String deviceId) throws IOException {
        List<String> alias = new ArrayList<>();
        equipmentServers.get(equipmentId).entrySet().stream().forEach(e -> {
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
