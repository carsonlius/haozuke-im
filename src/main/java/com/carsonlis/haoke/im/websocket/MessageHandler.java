package com.carsonlis.haoke.im.websocket;

import com.carsonlis.haoke.im.pojo.Message;
import com.carsonlis.haoke.im.pojo.User;
import com.carsonlis.haoke.im.pojo.UserData;
import com.carsonlis.haoke.im.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageHandler extends TextWebSocketHandler {
    @Autowired
    private MessageService messageService;

    private static  final  ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<Long, WebSocketSession> SESSIONS = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 发送消息模块
        Long uid = (Long) session.getAttributes().get("uid");

        // 生成消息
        JsonNode jsonNode = MAPPER.readTree(message.getPayload());
        Long toId = jsonNode.get("toId").asLong();
        String msg = jsonNode.get("msg").asText();

        Message message1 = Message.builder()
                .from(UserData.USER_MAP.get(uid))
                .to(UserData.USER_MAP.get(toId))
                .msg(msg)
                .build();

        // 保存消息
        message1 = messageService.saveMessage(message1);

        // 用户在线 则发送消息且标记为已读
        WebSocketSession readManSession = SESSIONS.get(toId);
        if (readManSession != null && readManSession.isOpen()) {
            readManSession.sendMessage(new TextMessage(MAPPER.writeValueAsString(message1)));
            messageService.updateMessageState(message1.getId(), 2);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 链接成功之后
        Long uid = (Long)session.getAttributes().get("uid");
        // session加入map, 每个session对应一个用户
        SESSIONS.put(uid, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 链接关闭之后
        // todo 删除map中的session, 关闭sesson
        if (session.isOpen()) {
            session.close();
        }

        // 查看此时是否可以拿到uid

    }
}
