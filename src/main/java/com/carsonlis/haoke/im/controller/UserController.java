package com.carsonlis.haoke.im.controller;

import com.carsonlis.haoke.im.pojo.Message;
import com.carsonlis.haoke.im.pojo.UserData;
import com.carsonlis.haoke.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Map<String, Object>> queryUserList(@RequestParam(name="fromId") Long fromId){
        List<Map<String, Object>> response = new ArrayList<>();
        UserData.USER_MAP.entrySet().stream().forEach(longUserEntry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", longUserEntry.getValue().getId());
            map.put("from_user", fromId);
            map.put("info_type", null);
            map.put("to_user", longUserEntry.getKey());
            map.put("username", longUserEntry.getValue().getUsername());
            Message message = messageService.getLastMessage(fromId, longUserEntry.getValue().getId());
            if (message != null) {
                map.put("chat_msg", message.getMsg());
                map.put("chat_time", message.getSendDate().getTime());
            }

            response.add(map);
        });

        return response;
    }
}
