package com.carsonlius.haoke.im.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "itcast", type = "user", shards = 6, replicas = 1, createIndex = true)
public class Member {
    @Id
    private Long id;

    @Field(store = true)
    private String name;

    @Field
    private Integer age;

    @Field
    private String hobby;
}
