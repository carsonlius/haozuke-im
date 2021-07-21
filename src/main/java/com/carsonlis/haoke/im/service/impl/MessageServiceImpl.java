package com.carsonlis.haoke.im.service.impl;

import com.carsonlis.haoke.im.pojo.Message;
import com.carsonlis.haoke.im.service.MessageService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Message> findListByFromAndTo(Long fromId, Long toId, Integer page, Integer rows) {
        Criteria fromCriteria = Criteria.where("from.id").is(fromId).and("to.id").is(toId);
        Criteria toCriteria = Criteria.where("from.id").is(toId).and("to.id").is(fromId);
        Criteria criteria = (new Criteria()).orOperator(fromCriteria, toCriteria);

        Sort sort = Sort.by(Sort.Direction.ASC, "send_date");
        PageRequest pageRequest = PageRequest.of(page-1, rows, sort);
        Query query = new Query(criteria).with(pageRequest);
        System.out.println(query);

        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public Message findMessageById(String id) {

        return mongoTemplate.findById(new ObjectId(id), Message.class);
    }

    @Override
    public UpdateResult updateMessageState(ObjectId objectId, Integer status) {
        Criteria criteria = Criteria.where("id").is(objectId);
        Query query = Query.query(criteria);
        Update update = Update.update("status", status);
        if (status == 1) {
            update.set("send_date", new Date());
        } else {
            update.set("read_date", new Date());
        }

        return mongoTemplate.updateFirst(query, update, Message.class);
    }

    @Override
    public Message saveMessage(Message message) {
        message.setId(ObjectId.get());
        message.setSendDate(new Date());
        message.setStatus(1);
        return mongoTemplate.save(message);
    }

    @Override
    public DeleteResult deleteMessage(String id) {
        Criteria criteria = Criteria.where("id").is(new ObjectId(id));
        Query query = Query.query(criteria);
        return mongoTemplate.remove(query,Message.class);
    }
}
