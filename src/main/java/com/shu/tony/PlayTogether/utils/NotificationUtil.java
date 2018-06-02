package com.shu.tony.PlayTogether.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.shu.tony.PlayTogether.nonentity.message.MessageVo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class NotificationUtil {

    public static void SendNotification(String message, List<String> alias) {
        MessageVo messageVo = JSONObject.parseObject(message, MessageVo.class);
        JPushClient jPushClient = new JPushClient(JpushConfig.MASTER_SECRET.getConfig(), JpushConfig.APP_KEY.getConfig(),null, ClientConfig.getInstance());
        PushPayload pushPayload = buildPushObject(alias, messageVo);
        try {
            jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            log.error(pushPayload.toString());
            log.error(e.getMessage());
        } catch (APIRequestException e) {
            log.error(pushPayload.toString());
            log.error(e.getMessage());
        }
    }

    private static PushPayload buildPushObject(List<String> alias,MessageVo messageVo) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.android(messageVo.getMessage(), messageVo.getTitle(), new HashMap<String, String>() {{
                    put("id", messageVo.getEventId());
                    put("type", messageVo.getType());
                    put("title", messageVo.getTitle());
                }}))
                .build();
    }
}
