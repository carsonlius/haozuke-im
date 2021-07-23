package com.carsonlis.haoke.im.service;

import com.carsonlis.haoke.im.pojo.Message;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;

import java.util.List;

public interface MessageService {
    /**
     * 查询点对点的聊天记录
     * */
    List<Message> findListByFromAndTo(Long fromId, Long toId, Integer page, Integer rows);

    /**
     * 根据ID查询数据
     * */
    Message findMessageById(String id);

    /**
     * 更新消息状态
     * */
    UpdateResult updateMessageState(ObjectId objectId, Integer status);

    /**
     * 保存消息
     * */
    Message saveMessage(Message message);

    /**
     * 根据消息ID删除数据
     * */
    DeleteResult deleteMessage(String id);

    Message getLastMessage(Long fromId, Long toId);
}
