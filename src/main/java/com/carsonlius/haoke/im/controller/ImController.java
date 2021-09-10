package com.carsonlius.haoke.im.controller;

import com.carsonlius.haoke.im.pojo.Message;
import com.carsonlius.haoke.im.pojo.User;
import com.carsonlius.haoke.im.service.MessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ImController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public List<Message> getMessages(){
        return messageService.findListByFromAndTo(1001L, 1002L, 1, 20);
    }


    public void init(){
        Message message = Message.builder()
                .id(ObjectId.get())
                .msg("你好")
                .sendDate(new Date())
                .status(1)
                .from(new User(1001L, "zhangsan"))
                .to(new User(1002L,"lisi"))
                .build();

        this.messageService.saveMessage(message);
        message = Message.builder()
                .id(ObjectId.get())
                .msg("你也好")
                .sendDate(new Date())
                .status(1)
                .to(new User(1001L, "zhangsan"))
                .from(new User(1002L,"lisi"))
                .build();
        this.messageService.saveMessage(message);


        message = Message.builder()
                .id(ObjectId.get())
                .msg("我在学习开发IM")
                .sendDate(new Date())
                .status(1)
                .from(new User(1001L, "zhangsan"))
                .to(new User(1002L,"lisi"))
                .build();
        this.messageService.saveMessage(message);

        message = Message.builder()
                .id(ObjectId.get())
                .msg("那很好啊！")
                .sendDate(new Date())
                .status(1)
                .to(new User(1001L, "zhangsan"))
                .from(new User(1002L,"lisi"))
                .build();
        this.messageService.saveMessage(message);
        System.out.println("ok");
    }
    public static void main(String[] args) {
        try {
            Message message = new ImController().messageService.findMessageById("60f7f8b66bd7d5d85e29e516");
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
