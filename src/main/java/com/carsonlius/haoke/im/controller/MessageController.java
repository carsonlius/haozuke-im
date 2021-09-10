package com.carsonlius.haoke.im.controller;

import com.carsonlius.haoke.im.pojo.Message;
import com.carsonlius.haoke.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> queryMessageList(@RequestParam("formId") Long fromId,
                                          @RequestParam("toId") Long toId,
                                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
       List<Message> messageList= messageService.findListByFromAndTo(fromId, toId, page, pageSize);
       messageList.forEach(message -> {
           // 变成已读
           if (message.getStatus() == 1 && message.getFrom().getId().equals(toId)) {
               messageService.updateMessageState(message.getId(),2);
           }
       });

       return messageList;
    }
}
