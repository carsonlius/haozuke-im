package com.carsonlis.haoke.im.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class MessageHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 建立链接之前
        String path = request.getURI().getPath();
        String[] pathSeparations = path.split("/");
        if (pathSeparations.length != 3) {
            return false;
        }

        if (!StringUtils.isNumeric(pathSeparations[2])) {
            System.out.println("参数中的uid不合法");
            return false;
        }
        Long uid = Long.valueOf(pathSeparations[2]);
        attributes.put("uid", uid);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 建立链接之后
        System.out.println("哈哈 握手之后了");

    }
}
